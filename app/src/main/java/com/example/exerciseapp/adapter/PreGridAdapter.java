package com.example.exerciseapp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cherie_No.47 on 2016/4/3 13:10.
 * Email jascal@163.com
 */
public class PreGridAdapter extends BaseAdapter {
    private List<JSONObject> list;
    private Context context;
    private List<Integer> choiced;

    public PreGridAdapter(Context context, List<JSONObject> list) {
        super();
        this.list = list;
        this.context = context;
        this.choiced = new ArrayList<Integer>();
    }

    public List<Integer> getChoiced() {
        return choiced;
    }

    public String getChoiceString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < getChoiced().size(); i++) {
            builder.append(findNameById(choiced.get(i)));
            if (i != getChoiced().size() - 1) {
                builder.append("|");
            }
        }

        return builder.toString();
    }

    private String findNameById(int id) {
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getInt("id") == id) {
                    return list.get(i).getString("name");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean setChoiced(int id) {
        if (choiced.contains(id)) {
            choiced.remove(choiced.indexOf(id));
            return true;
        } else if (choiced.size() < 3) {
            choiced.add(id);
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_pre_grid_item, null);
            holder = new ViewHolder();
            holder.item_icon = (ImageView) convertView.findViewById(R.id.pre_icon);
            holder.item_title = (TextView) convertView.findViewById(R.id.pre_title);
            holder.item_Layout = (LinearLayout) convertView.findViewById(R.id.pre_icon_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.item_title.setText(list.get(position).getString("name"));
            Picasso.with(context).load(list.get(position).getString("pic")).into(holder.item_icon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (choiced.size() != 0 && choiced.contains(list.get(position).getInt("id"))) {
                holder.item_Layout.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_oval));
            } else {
                holder.item_Layout.setBackground(ContextCompat.getDrawable(context, R.color.common_white));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView item_icon;
        public TextView item_title;
        public LinearLayout item_Layout;
    }
}
