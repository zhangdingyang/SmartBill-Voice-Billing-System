package com.example.mybill;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import cn.bmob.v3.Bmob;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"8915f79c08da038d8b396e10a000abf2");

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5c2b6fb0");
    }
}
