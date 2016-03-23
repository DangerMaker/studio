package com.example.exerciseapp.fragment;
/**
 * 个人报名Fragment
 */


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyRegisterHomePage.OnTextViewClickListener;
import com.example.exerciseapp.aty.sliding.AtyEntryForm;
import com.example.exerciseapp.aty.sliding.AtyGradeDetail;
import com.example.exerciseapp.aty.sliding.AtyPay;
import com.example.exerciseapp.listener.TextClickSpan;

public class PersonalEntryFormFragment extends Fragment {

	private String gameId;
	private String eId;
	private JSONObject jsonObj;
	JSONObject[] jsonItem = null;
	List<JSONObject> listJsonObj = new ArrayList<JSONObject>();
	List list = new ArrayList();
	String[] items = null;
	private TextView tvGameNamePersonalEntryForm;
	private TextView tvGameItemPersonalEntryForm;
	private TextView tvPayFeePersonalEntryForm;
	private EditText etUserNamePersonalEntryForm;
	private TextView tvUserSexPersonalEntryForm;
	private EditText etUserAgePersonalEntryForm;
	private EditText etUserPhoneNumPersonalEntryForm;
	private EditText etUserIDCardPersonalEntryForm;
	private CheckBox cbAgreeRulesPersonalEntryForm;
	private Button btnCommitPersonalEntry;
	private TextView tvRules;
	
	private RequestQueue mRequestQueue;  
	
