package com.example.exerciseapp.aty.sliding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.exerciseapp.utils.SpeedConvert;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

import butterknife.Bind;

public class AtyRunRecordDetail extends BaseActivity {
    private static IWXAPI api;
    MapView mMapView;
    AMap mMap;
    double[] polyjing;
    double[] polywei;
    double[] polyalti;
    int z = 0;
    private RequestQueue mRequestQueue;
    RelativeLayout relativeshowmap, relativeshowsudu, relativeshowpingjunsudu, relativeshowkaluli;
    LinearLayout linearshowjulishijian, linearshowsuduhepingjunsudu, linearshowjuli, linearshowshijian, linearshowsudu1,
            linearshowsudu2, linearshowpingjunsudu1, linearshowpingjunsudu2, linearshowhaibahekaluli, linearshowkaluli1,
            linearshowkaluli2;
    TextView textshowjuli, textshowjulidanwei, textshowshijian, textshowshijiandanwei, textshowzuigaosudu,
            textshowpingjunsudu, textshowkaluli;
    Button btnsave, btnfangqi;
    EditText edittextbeizhu;
    long shijian;
    float juli;
    float kaluli;
    static String filename_;

    private int sport_type;
    private double max_speed;
    private double minSpeed_onAverage;

    private Toolbar toolbar;
    private TextView pageTitle;

    @Bind(R.id.relativeshowDesc)
    RelativeLayout distance;
    @Bind(R.id.textshowDesc)
    TextView descText;

    @Bind(R.id.relativeshowPace)
    RelativeLayout paces;
    @Bind(R.id.textshowPace)
    TextView paceText;

    @Bind(R.id.relativeshowAlit)
    RelativeLayout alit;
    @Bind(R.id.textshowAlit)
    TextView alitText;

