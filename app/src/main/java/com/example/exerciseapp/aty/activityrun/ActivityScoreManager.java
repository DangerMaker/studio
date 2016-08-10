package com.example.exerciseapp.aty.activityrun;
/**
 * 积分管理页面
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

public class ActivityScoreManager extends CreditActivity {
    private WebView webview;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
