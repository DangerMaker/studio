package com.example.exerciseapp.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtyGameInformation;
import com.example.exerciseapp.aty.sliding.AtyGradeReport;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class MyGradeAdapter extends BaseAdapter {

	private Activity activity;
	private LinkedList<JSONObject> list;
	private RequestQueue mRequestQueue;
	
	public MyGradeAdapter(Activity activity,LinkedList<JSONObject> list) {
		this.activity = activity;
		this.list = list;
		mRequestQueue =  Volley.newRequestQueue(activity);
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
//        TextView tvRewardMyGradeList = (TextView) itemView.findViewById(R.id.tvRewardMyGradeList);
        ImageView ivNewsMyGradeList = (ImageView) itemView.findViewById(R.id.ivNewsMyGradeList);
        ImageView ivGradesReportMyGradeList = (ImageView) itemView.findViewById(R.id.ivGradesReportMyGradeList);
        
        try {
			Picasso.with(activity).load(list.get(position).getString(Config.KEY_FRONT_PAGE)).into(frontImageMyGradeList);
			tvGameTitleMyGradeList.setText(list.get(position).getString(Config.KEY_GAME_NAME));
			tvGradeMyGradeList.setText("成绩："+list.get(position).getString(Config.KEY_ESCORE));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ivNewsMyGradeList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Config.SHOW_NEWS_FRAGMENT = true;
				Intent intent = new Intent(activity,AtySlidingHome.class);
				activity.startActivity(intent);
			}
		});
        ivGradesReportMyGradeList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringRequest stringRequestGradeReport = new StringRequest(
	                    Request.Method.POST,
	                    Config.SERVER_URL+"Game/scoreShow",
	                    new Response.Listener<String>() {
	 
	                        @Override
	                        public void onResponse(String s) {
	                            try {
	                                JSONObject jsonObject = new JSONObject(s);
	                                if(jsonObject.getInt("result") == 1){
	                                	Intent intent = new Intent(activity,AtyGradeReport.class);
										intent.putExtra(Config.KEY_GAME_NAME, tvGameTitleMyGradeList.getText().toString());
										Date date = new Date();
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										intent.putExtra(Config.KEY_GAME_START_TIME, sdf.format(date));
										intent.putExtra("information", jsonObject.getString("data"));
										activity.startActivity(intent);
	                                }else{
	                                	
	                                }
	                                
	                            } catch (JSONException e) {
	                                e.printStackTrace();
	                            }
	                        }
	                    },
	                    new Response.ErrorListener() {
	 
	                        @Override
	                        public void onErrorResponse(VolleyError volleyError) {
	                        	Toast.makeText(activity.getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
	                        }
	                    }){
	 
	                @Override
	                protected Map<String, String> getParams() throws AuthFailureError {
	                    Map<String,String> map = new HashMap<String,String>();
	                    try {
							map.put(Config.KEY_GAME_ID,list.get(position).getString(Config.KEY_GAME_ID));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                    return map;
	                }
	            };
	            mRequestQueue.add(stringRequestGradeReport);
			}
		});
        
		return itemView;
	}

}
