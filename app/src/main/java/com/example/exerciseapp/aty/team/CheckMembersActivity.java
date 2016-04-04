package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.exerciseapp.R;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.Bind;

/**
 * Created by lyjq on 2016/4/3.
 */
public class CheckMembersActivity extends BackBaseActivity{

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;

    public static Intent getCheckMembersIntent(Context context, int teamId) {
        Intent intent = new Intent(context, CheckMembersActivity.class);
        return intent.putExtra("teamId", teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
    }
}
