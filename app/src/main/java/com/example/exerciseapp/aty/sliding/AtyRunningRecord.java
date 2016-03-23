package com.example.exerciseapp.aty.sliding;

/**
 * 跑步历史记录
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.view.se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import com.example.exerciseapp.view.se.emilsjolander.stickylistheaders.StickyListHeadersListView;
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

public class AtyRunningRecord extends BaseActivity {
	private RequestQueue mRequestQueue;
	private static IWXAPI api;
	static String result = "结果：";
	static String[] countryyy = new String[10000];
	static int intcountry = 0;
	// private TextView t1;
	MyAdapter adapter;
	private LinkedList<JSONObject> list = new LinkedList<JSONObject>();
	// private LinkedList<JSONObject> listItem = new LinkedList<JSONObject>();
	
	private Toolbar toolbar;
	private TextView pageTitle;

	private class myTaskAtyRunningRecord extends AsyncTask<JSONObject, String, JSONObject> {

		@Override
		protected JSONObject doInBackground(JSONObject... params) {

			// 要下载的文件路径
			String urlDownload = null;
			try {
				urlDownload = params[0].getString("pointdata");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// urlDownload = "http://192.168.3.39/text.txt";
			// urlDownload = "http://www.baidu.com/img/baidu_sylogo1.gif";
			// 获得存储卡路径，构成 保存文件的目标路径
			String dirName = "";
			dirName = Environment.getExternalStorageDirectory() + "/MyDownload/";
			File f = new File(dirName);
			if (!f.exists()) {
				f.mkdir();
			}

			// 下载的操作：

			// 准备拼接新的文件名（保存在存储卡后的文件名）
			String newFilename = urlDownload.substring(urlDownload.lastIndexOf("/") + 1);
			newFilename = dirName + newFilename;
			File file = new File(newFilename);
			// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
			if (file.exists()) {
				file.delete();
			}
			try {
				// 构造URL
				URL url = new URL(urlDownload);
				// 打开连接
				URLConnection con = url.openConnection();
				// 获得文件的长度
				int contentLength = con.getContentLength();
				System.out.println("长度 :" + contentLength);
				// 输入流

				InputStream is = con.getInputStream();
				// 1K的数据缓冲
				byte[] bs = new byte[1024];
				// 读取到的数据长度
				int len;
				// 输出的文件流
				OutputStream os = new FileOutputStream(newFilename);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				// 完毕，关闭所有链接
				os.close();
				is.close();
				String content = ""; // 文件内容字符串
				// 如果path是传递过来的参数，可以做一个非目录的判断
				if (file.isDirectory()) {
					Log.d("TestFile", "The File doesn't not exist.");
				} else {
					try {
						InputStream instream = new FileInputStream(file);
						if (instream != null) {
							InputStreamReader inputreader = new InputStreamReader(instream);
							BufferedReader buffreader = new BufferedReader(inputreader);
							String line;
							// 分行读取
							while ((line = buffreader.readLine()) != null) {
								content += line + " ";
							}
							instream.close();
							JSONObject json = new JSONObject();
							json.put("data", content);
							json.put("intentjuli", params[0].getString("distance"));
							json.put("intentshijian", params[0].getString("duration"));
							json.put("kaluli", params[0].getString("calorie"));
							json.put("remark", params[0].getString("remark"));
							return json;
						}
					} catch (java.io.FileNotFoundException e) {
						Log.d("TestFile", "The File doesn't not exist.");
					} catch (IOException e) {
						Log.d("TestFile", e.getMessage());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			try {
				// String data[] = result.getString("data").split(" ");
				// double []polyjing = new double[1000];
				// double []polywei = new double[1000];;
				// int z = 0;
				// for(int i=0;i<data.length;i++){
				// if(i%2 == 0){
				// polyjing[z] = Double.valueOf(data[i]);
				// }else{
				// polywei[z] = Double.valueOf(data[i]);
				// z++;
				// }
				// }
				if (result == null) {
					Toast.makeText(getApplicationContext(), "因轨迹太少或跑步时间太短或文件不存在，无详细记录", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(AtyRunningRecord.this, AtyRunRecordDetail.class);
				intent.putExtra("intentjuli", Float.valueOf(result.getString("intentjuli")));
				intent.putExtra("intentshijian", Long.valueOf(result.getString(("intentshijian"))));
				intent.putExtra("intentkaluli", Float.valueOf(result.getString("kaluli")));
				intent.putExtra("data", result.getString("data"));
				intent.putExtra("remark", result.getString("remark"));
				// intent.putExtra("z", z);
				// intent.putExtra("intentpolyjing", polyjing);
				// intent.putExtra("intentpolywei",polywei);
				intent.putExtra("isRecord", true);
				startActivity(intent);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_running_record);
		api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
		api.registerApp(Config.WxAPP_ID);
		mRequestQueue = Volley.newRequestQueue(this);
		StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.list);
		adapter = new MyAdapter(this);
		stickyList.setAdapter(adapter);
		setTitleBar();
		// t1=(TextView) findViewById(R.id.textt1000);
		stickyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				new myTaskAtyRunningRecord().execute(list.get(position));
			}
		});
	}

	public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

		// private String[] countries;
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			// countries =
			// context.getResources().getStringArray(R.array.countries);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public JSONObject getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			try {
				return Long.valueOf(list.get(position).getString("id"));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.test_list_item_layout, parent, false);
				holder.tvRunDateTime = (TextView) convertView.findViewById(R.id.tvRunDateTime);
				holder.tvAverageSpeed = (TextView) convertView.findViewById(R.id.tvAverageSpeed);
				holder.tvCalorie = (TextView) convertView.findViewById(R.id.tvCalorie);
				holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistanceItem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			try {
				Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/impact.ttf");
				holder.tvRunDateTime.setText(list.get(position).getString("rundatetime"));
				holder.tvAverageSpeed.setText(list.get(position).getString("averagespeed"));
				holder.tvCalorie.setText(list.get(position).getString("calorie"));
				holder.tvDistance.setText(list.get(position).getString("distance"));
				holder.tvAverageSpeed.setTypeface(fontFace);
				holder.tvCalorie.setTypeface(fontFace);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

		@Override
		public View getHeaderView(int position, View convertView, ViewGroup parent) {
			HeaderViewHolder holder;
			if (convertView == null) {
				holder = new HeaderViewHolder();
				convertView = inflater.inflate(R.layout.header, parent, false);
				holder.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
				holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistanceHeader);
				holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				// holder.img1=(ImageView) convertView.findViewById(R.id.img1);
				// holder.img2=(ImageView) convertView.findViewById(R.id.img2);
				convertView.setTag(holder);
			} else {
				holder = (HeaderViewHolder) convertView.getTag();
			}
			// set header text as first char in name
			String headerText;
			try {
				headerText = list.get(position).getString("month");
				holder.tvMonth.setText(list.get(position).getString("month"));
				holder.tvDistance.setText("公里" + list.get(position).getString("sumdistance"));
				holder.tvTime.setText("时长" + list.get(position).getString("sumduration"));// holder.img1.setImageURI(uri)
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			// return the first character of the country as ID because this is
			// what headers are based upon
			try {
				return list.get(position).getLong("headerId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

		class HeaderViewHolder {
			TextView tvMonth, tvDistance, tvTime;
			ImageView img1, img2;
		}

		class ViewHolder {
			TextView tvRunDateTime;
			TextView tvDistance;
			TextView tvAverageSpeed;
			TextView tvCalorie;
		}

	}

	public static JSONObject jsonObject;

	@Override
	protected void onResume() {
		super.onResume();
		list.clear();
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				Config.SERVER_URL + "Users/getUserRunRecord", new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						try {
							// progressDialog.dismiss();
							jsonObject = new JSONObject(s);
							JSONObject json = new JSONObject();
							if (jsonObject.getString("result").equals("1")) {
								JSONArray jsonArray = jsonObject.getJSONArray("data");
								for (int i = 0; i < jsonArray.length(); i++) {
									for (int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("rundata")
											.length(); j++) {
										json = jsonArray.getJSONObject(i).getJSONArray("rundata").getJSONObject(j);
										json.put("headerId", i);
										json.put("month", jsonArray.getJSONObject(i).getString("month"));
										json.put("sumduration", jsonArray.getJSONObject(i).getString("sumduration"));
										json.put("sumdistance", jsonArray.getJSONObject(i).getString("sumdistance"));
										list.add(json);
									}
								}
								adapter.notifyDataSetChanged();
								// int t=jsonArray.length();
								//// s=t+s;
								// List<String> list = new ArrayList<String>();
								// for (int i=0; i<t; i++) {
								// list.add( jsonArray.getString(i) );
								// }
								// String[] stringArray = list.toArray(new
								// String[list.size()]);
								//
								// //设置list里面的参数
								//// final int sectionsNumber = to - from + 1;
								//
								//
								//// int sectionPosition = 0, listPosition = 0;
								// for(int
								// i=0;i<t;i++){//result+=stringArray[i];
								// JSONObject json1 =
								// jsonArray.getJSONObject(i);//月份的
								// String month=(String)
								// json1.get("month");result+="月份："+month+" ";
								// String sumduration=(String)
								// json1.get("sumduration");result+="总时间："+sumduration+"
								// ";
								// float sumdistance =
								// json1.getLong("sumdistance");result+="总距离："+sumdistance+"
								// ";
								// JSONArray jsonArray_ =
								// json1.getJSONArray("rundata");result+="具体数据:\r\n";
								// String listtext=month+" 公里"+sumduration+"
								// 时长"+sumdistance;
								// for(int j=0;j<jsonArray_.length();j++){
								// JSONObject json2 =
								// jsonArray_.getJSONObject(j);//具体每次的
								// int id =
								// json2.getInt("id");result+="id:"+id+" ";
								// String rundatetime=(String)
								// json2.get("rundatetime");result+="总时间："+rundatetime+"
								// ";
								// String averagespeed=(String)
								// json2.get("averagespeed");result+="平均速度："+averagespeed+"
								// ";
								// String calorie=(String)
								// json2.get("calorie");result+="卡路里："+calorie+"
								// ";
								// String distance=(String)
								// json2.get("distance");result+="距离："+distance+"\r\n";
								// }
								// }
								// Toast.makeText(getApplicationContext(),
								// "下载成功", 2).show();

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
				map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
				// map.put(Config.KEY_UID,
				// Config.getCachedUserUid(AtyRunningRecord.this.getApplicationContext()));
				return map;
			}
		};
		mRequestQueue.add(stringRequest);
	}

	@SuppressLint("InlinedApi")
	public void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("运动记录");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyRunningRecord.this.finish();
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
			popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
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
		popupWindow.showAtLocation(findViewById(R.id.running_content), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);


	}
}
