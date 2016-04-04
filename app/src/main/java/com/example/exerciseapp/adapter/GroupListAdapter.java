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
import com.example.exerciseapp.model.SingleGroup;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyjq on 2016/3/29.
 */
public class GroupListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<SingleGroup> collection;
    Context context;
    OnListClick listClick;
    public GroupListAdapter(Context context,OnListClick listClick) {
        this.context = context;
        collection = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.listClick = listClick;
    }

    public interface OnListClick{
        void click(SingleGroup group);
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

    public void addItems(List<SingleGroup> list){
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItems(List<SingleGroup> list){
        collection.clear();
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void clear(){
        collection.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SingleGroup singleGroup = collection.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_group,null);
            holder = new ViewHolder();
            holder.item_group_img = (SimpleDraweeView) convertView.findViewById(R.id.item_group_img);
            holder.item_group_name = (TextView)convertView.findViewById(R.id.item_group_name);
            holder.item_group_tag = (TextView)convertView.findViewById(R.id.item_group_tag);
            holder.item_group_num = (TextView)convertView.findViewById(R.id.item_group_num);
            holder.item_group_des = (TextView)convertView.findViewById(R.id.item_group_des);
            holder.item_group_invite = (TextView)convertView.findViewById(R.id.item_group_invite);
            holder.item_group_sum = (TextView)convertView.findViewById(R.id.item_group_sum);
            holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.item_root);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        if(singleGroup.getType().equals("invite_list_return")){
            holder.item_group_invite.setVisibility(View.VISIBLE);
        }else{
            holder.item_group_invite.setVisibility(View.GONE);
        }

        if(singleGroup.getType().equals("apply_list_return")){
            holder.item_group_name.setText(singleGroup.getUsername());
            holder.item_group_invite.setText("申请加入");
            holder.item_group_invite.setVisibility(View.VISIBLE);
            holder.item_group_des.setVisibility(View.INVISIBLE);
            holder.item_group_num.setVisibility(View.INVISIBLE);
        }else{
            holder.item_group_name.setText(singleGroup.getGroup_name());
        }

        holder.item_group_img.setImageURI(Uri.parse(singleGroup.getAvatar()));
        holder.item_group_tag.setText(singleGroup.getTag_name());
        holder.item_group_des.setText(singleGroup.getIntro());
        holder.item_group_num.setText("人数：" + singleGroup.getMembernum() +"");
        holder.item_group_sum.setText(singleGroup.getSumrun() + "公里");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClick.click(singleGroup);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public RelativeLayout relativeLayout;
        public SimpleDraweeView item_group_img;
        public TextView item_group_name;
        public TextView item_group_tag;
        public TextView item_group_num;
        public TextView item_group_des;
        public TextView item_group_invite;
        public TextView item_group_sum;
    }
}
