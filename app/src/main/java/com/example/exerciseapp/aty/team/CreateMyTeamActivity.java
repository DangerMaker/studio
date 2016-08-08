package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.exerciseapp.R;

import butterknife.OnClick;

/**
 * Created by lyjq on 2016/3/27.
 */
public class CreateMyTeamActivity extends BackBaseActivity {

    public static Intent getCreateMyTeamIntent(Context context) {
        Intent intent = new Intent(context, CreateMyTeamActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_on);
        setTitleBar("创建我的团队");
    }

    @OnClick(R.id.create_team_submit1)
    public void submit() {
        startActivity(CreateTeamActivity.getCreateTeamIntent(this));
        finish();
    }

}
