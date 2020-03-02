package com.example.mybill.Activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;

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
    }

    @OnClick({R.id.btn_user_signup_password, R.id.btn_signup_or_login_sms, R.id.btn_user_login_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_user_signup_password:
                startActivity(new Intent(this, UserSignUpPasswordActivity.class));
                break;
            case R.id.btn_signup_or_login_sms:
                startActivity(new Intent(this, UserSignUpOrLoginSmsActivity.class));
                break;
            case R.id.btn_user_login_password:
                startActivity(new Intent(this, UserLoginPasswordActivity.class));
                break;
            default:
                break;
        }
    }
}
