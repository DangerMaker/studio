package com.example.exerciseapp.aty.login;
/*
 * 第一个注册界面
 */
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.example.exerciseapp.listener.TextClickSpan;
import com.example.exerciseapp.utils.CheckInput;
import com.umeng.message.PushAgent;

public class AtyRegisterHomePage extends Activity {

	private EditText etPhoneNum,etPassword,etConfigPassword,etIdentifyCode;
	private TextView tvSecretAndInformation;
	private static final Pattern secretPattern = Pattern.compile("隐私政策");
	private static final Pattern informationPattern = Pattern.compile("使用条款");
	private RequestQueue mRequestQueue; 
	ProgressDialog progressDialog;
	private RelativeLayout root;
    private Button btnRegisterNow;
    private Button btnTouristLogin;
    private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PushAgent.getInstance(this).onAppStart();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_register_home_page);
		mRequestQueue =  Volley.newRequestQueue(this);
		etPhoneNum = (EditText) findViewById(R.id.etTouristPhoneNum);
		etPassword = (EditText) findViewById(R.id.etTouristPassword);
		etConfigPassword = (EditText) findViewById(R.id.etTouristConfigPassword);
		etIdentifyCode = (EditText) findViewById(R.id.etTouristIdentifyCode);
		btnRegisterNow = (Button) findViewById(R.id.btnRegisterNow);
		root = (RelativeLayout) findViewById(R.id.rootAtyRegister);
		btnTouristLogin = (Button) findViewById(R.id.btnTouristLogin);
		btnTouristLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Config.TOURIST_MODE = true;
				startActivity(new Intent(AtyRegisterHomePage.this,AtySlidingHome.class));
			}
		});
		controlKeyboardLayout(root, btnRegisterNow);
		etPhoneNum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnTouristLogin.setVisibility(View.GONE);
				//游客登录按钮响应事件
//				findViewById(R.id.btnTouristLogin).setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						startActivity(new Intent(AtyRegisterHomePage.this,AtyRegisterPage.class));
//					}
//				});
				
				
			}
		});

		
		//点击验证码，获取验证码
		findViewById(R.id.btnTouristGetIdentifyCode).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//利用正则表达式判断输入是否为电话号码
				if(TextUtils.isEmpty(etPhoneNum.getText())){
					Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!CheckInput.isPhoneNum(etPhoneNum.getText().toString())){
					Toast.makeText(getApplicationContext(), "手机号码格式错误", Toast.LENGTH_LONG).show();
					return;
				}
				
				//获取验证码通信
				progressDialog = new ProgressDialog(AtyRegisterHomePage.this);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setMessage("获取中……");
				progressDialog.show();
				StringRequest stringRequest = new StringRequest(Request.Method.POST,
						Config.SERVER_URL + "Users/getCode", new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							progressDialog.dismiss();
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.getString("result").equals("1")) {
								Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
								Config.STATUS_FINISH_ACTIVITY = 1;
								progressDialog.dismiss();
							} else {
								Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
					}
				}) {

					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> map = new HashMap<String, String>();
						map.put(Config.KEY_TEL, etPhoneNum.getText().toString());
						return map;
					}
				};
				mRequestQueue.add(stringRequest);

				// new UserGetCodeConnection(Config.SERVER_URL+"Users/getCode",
				// "POST", new UserGetCodeConnection.SuccessCallBack() {
				//
				// @Override
				// public void onSuccess(String result) {
				// Toast.makeText(getApplicationContext(), result,
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				// }, new UserGetCodeConnection.FailCallBack() {
				//
				// @Override
				// public void onFail(String result) {
				// Toast.makeText(getApplicationContext(), result,
				// Toast.LENGTH_LONG).show();
				// return;
				// }
				// }, Config.KEY_TEL,etPhoneNum.getText().toString());
			}
		});
		/*
		 * 点击文本跳转，未实现
		 */
		tvSecretAndInformation = (TextView) findViewById(R.id.tvSecretAndInformation);
		SpannableString ss = new SpannableString(tvSecretAndInformation.getText());
		
		tvSecretAndInformation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AtyRegisterHomePage.this,AtyUserLawItem.class);
				intent.putExtra("title","使用条款");
				intent.putExtra("url","http://101.200.214.68/index.php/Api/System/usingClause");
				startActivity(intent);
//				final AlertDialog alertDialog = new AlertDialog.Builder(AtyRegisterHomePage.this).create();
//				alertDialog.show();
//				alertDialog.getWindow().setContentView(R.layout.alert_dialog_show_web_view);
//				alertDialog.getWindow().findViewById(R.id.btnOk)
//				.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						alertDialog.dismiss();
//					}
//				});
//				webView = (WebView) alertDialog.getWindow().findViewById(R.id.webView);
//				webView.loadUrl("http://101.200.214.68/index.php/Api/System/usingClause");
				
