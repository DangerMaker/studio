package com.example.exerciseapp.aty.team;

import android.os.Bundle;

import com.example.exerciseapp.R;
import com.example.exerciseapp.fragment.SearchFragment;

/**
 * Created by Administrator on 2016/4/17.
 */
public class SearchActivity extends BackBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitleBar("团队搜索");

        addFragment(SearchFragment.newInstance(),R.id.content);
    }

}
