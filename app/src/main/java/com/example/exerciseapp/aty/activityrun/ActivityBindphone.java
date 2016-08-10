package com.example.exerciseapp.aty.activityrun;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class ActivityBindphone extends Activity {

    private RequestQueue mRequestQueue;
    Button btnGetPhoneCode;
    ImageView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_bind);
        mRequestQueue = Volley.newRequestQueue(this);
        final EditText etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        final EditText etPhoneCode = (EditText) findViewById(R.id.etPhoneCode);

        goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnGetPhoneCode = (Button) findViewById(R.id.btnGetPhoneCode);
        btnGetPhoneCode.setOnClickListener(new OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                btnGetPhoneCode.setClickable(false);
                btnGetPhoneCode.setBackground(getResources().getDrawable(R.drawable.cornergray));
                CountDown cd = new CountDown(120000, 1000);
                cd.start();
                // get phone code
                String cellphone = etPhoneNumber.getText().toString();
                getPhoneCode(cellphone);
            }
        });
        Button btnfinish = (Button) findViewById(R.id.btnfinish);
        btnfinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhoneNumber.getText().toString();
                String phoneCode = etPhoneCode.getText().toString();
                getbind(phoneNumber, phoneCode);
            }
        });

    }

    public class CountDown extends CountDownTimer {

        String reGet = getResources().getString(R.string.reget);

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("NewApi")
        @Override
        public void onTick(long millisUntilFinished) {
            btnGetPhoneCode.setText(reGet + "(" + millisUntilFinished / 1000 + "s)");
        }

        @SuppressLint("NewApi")
        @Override
        public void onFinish() {
            btnGetPhoneCode.setBackground(getResources().getDrawable(R.drawable.cornerwhite));
            btnGetPhoneCode.setText(getResources().getString(R.string.getphonecode));
            btnGetPhoneCode.setClickable(true);
        }

    }

    public void getPhoneCode(String cellphone) {

        String url = "http://101.200.214.68/py/bind?action=send_bind_code" +
                "&token=" + MyApplication.getInstance().getToken() +
                "&version=3.0" +
                "&uid=" + MyApplication.getInstance().getUid() +
                "&tel=" + cellphone;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            Toast.makeText(ActivityBindphone.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }) {
        };
        mRequestQueue.add(stringRequest);
    }

    public void getbind(String cellphone, String code) {
        String url = "http://101.200.214.68/py/bind?action=treat_tel_bind" +
                "&token=" + MyApplication.getInstance().getToken() +
                "&version=3.0" +
                "&tel=" + cellphone +
                "&code=" + code;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("result").equals("1")) {
                                Toast.makeText(ActivityBindphone.this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                                ActivityBindphone.this.finish();
                            } else
                                Toast.makeText(ActivityBindphone.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }) {
        };
        mRequestQueue.add(stringRequest);
    }
}