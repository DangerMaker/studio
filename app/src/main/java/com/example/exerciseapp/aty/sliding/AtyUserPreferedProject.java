package com.example.exerciseapp.aty.sliding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.adapter.PreGridAdapter;
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
import com.umeng.message.PushAgent;

import butterknife.Bind;
import butterknife.OnClick;

public class AtyUserPreferedProject extends BaseActivity {
    private RequestQueue mRequestQueue;
    private int selectedNum = 0;
    private Map<Integer, String> map = new HashMap<Integer, String>();
    private Toolbar toolbar;
    private TextView pageTitle;
    @Bind(R.id.user_prefered_gridView)
    GridView gridView;
    private List<JSONObject> list = new ArrayList<>();
    private PreGridAdapter preGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_user_prefered_project);
        mRequestQueue = Volley.newRequestQueue(this);
        setTitleBar();
        initView();
    }

    private void initView() {
        preGridAdapter = new PreGridAdapter(this.getApplicationContext(), list);
        gridView.setAdapter(preGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (!preGridAdapter.setChoiced(list.get(position).getInt("id"))) {
                        Toast.makeText(AtyUserPreferedProject.this, "最多选择三项哦", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                preGridAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("我喜欢的运动项目");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyUserPreferedProject.this.finish();
            }
        });
    }

    @OnClick(R.id.btnsave)
    protected void submit() {
        String data = preGridAdapter.getChoiced().toString();
        data = data.replace("[", "");
        data = data.replace("]", "");
        data = data.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Config.SERVER_URL + "Users/treatNewInterest?" + Config.KEY_UID + "="
                        + Config.getCachedUserUid(getApplicationContext()) + "&" + "interest" + "=" + data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Toast.makeText(AtyUserPreferedProject.this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.SERVER_URL + "Users/getInterestList", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("result") == 1) {
                        JSONArray jsonArr = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            list.add(jsonArr.getJSONObject(i));
                            if(jsonArr.getJSONObject(i).getString("select").equals("1")){
                                preGridAdapter.setChoiced(jsonArr.getJSONObject(i).getInt("id"));
                            }
                        }
                        preGridAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequest);

    }
}
