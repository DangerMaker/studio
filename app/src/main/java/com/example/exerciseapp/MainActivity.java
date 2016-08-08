package com.example.exerciseapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.exerciseapp.aty.login.AtyWelcome;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends Activity {

    public static PushAgent mPushAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
        mPushAgent = PushAgent.getInstance(getApplicationContext());
        mPushAgent.enable();
        mPushAgent.onAppStart();
        if (Config.getCachedUserUid(getApplicationContext()) != null) {
            if (Config.getCachedUserUid(getApplicationContext()).equals("")) {
                startActivity(new Intent(MainActivity.this, AtyWelcome.class));
            } else {
                startActivity(new Intent(MainActivity.this, TabMainActivity.class));
            }
        } else {
            startActivity(new Intent(MainActivity.this, AtyWelcome.class));
        }
        finish();

    }
}
