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

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.squareup.picasso.Picasso;

public class LocalSceneListAdapter extends BaseAdapter {

	private Activity activity;
	private LinkedList<JSONObject> list;
	
	public LocalSceneListAdapter(Activity activity,LinkedList<JSONObject> list) {
		this.activity = activity;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public JSONObject getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = activity.getLayoutInflater();  
        convertView = inflater.inflate(R.layout.local_scene_list_item, null);
        ImageView ivLocalScene1 = (ImageView) convertView.findViewById(R.id.ivLocalScene1);
        ImageView ivLocalScene2 = (ImageView) convertView.findViewById(R.id.ivLocalScene2);
        ImageView ivLocalScene3 = (ImageView) convertView.findViewById(R.id.ivLocalScene3);
        TextView tvContentLocalScene = (TextView) convertView.findViewById(R.id.tvContentLocalScene);
        TextView tvTimeLocalScene = (TextView) convertView.findViewById(R.id.tvTimeLocalScene);
        
        try {
        	JSONArray picList = list.get(position).getJSONArray(Config.KEY_PIC_LIST);
			if(list.get(position).getInt(Config.KEY_PIC_NUM) == 1){
				Picasso.with(activity).load(picList.getString(0)).into(ivLocalScene1);
			}else if(list.get(position).getInt(Config.KEY_PIC_NUM) == 2){
				Picasso.with(activity).load(picList.getString(0)).into(ivLocalScene1);
				Picasso.with(activity).load(picList.getString(1)).into(ivLocalScene2);
				
			}else if(list.get(position).getInt(Config.KEY_PIC_NUM) == 3){
				Picasso.with(activity).load(picList.getString(0)).into(ivLocalScene1);
				Picasso.with(activity).load(picList.getString(1)).into(ivLocalScene2);
				Picasso.with(activity).load(picList.getString(2)).into(ivLocalScene3);
			}
			tvContentLocalScene.setText(list.get(position).getString(Config.KEY_TITLE));
			tvTimeLocalScene.setText(list.get(position).getString(Config.KEY_TIME));
			
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return convertView;
	}

}
