package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.CreateSuc;
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
    @Bind(R.id.create_team_tagadd)
    RelativeLayout mCreateTag;
    @Bind(R.id.create_team_des)
    EditText mCreateDes;
    @Bind(R.id.create_team_tag)
    TextView mTagText;

    int teamId;
    String token = MyApplication.getInstance().getToken();
    String uid = MyApplication.getInstance().getUid();
    String version = "3.0";

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

    @OnClick(R.id.create_team_tagadd)
    public void tag() {
        startActivityForResult(TeamTagActivity.getTeamTagIntent(this), 9);
    }

    @OnClick(R.id.create_team_submit)
    public void submit() {

        if (uid == null || uid.equals("")) {
            ScreenUtils.show_msg(this, "没有用户id,请登录");
            finish();
            return;
        }
        String name = mCreateName.getText().toString();
        String tag = mTagText.getText().toString();
        String des = mCreateDes.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ScreenUtils.show_msg(this, "团队名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(tag)) {
            ScreenUtils.show_msg(this, "请选择一个标签");
            return;
        }
        if (TextUtils.isEmpty(des)) {
            ScreenUtils.show_msg(this, "简介不能为空");
            return;
        }
        showDialog();

        RestAdapterUtils.getTeamAPI().createTeam(uid, name, des, tagCode, "create_group", token, version, new Callback<CreateSuc>() {
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

    String tagName = "跑步";
    String tagCode = "1";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            tagName = data.getStringExtra("tagName");
            tagCode = data.getStringExtra("tagId");
            mTagText.setText(tagName);
        }
    }
}
