package com.example.exerciseapp.aty.team;

import android.os.Bundle;

import com.example.exerciseapp.R;
import com.example.exerciseapp.fragment.SearchFragment;
import com.example.exerciseapp.fragment.SearchOrganizFragment;

/**
 * Created by Administrator on 2016/4/17.
 */
public class SearchActivity extends BackBaseActivity {
    String gid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        gid = getIntent().getStringExtra("gid");
        if(gid == null){
            setTitleBar("团队搜索");
            addFragment(SearchFragment.newInstance(),R.id.content);
        }
        else{
            setTitleBar("组织名称搜索");
            addFragment(SearchOrganizFragment.newInstance(gid),R.id.content);
        }

    }

}
