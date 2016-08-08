package com.example.exerciseapp.aty.sliding;
/**
 * 我的报名页面
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.MyEntryFormListAdapter;
import com.example.exerciseapp.aty.activityrun.ActivityMyEntryGameDetail;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AtyMyEntryForm extends BaseActivity {

    private LinkedList<JSONObject> list = new LinkedList<JSONObject>();
    private MyEntryFormListAdapter mAdapter;
    private ListView listView;
    private JSONArray jsonArr;
    private RequestQueue mRequestQueue;

    private Toolbar toolbar;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_my_entry_form);
        mRequestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.lvMyEntryForm);
        mAdapter = new MyEntryFormListAdapter(this, list);
        listView.setAdapter(mAdapter);
        setTitleBar();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String url = list.get(position).getString("click_brief");
                    Intent intent = new Intent(AtyMyEntryForm.this, ActivityMyEntryGameDetail.class);
                    intent.putExtra("click_brief", url);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("我的报名");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyMyEntryForm.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        StringRequest stringRequestMyEntryForm = new StringRequest(
                Request.Method.GET,
                "http://101.200.214.68/py/attend?action=get_all_attend&" + Config.KEY_UID + "=" + Config.getCachedUserUid(getApplicationContext()),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("result") == 1) {
                                try {
                                    for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                                        list.addFirst(jsonObject.getJSONArray("data").getJSONObject(i));
                                    }
                                    mAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(AtyMyEntryForm.this, Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        mRequestQueue.add(stringRequestMyEntryForm);
    }
}
