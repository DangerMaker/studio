package com.example.exerciseapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.OrganizListAdapter;
import com.example.exerciseapp.model.OrganizeList;
import com.example.exerciseapp.model.OrganizeName;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/4/7.
 */
public class SearchOrganizFragment extends BaseFragment implements OrganizListAdapter.OnListClick {

    @Bind(R.id.fragment_search_list)
    ListView listView;
    @Bind(R.id.fragment_search_edit)
    EditText editText;

    OrganizListAdapter adapter;
    String gid;

    public static SearchOrganizFragment newInstance(String gid) {
        SearchOrganizFragment fragment = new SearchOrganizFragment();
        fragment.gid = gid;
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new OrganizListAdapter(getActivity(), this);
        listView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_search;
    }

    @OnEditorAction(R.id.fragment_search_edit)
    protected boolean onEditorAction(int actionID) {
        if (actionID == EditorInfo.IME_ACTION_SEARCH) {
            loadSelf();
        }
        return true;
    }


    @OnTextChanged(value = R.id.fragment_search_edit, callback = OnTextChanged.Callback.BEFORE_TEXT_CHANGED)
    void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @OnTextChanged(value = R.id.fragment_search_edit, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @OnTextChanged(value = R.id.fragment_search_edit, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        onClickSend();
    }

    private void onClickSend() {
        loadFind();
    }

    private void loadSelf() {
        String text = editText.getText().toString();
        if (text != null && !text.trim().equals("")) {
            Intent intent = new Intent();
            intent.putExtra("name", text);
            getActivity().setResult(20, intent);
            getActivity().finish();
        }
    }

    private void loadFind() {
        String find = editText.getText().toString();
        if (TextUtils.isEmpty(find)) {
            return;
        }

        adapter.clear();
        RestAdapterUtils.getTeamAPI().getOrganizeName(gid, find, new Callback<OrganizeList>() {
            @Override
            public void success(OrganizeList organizeList, Response response) {
                if (organizeList != null && organizeList.getResult() == 1) {
                    if (organizeList.getData() != null) {
                        if (organizeList.getData().size() == 0) {

                        } else {
                            adapter.addItems(organizeList.getData());
                        }

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
    public void click(OrganizeName group) {
        Intent intent = new Intent();
        intent.putExtra("name", group.getName());
        getActivity().setResult(20, intent);
        getActivity().finish();
    }
}
