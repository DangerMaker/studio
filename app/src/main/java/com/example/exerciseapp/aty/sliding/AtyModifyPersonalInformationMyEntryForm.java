package com.example.exerciseapp.aty.sliding;
/**
 * 修改资料界面
 */


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
import com.example.exerciseapp.aty.login.AtyRegisterHomePage.OnTextViewClickListener;
import com.example.exerciseapp.listener.TextClickSpan;
import com.umeng.message.PushAgent;

public class AtyModifyPersonalInformationMyEntryForm extends BaseActivity {
	
	private TextView tvGameNameModifyPersonalInformation;
	private EditText etUserNameModifyPersonalInformation;
	private EditText etUserSexModifyPersonalInformation;
	private EditText etUserAgeModifyPersonalInformation;
	private EditText etUserPhoneNumModifyPersonalInformation;
	private EditText etUserIDCardModifyPersonalInformation;
	
	private Button btnCommitModifyPersonalInformation;
	
	private CheckBox cbAgreeRulesAssocEntryForm;
	private TextView tvRules;
	private String agreement = new String();
	
	private static final Pattern agreeRulePattern = Pattern.compile(Config.AGREE_RULE);
	
	private String ueid = new String();
	private String gameName = new String();
	
//	private JSONObject jsonObj;
	
	private RequestQueue mRequestQueue; 
	
