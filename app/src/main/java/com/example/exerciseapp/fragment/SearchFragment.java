package com.example.exerciseapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GroupListAdapter;
import com.example.exerciseapp.aty.team.TeamDetailActivity;
import com.example.exerciseapp.model.GroupList;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.ta.utdid2.android.utils.SystemUtils;

import butterknife.Bind;
import butterknife.OnEditorAction;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/4/7.
 */
public class SearchFragment extends BaseFragment implements GroupListAdapter.OnListClick{

    @Bind(R.id.fragment_search_list)
    ListView listView;
    @Bind(R.id.fragment_search_edit)
    EditText editText;

    GroupListAdapter adapter;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new GroupListAdapter(getActivity(),this);
        listView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search;
    }

    @OnEditorAction(R.id.fragment_search_edit)
    protected boolean onEditorAction(int actionID) {
        if (actionID == EditorInfo.IME_ACTION_SEARCH) {
            Log.e("des", "fsfd");
            onClickSend();
        }
        return true;
    }

    private void onClickSend() {
        loadFind();
    }

    private void loadFind(){
        String find = editText.getText().toString();
        if(TextUtils.isEmpty(find)){
            return;
        }

        adapter.clear();
        RestAdapterUtils.getTeamAPI().getGroupFind(find,new Callback<GroupList>() {
            @Override
            public void success(GroupList allGroup, Response response) {
                if (allGroup != null && allGroup.getResult() == 1) {
                    if (allGroup.getData() != null) {
                        if(allGroup.getData().size() == 0){
                            ScreenUtils.show_msg(getActivity(),"搜索结果为空");
                        }
                        for (int i = 0; i < allGroup.getData().size(); i++) {
                            SingleGroup singleGroup = allGroup.getData().get(i);
                            singleGroup.setType("group_info_normal");
                            allGroup.getData().set(i, singleGroup);
                        }
                        adapter.addItems(allGroup.getData());
                    }else{
                        ScreenUtils.show_msg(getActivity(),"搜索结果为空");
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
    public void click(SingleGroup group) {
        startActivity(TeamDetailActivity.getTeamDetailIntent(getActivity(), group.getId(), group.getType(), null));
        getActivity().finish();
    }
}
