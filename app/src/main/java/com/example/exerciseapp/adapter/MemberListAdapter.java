package com.example.exerciseapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.team.CheckMembersActivity;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.Member;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/29.
 */
public class MemberListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<Member> collection;
    Context context;
    OnListClick listClick;
    int type;
    String teamId;
    String token = MyApplication.getInstance().getToken();
    String version = "0.3";

    public MemberListAdapter(Context context, OnListClick listClick, int type, String teamId) {
        this.context = context;
        collection = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        this.listClick = listClick;
        this.type = type;
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
            convertView = layoutInflater.inflate(R.layout.item_member, null);
            holder = new ViewHolder();
            holder.item_group_img = (SimpleDraweeView) convertView.findViewById(R.id.item_member_img);
            holder.item_group_name = (TextView) convertView.findViewById(R.id.item_member_name);
            holder.item_group_tag = (TextView) convertView.findViewById(R.id.item_member_tag);
            holder.item_goup_del = (ImageView) convertView.findViewById(R.id.item_member_delete);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.item_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type == CheckMembersActivity.CHECK) {
            holder.item_goup_del.setVisibility(View.GONE);
        } else {
            holder.item_goup_del.setVisibility(View.VISIBLE);
        }

        if (position == 0) {
            holder.item_group_tag.setVisibility(View.VISIBLE);
            holder.item_goup_del.setVisibility(View.GONE);
        }
        holder.item_group_img.setImageURI(Uri.parse(member.getAvatar()));
        holder.item_group_name.setText(member.getUsername());
//        holder.item_group_tag.setText("");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClick.click(member);
            }
        });

        holder.item_goup_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确认删除该队员吗?");
                builder.setTitle("提示");

                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(member);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });
        return convertView;
    }

    private void delete(final Member member) {
        RestAdapterUtils.getTeamAPI().exitGroup(Integer.parseInt(teamId), member.getUid(), "exit_group", token, version, new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("退出成功");
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(context, "退出成功");
                    collection.remove(member);
                    notifyDataSetChanged();
                } else {
                    ScreenUtils.show_msg(context, errorMsg.getDesc());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("退出失败");
                ScreenUtils.show_msg(context, "退出失败");
            }
        });
    }

    static class ViewHolder {
        public RelativeLayout relativeLayout;
        public SimpleDraweeView item_group_img;
        public TextView item_group_name;
        public ImageView item_goup_del;
        public TextView item_group_tag;
    }
}
