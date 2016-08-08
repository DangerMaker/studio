package com.example.exerciseapp.aty.activityrun;

/**
 * 我的成绩页面
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.MyGradeAdapter;
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
import java.util.LinkedList;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ActivityMyGrades extends BaseActivity {

    private LinkedList<JSONObject> list = new LinkedList<JSONObject>();
    private MyGradeAdapter mAdapter;
    private ListView listView;
    private RequestQueue mRequestQueue;
    SpotsDialog spotsDialog;

    private Toolbar toolbar;
    private TextView pageTitle;

    @Override
    protected void onResume() {
        super.onResume();
        spotsDialog = new SpotsDialog(this);
        spotsDialog.show();
        StringRequest stringRequestMyGrade = new StringRequest(Request.Method.GET,
                "http://101.200.214.68/py/score?action=get_score_list&" + Config.KEY_UID + "=" + Config.getCachedUserUid(getApplicationContext()), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("result") == 1) {
                        list.clear();
                        for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                            list.add(jsonObject.getJSONArray("data").getJSONObject(i));
                        }
                        mAdapter.notifyDataSetChanged();
                        spotsDialog.dismiss();
                    } else {
                        spotsDialog.dismiss();
                    }

                } catch (JSONException e) {
                    spotsDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                spotsDialog.dismiss();
                Toast.makeText(ActivityMyGrades.this, Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
//				map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequestMyGrade);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_my_grade);
        mRequestQueue = Volley.newRequestQueue(this);
        mAdapter = new MyGradeAdapter(this, list);
        listView = (ListView) findViewById(R.id.lvMyGrade);
        listView.setAdapter(mAdapter);
        setTitleBar();
    }

    // 設置沉浸式菜单栏
    @SuppressLint("NewApi")
    public void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("我的成绩");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMyGrades.this.finish();
            }
        });
    }

}
