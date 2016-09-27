package com.example.exerciseapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ApplyList;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/29.
 */
public class ApplyListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<ApplyList.DataBean.ApplyListBean> collection;
    Context context;
    OnListClick listClick;

    public ApplyListAdapter(Context context, OnListClick listClick) {
        this.context = context;
        collection = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.listClick = listClick;
    }

    public interface OnListClick {
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

    public void addItems(List<ApplyList.DataBean.ApplyListBean> list) {
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItems(List<ApplyList.DataBean.ApplyListBean> list) {
        collection.clear();
        collection.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        collection.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ApplyList.DataBean.ApplyListBean bean = collection.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_get_apply, null);
            holder = new ViewHolder();
            holder.iconView = (SimpleDraweeView) convertView.findViewById(R.id.icon);
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.descView = (TextView) convertView.findViewById(R.id.reason);
            holder.timeView = (TextView) convertView.findViewById(R.id.time);
            holder.agreeView = (TextView) convertView.findViewById(R.id.status);
            holder.disagreeView = (TextView) convertView.findViewById(R.id.refuse);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.iconView.setImageURI(Uri.parse(bean.getAvatar()));
        holder.nameView.setText(bean.getNickname());
        holder.descView.setText(bean.getMsg());
        holder.timeView.setText(bean.getChange_date());

        holder.agreeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                RestAdapterUtils.getZhuAPI().passApply(MyApplication.uid, "pass_apply", "3.3.0", MyApplication.token,Integer.toString(bean.getId()), "1", new Callback<ErrorMsg>() {
                    @Override
                    public void success(ErrorMsg errorMsg, Response response) {
                        ((TextView)view).setText("通过");
                        view.setEnabled(false);;
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });

        holder.disagreeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                RestAdapterUtils.getZhuAPI().passApply(MyApplication.uid, "pass_apply", "3.3.0", MyApplication.token,Integer.toString(bean.getId()), "0", new Callback<ErrorMsg>() {
                    @Override
                    public void success(ErrorMsg errorMsg, Response response) {
                        ((TextView)view).setText("拒绝成功");
                        view.setEnabled(false);;
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });


        return convertView;
    }

    static class ViewHolder {
        public SimpleDraweeView iconView;
        public TextView nameView;
        public TextView descView;
        public TextView timeView;

        public TextView agreeView;
        public TextView disagreeView;
    }
}
