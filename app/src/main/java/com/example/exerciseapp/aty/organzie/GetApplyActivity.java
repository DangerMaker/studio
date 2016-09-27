package com.example.exerciseapp.aty.organzie;

import android.os.Bundle;
import android.widget.ListView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.ApplyListAdapter;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.model.ApplyList;
import com.example.exerciseapp.net.rest.RestAdapterUtils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2016/9/4.
 */
public class GetApplyActivity extends BindActivity {

    @Bind(R.id.list)
    ListView mListView;

    ApplyListAdapter adapter;

    String id;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list);
        id = getIntent().getStringExtra("id");

        RestAdapterUtils.getZhuAPI().getApplyList("1", "get_apply_list", "3.3.0", id, MyApplication.token,"0", new Callback<ApplyList>() {
            @Override
            public void success(ApplyList applyList, Response response) {
                if(applyList != null){
                    adapter = new ApplyListAdapter(GetApplyActivity.this,null);
                    mListView.setAdapter(adapter);
                    adapter.updateItems(applyList.getData().getApply_list());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
