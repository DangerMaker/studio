package com.example.exerciseapp.aty.organzie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.team.BackBaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/1.
 */
public class AlterEditActivity extends BindActivity {

    @Bind(R.id.oranzie_sub)
    TextView submit;
    @Bind(R.id.edit)
    EditText editText;

    @Bind(R.id.toolbar_text)
    TextView toolbarText;
    int id;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        switch (id) {
            case R.id.pname:
                toolbarText.setText("机构名称");
                editText.setText(CreateZhuzhiFragment.model.name);
                break;
            case R.id.paddr:
                toolbarText.setText("机构地址");
                editText.setText(CreateZhuzhiFragment.model.addr);
                break;
            case R.id.powner:
                toolbarText.setText("负责人");
                editText.setText(CreateZhuzhiFragment.model.owner);
                break;
            case R.id.pphone:
                toolbarText.setText("电话");
                editText.setText(CreateZhuzhiFragment.model.phone);
                break;
            case R.id.pqq:
                toolbarText.setText("QQ");
                editText.setText(CreateZhuzhiFragment.model.qq);
                break;
            default:
                toolbarText.setText("未知");
                break;
        }
    }

    @OnClick(R.id.oranzie_sub)
    public void setSubmit() {
        if (editText.getText().toString() != null && !editText.getText().toString().trim().equals("")) {
            switch (id) {
                case R.id.pname:
                    CreateZhuzhiFragment.model.name = editText.getText().toString();
                    break;
                case R.id.paddr:
                    CreateZhuzhiFragment.model.addr = editText.getText().toString();
                    break;
                case R.id.powner:
                    CreateZhuzhiFragment.model.owner = editText.getText().toString();
                    break;
                case R.id.pphone:
                    CreateZhuzhiFragment.model.phone = editText.getText().toString();
                    break;
                case R.id.pqq:
                    CreateZhuzhiFragment.model.qq = editText.getText().toString();
                    break;
                default:
                    toolbarText.setText("未知");
                    break;
            }
        }
        finish();
    }
}
