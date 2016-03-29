package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.exerciseapp.R;
import com.example.exerciseapp.utils.ScreenUtils;

/**
 * Created by lyjq on 2016/3/28.
 */
public class TeamDetailActivity extends BackBaseActivity {

    int teamId;
    public static Intent getTeamDetailIntent(Context context,int teamId) {
        Intent intent = new Intent(context, TeamDetailActivity.class);
        return intent.putExtra("teamId",teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myteam);
        setTitleBar("我的团队");
        teamId = getIntent().getIntExtra("teamId",-1);
        if(teamId == -1){
            ScreenUtils.show_msg(this, "创建失败");
            finish();
            return;
        }

//        RestAdapterUtils.getTeamAPI()
    }
}
