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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybill.R;
import com.example.mybill.bean.Bill;
import com.example.mybill.bean.Category;
import com.example.mybill.bean.PaymentMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class BillAddFragment extends Fragment{
    private TextView date;
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
    private Bill bill;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_add_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        edt_date = getActivity().findViewById(R.id.edt_date);
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = (TextView)getActivity().findViewById(R.id.text_date);
                        date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                Toast.makeText(getActivity(),"选择类型成功" + typeData.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"选择类型出错",Toast.LENGTH_SHORT).show();
            }
        });

        edt_paymentMethod = getActivity().findViewById(R.id.edt_paymentMethod);
        paymentMethodData = new ArrayList<String>();
        paymentMethodData.add("请选择交易方式");
        BmobQuery<PaymentMethod> paymentMethodBmobQuery = new BmobQuery<PaymentMethod>();
        paymentMethodBmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        paymentMethodBmobQuery.findObjects(new FindListener<PaymentMethod>() {
            @Override
            public void done(List<PaymentMethod> list, BmobException e) {
                if (e == null){
                    for (PaymentMethod paymentMethod: list){
                        paymentMethodData.add(paymentMethod.getObjectId() + paymentMethod.getName());
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
                Toast.makeText(getActivity(),"选择交易方式成功" + paymentMethodData.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"选择交易方式出错",Toast.LENGTH_SHORT).show();
            }
        });

        edt_category = getActivity().findViewById(R.id.edt_category);
        categoryData = new ArrayList<String>();
        categoryData.add("请选择类别");
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<Category>();
        categoryBmobQuery.addWhereEqualTo("userId", BmobUser.getCurrentUser().getObjectId());
        categoryBmobQuery.findObjects(new FindListener<Category>() {
            @Override
            public void done(List<Category> list, BmobException e) {
                if (e == null){
                    for (Category category: list){
                        categoryData.add(category.getObjectId() + category.getName());
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
                Toast.makeText(getActivity(),"选择类别成功" + categoryData.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(),"选择类别出错",Toast.LENGTH_SHORT).show();
            }
        });

        submit_button =(Button)getActivity().findViewById(R.id.button_summit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"BillAddFragment",Toast.LENGTH_SHORT).show();



                bill = new Bill();
            }
        });

        cancel_button =(Button)getActivity().findViewById(R.id.button_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BillMainActivity.class));
            }
        });
    }

}
