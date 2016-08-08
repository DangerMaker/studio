package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.MemberListAdapter;
import com.example.exerciseapp.model.AllMember;
import com.example.exerciseapp.model.Member;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/4/3.
 */
public class CheckMembersActivity extends BackBaseActivity implements MemberListAdapter.OnListClick {

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;

    MemberListAdapter adapter;
    String teamId;
    int type;

    public static final int CHECK = 0;
    public static final int MANAGER = 1;

    public static Intent getCheckMembersIntent(Context context, int teamId) {
        Intent intent = new Intent(context, CheckMembersActivity.class);
        intent.putExtra("type", CHECK);
        return intent.putExtra("teamId", teamId);
    }

    public static Intent getManagerMembersIntent(Context context, int teamId) {
        Intent intent = new Intent(context, CheckMembersActivity.class);
        intent.putExtra("type", MANAGER);
        return intent.putExtra("teamId", teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        setTitleBar("成员列表");
        teamId = getIntent().getIntExtra("teamId", -1) + "";
        type = getIntent().getIntExtra("type", -1);
        adapter = new MemberListAdapter(this, this, type, teamId);
        listView.setAdapter(adapter);
        load();
    }

    private void load() {
        RestAdapterUtils.getTeamAPI().showAllMembers(teamId, "show_all_members", new Callback<AllMember>() {
            @Override
            public void success(AllMember allMember, Response response) {
                if (allMember != null && allMember.getResult() == 1) {
                    if (allMember.getData() != null) {
                        adapter.addItems(allMember.getData());
                    }
                }
                if (listView != null) listView.onRefreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(CheckMembersActivity.this, error.getMessage());
                if (listView != null) listView.onRefreshComplete();
            }
        });
    }

    @Override
    public void click(Member group) {

    }
}
