package com.example.exerciseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.GameModel;
import com.example.exerciseapp.model.OrganizeName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyjq on 2016/3/29.
 */
public class GameSubListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<GameModel> collection;
    Context context;
    OnListClick listClick;

    public GameSubListAdapter(Context context, OnListClick listClick) {
        this.context = context;
        collection = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.listClick = listClick;
    }

    public interface OnListClick {
        void click(GameModel group);
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

    public void addItems(List<GameModel> list) {
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItems(List<GameModel> list) {
        collection.clear();
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        collection.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GameModel singleGroup = collection.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_oragnize, null);
            holder = new ViewHolder();
            holder.item_group_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.item_root);
            holder.item_select =  (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item_group_name.setText(singleGroup.getName());
        if (singleGroup.isFlag()) {
            holder.item_select.setVisibility(View.VISIBLE);
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.edit_color_grey));
        } else {
            holder.item_select.setVisibility(View.GONE);
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.common_white));
        }
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listClick.click(singleGroup);
//            }
//        });
        return convertView;
    }

    static class ViewHolder {
        public RelativeLayout relativeLayout;
        public TextView item_group_name;
        public ImageView item_select;
    }
}
