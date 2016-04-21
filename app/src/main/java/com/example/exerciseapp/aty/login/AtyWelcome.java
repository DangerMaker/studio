package com.example.exerciseapp.aty.login;
/*
 * 欢迎界面
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pers.medusa.circleindicator.widget.CircleIndicator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

public class AtyWelcome extends Activity {

	 private List<View> viewList;
	 private ViewPager viewPager;
	 private CircleIndicator circleIndicator;
	 public static Activity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_welcome);
		instance = this;
		initData();
		viewPager = (ViewPager) findViewById(R.id.viewPagerWelcome);
		viewPager.setAdapter(pagerAdapter);
		circleIndicator = (CircleIndicator) findViewById(R.id.indicatorWelcome);
		circleIndicator.setViewPager(viewPager);
		//现有用户按钮监听事件
		findViewById(R.id.btnCurrentUser).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(AtyWelcome.this, AtyLogin.class),0);
			}
		});

		//立即加入按钮监听事件
		findViewById(R.id.btnJoinNow).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(AtyWelcome.this, AtyRegisterHomePage.class),0);
			}
		});

	}
	@SuppressLint("NewApi") private void initData(){
        viewList = new ArrayList<View>();
        Random random = new Random();

        ImageView view1 = new ImageView(getApplicationContext());
        view1.setImageDrawable(getResources().getDrawable(R.drawable.welcome1));
        viewList.add(view1);
        ImageView view2 = new ImageView(getApplicationContext());
        view2.setImageDrawable(getResources().getDrawable(R.drawable.welcome2));
        viewList.add(view2);
        ImageView view3 = new ImageView(getApplicationContext());
        view3.setImageDrawable(getResources().getDrawable(R.drawable.welcome3));
        viewList.add(view3);
        ImageView view4 = new ImageView(getApplicationContext());
        view4.setImageDrawable(getResources().getDrawable(R.drawable.welcome4));
        viewList.add(view4);
//            View view = new View(this);
//            view.setBackgroundColor(0xff000000| random.nextInt(0x00ffffff));
}

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {

            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));

        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "title";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));

            return viewList.get(position);
        }

    };
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(Config.STATUS_FINISH_ACTIVITY == 1){
//			finish();
//			Config.STATUS_FINISH_ACTIVITY = 0;
//		}
//	}

	 @Override
	protected void onResume() {
		super.onResume();
		if(Config.STATUS_FINISH_ACTIVITY == 1){
			finish();
			Config.STATUS_FINISH_ACTIVITY = 0;
		}
	}
}
