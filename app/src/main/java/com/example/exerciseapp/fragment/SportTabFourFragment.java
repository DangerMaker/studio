package com.example.exerciseapp.fragment;

/**
 * Created by sonchcng on 16/6/4.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.activityrun.ActivityHotcard;
import com.example.exerciseapp.aty.activityrun.ActivityInteraction;
import com.example.exerciseapp.aty.activityrun.ActivityRunCircle;
import com.example.exerciseapp.view.ViewPager;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import java.util.ArrayList;

/**
 * 首页 fragment
 *
 * @author Sonchcng
 */
@SuppressLint("NewApi")
public class SportTabFourFragment extends Fragment {
    private static IWXAPI api;
    private View mview;
    public ImageView goback;
    private RequestQueue mRequestQueue;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private ImageView image;
    private ImageView personFriend;
    private ImageView commitcard;
    private ImageView commitinter;
    private int currIndex;//当前页卡编号
    private int offset;//图片移动的偏移量
    private TextView sportfriendhotcard;
    private TextView sportfriendfind;
    private TextView sportfriendinter;
    private boolean isfirst = true;
    private String webUrl;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        api = WXAPIFactory.createWXAPI(getActivity(), Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        mview = LayoutInflater.from(getActivity()).inflate(R.layout.activity_fragment_friend, container, false);

        InitTextView();
        InitImageView();
        InitViewPager();

        return mview;
    }

    private void InitImageView() {
        personFriend = (ImageView) mview.findViewById(R.id.sportperson);
        commitcard = (ImageView) mview.findViewById(R.id.sportfriendcommintcard);
        commitinter = (ImageView) mview.findViewById(R.id.sportfriendcommitinter);

        personFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), ActivityRunCircle.class);
                startActivity(intent);
            }
        });
        commitcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ActivityHotcard.class);
                startActivity(intent);
            }
        });
        commitinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ActivityInteraction.class);
                startActivity(intent);
            }
        });
    }


    public void InitTextView() {
        sportfriendinter = (TextView) mview.findViewById(R.id.sportfriendinter);
        sportfriendhotcard = (TextView) mview.findViewById(R.id.sportfriendhotcard);
        sportfriendfind = (TextView) mview.findViewById(R.id.sportfriendfind);

        sportfriendinter.setOnClickListener(new sportListener(0));
        sportfriendhotcard.setOnClickListener(new sportListener(1));
        sportfriendfind.setOnClickListener(new sportListener(2));
    }

    public class sportListener implements View.OnClickListener {
        private int index = 0;

        public sportListener(int position) {
            index = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            mPager.setCurrentItem(index);
        }
    }

    public void InitViewPager() {
        mPager = (ViewPager) mview.findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
        Fragment sportFriendTabfirstFragment = new SportFriendInterActionFragment();
        Fragment sportFriendTabsecondFragment = new SportFriendHotCardFragment();
        Fragment sportFriendTabthirdFragment = new SportFriendFindFriendFragment();
        fragmentList.add(sportFriendTabfirstFragment);
        fragmentList.add(sportFriendTabsecondFragment);
        fragmentList.add(sportFriendTabthirdFragment);
        //给ViewPager设置适配器
        mPager.setAdapter(new SportFriendFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList));
        mPager.setOffscreenPageLimit(3);
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
        mPager.setSmoothScroll(false);
        mPager.setPagingEnabled(false);
        switchtab(0);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int one = offset * 2;//两个相邻页面的偏移量

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);//平移动画
            currIndex = arg0;
            animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
            animation.setDuration(200);//动画持续时间0.2秒
//            image.startAnimation(animation);//是用ImageView来显示动画的

            if (currIndex == 0) {
                commitcard.setVisibility(View.GONE);
                commitinter.setVisibility(View.VISIBLE);

            } else if (currIndex == 1) {
                commitcard.setVisibility(View.VISIBLE);
                commitinter.setVisibility(View.GONE);
                sportfriendfind.setTextColor(getResources().getColor(R.color.blackfont));
                sportfriendhotcard.setTextColor(getResources().getColor(R.color.common_white));
                sportfriendinter.setTextColor(getResources().getColor(R.color.blackfont));
            } else {
                commitcard.setVisibility(View.INVISIBLE);
                commitinter.setVisibility(View.GONE);
            }
            switchtab(currIndex);
        }

    }
    public void switchtab(int pos){
        if (pos == 0) {
            sportfriendfind.setTextColor(getResources().getColor(R.color.blackfont));
            sportfriendhotcard.setTextColor(getResources().getColor(R.color.blackfont));
            sportfriendinter.setTextColor(getResources().getColor(R.color.common_white));

        } else if (pos == 1) {
            sportfriendfind.setTextColor(getResources().getColor(R.color.blackfont));
            sportfriendhotcard.setTextColor(getResources().getColor(R.color.common_white));
            sportfriendinter.setTextColor(getResources().getColor(R.color.blackfont));
        } else {
            sportfriendfind.setTextColor(getResources().getColor(R.color.common_white));
            sportfriendhotcard.setTextColor(getResources().getColor(R.color.blackfont));
            sportfriendinter.setTextColor(getResources().getColor(R.color.blackfont));
        }
    }
}
