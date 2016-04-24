package com.example.exerciseapp.aty.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GameSubListAdapter;
import com.example.exerciseapp.model.GameList;
import com.example.exerciseapp.model.GameModel;
import com.example.exerciseapp.net.rest.RestAdapterUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * User: lyjq(1752095474)
 * Date: 2016-04-22
 */
public class GameSelectActivity extends BackBaseActivity implements GameSubListAdapter.OnListClick {
    @Bind(R.id.listView1)
    ListView mListView1;
    @Bind(R.id.listView2)
    ListView mListView2;
    @Bind(R.id.listView3)
    ListView mListView3;

    GameSubListAdapter adapter1;
    GameSubListAdapter adapter2;
    GameSubListAdapter adapter3;

    String gid;
    String is_group;
    List<GameModel> list1 = new ArrayList<>();
    List<GameModel> list2 = new ArrayList<>();
    List<GameModel> list3 = new ArrayList<>();

    String temp1;
    String temp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_region);
        setTitleBar("项目选择");
        gid = getIntent().getStringExtra("gid");
        is_group = getIntent().getStringExtra("is_group");

        adapter1 = new GameSubListAdapter(this, this);
        adapter2 = new GameSubListAdapter(this, this);
        adapter3 = new GameSubListAdapter(this, this);
        mListView1.setAdapter(adapter1);
        mListView2.setAdapter(adapter2);
        mListView3.setAdapter(adapter3);
        loadData1();
        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                adapter2.clear();
                adapter3.clear();

                Iterator iterator = list1.iterator();
                while (iterator.hasNext()) {
                    GameModel gameModel = (GameModel) iterator.next();
                    gameModel.setFlag(false);
                }
                GameModel model = list1.get(i);
                temp1 = model.getName();
                model.setFlag(true);
                adapter1.updateItems(list1);
                if (model.getIs_event() == 0) {
                    loadData2(model.getId() + "");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("game_detail_id", model.getId());
                    intent.putExtra("game_detail_name",model.getName());
                    intent.putExtra("game_fee", model.getEpayfee());
                    setResult(21, intent);
                    finish();
                }
            }
        });

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter3.clear();
                Iterator iterator = list2.iterator();
                while (iterator.hasNext()) {
                    GameModel gameModel = (GameModel) iterator.next();
                    gameModel.setFlag(false);
                }
                GameModel model = list2.get(i);
                temp2 = model.getName();
                model.setFlag(true);
                adapter2.updateItems(list2);
                if (model.getIs_event() == 0) {
                    loadData3(model.getId() + "");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("game_detail_id", model.getId());
                    intent.putExtra("game_detail_name",temp1 + "-" +model.getName());
                    intent.putExtra("game_fee", model.getEpayfee());
                    setResult(21, intent);
                    finish();
                }
            }
        });

        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameModel model = list3.get(i);
                Intent intent = new Intent();
                intent.putExtra("game_detail_id", model.getId());
                intent.putExtra("game_detail_name",temp1 + "-" + temp2 +"-" + model.getName());
                intent.putExtra("game_fee", model.getEpayfee());
                setResult(21, intent);
                finish();

            }
        });
    }

    public void loadData1() {
        list1.clear();
        RestAdapterUtils.getTeamAPI().getFirstLevel(gid, is_group, new Callback<GameList>() {
            @Override
            public void success(GameList gameList, Response response) {
                if (gameList != null) {
                    list1 = gameList.getData();
                    adapter1.updateItems(list1);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void loadData2(String subId) {
        list2.clear();
        RestAdapterUtils.getTeamAPI().getSecondLevel(subId, is_group, new Callback<GameList>() {
            @Override
            public void success(GameList gameList, Response response) {
                if (gameList != null) {
                    list2 = gameList.getData();
                    adapter2.updateItems(list2);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void loadData3(String subId) {
        list3.clear();
        RestAdapterUtils.getTeamAPI().getThirdLevel(subId, is_group, new Callback<GameList>() {
            @Override
            public void success(GameList gameList, Response response) {
                if (gameList != null) {
                    list3 = gameList.getData();
                    adapter3.updateItems(list3);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @Override
    public void click(GameModel group) {

    }
}
