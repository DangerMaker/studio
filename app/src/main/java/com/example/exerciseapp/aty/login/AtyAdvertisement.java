package com.example.exerciseapp.aty.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;
import com.example.exerciseapp.TabMainActivity;

/**
 * Created by Cherie_No.47 on 2016/4/4 17:55.
 * Email jascal@163.com
 */
public class AtyAdvertisement extends BaseActivity {
    private ImageView backgroundImage;
    private Animation animation;
    public String bind_layer_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        Intent intent = getIntent();
        bind_layer_show = intent.getStringExtra("bind_layer_show");
        initView();
    }

    public void initView() {
        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        animation = AnimationUtils.loadAnimation(this, R.anim.entrance);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                next();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        backgroundImage.startAnimation(animation);
    }

    public void next() {
        Intent intent = new Intent();
        intent.putExtra("bind_layer_show", "" + bind_layer_show);
        intent.setClass(AtyAdvertisement.this, TabMainActivity.class);
        startActivity(intent);
        finish();
    }
}
