package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.GroupInstance;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/4/1.
 */
public class AlterTeamDesActivity extends BackBaseActivity {

    String text;
    String teamId;
    String des;

    @Bind(R.id.team_alter_des)
    EditText teamAlterDes;

    public static Intent getAlterDesIntent(Context context, int teamId, String des) {
        Intent intent = new Intent(context, AlterTeamDesActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("des", des);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_alter_des);
        ButterKnife.bind(this);
        setTitleBar("更改介绍");
        teamId = getIntent().getIntExtra("teamId", -1) + "";
        des = getIntent().getStringExtra("des");
        teamAlterDes.setText(des);
    }

    @OnEditorAction(R.id.team_alter_des)
    protected boolean onEditorAction(int actionID) {
        if (actionID == EditorInfo.IME_ACTION_SEND) {
            Log.e("des", "fsfd");
            onClickSend();
        }
        return true;
    }

    private void onClickSend() {
        text = teamAlterDes.getText().toString();
        if(TextUtils.isEmpty(text)){
            System.out.println("输入内容不能为空");
            return;
        }

        RestAdapterUtils.getTeamAPI().changeTeamDes(teamId, text, "group_intro", "change_param", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("修改成功");
                ScreenUtils.show_msg(AlterTeamDesActivity.this, "修改成功");
                GroupInstance.getInstance().setIntro(text);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("修改失败");
                ScreenUtils.show_msg(AlterTeamDesActivity.this, "修改失败");
            }
        });

    }
}
