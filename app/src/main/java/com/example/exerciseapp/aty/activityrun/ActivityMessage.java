package com.example.exerciseapp.aty.activityrun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.MessageAdapter;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Cherie_No.47 on 2016/4/14 19:36.
 * Email jascal@163.com
 */
public class ActivityMessage extends Activity {
    private ListView listView;
    private MessageAdapter adapter;
    private List<JSONObject> list = new LinkedList<JSONObject>();
    private RequestQueue mRequestQueue;
    private JSONArray cachedMessage;
    ImageView goback;
    TextView message_none_toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_message);
        goback = (ImageView) findViewById(R.id.goback);
        message_none_toast = (TextView) findViewById(R.id.message_none_toast);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.listView_);
        cachedMessage = Config.getCachedMessage(getApplicationContext());
        try {
            if (cachedMessage != null) {
                for (int i = 0; i < cachedMessage.length(); i++) {
                    list.add(cachedMessage.getJSONObject(i));
                }
            } else {
                cachedMessage = new JSONArray();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (0 == list.size()) {
            message_none_toast.setVisibility(View.VISIBLE);
        } else {
            message_none_toast.setVisibility(View.GONE);
        }
        adapter = new MessageAdapter(ActivityMessage.this, list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.STATUS_HAS_MESSAGE = false;
        String url = "" + "http://101.200.214.68/py/system?action=get_sysinfo&uid=";
        StringRequest stringRequestMyEntryForm = new StringRequest(Request.Method.GET,
                url + Config.getCachedUserUid(getApplicationContext()), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("1")) {
                        try {
                            list.clear();
                            for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                                list.add(jsonObject.getJSONArray("data").getJSONObject(i));
                                cachedMessage.put(jsonObject.getJSONArray("data").getJSONObject(i));
                            }
                            Config.cacheMessage(getApplicationContext(), cachedMessage);
                            adapter.notifyDataSetChanged();
                            if (0 == list.size()) {
                                message_none_toast.setVisibility(View.VISIBLE);
                            } else {
                                message_none_toast.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ActivityMessage.this, Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
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
