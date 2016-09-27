package com.example.exerciseapp.aty.login;
/*
 * *登录界面
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MainActivity;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.TabMainActivity;
import com.example.exerciseapp.utils.CheckInput;
import com.example.exerciseapp.utils.LocationPro;
import com.example.exerciseapp.utils.SharedPreferencesHelper;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.aa.e;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

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
        mRequestQueue = Volley.newRequestQueue(this);
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
                startActivity(new Intent(AtyLogin.this, AtyForgetPwd.class));
            }
        });
        //立即注册按钮监听事件
        findViewById(R.id.btnLoginNow).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //电话号码为空
                if (TextUtils.isEmpty(etPhoneNum.getText())) {
                    Toast.makeText(getApplicationContext(), "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //电话号码格式不对
                if (!CheckInput.isPhoneNum(etPhoneNum.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "电话号码格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                //密码不为空
                if (TextUtils.isEmpty(etPassword.getText())) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //密码格式不对
                if (!CheckInput.isPassword(etPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "密码格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                spotsDialog = new SpotsDialog(AtyLogin.this);
                spotsDialog.show();

                String url = "http://101.200.214.68/py/login?version=3.0&tel=" + etPhoneNum.getText().toString()
                        + "&password=" + etPassword.getText().toString();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getString("result").equals("1")) {
                                        Config.cacheUserUid(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_UID));
                                        Config.cacheUserToken(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_TOKEN));

                                        Config.cacheUserTel(getApplicationContext(), etPhoneNum.getText().toString());
                                        String uid = jsonObject.getJSONObject("data").getString(Config.KEY_UID);
                                        String token = jsonObject.getJSONObject("data").getString(Config.KEY_TOKEN);
                                        MyApplication.getInstance().setUid(uid);
                                        MyApplication.getInstance().setToken(token);
                                        SharedPreferencesHelper.getInstance(AtyLogin.this).setValue("uid", uid);
                                        SharedPreferencesHelper.getInstance(AtyLogin.this).setValue("token", token);
                                        ((MyApplication) getApplication()).setUid(uid);
                                        ((MyApplication) getApplication()).setToken(token);
//										finish();
                                        moveToMainView();
                                        Config.STATUS_FINISH_ACTIVITY = 1;
                                        Config.TOURIST_MODE = false;
                                        MainActivity.mPushAgent.addAlias(jsonObject.getJSONObject("data").getString(Config.KEY_UID), "userId");
                                    } else {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                    }
                                    spotsDialog.dismiss();
                                } catch (JSONException e) {
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
                        }) {
                };
                mRequestQueue.add(stringRequest);

            }
        });


    }

    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
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

    private void moveToMainView() {
        if (LocationPro.getInstances(AtyLogin.this).getLocation().equals("房山区")) {
            startActivity(new Intent(AtyLogin.this, AtyAdvertisement.class));
            AtyLogin.this.finish();
        } else {
            Intent intent = new Intent(new Intent(AtyLogin.this, TabMainActivity.class));
            intent.putExtra("showAd",true);
            startActivity(intent);
            AtyLogin.this.finish();
        }
    }

}
