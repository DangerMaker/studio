package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.GroupData;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/28.
 */
public class CreateTeamSucActivity extends BackBaseActivity {

    @Bind(R.id.success_add_member)
    TextView addMember;
    @Bind(R.id.success_team_icon)
    SimpleDraweeView draweeView;
    @Bind(R.id.success_team_name)
    TextView teamName;
    int teamId;

    public static Intent getCreateSuccessIntent(Context context, int teamId) {
        Intent intent = new Intent(context, CreateTeamSucActivity.class);
        return intent.putExtra("teamId", teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_success);
        setTitleBar("创建团队成功");
        teamId = getIntent().getIntExtra("teamId", -1);
        if (teamId == -1) {
            ScreenUtils.show_msg(this, "创建失败");
            finish();
            return;
        }
        load();
    }

    private void load() {
        RestAdapterUtils.getTeamAPI().getGroupInfo(teamId + "", "get_group_info", new Callback<GroupData>() {
            @Override
            public void success(GroupData data, Response response) {
                if (data != null && data.getResult() == 1) {
                    draweeView.setImageURI(Uri.parse(data.getData().getAvatar()));
                    teamName.setText(data.getData().getGroup_name());
                    // TODO: 2016/3/27
                } else {
                    ScreenUtils.show_msg(CreateTeamSucActivity.this, data.getDesc());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @OnClick(R.id.success_team_submit)
    public void enter() {
        startActivity(TeamDetailActivity.getTeamDetailIntent(this, teamId, "leader_group_info_return", null));
        finish();
    }

    @OnClick(R.id.success_add_member)
    public void add() {
        startActivity(AddMemberActivity.getAddMemberIntent(this, teamId));
    }
}
