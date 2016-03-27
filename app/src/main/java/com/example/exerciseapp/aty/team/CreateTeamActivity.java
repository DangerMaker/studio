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
import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/27.
 */
public class CreateTeamActivity extends BaseActivity {

    @Bind(R.id.create_team_name)
    EditText mCreateName;
    @Bind(R.id.create_team_tag)
    EditText mCreateTag;
    @Bind(R.id.create_team_des)
    EditText mCreateDes;

    Toolbar toolbar;
    TextView pageTitle;

    public static Intent getCreateTeamIntent(Context context) {
//        if (StringUtils.isNullOrEmpty(tvShowId)) {
//            throwIllegalArgumentException();
//        }
        Intent intent = new Intent(context, CreateTeamActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_myteam);
        setTitleBar(this);
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
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("uid", "1");
        map.put("group_name", "fsdf");
        map.put("group_intro", "group_intro");
        map.put("group_tag_id", "2");
        map.put("action", "create_group");
        String str = gson.toJson(map);

//        RestAdapterUtils.getTeamAPI().createTeam(uid, name, tag, des, "create_group", new Callback<ErrorMsg>() {
        RestAdapterUtils.getTeamAPI().createTeam(str, new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorrMsg, Response response) {
                closeDialog();
                if (errorrMsg != null && errorrMsg.getResult().equals("1")) {
                    ScreenUtils.show_msg(CreateTeamActivity.this, "创建成功！");
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

    public void setTitleBar(final Activity activity) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        pageTitle.setText("创建团队");
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
