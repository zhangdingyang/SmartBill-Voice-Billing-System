package com.example.mybill;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"8915f79c08da038d8b396e10a000abf2");
    }
}
