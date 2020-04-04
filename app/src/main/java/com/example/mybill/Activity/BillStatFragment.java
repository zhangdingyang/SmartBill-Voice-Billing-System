package com.example.mybill.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.Category;
import com.example.mybill.bean.User;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;


public class BillStatFragment extends Fragment {
    private TextView text_statTitle;
    private TextView text_income;
    private TextView text_outcome;
    private TextView text_EngelCoefficient;

    private PieChart pieChart_income;
    private PieChart pieChart_outcome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_stat_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化标题行
        text_statTitle = getActivity().findViewById(R.id.text_statTitle);
        String currentUserName = BmobUser.getCurrentUser().getUsername();
        text_statTitle.setText("尊敬的" + currentUserName + "，您的收入和支出情况已经帮你统计出来啦！");

        //获取总收入支出
        text_income = getActivity().findViewById(R.id.text_income);
        text_outcome = getActivity().findViewById(R.id.text_outcome);
        getTotalIncomeOrOutcome("in");
        getTotalIncomeOrOutcome("out");

        //获取恩格尔系数
        text_EngelCoefficient = getActivity().findViewById(R.id.text_EngelCoefficient);
        getEngelCoefficient();

        //初始化收入饼图

        pieChart_income = getActivity().findViewById(R.id.pieChart_income);
        pieChart_income.setNoDataText("你竟然没有任何收入！");
        pieChart_income.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart_income.setDrawHoleEnabled(true);//饼状图中心圆是否存在
        pieChart_income.setCenterText("我的收入");  //饼状图中间的文字

        //初始化支出饼图
        pieChart_income = getActivity().findViewById(R.id.pieChart_outcome);
        pieChart_income.setNoDataText("你竟然没有任何支出！");
        pieChart_income.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart_income.setDrawHoleEnabled(true);//饼状图中心圆是否存在
        pieChart_income.setCenterText("我的支出");  //饼状图中间的文字
    }

    //获取总收入
    private void getTotalIncomeOrOutcome(final String inOrOut){
        Calendar calendar = Calendar.getInstance();

        BmobQuery<Bill> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        if (inOrOut.equals("in"))
            bmobQuery.addWhereEqualTo("type", "in");
        else if (inOrOut.equals("out"))
            bmobQuery.addWhereEqualTo("type", "out");
        bmobQuery.addWhereGreaterThan("date", new BmobDate(new Date(calendar.get(Calendar.YEAR-1), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))));
        bmobQuery.findObjects(new FindListener<Bill>() {
            @Override
            public void done(List<Bill> list, BmobException e) {
                if (e == null){
                    float sum = 0;
                    for (Bill bill: list){
                        sum = sum + bill.getAmount();
                    }
                    if (inOrOut.equals("in"))
                        text_income.setText("￥" + sum);
                    if (inOrOut.equals("out"))
                        text_outcome.setText("￥" + sum);
                }
                else
                    Toast.makeText(getActivity(),"查询账单出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    //获取恩格尔系数
    private void getEngelCoefficient(){
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        categoryBmobQuery.addWhereEqualTo("name", "消费-饮食");
        categoryBmobQuery.findObjects(new FindListener<Category>() {
            @Override
            public void done(List<Category> list, BmobException e) {
                if (e == null){
                    if (list.size() == 1){
                        String categoryId = list.get(0).getObjectId();
                        getAndSetEngelCoefficient(categoryId);
                    }
                    else
                        Toast.makeText(getActivity(),"无法统计恩格尔系数。你删除或重复新增了“消费-饮食”类别。",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(),"查询账单出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 从数据库获取恩格尔系数
     * @param categoryId 该用户的饮食类别Id
     */
    private void getAndSetEngelCoefficient(String categoryId) {
        Calendar calendar = Calendar.getInstance();
        BmobQuery<Bill> billBmobQuery = new BmobQuery<>();
        billBmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        billBmobQuery.addWhereEqualTo("type", "out");
        billBmobQuery.addWhereEqualTo("categoryId", new Category(categoryId));
        billBmobQuery.addWhereGreaterThan("date", new BmobDate(new Date(calendar.get(Calendar.YEAR-1), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))));
        billBmobQuery.findObjects(new FindListener<Bill>() {
            @Override
            public void done(List<Bill> list, BmobException e) {
                if (e == null){
                    float sum = 0;
                    for (Bill bill: list){
                        sum = sum + bill.getAmount();
                    }
                    float totalOutcome = Float.parseFloat(text_outcome.getText().toString().substring(1));
                    text_EngelCoefficient.setText((sum / totalOutcome * 100) + "%");
                }
                else
                    Toast.makeText(getActivity(),"查询账单出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
