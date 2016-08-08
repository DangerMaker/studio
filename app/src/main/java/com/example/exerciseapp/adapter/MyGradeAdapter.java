package com.example.exerciseapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtyGradeReport;
import com.example.exerciseapp.aty.sliding.AtyLocalScene;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class MyGradeAdapter extends BaseAdapter {
    private Activity activity;
    private LinkedList<JSONObject> list;

    public MyGradeAdapter(Activity activity, LinkedList<JSONObject> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.my_grade_list_item, null);
        ImageView frontImageMyGradeList = (ImageView) itemView.findViewById(R.id.frontImageMyGradeList);
        final TextView tvGameTitleMyGradeList = (TextView) itemView.findViewById(R.id.tvGameTitleMyGradeList);
        TextView tvGradeMyGradeList = (TextView) itemView.findViewById(R.id.tvGradeMyGradeList);

        TextView tvGradebouns = (TextView) itemView.findViewById(R.id.tvGradebouns);

        ImageView ivNewsMyGradeList = (ImageView) itemView.findViewById(R.id.ivNewsMyGradeList);
        ImageView ivGradesReportMyGradeList = (ImageView) itemView.findViewById(R.id.ivGradesReportMyGradeList);
        JSONObject jsonObject = list.get(position);
        try {
            Picasso.with(activity).load(jsonObject.getString(Config.KEY_FRONT_PAGE_NEW)).into(frontImageMyGradeList);
            tvGameTitleMyGradeList.setText(jsonObject.getString(Config.KEY_GAME_NAME));
            tvGradeMyGradeList.setText("成绩：" + jsonObject.getString(Config.KEY_ESCORE));
            tvGradebouns.setText("奖励：" + jsonObject.getString("bonus"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ivNewsMyGradeList.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AtyLocalScene.class);
                try {
                    intent.putExtra(Config.KEY_GAME_ID, list.get(position).getString(Config.KEY_GAME_ID));
                    intent.putExtra(Config.KEY_GAME_NAME, list.get(position).getString(Config.KEY_GAME_NAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                activity.startActivity(intent);
            }
        });
        ivGradesReportMyGradeList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AtyGradeReport.class);
                try {
                    intent.putExtra(Config.KEY_GAME_ID, list.get(position).getString(Config.KEY_GAME_ID));
                    intent.putExtra(Config.KEY_GAME_NAME, list.get(position).getString(Config.KEY_GAME_NAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                activity.startActivity(intent);
            }
        });

        return itemView;
    }

}
