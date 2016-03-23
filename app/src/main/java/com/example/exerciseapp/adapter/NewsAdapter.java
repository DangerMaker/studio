package com.example.exerciseapp.adapter;

import java.util.LinkedList;

import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NewsAdapter extends BaseAdapter {

	private Activity activity;
	private LinkedList<JSONObject> list;
	
	
	public NewsAdapter(Activity activity, LinkedList<JSONObject> list) {
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
		return null;
	}

}
