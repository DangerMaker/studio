package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/30.
 */
public class TeamSettingActivity extends BackBaseActivity {


    @Bind(R.id.team_setting_id)
    TextView teamSettingId;
    @Bind(R.id.team_setting_name)
    RelativeLayout teamSettingName;
    @Bind(R.id.team_setting_des)
    RelativeLayout teamSettingDes;
    @Bind(R.id.team_setting_manager)
    RelativeLayout teamSettingManager;
    @Bind(R.id.team_setting_invate)
    RelativeLayout teamSettingInvate;
    @Bind(R.id.team_setting_disappear)
    Button teamSettingDisappear;

    Intent intent;
    private int teamId;
    private String name;
    private String des;
    public static Intent getTeamSettingIntent(Context context, int teamId,String name,String des) {
        Intent intent = new Intent(context, TeamSettingActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("name", name);
        intent.putExtra("des",des);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_setting);
        setTitleBar("团队设置");

        intent = getIntent();
        teamId = intent.getIntExtra("teamId", -1);
        name = intent.getStringExtra("name");
        des = intent.getStringExtra("des");

        teamSettingId.setText("团队ID:" + teamId);
    }

    @OnClick({R.id.team_setting_name,R.id.team_setting_des,R.id.team_setting_invate,R.id.team_setting_disappear})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.team_setting_name:
                startActivity(AlterTeamNameActivity.getAlterNameIntent(this,teamId,name));
                break;
            case R.id.team_setting_des:
                startActivity(AlterTeamDesActivity.getAlterDesIntent(this,teamId,des));
                break;
            case R.id.team_setting_invate:
                startActivity(AddMemberActivity.getAddMemberIntent(this,teamId));
                break;
            case R.id.team_setting_disappear:
                disappear();
                break;
        }
    }

    private void disappear() {
        RestAdapterUtils.getTeamAPI().breakGroup(teamId, getUid(), "break_group", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("解散成功");
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(TeamSettingActivity.this, "解散成功");
                    finish();
                } else {
                    ScreenUtils.show_msg(TeamSettingActivity.this, errorMsg.getDesc());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("解散失败");
                ScreenUtils.show_msg(TeamSettingActivity.this, "添加失败");
            }
        });
    }
}
