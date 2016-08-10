package com.example.exerciseapp.aty.activityrun;
/**
 * 个人积分页面
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

public class ActivityCity extends Activity {
    private WebView webview;
    private Toolbar toolbar;
    private TextView pageTitle;

    String webUrl = "http://101.200.214.68/process.html";

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            if (webview.getUrl().equals(webUrl)) {
                super.onBackPressed();
            } else {
                webview.goBack();
            }
        } else {
            super.onBackPressed();
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_finder_city);
        ActionBar actionBar = getActionBar();
        if (null != actionBar)
            actionBar.hide();
        ImageView mygoback = (ImageView) findViewById(R.id.goback);
        mygoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview.canGoBack()) {
                    if (webview.getUrl().equals(webUrl)) {
                        ActivityCity.this.finish();
                    } else {
                        webview.goBack();
                    }
                } else {
                    ActivityCity.this.finish();
                }
            }
        });
        webview = (WebView) findViewById(R.id.mycitywebView);
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

}
