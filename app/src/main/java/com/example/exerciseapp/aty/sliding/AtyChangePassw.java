package com.example.exerciseapp.aty.sliding;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.umeng.message.PushAgent;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        uPrePassw = (EditText) findViewById(R.id.change_passw_pre_passw);
        uNewPassw = (EditText) findViewById(R.id.change_passw_new_passw);
        uNewPasswAgain = (EditText) findViewById(R.id.change_passw_new_passw_again);
        confirm = (Button) findViewById(R.id.change_passw_confirm);
    }

    private void initListener() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SERVER_URL + "Users/shareFunc", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                        return map;
                    }
                };
                mRequestQueue.add(stringRequest);
            }
        });
    }
}
