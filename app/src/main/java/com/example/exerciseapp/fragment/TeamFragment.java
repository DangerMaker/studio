package com.example.exerciseapp.fragment;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GroupListAdapter;
import com.example.exerciseapp.aty.team.TeamDetailActivity;
import com.example.exerciseapp.model.AllGroup;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/24.
 */
public class TeamFragment extends BaseFragment implements GroupListAdapter.OnListClick{

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;
    String uid;
    GroupListAdapter adapter;
    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new GroupListAdapter(getActivity(),this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                load();
            }

        });


        listView.setAdapter(adapter);
        uid = ((MyApplication) getActivity().getApplication()).getUid();
        if (uid == null || uid.equals("")) {
            ScreenUtils.show_msg(getActivity(), "没有用户id,请登录");
            return;
        }
        load();
    }

    private void load() {

        adapter.clear();
        RestAdapterUtils.getTeamAPI().getAllGroup(uid, "get_all_group", new Callback<AllGroup>() {
            @Override
            public void success(AllGroup allGroup, Response response) {
                if(allGroup != null && allGroup.getResult() == 1){
                    adapter.addItems(allGroup.getData().getGroup_info_return());
                    adapter.addItems(allGroup.getData().getLeader_group_info_return());
                }
                if(listView != null) listView.onRefreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(getActivity(),error.getMessage());
                if(listView != null) listView.onRefreshComplete();
            }
        });
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_team;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void click(SingleGroup group) {
        startActivity(TeamDetailActivity.getTeamDetailIntent(getActivity(),group.getId()));
    }
}
