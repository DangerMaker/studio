package com.example.exerciseapp.aty.organzie;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.PreGridAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectSubjectActivity extends BindActivity {
    private RequestQueue mRequestQueue;
    private int selectedNum = 0;
    private Map<Integer, String> map = new HashMap<Integer, String>();
    private RelativeLayout toolbar;
    private TextView pageTitle;
    @Bind(R.id.user_prefered_gridView)
    GridView gridView;
    private List<JSONObject> list = new ArrayList<>();
    private PreGridAdapter preGridAdapter;
    private Toast mToast;

    String name;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_user_prefered_project);
        mRequestQueue = Volley.newRequestQueue(this);
        name = getIntent().getStringExtra("name");
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
                        showToast("最多选择三项哦");
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
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
    }

    @OnClick(R.id.btnsave)
    protected void submit() {
        String data = preGridAdapter.getChoiceString();
        if(name.equals("0")) {
            CreateZhuzhiFragment.model.subject = data;
        }else{
            CreateClubFragment.model.subject = data;
        }
        finish();
//        data = data.replace("[", "");
//        data = data.replace("]", "");
//        data = data.replace(" ", "");
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,
//                Config.SERVER_URL + "Users/treatNewInterest?" + Config.KEY_UID + "="
//                        + Config.getCachedUserUid(getApplicationContext()) + "&" + "interest" + "=" + data,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(s);
//                            Toast.makeText(SelectSubjectActivity.this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        finish();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                return map;
//            }
//        };
//        mRequestQueue.add(stringRequest);
    }

    public void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(SelectSubjectActivity.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void onBackPressed() {
        cancelToast();
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://101.200.214.68/py/tag", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("result") == 1) {
                        JSONArray jsonArr = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            list.add(jsonArr.getJSONObject(i));
//                            if(jsonArr.getJSONObject(i).getString("select").equals("1")){
//                                preGridAdapter.setChoiced(jsonArr.getJSONObject(i).getInt("id"));
//                            }
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
