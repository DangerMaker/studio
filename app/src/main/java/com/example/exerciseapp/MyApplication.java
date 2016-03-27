package com.example.exerciseapp;

import android.app.Application;

import com.example.exerciseapp.utils.SharedPreferencesHelper;

/**
 * Created by lyjq on 2016/3/27.
 */
public class MyApplication extends Application {
    String uid = "";
    @Override
    public void onCreate() {
        super.onCreate();
        uid = SharedPreferencesHelper.getInstance(this).getValue("uid");
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
