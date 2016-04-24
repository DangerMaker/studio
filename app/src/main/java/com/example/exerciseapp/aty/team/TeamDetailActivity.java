package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyLogin;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.GroupDetail;
import com.example.exerciseapp.model.GroupInstance;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

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
    String type;
    GroupDetail.DataEntity entity;
    @Bind(R.id.detail_team_icon)
    SimpleDraweeView detailTeamIcon;
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
    //
//    @Bind(R.id.toolbar_text_right)
//    TextView add;
    @Bind(R.id.toolbar_img_right)
    ImageView setting;

    @Bind(R.id.view_parent)
    LinearLayout view_parent;

    LayoutInflater inflater;

    int screenWidth;
    String invite_id;

    public static Intent getTeamDetailIntent(Context context, int teamId, String type, String invite_id) {
        Intent intent = new Intent(context, TeamDetailActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("type", type);
        intent.putExtra("invite_id", invite_id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myteam);
        setTitleBar("我的团队");

        inflater = LayoutInflater.from(this);
        screenWidth = ScreenUtils.getScreenWidth(this);
        widthUnit = screenWidth / 3;
        teamId = getIntent().getIntExtra("teamId", -1);
        type = getIntent().getStringExtra("type");
        if (teamId == -1) {
            ScreenUtils.show_msg(this, "创建失败");
            finish();
            return;
        }

        if (type.equals("leader_group_info_return")) {
            setting.setVisibility(View.VISIBLE);
        } else if (type.equals("invite_list_return")) {
            setting.setVisibility(View.GONE);
            invite_id = getIntent().getStringExtra("invite_id");
            setting.post(new Runnable() {
                @Override
                public void run() {
                    showAgreeWindow();
                }
            });
        } else if (type.equals("group_info_normal")) {
            setting.setVisibility(View.GONE);
            setting.post(new Runnable() {
                @Override
                public void run() {
                    showApplyWindow();
                }
            });
        } else if (type.equals("group_info_return")) {
            setting.setVisibility(View.VISIBLE);
        }

        if (!isUidAvailable()) {
            finish();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
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

        detailTeamSome.removeAllViews();
        detailTeamRank.removeAllViews();
        GroupInstance.getInstance().setAvatar(entity.getGroup_info().getAvatar());
        GroupInstance.getInstance().setGroup_name(entity.getGroup_info().getGroup_name());
        GroupInstance.getInstance().setIntro(entity.getGroup_info().getIntro());
        GroupInstance.getInstance().setId(Integer.parseInt(entity.getGroup_info().getId()));

        detailTeamIcon.setImageURI(Uri.parse(entity.getGroup_info().getAvatar()));
        detailTeamName.setText(entity.getGroup_info().getGroup_name());
        detailTeamNum.setText("团队成员：" + entity.getGroup_info().getMembernum()+"人");
        detailTeamDes.setText(entity.getGroup_info().getIntro());

//        if (entity.getUser_info_some_return().size() == 0) {
//            detailTeamAllMember.setText("去邀请新成员");
//        }
        int someSize = 0;
        if(entity.getUser_info_some_return().size() > 6){
            someSize = 6;
        }else{
            someSize = entity.getUser_info_some_return().size();
        }

        for (int i = 0; i < someSize; i++) {
            LinearLayout userItem = (LinearLayout) inflater.inflate(R.layout.item_team_some, detailTeamSome, false);
            TextView name = (TextView) userItem.findViewById(R.id.item_some_name);
            SimpleDraweeView item_some_img = (SimpleDraweeView) userItem.findViewById(R.id.item_some_img);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.setGravity(Gravity.FILL);
            param.width = widthUnit;
            userItem.setOnClickListener(this);
            userItem.setLayoutParams(param);
            item_some_img.setImageURI(Uri.parse(entity.getUser_info_some_return().get(i).getAvatar()));
            userItem.setTag(entity.getUser_info_some_return().get(i));
            name.setText(entity.getUser_info_some_return().get(i).getUsername());
            if (detailTeamSome != null)
                detailTeamSome.addView(userItem);
        }

//        if (entity.getUser_info_point_some_return().size() == 0) {
//            detailTeamRank.setVisibility(View.GONE);
//        }
        int rankSize = 0;
        if(entity.getUser_info_some_return().size() > 3){
            rankSize = 3;
        }else{
            rankSize = entity.getUser_info_point_some_return().size();
        }

        for (int i = 0; i < rankSize; i++) {
            RelativeLayout userItem = (RelativeLayout) inflater.inflate(R.layout.item_team_rank, detailTeamSome, false);
            TextView name = (TextView) userItem.findViewById(R.id.item_some_name);
            SimpleDraweeView item_some_img = (SimpleDraweeView) userItem.findViewById(R.id.item_some_img);
            TextView score = (TextView) userItem.findViewById(R.id.item_some_score);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.setGravity(Gravity.FILL);
            param.width = screenWidth;
            userItem.setOnClickListener(this);
            userItem.setLayoutParams(param);
            userItem.setTag(entity.getUser_info_point_some_return().get(i));
            score.setText(entity.getUser_info_point_some_return().get(i).getPoint() + "积分");
            item_some_img.setImageURI(Uri.parse(entity.getUser_info_point_some_return().get(i).getAvatar()));
            name.setText(entity.getUser_info_point_some_return().get(i).getUsername());
            if (detailTeamRank != null)
                detailTeamRank.addView(userItem);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invate_agree:
                System.out.println("invate_agree");
                agree();
                popupWindow.dismiss();
                break;
            case R.id.invate_disagree:
                System.out.println("invate_disagree");
                disagree();
                popupWindow.dismiss();
                break;
            case R.id.invate_apply:
                apply();
                popupWindow.dismiss();
                break;
        }
    }

    @OnClick(R.id.detail_team_access)
    public void acess(){
        ScreenUtils.show_msg(this,"请到首页参加活动");
    }

    @OnClick(R.id.toolbar_img_right)
    public void rightClick() {
        startActivityForResult(TeamSettingActivity.getTeamSettingIntent(this,type),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2){
            finish();
        }
    }

    @OnClick(R.id.detail_team_allMember)
    public void allMember() {
        startActivity(CheckMembersActivity.getCheckMembersIntent(this, teamId));
    }

    @OnClick(R.id.detail_team_allRank)
    public void allRank() {
        startActivity(CheckRanksActivity.getCheckRanksIntent(this,teamId));
    }

    PopupWindow popupWindow;

    private void showAgreeWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.view_invate_agree, null);
        Button agree = (Button) convertView.findViewById(R.id.invate_agree);
        Button disagree = (Button) convertView.findViewById(R.id.invate_disagree);
        agree.setOnClickListener(this);
        disagree.setOnClickListener(this);
        popupWindow = new PopupWindow(convertView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view_parent, Gravity.BOTTOM, 0, 0);
    }

    private void showApplyWindow() {
        View convertView = LayoutInflater.from(this).inflate(R.layout.view_invate_apply, null);
        Button apply = (Button) convertView.findViewById(R.id.invate_apply);
        apply.setOnClickListener(this);
        popupWindow = new PopupWindow(convertView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
//
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view_parent, Gravity.BOTTOM, 0, 0);
    }


    private void agree() {
        RestAdapterUtils.getTeamAPI().passInvite(invite_id, "pass_invite", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    if (isFinishing()) {
                        return;
                    }
                    ScreenUtils.show_msg(TeamDetailActivity.this, "加入成功");
                    if (popupWindow != null)
                        popupWindow.dismiss();
                    load();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(TeamDetailActivity.this, "加入失败");
            }
        });
    }

    private void disagree() {
        RestAdapterUtils.getTeamAPI().refuseInvite(invite_id, "refuse_invite", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    if (isFinishing()) {
                        return;
                    }
                    ScreenUtils.show_msg(TeamDetailActivity.this, "拒绝成功");
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(TeamDetailActivity.this, "拒绝失败");
            }
        });
    }

    private void apply() {

        if (!isUidAvailable()) {
            Intent i = new Intent(this, AtyLogin.class);
            startActivity(i);
            return;
        }

        RestAdapterUtils.getTeamAPI().postApply(teamId + "", "是", getUid(), "post_apply", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    if (isFinishing()) {
                        return;
                    }
                    ScreenUtils.show_msg(TeamDetailActivity.this, "申请成功");
                    if (popupWindow != null)
                        popupWindow.dismiss();

                    load();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(TeamDetailActivity.this, "申请失败");
            }
        });
    }

}