	private Toolbar toolbar;
	private TextView pageTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		mRequestQueue =  Volley.newRequestQueue(this);
		setContentView(R.layout.aty_modify_personal_information);
//		ueid = getIntent().getStringExtra(Config.KEY_UEID);
		gameName = getIntent().getStringExtra(Config.KEY_GAME_NAME);
		agreement = getIntent().getStringExtra("agreement");
		//初始通信
//		new BaseNetConnection( Config.SERVER_URL+"Game/changeGameInfo",Config.POST,new BaseNetConnection.SuccessCallback() {
//			
//			@Override
//			public void onSuccess(String result) {
//				try {
//					jsonObj = new JSONObject(result);
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		},new BaseNetConnection.FailCallback() {
//			
//			@Override
//			public void onFail() {
//				// TODO Auto-generated method stub
//				
//			}
//		},Config.KEY_UEID,"49");
//		StringRequest  stringRequest = new StringRequest(
//              Request.Method.POST,
//              Config.SERVER_URL+"Game/changeGameInfo",
//              new Response.Listener<String>() {
//
//                  @Override
//                  public void onResponse(String s) {
//                      try {
//                          JSONObject jsonObject = new JSONObject(s);
//                          if(jsonObject.getInt("result") == 1){
//                          	jsonObj = jsonObject.getJSONObject("data");
//                          	Toast.makeText(getApplicationContext(), "sfsfs", 3).show();
//                          }else{
//                          	
//                          }
//                          
//                      } catch (JSONException e) {
//                          e.printStackTrace();
//                      }
//                  }
//              },
//              new Response.ErrorListener() {
//
//                  @Override
//                  public void onErrorResponse(VolleyError volleyError) {
//                  }
//              }){
//
//          @Override
//          protected Map<String, String> getParams() throws AuthFailureError {
//              Map<String,String> map = new HashMap<String,String>();
//              map.put(Config.KEY_UEID, "49");
//              return map;
//          }
//      };
//      mRequestQueue.add(stringRequest);
		
		initView();
		if(Config.CAN_MODIFY_ALL_INFORMATION == 0){
			etUserNameModifyPersonalInformation.setFocusableInTouchMode(false);
			etUserSexModifyPersonalInformation.setFocusableInTouchMode(false);
			etUserAgeModifyPersonalInformation.setFocusableInTouchMode(false);
			etUserIDCardModifyPersonalInformation.setFocusableInTouchMode(false);
			Config.CAN_MODIFY_ALL_INFORMATION = 1;
		}
		
		try {
			JSONObject json = new JSONObject(getIntent().getStringExtra("information"));
			JSONObject jsonObj = json.getJSONObject("data");
			ueid = jsonObj.getString(Config.KEY_UEID);
			tvGameNameModifyPersonalInformation.setText(gameName);
			etUserNameModifyPersonalInformation.setText(jsonObj.getString(Config.KEY_EUSERNAME));
			etUserSexModifyPersonalInformation.setText(jsonObj.getString(Config.KEY_ESEX));
			etUserAgeModifyPersonalInformation.setText(jsonObj.getString(Config.KEY_EAGE));
			etUserPhoneNumModifyPersonalInformation.setText(jsonObj.getString(Config.KEY_ETEL));
			etUserIDCardModifyPersonalInformation.setText(jsonObj.getString(Config.KEY_EIDCARD));
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		btnCommitModifyPersonalInformation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url;
				try {
					url = Config.SERVER_URL+"Game/treatGameInfo?"
							+Config.KEY_UEID+"="+ueid+"&"
							+Config.KEY_EUSERNAME+"="+URLEncoder.encode(etUserNameModifyPersonalInformation.getText().toString(),"utf-8")+"&"
							+Config.KEY_ESEX+"="+URLEncoder.encode(etUserSexModifyPersonalInformation.getText().toString(),"utf-8")+"&"
							+Config.KEY_EAGE+"="+etUserAgeModifyPersonalInformation.getText().toString()+"&"
							+Config.KEY_ETEL+"="+etUserPhoneNumModifyPersonalInformation.getText().toString()+"&"
							+Config.KEY_EIDCARD+"="+etUserIDCardModifyPersonalInformation.getText().toString();
					StringRequest  stringRequest = new StringRequest(
							Request.Method.GET,
							url,
							new Response.Listener<String>() {
								
								@Override
								public void onResponse(String s) {
									try {
										JSONObject jsonObject = new JSONObject(s);
										if(jsonObject.getInt("result") == 1){
											Toast.makeText(AtyModifyPersonalInformationMyEntryForm.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
											finish();
										}else{
											Toast.makeText(AtyModifyPersonalInformationMyEntryForm.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
										}
										
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							},
							new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
								}
							}){
						
						@Override
						protected Map<String, String> getParams() throws AuthFailureError {
							Map<String,String> map = new HashMap<String,String>();
							return map;
						}
					};
					mRequestQueue.add(stringRequest);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
//		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
//		getActionBar().setCustomView(R.layout.actionbar_start_running);
//		TextView title = (TextView) getActionBar().getCustomView().findViewById(R.id.tvPageTitleOfAll);
//		title.setText("修改资料");
//		getActionBar().getCustomView().findViewById(R.id.ivBackBtnStartRunning).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});

tvRules.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AtyModifyPersonalInformationMyEntryForm.this,AtyGradeDetail.class);
				intent.putExtra("agreement",agreement);
				startActivity(intent);
			}
		});
		SpannableString ss = new SpannableString(tvRules.getText());
		setKeyworkClickable(tvRules, ss, agreeRulePattern, new TextClickSpan(new OnTextViewClickListener() {
			
			@Override
			public void setStyle(TextPaint ds) {
				ds.setColor(Color.rgb(58, 145, 173));
				ds.setUnderlineText(true);
			}
			
			@Override
			public void clickTextView() {
				//TODO
			}
		}));
		cbAgreeRulesAssocEntryForm.setChecked(true);
		cbAgreeRulesAssocEntryForm.setClickable(true);
		cbAgreeRulesAssocEntryForm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!cbAgreeRulesAssocEntryForm.isChecked()){
					cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_on_background);
				}else{
					cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_off_background);
				}
			}
		});
		cbAgreeRulesAssocEntryForm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 勾选和不勾选的动作分别是？？？？
				if(isChecked){
					Toast.makeText(AtyModifyPersonalInformationMyEntryForm.this, "请阅读协议", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		setTitleBar();
	}
	
	private void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("修改资料");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyModifyPersonalInformationMyEntryForm.this.finish();
			}
		});
	}

	private void initView(){
		tvGameNameModifyPersonalInformation = (TextView) findViewById(R.id.tvClubOrAssocNameModifyClubOrAssocEntryForm);
		etUserNameModifyPersonalInformation = (EditText) findViewById(R.id.etAgeClubOrAssocEntryForm);
		etUserSexModifyPersonalInformation = (EditText) findViewById(R.id.etTelClubOrAssocEntryForm);
		etUserAgeModifyPersonalInformation = (EditText) findViewById(R.id.etIdCardClubOrAssocEntryForm);
		etUserPhoneNumModifyPersonalInformation = (EditText) findViewById(R.id.etEmailClubOrAssocEntryForm);
		etUserIDCardModifyPersonalInformation = (EditText) findViewById(R.id.etLocationClubOrAssocEntryForm);
		btnCommitModifyPersonalInformation = (Button) findViewById(R.id.btnCommitModifyClubOrAssocEntryForm);
		tvRules = (TextView) findViewById(R.id.tvRules);
		cbAgreeRulesAssocEntryForm = (CheckBox) findViewById(R.id.cbAgreeRulesAssocEntryForm);
		
	}
	public void setClickTextView(TextView textview, SpannableString ss, int start, int end, ClickableSpan cs){
		ss.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textview.setText(ss);
		textview.setMovementMethod(LinkMovementMethod.getInstance());
	}
	public void setKeyworkClickable(TextView textview, SpannableString ss, Pattern pattern, ClickableSpan cs){
		Matcher matcher = pattern.matcher(ss.toString());
		while(matcher.find()){
			String key = matcher.group();
			if(!"".equals(key)){
				int start = ss.toString().indexOf(key);
				int end = start+key.length();
				setClickTextView(textview, ss, start, end, cs);
			}
		}
	}
	
}
