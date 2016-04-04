package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyjq on 2016/3/28.
 */
public class CreateTeamSucActivity extends BackBaseActivity{

    @Bind(R.id.success_add_member)
    TextView addMember;
    int teamId;
    public static Intent getCreateSuccessIntent(Context context,int teamId) {
        Intent intent = new Intent(context, CreateTeamSucActivity.class);
        return intent.putExtra("teamId",teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_success);
        setTitleBar("创建团队成功");
        teamId = getIntent().getIntExtra("teamId",-1);
        if(teamId == -1){
            ScreenUtils.show_msg(this,"创建失败");
            finish();
            return;
        }
    }

    @OnClick(R.id.success_team_submit)
    public void enter(){
        startActivity(TeamDetailActivity.getTeamDetailIntent(this,teamId,"leader_group_info_return",null));
        finish();
    }

    @OnClick(R.id.success_add_member)
    public void add(){
        startActivity(AddMemberActivity.getAddMemberIntent(this,teamId));
    }
}
