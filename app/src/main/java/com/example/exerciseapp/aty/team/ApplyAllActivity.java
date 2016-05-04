package com.example.exerciseapp.aty.team;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.fragment.ApplyPersonalFragment;
import com.example.exerciseapp.fragment.ApplyTeamFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * User: lyjq(1752095474)
 * Date: 2016-04-20
 */
public class ApplyAllActivity extends BackBaseActivity {

    @Bind(R.id.cursorOneAtyAboutUs)
    ImageView cursorOneAtyAboutUs;
    @Bind(R.id.cursorTwoAtyAboutUs)
    ImageView cursorTwoAtyAboutUs;
    @Bind(R.id.tvAssociationTitleAtyAboutUs)
    TextView t1;
    @Bind(R.id.tvClubTitleAtyAboutUs)
    TextView t2;

    @Bind(R.id.vPagerAtyAboutUs)
    ViewPager mPager;
    ViewPagerAdapter adapter;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    String gameId;
    String gameName;
    String agreement;
    JSONObject jsonObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        setTitleBar("报名表");

        gameId = getIntent().getStringExtra(Config.KEY_GAME_ID);
        gameName = getIntent().getStringExtra(Config.KEY_GAME_NAME);
        agreement = getIntent().getStringExtra("agreement");
        try {
            jsonObj = new JSONObject(getIntent().getStringExtra("entryInfor"));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (jsonObj == null) {
            return;
        }


        cursorTwoAtyAboutUs.setVisibility(View.INVISIBLE);
        t1.setBackgroundColor(Color.rgb(40, 144, 178));
        t2.setBackgroundColor(Color.rgb(104, 177, 201));

        if (mPager == null) return;

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @OnClick(R.id.tvAssociationTitleAtyAboutUs)
    public void t1(){
        mPager.setCurrentItem(0);
    }

    @OnClick(R.id.tvClubTitleAtyAboutUs)
    public void t2(){
        mPager.setCurrentItem(1);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (!cursorOneAtyAboutUs.isShown()) {
                        cursorOneAtyAboutUs.setVisibility(ImageView.VISIBLE);
                    }
                    t1.setBackgroundColor(Color.rgb(40, 144, 178));
                    t2.setBackgroundColor(Color.rgb(104, 177, 201));
                    cursorTwoAtyAboutUs.setVisibility(ImageView.INVISIBLE);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    t2.setBackgroundColor(Color.rgb(40, 144, 178));
                    t1.setBackgroundColor(Color.rgb(104, 177, 201));
                    if (!cursorTwoAtyAboutUs.isShown()) {
                        cursorTwoAtyAboutUs.setVisibility(ImageView.VISIBLE);
                    }
                    cursorOneAtyAboutUs.setVisibility(ImageView.INVISIBLE);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                // case 2:
                // if (currIndex == 0) {
                // animation = new TranslateAnimation(offset, two, 0, 0);
                // } else if (currIndex == 1) {
                // animation = new TranslateAnimation(one, two, 0, 0);
                // }
                // break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            // cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ApplyPersonalFragment.newInstance(gameId,gameName,agreement,jsonObj);
                default:
                    return ApplyTeamFragment.newInstance(gameId,gameName,agreement,jsonObj);
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
