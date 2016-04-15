package com.example.exerciseapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Cherie_No.47 on 2016/4/14 19:36.
 * Email jascal@163.com
 */
public class MessageFragment extends Fragment {
    private ListView listView;
    private MessageAdapter adapter;
    private List<JSONObject> list = new LinkedList<JSONObject>();
    private RequestQueue mRequestQueue;
    private JSONArray cachedMessage;
    private View view;
    @Bind(R.id.message_none_toast)
    TextView message_none_toast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_layout, null);
        ButterKnife.bind(this, view);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        listView = (ListView) view.findViewById(R.id.listView_);
        cachedMessage = Config.getCachedMessage(getActivity().getApplicationContext());
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
        adapter = new MessageAdapter(MessageFragment.this.getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Config.STATUS_HAS_MESSAGE = false;
        StringRequest stringRequestMyEntryForm = new StringRequest(Request.Method.GET,
                "http://101.200.214.68/py/system?action=get_sysinfo&" + "uid=" + Config.getCachedUserUid(MessageFragment.this.getActivity()), new Response.Listener<String>() {
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
                            Config.cacheMessage(getActivity().getApplicationContext(), cachedMessage);
                            adapter.notifyDataSetChanged();
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
                Toast.makeText(MessageFragment.this.getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
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
