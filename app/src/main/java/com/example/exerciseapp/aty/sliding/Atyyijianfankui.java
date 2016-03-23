package com.example.exerciseapp.aty.sliding;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.exerciseapp.fragment.shangchuanyijianDialog;
import com.umeng.message.PushAgent;

public class Atyyijianfankui extends BaseActivity {
	EditText edittextyijianfankui;
	Button btntijiaoyijian;
	private RequestQueue mRequestQueue;
	private Toolbar toolbar;
	private TextView pageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.yijianfankui);
		setTitleBar(Atyyijianfankui.this);
		// initActionBar();
		mRequestQueue = Volley.newRequestQueue(this);
		edittextyijianfankui = (EditText) findViewById(R.id.edittextyijianfankui);
		btntijiaoyijian = (Button) findViewById(R.id.btntijiaoyijian);
		btntijiaoyijian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringRequest stringRequest = new StringRequest(Request.Method.POST,
						Config.SERVER_URL + "Users/adviceNew", new Response.Listener<String>() {

					@Override
					public void onResponse(String s) {
						try {
							// progressDialog.dismiss();
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.getString("result").equals("1")) {
								// Toast.makeText(getApplicationContext(),
								// "上传成功", 2).show();
								// new AlertDialog.Builder(Atyyijianfankui.this)
								// .setTitle(" 提示" )
								//
								// .setMessage(" 提交完成" )
								// .setPositiveButton("确定" , new
								// DialogInterface.OnClickListener() {
								//
								// @Override
								// public void onClick(DialogInterface dialog,
								// int which) {
								// // TODO Auto-generated method stub
								// Intent i=new
								// Intent(getApplicationContext(),AtySlidingHome.class);
								// startActivity(i);
								// }
								// } )
								// .show();
								shangchuanyijianDialog dl = new shangchuanyijianDialog(Atyyijianfankui.this, "提示",
										new shangchuanyijianDialog.OnCustomDialogListener() {

									@Override
									public void back(String name) {
										// TODO Auto-generated method stub

									}
								});
								dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dl.show();
								// Config.cacheUserUid(getApplicationContext(),
								// jsonObject.getJSONObject("data").getString(Config.KEY_UID));
								// startActivity(new
								// Intent(AtyLogin.this,AtyPersonalize.class));
								// Toast.makeText(getApplicationContext(),
								// jsonObject.getString("desc"), 2).show();
								// Config.STATUS_FINISH_ACTIVITY = 1;
								// finish();
							} else {
								Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							// progressDialog.dismiss();
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						// progressDialog.dismiss();
						Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
					}
				}) {

					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> map = new HashMap<String, String>();
						map.put(Config.KEY_UID, "1");
						map.put(Config.KEY_CONTENT, edittextyijianfankui.getText().toString());
						// map.put(Config.KEY_TEL,etPhoneNum.getText().toString());
						// map.put(Config.KEY_PASSWORD,etPassword.getText().toString());
						return map;
					}
				};
				mRequestQueue.add(stringRequest);

			}
		});
	}

	@SuppressLint("InlinedApi")
	public void setTitleBar(final Activity activity) {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("意见反馈");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}

}
