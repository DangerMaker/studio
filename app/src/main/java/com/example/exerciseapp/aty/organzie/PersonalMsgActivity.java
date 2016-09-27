package com.example.exerciseapp.aty.organzie;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;

import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/19.
 */
public class PersonalMsgActivity extends Activity {
    public String TAG = this.getClass().getSimpleName();

    WebView mWebView;

    String orgId;
    String url = "http://101.200.214.68/html5/messages/list.html?";

    RelativeLayout toolbar;
    TextView pageTitle;
    ImageView goback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organzie2);

        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        goback = (ImageView)findViewById(R.id.goback);
        if (pageTitle != null) {
            pageTitle.setText("个人消息");
        }

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack(); // goBack()表示返回WebView的上一页面
                }else{
                    finish();
                }
            }
        });


        String dataUrl = url + "uid=" + MyApplication.getInstance().getUid()
                + "&token=" + MyApplication.getInstance().getToken();

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (url.startsWith("http://101.200.214.68/html5/org/album.html?albumListId=")) { //伪代码。判断是否是需要过滤的url,是的话，就返回不处理
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int start = url.indexOf("albumListId=");
                            int end = url.length();
                            String albumId = url.substring(start + 12, end);
                            Log.e(TAG, "run:albumId" + albumId);
                            ;
                            Log.e(TAG, "run:orgId" + orgId);
                            ;
                            Intent intent = new Intent(PersonalMsgActivity.this, PhotoGridActivity.class);
                            intent.putExtra("orgid", orgId);
                            intent.putExtra("albumid", albumId);
                            startActivityForResult(intent,200);
                        }
                    });

                    return true;
                } else if (url.startsWith("http://101.200.214.68/html5/org/org")) {
                    Intent intent = new Intent(PersonalMsgActivity.this, OrganzieDetailActivity.class);
                    intent.putExtra("orgId", orgId);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {

                return super.shouldInterceptRequest(view, url);
            }
        });
        mWebView.loadUrl(dataUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
