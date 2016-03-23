package com.example.exerciseapp.aty.sliding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ListView;
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
import com.example.exerciseapp.adapter.MessageAdapter;
import com.umeng.message.PushAgent;

public class AtyMessage extends BaseActivity {

	// private SwipeMenuListView listView;
	private ListView listView;
	private MessageAdapter adapter;
	private List<JSONObject> list = new LinkedList<JSONObject>();
	private RequestQueue mRequestQueue;
	private JSONArray cachedMessage;

	private Toolbar toolbar;
	private TextView pageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_message);
		mRequestQueue = Volley.newRequestQueue(this);
		listView = (ListView) findViewById(R.id.listView);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		cachedMessage = Config.getCachedMessage(getApplicationContext());
		try {
			if (cachedMessage != null) {
				for (int i = 0; i < cachedMessage.length(); i++) {
					list.add(cachedMessage.getJSONObject(i));
				}
			} else {
				cachedMessage = new JSONArray();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new MessageAdapter(this, list);
		listView.setAdapter(adapter);
		// SwipeMenuCreator creator = new SwipeMenuCreator() {
		//
		// @Override
		// public void create(SwipeMenu menu) {
		// // TODO Auto-generated method stub
		// SwipeMenuItem item1 = new SwipeMenuItem(
		// getApplicationContext());
		// item1.setBackground(new ColorDrawable(Color.rgb(0xff, 0xff,
		// 0xff)));
		// item1.setWidth(90);
		// item1.setIcon(R.drawable.delete);
		// menu.addMenuItem(item1);
		// }
		// };
		// listView.setMenuCreator(creator);
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(AtyMessage.this,AtyMessageDetail.class);
		// intent.putExtra("information", list.get(position).toString());
		// startActivity(intent);
		// }
		// });
		// listView.setOnMenuItemClickListener(new
		// SwipeMenuListView.OnMenuItemClickListener() {
		// @Override
		// public boolean onMenuItemClick(int position, SwipeMenu menu, int
		// index) {
		// JSONObject jsonObj = list.get(position);
		// list.remove(jsonObj);
		// cachedMessage = new JSONArray();
		// for(int i=0;i<list.size();i++){
		// cachedMessage.put(list.get(i));
		// }
		// adapter.notifyDataSetChanged();
		// return false;
		// }
		// });

		// getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// getActionBar().setCustomView(R.layout.actionbar_start_running);
		// TextView title = (TextView) getActionBar()
		// .getCustomView().findViewById(R.id.tvPageTitleOfAll);
		// title.setText("消息");
		// getActionBar().getCustomView().findViewById(R.id.ivBackBtnStartRunning).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// startActivity(new Intent(AtyMessage.this,AtySlidingHome.class));
		// }
		// });
		setTitleBar();
	}

	@SuppressLint("InlinedApi")
	public void setTitleBar() {
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("消息");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyMessage.this.finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Config.STATUS_HAS_MESSAGE = false;
		StringRequest stringRequestMyEntryForm = new StringRequest(Request.Method.POST,
				Config.SERVER_URL + "System/sysInfo", new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.getString("result").equals("1")) {
								try {
									list.clear();
									for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
										list.add(jsonObject.getJSONArray("data").getJSONObject(i));
										cachedMessage.put(jsonObject.getJSONArray("data").getJSONObject(i));
									}
									Config.cacheMessage(getApplicationContext(), cachedMessage);
									pageTitle.setText("消息(" + list.size() + ")");
									adapter.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}

							} else {

							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						Toast.makeText(AtyMessage.this, Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
					}
				}) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				return map;
			}
		};
		mRequestQueue.add(stringRequestMyEntryForm);

	}
}
