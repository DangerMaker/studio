package com.example.exerciseapp.adapter;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class ClubAndAssocListAdapter extends BaseAdapter {
    private Activity context;
    private LinkedList<JSONObject> list;

    public ClubAndAssocListAdapter(Activity context, LinkedList<JSONObject> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
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
        LayoutInflater inflater = context.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.club_list_item, null);
        TextView tvClubTitleClubList = (TextView) itemView.findViewById(R.id.tvClubTitleClubList);
        ImageView imageClubList = (ImageView) itemView
                .findViewById(R.id.imageClubList);
        TextView tvPersonNum = (TextView) itemView.findViewById(R.id.tvClubPersonNum);
        TextView tvClubBriefIntroClubList = (TextView) itemView.findViewById(R.id.tvClubBriefIntroClubList);

        try {
            tvClubTitleClubList.setText(list.get(position).getString(Config.KEY_ANAME));
            Picasso.with(context).load(list.get(position).getString(Config.KEY_FRONT_PAGE)).into(imageClubList);
            tvPersonNum.setText(list.get(position).getString(Config.KEY_MEMBER_NUM));
            tvClubBriefIntroClubList.setText(list.get(position).getString(Config.KEY_OPENTYPE));
            itemView.setTag(list.get(position).toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        tvClubTitleClubList.setText(list.get(position).get(Config.KEY_CLUB_NAME));
//        //TODO
//        Picasso.with(context).load("http://101.200.214.68/Uploads/Picture/gamePic/2015-11-04/5639c7f545377.jpg").into(imageClubList); 
//        tvPersonNum.setText(list.get(position).get(Config.KEY_CLUB_MAX_ATTEND));
//        tvClubBriefIntroClubList.setText(list.get(position).get(Config.KEY_CLUB_BIREF_INTRO));
//        itemView.setTag(list.get(position).get(Config.KEY_CLUB_ID));
//        Config.currentClickedGameItem = list.get(position).get(Config.KEY_GAME_ID);
        return itemView;
    }

}
