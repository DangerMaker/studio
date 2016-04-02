package com.example.exerciseapp.aty.login;
/*
 * *登录界面
 */
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.utils.SharedPreferencesHelper;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.MainActivity;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.example.exerciseapp.utils.CheckInput;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.aa.e;

import dmax.dialog.SpotsDialog;

public class AtyLogin extends Activity {

	
	private EditText etPhoneNum;
	private EditText etPassword;
    private RequestQueue mRequestQueue;  
    ProgressDialog progressDialog;
    SpotsDialog spotsDialog;
    private RelativeLayout root;
    private Button btnLoginNow;
    private Button btnForgetPwd;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);
		PushAgent.getInstance(this).onAppStart();
		mRequestQueue =  Volley.newRequestQueue(this);
		etPhoneNum = (EditText) findViewById(R.id.etPhoneNumLogin);
		etPassword = (EditText) findViewById(R.id.etPasswordLogin);
		//返回键监听事件
		root = (RelativeLayout) findViewById(R.id.rootAtyLogin);
		btnLoginNow = (Button) findViewById(R.id.btnLoginNow);
		btnForgetPwd = (Button) findViewById(R.id.btnForgetPassword);
		controlKeyboardLayout(root, btnLoginNow);
		//找回密码
		btnForgetPwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AtyLogin.this,AtyForgetPwd.class));
			}
		});
		//立即注册按钮监听事件
		findViewById(R.id.btnLoginNow).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//电话号码为空
				if(TextUtils.isEmpty(etPhoneNum.getText())){
					Toast.makeText(getApplicationContext(), "电话号码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				//电话号码格式不对
				if(!CheckInput.isPhoneNum(etPhoneNum.getText().toString())){
					Toast.makeText(getApplicationContext(), "电话号码格式错误", Toast.LENGTH_SHORT).show();
					return;
				}
				//密码不为空
				if(TextUtils.isEmpty(etPassword.getText())){
					Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				//密码格式不对
				if(!CheckInput.isPassword(etPassword.getText().toString())){
					Toast.makeText(getApplicationContext(), "密码格式错误", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//根据返回状态，输出相关信息，并保存用户uid
//				progressDialog = new ProgressDialog(AtyLogin.this);
//				progressDialog.setCanceledOnTouchOutside(false);
//				progressDialog.setMessage("登录中……");
//				progressDialog.show();
				spotsDialog = new SpotsDialog(AtyLogin.this);
				spotsDialog.show();
				StringRequest  stringRequest = new StringRequest(
	                    Request.Method.POST,
	                    Config.SERVER_URL+"Users/loginNew",
	                    new Response.Listener<String>() {
	 
	                        @Override
	                        public void onResponse(String s) {
	                            try {
//	                            	progressDialog.dismiss();
	                                JSONObject jsonObject = new JSONObject(s);
	                                if(jsonObject.getString("result").equals("1")){
	                                	Config.cacheUserUid(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_UID));
										Config.cacheUserTel(getApplicationContext(), etPhoneNum.getText().toString());
										String uid =jsonObject.getJSONObject("data").getString(Config.KEY_UID);
										SharedPreferencesHelper.getInstance(AtyLogin.this).setValue("uid",uid);
										((MyApplication)getApplication()).setUid(uid);
										finish();
	                                	startActivity(new Intent(AtyLogin.this,AtySlidingHome.class));
	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
	                                	Config.STATUS_FINISH_ACTIVITY = 1;
	                                	Config.TOURIST_MODE = false;
	                                	MainActivity.mPushAgent.addAlias(jsonObject.getJSONObject("data").getString(Config.KEY_UID), "userId");
	                                }else{
	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
	                                }
	                                spotsDialog.dismiss();
	                            } catch (JSONException e) {
//	                            	progressDialog.dismiss();
	                            	spotsDialog.dismiss();
	                                e.printStackTrace();
	                            } catch (e e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
	                        }
	                    },
	                    new Response.ErrorListener() {
	 
	                        @Override
	                        public void onErrorResponse(VolleyError volleyError) {
//	                        	progressDialog.dismiss();
	                        	spotsDialog.dismiss();
	                        	Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
	                        }
	                    }){
	 
	                @Override
	                protected Map<String, String> getParams() throws AuthFailureError {
	                    Map<String,String> map = new HashMap<String,String>();
	                    map.put(Config.KEY_TEL,etPhoneNum.getText().toString());
	                    map.put(Config.KEY_PASSWORD,etPassword.getText().toString());
	                    return map;
	                }
	            };
	            mRequestQueue.add(stringRequest);
				
//				new RegisterConnection(Config.SERVER_URL+"Users/loginNew", "POST", new RegisterConnection.SuccessCallBack() {
//					
//					@Override
//					public void onSuccess(String uid, String result) {
//						//缓存用户uid并跳转到个性化界面
//						Config.cacheUserUid(getApplicationContext(), uid);
//						Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//						startActivity(new Intent(AtyLogin.this,AtyPersonalize.class));
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
//				}, Config.KEY_TEL,etPhoneNum.getText().toString(),Config.KEY_PASSWORD,etPassword.getText().toString());
//				
			}
		});
		
		
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
