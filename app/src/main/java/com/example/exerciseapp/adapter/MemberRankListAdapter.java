package com.example.exerciseapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.Member;
import com.example.exerciseapp.model.Rank;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyjq on 2016/3/29.
 */
public class MemberRankListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<Rank> collection;
    Context context;
    OnListClick listClick;
    public MemberRankListAdapter(Context context, OnListClick listClick) {
        this.context = context;
        collection = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.listClick = listClick;
    }

    public interface OnListClick{
        void click(Rank rank);
    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public Object getItem(int position) {
        return collection.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItems(List<Rank> list){
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItems(List<Rank> list){
        collection.clear();
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        collection.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Rank rank = collection.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_rank,null);
            holder = new ViewHolder();
            holder.rank_img = (SimpleDraweeView)convertView.findViewById(R.id.item_some_img);
            holder.rank_position = (TextView)convertView.findViewById(R.id.item_rank_position);
            holder.rank_username = (TextView)convertView.findViewById(R.id.item_some_name);
            holder.rank_score = (TextView)convertView.findViewById(R.id.item_some_score);
            holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.item_root);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.rank_img.setImageURI(Uri.parse(rank.getAvatar()));
        holder.rank_username.setText(rank.getUsername());
        holder.rank_position.setText(rank.getRank_string());
        holder.rank_score.setText(rank.getPoint() + "积分");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClick.click(rank);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public RelativeLayout relativeLayout;
        public SimpleDraweeView rank_img;
        public TextView rank_position;
        public TextView rank_username;
        public TextView rank_score;
    }
}
