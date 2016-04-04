package com.example.exerciseapp;

import android.app.Application;

import com.example.exerciseapp.utils.SharedPreferencesHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by lyjq on 2016/3/27.
 */
public class MyApplication extends Application {
    String uid = "";
    @Override
    public void onCreate() {
        super.onCreate();
        uid = SharedPreferencesHelper.getInstance(this).getValue("uid");

        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, new OkHttpClient())
                .build();
        Fresco.initialize(this, config);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
