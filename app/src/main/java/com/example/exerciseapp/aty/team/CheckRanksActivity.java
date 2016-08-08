package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.MemberRankListAdapter;
import com.example.exerciseapp.model.AllRank;
import com.example.exerciseapp.model.Rank;
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
public class CheckRanksActivity extends BackBaseActivity implements MemberRankListAdapter.OnListClick {

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;

    MemberRankListAdapter adapter;
    String teamId;

    public static Intent getCheckRanksIntent(Context context, int teamId) {
        Intent intent = new Intent(context, CheckRanksActivity.class);
        return intent.putExtra("teamId", teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);
        setTitleBar("排行列表");
        teamId = getIntent().getIntExtra("teamId", -1) + "";
        adapter = new MemberRankListAdapter(this, this);
        listView.setAdapter(adapter);
        load();
    }

    private void load() {
        RestAdapterUtils.getTeamAPI().showAllRank(teamId, "show_all_rank", new Callback<AllRank>() {
            @Override
            public void success(AllRank allRank, Response response) {
                if (allRank != null && allRank.getResult() == 1) {
                    if (allRank.getData() != null) {
                        adapter.addItems(allRank.getData());
                    }
                }
                if (listView != null) listView.onRefreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(CheckRanksActivity.this, error.getMessage());
                if (listView != null) listView.onRefreshComplete();
            }
        });
    }

    @Override
    public void click(Rank rank) {

    }
}
