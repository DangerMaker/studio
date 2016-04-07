package com.example.exerciseapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.exerciseapp.aty.login.AtyAdvertisement;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends Activity {

    ImageView imageView;
    public static PushAgent mPushAgent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
        mPushAgent = PushAgent.getInstance(getApplicationContext());
        mPushAgent.enable();
        mPushAgent.onAppStart();
        String device_token = mPushAgent.getRegistrationId();
        System.out.println(device_token);
        //如果已经登录过了，就直接进入主界面
        if(Config.getCachedUserUid(getApplicationContext())!=null){
            if(Config.getCachedUserUid(getApplicationContext()).equals("")){
                startActivity(new Intent(MainActivity.this, AtyWelcome.class));
            }else{
                startActivity(new Intent(MainActivity.this, AtySlidingHome.class));
            }
        }else{
            startActivity(new Intent(MainActivity.this, AtyWelcome.class));
        }
        finish();
    }
}
