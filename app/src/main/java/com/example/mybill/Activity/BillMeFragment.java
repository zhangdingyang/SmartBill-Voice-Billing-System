package com.example.mybill.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;

import com.baidu.location.LocationClientOption;
import com.example.mybill.ResetLocationListener;
import com.example.mybill.R;

import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class BillMeFragment extends Fragment {
    private TextView textView;
    private Button button_exit;
    private Button button_myCategory;
    private Button button_myPaymentMethod;
    private Button button_resetLocation;

    public LocationClient mLocationClient = null;
    private ResetLocationListener myListener = new ResetLocationListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bill_me_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView=(TextView)getActivity().findViewById(R.id.textView);
        textView.setText(textView.getText() + BmobUser.getCurrentUser().getUsername());

        button_exit =(Button)getActivity().findViewById(R.id.btn_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        button_myCategory = getActivity().findViewById(R.id.btn_myCategory);
        button_myCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyCategoryActivity.class));
            }
        });

        button_myPaymentMethod = getActivity().findViewById(R.id.btn_myPaymentMethod);
        button_myPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyPaymentMethodActivity.class));
            }
        });

        //重设所在地按钮
        button_resetLocation = getActivity().findViewById(R.id.btn_resetLocation);
        button_resetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient = new LocationClient(getApplicationContext());
                //声明LocationClient类
                mLocationClient.registerLocationListener(myListener);
                //注册监听函数

                LocationClientOption option = new LocationClientOption();
                option.setOpenGps(true);
                option.setScanSpan(0);

                mLocationClient.setLocOption(option);
                //mLocationClient为第二步初始化过的LocationClient对象
                //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
                //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

                mLocationClient.start();
            }
        });
    }

}
