package com.example.exerciseapp.aty.sliding;

/*
 * 主界面的主页面，可左滑
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.fragment.ClubFragment;
import com.example.exerciseapp.fragment.ConfigFragment;
import com.example.exerciseapp.fragment.GameListFragment_;
import com.example.exerciseapp.fragment.MyListFragment;
import com.example.exerciseapp.fragment.NewsFragment;
import com.example.exerciseapp.fragment.PersonalCenterFragment;
import com.example.exerciseapp.fragment.StartRunFragment;
import com.example.exerciseapp.view.SlidingMenu;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

public class AtySlidingHome extends BaseActivity {
	private Fragment mContent;
	private Activity activity = this;
	private GameListFragment_ gameListFragment = null;
	private ClubFragment clubFragment = null;
	private ConfigFragment configFragment = null;
	private MyListFragment myListFragment = null;
	private NewsFragment newsFragment = null;
	private PersonalCenterFragment personalCenterFragment = null;
	private StartRunFragment startRunFragment = null;
	public static Activity instance;
	private RequestQueue mRequestQueue;
	private static IWXAPI api;
	private long exitTime = 0;

	private Toolbar toolbar;
	private View menu, item_gamelist, item_run, item_club, item_person, item_news, item_setting;
	protected SlidingMenu slidingMenu;
	private FragmentManager fragmentManager;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mRequestQueue = Volley.newRequestQueue(this);
		instance = this;
		api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
		api.registerApp(Config.WxAPP_ID);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		PushAgent.getInstance(getApplicationContext()).onAppStart();

		initView();
		setTitleBar();
		fragmentManager = getSupportFragmentManager();
		setNavigateSelected();

		boolean isMessagePush = true;// 不开启就设置为false;
		if (isMessagePush) {
			startService(new Intent(this, com.example.exerciseapp.service.MessageService.class));
		}
		;

	}

	private void setNavigateSelected() {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (mContent == null) {
			if (startRunFragment == null) {
				startRunFragment = new StartRunFragment();
				mContent = startRunFragment;
			}
		}
		transaction.replace(R.id.content_frame, mContent).commit();
		initActionBar(Config.PAGE_TAG_START_RUNNING);
	}

	public void toggle() {
		slidingMenu.toggle();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.main_toolbar_items, menu);
	// return true;
	// }

	private void setTitleBar() {
		menu.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
//		toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				showPopupWindow();
//				return false;
//			}
//		});
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
				return;
			}
		});
	}

	private void initView() {
		menu = findViewById(R.id.main_menu_layout);
		item_gamelist = findViewById(R.id.menu_gamelist);
		item_run = findViewById(R.id.menu_startrun);
		item_club = findViewById(R.id.menu_club);
		item_person = findViewById(R.id.menu_personal);
		item_news = findViewById(R.id.menu_news);
		item_setting = findViewById(R.id.menu_setting);
		slidingMenu = (SlidingMenu) findViewById(R.id.start_content);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		
		item_gamelist.setOnClickListener(menuItemListener);
		item_run.setOnClickListener(menuItemListener);
		item_club.setOnClickListener(menuItemListener);
		item_person.setOnClickListener(menuItemListener);
		item_news.setOnClickListener(menuItemListener);
		item_setting.setOnClickListener(menuItemListener);
	}

	private OnClickListener menuItemListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			hideFragments(transaction);
			switch (v.getId()) {
				case R.id.menu_gamelist:
					if (null == gameListFragment) {
						gameListFragment = new GameListFragment_();
						transaction.add(R.id.content_frame, gameListFragment);
					} else {
						transaction.show(gameListFragment);
					}
					mContent = gameListFragment;
					initActionBar(Config.PAGE_TAG_COMPETETION_ACTIVIES);
					break;
				case R.id.menu_startrun:
					if (null == startRunFragment) {
						startRunFragment = new StartRunFragment();
						transaction.add(R.id.content_frame, startRunFragment);
					} else {
						transaction.show(startRunFragment);
					}
					mContent = startRunFragment;
					initActionBar(Config.PAGE_TAG_START_RUNNING);
					break;
				case R.id.menu_club:
					if (null == clubFragment) {
						clubFragment = new ClubFragment();
						transaction.add(R.id.content_frame, clubFragment);
					} else {
						transaction.show(clubFragment);
					}
					initActionBar(Config.PAGE_TAG_CLUB);
					mContent = clubFragment;
					break;
				case R.id.menu_personal:
					if (null == personalCenterFragment) {
						personalCenterFragment = new PersonalCenterFragment();
						transaction.add(R.id.content_frame, personalCenterFragment);
					} else {
						transaction.show(personalCenterFragment);
					}
					initActionBar(Config.PAGE_TAG_PERSONAL_CENTER);
					mContent = personalCenterFragment;
					break;
				case R.id.menu_news:
					if (null == newsFragment) {
						newsFragment = new NewsFragment();
						transaction.add(R.id.content_frame, newsFragment);
					} else {
						transaction.show(newsFragment);
					}
					initActionBar(Config.PAGE_TAG_NEWS);
					mContent = newsFragment;
					break;
				case R.id.menu_setting:
					if (null == configFragment) {
						configFragment = new ConfigFragment();
						transaction.add(R.id.content_frame, configFragment);
					} else {
						transaction.show(configFragment);
					}
					initActionBar(Config.PAGE_TAG_CONFIG);
					mContent = configFragment;
					break;
					
				default:
					break;
			}
			transaction.commit();
			toggle();
		}

	};

	private void hideFragments(FragmentTransaction transaction) {
		if (gameListFragment != null) {
			transaction.hide(gameListFragment);
		}
		if (clubFragment != null) {
			transaction.hide(clubFragment);
		}
		if (configFragment != null) {
			transaction.hide(configFragment);
		}
		if (myListFragment != null) {
			transaction.hide(myListFragment);
		}
		if (newsFragment != null) {
			transaction.hide(newsFragment);
		}
		if (personalCenterFragment != null) {
			transaction.hide(personalCenterFragment);
		}
		if (startRunFragment != null) {
			transaction.hide(startRunFragment);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		TextView textview;
		textview = (TextView) activity.findViewById(R.id.toolbar_text);
		if (Config.SHOW_NEWS_FRAGMENT) {
			textview.setText("新闻资讯");
			if (newsFragment == null) {
				newsFragment = new NewsFragment();
			}
			if (!newsFragment.isAdded()) {
				getSupportFragmentManager().beginTransaction().hide(mContent).add(R.id.content_frame, newsFragment)
						.commit();
			} else {
				getSupportFragmentManager().beginTransaction().hide(mContent).show(newsFragment).commit();
			}
			Config.SHOW_NEWS_FRAGMENT = false;
			return;
		}
		if (Config.SHOW_GAME_LIST_FRAGMENT) {
			textview.setText("赛事活动");
			if (gameListFragment == null) {
				gameListFragment = new GameListFragment_();
			}
			if (!gameListFragment.isAdded()) {
				getSupportFragmentManager().beginTransaction().hide(mContent).add(R.id.content_frame, gameListFragment)
						.commit();
			} else {
				getSupportFragmentManager().beginTransaction().hide(mContent).show(gameListFragment).commit();
			}
			Config.SHOW_GAME_LIST_FRAGMENT = false;
			return;
		}

	}

	public void initActionBar(int clickPosition) {
		TextView textview;
		textview = (TextView) activity.findViewById(R.id.toolbar_text);
		switch (clickPosition) {
		// case Config.PAGE_TAG_ABOUT_US:
		// newContent = new PageFragment(Config.PAGE_TAG_ABOUT_US);
		// break;
		case Config.PAGE_TAG_COMPETETION_ACTIVIES:
			textview.setText("赛事活动");
			break;
		case Config.PAGE_TAG_START_RUNNING:
			// textview = (TextView)
			// actionBar.getCustomView().findViewById(R.id.tvPageTitleOfAll);
			textview.setText("开始跑步");
			break;
		// case Config.PAGE_TAG_MY_LIST:
		// textview = (TextView)
		// actionBar.getCustomView().findViewById(R.id.tvPageTitleOfAll);
		// textview.setText("我的订单");
		// break;
		case Config.PAGE_TAG_PERSONAL_CENTER:
			// textview = (TextView)
			// actionBar.getCustomView().findViewById(R.id.tvPageTitleOfAll);
			if (Config.TOURIST_MODE) {

			} else {
				textview.setText("个人中心");
			}
			break;
		case Config.PAGE_TAG_CLUB:
			// textview = (TextView)
			// actionBar.getCustomView().findViewById(R.id.tvPageTitleOfAll);
			textview.setText("协会俱乐部");
			break;
		case Config.PAGE_TAG_NEWS:
			// textview = (TextView)
			// actionBar.getCustomView().findViewById(R.id.tvPageTitleOfAll);
			textview.setText("新闻资讯");
			break;
		case Config.PAGE_TAG_CONFIG:
			// textview = (TextView)
			// actionBar.getCustomView().findViewById(R.id.tvPageTitleOfAll);
			textview.setText("设置");
			break;
		default:
			break;
		}
		// actionBar.getCustomView().findViewById(R.id.ivBackBtnStartRunning).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// toggle();
		// }
		// });
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			if (AtyWelcome.instance != null) {
				AtyWelcome.instance.finish();
			}
			finish();
			System.exit(0);
		}
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
							map.put("type", "1");
							map.put("gid", "9");
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
							map.put("gid", "9");
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
		popupWindow.showAtLocation(findViewById(R.id.start_content), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
	}

}
