package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.MemberListAdapter;
import com.example.exerciseapp.adapter.SelectMemberAdapter;
import com.example.exerciseapp.model.AllMember;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.Member;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/4/3.
 */
public class SelectMemberActivity extends BackBaseActivity implements SelectMemberAdapter.OnListClick{

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;

    @Bind(R.id.toolbar_text_right)
    TextView right;
    SelectMemberAdapter adapter;
    String teamId;
    String eid;
    List<Member> list;
    public static Intent getManagerMembersIntent(Context context,int teamId,String eid) {
        Intent intent = new Intent(context, SelectMemberActivity.class);
        intent.putExtra("eid",eid);
        return intent.putExtra("teamId", teamId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        setTitleBar("成员列表");
        teamId = getIntent().getIntExtra("teamId",-1) + "";
        eid = getIntent().getStringExtra("eid");
        adapter = new SelectMemberAdapter(this,this,teamId);
        right.setVisibility(View.VISIBLE);
        right.setText("确定");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Member member = list.get(i - 1);
                if(!member.isFlag()) {
                    check(member);
                }else {
                    member.setFlag(!member.isFlag());
                    adapter.updateItems(list);
                }

            }
        });
        load();
    }

    private void load() {
        RestAdapterUtils.getTeamAPI().showAllMembers(teamId,"show_all_members",new Callback<AllMember>() {
            @Override
            public void success(AllMember allMember, Response response) {
                if (allMember != null && allMember.getResult() == 1) {
                    if (allMember.getData() != null) {
                        list = allMember.getData();
                        adapter.addItems(list);
                    }
                }
                if (listView != null) listView.onRefreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(SelectMemberActivity.this, error.getMessage());
                if (listView != null) listView.onRefreshComplete();
            }
        });
    }

    private void check(final Member member) {
        RestAdapterUtils.getTeamAPI().checkUserInfo(member.getUid(),eid,new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    member.setFlag(!member.isFlag());
                    adapter.updateItems(list);
                }else{
                    ScreenUtils.show_msg(SelectMemberActivity.this, errorMsg.getDesc());
                }
            }


            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(SelectMemberActivity.this, error.getMessage());
            }
        });
    }

    @OnClick(R.id.toolbar_text_right)
    public void right(){
        StringBuilder uids = new StringBuilder();
        StringBuilder uidsName = new StringBuilder();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            Member member = (Member) iterator.next();
            if(member.isFlag()) {
                uids.append(member.getUid() + ",");
                uidsName.append(member.getUsername() + ",");
            }
        }

        if(uids.toString().trim().equals("")){
            finish();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("uids",uids.delete(uids.length() - 1,uids.length()).toString());
        intent.putExtra("uidsName",uidsName.delete(uidsName.length() - 1,uidsName.length()).toString());
        intent.putExtra("teamId",teamId);
        setResult(28,intent);
        finish();
    }

    @Override
    public void click(Member group) {

    }
}
