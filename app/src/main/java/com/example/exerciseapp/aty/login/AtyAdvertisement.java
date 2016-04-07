package com.example.exerciseapp.aty.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;

/**
 * Created by Cherie_No.47 on 2016/4/4 17:55.
 * Email jascal@163.com
 */
public class AtyAdvertisement extends BaseActivity {
    private ImageView backgroundImage;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
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
        intent.setClass(AtyAdvertisement.this, AtySlidingHome.class);
        startActivity(intent);
        finish();
    }
}
