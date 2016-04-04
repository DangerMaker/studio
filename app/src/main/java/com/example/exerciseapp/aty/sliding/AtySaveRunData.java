package com.example.exerciseapp.aty.sliding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.HttpClientUploadManager;
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
//import com.tencent.mm.sdk.openapi.WXTextObject;

public class AtySaveRunData extends BaseActivity {

    MapView mMapView;
    AMap mMap;
    double[] polyjing;
    double[] polywei;
    double[] polyalti;
    int z;
    private RequestQueue mRequestQueue;
    private static IWXAPI api;
    RelativeLayout relativeshowmap, relativeshowsudu, relativeshowpingjunsudu, relativeshowkaluli, linearshowjuli, linearshowshijian;
    LinearLayout linearshowjulishijian, linearshowsuduhepingjunsudu,
            linearshowsudu1, linearshowsudu2, linearshowpingjunsudu1, linearshowpingjunsudu2,
            linearshowhaibahekaluli, linearshowkaluli1, linearshowkaluli2;
    TextView textshowjuli, textshowjulidanwei, textshowshijian, textshowshijiandanwei, textshowzuigaosudu,
            textshowpingjunsudu, textshowkaluli;
    Button btnsave, btnfangqi;
    EditText edittextbeizhu;
    long shijian;
    float juli;
    int speed;
    float kaluli;
    static String filename_;

    private Toolbar toolbar;
    private TextView pageTitle;

    private int sport_type;

    @Bind(R.id.textshowAlit)
    TextView textAlit;
    @Bind(R.id.textshowzuigaosudu)
    TextView textSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.saverundata);
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
//		initActionBar();
        setTitleBar();
        mRequestQueue = Volley.newRequestQueue(this);
        Intent i = getIntent();
        BigDecimal b = new BigDecimal(i.getFloatExtra("intentjuli", 0) / 1000.0);
        juli = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//		juli=i.getFloatExtra("intentjuli", 0);
//		 juli=Math.round((i.getFloatExtra("intentjuli", -1)/1000)*100)/100;//Math.round((i.getFloatExtra("intentjuli", -1)/1000)*100))/100
        shijian = i.getLongExtra("intentshijian", -1);
