package com.example.exerciseapp.aty.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.utils.CheckInput;
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

import java.util.HashMap;
import java.util.Map;

public class AtyForgetPwd extends Activity {
	private EditText etPhoneNum;
	private EditText etPassword;
	private EditText etPasswordAgain;
	private EditText etCode;
    private RequestQueue mRequestQueue;  
    ProgressDialog progressDialog;
    
    private RelativeLayout root;
    private Button btnCertain;
    private Button btnGetCode;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_forget_pwd);
		PushAgent.getInstance(this).onAppStart();
		mRequestQueue =  Volley.newRequestQueue(this);
		etPhoneNum = (EditText) findViewById(R.id.etPhoneNum);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPasswordAgain = (EditText) findViewById(R.id.etPasswordAgain);
		etCode = (EditText) findViewById(R.id.etCode);
		//返回键监听事件
		root = (RelativeLayout) findViewById(R.id.root);
		btnCertain = (Button) findViewById(R.id.btnCertain);
		btnGetCode = (Button) findViewById(R.id.btnGetCode);
		controlKeyboardLayout(root, btnCertain);
		btnGetCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
				Toast.makeText(getApplicationContext(), "正在发送验证码", Toast.LENGTH_SHORT).show();
				StringRequest  stringRequest = new StringRequest(
	                    Request.Method.POST,
	                    Config.SERVER_URL+"Users/sendCode",
	                    new Response.Listener<String>() {
	 
	                        @Override
	                        public void onResponse(String s) {
	                            try {
	                                JSONObject jsonObject = new JSONObject(s);
	                                if(jsonObject.getString("result").equals("1")){
	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
	                                }else{
	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
	                                }
	                                
	                            } catch (JSONException e) {
	                                e.printStackTrace();
	                            }
	                        }
	                    },
	                    new Response.ErrorListener() {
	 
	                        @Override
	                        public void onErrorResponse(VolleyError volleyError) {
	                        	Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
	                        }
	                    }){
	 
	                @Override
	                protected Map<String, String> getParams() throws AuthFailureError {
	                    Map<String,String> map = new HashMap<String,String>();
	                    map.put(Config.KEY_TEL,etPhoneNum.getText().toString());
	                    return map;
	                }
	            };
	            mRequestQueue.add(stringRequest);
			}
		});
		
		findViewById(R.id.btnCertain).setOnClickListener(new OnClickListener() {
				
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
				if(!etPassword.getText().toString().equals(etPasswordAgain.getText().toString())){
					Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//根据返回状态，输出相关信息，并保存用户uid
				progressDialog = new ProgressDialog(AtyForgetPwd.this);
				progressDialog.setCanceledOnTouchOutside(false);
//				progressDialog.setMessage("登录中……");
				progressDialog.show();
				StringRequest  stringRequest = new StringRequest(
	                    Request.Method.POST,
	                    Config.SERVER_URL+"Users/findPassword",
	                    new Response.Listener<String>() {
	 
	                        @Override
	                        public void onResponse(String s) {
	                            try {
	                            	progressDialog.dismiss();
	                                JSONObject jsonObject = new JSONObject(s);
	                                if(jsonObject.getString("result").equals("1")){
	                                	finish();
	                                	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
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
	                    map.put(Config.KEY_CODE,etCode.getText().toString());
	                    map.put(Config.KEY_PASSWORD,etPassword.getText().toString());
	                    return map;
	                }
	            };
	            mRequestQueue.add(stringRequest);
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
    
    /*
     * 验证码倒计时
     */
    public class TimeCountUtils extends CountDownTimer {
    	private Activity mActivity;
    	private Button btn;// 按钮

    	// 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
    	public TimeCountUtils(Activity mActivity, long millisInFuture,
    			long countDownInterval, Button btn) {
    		super(millisInFuture, countDownInterval);
    		this.mActivity = mActivity;
    		this.btn = btn;
    	}

    	@SuppressLint("NewApi")
    	@Override
    	public void onTick(long millisUntilFinished) {
    		btn.setClickable(false);// 设置不能点击
    		btn.setText(millisUntilFinished / 1000 + "秒后可重新发送");// 设置倒计时时间
    		// 设置按钮为灰色，这时是不能点击的
    		btn.setBackground(mActivity.getResources().getDrawable(
    				R.drawable.shape_btn_grey_5dip));
    		Spannable span = new SpannableString(btn.getText().toString());// 获取按钮的文字
    		span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,
    				Spannable.SPAN_INCLUSIVE_EXCLUSIVE);// 讲倒计时时间显示为红色
    		btn.setText(span);
    	}

    	@SuppressLint("NewApi")
    	@Override
    	public void onFinish() {
    		btn.setText("重新获取验证码");
    		btn.setClickable(true);// 重新获得点击
    		btn.setBackground(mActivity.getResources().getDrawable(
    				R.drawable.shape_btn_blue_radius_5dip));// 还原背景色
    	}

    }
}
