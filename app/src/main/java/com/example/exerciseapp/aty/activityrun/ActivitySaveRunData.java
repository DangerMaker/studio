package com.example.exerciseapp.aty.activityrun;

import android.app.ProgressDialog;
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
import android.util.Log;
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
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.TabMainActivity;
import com.example.exerciseapp.utils.SpeedConvert;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.HttpClientUploadManager;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import butterknife.Bind;

public class ActivitySaveRunData extends BaseActivity {

    MapView mMapView;
    AMap mMap;
    double[] polyjing;
    double[] polywei;
    double[] polyalti;
    int z;
    private RequestQueue mRequestQueue;
    private static IWXAPI api;
    RelativeLayout relativeshowmap, relativeshowpingjunsudu, relativeshowkaluli, linearshowjuli, linearshowshijian;
    LinearLayout linearshowjulishijian, linearshowsuduhepingjunsudu,
            linearshowpingjunsudu1, linearshowpingjunsudu2,
            linearshowhaibahekaluli, linearshowkaluli1, linearshowkaluli2;
    TextView textshowjuli, textshowjulidanwei, textshowshijian, textshowshijiandanwei,
            textshowpingjunsudu, textshowkaluli;
    Button btnsave, btnfangqi;
    EditText edittextbeizhu;
    long shijian;
    Double juli;
    int speed;
    float kaluli;
    static String filename_;

    private Toolbar toolbar;
    private TextView pageTitle;

    private int sport_type;
    private double max_speed;
    private double minSpeed_onAverage;