	private static final Pattern agreeRulePattern = Pattern.compile(Config.AGREE_RULE);
	private AlertDialog alertDialogNoCompleteInformation = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRequestQueue =  Volley.newRequestQueue(getActivity());
		gameId = getActivity().getIntent().getStringExtra(Config.KEY_GAME_ID);
		try {
			jsonObj = new JSONObject(getActivity().getIntent().getStringExtra("entryInfor"));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		View view = inflater.inflate(R.layout.fragment_personal_entry_form, null);
		if(jsonObj == null){
			return view;
		}
		
		tvGameNamePersonalEntryForm = (TextView) view.findViewById(R.id.tvGameNameAssocEntryForm);
		tvGameItemPersonalEntryForm = (TextView) view.findViewById(R.id.etApplyUserNameAssocEntryForm);
		tvPayFeePersonalEntryForm = (TextView) view.findViewById(R.id.tvSexAssocEntryForm);
		etUserNamePersonalEntryForm = (EditText) view.findViewById(R.id.etUserAgeAssocEntryForm);
		tvUserSexPersonalEntryForm = (TextView) view.findViewById(R.id.tvUserNumAssocEntryForm);
		etUserAgePersonalEntryForm = (EditText) view.findViewById(R.id.etUserIdCardAssocEntryForm);
		etUserPhoneNumPersonalEntryForm = (EditText) view.findViewById(R.id.etUserEmailAssocEntryForm);
		etUserIDCardPersonalEntryForm = (EditText) view.findViewById(R.id.etPayFeeAssocEntryForm);
		cbAgreeRulesPersonalEntryForm = (CheckBox) view.findViewById(R.id.cbAgreeRulesAssocEntryForm);
		btnCommitPersonalEntry = (Button) view.findViewById(R.id.btnCommitPersonalEntry);
		tvRules = (TextView) view.findViewById(R.id.tvRules);
		SpannableString ss = new SpannableString(tvRules.getText());
		setKeyworkClickable(tvRules, ss, agreeRulePattern, new TextClickSpan(new OnTextViewClickListener() {
			
			@Override
			public void setStyle(TextPaint ds) {
				ds.setColor(Color.rgb(58, 145, 173));
				ds.setUnderlineText(true);
			}
			
			@Override
			public void clickTextView() {
				
			}
		}));
		tvRules.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),AtyGradeDetail.class);
				intent.putExtra("agreement",getActivity().getIntent().getStringExtra("agreement"));
				getActivity().startActivity(intent);				
			}
		});
		
		try {
			if(!jsonObj.isNull("eventInfo")){
				for (int i = 0; i < jsonObj.getJSONArray("eventInfo").length(); i++) {
					listJsonObj.add(jsonObj.getJSONArray("eventInfo").getJSONObject(i));
					list.add(jsonObj.getJSONArray("eventInfo").getJSONObject(i).getString("ename"));
				}
				items = (String[]) list.toArray(new String[0]);
			}
			tvGameNamePersonalEntryForm.setText(jsonObj.getString(Config.KEY_GAME_NAME));
			//點擊提交動作
			btnCommitPersonalEntry.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 
					if(tvGameItemPersonalEntryForm.getText().toString().equals("选择")||etUserNamePersonalEntryForm.getText().toString().equals("")
							||tvUserSexPersonalEntryForm.getText().toString().equals("")
							||etUserAgePersonalEntryForm.getText().toString().equals("")
							||etUserPhoneNumPersonalEntryForm.getText().toString().equals("")
							||etUserIDCardPersonalEntryForm.getText().toString().equals("")){
						
						alertDialogNoCompleteInformation = new AlertDialog.Builder(getActivity()).create();
						alertDialogNoCompleteInformation.show();
						alertDialogNoCompleteInformation.getWindow().setContentView(R.layout.alert_dialog_no_complete_information);
						alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCertainAlertDialogNoCompleteInformation)
						.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								alertDialogNoCompleteInformation.dismiss();
							}
						});
						alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCancelAlertDialogNoCompleteInformation)
						.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								alertDialogNoCompleteInformation.dismiss();
							}
						});
					}else{
						alertDialogNoCompleteInformation = new AlertDialog.Builder(getActivity()).create();
						alertDialogNoCompleteInformation.show();
						alertDialogNoCompleteInformation.getWindow().setContentView(R.layout.alert_dialog_be_sure_submit);
						alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCertainAlertDialogBeSureToSubmit)
						.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								alertDialogNoCompleteInformation.dismiss();
									//TODO  new connection
								String url = null;
								try {
									url = Config.SERVER_URL+"Users/treatUserAttendNew?" +
											Config.KEY_GAME_ID +"="+ gameId+"&" +
											Config.KEY_USER_ATTEND_EID +"="+ eId+"&" +
											Config.KEY_UID +"="+ Config.getCachedUserUid(getActivity().getApplicationContext())+"&" +
											Config.KEY_USER_ATTEND_USER_NAME +"="+ URLEncoder.encode(etUserNamePersonalEntryForm.getText().toString(),"utf-8")+"&" +
											Config.KEY_USER_ATTEND_TEL +"="+ etUserPhoneNumPersonalEntryForm.getText().toString()+"&" +
											Config.KEY_USER_ATTEND_SEX +"="+ URLEncoder.encode(tvUserSexPersonalEntryForm.getText().toString(),"utf-8")+"&" +
											Config.KEY_USER_ATTEND_IDCARD +"="+ etUserIDCardPersonalEntryForm.getText().toString()+"&" +
											Config.KEY_USER_ATTEND_AGE +"="+ etUserAgePersonalEntryForm.getText().toString();
								} catch (UnsupportedEncodingException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								StringRequest  stringRequest = new StringRequest(
					                    Request.Method.GET,
					                    url,
					                    new Response.Listener<String>() {
					 
					                        @Override
					                        public void onResponse(String s) {
					                            try {
					                                JSONObject jsonObject = new JSONObject(s);
					                                if(jsonObject.getString("result").equals("1")){
					                                	Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
					                                	if(tvPayFeePersonalEntryForm.getText().toString().equals("0")||tvPayFeePersonalEntryForm.getText().toString().equals("免费")){	
					        							}else{
					        									Intent intent = new Intent(getActivity(),AtyPay.class);
					        									intent.putExtra(Config.KEY_ID, jsonObject.getJSONObject("data").getString("id"));
					        									intent.putExtra(Config.KEY_USER_ATTEND_EPAYFEE, tvPayFeePersonalEntryForm.getText().toString());
					        									intent.putExtra(Config.KEY_USER_ATTEND_ENAME, tvGameItemPersonalEntryForm.getText().toString());
					        									intent.putExtra(Config.KEY_GAME_NAME, tvGameNamePersonalEntryForm.getText().toString());
					        									intent.putExtra("apayfee",tvPayFeePersonalEntryForm.getText().toString());
					        									intent.putExtra("type", "game");
					        									startActivity(intent);
					        								}
					                                	AtyEntryForm.instance.finish();
					                                }else{
					                                	Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
					                                }
					                                
					                            } catch (JSONException e) {
					                                e.printStackTrace();
					                            }
					                        }
					                    },
					                    new Response.ErrorListener() {
					 
					                        @Override
					                        public void onErrorResponse(VolleyError volleyError) {
					                        	Toast.makeText(getActivity().getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
					                        }
					                    }){
					 
					                @Override
					                protected Map<String, String> getParams() throws AuthFailureError {
					                    Map<String,String> map = new HashMap<String,String>();
					                    return map;
					                }
					            };
					            mRequestQueue.add(stringRequest);
								
							
							}
						});
						alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCancelAlertDialogBeSureToSubmit)
						.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								alertDialogNoCompleteInformation.dismiss();
							}
						});
					}
				}
			});
			
			tvGameItemPersonalEntryForm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 点击出现项目选择   alertDialog
						if(!jsonObj.isNull("eventInfo")){
						new AlertDialog.Builder(getActivity())
						.setItems(items,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
							int which) {
								dialog.dismiss();
								try {
									tvGameItemPersonalEntryForm.setText(listJsonObj.get(which).getString(Config.KEY_USER_ATTEND_ENAME));
									tvPayFeePersonalEntryForm.setText(listJsonObj.get(which).getString(Config.KEY_USER_ATTEND_EPAYFEE));
									eId = listJsonObj.get(which).getString(Config.KEY_USER_ATTEND_EID);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

						}).show();
						}
					
				}
			});
			etUserNamePersonalEntryForm.setText(jsonObj.getJSONObject(Config.KEY_USER_INFO).getString(Config.KEY_USER_NAME));
			tvUserSexPersonalEntryForm.setText(jsonObj.getJSONObject(Config.KEY_USER_INFO).getString(Config.KEY_SEX));
			tvUserSexPersonalEntryForm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 点击出现选择性别
					new AlertDialog.Builder(getActivity())
					.setItems(new String[]{"男", "女"},
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
						int which) {
							dialog.dismiss();
							switch (which) {
							case 0:
								tvUserSexPersonalEntryForm.setText("男");
								break;
							case 1:
								tvUserSexPersonalEntryForm.setText("女");
								break;
							default:
								break;
							}
							Toast.makeText(getActivity(), 
							"你选择了: " + which, Toast.LENGTH_SHORT).show();
						}
			
					}).show();
					
				}
			});
			etUserAgePersonalEntryForm.setText(jsonObj.getJSONObject(Config.KEY_USER_INFO).getString(Config.KEY_AGE));
			etUserPhoneNumPersonalEntryForm.setText(jsonObj.getJSONObject(Config.KEY_USER_INFO).getString(Config.KEY_TEL));
			etUserIDCardPersonalEntryForm.setText(jsonObj.getJSONObject(Config.KEY_USER_INFO).getString(Config.KEY_ID_CARD));
			
			cbAgreeRulesPersonalEntryForm.setChecked(true);
			cbAgreeRulesPersonalEntryForm.setClickable(true);
			cbAgreeRulesPersonalEntryForm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!cbAgreeRulesPersonalEntryForm.isChecked()){
						cbAgreeRulesPersonalEntryForm.setButtonDrawable(android.R.drawable.checkbox_on_background);
					}else{
						cbAgreeRulesPersonalEntryForm.setButtonDrawable(android.R.drawable.checkbox_off_background);
					}
				}
			});
			cbAgreeRulesPersonalEntryForm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO 勾选和不勾选的动作分别是？？？？
					if(isChecked){
						Toast.makeText(getActivity(), "请阅读协议", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return view;
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
