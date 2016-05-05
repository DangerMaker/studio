package com.example.exerciseapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyUserLawItem;
import com.example.exerciseapp.aty.sliding.AtyPay;
import com.example.exerciseapp.aty.team.GameSelectActivity;
import com.example.exerciseapp.aty.team.SearchActivity;
import com.example.exerciseapp.model.CreateSuc;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.GameList;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.volley.Cache;
import com.ta.utdid2.android.utils.SystemUtils;

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
public class ApplyPersonalFragment extends BaseFragment {

    @Bind(R.id.game_name)
    TextView name;
    @Bind(R.id.personal_name)
    EditText personalName;
    @Bind(R.id.personal_sex)
    TextView personalSex;
    //    @Bind(R.id.personal_age)
//    EditText personalAge;
    @Bind(R.id.personal_phone)
    EditText personalPhone;
    @Bind(R.id.personal_card)
    EditText personalCard;

    @Bind(R.id.game_organize)
    TextView organize;
    @Bind(R.id.game_select)
    TextView select;
    @Bind(R.id.game_pay)
    TextView gamePay;
    @Bind(R.id.cbAgreeRulesAssocEntryForm)
    CheckBox cbAgreeRulesAssocEntryForm;
    @Bind(R.id.btnCommitPersonalEntry)
    Button commit;

    String gameId;
    String gameName;
    String agreement;
    JSONObject jsonObject;

    String org_name = "北京海淀体育组织";

    public static ApplyPersonalFragment newInstance(String gameId, String gameName, String agreement, JSONObject jsonObject) {
        ApplyPersonalFragment fragment = new ApplyPersonalFragment();
        fragment.gameId = gameId;
        fragment.gameName = gameName;
        fragment.jsonObject = jsonObject;
        fragment.agreement = agreement;
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_apply_personal;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name.setText(gameName);
        try {
            JSONObject userInfo = (JSONObject) jsonObject.get("userInfo");
            personalName.setText(userInfo.getString("username"));
            personalSex.setText(userInfo.getString("sex"));
//            personalAge.setText(userInfo.getString("age"));
            personalPhone.setText(userInfo.getString("tel"));
            personalCard.setText(userInfo.getString("idcard"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        personalSex.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 点击出现选择性别
                new AlertDialog.Builder(getActivity())
                        .setItems(new String[]{"男", "女"},
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        switch (which) {
                                            case 0:
                                                personalSex.setText("男");
                                                break;
                                            case 1:
                                                personalSex.setText("女");
                                                break;
                                            default:
                                                break;
                                        }
                                        Toast.makeText(getActivity(),
                                                "你选择了: " + which, Toast.LENGTH_SHORT).show();
                                    }

                                }).show();

            }
        });

        cbAgreeRulesAssocEntryForm.setChecked(true);
        cbAgreeRulesAssocEntryForm.setClickable(true);
        cbAgreeRulesAssocEntryForm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!cbAgreeRulesAssocEntryForm.isChecked()) {
                    cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_on_background);
                } else {
                    cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_off_background);
                }
            }
        });
        cbAgreeRulesAssocEntryForm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO 勾选和不勾选的动作分别是？？？？
                if (isChecked) {
                    commit.setClickable(false);
                    Toast.makeText(getActivity(), "请阅读协议", Toast.LENGTH_SHORT).show();
                } else {
                    commit.setClickable(true);
                }


            }
        });
    }

    @OnClick(R.id.btnCommitPersonalEntry)
    public void commit() {

        if (detailId == null || detailId.trim().equals("")) {
            ScreenUtils.show_msg(getActivity(), "请选择项目");
            return;
        }

        if (TextUtils.isEmpty(personalPhone.getText().toString())) {
            ScreenUtils.show_msg(getActivity(), "请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(personalCard.getText().toString())) {
            ScreenUtils.show_msg(getActivity(), "请输入身份证");
            return;
        }

        if (TextUtils.isEmpty(personalName.getText().toString())) {
            ScreenUtils.show_msg(getActivity(), "请输入名字");
            return;
        }

        if (TextUtils.isEmpty(organize.getText().toString()) || organize.getText().toString().equals("点击选择 >")) {
            ScreenUtils.show_msg(getActivity(), "请输入组织名字");
            return;
        }

        RestAdapterUtils.getTeamAPI().applyPersonal(gameId, detailId, personalPhone.getText().toString(),
                personalSex.getText().toString(), personalCard.getText().toString(), personalName.getText().toString(),
                organize.getText().toString(), Config.getCachedUserUid(getActivity()), new Callback<CreateSuc>() {
                    @Override
                    public void success(CreateSuc errorMsg, Response response) {
                        if (errorMsg != null) {
                            if (errorMsg.getResult() == 1) {
                                ScreenUtils.show_msg(getActivity(), "报名成功");
                                if (Float.parseFloat(fee) != 0) {
                                    Intent intent = new Intent(getActivity(), AtyPay.class);
                                    intent.putExtra("id", errorMsg.getData().getId() + "");
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
        intent.putExtra("is_group", "0");
        startActivityForResult(intent, 24);
    }

    @OnClick(R.id.tvRules)
    public void enterAgreement() {
        Intent intent = new Intent(getActivity(),AtyUserLawItem.class);
        intent.putExtra("title","报名须知");
        intent.putExtra("url",agreement);
        startActivity(intent);
    }

    String detailId;
    String fee = "0";

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
                String gameDetailId = data.getIntExtra("game_detail_id", -1) + "";
                String gameDetailName = data.getStringExtra("game_detail_name");
                fee = data.getStringExtra("game_fee");
                if (gameDetailId != null) {
                    select.setText(gameDetailName);
                    detailId = gameDetailId;
                }
                if (fee != null)
                    gamePay.setText(fee);
            }
        }
    }
}
