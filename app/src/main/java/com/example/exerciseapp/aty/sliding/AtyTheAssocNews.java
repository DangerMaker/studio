package com.example.exerciseapp.aty.sliding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.NewsListAdapter;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Cherie_No.47 on 2016/4/7 14:24.
 * Email jascal@163.com
 */
public class AtyTheAssocNews extends BaseActivity {
    private RequestQueue mRequestQueue;
    private String aID;
    private LinkedList<JSONObject> mNewsListItems = new LinkedList<JSONObject>();
    private NewsListAdapter mNewsAdapter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text)
    TextView toolBarTitle;
    @Bind(R.id.pull_to_refresh_for_the_assocnews)
    PullToRefreshListView mPullToRefreshNewsListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_assoc_news);
        mRequestQueue = Volley.newRequestQueue(this);
        aID = getIntent().getStringExtra(Config.KEY_AID);
        setTitleBar();
        getInfo();
        mPullToRefreshNewsListView.setRefreshing(false);
    }

    private void setTitleBar() {
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        toolBarTitle.setText("组织资讯");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyTheAssocNews.this.finish();
            }
        });
    }

    public void getInfo() {
        mNewsListItems = new LinkedList<JSONObject>();
        mNewsAdapter = new NewsListAdapter(AtyTheAssocNews.this, mNewsListItems);
        mPullToRefreshNewsListView.setAdapter(mNewsAdapter);
        mPullToRefreshNewsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadNews();
            }
        });
        mPullToRefreshNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(AtyTheAssocNews.this, AtyNewsDetails.class);
                intent.putExtra("newsDetails", mNewsListItems.get(position - 1).toString());
                startActivity(intent);
                return;
            }
        });
        loadNews();
    }

    private void loadNews(){
        StringRequest stringGetNewsForTheAssoc = new StringRequest(Request.Method.GET,
                "http://101.200.214.68/index.php/Api/Assoc/getAssocNews", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("result") == 1) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            mNewsListItems.add(data.getJSONObject(i));
                        }
                        mNewsAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_AID, aID);
                return map;
            }
        };
        mRequestQueue.add(stringGetNewsForTheAssoc);
        return;
    }
}
