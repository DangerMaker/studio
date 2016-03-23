package com.example.exerciseapp.aty.sliding;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.view.liangfeizc.flowlayout.FlowLayout;
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
import com.umeng.message.PushAgent;

public class AtyUserPreferedProject extends BaseActivity {

	private FlowLayout flow_layout0;
	private FlowLayout flow_layout1;
	private FlowLayout flow_layout2;
	private FlowLayout flow_layout3;
	private FlowLayout flow_layout4;
	private RequestQueue mRequestQueue;
	private int selectedNum = 0;

	private Map<Integer, String> map = new HashMap<Integer, String>();
	
	private Toolbar toolbar;
	private TextView pageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_user_prefered_project);
		mRequestQueue = Volley.newRequestQueue(this);
		flow_layout0 = (FlowLayout) findViewById(R.id.flow_layout0);
		flow_layout1 = (FlowLayout) findViewById(R.id.flow_layout1);
		flow_layout2 = (FlowLayout) findViewById(R.id.flow_layout2);
		flow_layout3 = (FlowLayout) findViewById(R.id.flow_layout3);
		flow_layout4 = (FlowLayout) findViewById(R.id.flow_layout4);

		findViewById(R.id.btnsave).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StringRequest stringRequest = new StringRequest(Request.Method.GET,
						Config.SERVER_URL + "Users/treatNewInterest?" + Config.KEY_UID + "="
								+ Config.getCachedUserUid(getApplicationContext()) + "&" + "interest" + "=" + map.get(0)
								+ "," + map.get(1) + "," + map.get(2),
						new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							Toast.makeText(AtyUserPreferedProject.this, jsonObject.getString("desc"), 2).show();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						finish();
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
						return map;
					}
				};
				mRequestQueue.add(stringRequest);
			}
		});

		// getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// getActionBar().setCustomView(R.layout.actionbar_start_running);
		// TextView title = (TextView)
		// getActionBar().getCustomView().findViewById(R.id.tvPageTitleOfAll);
		// title.setText("我喜爱的健身项目");
		// getActionBar().getCustomView().findViewById(R.id.ivBackBtnStartRunning).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
		setTitleBar();
	}

	@SuppressLint("InlinedApi")
	public void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("我喜欢的运动项目");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyUserPreferedProject.this.finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		flow_layout0.removeAllViews();
		flow_layout1.removeAllViews();
		flow_layout2.removeAllViews();
		flow_layout3.removeAllViews();
		flow_layout4.removeAllViews();
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				Config.SERVER_URL + "Users/getInterestList", new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.getInt("result") == 1) {
								JSONArray jsonArr = jsonObject.getJSONArray("data");
								for (int i = 0; i < jsonArr.length(); i++) {
									final JSONObject json;
									json = jsonArr.getJSONObject(i);
									final TextView t = new TextView(AtyUserPreferedProject.this);
									t.setText(json.getString("iname"));
									t.setTextColor(Color.rgb(99, 97, 97));
									t.setTextSize(18);
									json.put("isClicked", false);
									if (json.getInt("select") == 0) {

									} else {
										map.put(selectedNum, json.getString("iid"));
										selectedNum++;
										json.put("isClicked", true);
										t.setTextColor(Color.rgb(0, 130, 164));
									}
									t.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											try {
												if (selectedNum >= 3) {
													if (json.getBoolean("isClicked")) {
														t.setTextColor(Color.rgb(99, 97, 97));
														json.put("isClicked", false);
														selectedNum--;
														map.remove(selectedNum);
													}
													Toast.makeText(getApplicationContext(), "只能选择三项", Toast.LENGTH_SHORT).show();
													return;
												} else {
													if (!json.getBoolean("isClicked")) {
														t.setTextColor(Color.rgb(0, 130, 164));
														json.put("isClicked", true);
														map.put(selectedNum, json.getString("iid"));
														selectedNum++;
													} else {
														t.setTextColor(Color.rgb(99, 97, 97));
														json.put("isClicked", false);
														selectedNum--;
														map.remove(selectedNum);
													}
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}
									});
									if (json.getString("type").equals("0")) {
										flow_layout0.addView(t);
									} else if (json.getString("type").equals("1")) {
										flow_layout1.addView(t);
									} else if (json.getString("type").equals("2")) {
										flow_layout2.addView(t);
									} else if (json.getString("type").equals("3")) {
										flow_layout3.addView(t);
									} else {
										flow_layout4.addView(t);
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR,Toast.LENGTH_SHORT).show();
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
				return map;
			}
		};
		mRequestQueue.add(stringRequest);

	}
}
