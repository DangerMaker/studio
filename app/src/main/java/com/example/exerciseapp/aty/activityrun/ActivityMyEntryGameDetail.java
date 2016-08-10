package com.example.exerciseapp.aty.activityrun;
/**
 * 报名详情页面
 * add by sonchcng
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

public class ActivityMyEntryGameDetail extends BaseActivity {
    private WebView webview;
    private Toolbar toolbar;
    private TextView pageTitle;

    String webUrl;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_personal_entrygamedetail);
        Intent intent = getIntent();
        webUrl = intent.getStringExtra("click_brief");
        setTitleBar();
        webview = (WebView) findViewById(R.id.myentrydetail);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setWebViewClient(new myWebViewClient());
        webview.loadUrl(webUrl);
    }

    // 設置沉浸式菜单栏
    @SuppressLint("NewApi")
    public void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("报名详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    if (webview.getUrl().equals(webUrl)) {
                        ActivityMyEntryGameDetail.this.finish();
                    } else {
                        webview.goBack();
                    }
                } else {
                    ActivityMyEntryGameDetail.this.finish();
                }

            }
        });
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

}
