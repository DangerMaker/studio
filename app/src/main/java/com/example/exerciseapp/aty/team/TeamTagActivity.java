package com.example.exerciseapp.aty.team;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.TagGridAdapter;
import com.example.exerciseapp.model.AllTag;
import com.example.exerciseapp.model.TagModel;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/4/5.
 */
public class TeamTagActivity extends BackBaseActivity {

    @Bind(R.id.team_tag_gridview)
    GridView gridView;
    @Bind(R.id.team_tag_submit)
    Button button;
    private TagGridAdapter preGridAdapter;
    List<TagModel> list = new ArrayList<TagModel>();

    public static Intent getTeamTagIntent(Context context) {
        Intent intent = new Intent(context, TeamTagActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_tag);
        setTitleBar("标签选择");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("tagName", list.get(position).getName());
                data.putExtra("tagId", list.get(position).getId() + "");
                setResult(10, data);
                finish();
            }
        });
        load();
    }

    private void load() {
        RestAdapterUtils.getTeamAPI().getTag(new Callback<AllTag>() {
            @Override
            public void success(AllTag allTag, Response response) {
                if (allTag != null && allTag.getResult() == 1) {
                    list = allTag.getList();
                    preGridAdapter = new TagGridAdapter(TeamTagActivity.this, list);
                    gridView.setAdapter(preGridAdapter);
                } else {
                    ScreenUtils.show_msg(TeamTagActivity.this, allTag.getDesc());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                ScreenUtils.show_msg(TeamTagActivity.this, "获取失败");
            }
        });
    }


}
