package com.example.exerciseapp.aty.login;
/*
 * 第二个注册界面
 */

import android.app.Activity;
import android.os.Bundle;

import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

public class AtyRegisterPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_register_page);
    }
}
