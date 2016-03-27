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
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.toolbox.Volley;

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
    private RequestQueue mRequestQueue;

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
        mRequestQueue =  Volley.newRequestQueue(this);
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

        RestAdapterUtils.getTeamAPI().createTeam("3", "sdfsd", "sdf", "6", "create_group", new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorrMsg, Response response) {
                closeDialog();
                if (errorrMsg != null && errorrMsg.getResult() == 1) {
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
//        String url = HttpConfig.BASE_CMS_URL_NEW + "/py/group";
//        StringRequest  stringRequest = new StringRequest(
//                com.example.exerciseapp.volley.Request.Method.POST,
//                url,
//                new com.example.exerciseapp.volley.Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String s) {
//                        try {
////	                            	progressDialog.dismiss();
//                            JSONObject jsonObject = new JSONObject(s);
//                            if(jsonObject.getString("result").equals("1")){
//                                ScreenUtils.show_msg(CreateTeamActivity.this,"fsdfsadf");
//                            }else{
//                                Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (aa.e e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new com.example.exerciseapp.volley.Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
////	                        	progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
//                    }
//                }){
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("uid", "1");
//                map.put("group_name", "fsdf");
//                map.put("group_intro", "group_intro");
//                map.put("group_tag_id", "2");
//                map.put("action", "create_group");
//                return map;
//            }
//        };
//        mRequestQueue.add(stringRequest);

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
