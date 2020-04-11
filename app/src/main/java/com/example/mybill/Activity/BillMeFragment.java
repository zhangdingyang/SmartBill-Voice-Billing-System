package com.example.mybill.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;

import com.baidu.location.LocationClientOption;
import com.example.mybill.ResetLocationListener;
import com.example.mybill.R;
import com.example.mybill.bean.Category;
import com.example.mybill.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class BillMeFragment extends Fragment {
    private TextView textView;
    private Button button_exit;
    private Button button_myCategory;
    private Button button_myPaymentMethod;
    private Button button_resetLocation;
    private Button button_resetGender;

    private SwipeRefreshLayout swipe;

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
        setUserInfo();


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
                option.setIsNeedAddress(true);
                option.setOpenGps(true);
                option.setScanSpan(0);

                mLocationClient.setLocOption(option);
                //mLocationClient为第二步初始化过的LocationClient对象
                //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
                //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

                mLocationClient.start();
            }
        });

        //修改性别按钮
        button_resetGender = getActivity().findViewById(R.id.btn_resetGender);
        button_resetGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("修改性别")
                        .setMessage("请选择您的性别")
                        .setPositiveButton("男", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User user = BmobUser.getCurrentUser(User.class);
                                user.setGender("男");
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Toast.makeText(getActivity(),"修改信息成功",Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                            Toast.makeText(getActivity(),"修改信息出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("女", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                User user = BmobUser.getCurrentUser(User.class);
                                user.setGender("女");
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Toast.makeText(getActivity(),"修改信息成功",Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                            Toast.makeText(getActivity(),"修改信息出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });

        //设置下拉刷新
        swipe = getActivity().findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUserInfo();
                Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });
    }

    /**
     * 加载用户信息
     */
    public void setUserInfo() {
        new BmobQuery<User>().getObject(BmobUser.getCurrentUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    textView.setText("当前用户\n用户名：" + user.getUsername() +
                            "\n手机号：" + user.getMobilePhoneNumber() +
                            "\n性别：" + user.getGender() +
                            "\n所在地：" + user.getAddress());
                }
                else
                    Toast.makeText(getActivity(),"查询用户出错:" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
