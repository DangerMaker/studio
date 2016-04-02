package com.example.exerciseapp.adapter;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.squareup.picasso.Picasso;

public class LocalSceneListAdapter extends BaseAdapter {
    private Activity activity;
    private LinkedList<JSONObject> list;
    public LocalSceneListAdapter(Activity activity, LinkedList<JSONObject> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        convertView = inflater.inflate(R.layout.local_scene_list_item, null);
        ImageView ivLocalScene = (ImageView) convertView.findViewById(R.id.ivLocalScene);
        TextView tvContentLocalScene = (TextView) convertView.findViewById(R.id.tvContentLocalScene);
        TextView tvTimeLocalScene = (TextView) convertView.findViewById(R.id.tvTimeLocalScene);
        try {
            tvContentLocalScene.setText(list.get(position).getString("title"));
            tvTimeLocalScene.setText(list.get(position).getString("time"));
            Picasso.with(activity).load(list.get(position).getString("pic")).into(ivLocalScene);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

}