//		int speed;
//		PolylineOptions pol=i.getParcelableExtra("intentpolyline");
        polyjing = i.getDoubleArrayExtra("intentpolyjing");
        polywei = i.getDoubleArrayExtra("intentpolywei");
        polyalti = i.getDoubleArrayExtra("intentpolyalti");
        sport_type = i.getIntExtra("sport_type", 0);
        z = i.getIntExtra("z", -1);
        PolylineOptions polys = new PolylineOptions();


        if (juli == 0) speed = 0;
        else speed = (int) (shijian / juli);
        kaluli = i.getFloatExtra("intentkaluli", -1);
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/impact.ttf");
//	}
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState){
//		View view=inflater.inflate(R.layout.saverundata, container, false);
        mMapView = (MapView) findViewById(R.id.mapViewshow);
        mMapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();
        init();
        if (z > 1) {
            for (int m = 0; m < z; m++) {
                polys.add(new LatLng(polyjing[m], polywei[m]));
//			Toast.makeText(getApplicationContext(), "polyjing", Toast.LENGTH_SHORT).show();
            }
            mMap.addPolyline(polys);
        }
//		getActionBar().getCustomView().findViewById(R.id.tvPageTitleOfAll).setOnClickListener(new 
//				OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
        relativeshowmap = (RelativeLayout) findViewById(R.id.relativeshowmap);
        linearshowjulishijian = (LinearLayout) findViewById(R.id.linearshowjulishijian);
        linearshowjuli = (RelativeLayout) findViewById(R.id.linearshowjuli);
        textshowjuli = (TextView) findViewById(R.id.textshowjuli);
        textshowjuli.setText(juli + " ");   //获取并显示距离
        textshowjuli.setTypeface(fontFace);
        textshowjulidanwei = (TextView) findViewById(R.id.textshowjulidanwei);
        linearshowshijian = (RelativeLayout) findViewById(R.id.linearshowshijian);
        textshowshijian = (TextView) findViewById(R.id.textshowshijian);
        textshowshijian.setText(shijian / 3600 + ":" + (shijian - (shijian / 3600) * 3600) / 60 +
                ":" + (shijian - shijian / 3600 * 3600 - (shijian - (shijian / 3600) * 3600) / 60 * 60));//获取并显示时间
        textshowshijian.setTypeface(fontFace);
        textshowshijiandanwei = (TextView) findViewById(R.id.textshowshijiandanwei);
        linearshowsuduhepingjunsudu = (LinearLayout) findViewById(R.id.linearshowsuduhepingjunsudu);
//		relativeshowsudu=(RelativeLayout) findViewById(R.id.relativeshowsudu);
//		linearshowsudu1=(LinearLayout) findViewById(R.id.linearshowsudu1);
//		textshowzuigaosudu=(TextView) findViewById(R.id.textshowzuigaosudu);
//		textshowzuigaosudu.setTypeface(fontFace);
//		linearshowsudu2=(LinearLayout) findViewById(R.id.linearshowsudu2);
        relativeshowpingjunsudu = (RelativeLayout) findViewById(R.id.relativeshowpingjunsudu);
        linearshowpingjunsudu1 = (LinearLayout) findViewById(R.id.linearshowpingjunsudu1);
        textshowpingjunsudu = (TextView) findViewById(R.id.textshowpingjunsudu);
        edittextbeizhu = (EditText) findViewById(R.id.edittextbeizhu);
        if (speed == 0) textshowpingjunsudu.setText("0");
        else if ((int) (speed / 3600) == 0)
            textshowpingjunsudu.setText((int) ((speed - (speed / 3600) * 3600) / 60) +
                    ":" + (int) ((speed - speed / 3600 * 3600 - (speed - (speed / 3600) * 3600) / 60 * 60)));//速度
        else
            textshowpingjunsudu.setText((int) speed / 3600 + ":" + (int) (speed - (speed / 3600) * 3600) / 60 +
                    ":" + (int) (speed - speed / 3600 * 3600 - (speed - (speed / 3600) * 3600) / 60 * 60));//速度
        textshowpingjunsudu.setTypeface(fontFace);
        linearshowpingjunsudu2 = (LinearLayout) findViewById(R.id.linearshowpingjunsudu2);
        linearshowhaibahekaluli = (LinearLayout) findViewById(R.id.linearshowhaibahekaluli);
        relativeshowkaluli = (RelativeLayout) findViewById(R.id.relativeshowkaluli);
        linearshowkaluli1 = (LinearLayout) findViewById(R.id.linearshowkaluli1);
        textshowkaluli = (TextView) findViewById(R.id.textshowkaluli);
        textshowkaluli.setText(kaluli + " ");//卡路里
        textshowkaluli.setTypeface(fontFace);

        int alit = (int) (polyalti[polyalti.length - 1] - polyalti[0]);
        if (alit == 0) {
            textAlit.setText("0");
        } else {
            textAlit.setText(alit + "");
        }
        int mSpeed = (int) (juli / shijian);
        if (mSpeed == 0) textSpeed.setText("0");
        else if ((int) (mSpeed / 3600) == 0)
            textSpeed.setText((int) ((mSpeed - (mSpeed / 3600) * 3600) / 60) +
                    ":" + (int) ((mSpeed - mSpeed / 3600 * 3600 - (mSpeed - (mSpeed / 3600) * 3600) / 60 * 60)));//速度
        else
            textSpeed.setText((int) mSpeed / 3600 + ":" + (int) (mSpeed - (mSpeed / 3600) * 3600) / 60 +
                    ":" + (int) (mSpeed - mSpeed / 3600 * 3600 - (mSpeed - (mSpeed / 3600) * 3600) / 60 * 60));//速度

        linearshowkaluli2 = (LinearLayout) findViewById(R.id.linearshowkaluli2);
        btnsave = (Button) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new OnClickListener() {
            /*生成txt文件
             *
			 */
            private String note = "";

            private void initData() {
                String filePath = "/sdcard/Test/";
//			    String fileName = "log.txt";            //要改成以时间为名        
                long time = System.currentTimeMillis();
                String fileName = time + ".txt";
                filename_ = fileName;
                if (z > 1) {
                    for (int m = 0; m < z; m++) {
//					polys.add(new LatLng(polyjing[m], polywei[m]));
//					Toast.makeText(getApplicationContext(), "polyjing", Toast.LENGTH_SHORT).show();
                        writeTxtToFile(polyjing[m] + " " + polywei[m] + polyalti[m] + "\n", filePath, fileName);
                    }
                }
//			    writeTxtToFile("txt content", filePath, fileName);
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
                //保存按钮的内容

//				StringRequest  stringRequest = new StringRequest(
//	                    Request.Method.POST,
//	                    Config.SERVER_URL+"Users/submitRunData",
//	                    new Response.Listener<String>() {
//	 
//	                        @Override
//	                        public void onResponse(String s) {
//	                            try {
////	                            	progressDialog.dismiss();
//	                                JSONObject jsonObject = new JSONObject(s);
//	                                if(jsonObject.getString("result").equals("1")){
////	                                	Toast.makeText(getApplicationContext(), "上传成功", 2).show();
//shangchuanyijianDialog dl=new shangchuanyijianDialog(AtySaveRunData.this, "提示", new shangchuanyijianDialog.OnCustomDialogListener() {
//	                    					
//	                    					@Override
//	                    					public void back(String name) {
//	                    						// TODO Auto-generated method stub
//	                    						
//	                    					}
//	                    				});
//	                    				dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
//	                    				dl.show();
////	                                	new  AlertDialog.Builder(AtySaveRunData.this)    
////	                    				.setTitle("                          提示" )  
////	                    				
////	                    				.setMessage("                     保存完成" )  
////	                    				.setPositiveButton("确定" ,  new DialogInterface.OnClickListener() {
////	                    					
////	                    					@Override
////	                    					public void onClick(DialogInterface dialog, int which) {
////	                    						// TODO Auto-generated method stub
////	                    						Intent i=new Intent(getApplicationContext(),AtySlidingHome.class);
////	                    						startActivity(i);
////	                    					}
////	                    				} )  
////	                    				.show();
////	                                	Config.cacheUserUid(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_UID));
////	                                	startActivity(new Intent(AtyLogin.this,AtyPersonalize.class));
////	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), 2).show();
////	                                	Config.STATUS_FINISH_ACTIVITY = 1;
////	                                	finish();
//	                                }else{
//	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), 2).show();
//	                                }
//	                                
//	                            } catch (JSONException e) {
////	                            	progressDialog.dismiss();
//	                                e.printStackTrace();
//	                            }
//	                        }
//	                    },
//	                    new Response.ErrorListener() {
//	 
//	                        @Override
//	                        public void onErrorResponse(VolleyError volleyError) {
////	                        	progressDialog.dismiss();
//	                        	Toast.makeText(getApplicationContext(), "error", 2).show();
//	                        }
//	                    }){
//	 
//	                @Override
//	                protected Map<String, String> getParams() throws AuthFailureError {
//	                    Map<String,String> map = new HashMap<String,String>();
//	                    map.put(Config.KEY_UID, "1");
//	                    map.put(Config.KEY_DURATION,Long.toString(shijian));
//	                    //  (float)(Math.round((3.6*juli/shijian)*100))/100
//	                    map.put(Config.KEY_AVERAGESPEED,Float.toString((float)(Math.round((3.6*juli*1000/shijian)*100))/100) );
//	                    map.put(Config.KEY_CALORIE, Integer.toString((int)kaluli));
//	                    map.put(Config.KEY_DISTANCE, Float.toString(juli));
//	                    map.put(Config.KEY_REMARK, edittextbeizhu.getText().toString());
//
////	                    map.put(Config.KEY_TEL,etPhoneNum.getText().toString());
////	                    map.put(Config.KEY_PASSWORD,etPassword.getText().toString());
//	                    return map;
//	                }
//	            };
//	            mRequestQueue.add(stringRequest);
//				


                new AsyncTask<String, Void, String>() {
                    ProgressDialog progressDialog;

                    protected void onPreExecute() {
                        progressDialog = new ProgressDialog(AtySaveRunData.this);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMessage("上传中……");
                        progressDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(Config.KEY_UID, Config.getCachedUserUid(AtySaveRunData.this.getApplicationContext()));
                        map.put(Config.KEY_DURATION, Long.toString(shijian));
                        //  (float)(Math.round((3.6*juli/shijian)*100))/100
                        map.put(Config.KEY_AVERAGESPEED, Float.toString((float) (Math.round((3.6 * juli * 1000 / shijian) * 100)) / 100));
                        map.put(Config.KEY_CALORIE, Integer.toString((int) kaluli));
                        map.put(Config.KEY_DISTANCE, Float.toString(juli));
                        map.put(Config.KEY_REMARK, note);
                        int alit = (int) (polyalti[polyalti.length - 1] - polyalti[0]);
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
                        return HttpClientUploadManager.upload(Config.SERVER_URL + "Users/submitRunData", "/sdcard/Test/" + filename_, "rundata", map);
                    }

                    protected void onPostExecute(String result) {
                        progressDialog.dismiss();
                        if (result != null && !result.equals("")) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (null != jsonObject
                                        && "1".equals(jsonObject.getString("result"))) {// 上传成功
//									JSONArray jsonArray = jsonObject.getJSONArray("data");
//									JSONObject json1 = (JSONObject) jsonArray.get(0);
                                    Toast.makeText(AtySaveRunData.this, "上传成功",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
//									Config.cacheUserHwURL(getActivity().getApplicationContext(), jsonObject.getString(Config.KEY_FILE_URL));
                                }
                                Toast.makeText(AtySaveRunData.this.getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(AtySaveRunData.this, "上传失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    ;
                }.execute("");


            }
        });
        btnfangqi = (Button) findViewById(R.id.btnfangqi);
        btnfangqi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), AtySlidingHome.class);
                startActivity(i);
            }
        });
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
                AtySaveRunData.this.finish();
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
     * @param bitmap    图片地址
     * @param flag        分享到朋友还是朋友圈的flag
     */

    public void wechatShare(String webPageUrl, String title, String description, Bitmap bitmap, int flag) throws MalformedURLException, IOException {
//		    	api.openWXApp();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webPageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        //				bmp = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
//			    Bitmap bitmap = null;  
//		        try {  
//		            //加载一个网络图片  
//		            InputStream is = new URL(imageUrl).openStream();  
//		            bitmap = BitmapFactory.decodeStream(is);  
//		        } catch (Exception e) {  
//		            e.printStackTrace();  
//		        }  
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
}
