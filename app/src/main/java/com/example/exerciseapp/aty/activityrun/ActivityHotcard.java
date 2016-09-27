package com.example.exerciseapp.aty.activityrun;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sonchcng on 16/7/30.
 */
public class ActivityHotcard extends Activity implements View.OnClickListener {
    private EditText hotcardtitle;
    private EditText hotcardcontent;
    private TextView hotcardcommit;
    private ImageView goback;
    private RequestQueue mRequestQueue;
    private static final String BASEURL = "http://101.200.214.68/py/notepri?action=release_note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_friend_commit_hotcard);
        InitView();
        mRequestQueue = Volley.newRequestQueue(this);
    }

    private void InitView() {
        hotcardtitle = (EditText) findViewById(R.id.hotcardtitle);
        hotcardcontent = (EditText) findViewById(R.id.hotcardcontent);
        hotcardcommit = (TextView) findViewById(R.id.hotcardcommit);
        goback = (ImageView) findViewById(R.id.goback);
        hotcardcommit.setOnClickListener(this);
        goback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:
                finish();
                break;
            case R.id.hotcardcommit:
                if (TextUtils.isEmpty(hotcardtitle.getText().toString())) {
                    Toast.makeText(this, "标题不能为空", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(hotcardcontent.getText().toString())) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_LONG).show();
                } else {
                    docommithotcard();
                }
                break;
            default:
                break;
        }
    }

    private void docommithotcard() {
        String title = hotcardtitle.getText().toString();
        String content = hotcardcontent.getText().toString();
        String url = BASEURL + "&token=" + MyApplication.getInstance().getToken() +
                "&version=3.2" + "&uid=" + MyApplication.getInstance().getUid()
                + "&title=" + title + "&content=" + content;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("result").equals("1")) {
                                ScreenUtils.show_msg(ActivityHotcard.this,"发布成功");
                                finish();
                            } else {
                                Toast.makeText(ActivityHotcard.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
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

}
