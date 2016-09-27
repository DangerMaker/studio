package com.example.exerciseapp.aty.login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

/**
 * Created by Administrator on 2016/8/10.
 */
public class ActivityWeb  extends Activity {
    private WebView webview;
    String webUrl;

    @Override
    public void onBackPressed() {
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initwindow();
        setContentView(R.layout.activity_web);
        if(getIntent() != null && getIntent().getStringExtra("url") != null){
            webUrl = getIntent().getStringExtra("url");
        }
        ActionBar actionBar = getActionBar();
        if (null != actionBar)
            actionBar.hide();
//        setTitleBar();
        String test = Config.getCachedUserUid(getApplicationContext());
        ImageView mygoback = (ImageView) findViewById(R.id.goback);
        mygoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    if (webview.getUrl().equals(webUrl)) {
                        ActivityWeb.this.finish();
                    } else {
                        webview.goBack();
                    }
                } else {
                    ActivityWeb.this.finish();
                }
            }
        });
        webview = (WebView) findViewById(R.id.mypointwebView);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new myWebViewClient());

        webview.loadUrl(webUrl);
    }

    class myWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
    }

    /**
     * add by sonchcng
     * 设置顶部状态栏
     */
    public void initwindow() {

        Window window = ActivityWeb.this.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbarbg));

        ViewGroup mContentView = (ViewGroup) ActivityWeb.this.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

}

