package com.example.exerciseapp.aty.organzie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.model.Judge;
import com.example.exerciseapp.model.OrgPubInfo;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2016/9/4.
 */
public class OrganzieDetailActivity extends BindActivity {

    @Bind(R.id.org_name)
    TextView nameView;
    @Bind(R.id.org_id)
    TextView idView;
    @Bind(R.id.org_logo)
    SimpleDraweeView logoImageView;
    @Bind(R.id.org_star)
    SimpleDraweeView starImageView;
    @Bind(R.id.org_addr)
    TextView addrView;
    @Bind(R.id.org_subject)
    TextView subjectView;
    @Bind(R.id.org_join)
    TextView joinView;
    @Bind(R.id.org_slogan)
    TextView sloganView;

    @Bind(R.id.mm_part)
    LinearLayout mm;
    String orgId;
    int joinType;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }

    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_org);
        orgId = getIntent().getStringExtra("orgId");
    }

    private void load(){
        RestAdapterUtils.getZhuAPI().isManager(MyApplication.uid, "judge_is_admin", "3.3.0", orgId, new Callback<Judge>() {
            @Override
            public void success(Judge judge, Response response) {
                if(judge.getData() == 1){
                    flag = true;
                }else{
                    flag = false;
                }

                getData();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }

    public void getData(){
        RestAdapterUtils.getZhuAPI().getOrgInfo(MyApplication.uid,"get_brief","3.30",orgId, new Callback<OrgPubInfo>() {
            @Override
            public void success(OrgPubInfo orgPubInfo, Response response) {
                if(orgPubInfo != null && !isFinishing()){
                    nameView.setText(orgPubInfo.getData().getName());
                    idView.setText(orgId);
                    logoImageView.setImageURI(Uri.parse(orgPubInfo.getData().getImg_path()));
                    starImageView.setImageURI(Uri.parse(orgPubInfo.getData().getStars()));
                    addrView.setText(orgPubInfo.getData().getAddress());
                    String tempAddr = "";
                    for (int i = 0; i < orgPubInfo.getData().getAthletics().size(); i++) {
                        tempAddr = orgPubInfo.getData().getAthletics().get(i)  + "," + tempAddr;
                    }
                    tempAddr = tempAddr.substring(0,tempAddr.length()-1);
                    subjectView.setText(tempAddr);
                    String tempJoin;
                    joinType = orgPubInfo.getData().getJoin_type();
                    if(joinType == 0){
                        tempJoin = "无需验证";
                    }else{
                        tempJoin = "需要审核";
                    }
                    joinView.setText(tempJoin);
                    sloganView.setText(orgPubInfo.getData().getDesc());

                    if(flag){
                        mm.setVisibility(View.VISIBLE);
                    }else{
                        mm.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @OnClick({R.id.org_pjoin,R.id.org_pmessage,R.id.org_pslogan})
    public void btnStartActivity(View view){
        switch (view.getId()){
            case R.id.org_pjoin:
                Intent intent = new Intent(OrganzieDetailActivity.this,JoinTypeActivity.class);
                intent.putExtra("type",joinType);
                intent.putExtra("id",orgId);
                startActivity(intent);
                break;
            case R.id.org_pmessage:
                Intent intent2 = new Intent(OrganzieDetailActivity.this,GetApplyActivity.class);
                intent2.putExtra("id",orgId);
                startActivity(intent2);
                break;
            case R.id.org_pslogan:
                Intent intent1 = new Intent(OrganzieDetailActivity.this,ChangeSloganActivity.class);
                intent1.putExtra("id",orgId);
                intent1.putExtra("slogan",sloganView.getText().toString());
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
