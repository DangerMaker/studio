package com.example.exerciseapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtyPay;
import com.example.exerciseapp.aty.team.GameSelectActivity;
import com.example.exerciseapp.aty.team.SearchActivity;
import com.example.exerciseapp.aty.team.SelectTeamActivity;
import com.example.exerciseapp.model.CreateSuc;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * User: lyjq(1752095474)
 * Date: 2016-04-20
 */
public class ApplyTeamFragment extends BaseFragment {

    @Bind(R.id.game_name)
    TextView name;
    @Bind(R.id.game_organize)
    TextView organize;
    @Bind(R.id.game_select)
    TextView select;
    @Bind(R.id.game_pay)
    TextView gamePay;

    @Bind(R.id.organize_member)
    TextView member;
    @Bind(R.id.organize_leader)
    EditText leader;
    @Bind(R.id.game_phone)
    EditText phone;
    @Bind(R.id.game_mail)
    EditText mail;

    String gameId;
    String gameName;
    String uid;
    JSONObject jsonObject;

    public static ApplyTeamFragment newInstance(String gameId, String gameName, JSONObject jsonObject) {
        ApplyTeamFragment fragment = new ApplyTeamFragment();
        fragment.gameId = gameId;
        fragment.gameName = gameName;
        fragment.jsonObject = jsonObject;
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_apply_team;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name.setText(gameName);
        try {
            JSONObject userInfo = (JSONObject) jsonObject.get("userInfo");
            leader.setText(userInfo.getString("username"));
            phone.setText(userInfo.getString("tel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnCommitPersonalEntry)
    public void commit() {

        if(detailId ==null || detailId.trim().equals("")){
            ScreenUtils.show_msg(getActivity(),"请选择项目");
            return;
        }

        if(TextUtils.isEmpty(phone.getText().toString())){
            ScreenUtils.show_msg(getActivity(),"请输入手机号");
            return;
        }

        if(TextUtils.isEmpty(leader.getText().toString())){
            ScreenUtils.show_msg(getActivity(),"请输入领队");
            return;
        }
        if(TextUtils.isEmpty(mail.getText().toString())){
            ScreenUtils.show_msg(getActivity(),"请输入邮箱");
            return;
        }

        if(uid != null && uid.trim().equals("")){
            ScreenUtils.show_msg(getActivity(),"请选择队员");
            return;
        }

        if(TextUtils.isEmpty( organize.getText().toString()) || organize.getText().toString().equals("点击选择 >")){
            ScreenUtils.show_msg(getActivity(),"请输入组织名字");
            return;
        }

        RestAdapterUtils.getTeamAPI().applyTeam(teamId,uid,gameId,detailId , phone.getText().toString(),
                leader.getText().toString(), mail.getText().toString(), organize.getText().toString(), new Callback<CreateSuc>() {
                    @Override
                    public void success(CreateSuc errorMsg, Response response) {
                        if (errorMsg != null) {
                            if (errorMsg.getResult() == 1) {
                                ScreenUtils.show_msg(getActivity(), "报名成功");
                                if(Float.parseFloat(fee) != 0){
                                    Intent intent = new Intent(getActivity(), AtyPay.class);
                                    intent.putExtra("ueid", errorMsg.getData().getId() +"");
                                    intent.putExtra(Config.KEY_USER_ATTEND_ENAME, select.getText().toString());
                                    intent.putExtra("apayfee", gamePay.getText().toString());
                                    intent.putExtra("type", "game");
                                    startActivity(intent);
                                }
                                getActivity().finish();
                            } else {
                                ScreenUtils.show_msg(getActivity(), errorMsg.getDesc());
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
    }

    @OnClick(R.id.game_organize)
    public void setOrganize() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("gid", gameId);
        startActivityForResult(intent, 23);
    }

    @OnClick(R.id.game_select)
    public void setSelect() {
        Intent intent = new Intent(getActivity(), GameSelectActivity.class);
        intent.putExtra("gid", gameId);
        intent.putExtra("is_group", "1");
        startActivityForResult(intent, 24);
    }

    @OnClick(R.id.organize_member)
    public void setMember(){
        if(detailId == null){
            ScreenUtils.show_msg(getActivity(),"请先选择项目");
            return;
        }
        Intent intent = new Intent(getActivity(), SelectTeamActivity.class);
        intent.putExtra("eid", detailId);
        startActivityForResult(intent, 25);
    }

    String detailId;
    String teamId;
    String fee;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            if (data != null) {
                String name = data.getStringExtra("name");
                if (name != null) {
                    organize.setText(name);
                }
            }
        } else if (resultCode == 21) {
            if (data != null) {
                String gameDetailId = data.getIntExtra("game_detail_id",-1) + "";
                String gameDetailName = data.getStringExtra("game_detail_name");
                fee = data.getStringExtra("game_fee");
                if (gameDetailId != null) {
                    select.setText(gameDetailName);
                    detailId = gameDetailId;
                }
                if (fee != null)
                    gamePay.setText(fee);

                member.setText("去选择");
                uid = "";
            }
        } else if(resultCode == 29){
            if(data != null){
                String uids = data.getStringExtra("uids");
                String uidsName = data.getStringExtra("uidsName");
                String id = data.getStringExtra("teamId");
                member.setText(uidsName);
                uid = uids;
                teamId = id;
                if(uid.contains(",")){
                    String temp[] = uid.split(",");
                    gamePay.setText(Integer.parseInt(fee) * temp.length + "");
                }
            }
        }
    }

}
