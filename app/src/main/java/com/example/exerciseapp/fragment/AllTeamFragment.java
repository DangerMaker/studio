package com.example.exerciseapp.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GroupListAdapter;
import com.example.exerciseapp.aty.team.TeamDetailActivity;
import com.example.exerciseapp.model.AllGroup;
import com.example.exerciseapp.model.GroupList;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.utils.UiUtils;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/24.
 */
public class AllTeamFragment extends BaseFragment implements GroupListAdapter.OnListClick {

    @Bind(R.id.pull_to_refresh_team)
    PullToRefreshListView listView;
    String uid;
    GroupListAdapter adapter;

    public static AllTeamFragment newInstance() {
        AllTeamFragment fragment = new AllTeamFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new GroupListAdapter(getActivity(), this);
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

        initView();
    }


    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        adapter.clear();
        RestAdapterUtils.getTeamAPI().getGroupList(new Callback<GroupList>() {
            @Override
            public void success(GroupList allGroup, Response response) {
                if (allGroup != null && allGroup.getResult() == 1) {
                    if (allGroup.getData() != null) {
                        for (int i = 0; i < allGroup.getData().size(); i++) {
                            SingleGroup singleGroup = allGroup.getData().get(i);
                            singleGroup.setType("group_info_normal");
                            allGroup.getData().set(i, singleGroup);
                        }
                        adapter.addItems(allGroup.getData());
                    }

                }
                if (listView != null) listView.onRefreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(getActivity(), error.getMessage());
                if (listView != null) listView.onRefreshComplete();
            }
        });
    }

    private void loadFind(){
        String find = editText1.getText().toString();
        if(TextUtils.isEmpty(find)){
            return;
        }

        adapter1.clear();
        RestAdapterUtils.getTeamAPI().getGroupFind(find,new Callback<GroupList>() {
            @Override
            public void success(GroupList allGroup, Response response) {
                if (allGroup != null && allGroup.getResult() == 1) {
                    if (allGroup.getData() != null) {
                        for (int i = 0; i < allGroup.getData().size(); i++) {
                            SingleGroup singleGroup = allGroup.getData().get(i);
                            singleGroup.setType("group_info_normal");
                            allGroup.getData().set(i, singleGroup);
                        }
                        adapter1.addItems(allGroup.getData());
                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(getActivity(), error.getMessage());
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
        startActivity(TeamDetailActivity.getTeamDetailIntent(getActivity(), group.getId(), group.getType(), null));
    }

    @Bind(R.id.team_search)
    LinearLayout searchRoot;

    @OnClick(R.id.team_search)
    public void search() {
        showWindow();
    }

    PopupWindow popupWindow;
    GroupListAdapter adapter1;
    View convertView;
    EditText editText1;
    ListView listView1;

    private void initView() {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search, null);
        editText1 = (EditText)convertView.findViewById(R.id.fragment_search_edit);
        listView1 = (ListView)convertView.findViewById(R.id.fragment_search_list);
        editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClickSend();
                }
                return true;
            }
        });
    }



    private void showWindow() {
        adapter1 = new GroupListAdapter(getActivity(), this);
        listView1.setAdapter(adapter1);
        popupWindow = new PopupWindow(convertView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(searchRoot, Gravity.TOP, 0, UiUtils.dip2px(getActivity(), 68));

        InputMethodManager inputManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void onClickSend() {
        loadFind();
    }


}
