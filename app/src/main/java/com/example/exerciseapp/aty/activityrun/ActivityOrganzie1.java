package com.example.exerciseapp.aty.activityrun;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.organzie.BindActivity;
import com.example.exerciseapp.aty.organzie.CreateOrganzieActivity;
import com.example.exerciseapp.aty.organzie.OrganzieDetailActivity;
import com.example.exerciseapp.aty.organzie.PhotoGridActivity;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/29.
 */
public class ActivityOrganzie1 extends BindActivity {
    public String TAG = this.getClass().getSimpleName();

    WebView mWebView;
    TextView mCreate;
    ImageView mSetting;
    TextView mShare;

    String orgId;
    String activityId;
    String url = "http://101.200.214.68/html5/org/list.html?";

    RelativeLayout toolbar;
    TextView pageTitle;

    ImageView goback;

    private static IWXAPI api;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organzie1);

        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        mRequestQueue = Volley.newRequestQueue(this);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        goback = (ImageView) findViewById(R.id.goback);

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
        if (pageTitle != null) {
            pageTitle.setText("组织机构");
        }


        String dataUrl = url + "uid=" + MyApplication.getInstance().getUid()
                + "&token=" + MyApplication.getInstance().getToken();

        mCreate = (TextView) findViewById(R.id.oranzie_creat);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityOrganzie1.this, CreateOrganzieActivity.class));
            }
        });
        mWebView = (WebView) findViewById(R.id.webView);

        mSetting = (ImageView) findViewById(R.id.setting);
        mShare = (TextView) findViewById(R.id.oranzie_share);
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
                            Intent intent = new Intent(ActivityOrganzie1.this, PhotoGridActivity.class);
                            intent.putExtra("orgid", orgId);
                            intent.putExtra("albumid", albumId);
                            startActivityForResult(intent,200);
                        }
                    });

                    return true;
                } else if (url.startsWith("http://101.200.214.68/html5/org/org")) {
                    Intent intent = new Intent(ActivityOrganzie1.this, OrganzieDetailActivity.class);
                    intent.putExtra("orgId", orgId);
                    startActivity(intent);
                    return true;
                }
                else {
                    return false;
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {
                Log.e(TAG, "shouldInterceptRequest: " + url);
                if (url.startsWith("http://101.200.214.68/html5/org/show.html?")) {
//                    http://101.200.214.68/html5/org/show.html?uid=33&id=7&token=77500ecd671e12dff78446e62d210132
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int start = url.indexOf("&id=");
                            int end = url.indexOf("&token=");
                            orgId = url.substring(start + 4, end);
                            Log.e(TAG, "run: orgId" + orgId);
                            mCreate.setVisibility(View.GONE);
                            mSetting.setVisibility(View.VISIBLE);
                            mShare.setVisibility(View.GONE);
                            pageTitle.setText("机构信息");
                        }
                    });
                } else if (url.startsWith("http://101.200.214.68/html5/org/list.html?")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mCreate.setVisibility(View.VISIBLE);
                            mSetting.setVisibility(View.GONE);
                            mShare.setVisibility(View.GONE);
                            pageTitle.setText("组织机构");
                        }
                    });
                } else if (url.startsWith("http://101.200.214.68/html5/org/album.html?albumListId=")) {

                }else if(url.startsWith("http://101.200.214.68/html5/org/detail.html")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int start = url.indexOf("&activity_id=");
                            int end = url.indexOf("&token");
                            activityId = url.substring(start + 13, end);
                            Log.e(TAG, "run: activity_id" + activityId );
                            mCreate.setVisibility(View.GONE);
                            mSetting.setVisibility(View.GONE);
                            mShare.setVisibility(View.VISIBLE);
                            pageTitle.setText("报名信息");
                        }
                    });
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
        mWebView.loadUrl(dataUrl);
    }

    private void showPopupWindow(String activityid) {
       final  String shareurl = "http://101.200.214.68/py/share?action=share_org_act&uid="+MyApplication.uid+"&version=3.3.0&activity_id=" + activityid ;
        PopupWindow popupWindow = null;
        final View view;
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.share_layout, null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 分享到朋友圈
            view.findViewById(R.id.btnWxFriends).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(shareurl, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getString("result").equals("1")) {
                                    JSONObject json = jsonObject.getJSONObject("data");
                                    json.put("flag", 1);
                                    new MyTask().execute(json);

                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }) {


                    };
                    mRequestQueue.add(stringRequest);
                }
            });
            // 分享到朋友
            view.findViewById(R.id.btnWxTimeLine).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(shareurl, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getString("result").equals("1")) {
                                    JSONObject json = jsonObject.getJSONObject("data");
                                    json.put("flag", 0);
                                    new MyTask().execute(json);
                                } else {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                    };
                    mRequestQueue.add(stringRequest);
                }
            });
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // WindowManager windowManager = (WindowManager)
        // getSystemService(Context.WINDOW_SERVICE);
        popupWindow.showAtLocation(mWebView, Gravity.RIGHT | Gravity.BOTTOM, 0, 0);

    }

    class MyTask extends AsyncTask<JSONObject, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            Bitmap bitmap = null;
            try {
                String url = json.getString("image");
                // 加载一个网络图片
                InputStream is = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(is);
                wechatShare(json.getString("url"), json.getString("title"), json.getString("content"), bitmap,
                        json.getInt("flag"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }

    public void wechatShare(String webPageUrl, String title, String description, Bitmap bitmap, int flag)
            throws IOException {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webPageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        msg.setThumbImage(thumbBmp);
        thumbBmp.recycle();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.setting)
    public void setting() {
        Intent intent = new Intent(ActivityOrganzie1.this, OrganzieDetailActivity.class);
        intent.putExtra("orgId", orgId);
        startActivity(intent);
    }

    @OnClick(R.id.oranzie_share)
    public void share(){
        showPopupWindow(activityId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(url != null){
//            mWebView.reload();
//        }
    }
}
