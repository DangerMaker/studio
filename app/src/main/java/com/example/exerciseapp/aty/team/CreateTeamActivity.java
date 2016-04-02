package com.example.exerciseapp.aty.team;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.CreateSuc;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/27.
 */
public class CreateTeamActivity extends BackBaseActivity {

    @Bind(R.id.create_team_name)
    EditText mCreateName;
    @Bind(R.id.create_team_tag)
    EditText mCreateTag;
    @Bind(R.id.create_team_des)
    EditText mCreateDes;

    int teamId;

    public static Intent getCreateTeamIntent(Context context) {
        Intent intent = new Intent(context, CreateTeamActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_myteam);
        setTitleBar("创建团队");
    }

    @OnClick(R.id.create_team_submit)
    public void submit() {
        String uid = ((MyApplication) getApplication()).getUid();
        if (uid == null || uid.equals("")) {
            ScreenUtils.show_msg(this, "没有用户id,请登录");
            finish();
            return;
        }
        String name = mCreateName.getText().toString();
        String tag = mCreateTag.getText().toString();
        String des = mCreateDes.getText().toString();
        showDialog();

        RestAdapterUtils.getTeamAPI().createTeam(uid, name, des, "10", "create_group", new Callback<CreateSuc>() {
            @Override
            public void success(CreateSuc createSuc, Response response) {
                closeDialog();
                if (createSuc != null && createSuc.getResult() == 1) {
                    teamId = createSuc.getData().getId();
                    ScreenUtils.show_msg(CreateTeamActivity.this, "创建成功！");
                    startActivity(CreateTeamSucActivity.getCreateSuccessIntent(CreateTeamActivity.this, teamId));
                    finish();
                    // TODO: 2016/3/27
                } else {
                    ScreenUtils.show_msg(CreateTeamActivity.this, "创建失败！");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                closeDialog();
                ScreenUtils.show_msg(CreateTeamActivity.this, "创建失败" + error.getMessage());
            }
        });
    }

}
