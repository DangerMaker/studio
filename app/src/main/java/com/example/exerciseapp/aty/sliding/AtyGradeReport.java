package com.example.exerciseapp.aty.sliding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GradeReportListAdapter;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AtyGradeReport extends BaseActivity {
	private TextView tvGameTitleGradeReport;
	private ListView listView;
	private LinkedList<JSONObject> list = new LinkedList<JSONObject>();
	private GradeReportListAdapter mAdapter;
	private String gameName = null;
	private RequestQueue mRequestQueue;
	private static IWXAPI api;
	private Toolbar toolbar;
	private TextView pageTitle;
	private String gameId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_grade_report);
		api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
		api.registerApp(Config.WxAPP_ID);
		mRequestQueue =  Volley.newRequestQueue(this);
		initView();
		setTitleBar();

		getInfo();
		mAdapter = new GradeReportListAdapter(this, list);
		listView.setAdapter(mAdapter);
		listView.setFocusableInTouchMode(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AtyGradeReport.this,AtyGradeDetail.class);
				try {
					intent.putExtra("url", list.get(position).getString(Config.KEY_GAME_GRADE_DETAIL));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
	}
	
	private void getInfo(){
		gameId = getIntent().getStringExtra(Config.KEY_GAME_ID);
		gameName = getIntent().getStringExtra(Config.KEY_GAME_NAME);
		tvGameTitleGradeReport.setText(gameName);
		StringRequest stringRequest = new StringRequest(
				Request.Method.GET,
				"http://101.200.214.68/py/game?action=get_game_score&" + Config.KEY_GAME_ID + "=" + gameId,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject response = new JSONObject(s);
							JSONArray data = response.getJSONArray("data");
							list.clear();
							for (int i = 0; i < data.length(); i++) {
								list.add(data.getJSONObject(i));
							}
							mAdapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Toast.makeText(AtyGradeReport.this, Config.CONNECTION_ERROR, Toast.LENGTH_LONG).show();
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
	
	private void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("成绩播报");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyGradeReport.this.finish();
			}
		});
	}
	private void initView(){
		tvGameTitleGradeReport = (TextView) findViewById(R.id.tvGameTitleGradeReport);
		listView = (ListView) findViewById(R.id.listViewGradeReport);
	}
}