//				Toast.makeText(AtyRegisterHomePage.this,"使用条款",2).show();
//				webView = new WebView(AtyRegisterHomePage.this);
//				webView.loadUrl("http://101.200.214.68/index.php/Api/System/usingClause");
			}
		});
		
		setKeyworkClickable(tvSecretAndInformation, ss, secretPattern, new TextClickSpan(new OnTextViewClickListener() {
			
			@Override
			public void setStyle(TextPaint ds) {
				ds.setColor(Color.rgb(58, 145, 173));
				ds.setUnderlineText(false);
			}
			
			@Override
			public void clickTextView() {
				//TODO
			}
		}));
		setKeyworkClickable(tvSecretAndInformation, ss, informationPattern, new TextClickSpan(new OnTextViewClickListener() {
			
			@Override
			public void setStyle(TextPaint ds) {
				ds.setColor(Color.rgb(58, 145, 173));
				ds.setUnderlineText(false);
			}
			
			@Override
			public void clickTextView() {
				//to-do
				Toast.makeText(AtyRegisterHomePage.this,"使用条款",Toast.LENGTH_SHORT).show();
				webView = new WebView(AtyRegisterHomePage.this);
				webView.loadUrl("http://101.200.214.68/index.php/Api/System/usingClause");
			}
		}));
		
		//立即注册按钮响应事件
		findViewById(R.id.btnRegisterNow).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				//密码为空
				if(TextUtils.isEmpty(etPassword.getText())){
					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				//确认密码为空
				if(TextUtils.isEmpty(etConfigPassword.getText())){
					Toast.makeText(getApplicationContext(), "确认密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				//两次密码不相等
				if(!etPassword.getText().toString().equals(etConfigPassword.getText().toString())){
					Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				//正则判断输入密码是否符合格式
				if(!CheckInput.isPassword(etPassword.getText().toString())){
					Toast.makeText(getApplicationContext(), "\t\t密码格式错误,\n请输入4-10位字母数字下划线组合", Toast.LENGTH_SHORT).show();
					return;
				}
				//注册成功，返回用户uid并缓存uid
				progressDialog = new ProgressDialog(AtyRegisterHomePage.this);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setMessage("注册中……");
				progressDialog.show();
				StringRequest  stringRequest = new StringRequest(
	                    Request.Method.POST,
	                    Config.SERVER_URL+"Users/register",
	                    new Response.Listener<String>() {
	 
	                        @Override
	                        public void onResponse(String s) {
	                            try {
	                            	progressDialog.dismiss();
	                                JSONObject jsonObject = new JSONObject(s);
	                                if(jsonObject.getString("result").equals("1")){
	                                	Config.cacheUserUid(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_UID));
	                                	startActivity(new Intent(AtyRegisterHomePage.this, AtySlidingHome.class));
	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
	                                	Config.STATUS_FINISH_ACTIVITY = 1;
										Config.TOURIST_MODE = false;
	                                	finish();
	                                }else{
	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
	                                }
	                                
	                            } catch (JSONException e) {
	                            	progressDialog.dismiss();
	                                e.printStackTrace();
	                            }
	                        }
	                    },
	                    new Response.ErrorListener() {
	 
	                        @Override
	                        public void onErrorResponse(VolleyError volleyError) {
	                        	progressDialog.dismiss();
	                        	Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
	                        }
	                    }){
	 
	                @Override
	                protected Map<String, String> getParams() throws AuthFailureError {
	                    Map<String,String> map = new HashMap<String,String>();
	                    map.put(Config.KEY_TEL,etPhoneNum.getText().toString());
	                    map.put(Config.KEY_CODE,etIdentifyCode.getText().toString());
	                    map.put(Config.KEY_PASSWORD,etPassword.getText().toString());
	                    return map;
	                }
	            };
	            mRequestQueue.add(stringRequest);
//				new RegisterConnection(Config.SERVER_URL+"Users/register", Config.POST, new RegisterConnection.SuccessCallBack() {
//					
//					@Override
//					public void onSuccess(String uid, String result) {
//						//注册成功，保存用户uid，并跳转到主页面
//						Config.cacheUserUid(getApplicationContext(), uid);
//						Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//						startActivity(new Intent(AtyRegisterHomePage.this,AtySlidingHome.class));
//						setResult(Config.STATUS_FINISH_ACTIVITY);
//						finish();
//						return;
//					}
//				}, new RegisterConnection.FailCallBack() {
//					
//					@Override
//					public void onFail(String result) {
//						Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//						return;
//					}
//				}, Config.KEY_TEL, etPhoneNum.getText().toString(),
//				Config.KEY_CODE,etIdentifyCode.getText().toString(),
//				Config.KEY_PASSWORD,etPassword.getText().toString());
			}
		});
	}
	public interface OnTextViewClickListener{
		public void clickTextView();
		public void setStyle(TextPaint ds);
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
	
	 /**
     * @param root 最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
	
}
