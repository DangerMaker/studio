package com.example.exerciseapp.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.exerciseapp.R;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.Bind;

/**
 * Created by lyjq on 2016/3/24.
 */
public class TeamFragment extends BaseFragment{

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;

    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("roomID", roomID);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_team;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