    @Bind(R.id.textshowzuigaosudu)
    TextView textSpeed;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.saverundata);
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        setTitleBar();
        mRequestQueue = Volley.newRequestQueue(this);
        Intent i = getIntent();
        juli = i.getDoubleExtra("intentjuli", 0);
        shijian = i.getLongExtra("intentshijian", -1);//重要 时间
        polyjing = i.getDoubleArrayExtra("intentpolyjing");//重要
        polywei = i.getDoubleArrayExtra("intentpolywei");//重要
        polyalti = i.getDoubleArrayExtra("intentpolyalti");//重要
        sport_type = i.getIntExtra("sport_type", 0);//重要
        max_speed = i.getDoubleExtra("max_speed", 0);//重要
        minSpeed_onAverage = i.getDoubleExtra("minSpeed_onAverage", 0);//重要
        z = polywei.length;
        if (juli == 0) speed = 0;
        else speed = (int) (shijian / juli);
        kaluli = i.getFloatExtra("intentkaluli", -1);
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/impact.ttf");
        mMapView = (MapView) findViewById(R.id.mapViewshow);
        mMapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();
        init();
        new Thread() {
            @Override
            public void run() {
                if (z > 1)
                    drawmyline();
            }
        }.start();
        relativeshowmap = (RelativeLayout) findViewById(R.id.relativeshowmap);
        linearshowjulishijian = (LinearLayout) findViewById(R.id.linearshowjulishijian);
        linearshowjuli = (RelativeLayout) findViewById(R.id.linearshowjuli);
        textshowjuli = (TextView) findViewById(R.id.textshowjuli);
        textshowjuli.setText((float) (Math.round(juli / 1000 * 100)) / 100 + "");   //获取并显示距离
        textshowjuli.setTypeface(fontFace);
        textshowjulidanwei = (TextView) findViewById(R.id.textshowjulidanwei);
        linearshowshijian = (RelativeLayout) findViewById(R.id.linearshowshijian);
        textshowshijian = (TextView) findViewById(R.id.textshowshijian);
        textshowshijian.setText(shijian / 3600 + ":" + (shijian - (shijian / 3600) * 3600) / 60 +
                ":" + (shijian - shijian / 3600 * 3600 - (shijian - (shijian / 3600) * 3600) / 60 * 60));//获取并显示时间
        textshowshijian.setTypeface(fontFace);
        textshowshijiandanwei = (TextView) findViewById(R.id.textshowshijiandanwei);
        linearshowsuduhepingjunsudu = (LinearLayout) findViewById(R.id.linearshowsuduhepingjunsudu);
        relativeshowpingjunsudu = (RelativeLayout) findViewById(R.id.relativeshowpingjunsudu);
        linearshowpingjunsudu1 = (LinearLayout) findViewById(R.id.linearshowpingjunsudu1);
        textshowpingjunsudu = (TextView) findViewById(R.id.textshowpingjunsudu);
        edittextbeizhu = (EditText) findViewById(R.id.edittextbeizhu);
        if (minSpeed_onAverage == 0) textshowpingjunsudu.setText("0");

        textshowpingjunsudu.setText((Math.round(SpeedConvert.oriToShow(minSpeed_onAverage) * 100)) / 100 + "");//速度
        textshowpingjunsudu.setTypeface(fontFace);
        linearshowpingjunsudu2 = (LinearLayout) findViewById(R.id.linearshowpingjunsudu2);
        linearshowhaibahekaluli = (LinearLayout) findViewById(R.id.linearshowhaibahekaluli);
        relativeshowkaluli = (RelativeLayout) findViewById(R.id.relativeshowkaluli);
        linearshowkaluli1 = (LinearLayout) findViewById(R.id.linearshowkaluli1);
        textshowkaluli = (TextView) findViewById(R.id.textshowkaluli);
        textshowkaluli.setText(kaluli + " ");//卡路里
        textshowkaluli.setTypeface(fontFace);
        setUpSportData();
        textSpeed.setText((Math.round(SpeedConvert.oriToShow(max_speed) * 100)) / 100 + "");
        textSpeed.setTypeface(fontFace);
        linearshowkaluli2 = (LinearLayout) findViewById(R.id.linearshowkaluli2);
        btnsave = (Button) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new OnClickListener() {
            /*生成txt文件
             *
			 */
            private String note = "";

            private void initData() {
                String filePath = "/sdcard/Test/";
                long time = System.currentTimeMillis();
                String fileName = time + ".txt";
                filename_ = fileName;
                if (z > 1) {
                    for (int m = 0; m < z; m++) {
                        writeTxtToFile(polyjing[m] + " " + polywei[m] + " " + polyalti[m] + "\n", filePath, fileName);//写入点数据，包括经度纬度海拔
                    }
                }
            }

            // 将字符串写入到文本文件中
            public void writeTxtToFile(String strcontent, String filePath, String fileName) {
                //生成文件夹之后，再生成文件，不然会出错
                makeFilePath(filePath, fileName);

                String strFilePath = filePath + fileName;
                // 每次写入时，都换行写
                String strContent = strcontent + "\r\n";
                try {
                    File file = new File(strFilePath);
                    if (!file.exists()) {
                        Log.d("TestFile", "Create the file:" + strFilePath);
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    raf.seek(file.length());
                    raf.write(strContent.getBytes());
                    raf.close();
                } catch (Exception e) {
                    Log.e("TestFile", "Error on write File:" + e);
                }
            }

            // 生成文件
            public File makeFilePath(String filePath, String fileName) {
                File file = null;
                makeRootDirectory(filePath);
                try {
                    file = new File(filePath + fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return file;
            }

            // 生成文件夹
            public void makeRootDirectory(String filePath) {
                File file = null;
                try {
                    file = new File(filePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                } catch (Exception e) {
                    Log.i("error:", e + "");
                }
            }

            @Override
            public void onClick(View v) {
                if (z > 1)
                    initData();
                // TODO Auto-generated method stub
                note = edittextbeizhu.getText().toString();
                new AsyncTask<String, Void, String>() {
                    ProgressDialog progressDialog;

                    protected void onPreExecute() {
                        progressDialog = new ProgressDialog(ActivitySaveRunData.this);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMessage("上传中……");
                        progressDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Config.KEY_UID, Config.getCachedUserUid(ActivitySaveRunData.this.getApplicationContext()));
                        map.put(Config.KEY_DURATION, Long.toString(shijian));
                        map.put(Config.KEY_AVERAGESPEED, minSpeed_onAverage + "");
                        map.put(Config.KEY_CALORIE, Integer.toString((int) kaluli));
                        map.put(Config.KEY_DISTANCE, Double.toString(juli));
                        map.put(Config.KEY_REMARK, note);
                        int alit = (int) (polyalti[z - 1] - polyalti[0]);
                        if (alit == 0) {
                            map.put(Config.KEY_ALTI, "0");
                        } else {
                            map.put(Config.KEY_ALTI, alit + "");
                        }
                        int pace = (int) (juli / 0.5);
                        if (pace == 0) {
                            map.put(Config.KEY_PACE, "0");
                        } else {
                            map.put(Config.KEY_PACE, pace + "");
                        }
                        map.put("sport_type", sport_type + "");
                        map.put("max_speed", max_speed + "");
                        return HttpClientUploadManager.upload(Config.SERVER_URL + "Users/submitRunData", "/sdcard/Test/" + filename_, "rundata", map);
                    }

                    protected void onPostExecute(String result) {
                        progressDialog.dismiss();
                        if (result != null && !result.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (null != jsonObject
                                        && "1".equals(jsonObject.getString("result"))) {// 上传成功
                                    Toast.makeText(ActivitySaveRunData.this, "上传成功",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setClass(ActivitySaveRunData.this, TabMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                Toast.makeText(ActivitySaveRunData.this.getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ActivitySaveRunData.this, "上传失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                }.execute("");
            }
        });
        btnfangqi = (Button) findViewById(R.id.btnfangqi);
        btnfangqi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), TabMainActivity.class);
                startActivity(i);
            }
        });
    }

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

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
    }

    void init() {
        if (mMap == null) {
            setUpMapIfNeeded();
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
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

    public void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("保存数据");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySaveRunData.this.finish();
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

    //分享函数
    class MyTask extends AsyncTask<JSONObject, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            Bitmap bitmap = null;
            try {
                String url = json.getString("image");
                if (url == null || url.equals("")) {
                    return null;
                }
                //加载一个网络图片
                InputStream is = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(is);
                wechatShare(json.getString("url"), json.getString("title"), json.getString("content"), bitmap, json.getInt("flag"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }

    /**
     * @param webPageUrl  需要跳转的链接
     * @param title       分享标题
     * @param description 分享内容
     * @param bitmap      图片地址
     * @param flag        分享到朋友还是朋友圈的flag
     */

    public void wechatShare(String webPageUrl, String title, String description, Bitmap bitmap, int flag) throws IOException {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webPageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        Bitmap thumbBmp = null;
        if (bitmap == null) {
            thumbBmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_of_app);
        } else {
            thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        }
        //		    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.addphoto);
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
            popupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            //分享到朋友圈
            view.findViewById(R.id.btnWxFriends).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Config.SERVER_URL + "Users/shareFunc",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        if (jsonObject.getString("result").equals("1")) {
                                            JSONObject json = jsonObject.getJSONObject("data");
                                            //													wechatShare(json.getString("url"), json.getString("title"), json.getString("content"), json.getString("image"), 0);
                                            json.put("flag", 1);
                                            new MyTask().execute(json);
                                        } else {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

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
            //分享到朋友
            view.findViewById(R.id.btnWxTimeLine).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Config.SERVER_URL + "Users/shareFunc",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        if (jsonObject.getString("result").equals("1")) {
                                            JSONObject json = jsonObject.getJSONObject("data");
                                            //													wechatShare(json.getString("url"), json.getString("title"), json.getString("content"), json.getString("image"), 0);
                                            json.put("flag", 0);
                                            new MyTask().execute(json);
                                        } else {
                                            Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

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
//		            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        popupWindow.showAtLocation(findViewById(R.id.save_content), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);


    }

    public void drawmyline() {

        LinkedList<PolylineOptions> mypolys = new LinkedList<PolylineOptions>();
        double expected = 15.0;
        int k = 0;
        double temp = 0.0;

        LatLng firstm = new LatLng(polyjing[0], polywei[0]);
        for (int m = 0; m < z; m++) {
            LatLng secondm = new LatLng(polyjing[m], polywei[m]);
            double temp1 = temp;
            if (m == 0) {
                PolylineOptions polys = new PolylineOptions();
                mypolys.add(polys);
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(secondm)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.goscc))
                        .draggable(false));
                marker.showInfoWindow();
            }
            if (m == z - 1) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(secondm)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.endscc))
                        .draggable(false));
                marker.showInfoWindow();
            }
            temp = AMapUtils.calculateLineDistance(firstm, secondm);
            if (m == 0 || temp < expected
                    || ((temp - temp1) < expected && (temp1 - temp) < expected)) {

                mypolys.get(k).add(secondm);
                firstm = secondm;
            } else {
                PolylineOptions polys = new PolylineOptions();
                mypolys.add(polys);
                k++;

            }
        }
        for (int j = 0; j < mypolys.size(); j++)
            mMap.addPolyline(mypolys.get(j));

    }
}
