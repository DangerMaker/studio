package com.example.exerciseapp.aty.sliding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.exerciseapp.aty.sliding.AtyGameInformation.MyTask;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

public class AtyNewsDetails extends BaseActivity {

	private TextView tvNewsTitle;
	private ImageView ivNewsPic;
	private TextView tvAssocName;
	private TextView tvDateNews;
	private TextView tvContent;

	private ActionBar actionBar;
	private RequestQueue mRequestQueue;
	private static IWXAPI api;
	private String id = null;

	private Toolbar toolbar;
	private TextView pageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
		api.registerApp(Config.WxAPP_ID);
		mRequestQueue = Volley.newRequestQueue(this);
		setContentView(R.layout.aty_news_details);
		initView();
		// initActionBar();
		setTitleBar();
		try {
			JSONObject json = new JSONObject(getIntent().getStringExtra("newsDetails"));
			tvNewsTitle.setText(json.getString(Config.KEY_TITLE));
			Picasso.with(this).load(json.getString(Config.KEY_PIC)).into(ivNewsPic);
			tvAssocName.setText(json.getString(Config.KEY_ANAME));
			tvDateNews.setText(json.getString(Config.KEY_DATE));
			tvContent.setText("\t\t" + json.getString(Config.KEY_CONTENT));
			id = json.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_toolbar_items, menu);
		return true;
	}

	private void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("新闻详情");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyNewsDetails.this.finish();
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

	private void initView() {
		tvNewsTitle = (TextView) findViewById(R.id.tvNewsTitle);
		ivNewsPic = (ImageView) findViewById(R.id.ivNewsPic);
		tvAssocName = (TextView) findViewById(R.id.tvAssocName);
		tvDateNews = (TextView) findViewById(R.id.tvDateNews);
		tvContent = (TextView) findViewById(R.id.tvContent);
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

	/**
	 * 
	 * @param webPageUrl
	 *            需要跳转的链接
	 * @param title
	 *            分享标题
	 * @param description
	 *            分享内容
	 * @param imageUrl
	 *            图片地址
	 * @param flag
	 *            分享到朋友还是朋友圈的flag
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
							map.put("type", "3");
							map.put("id", id);
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
							map.put("type", "3");
							map.put("id", id);
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
		popupWindow.showAtLocation(findViewById(R.id.news_content), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);

	}
}
