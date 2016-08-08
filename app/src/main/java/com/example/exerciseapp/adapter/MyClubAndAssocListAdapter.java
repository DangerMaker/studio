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
import com.example.exerciseapp.aty.activityrun.ActivityPay;
import com.example.exerciseapp.aty.sliding.AtyModifyClubEntryForm;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MyClubAndAssocListAdapter extends BaseAdapter {

    private Activity activity;
    private LinkedList<JSONObject> list;
    private RequestQueue mRequestQueue;

    TextView tvClubOrAssocNameMyClubAndAssocList;

    public MyClubAndAssocListAdapter(Activity activity, LinkedList<JSONObject> list) {
        this.activity = activity;
        this.list = list;
        mRequestQueue = Volley.newRequestQueue(activity);
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
        LayoutInflater inflater = activity.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.my_club_and_assoc_list_item, null);
        ImageView frontImageMyClubAndAssocList = (ImageView) itemView.findViewById(R.id.frontImageMyClubAndAssocList);
        ImageView ivStatusMyClubAndAssocList = (ImageView) itemView.findViewById(R.id.ivStatusMyClubAndAssocList);

        tvClubOrAssocNameMyClubAndAssocList = (TextView) itemView.findViewById(R.id.tvClubOrAssocNameMyClubAndAssocList);
        TextView tvParticipantPersonMyClubAndAssocList = (TextView) itemView.findViewById(R.id.tvParticipantPersonMyClubAndAssocList);
        TextView tvTimeMyClubAndAssocList = (TextView) itemView.findViewById(R.id.tvTimeMyClubAndAssocList);
        ImageView ivModifyInformationMyClubAndAssocList = (ImageView) itemView.findViewById(R.id.ivModifyInformationMyClubAndAssocList);
        ImageView ivPayMyClubAndAssocList = (ImageView) itemView.findViewById(R.id.ivPayMyClubAndAssocList);

        try {
            Picasso.with(activity).load(list.get(position).getString(Config.KEY_FRONT_PAGE)).into(frontImageMyClubAndAssocList);
            if (list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_FAILED)) {
                ivModifyInformationMyClubAndAssocList.setImageResource(R.drawable.modify_information_my_entry_form);
                ivModifyInformationMyClubAndAssocList.setClickable(true);
                ivStatusMyClubAndAssocList.setImageResource(R.drawable.nopass_my_entry_form);
                tvParticipantPersonMyClubAndAssocList.setText("申请人：" + list.get(position).getString(Config.KEY_USER_NAME));
            } else if (list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_IN_CHECK)) {
                tvParticipantPersonMyClubAndAssocList.setText("申请人：" + list.get(position).getString(Config.KEY_USER_NAME));
                ivStatusMyClubAndAssocList.setImageResource(R.drawable.incheck_my_entry_form);
                ivModifyInformationMyClubAndAssocList.setImageResource(R.drawable.modify_information_my_entry_form);
                ivModifyInformationMyClubAndAssocList.setClickable(true);
            } else if (list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_NO_PAY)) {
                tvParticipantPersonMyClubAndAssocList.setText("申请人：" + list.get(position).getString(Config.KEY_USER_NAME));
                ivModifyInformationMyClubAndAssocList.setImageResource(R.drawable.modify_information_my_entry_form);
                ivModifyInformationMyClubAndAssocList.setClickable(true);
                ivPayMyClubAndAssocList.setImageResource(R.drawable.pay_my_entry_form);
                ivPayMyClubAndAssocList.setClickable(true);
                ivStatusMyClubAndAssocList.setImageResource(R.drawable.nopay_my_entry_form);
            } else if (list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_SUCCESS)) {
                tvParticipantPersonMyClubAndAssocList.setText("会员：" + list.get(position).getString(Config.KEY_USER_NAME));
                ivModifyInformationMyClubAndAssocList.setImageResource(R.drawable.modify_information_my_entry_form);
                ivModifyInformationMyClubAndAssocList.setClickable(true);
                ivStatusMyClubAndAssocList.setImageResource(R.drawable.success_my_entry_form);
            }

            tvClubOrAssocNameMyClubAndAssocList.setText(list.get(position).getString(Config.KEY_ANAME));
            tvTimeMyClubAndAssocList.setText("时间：" + list.get(position).getString(Config.KEY_ATTEND_TIME));


            if (ivModifyInformationMyClubAndAssocList.isClickable()) {
                ivModifyInformationMyClubAndAssocList.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                Config.SERVER_URL + "Assoc/changeAssocInfo",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            Intent intent = new Intent(activity, AtyModifyClubEntryForm.class);
                                            intent.putExtra(Config.KEY_ANAME, tvClubOrAssocNameMyClubAndAssocList.getText().toString());
                                            intent.putExtra(Config.KEY_ID, list.get(position).getString(Config.KEY_ID));
                                            intent.putExtra("agreement", list.get(position).getString("agreement"));
                                            JSONObject jsonObj = new JSONObject(s);
                                            if (jsonObj.getInt("result") == 1) {
                                                intent.putExtra("information", s);
                                                activity.startActivity(intent);
                                            }
                                            if (list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_IN_CHECK)
                                                    || list.get(position).getString(Config.KEY_CHECK_STATUS).equals(Config.CHECK_STATUS_SUCCESS)) {
                                                Config.CAN_MODIFY_ALL_INFORMATION = 0;
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO Auto-generated method stub

                                    }
                                }) {

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                try {
                                    map.put(Config.KEY_ID, list.get(position).getString(Config.KEY_ID));
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                return map;
                            }
                        };
                        mRequestQueue.add(stringRequest);
                    }
                });
            }

            if (ivPayMyClubAndAssocList.isClickable()) {
                ivPayMyClubAndAssocList.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(activity, ActivityPay.class);
                            intent.putExtra(Config.KEY_ID, list.get(position).getString(Config.KEY_ID));
                            intent.putExtra(Config.KEY_GAME_NAME, tvClubOrAssocNameMyClubAndAssocList.getText().toString());
                            intent.putExtra(Config.KEY_USER_ATTEND_ENAME, "申请入会");
                            intent.putExtra("type", "assoc");
                            intent.putExtra("apayfee", list.get(position).getString("apayfee"));
                            activity.startActivity(intent);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return itemView;
    }

}
