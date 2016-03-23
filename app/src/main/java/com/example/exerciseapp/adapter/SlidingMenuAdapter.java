package com.example.exerciseapp.adapter;



import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.SlidingMenuAdapter.SlidingMenuItem;

public class SlidingMenuAdapter extends ArrayAdapter<SlidingMenuItem> {

	public SlidingMenuAdapter(Context context) {
		super(context, 0);
	}
	
	@SuppressLint("InflateParams") 
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_slidingmenu_left, null);
		}
		
		ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
		icon.setImageResource(getItem(position).iconRes);
		TextView title = (TextView) convertView.findViewById(R.id.row_title);
		title.setText(getItem(position).tag);

		return convertView;
	}
	
	public class SlidingMenuItem {
		public String tag;
		public int iconRes;
		public SlidingMenuItem(int iconRes,String tag) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

}
