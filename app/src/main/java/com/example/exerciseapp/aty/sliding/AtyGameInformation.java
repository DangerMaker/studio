package com.example.exerciseapp.aty.sliding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
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
import com.example.exerciseapp.R.color;
import com.example.exerciseapp.aty.sliding.AtyAssocOrClubInformation.MyTask;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

public class AtyGameInformation extends BaseActivity {

	private String gameId = null;
	private JSONObject jsonObj = null;
	private String gameName = null;
	private String time = null;

	private ImageView ivFrontImageGameInformation; // 封面图片
	private TextView tvGameSponserGameInformation;// 主办单位
	private TextView tvGameOrganizerGameInformation;// 承办单位
	private TextView tvCountNowGameInformation; // 已报名人数
	private Button btnFocusGameInformation; // 关注按钮
	private TextView tvGameTitleGameInformation; // 赛事名字
	private TextView tvGameStartTimeGameInformation;// 赛事发布时间
	private TextView tvGameTimeGameInformation; // 赛事活动时间

	private TextView tvGameJoinDateGameInformation; // 赛事报名日期
	private TextView tvGameLocationGameInformation; // 赛事活动地址
	private TextView tvGameParticipantNumGameInformation;// 赛事参加人数
	private TextView tvGamePayFeeGameInformation; // 赛事需要支付费用
	private TextView tvGameIntroGameInformation; // 赛事活动介绍
	private ImageView ivBriefImageGameInformation; // 赛事简介图片
	private Button btnIWannaJoin; // 我要报名按钮

	private PopupWindow popTitleMenu;
	private View layoutTitle;
	private ListView lvMenuListTitle;
	private List<Map<String, String>> listMenuTitle;
	private RequestQueue mRequestQueue;
	private static IWXAPI api;

	private String agreement;

	private ProgressDialog progressDialog;

	private Toolbar toolbar;
	private TextView pageTitle;

