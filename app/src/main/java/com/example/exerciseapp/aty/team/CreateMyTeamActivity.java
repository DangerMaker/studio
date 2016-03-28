package com.example.exerciseapp.aty.team;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;

import butterknife.OnClick;

/**
 * Created by lyjq on 2016/3/27.
 */
public class CreateMyTeamActivity extends BaseActivity {

    Toolbar toolbar;
    TextView pageTitle;

    public static Intent getCreateMyTeamIntent(Context context) {
        Intent intent = new Intent(context, CreateMyTeamActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_on);
        setTitleBar(this);
    }

    @OnClick(R.id.create_team_submit1)
    public void submit(){
        startActivity(CreateTeamActivity.getCreateTeamIntent(this));
    }

    public void setTitleBar(final Activity activity) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        pageTitle.setText("创建我的团队");
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
}
