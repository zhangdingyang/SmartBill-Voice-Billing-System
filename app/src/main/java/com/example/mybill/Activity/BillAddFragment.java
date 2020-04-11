package com.example.mybill.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.Category;
import com.example.mybill.bean.PaymentMethod;
import com.example.mybill.bean.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


public class BillAddFragment extends Fragment{
    private TextView text_date;

    private Date javaDate_date;

    private Button edt_date;
    private Button submit_button;
    private Button cancel_button;

    private Spinner edt_type;
    private Spinner edt_category;
    private Spinner edt_paymentMethod;

    private List<String> typeData;
    private List<String> categoryData;
    private List<String> paymentMethodData;

    private ArrayAdapter typeAdapter;
    private ArrayAdapter categoryAdapter;
    private ArrayAdapter paymentMethodAdapter;

    private EditText edt_amount;
    private EditText edt_title;
    private EditText edt_comment;

    private Bill bill;

    private Map<String, String> categoryMap;
    private Map<String, String> paymentMethodMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_add_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //日期选择按钮的组件弹出
        text_date = (TextView)getActivity().findViewById(R.id.text_date);
        edt_date = getActivity().findViewById(R.id.edt_date);
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        javaDate_date = new Date(year - 1900, month, dayOfMonth);
                        text_date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //类型选择下拉列表初始化
        edt_type = getActivity().findViewById(R.id.edt_type);
        typeData = new ArrayList<String>();
        typeData.add("请选择类型");
        typeData.add("in");
        typeData.add("out");
        typeAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, typeData);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edt_type.setAdapter(typeAdapter);
        edt_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!typeData.get(position).equals("请选择类型"))
                    Toast.makeText(getActivity(),"选择类型成功:" + typeData.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"选择类型出错",Toast.LENGTH_SHORT).show();
            }
        });

        //交易方式选择下拉列表初始化
        edt_paymentMethod = getActivity().findViewById(R.id.edt_paymentMethod);
        paymentMethodData = new ArrayList<String>();
        paymentMethodData.add("请选择交易方式");
        BmobQuery<PaymentMethod> paymentMethodBmobQuery = new BmobQuery<PaymentMethod>();
        paymentMethodBmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        paymentMethodBmobQuery.addWhereEqualTo("isDeleted", false);
        paymentMethodBmobQuery.findObjects(new FindListener<PaymentMethod>() {
            @Override
            public void done(List<PaymentMethod> list, BmobException e) {
                if (e == null){
                    paymentMethodMap = new HashMap<>();
                    for (PaymentMethod paymentMethod: list){
                        paymentMethodMap.put(paymentMethod.getName(), paymentMethod.getObjectId());
                        paymentMethodData.add(paymentMethod.getName());
                    }
                }
                else {
                    Toast.makeText(getActivity(),"查询交易方式时出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        paymentMethodAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, paymentMethodData);
        paymentMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edt_paymentMethod.setAdapter(paymentMethodAdapter);
        edt_paymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!paymentMethodData.get(position).equals("请选择交易方式"))
                    Toast.makeText(getActivity(),"选择交易方式成功:" + paymentMethodData.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"选择交易方式出错",Toast.LENGTH_SHORT).show();
            }
        });

        //类别选择下拉列表初始化
        edt_category = getActivity().findViewById(R.id.edt_category);
        categoryData = new ArrayList<String>();
        categoryData.add("请选择类别");
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<Category>();
        categoryBmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        categoryBmobQuery.addWhereEqualTo("isDeleted", false);
        categoryBmobQuery.findObjects(new FindListener<Category>() {
            @Override
            public void done(List<Category> list, BmobException e) {
                if (e == null){
                    categoryMap = new HashMap<>();
                    for (Category category: list){
                        categoryMap.put(category.getName(), category.getObjectId());
                        categoryData.add(category.getName());
                    }
                }
                else {
                    Toast.makeText(getActivity(),"查询类别时出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        categoryAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, categoryData);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edt_category.setAdapter(categoryAdapter);
        edt_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!categoryData.get(position).equals("请选择类别"))
                    Toast.makeText(getActivity(),"选择类别成功:" + categoryData.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"选择类别出错",Toast.LENGTH_SHORT).show();
            }
        });

        //提交按钮操作，生成Bill对象并新增
        submit_button =(Button)getActivity().findViewById(R.id.button_summit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_amount = getActivity().findViewById(R.id.edt_amount);
                edt_title = getActivity().findViewById(R.id.edt_title);
                edt_comment = getActivity().findViewById(R.id.edt_comment);

                //检查表单完整性
                if (text_date.getText().toString().equals("未选择日期")){
                    Toast.makeText(getActivity(),"未选择日期",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_type.getSelectedItem().toString().equals("请选择类型")){
                    Toast.makeText(getActivity(),"未选择类型",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_category.getSelectedItem().toString().equals("请选择类别")){
                    Toast.makeText(getActivity(),"未选择类别",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_paymentMethod.getSelectedItem().toString().equals("请选择交易方式")){
                    Toast.makeText(getActivity(),"未选择交易方式",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_amount.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"未输入金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_title.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"未输入标题",Toast.LENGTH_SHORT).show();
                    return;
                }

                //表单完整，创建账本对象
                bill = new Bill();
                bill.setDate(new BmobDate(javaDate_date));
                bill.setType(edt_type.getSelectedItem().toString());
                Category category = new Category();
                category.setObjectId(categoryMap.get(edt_category.getSelectedItem().toString()));
                bill.setCategoryId(category);
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setObjectId(paymentMethodMap.get(edt_paymentMethod.getSelectedItem().toString()));
                bill.setPaymentMethod(paymentMethod);
                bill.setAmount(Integer.parseInt(edt_amount.getText().toString().trim()));
                bill.setTitle(edt_title.getText().toString());
                bill.setComment(edt_comment.getText().toString());
                User user = new User();
                user.setObjectId(BmobUser.getCurrentUser().getObjectId());
                bill.setUserId(user);

                //插入数据
                bill.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            Toast.makeText(getActivity(),"添加数据成功，返回objectId为:" + s,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), BillMainActivity.class));
                        }else{
                            Toast.makeText(getActivity(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });

        //取消按钮操作，返回账本主页
        cancel_button =(Button)getActivity().findViewById(R.id.button_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BillMainActivity.class));
            }
        });
    }

}
