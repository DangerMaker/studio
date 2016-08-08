package com.example.exerciseapp.aty.team;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GroupListAdapter;
import com.example.exerciseapp.model.AllGroup;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * User: lyjq(1752095474)
 * Date: 2016-04-23
 */
public class SelectTeamActivity extends BackBaseActivity implements GroupListAdapter.OnListClick {

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;

    String uid;
    String eid;
    GroupListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);
        setTitleBar("选择团队");
        eid = getIntent().getStringExtra("eid");
        adapter = new GroupListAdapter(SelectTeamActivity.this, this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                load();
            }

        });


        listView.setAdapter(adapter);
        uid = ((MyApplication) getApplication()).getUid();
        if (uid == null || uid.equals("")) {
            ScreenUtils.show_msg(this, "没有用户id,请登录");
            return;
        }
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

//                    if (allGroup.getData().getInvite_list_return() != null) {
//                        for (int i = 0; i < allGroup.getData().getInvite_list_return().size(); i++) {
//                            SingleGroup singleGroup = allGroup.getData().getInvite_list_return().get(i);
//                            singleGroup.setType("invite_list_return");
//                            allGroup.getData().getInvite_list_return().set(i, singleGroup);
//                        }
//                        adapter.addItems(allGroup.getData().getInvite_list_return());
//                    }
//
//                    if (allGroup.getData().getApply_list_return() != null) {
//                        for (int i = 0; i < allGroup.getData().getApply_list_return().size(); i++) {
//                            SingleGroup singleGroup = allGroup.getData().getApply_list_return().get(i);
//                            singleGroup.setType("apply_list_return");
//                            allGroup.getData().getApply_list_return().set(i, singleGroup);
//                        }
//                        adapter.addItems(allGroup.getData().getApply_list_return());
//                    }

                }
                if (listView != null) listView.onRefreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(SelectTeamActivity.this, error.getMessage());
                if (listView != null) listView.onRefreshComplete();
            }
        });
    }

    @Override
    public void click(SingleGroup group) {
        startActivityForResult(SelectMemberActivity.getManagerMembersIntent(this, group.getId(), eid), 25);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 28) {
            setResult(29, data);
            finish();
        }
    }
}
