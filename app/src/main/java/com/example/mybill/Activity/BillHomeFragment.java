package com.example.mybill.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.Adapter.BillAdapter;
import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.PaymentMethod;

import java.util.ArrayList;
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
import cn.bmob.v3.listener.UpdateListener;


public class BillHomeFragment extends Fragment {
    ListView listView;

    Switch inOrOutSwitch;

    Spinner spinner_length;

    List<String> lengthData;
    List<Bill> listData;

    BillAdapter billAdapter;
    ArrayAdapter lengthAdapter;

    String inOrOut;
    boolean isToday;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_home_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //默认筛选条件
        inOrOut = "in";
        isToday = false;

        //列表初始化
        initListView();

        //收入支出选择初始化
        inOrOutSwitch = getActivity().findViewById(R.id.switch_in_out);
        inOrOutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    inOrOut = "out";
                else
                    inOrOut = "in";
                initListView();
            }
        });

        //时间选择器初始化
        spinner_length = getActivity().findViewById(R.id.spinner_length);
        lengthData = new ArrayList<String>();
        lengthData.add("近一年");
        lengthData.add("今日");
        lengthAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lengthData);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_length.setAdapter(lengthAdapter);
        spinner_length.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (lengthData.get(position).equals("近一年"))
                    isToday = false;
                else if (lengthData.get(position).equals("今日"))
                    isToday = true;
                initListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"选择时间范围出错",Toast.LENGTH_SHORT).show();
            }
        });

        //单击一行数据的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                BmobQuery<Bill> bmobQuery = new BmobQuery<Bill>();
                bmobQuery.getObject(listData.get(position).getObjectId(), new QueryListener<Bill>() {
                    @Override
                    public void done(Bill bill, BmobException e) {
                        if (e == null){
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("详情")
                                    .setMessage("详细信息：" + bill.getComment())
                                    .show();
                        }
                        else {
                            Toast.makeText(getActivity(),"查询详情失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //长按一行数据的事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("删除")
                        .setMessage("是否删除该项？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Bill bill = new Bill();
                                bill.setObjectId(listData.get(position).getObjectId());
                                bill.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                                            initListView();
                                        }else{
                                            Toast.makeText(getActivity(),"删除失败:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getActivity(), BillMainActivity.class));
                            }
                        })
                        .show();


                return false;
            }
        });

    }

    //列表初始化
    private void initListView() {
        listView = getActivity().findViewById(R.id.billListView);
        listData = new ArrayList<>();
        getBillData(inOrOut, isToday);

    }

    //根据条件查询相应的账本
    private void getBillData(String inOrOut, boolean isToday){
        Calendar calendar = Calendar.getInstance();

        BmobQuery<Bill> bmobQuery = new BmobQuery<Bill>();
        bmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        if (isToday)
            bmobQuery.addWhereEqualTo("date", new BmobDate(new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))));
        else
            bmobQuery.addWhereGreaterThan("date", new BmobDate(new Date(calendar.get(Calendar.YEAR-1), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))));
        if (inOrOut.equals("out"))
            bmobQuery.addWhereEqualTo("type", "out");
        else if (inOrOut.equals("in"))
            bmobQuery.addWhereEqualTo("type", "in");
        bmobQuery.findObjects(new FindListener<Bill>() {
            @Override
            public void done(List<Bill> list, BmobException e) {
                if (e == null){
                    listData.addAll(list);

                    billAdapter = new BillAdapter(getActivity(), R.layout.bill_item, listData);
                    listView.setAdapter(billAdapter);
                }
                else {
                    Toast.makeText(getActivity(),"查询账单出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
