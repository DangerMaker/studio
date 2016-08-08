package com.example.exerciseapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.Member;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyjq on 2016/3/29.
 */
public class SelectMemberAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<Member> collection;
    Context context;
    OnListClick listClick;
    String teamId;

    public SelectMemberAdapter(Context context, OnListClick listClick, String teamId) {
        this.context = context;
        collection = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.listClick = listClick;
        this.teamId = teamId;
    }

    public interface OnListClick {
        void click(Member group);
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

    public void addItems(List<Member> list) {
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItems(List<Member> list) {
        collection.clear();
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        collection.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Member member = collection.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_member_select, null);
            holder = new ViewHolder();
            holder.item_group_img = (SimpleDraweeView) convertView.findViewById(R.id.item_member_img);
            holder.item_group_name = (TextView) convertView.findViewById(R.id.item_member_name);
            holder.item_group_tag = (TextView) convertView.findViewById(R.id.item_member_tag);
            holder.item_member_select = (ImageView) convertView.findViewById(R.id.item_member_select);
//            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.item_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.item_group_tag.setVisibility(View.VISIBLE);
        }

        if (!member.isFlag()) {
            holder.item_member_select.setImageResource(R.drawable.btn_agree_nor);
        } else {
            holder.item_member_select.setImageResource(R.drawable.btn_agree_sel);
        }

        holder.item_group_img.setImageURI(Uri.parse(member.getAvatar()));
        holder.item_group_name.setText(member.getUsername());
//        holder.item_group_tag.setText("");
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listClick.click(member);
//            }
//        });

        return convertView;
    }


    static class ViewHolder {
        public RelativeLayout relativeLayout;
        public SimpleDraweeView item_group_img;
        public TextView item_group_name;
        public ImageView item_member_select;
        public TextView item_group_tag;
    }
}