	private Drawable arrowUp;
	private Drawable arrowDown;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
		api.registerApp(Config.WxAPP_ID);
		mRequestQueue = Volley.newRequestQueue(this);
		JSONArray jsonArr = Config.getCachedGameList(getApplicationContext());
		if (jsonArr == null) {
			return;
		}
		gameId = getIntent().getStringExtra(Config.KEY_GAME_ID);
		try {
			for (int i = 0; i < jsonArr.length(); i++) {
				if (jsonArr.getJSONObject(i).getString(Config.KEY_GAME_ID).equals(gameId)) {
					jsonObj = jsonArr.getJSONObject(i);
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		setContentView(R.layout.aty_game_information);
		// TODO
		ivFrontImageGameInformation = (ImageView) findViewById(R.id.ivFrontImageGameInformation);

		tvGameSponserGameInformation = (TextView) findViewById(R.id.tvGameSponserGameInformation);
		tvGameOrganizerGameInformation = (TextView) findViewById(R.id.tvGameOrganizerGameInformation);
		tvCountNowGameInformation = (TextView) findViewById(R.id.tvCountNowGameInformation);

		btnFocusGameInformation = (Button) findViewById(R.id.btnFocusGameInformation);
		tvGameTitleGameInformation = (TextView) findViewById(R.id.tvGameTitleGameInformation);
		tvGameStartTimeGameInformation = (TextView) findViewById(R.id.tvGameStartTimeGameInformation);
		tvGameTimeGameInformation = (TextView) findViewById(R.id.tvGameTimeGameInformation);
		tvGameJoinDateGameInformation = (TextView) findViewById(R.id.tvGameJoinDateGameInformation);
		tvGameLocationGameInformation = (TextView) findViewById(R.id.tvGameLocationGameInformation);
		tvGameParticipantNumGameInformation = (TextView) findViewById(R.id.tvGameParticipantNumGameInformation);
		tvGamePayFeeGameInformation = (TextView) findViewById(R.id.tvGamePayFeeGameInformation);
		tvGameIntroGameInformation = (TextView) findViewById(R.id.tvGameIntroGameInformation);
		ivBriefImageGameInformation = (ImageView) findViewById(R.id.ivBriefImageGameInformation);
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

		// 关注按钮
		// btnFocusGameInformation.setOnClickListener(new OnClickListener() {
		//
		// @SuppressLint("NewApi") @Override
		// public void onClick(View v) {
		// if(!Config.STATUS_HAS_FOCUSED){
		// Config.STATUS_HAS_FOCUSED = true;
		// btnFocusGameInformation.setBackground(AtyGameInformation.this.getResources().getDrawable(R.drawable.btn_has_focus_bg));
		// }else{
		// Config.STATUS_HAS_FOCUSED = false;
		// btnFocusGameInformation.setBackground(AtyGameInformation.this.getResources().getDrawable(R.drawable.btnfocusbg));
		// }
		// }
		// });

		if (btnIWannaJoin.isClickable()) {
			btnIWannaJoin.setOnClickListener(new OnClickListener() {
				JSONObject jsonObj;

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
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
									Intent intent = new Intent(AtyGameInformation.this, AtyEntryForm.class);
									intent.putExtra(Config.KEY_GAME_ID, gameId);
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
			});
		}

		if (jsonObj != null) {
			try {
				if (jsonObj.getString(Config.KEY_GAME_STATUS_ID).equals("0")
						|| jsonObj.getString(Config.KEY_GAME_STATUS_ID).equals("2")) {
					btnIWannaJoin.setClickable(false);
					btnIWannaJoin.setBackgroundColor(Color.GRAY);
					if (jsonObj.getString(Config.KEY_GAME_STATUS_ID).equals("0")) {
						btnIWannaJoin.setText("报名未开始");
					} else {
						btnIWannaJoin.setText("报名结束");
					}
				}
				agreement = jsonObj.getString("agreement");
				Picasso.with(AtyGameInformation.this).load(jsonObj.getString(Config.KEY_GAME_FRONT_PAGE))
						.into(ivFrontImageGameInformation);
				Picasso.with(AtyGameInformation.this).load(jsonObj.getString(Config.KEY_GAME_INTRO))
						.into(ivBriefImageGameInformation);
				if (jsonObj.getString(Config.KEY_GAME_SPONSER) == null
						|| jsonObj.getString(Config.KEY_GAME_SPONSER).equals("")) {
					tvGameSponserGameInformation.setText("");
				}
				if (jsonObj.getString(Config.KEY_GAME_ORGANIZER) == null
						|| jsonObj.getString(Config.KEY_GAME_ORGANIZER).equals("")) {
					tvGameOrganizerGameInformation.setText("");
				}
				tvGameSponserGameInformation.setText("主办单位：" + jsonObj.getString(Config.KEY_GAME_SPONSER));
				tvGameOrganizerGameInformation.setText("承办单位：" + jsonObj.getString(Config.KEY_GAME_ORGANIZER));
				tvCountNowGameInformation.setText("已参加人数：" + jsonObj.getString(Config.KEY_GAME_COUNT_NOW));
				tvGameTitleGameInformation.setText(jsonObj.getString(Config.KEY_GAME_NAME));
				gameName = jsonObj.getString(Config.KEY_GAME_NAME);
				tvGameStartTimeGameInformation.setText("发布时间：" + jsonObj.getString(Config.KEY_GAME_OUT_TIME));
				tvGameTimeGameInformation.setText("活动时间：" + jsonObj.getString(Config.KEY_GAME_START_TIME) + "-"
						+ jsonObj.getString(Config.KEY_GAME_END_TIME));
				time = jsonObj.getString(Config.KEY_GAME_START_TIME);
				tvGameJoinDateGameInformation.setText("报名时间：" + jsonObj.getString(Config.KEY_GAME_ACCEPT_START) + "-"
						+ jsonObj.getString(Config.KEY_GAME_ACCEPT_END));
				tvGameLocationGameInformation.setText(jsonObj.getString(Config.KEY_GAME_POSITION));
				tvGameParticipantNumGameInformation.setText("参加人数：" + jsonObj.getString(Config.KEY_GAME_MAX_ATTEND));
				if (jsonObj.getString(Config.KEY_GAME_PAY_FEE).equals("0")) {

					tvGamePayFeeGameInformation.setText("费用:公益活动（免费）");
				} else {
					tvGamePayFeeGameInformation.setText("费用:" + jsonObj.getString(Config.KEY_GAME_PAY_FEE));
				}
				String intro = "活动介绍：" + "\n\t\t" + jsonObj.getString(Config.KEY_GAME_BIREF_INTRO);
				tvGameIntroGameInformation.setText(intro);
				// 图片都没弄TODO
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		setTitleBar();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_toolbar_items, menu);
		return true;
	}

	private void setTitleBar() {
		arrowUp = ContextCompat.getDrawable(AtyGameInformation.this.getBaseContext(), R.drawable.arrowup);
		arrowUp.setBounds(0, 0, 32, 32);
		arrowDown = ContextCompat.getDrawable(AtyGameInformation.this.getBaseContext(), R.drawable.arrowdown);
		arrowDown.setBounds(0, 0, 32, 32);

		toolbar = (Toolbar) findViewById(R.id.toolbar_withpopwin);
		pageTitle = (TextView) findViewById(R.id.toolbar_withpopwin_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("赛事活动信息");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		pageTitle.setCompoundDrawables(null, null, arrowDown, null);

		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyGameInformation.this.finish();
			}
		});
		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showPopupWindow();
				return false;
			}
		});
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
			layoutTitle = getLayoutInflater().inflate(R.layout.game_information_title_menu_list, null);
			lvMenuListTitle = (ListView) layoutTitle.findViewById(R.id.titleMenuListGameInformation);
			SimpleAdapter listAdapter = new SimpleAdapter(AtyGameInformation.this, listMenuTitle,
					R.layout.game_information_title_tab_menu_list_item, new String[] { "item" },
					new int[] { R.id.titleMenuItemGameInformation });
			lvMenuListTitle.setAdapter(listAdapter);

			// 点击listview中item的处理
			lvMenuListTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// String strItem = listMenuTitle.get(arg2).get(
					// "item");
					// tvTitle.setText(strItem);

					switch (arg2) {
					case 0:
						Toast.makeText(AtyGameInformation.this, "暂无往届回顾", Toast.LENGTH_SHORT).show();
						break;
					case 1:
						StringRequest stringRequest = new StringRequest(Request.Method.POST,
								Config.SERVER_URL + "Game/gameLive", new Response.Listener<String>() {

							@Override
							public void onResponse(String s) {
								try {
									JSONObject jsonObject = new JSONObject(s);
									if (jsonObject.getInt("result") == 1) {
										Intent intent = new Intent(AtyGameInformation.this, AtyLocalScene.class);
										intent.putExtra(Config.KEY_GAME_NAME, gameName);
										Date date = new Date();
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										intent.putExtra(Config.KEY_GAME_START_TIME, sdf.format(date));
										intent.putExtra("information", jsonObject.getString("data"));
										intent.putExtra("gid", gameId);
										startActivity(intent);
									} else {

									}

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError volleyError) {
								Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
							}
						}) {

							@Override
							protected Map<String, String> getParams() throws AuthFailureError {
								Map<String, String> map = new HashMap<String, String>();
								map.put(Config.KEY_GAME_ID, gameId);
								return map;
							}
						};
						mRequestQueue.add(stringRequest);
						break;
					case 2:
						StringRequest stringRequestGradeReport = new StringRequest(Request.Method.POST,
								Config.SERVER_URL + "Game/scoreShow", new Response.Listener<String>() {

							@Override
							public void onResponse(String s) {
								try {
									JSONObject jsonObject = new JSONObject(s);
									if (jsonObject.getInt("result") == 1) {
										Intent intent = new Intent(AtyGameInformation.this, AtyGradeReport.class);
										intent.putExtra(Config.KEY_GAME_NAME, gameName);
										intent.putExtra(Config.KEY_GAME_START_TIME, time);
										intent.putExtra("gid", gameId);
										intent.putExtra("information", jsonObject.getString("data"));
										startActivity(intent);
									} else {

									}

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError volleyError) {
								Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
							}
						}) {

							@Override
							protected Map<String, String> getParams() throws AuthFailureError {
								Map<String, String> map = new HashMap<String, String>();
								map.put(Config.KEY_GAME_ID, gameId);
								return map;
							}
						};
						mRequestQueue.add(stringRequestGradeReport);
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

			popTitleMenu = new PopupWindow(layoutTitle, pageTitle.getWidth(),
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
			popTitleMenu.showAsDropDown(pageTitle, 0,
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
		// Bitmap thumb =
		// BitmapFactory.decodeResource(AtyGameInformation.this.getResources(),
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
}
