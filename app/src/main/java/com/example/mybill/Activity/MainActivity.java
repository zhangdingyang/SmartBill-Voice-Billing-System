package com.example.mybill.Activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import android.os.Bundle;
import android.view.View;

import com.example.mybill.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bmob.initialize(this,"8915f79c08da038d8b396e10a000abf2");

        //增加判断，若缓存中有用户信息则无需重新登录
        if (BmobUser.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, BillMainActivity.class));
        }
    }

    @OnClick({R.id.btn_signup_sms, R.id.btn_login_sms, R.id.btn_user_login_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signup_sms:
                startActivity(new Intent(this, UserSignUpPasswordAndSmsActivity.class));
                break;
            case R.id.btn_login_sms:
                startActivity(new Intent(this, UserLoginSmsActivity.class));
                break;
            case R.id.btn_user_login_password:
                startActivity(new Intent(this, UserLoginPasswordActivity.class));
                break;
            default:
                break;
        }
    }
}
