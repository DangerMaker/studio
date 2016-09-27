package com.example.exerciseapp.aty.organzie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.OrgPubInfo;
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
public class JoinTypeActivity extends BindActivity {

    @Bind(R.id.submit)
    TextView submitView;
    @Bind(R.id.img1)
    ImageView imageView1;
    @Bind(R.id.img2)
    ImageView imageView2;

    int type;
    String id;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_org);
        type = getIntent().getIntExtra("type",-1);
        id = getIntent().getStringExtra("id");
        if(type == 0){
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.GONE);
        }else{
            imageView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.none,R.id.need,R.id.submit})
    public void selectOne(View view){
        if(view.getId() == R.id.none){
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.GONE);
            type = 0;
        }else if(view.getId() == R.id.need){
            imageView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.VISIBLE);
            type = 1;
        }else if(view.getId() == R.id.submit){
            RestAdapterUtils.getZhuAPI().changeField(MyApplication.uid, "set_field", "3.3.0", id, MyApplication.token, "join_type", Integer.toString(type), new Callback<ErrorMsg>() {
                @Override
                public void success(ErrorMsg errorMsg, Response response) {
                    System.out.println("成功");
                    if (errorMsg != null && errorMsg.getResult() == 1) {
                        ScreenUtils.show_msg(JoinTypeActivity.this, errorMsg.getDesc());
                    } else {
                        ScreenUtils.show_msg(JoinTypeActivity.this, errorMsg.getDesc());
                    }
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    ScreenUtils.show_msg(JoinTypeActivity.this, "修改失败");
                }
            });
        }
    }
}
