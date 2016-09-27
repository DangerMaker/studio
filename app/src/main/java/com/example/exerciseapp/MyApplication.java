package com.example.exerciseapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.example.exerciseapp.utils.SharedPreferencesHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lyjq on 2016/3/27.
 */
public class MyApplication extends Application {
    public static String uid = "";
    public static String token = "";
    public static Context context;
    public List<Activity> activities = new LinkedList<Activity>();
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        uid = SharedPreferencesHelper.getInstance(this).getValue("uid");
        token = SharedPreferencesHelper.getInstance(this).getValue("token");
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, new OkHttpClient())
                .build();
        Fresco.initialize(this, config);
        context = getApplicationContext();

    }

    /**
     * 获取 application 的  实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static Context getContext() {
        return context;
    }

    public static void showMessage(String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Looper.loop();
    }

    /**
     * 增加activity 到维护列表中
     *
     * @param activity：增加的activity
     */
    public void addmyActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    /**
     * 便利所有Activigty并finish
     */
    public void finishmyActivity() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }
}
