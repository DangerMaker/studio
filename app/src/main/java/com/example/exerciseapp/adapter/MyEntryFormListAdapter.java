package com.example.exerciseapp.adapter;

/**
 * 我的报名Adapter
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.sax.StartElementListener;
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
import com.example.exerciseapp.aty.sliding.AtyModifyPersonalInformationMyEntryForm;
import com.example.exerciseapp.aty.sliding.AtyMyEntryForm;
import com.example.exerciseapp.aty.sliding.AtyPay;
import com.example.exerciseapp.net.BaseNetConnection;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class MyEntryFormListAdapter extends BaseAdapter {

	private Activity activity;
	private LinkedList<JSONObject> list;
	TextView tvGameTitleMyEntryFormList;
	
	private RequestQueue mRequestQueue;
	
	public MyEntryFormListAdapter(Activity activity,LinkedList<JSONObject> list) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.list = list;
		mRequestQueue =  Volley.newRequestQueue(activity);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = activity.getLayoutInflater();  
        View itemView = inflater.inflate(R.layout.my_entry_form_list_item, null);
        ImageView frontImageMyEntryFormList = (ImageView) itemView.findViewById(R.id.frontImageMyEntryFormList);
        ImageView ivStatusMyEntryFormList = (ImageView) itemView.findViewById(R.id.ivStatusMyEntryFormList);
        tvGameTitleMyEntryFormList = (TextView) itemView.findViewById(R.id.tvGameTitleMyEntryFormList);
        TextView tvParticipantPersonMyEntryFormList = (TextView) itemView.findViewById(R.id.tvParticipantPersonMyEntryFormList);
        TextView tvTimeMyEntryFormList = (TextView) itemView.findViewById(R.id.tvTimeMyEntryFormList);
        TextView tvLocationMyEntryFormList = (TextView) itemView.findViewById(R.id.tvLocationMyEntryFormList);
        ImageView tvModifyInformationMyEntryFormList = (ImageView) itemView.findViewById(R.id.tvModifyInformationMyEntryFormList);
        ImageView tvPayMyEntryFormList = (ImageView) itemView.findViewById(R.id.tvPayMyEntryFormList);
//        ImageView tvFinishEntryFormMyEntryFormList = (ImageView) itemView.findViewById(R.id.tvFinishEntryFormMyEntryFormList);
        
        try {
			Picasso.with(activity).load(list.get(position).getString(Config.KEY_FRONT_PAGE)).into(frontImageMyEntryFormList);
			//赛事状态
			if(list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_FAILED)){
				tvModifyInformationMyEntryFormList.setClickable(false);
				ivStatusMyEntryFormList.setImageResource(R.drawable.nopass_my_entry_form);
			}else if(list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_IN_CHECK)){
				tvModifyInformationMyEntryFormList.setImageResource(R.drawable.modify_information_my_entry_form);
				tvModifyInformationMyEntryFormList.setClickable(true);
				ivStatusMyEntryFormList.setImageResource(R.drawable.incheck_my_entry_form);
			}else if(list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_NO_PAY)){
				tvModifyInformationMyEntryFormList.setImageResource(R.drawable.modify_information_my_entry_form);
				tvModifyInformationMyEntryFormList.setClickable(true);
				tvPayMyEntryFormList.setImageResource(R.drawable.pay_my_entry_form);
				tvPayMyEntryFormList.setClickable(true);
				ivStatusMyEntryFormList.setImageResource(R.drawable.nopay_my_entry_form);
			}else if(list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_SUCCESS)){
				tvModifyInformationMyEntryFormList.setImageResource(R.drawable.modify_information_my_entry_form);
				tvModifyInformationMyEntryFormList.setClickable(true);
				ivStatusMyEntryFormList.setImageResource(R.drawable.success_my_entry_form);
			}
			
			//修改资料，根据支付状态决定是否可以修改所有资料
			if(tvModifyInformationMyEntryFormList.isClickable()){
				tvModifyInformationMyEntryFormList.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
							StringRequest  stringRequest = new StringRequest(
						              Request.Method.POST,
						              Config.SERVER_URL+"Game/changeGameInfo",
						              new Response.Listener<String>() {
						
						                  @Override
						                  public void onResponse(String s) {
						                	  try {
													Intent intent = new Intent(activity,AtyModifyPersonalInformationMyEntryForm.class);
													intent.putExtra(Config.KEY_GAME_NAME, tvGameTitleMyEntryFormList.getText().toString());
													intent.putExtra("agreement",list.get(position).getString("agreement"));
													JSONObject jsonObj = new JSONObject(s);
													if(jsonObj.getInt("result") == 1){
														intent.putExtra("information", s);
														activity.startActivity(intent);
													}
													if(list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_IN_CHECK)
															||list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_SUCCESS)){
														Config.CAN_MODIFY_ALL_INFORMATION = 0;
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
						              }},
						              new Response.ErrorListener() {
										@Override
										public void onErrorResponse(VolleyError error) {
											// TODO Auto-generated method stub
											
										}
						              }){
						
						          @Override
						          protected Map<String, String> getParams() throws AuthFailureError {
						              Map<String,String> map = new HashMap<String,String>();
						              try {
										map.put(Config.KEY_UEID, list.get(position).getString("id"));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						              return map;
						          }
						      };
						      mRequestQueue.add(stringRequest);
							
//						try {
//							new BaseNetConnection( Config.SERVER_URL+"Game/changeGameInfo",Config.POST,new BaseNetConnection.SuccessCallback() {
//								
//								@Override
//								public void onSuccess(String result) {
//									try {
//										Intent intent = new Intent(activity,AtyModifyPersonalInformationMyEntryForm.class);
//										intent.putExtra(Config.KEY_GAME_NAME, tvGameTitleMyEntryFormList.getText().toString());
//										intent.putExtra(Config.KEY_UEID, list.get(position).getString(Config.KEY_UEID));
//										JSONObject jsonObj = new JSONObject(result);
//										if(jsonObj.getInt("result") == 1){
//											intent.putExtra("information", result);
//											activity.startActivity(intent);
//										}
//										if(list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_IN_CHECK)
//												||list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_SUCCESS)){
//											Config.CAN_MODIFY_ALL_INFORMATION = 0;
//										}
//									} catch (JSONException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//								}
//							},new BaseNetConnection.FailCallback() {
//								
//								@Override
//								public void onFail() {
//									// TODO Auto-generated method stub
//									
//								}
//							},Config.KEY_UEID,list.get(position).getString(Config.KEY_UEID));
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
					}
				});
			}
			
			if(tvPayMyEntryFormList.isClickable()){
				tvPayMyEntryFormList.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							Intent intent = new Intent(activity,AtyPay.class);
							intent.putExtra(Config.KEY_ID, list.get(position).getString("id"));
							intent.putExtra(Config.KEY_GAME_NAME, tvGameTitleMyEntryFormList.getText().toString());
							intent.putExtra(Config.KEY_USER_ATTEND_ENAME, list.get(position).getString("ename"));
							intent.putExtra("type", "game");
							intent.putExtra("apayfee",list.get(position).getString("epayfee"));
							activity.startActivity(intent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			
			tvGameTitleMyEntryFormList.setText(list.get(position).getString(Config.KEY_GAME_NAME));
			tvParticipantPersonMyEntryFormList.setText("参赛人员："+ list.get(position).getString(Config.KEY_ATTEND_PERSON));
			tvTimeMyEntryFormList.setText("时间："+ list.get(position).getString(Config.KEY_ATTEND_TIME));
			tvLocationMyEntryFormList.setText("地点："+ list.get(position).getString(Config.KEY_GAME_POSITION));
			itemView.setTag(list.get(position).get(Config.KEY_UEID));
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return itemView;
	}

}
