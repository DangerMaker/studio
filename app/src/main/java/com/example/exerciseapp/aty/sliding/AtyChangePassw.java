package com.example.exerciseapp.aty.sliding;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
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

public class AtyChangePassw extends BaseActivity {
    private Toolbar toolbar;
    private TextView pageTitle;
    private EditText uPhone;
    private EditText uPrePassw;
    private EditText uNewPassw;
    private EditText uNewPasswAgain;
    private Button confirm;
    private RequestQueue mRequestQueue;
    private ProgressDialog progressDialog;
    String token = MyApplication.getInstance().getToken();
    String uid = MyApplication.getInstance().getUid();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mRequestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.aty_change_passw);
        initView();
        setTitleBar();
        initListener();
    }

    private void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("修改密码");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyChangePassw.this.finish();
            }
        });
    }

    private void initView() {
        uPhone = (EditText) findViewById(R.id.change_passw_phone);
        if (Config.TOURIST_MODE) {
            Toast.makeText(getApplicationContext(), "尚未登陆", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            uPhone.setText(Config.getCachedUserTel(getApplicationContext()));
            uPhone.setEnabled(false);
        }
        uPrePassw = (EditText) findViewById(R.id.change_passw_pre_passw);
        uNewPassw = (EditText) findViewById(R.id.change_passw_new_passw);
        uNewPasswAgain = (EditText) findViewById(R.id.change_passw_new_passw_again);
        confirm = (Button) findViewById(R.id.change_passw_confirm);
    }

    private void initListener() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //密码为空
                if (TextUtils.isEmpty(uPrePassw.getText()) || TextUtils.isEmpty(uNewPassw.getText()) || TextUtils.isEmpty(uNewPasswAgain.getText())) {
                    Toast.makeText(getApplicationContext(), "请完善信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                //两次密码不相等
                if (!uNewPassw.getText().toString().equals(uNewPasswAgain.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //正则判断输入密码是否符合格式
                if (!CheckInput.isPassword(uNewPassw.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "\t\t密码格式错误,\n请输入4-10位字母数字下划线组合", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(AtyChangePassw.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("请稍等……");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://101.200.214.68/py/user", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        WindowManager wm = AtyChangePassw.this.getWindowManager();
                        int width = wm.getDefaultDisplay().getWidth();
                        int height = wm.getDefaultDisplay().getHeight();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String reason = jsonObject.getString("desc");
                            if (jsonObject.getInt("result") == 0) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        reason, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.getView().setMinimumWidth(width / 2);
                                LinearLayout toastView = (LinearLayout) toast.getView();
                                ImageView imageCodeProject = new ImageView(getApplicationContext());
                                imageCodeProject.setImageResource(R.drawable.toast_success);
                                toastView.addView(imageCodeProject, 0);
                                toast.show();
                            } else if (jsonObject.getInt("result") == 1) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        reason, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.getView().setMinimumWidth(width / 2);
                                LinearLayout toastView = (LinearLayout) toast.getView();
                                ImageView imageCodeProject = new ImageView(getApplicationContext());
                                imageCodeProject.setImageResource(R.drawable.toast_success);
                                toastView.addView(imageCodeProject, 0);
                                toast.show();
                                uPrePassw.setText("");
                                uNewPassw.setText("");
                                uNewPasswAgain.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(Config.KEY_UID, uid);
                        map.put(Config.KEY_ORI_PASSWORD, uPrePassw.getText().toString());
                        map.put(Config.KEY_NEW_PASSWORD, uNewPassw.getText().toString());
                        map.put("token", token);
                        map.put("version", "3.0");
                        map.put("action", "change_password");
                        return map;
                    }
                };
                mRequestQueue.add(stringRequest);
            }
        });
    }
}
