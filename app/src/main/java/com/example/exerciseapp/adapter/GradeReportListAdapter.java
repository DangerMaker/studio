package com.example.exerciseapp.adapter;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.squareup.picasso.Picasso;

public class GradeReportListAdapter extends BaseAdapter {

	private Activity activity;
	private LinkedList<JSONObject> list;
	
	public GradeReportListAdapter(Activity activity,LinkedList<JSONObject> list) {
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
        convertView = inflater.inflate(R.layout.grade_report_list_item, null);
//        WebView tvContentGradeReport = (WebView) convertView.findViewById(R.id.tvContentGradeReport);
        ImageView ivGradeReport1 = (ImageView) convertView.findViewById(R.id.ivGradeReport1);
        ImageView ivGradeReport2 = (ImageView) convertView.findViewById(R.id.ivGradeReport2);
        ImageView ivGradeReport3 = (ImageView) convertView.findViewById(R.id.ivGradeReport3);
        TextView tvTimeGradeReport = (TextView) convertView.findViewById(R.id.tvTimeGradeReport);
        TextView tvTitleGradeReport = (TextView) convertView.findViewById(R.id.tvTitleGradeReport);
        
        try {
        	JSONArray picList = list.get(position).getJSONArray(Config.KEY_PIC_LIST);
			if(picList.length() == 1){
				Picasso.with(activity).load(picList.getString(0)).into(ivGradeReport1);
			}else if(picList.length() == 2){
				Picasso.with(activity).load(picList.getString(0)).into(ivGradeReport1);
				Picasso.with(activity).load(picList.getString(1)).into(ivGradeReport2);
				
			}else if(picList.length() == 3){
				Picasso.with(activity).load(picList.getString(0)).into(ivGradeReport1);
				Picasso.with(activity).load(picList.getString(1)).into(ivGradeReport2);
				Picasso.with(activity).load(picList.getString(2)).into(ivGradeReport3);
			}
//			tvContentGradeReport.loadUrl("http://101.200.214.68/index.php/Api/System/usingClause");
			tvTimeGradeReport.setText(list.get(position).getString(Config.KEY_TIME));
			tvTitleGradeReport.setText(list.get(position).getString(Config.KEY_TITLE));
			convertView.setTag(list.get(position).getString("html5Url"));
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return convertView;
	}

}
