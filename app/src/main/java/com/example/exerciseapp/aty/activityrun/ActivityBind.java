package com.example.exerciseapp.aty.activityrun;

/**
 * Created by sonchcng on 16/6/12.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.wxapi.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;


public class ActivityBind extends Activity {
    private RequestQueue mRequestQueue;


    public static boolean islock = false;
    public static boolean istrue = true;
    LinearLayout phonebind;
    LinearLayout wxbind;
    TextView phonenumbind;
    TextView isbindphonetext;
    TextView isbindwxtext;
    TextView phonebindforcenter;
    ImageView isbindphone;
    ImageView isbindwx;
    LinearLayout onlyphone;
    LinearLayout phonewithnumber;
    String tel;
    int tel_status = 0;
    int wecha_status = 0;
    IWXAPI api;
    private SendAuth.Req req;
    ImageView goback;

    @Override
    protected void onResume() {
        dogetstatus();
        super.onResume();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_config_bind);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
        phonebind = (LinearLayout) findViewById(R.id.linearphonebind);
        goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wxbind = (LinearLayout) findViewById(R.id.linearwxbind);
        phonenumbind = (TextView) findViewById(R.id.phonenumbind);
        phonebindforcenter = (TextView) findViewById(R.id.phonebindforcenter);
        isbindphonetext = (TextView) findViewById(R.id.isbindphonetext);
        isbindwxtext = (TextView) findViewById(R.id.isbindwxtext);
        isbindphone = (ImageView) findViewById(R.id.isbindphone);
        isbindwx = (ImageView) findViewById(R.id.isbindwx);
        onlyphone = (LinearLayout) findViewById(R.id.onlyphone);
        phonewithnumber = (LinearLayout) findViewById(R.id.phonewithnumber);
        phonebind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tel_status == 0) {
                    Intent intent = new Intent();
                    intent.setClass(ActivityBind.this, ActivityBindphone.class);
                    startActivity(intent);
                }
            }
        });
        wxbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wecha_status == 0) {
                    req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "myapp_for_readyauthor";
                    api.sendReq(req);
                }
            }
        });
        mRequestQueue = Volley.newRequestQueue(this);
        dogetstatus();

    }

    public void dogetstatus() {
        String url = "http://101.200.214.68/py/bind?action=get_bind_info" +
                "&token=" + MyApplication.getInstance().getToken() +
                "&version=3.0" +
                "&uid=" + MyApplication.getInstance().getUid();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("result").equals("1")) {
                                tel = jsonObject.getJSONObject("data").getString("tel");
                                tel_status = jsonObject.getJSONObject("data").getInt("tel_status");
                                wecha_status = jsonObject.getJSONObject("data").getInt("wecha_status");
                                doinitview();
                            } else {
                                Toast.makeText(ActivityBind.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                            }

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

    public void doinitview() {
        if (tel_status == 1) {
            phonenumbind.setText("" + tel);
            isbindphonetext.setText("已绑定");
            isbindphonetext.setVisibility(View.VISIBLE);
            isbindphone.setVisibility(View.INVISIBLE);

            onlyphone.setVisibility(View.GONE);
            phonewithnumber.setVisibility(View.VISIBLE);
        } else {
            isbindphonetext.setText("去绑定");
            onlyphone.setVisibility(View.VISIBLE);
            phonewithnumber.setVisibility(View.GONE);
            phonebindforcenter.setGravity(RelativeLayout.CENTER_IN_PARENT);
            phonenumbind.setVisibility(View.GONE);
            isbindphonetext.setVisibility(View.VISIBLE);
            isbindphone.setVisibility(View.VISIBLE);
        }
        if (wecha_status == 1) {
            isbindwxtext.setText("已绑定");
            isbindwx.setVisibility(View.INVISIBLE);
            isbindwxtext.setVisibility(View.VISIBLE);
        } else {
            isbindwxtext.setVisibility(View.VISIBLE);
            isbindwxtext.setText("去绑定");
            isbindwx.setVisibility(View.VISIBLE);
        }
    }

}
