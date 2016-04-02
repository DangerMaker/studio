package com.example.exerciseapp.aty.sliding;
/**
 * 现场实况界面
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.LocalSceneListAdapter;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;

public class AtyLocalScene extends BaseActivity {
    private ListView listView;
    private LinkedList<JSONObject> list = new LinkedList<JSONObject>();
    private LocalSceneListAdapter mAdapter;
    private String gameId = null;
    private RequestQueue mRequestQueue;
    private static IWXAPI api;
    private Toolbar toolbar;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mRequestQueue = Volley.newRequestQueue(this);
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        gameId = getIntent().getStringExtra(Config.KEY_GAME_ID);
        setContentView(R.layout.aty_local_scene);
        initView();
        setTitleBar();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://101.200.214.68/py/game?action=get_game_lives&" + Config.KEY_GAME_ID + "=" + gameId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject response = new JSONObject(s);
                            JSONArray data = response.getJSONArray("data");
                            list.clear();
                            for (int i = 0; i < data.length(); i++) {
                                list.add(data.getJSONObject(i));
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(AtyLocalScene.this, Config.CONNECTION_ERROR, Toast.LENGTH_LONG).show();
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

    private void initView() {
        listView = (ListView) findViewById(R.id.listViewLocalScene);
        mAdapter = new LocalSceneListAdapter(this, list);
        listView.setAdapter(mAdapter);
        listView.setFocusableInTouchMode(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AtyLocalScene.this, AtyLocalSceneDetail.class);
                if (null == list.get(position)||list.get(position).equals("")) {
                    Toast.makeText(AtyLocalScene.this,"遇到了问题，网络错误",Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("info", list.get(position).toString());
                startActivity(intent);
            }
        });
    }

    private void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("现场实况");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyLocalScene.this.finish();
            }
        });
    }
}
