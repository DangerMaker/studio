package com.example.exerciseapp.aty.organzie;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.fragment.SportTabFirstFragment;
import com.example.exerciseapp.fragment.SportTableSecondFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/31.
 */
public class CreateOrganzieActivity extends BindActivity {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.main_viewpager)
    ViewPager viewPager;

    @Bind(R.id.goback)
    ImageView goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_organzie);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        this.viewPager.setOffscreenPageLimit(1);
        this.tabLayout.setupWithViewPager(this.viewPager);

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new CreateZhuzhiFragment();
                default:
                    return new CreateClubFragment();
            }
        }

        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "官方机构";
            }else {
                return "俱乐部";
            }
        }

        public int getCount(){
            return 2;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CreateClubFragment.model.name = "";
        CreateClubFragment.model.addr = "";
        CreateClubFragment.model.owner = "";
        CreateClubFragment.model.phone = "";
        CreateClubFragment.model.qq = "";
        CreateClubFragment.model.subject = "";

        CreateZhuzhiFragment.model.name = "";
        CreateZhuzhiFragment.model.addr = "";
        CreateZhuzhiFragment.model.owner = "";
        CreateZhuzhiFragment.model.phone = "";
        CreateZhuzhiFragment.model.qq = "";
        CreateZhuzhiFragment.model.subject = "";
    }
}