    private void setUpSportData() {
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/impact.ttf");
        switch (sport_type) {
            case 0:
            case 2:
                distance.setVisibility(View.VISIBLE);
                paces.setVisibility(View.GONE);
                alit.setVisibility(View.GONE);
                descText.setText((float) (Math.round(juli / 1000 * 100)) / 100 + "");
                descText.setTypeface(fontFace);
                break;
            case 1:
                distance.setVisibility(View.GONE);
                paces.setVisibility(View.VISIBLE);
                alit.setVisibility(View.GONE);
                paceText.setText((int) (juli / 0.5) + "");
                paceText.setTypeface(fontFace);
                break;
            case 3:
                distance.setVisibility(View.GONE);
                paces.setVisibility(View.GONE);
                alit.setVisibility(View.VISIBLE);
                alitText.setText((int) (polyalti[z - 1] - polyalti[0]) + "");
                alitText.setTypeface(fontFace);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        setContentView(R.layout.aty_run_record_detail);
        polyjing = new double[20000];
        polywei = new double[20000];
        polyalti = new double[20000];
        setTitleBar();
        mRequestQueue = Volley.newRequestQueue(this);
        Intent i = getIntent();
        juli = i.getFloatExtra("distance", -1);// Math.round((i.getFloatExtra("intentjuli",
        // -1)/1000)*100))/100
        shijian = i.getLongExtra("duration", -1);
        sport_type = i.getIntExtra("sport_type", 0);//重要
        max_speed = i.getDoubleExtra("max_speed", 0);//重要
        max_speed = SpeedConvert.oriToShow(max_speed);
        minSpeed_onAverage = i.getDoubleExtra("minSpeed_onAverage", 0);//重要
        minSpeed_onAverage = SpeedConvert.oriToShow(minSpeed_onAverage);
        textshowzuigaosudu = (TextView) findViewById(R.id.textshowzuigaosudu);
        String[] data = i.getStringExtra("data").trim().split(" ");
        for (int j = 0; j < data.length; j++) {
            if (j % 3 == 0) {
                polyjing[z] = Double.valueOf(data[j].substring(0, 7));
            } else {
                if (data[j].equals("")) continue;
                polywei[z] = Double.valueOf(data[j].substring(0, 7));
                polyalti[z] = Double.valueOf(data[j].substring(8));
                z++;
            }
        }
        PolylineOptions polys = new PolylineOptions();
        kaluli = i.getFloatExtra("calorie", -1);
        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/impact.ttf");
        textshowzuigaosudu.setText((float) (Math.round(SpeedConvert.oriToShow(max_speed) * 100)) / 100 + "");
        textshowzuigaosudu.setTypeface(fontFace);
        mMapView = (MapView) findViewById(R.id.mapViewshow);
        mMapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();
        init();
        setUpSportData();
        if (z > 1) {
            for (int m = 0; m < z; m++) {
                polys.add(new LatLng(polyjing[m], polywei[m]));
            }
            mMap.addPolyline(polys);
        }
        relativeshowmap = (RelativeLayout) findViewById(R.id.relativeshowmap);
        linearshowjulishijian = (LinearLayout) findViewById(R.id.linearshowjulishijian);
        linearshowjuli = (LinearLayout) findViewById(R.id.linearshowjuli);
        textshowjuli = (TextView) findViewById(R.id.textshowjuli);
        textshowjuli.setText((float) (Math.round(juli / 1000 * 100)) / 100 + ""); // 获取并显示距离
        textshowjuli.setTypeface(fontFace);
        textshowjulidanwei = (TextView) findViewById(R.id.textshowjulidanwei);
        linearshowshijian = (LinearLayout) findViewById(R.id.linearshowshijian);
        textshowshijian = (TextView) findViewById(R.id.textshowshijian);
        textshowshijian.setText(shijian / 3600 + ":" + (shijian - (shijian / 3600) * 3600) / 60 + ":"
                + (shijian - shijian / 3600 * 3600 - (shijian - (shijian / 3600) * 3600) / 60 * 60));// 获取并显示时间
        textshowshijian.setTypeface(fontFace);
        textshowshijiandanwei = (TextView) findViewById(R.id.textshowshijiandanwei);
        linearshowsuduhepingjunsudu = (LinearLayout) findViewById(R.id.linearshowsuduhepingjunsudu);
        relativeshowpingjunsudu = (RelativeLayout) findViewById(R.id.relativeshowpingjunsudu);
        linearshowpingjunsudu1 = (LinearLayout) findViewById(R.id.linearshowpingjunsudu1);
        textshowpingjunsudu = (TextView) findViewById(R.id.textshowpingjunsudu);
        edittextbeizhu = (EditText) findViewById(R.id.edittextbeizhu);
        edittextbeizhu.setText(i.getStringExtra("remark"));
        if (minSpeed_onAverage == 0) textshowpingjunsudu.setText("0");
        textshowpingjunsudu.setText((float) (Math.round(SpeedConvert.oriToShow(minSpeed_onAverage) * 100)) / 100 + "");//速度
        textshowpingjunsudu.setTypeface(fontFace);
        linearshowpingjunsudu2 = (LinearLayout) findViewById(R.id.linearshowpingjunsudu2);
        linearshowhaibahekaluli = (LinearLayout) findViewById(R.id.linearshowhaibahekaluli);
        relativeshowkaluli = (RelativeLayout) findViewById(R.id.relativeshowkaluli);
        linearshowkaluli1 = (LinearLayout) findViewById(R.id.linearshowkaluli1);
        textshowkaluli = (TextView) findViewById(R.id.textshowkaluli);
        textshowkaluli.setText(kaluli + " ");// 卡路里
        textshowkaluli.setTypeface(fontFace);
        linearshowkaluli2 = (LinearLayout) findViewById(R.id.linearshowkaluli2);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
    }

    void init() {
        if (mMap == null) {
            setUpMapIfNeeded();
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        if (z > 1)
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(polyjing[z - 1], polywei[z - 1])));
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_items, menu);
        return true;
    }

    @SuppressLint("InlinedApi")
    public void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("记录详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyRunRecordDetail.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showPopupWindow();
                return false;
            }
        });
    }

    // 分享函数
    class MyTaskAtyRunRecord extends AsyncTask<JSONObject, Integer, Bitmap> {

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
            throws MalformedURLException, IOException {
        // api.openWXApp();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webPageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        // bmp = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
        // Bitmap bitmap = null;
        // try {
        // //加载一个网络图片
        // InputStream is = new URL(imageUrl).openStream();
        // bitmap = BitmapFactory.decodeStream(is);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        // Bitmap thumb = BitmapFactory.decodeResource(getResources(),
        // R.drawable.addphoto);
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
                                    // wechatShare(json.getString("url"),
                                    // json.getString("title"),
                                    // json.getString("content"),
                                    // json.getString("image"), 0);
                                    json.put("flag", 1);
                                    new MyTaskAtyRunRecord().execute(json);
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
                            map.put("type", "4");
                            map.put("uid", Config.getCachedUserUid(getApplicationContext()));
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
                                    // wechatShare(json.getString("url"),
                                    // json.getString("title"),
                                    // json.getString("content"),
                                    // json.getString("image"), 0);
                                    json.put("flag", 0);
                                    new MyTaskAtyRunRecord().execute(json);
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
                            map.put("type", "4");
                            map.put("uid", Config.getCachedUserUid(getApplicationContext()));
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
        popupWindow.showAtLocation(findViewById(R.id.run_record_content), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);

    }
}
