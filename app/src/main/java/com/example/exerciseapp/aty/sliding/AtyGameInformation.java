package com.example.exerciseapp.aty.sliding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.aty.team.ApplyAllActivity;
import com.example.exerciseapp.myutils.HintDialog;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
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
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtyGameInformation extends Activity {
    private String gameId = null;
    private String gameH5Url = null;
    private String gameStat = null;
    private String agreement = null;
    public WebView webView;
    private String gameName = null;
    private Button btnIWannaJoin; // 我要报名按钮
    private PopupWindow popTitleMenu;
    private View layoutTitle;
    private ListView lvMenuListTitle;
    private List<Map<String, String>> listMenuTitle;
    private RequestQueue mRequestQueue;
    private static IWXAPI api;
    private ProgressDialog progressDialog;
    private RelativeLayout toolbar;

    public ImageView arrowdown;
    public ImageView arrowup;
    public ImageView wxshare;
    public TextView toolbar_text;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        mRequestQueue = Volley.newRequestQueue(this);
        gameId = getIntent().getStringExtra(Config.KEY_GAME_ID);
        gameH5Url = getIntent().getStringExtra(Config.KEY_GAME_H5_URL);
        gameStat = getIntent().getStringExtra(Config.KEY_GAME_STATUS_ID);
        gameName = getIntent().getStringExtra(Config.KEY_GAME_NAME);
        agreement = getIntent().getStringExtra("agreement");
        setContentView(R.layout.aty_game_information);

        btnIWannaJoin = (Button) findViewById(R.id.btnIWannaJoinGameInformation);
        // 点击标题下拉菜单
        listMenuTitle = new ArrayList<Map<String, String>>();
        HashMap<String, String> mapTemp = new HashMap<String, String>();
        mapTemp.put("item", "往届回顾");
        listMenuTitle.add(mapTemp);
        HashMap<String, String> mapTemp1 = new HashMap<String, String>();
        mapTemp1.put("item", "现场实况");
        listMenuTitle.add(mapTemp1);
        HashMap<String, String> mapTemp2 = new HashMap<String, String>();
        mapTemp2.put("item", "成绩播报");
        listMenuTitle.add(mapTemp2);

        if (btnIWannaJoin.isClickable()) {
            btnIWannaJoin.setOnClickListener(new OnClickListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View v) {
                    if ("0".equals(MyApplication.getInstance().getUid())) {
                        hintLogin();
                    } else {
                        progressDialog = new ProgressDialog(AtyGameInformation.this);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                Config.SERVER_URL + "Game/getEventNew", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getInt("result") == 1) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(AtyGameInformation.this, ApplyAllActivity.class);
                                        intent.putExtra(Config.KEY_GAME_ID, gameId);
                                        intent.putExtra(Config.KEY_GAME_NAME, gameName);
                                        intent.putExtra("agreement", agreement);
                                        intent.putExtra("entryInfor", jsonObject.getJSONObject("data").toString());
                                        startActivity(intent);
                                    } else {
                                        progressDialog.dismiss();
                                    }

                                } catch (JSONException e) {
                                    progressDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progressDialog.dismiss();
                                Toast.makeText(AtyGameInformation.this, Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
                                map.put(Config.KEY_GAME_ID, gameId);
                                return map;
                            }
                        };
                        mRequestQueue.add(stringRequest);
                    }
                }
            });
        }

        if (!gameStat.equals("1")) {
            btnIWannaJoin.setClickable(false);
            btnIWannaJoin.setBackgroundColor(Color.GRAY);
            if (gameStat.equals("2")) {
                btnIWannaJoin.setText("报名未开始");
            } else {
                btnIWannaJoin.setText("报名结束");
            }
        }
        initview();
        getInfo();
    }

    private void getInfo() {
        webView = (WebView) findViewById(R.id.game_info_ht5);

        webView.loadUrl(gameH5Url);
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        // enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath("/data/data/com.example.exercise.webview/databases/");
        // enable Web Storage: localStorage, sessionStorage
        s.setDomStorageEnabled(true);
        webView.requestFocus();
        webView.setWebViewClient(new myWebViewClient());
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

    private void initview() {
        ImageView mygoback = (ImageView) findViewById(R.id.goback);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        arrowdown = (ImageView) findViewById(R.id.arrowdown);
        arrowup = (ImageView) findViewById(R.id.arrowup);
        wxshare = (ImageView) findViewById(R.id.wxshare);
        toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        mygoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    if (webView.getUrl().equals(gameH5Url)) {
                        AtyGameInformation.this.finish();
                    } else {
                        webView.goBack();
                    }
                } else {
                    AtyGameInformation.this.finish();
                }
            }
        });
        arrowdown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowdown.setVisibility(View.GONE);
                arrowup.setVisibility(View.VISIBLE);
                showPopMenu();

            }
        });
        wxshare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

    }

    private void showPopMenu() {
        if (popTitleMenu != null && popTitleMenu.isShowing()) {
            arrowdown.setVisibility(View.VISIBLE);
            arrowup.setVisibility(View.GONE);
            popTitleMenu.dismiss();
        } else {
            layoutTitle = getLayoutInflater().inflate(R.layout.game_information_title_menu_list, null);
            lvMenuListTitle = (ListView) layoutTitle.findViewById(R.id.titleMenuListGameInformation);
            SimpleAdapter listAdapter = new SimpleAdapter(AtyGameInformation.this, listMenuTitle,
                    R.layout.game_information_title_tab_menu_list_item, new String[]{"item"},
                    new int[]{R.id.titleMenuItemGameInformation});
            lvMenuListTitle.setAdapter(listAdapter);

            lvMenuListTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Intent intent;
                    switch (arg2) {
                        case 0:
                            Toast.makeText(AtyGameInformation.this, "暂无往届回顾", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            intent = new Intent(AtyGameInformation.this, AtyLocalScene.class);
                            intent.putExtra(Config.KEY_GAME_ID, gameId);
                            intent.putExtra(Config.KEY_GAME_NAME, gameName);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(AtyGameInformation.this, AtyGradeReport.class);
                            intent.putExtra(Config.KEY_GAME_ID, gameId);
                            intent.putExtra(Config.KEY_GAME_NAME, gameName);
                            startActivity(intent);
                            break;
                        default:
                            arrowdown.setVisibility(View.VISIBLE);
                            arrowup.setVisibility(View.GONE);
                            popTitleMenu.dismiss();
                            break;
                    }
                    if (popTitleMenu != null && popTitleMenu.isShowing()) {

                        arrowdown.setVisibility(View.VISIBLE);
                        arrowup.setVisibility(View.GONE);
                        popTitleMenu.dismiss();
                    }
                }
            });

            popTitleMenu = new PopupWindow(layoutTitle, toolbar_text.getWidth(),
                    LayoutParams.WRAP_CONTENT);
            ColorDrawable cd = new ColorDrawable(0x000000);
            popTitleMenu.setBackgroundDrawable(cd);
            popTitleMenu.setAnimationStyle(R.style.PopupAnimation);
            popTitleMenu.update();
            popTitleMenu.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popTitleMenu.setTouchable(true); // 设置popupwindow可点击
            popTitleMenu.setOutsideTouchable(true); // 设置popupwindow外部可点击
            popTitleMenu.setFocusable(true); // 获取焦点
            // 设置popupwindow的位置
            int topBarHeight = toolbar.getBottom();
            popTitleMenu.showAsDropDown(toolbar_text, 0,
                    (topBarHeight - toolbar_text.getHeight()) / 2);

            popTitleMenu.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // 如果点击了popupwindow的外部，popupwindow也会消失
                    arrowdown.setVisibility(View.VISIBLE);
                    arrowup.setVisibility(View.GONE);

                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

                        popTitleMenu.dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    // 分享函数
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

    // 分享函数

    /**
     * @param webPageUrl  需要跳转的链接
     * @param title       分享标题
     * @param description 分享内容
     * @param bitmap      图片地址
     * @param flag        分享到朋友还是朋友圈的flag
     */

    public void wechatShare(String webPageUrl, String title, String description, Bitmap bitmap, int flag)
            throws IOException {
        // api.openWXApp();
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

    @SuppressWarnings("deprecation")
    private void showPopupWindow() {
        PopupWindow popupWindow = null;
        View view;
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.share_layout, null);
            popupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            // 分享到朋友圈
            view.findViewById(R.id.btnWxFriends).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Config.SERVER_URL + "Users/shareFunc", new Response.Listener<String>() {

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

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("type", "1");
                            map.put("gid", gameId);
                            return map;
                        }
                    };
                    mRequestQueue.add(stringRequest);
                }
            });
            // 分享到朋友
            view.findViewById(R.id.btnWxTimeLine).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            Config.SERVER_URL + "Users/shareFunc", new Response.Listener<String>() {

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

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("type", "1");
                            map.put("gid", gameId);
                            return map;
                        }
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
        popupWindow.showAtLocation(findViewById(R.id.game_info_content), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);

    }

    public void hintLogin() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.dialog_show_gotologin, null);
        final HintDialog dialog = new HintDialog(this, dialogView,
                R.style.MyDialogStyle);
        dialog.setCancelable(true);
        TextView tvHintDialogMessage = (TextView) dialogView
                .findViewById(R.id.tvHintDialogMessage);
        TextView btnCancel = (TextView) dialogView
                .findViewById(R.id.btnHintDialogNo);
        TextView btnToLogin = (TextView) dialogView
                .findViewById(R.id.btnHintDialogYes);
        tvHintDialogMessage.setText(this.getString(
                R.string.hintLoginMessage));
        btnCancel.setText(this.getString(R.string.cancel));
        btnToLogin.setText(this.getString(R.string.toLogin));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(AtyGameInformation.this, AtyWelcome.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }
}
