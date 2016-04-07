package com.example.exerciseapp.aty.sliding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class AtyAssocOrClubInformation extends BaseActivity {
    private String aId = null;
    private JSONObject jsonObj = null;
    private Button btnApplyToEntry;
    private PopupWindow popTitleMenu;
    private View layoutTitle;
    private ListView lvMenuListTitle;
    private List<Map<String, String>> listMenuTitle;
    private String agreement;
    private RequestQueue mRequestQueue;
    private static IWXAPI api;

    private Toolbar toolbar;
    private TextView pageTitle;
    private Drawable arrowUp;
    private Drawable arrowDown;
    private JSONObject info;
    @Bind(R.id.assoc_info_ht5)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mRequestQueue = Volley.newRequestQueue(this);
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        setContentView(R.layout.aty_club_or_assoc_information);
        initView();
        initActionBar();
        try {
            info = new JSONObject(getIntent().getStringExtra(Config.KEY_ASSOC_INFO));
            aId = info.getString("aid");
            webView.loadUrl(info.getString(Config.KEY_AINTRO));
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            agreement = info.getString("agreement");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnApplyToEntry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AtyAssocOrClubInformation.this, AtyAssocEntryForm.class);
                intent.putExtra(Config.KEY_AID, aId);
                try {
                    intent.putExtra(Config.KEY_ANAME, info.getString(Config.KEY_ANAME));
                    intent.putExtra("agreement", agreement);
                    intent.putExtra(Config.KEY_ASSOC_INFO, info.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        btnApplyToEntry = (Button) findViewById(R.id.btnApplyToEntry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_items, menu);
        return true;
    }

    private void initActionBar() {
        arrowUp = ContextCompat.getDrawable(AtyAssocOrClubInformation.this.getBaseContext(), R.drawable.arrowup);
        arrowUp.setBounds(0, 0, 32, 32);
        arrowDown = ContextCompat.getDrawable(AtyAssocOrClubInformation.this.getBaseContext(), R.drawable.arrowdown);
        arrowDown.setBounds(0, 0, 32, 32);

        listMenuTitle = new ArrayList<Map<String, String>>();
        HashMap<String, String> mapTemp = new HashMap<String, String>();
        mapTemp.put("item", "赛事活动");
        listMenuTitle.add(mapTemp);
        HashMap<String, String> mapTemp1 = new HashMap<String, String>();
        mapTemp1.put("item", "资讯");
        listMenuTitle.add(mapTemp1);

        toolbar = (Toolbar) findViewById(R.id.toolbar_withpopwin);
        pageTitle = (TextView) findViewById(R.id.toolbar_withpopwin_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("机构信息");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        pageTitle.setCompoundDrawables(null, null, arrowDown, null);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyAssocOrClubInformation.this.finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showPopupWindow();
                return false;
            }
        });

        //点击标题下拉菜单
        pageTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });
    }

    private void showPopMenu() {
        if (popTitleMenu != null && popTitleMenu.isShowing()) {
            pageTitle.setCompoundDrawables(null, null, arrowDown, null);
            popTitleMenu.dismiss();
        } else {
            pageTitle.setCompoundDrawables(null, null, arrowUp, null);
            layoutTitle = getLayoutInflater().inflate(
                    R.layout.game_information_title_menu_list, null);
            lvMenuListTitle = (ListView) layoutTitle
                    .findViewById(R.id.titleMenuListGameInformation);
            SimpleAdapter listAdapter = new SimpleAdapter(
                    AtyAssocOrClubInformation.this, listMenuTitle, R.layout.game_information_title_tab_menu_list_item,
                    new String[]{"item"},
                    new int[]{R.id.titleMenuItemGameInformation});
            lvMenuListTitle.setAdapter(listAdapter);

            // 点击listview中item的处理
            lvMenuListTitle
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0,
                                                View arg1, int arg2, long arg3) {
//									String strItem = listMenuTitle.get(arg2).get(
//										"item");
//									tvTitle.setText(strItem);
//								
                            switch (arg2) {
                                case 0:
                                    Toast.makeText(AtyAssocOrClubInformation.this, "暂无赛事信息", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Intent i = new Intent(AtyAssocOrClubInformation.this, AtyTheAssocNews.class);
                                    Config.aId = aId;
                                    i.putExtra(Config.KEY_AID, aId);
                                    startActivity(i);
                                    break;
                                default:
                                    break;
                            }
                            if (popTitleMenu != null && popTitleMenu.isShowing()) {
                                pageTitle.setCompoundDrawables(null, null, arrowDown, null);
                                popTitleMenu.dismiss();
                            }
                        }
                    });

            popTitleMenu = new PopupWindow(layoutTitle, pageTitle.getWidth() * 2,
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
            popTitleMenu.showAsDropDown(pageTitle, -(pageTitle.getWidth() / 2),
                    (topBarHeight - pageTitle.getHeight()) / 2);

            popTitleMenu.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // 如果点击了popupwindow的外部，popupwindow也会消失
                    pageTitle.setCompoundDrawables(null, null, arrowDown, null);
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        popTitleMenu.dismiss();
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    //分享函数
    class MyTask extends AsyncTask<JSONObject, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            Bitmap bitmap = null;
            try {
                String url = json.getString("image");
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
    //分享函数

    /**
     * @param webPageUrl  需要跳转的链接
     * @param title       分享标题
     * @param description 分享内容
     * @param bitmap      图片地址
     * @param flag        分享到朋友还是朋友圈的flag
     */

    public void wechatShare(String webPageUrl, String title, String description, Bitmap bitmap, int flag) throws MalformedURLException, IOException {
//	    	api.openWXApp();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webPageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        //				bmp = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
//		    Bitmap bitmap = null;  
//	        try {  
//	            //加载一个网络图片  
//	            InputStream is = new URL(imageUrl).openStream();  
//	            bitmap = BitmapFactory.decodeStream(is);  
//	        } catch (Exception e) {  
//	            e.printStackTrace();  
//	        }  
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        //		    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.addphoto);
        msg.setThumbImage(thumbBmp);
        thumbBmp.recycle();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

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
                            map.put("type", "0");
                            map.put("aId", aId);
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
                            map.put("type", "0");
                            map.put("aId", aId);
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
//	            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        popupWindow.showAtLocation(findViewById(R.id.club_or_assoc_content), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);


    }

}
