package com.example.exerciseapp.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.R.id;

public class MessageAdapter extends BaseAdapter {

	private Activity activity;
	private List<JSONObject> list;
	
	public MessageAdapter(Activity activity,List<JSONObject> list) {
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
		View itemView = convertView.inflate(activity, R.layout.message_list_item, null);
//		TextView tvMessageTitle = (TextView) itemView.findViewById(R.id.tvMessageTitle);
		TextView tvMessageTime = (TextView) itemView.findViewById(id.tvMessageTime);
		TextView tvMessageContent = (TextView) itemView.findViewById(id.tvMessageContent);
		try {
//			tvMessageTitle.setText(list.get(position).getString(Config.KEY_TITLE));
			tvMessageTime.setText(list.get(position).getString(Config.KEY_TIME));
			tvMessageContent.setText(list.get(position).getString(Config.KEY_CONTENT));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return itemView;
	}

}
