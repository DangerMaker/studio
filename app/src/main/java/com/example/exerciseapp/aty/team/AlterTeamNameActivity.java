package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.GroupInstance;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnEditorAction;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/4/1.
 */
public class AlterTeamNameActivity extends BackBaseActivity {

    String text;
    String teamId;
    String name;
    String token = MyApplication.getInstance().getToken();
    String uid = MyApplication.getInstance().getUid();
    String version = "3.0";
    @Bind(R.id.team_alter_name)
    EditText teamAlterName;

    public static Intent getAlterNameIntent(Context context, int teamId, String name) {
        Intent intent = new Intent(context, AlterTeamNameActivity.class);
        intent.putExtra("teamId", teamId);
        intent.putExtra("name", name);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_alter_name);
        setTitleBar("更改名称");
        teamId = getIntent().getIntExtra("teamId", -1) + "";
        name = getIntent().getStringExtra("name");
        teamAlterName.setText(name);
    }

    @OnEditorAction(R.id.team_alter_name)
    protected boolean onEditorAction(int actionID) {
        if (actionID == EditorInfo.IME_ACTION_SEND) {
            onClickSend();
        }
        return true;
    }

    private void onClickSend() {
        text = teamAlterName.getText().toString();
        if (TextUtils.isEmpty(text)) {
            System.out.println("输入内容不能为空");
            return;
        }

        RestAdapterUtils.getTeamAPI().changeTeamName(teamId, text, "groupname", "change_param", token, version, uid, new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("修改成功");
                ScreenUtils.show_msg(AlterTeamNameActivity.this, "修改成功");
                GroupInstance.getInstance().setGroup_name(text);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("修改失败");
                ScreenUtils.show_msg(AlterTeamNameActivity.this, "修改失败");
            }
        });

    }

}
