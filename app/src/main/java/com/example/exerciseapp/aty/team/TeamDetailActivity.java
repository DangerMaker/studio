package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.GroupDetail;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/28.
 */
public class TeamDetailActivity extends BackBaseActivity implements View.OnClickListener {

    int teamId;
    GroupDetail.DataEntity entity;
    @Bind(R.id.detail_team_icon)
    ImageView detailTeamIcon;
    @Bind(R.id.detail_team_name)
    TextView detailTeamName;
    @Bind(R.id.detail_team_num)
    TextView detailTeamNum;
    @Bind(R.id.detail_team_des)
    TextView detailTeamDes;
    @Bind(R.id.detail_team_access)
    TextView detailTeamAccess;
    @Bind(R.id.detail_team_allMember)
    TextView detailTeamAllMember;
    @Bind(R.id.detail_team_some)
    GridLayout detailTeamSome;
    @Bind(R.id.detail_team_rank)
    GridLayout detailTeamRank;

    @Bind(R.id.toolbar_img_right)
    ImageView setting;

    LayoutInflater inflater;

    int screenWidth;

    public static Intent getTeamDetailIntent(Context context, int teamId) {
        Intent intent = new Intent(context, TeamDetailActivity.class);
        return intent.putExtra("teamId", teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myteam);
        ButterKnife.bind(this);
        setTitleBar("我的团队");
        setting.setVisibility(View.VISIBLE);
        inflater = LayoutInflater.from(this);
        screenWidth = ScreenUtils.getScreenWidth(this);
        widthUnit = screenWidth / 3;
        teamId = getIntent().getIntExtra("teamId", -1);
        if (teamId == -1) {
            ScreenUtils.show_msg(this, "创建失败");
            finish();
            return;
        }

        if (!isUidAvailable()) {
            finish();
            return;
        }

        RestAdapterUtils.getTeamAPI().getDetailInfo(teamId + "", getUid(), "get_detail_group_info", new Callback<GroupDetail>() {
            @Override
            public void success(GroupDetail groupDetail, Response response) {
                if (groupDetail != null && groupDetail.getResult() == 1) {
                    if (isFinishing()) {
                        return;
                    }
                    setDataToDetail(groupDetail.getData());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    private int widthUnit;

    private void setDataToDetail(GroupDetail.DataEntity entity) {
        this.entity = entity;
        detailTeamName.setText(entity.getGroup_info().getGroup_name());
        detailTeamNum.setText("团队人数：" + entity.getGroup_info().getMembernum());
        detailTeamDes.setText(entity.getGroup_info().getIntro());

        if (entity.getUser_info_some_return().size() == 0) {
            detailTeamAllMember.setText("去邀请新成员");
        }

        for (int i = 0; i < entity.getUser_info_some_return().size(); i++) {
            LinearLayout userItem = (LinearLayout) inflater.inflate(R.layout.item_team_some, detailTeamSome, false);
            TextView name = (TextView) userItem.findViewById(R.id.item_some_name);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.setGravity(Gravity.FILL);
            param.width = widthUnit;
            userItem.setOnClickListener(this);
            userItem.setLayoutParams(param);
            userItem.setTag(entity.getUser_info_some_return().get(i));
            name.setText(entity.getUser_info_some_return().get(i).getUsername());
            if (detailTeamSome != null)
                detailTeamSome.addView(userItem);
        }

        if (entity.getUser_info_point_some_return().size() == 0) {
            detailTeamRank.setVisibility(View.GONE);
        }

        for (int i = 0; i < entity.getUser_info_point_some_return().size(); i++) {
            LinearLayout userItem = (LinearLayout) inflater.inflate(R.layout.item_team_rank, detailTeamSome, false);
            TextView name = (TextView) userItem.findViewById(R.id.item_some_name);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.setGravity(Gravity.FILL);
            param.width = screenWidth;
            userItem.setOnClickListener(this);
            userItem.setLayoutParams(param);
            userItem.setTag(entity.getUser_info_point_some_return().get(i));
            name.setText(entity.getUser_info_point_some_return().get(i).getUsername());
            if (detailTeamRank != null)
                detailTeamRank.addView(userItem);
        }


    }

    @Override
    public void onClick(View v) {

    }

    @OnClick(R.id.toolbar_img_right)
    public void rightClick(){
        startActivity(TeamSettingActivity.getTeamSettingIntent(this, teamId,entity.getGroup_info().getGroup_name(),entity.getGroup_info().getIntro()));
    }
}
