package com.example.exerciseapp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.model.TagModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cherie_No.47 on 2016/4/3 13:10.
 * Email jascal@163.com
 */
public class TagGridAdapter extends BaseAdapter {
    private List<TagModel> list;
    private Context context;

    public TagGridAdapter(Context context,List<TagModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TagModel model = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.team_tag_grid_item, null);
            holder = new ViewHolder();
            holder.item_icon = (SimpleDraweeView) convertView.findViewById(R.id.pre_icon);
            holder.item_title = (TextView) convertView.findViewById(R.id.pre_title);
            holder.item_Layout = (LinearLayout) convertView.findViewById(R.id.pre_icon_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_title.setText(model.getName());
        holder.item_icon.setImageURI(Uri.parse(model.getPic()));

        return convertView;
    }

    static class ViewHolder {
        public SimpleDraweeView item_icon;
        public TextView item_title;
        public LinearLayout item_Layout;
    }
}
