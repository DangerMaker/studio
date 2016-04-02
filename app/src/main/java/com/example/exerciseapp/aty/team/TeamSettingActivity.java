package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        ButterKnife.bind(this);
        setTitleBar("团队设置");

        intent = getIntent();
        teamId = intent.getIntExtra("teamId", -1);
        name = intent.getStringExtra("name");
        des = intent.getStringExtra("des");

        teamSettingId.setText("团队ID:" + teamId);
    }

    @OnClick({R.id.team_setting_name})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.team_setting_name:
                startActivity(AlterTeamNameActivity.getAlterNameIntent(this,teamId,name));
                break;
        }
    }
}
