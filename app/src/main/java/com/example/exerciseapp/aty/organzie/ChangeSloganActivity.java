package com.example.exerciseapp.aty.organzie;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2016/9/4.
 */
public class ChangeSloganActivity extends BindActivity {

    @Bind(R.id.org_alter_slogan)
    EditText editText;
    String orgId;
    String slogan;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_slogan);
        orgId = getIntent().getStringExtra("id");
        slogan = getIntent().getStringExtra("slogan");
        if(slogan != null && !slogan.equals("")){
            editText.setText(slogan);
        }


    }

    @OnClick(R.id.submit)
    public void submit(View view){

        slogan = editText.getText().toString();
        if(slogan != null && !slogan.trim().equals("")) {

            RestAdapterUtils.getZhuAPI().changeField(MyApplication.uid, "set_field", "3.3.0", orgId, MyApplication.token, "desc", slogan, new Callback<ErrorMsg>() {
                @Override
                public void success(ErrorMsg errorMsg, Response response) {
                    System.out.println("成功");
                    if (errorMsg != null && errorMsg.getResult() == 1) {
                        ScreenUtils.show_msg(ChangeSloganActivity.this, errorMsg.getDesc());
                    } else {
                        ScreenUtils.show_msg(ChangeSloganActivity.this, errorMsg.getDesc());
                    }
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    ScreenUtils.show_msg(ChangeSloganActivity.this, "修改失败");
                }
            });
        }
    }
}
