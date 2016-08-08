package com.example.exerciseapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GroupListAdapter;
import com.example.exerciseapp.aty.team.TeamDetailActivity;
import com.example.exerciseapp.model.AllGroup;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/24.
 */
public class TeamFragment extends BaseFragment implements GroupListAdapter.OnListClick {

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;
    String uid;
    GroupListAdapter adapter;

    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new GroupListAdapter(getActivity(), this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                load();
            }

        });


        listView.setAdapter(adapter);
        uid = ((MyApplication) getActivity().getApplication()).getUid();
        if (uid == null || uid.equals("")) {
            ScreenUtils.show_msg(getActivity(), "没有用户id,请登录");
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void load() {

        adapter.clear();
        RestAdapterUtils.getTeamAPI().getAllGroup(uid, "get_all_group", new Callback<AllGroup>() {
            @Override
            public void success(AllGroup allGroup, Response response) {
                if (allGroup != null && allGroup.getResult() == 1) {
                    if (allGroup.getData().getGroup_info_return() != null) {
                        for (int i = 0; i < allGroup.getData().getGroup_info_return().size(); i++) {
                            SingleGroup singleGroup = allGroup.getData().getGroup_info_return().get(i);
                            singleGroup.setType("group_info_return");
                            allGroup.getData().getGroup_info_return().set(i, singleGroup);
                        }
                        adapter.addItems(allGroup.getData().getGroup_info_return());
                    }

                    if (allGroup.getData().getLeader_group_info_return() != null) {
                        for (int i = 0; i < allGroup.getData().getLeader_group_info_return().size(); i++) {
                            SingleGroup singleGroup = allGroup.getData().getLeader_group_info_return().get(i);
                            singleGroup.setType("leader_group_info_return");
                            allGroup.getData().getLeader_group_info_return().set(i, singleGroup);
                        }
                        adapter.addItems(allGroup.getData().getLeader_group_info_return());
                    }

                    if (allGroup.getData().getInvite_list_return() != null) {
                        List<SingleGroup> mySingleGroup = new ArrayList<SingleGroup>();
                        for (int i = 0; i < allGroup.getData().getInvite_list_return().size(); i++) {
                            SingleGroup singleGroup = allGroup.getData().getInvite_list_return().get(i);
                            if (singleGroup.getAvatar() != null) {
                                singleGroup.setType("invite_list_return");
                                allGroup.getData().getInvite_list_return().set(i, singleGroup);
                                mySingleGroup.add(singleGroup);
                            }
                        }
                        if (mySingleGroup != null)
                            adapter.addItems(mySingleGroup);
                    }

                    if (allGroup.getData().getApply_list_return() != null) {
                        for (int i = 0; i < allGroup.getData().getApply_list_return().size(); i++) {
                            SingleGroup singleGroup = allGroup.getData().getApply_list_return().get(i);
                            singleGroup.setType("apply_list_return");
                            allGroup.getData().getApply_list_return().set(i, singleGroup);
                        }
                        adapter.addItems(allGroup.getData().getApply_list_return());
                    }

                }
                if (listView != null) listView.onRefreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(getActivity(), error.getMessage());
                if (listView != null) listView.onRefreshComplete();
            }
        });
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_myteam;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void click(SingleGroup group) {
        if (group.getType().equals("apply_list_return")) {
            dialog(group);
        } else {
            startActivity(TeamDetailActivity.getTeamDetailIntent(getActivity(), group.getId(), group.getType(), group.getInvite_id()));
        }
    }

    private void dialog(final SingleGroup singleGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否接收申请？"); //设置内容
        builder.setIcon(R.drawable.run_icon);//设置图标，图片id即可
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                agree(singleGroup);
            }
        });
        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                disagree(singleGroup);
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    private void agree(SingleGroup group) {
        RestAdapterUtils.getTeamAPI().passApply(group.getId() + "", "pass_apply", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(getActivity(), "加入成功");
                    load();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(getActivity(), "加入失败");
            }
        });
    }

    private void disagree(SingleGroup group) {
        RestAdapterUtils.getTeamAPI().refuseApply(group.getId() + "", "refuse_apply", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(getActivity(), "拒绝成功");
                    load();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(getActivity(), "拒绝失败");
            }
        });
    }
}
