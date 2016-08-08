package com.example.exerciseapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class GradeReportListAdapter extends BaseAdapter {

    private Activity activity;
    private LinkedList<JSONObject> list;

    public GradeReportListAdapter(Activity activity, LinkedList<JSONObject> list) {
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
        convertView = inflater.inflate(R.layout.grade_report_list_item, null);
//        WebView tvContentGradeReport = (WebView) convertView.findViewById(R.id.tvContentGradeReport);
        ImageView ivGradeReport1 = (ImageView) convertView.findViewById(R.id.ivGradeReport1);
        TextView tvTimeGradeReport = (TextView) convertView.findViewById(R.id.tvTimeGradeReport);
        TextView tvTitleGradeReport = (TextView) convertView.findViewById(R.id.tvTitleGradeReport);

        try {
            JSONObject info = list.get(position);
            Picasso.with(activity).load(info.getString("pic")).into(ivGradeReport1);
            tvTimeGradeReport.setText(info.getString("time"));
            tvTitleGradeReport.setText(info.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

}
