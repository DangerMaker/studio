package com.example.exerciseapp.aty.sliding;

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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyRegisterHomePage.OnTextViewClickListener;
import com.example.exerciseapp.listener.TextClickSpan;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtyModifyClubEntryForm extends BaseActivity {

	private TextView tvClubOrAssocNameModifyClubOrAssocEntryForm;
	private EditText etUserNameModifyClubOrAssocEntryForm;
	private EditText etSexClubOrAssocEntryForm;
	private EditText etAgeClubOrAssocEntryForm;
	private EditText etTelClubOrAssocEntryForm;
	private EditText etIdCardClubOrAssocEntryForm;
	private EditText etEmailClubOrAssocEntryForm;
	private EditText etLocationClubOrAssocEntryForm;

	private CheckBox cbAgreeRulesAssocEntryForm;
	private TextView tvRules;
	private String agreement = new String();

	private static final Pattern agreeRulePattern = Pattern.compile(Config.AGREE_RULE);

	private Button btnCommitModifyClubOrAssocEntryForm;

	private String id = new String();
	private String clubOrAssocName = new String();

	// private JSONObject jsonObj;

	private RequestQueue mRequestQueue;

	private Toolbar toolbar;
	private TextView pageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		mRequestQueue = Volley.newRequestQueue(this);
		setContentView(R.layout.aty_modify_club_entry_form);

		clubOrAssocName = getIntent().getStringExtra(Config.KEY_ANAME);
		id = getIntent().getStringExtra(Config.KEY_ID);
		agreement = getIntent().getStringExtra("agreement");
		initView();

		if (Config.CAN_MODIFY_ALL_INFORMATION == 0) {
			etUserNameModifyClubOrAssocEntryForm.setFocusableInTouchMode(false);
			etSexClubOrAssocEntryForm.setFocusableInTouchMode(false);
			etAgeClubOrAssocEntryForm.setFocusableInTouchMode(false);
			etIdCardClubOrAssocEntryForm.setFocusableInTouchMode(false);
			etEmailClubOrAssocEntryForm.setFocusableInTouchMode(false);
			etLocationClubOrAssocEntryForm.setFocusableInTouchMode(false);
			Config.CAN_MODIFY_ALL_INFORMATION = 1;
		}

		try {
			JSONObject json = new JSONObject(getIntent().getStringExtra("information"));
			JSONObject jsonObj = json.getJSONObject("data");
			tvClubOrAssocNameModifyClubOrAssocEntryForm.setText(clubOrAssocName);
			etUserNameModifyClubOrAssocEntryForm.setText(jsonObj.getString(Config.KEY_USER_NAME));
			etSexClubOrAssocEntryForm.setText(jsonObj.getString(Config.KEY_SEX));
			etAgeClubOrAssocEntryForm.setText(jsonObj.getString(Config.KEY_AGE));
			etIdCardClubOrAssocEntryForm.setText(jsonObj.getString(Config.KEY_ID_CARD));
			etTelClubOrAssocEntryForm.setText(jsonObj.getString(Config.KEY_TEL));
			etEmailClubOrAssocEntryForm.setText(jsonObj.getString(Config.KEY_EMAIL));
			etLocationClubOrAssocEntryForm.setText(jsonObj.getString(Config.KEY_USER_AREA));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		btnCommitModifyClubOrAssocEntryForm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url;
				try {
					url = Config.SERVER_URL + "Assoc/treatAssocInfo?" + Config.KEY_ID + "=" + id + "&"
							+ Config.KEY_USER_NAME + "="
							+ URLEncoder.encode(etUserNameModifyClubOrAssocEntryForm.getText().toString(), "utf-8")
							+ "&" + Config.KEY_SEX + "="
							+ URLEncoder.encode(etSexClubOrAssocEntryForm.getText().toString(), "utf-8") + "&"
							+ Config.KEY_AGE + "=" + etAgeClubOrAssocEntryForm.getText().toString() + "&"
							+ Config.KEY_TEL + "=" + etTelClubOrAssocEntryForm.getText().toString() + "&"
							+ Config.KEY_EMAIL + "=" + etEmailClubOrAssocEntryForm.getText().toString() + "&"
							+ Config.KEY_ID_CARD + "=" + etIdCardClubOrAssocEntryForm.getText().toString() + "&"
							+ Config.KEY_USER_AREA + "="
							+ URLEncoder.encode(etLocationClubOrAssocEntryForm.getText().toString(), "utf-8") + "&";
					StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
							new Response.Listener<String>() {

						@Override
						public void onResponse(String s) {
							try {
								JSONObject jsonObject = new JSONObject(s);
								if (jsonObject.getInt("result") == 1) {
									Toast.makeText(AtyModifyClubEntryForm.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
									finish();
								} else {
									Toast.makeText(AtyModifyClubEntryForm.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
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
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		tvRules.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AtyModifyClubEntryForm.this, AtyGradeDetail.class);
				intent.putExtra("agreement", agreement);
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
				// TODO
			}
		}));
		cbAgreeRulesAssocEntryForm.setChecked(true);
		cbAgreeRulesAssocEntryForm.setClickable(true);
		cbAgreeRulesAssocEntryForm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!cbAgreeRulesAssocEntryForm.isChecked()) {
					cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_on_background);
				} else {
					cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_off_background);
				}
			}
		});
		cbAgreeRulesAssocEntryForm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 勾选和不勾选的动作分别是？？？？
				if (isChecked) {
					Toast.makeText(AtyModifyClubEntryForm.this, "请阅读协议", Toast.LENGTH_SHORT).show();
				}

			}
		});
		// getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// getActionBar().setCustomView(R.layout.actionbar_start_running);
		// TextView title = (TextView)
		// getActionBar().getCustomView().findViewById(R.id.tvPageTitleOfAll);
		// title.setText("修改资料");
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

	private void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("报名表");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyModifyClubEntryForm.this.finish();
			}
		});
	}

	private void initView() {
		tvClubOrAssocNameModifyClubOrAssocEntryForm = (TextView) findViewById(
				R.id.tvClubOrAssocNameModifyClubOrAssocEntryForm);
		etUserNameModifyClubOrAssocEntryForm = (EditText) findViewById(R.id.etUserNameModifyClubOrAssocEntryForm);
		etSexClubOrAssocEntryForm = (EditText) findViewById(R.id.etSexClubOrAssocEntryForm);
		etAgeClubOrAssocEntryForm = (EditText) findViewById(R.id.etAgeClubOrAssocEntryForm);
		etTelClubOrAssocEntryForm = (EditText) findViewById(R.id.etTelClubOrAssocEntryForm);
		etIdCardClubOrAssocEntryForm = (EditText) findViewById(R.id.etIdCardClubOrAssocEntryForm);
		etEmailClubOrAssocEntryForm = (EditText) findViewById(R.id.etEmailClubOrAssocEntryForm);
		etLocationClubOrAssocEntryForm = (EditText) findViewById(R.id.etLocationClubOrAssocEntryForm);
		btnCommitModifyClubOrAssocEntryForm = (Button) findViewById(R.id.btnCommitModifyClubOrAssocEntryForm);
		tvRules = (TextView) findViewById(R.id.tvRules);
		cbAgreeRulesAssocEntryForm = (CheckBox) findViewById(R.id.cbAgreeRulesAssocEntryForm);

	}

	public void setClickTextView(TextView textview, SpannableString ss, int start, int end, ClickableSpan cs) {
		ss.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textview.setText(ss);
		textview.setMovementMethod(LinkMovementMethod.getInstance());
	}

	public void setKeyworkClickable(TextView textview, SpannableString ss, Pattern pattern, ClickableSpan cs) {
		Matcher matcher = pattern.matcher(ss.toString());
		while (matcher.find()) {
			String key = matcher.group();
			if (!"".equals(key)) {
				int start = ss.toString().indexOf(key);
				int end = start + key.length();
				setClickTextView(textview, ss, start, end, cs);
			}
		}
	}
}
