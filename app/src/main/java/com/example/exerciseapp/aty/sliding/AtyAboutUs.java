package com.example.exerciseapp.aty.sliding;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

import java.util.ArrayList;
import java.util.List;

public class AtyAboutUs extends BaseActivity {
    private ViewPager mPager;// 页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private ImageView cursorOneAtyAboutUs;
    private ImageView cursorTwoAtyAboutUs;
    TextView texttel;
    private Toolbar toolbar;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_about_us);
        mPager = (ViewPager) findViewById(R.id.vPagerAtyAboutUs);
        cursorOneAtyAboutUs = (ImageView) findViewById(R.id.cursorOneAtyAboutUs);
        cursorTwoAtyAboutUs = (ImageView) findViewById(R.id.cursorTwoAtyAboutUs);
        cursorTwoAtyAboutUs.setVisibility(View.INVISIBLE);
        setTitleBar(AtyAboutUs.this);
        InitTextView();
        InitViewPager();
    }

    public void setTitleBar(final Activity activity) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        pageTitle.setText("关于我们");
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.tvAssociationTitleAtyAboutUs);
        t2 = (TextView) findViewById(R.id.tvClubTitleAtyAboutUs);
        t1.setBackgroundColor(Color.rgb(40, 144, 178));
        t2.setBackgroundColor(Color.rgb(104, 177, 201));
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     * 初始化ViewPager
     */
    @SuppressLint("ResourceAsColor")
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPagerAtyAboutUs);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();

        // 协会列表初始化
        View viewAssociation = mInflater.inflate(R.layout.aty_app_brief_intro, null);

        // TODO

        listViews.add(viewAssociation);

        // 俱乐部列表初始化
        View viewClub = mInflater.inflate(R.layout.aty_contact_us, null);
        // TODO
        texttel = (TextView) viewClub.findViewById(R.id.texttel);
        texttel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Toast.makeText(getApplicationContext(), "呵呵dianhua",
                // 2).show();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:010-86381313"));
                startActivity(intent);
            }
        });

        listViews.add(viewClub);

        mPager.setAdapter(new ClubPagePagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * ViewPager适配器
     */
    public class ClubPagePagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public ClubPagePagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
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
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
