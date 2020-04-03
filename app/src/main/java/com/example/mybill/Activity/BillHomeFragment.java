package com.example.mybill.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.Adapter.BillAdapter;
import com.example.mybill.JsonParser;
import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.Category;
import com.example.mybill.bean.PaymentMethod;
import com.example.mybill.bean.User;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

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
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.iflytek.cloud.VerifierResult.TAG;


public class BillHomeFragment extends Fragment {
    ListView listView;

    Switch inOrOutSwitch;

    Spinner spinner_length;

    List<String> lengthData;
    List<Bill> listData;

    BillAdapter billAdapter;
    ArrayAdapter lengthAdapter;

    ImageButton btn_speak;

    String inOrOut;
    String speakResultText;
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

                            }
                        })
                        .show();


                return false;
            }
        });

        //语音按钮事件
        btn_speak = getActivity().findViewById(R.id.btn_speak);
        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognizerDialog recognizerDialog = new RecognizerDialog(getActivity(), mInitListener);
                    recognizerDialog.setParameter(SpeechConstant.LANGUAGE, "zh-cn");
                    recognizerDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
                    recognizerDialog.setListener(new RecognizerDialogListener() {
                        @Override
                        public void onResult(RecognizerResult recognizerResult, boolean b) {
                            speakResultText = JsonParser.parseIatResult(recognizerResult.getResultString());
                            if (!speakResultText.equals("。") && !speakResultText.equals("")){
                                final Bill newBill = new Bill();
                                if (speakResultText.contains("年") && speakResultText.contains("月") && speakResultText.contains("日")){
                                    try {
                                        int year = Integer.parseInt(speakResultText.substring(speakResultText.indexOf("年")-4, speakResultText.indexOf("年")));
                                        int month = Integer.parseInt(speakResultText.substring(speakResultText.indexOf("月")-2, speakResultText.indexOf("月")));
                                        int day = Integer.parseInt(speakResultText.substring(speakResultText.indexOf("日")-2, speakResultText.indexOf("日")));
                                        newBill.setDate(new BmobDate(new Date(year - 1900, month, day)));
                                    }catch (Exception e){
                                        Toast.makeText(getActivity(),"日期不合法。",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(getActivity(),"缺少信息。你没有说出正确的日期。",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (speakResultText.contains("收入"))
                                    newBill.setType("in");
                                else if (speakResultText.contains("支出"))
                                    newBill.setType("out");
                                else {
                                    Toast.makeText(getActivity(),"缺少信息。你没有说出收入还是支出。",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (speakResultText.contains("使用")){
                                    String paymentMethod = speakResultText.substring(speakResultText.indexOf("使用")+2).split("，")[0];
                                    BmobQuery<PaymentMethod> bmobQuery = new BmobQuery();
                                    bmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
                                    bmobQuery.addWhereEqualTo("name", paymentMethod);
                                    bmobQuery.findObjects(new FindListener<PaymentMethod>() {
                                        @Override
                                        public void done(List<PaymentMethod> list, BmobException e) {
                                            if (list.size() == 1)
                                                newBill.setPaymentMethod(list.get(0));
                                            else
                                                Toast.makeText(getActivity(),"交易方式不存在。",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(getActivity(),"缺少信息。你没有说出交易方式。",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (speakResultText.contains("类别是")){
                                    String category = speakResultText.substring(speakResultText.indexOf("类别是")+3).split("，")[0];
                                    BmobQuery<Category> bmobQuery = new BmobQuery();
                                    bmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
                                    bmobQuery.addWhereEqualTo("name", category);
                                    bmobQuery.findObjects(new FindListener<Category>() {
                                        @Override
                                        public void done(List<Category> list, BmobException e) {
                                            if (list.size() == 1)
                                                newBill.setCategoryId(list.get(0));
                                            else
                                                Toast.makeText(getActivity(),"交易方式不存在。",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(getActivity(),"缺少信息。你没有说出类别。",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (speakResultText.contains("金额是")){
                                    try {
                                        String amount_str = speakResultText.substring(speakResultText.indexOf("金额是")+3).split("元")[0];
                                        int amount_int = Integer.parseInt(amount_str);
                                        newBill.setAmount(amount_int);
                                    }catch (Exception e){
                                        Toast.makeText(getActivity(),"日期不合法。",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(getActivity(),"缺少信息。你没有说出金额。",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (speakResultText.contains("主题是")){
                                    String title = speakResultText.substring(speakResultText.indexOf("主题是")+3).split("。")[0];
                                    newBill.setTitle(title);
                                }
                                else {
                                    Toast.makeText(getActivity(),"缺少信息。你没有说出主题。",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                User user = new User();
                                user.setObjectId(BmobUser.getCurrentUser().getObjectId());
                                newBill.setUserId(user);
                                newBill.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null){
                                            Toast.makeText(getActivity(),"添加账单成功：" + s,Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(getActivity(),"添加账单出错：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(SpeechError speechError) {
                            Toast.makeText(getActivity(),"语音识别出错:" + speechError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    recognizerDialog.show();
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
        bmobQuery.order("-date");
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
                    if (listData.size() == 0)
                        listData.addAll(list);
                    else {
                        listData.clear();
                        listData.addAll(list);
                    }

                    billAdapter = new BillAdapter(getActivity(), R.layout.bill_item, listData);
                    listView.setAdapter(billAdapter);
                }
                else {
                    Toast.makeText(getActivity(),"查询账单出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 初始化语音识别监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(getActivity(),"初始化失败，错误码：" + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案",Toast.LENGTH_SHORT).show();
            }
        }
    };

}
