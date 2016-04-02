package com.example.exerciseapp.aty.sliding;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by Cherie_No.47 on 2016/4/2 11:05.
 * Email jascal@163.com
 */
public class AtyLocalSceneDetail extends BaseActivity {
    private JSONObject jsonObj;
    private static IWXAPI api;
    @Bind(R.id.local_sence_ht5)
    WebView webView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text)
    TextView toolBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        setContentView(R.layout.activity_local_sence_detail);
        try {
            jsonObj = new JSONObject(getIntent().getStringExtra("info"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTitleBar();
        getInfo();
    }

    private void getInfo() {
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        try {
            webView.loadUrl(jsonObj.getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void setTitleBar() {
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        try {
            toolBarTitle.setText(jsonObj.getString("title").equals("") ? "实况详情" : jsonObj.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyLocalSceneDetail.this.finish();
            }
        });
    }
}
