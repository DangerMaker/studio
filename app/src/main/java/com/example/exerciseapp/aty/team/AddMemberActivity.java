package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by lyjq on 2016/4/3.
 */
public class AddMemberActivity extends BackBaseActivity {

    String text;
    String teamId;
    @Bind(R.id.team_add_member)
    EditText teamAddMember;
    @Bind(R.id.team_add_btn)
    TextView teamAddBtn;
    @Bind(R.id.add_member_submit)
    Button addMemberSubmit;

    public static Intent getAddMemberIntent(Context context, int teamId) {
        Intent intent = new Intent(context, AddMemberActivity.class);
        return intent.putExtra("teamId", teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        setTitleBar("添加成员");
        teamId = getIntent().getIntExtra("teamId",-1) + "";
    }

    @OnClick(R.id.team_add_btn)
    public void teamAddBtn(){
        onClickSend();
    }

    @OnClick(R.id.add_member_submit)
    public void out(){
        finish();
    }

    private void onClickSend() {
        text = teamAddMember.getText().toString();
        if(TextUtils.isEmpty(text)){
            System.out.println("输入内容不能为空");
            return;
        }

        RestAdapterUtils.getTeamAPI().inviteFriends(teamId, text, "invite_friends",new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("添加成功");
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(AddMemberActivity.this, "添加成功");
                }else {
                    ScreenUtils.show_msg(AddMemberActivity.this, errorMsg.getDesc());
                }
                teamAddMember.setText("");
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("添加失败");
                ScreenUtils.show_msg(AddMemberActivity.this, "添加失败");
            }
        });

    }
}
