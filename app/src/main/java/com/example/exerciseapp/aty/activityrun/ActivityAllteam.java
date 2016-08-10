package com.example.exerciseapp.aty.activityrun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GroupListAdapter;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.aty.team.CreateMyTeamActivity;
import com.example.exerciseapp.aty.team.SearchActivity;
import com.example.exerciseapp.aty.team.TeamDetailActivity;
import com.example.exerciseapp.model.GroupList;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.myutils.HintDialog;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.utils.UiUtils;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by lyjq on 2016/3/24.
 */
public class ActivityAllteam extends Activity implements GroupListAdapter.OnListClick {

    PullToRefreshListView listView;
    String uid;
    GroupListAdapter adapter;
    public static int page = 1;
    LinearLayout searchRoot;
    PopupWindow popupWindow;
    GroupListAdapter adapter1;
    View convertView;
    EditText editText1;
    ListView listView1;
    ImageView goback;
    TextView creatteam;

    public static ActivityAllteam newInstance() {
        ActivityAllteam activityAllteam = new ActivityAllteam();
        return activityAllteam;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_allteams);
        goback = (ImageView) findViewById(R.id.goback);
        creatteam = (TextView) findViewById(R.id.allteamcreat);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        creatteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(MyApplication.getInstance().getUid())) {
                    hintLogin();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(ActivityAllteam.this, CreateMyTeamActivity.class);
                    startActivity(intent);
                }
            }
        });
        searchRoot = (LinearLayout) findViewById(R.id.team_search);
        searchRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityAllteam.this, SearchActivity.class));
            }
        });
        adapter = new GroupListAdapter(ActivityAllteam.this, this);
        listView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_team);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(ActivityAllteam.this.getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                load();

            }

        });


        listView.setAdapter(adapter);
        uid = MyApplication.getInstance().getUid();
        if (uid == null || uid.equals("")) {
            ScreenUtils.show_msg(ActivityAllteam.this, "没有用户id,请登录");
        }
    }

    public void hintLogin() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.dialog_show_gotologin, null);
        final HintDialog dialog = new HintDialog(this, dialogView,
                R.style.MyDialogStyle);
        dialog.setCancelable(true);
        TextView tvHintDialogMessage = (TextView) dialogView
                .findViewById(R.id.tvHintDialogMessage);
        TextView btnCancel = (TextView) dialogView
                .findViewById(R.id.btnHintDialogNo);
        TextView btnToLogin = (TextView) dialogView
                .findViewById(R.id.btnHintDialogYes);
        tvHintDialogMessage.setText(this.getString(
                R.string.hintLoginMessage));
        btnCancel.setText(this.getString(R.string.cancel));
        btnToLogin.setText(this.getString(R.string.toLogin));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(ActivityAllteam.this, AtyWelcome.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        RestAdapterUtils.getTeamAPI().getGroupList("get_group_list", new Callback<GroupList>() {
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
                ScreenUtils.show_msg(ActivityAllteam.this, error.getMessage());
                if (listView != null) listView.onRefreshComplete();
            }
        });
    }

    private void loadFind() {
        String find = editText1.getText().toString();
        if (TextUtils.isEmpty(find)) {
            return;
        }

        adapter1.clear();
        RestAdapterUtils.getTeamAPI().getGroupFind(find, new Callback<GroupList>() {
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
                ScreenUtils.show_msg(ActivityAllteam.this, error.getMessage());
            }
        });
    }


    @Override
    public void click(SingleGroup group) {
        startActivity(TeamDetailActivity.getTeamDetailIntent(ActivityAllteam.this, group.getId(), group.getType(), null));
    }


    private void initView() {
        convertView = LayoutInflater.from(ActivityAllteam.this).inflate(R.layout.fragment_search, null);
        editText1 = (EditText) convertView.findViewById(R.id.fragment_search_edit);
        editText1.getText();
        listView1 = (ListView) convertView.findViewById(R.id.fragment_search_list);
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
        adapter1 = new GroupListAdapter(ActivityAllteam.this, this);
        listView1.setAdapter(adapter1);
        popupWindow = new PopupWindow(convertView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(searchRoot, Gravity.TOP, 0, UiUtils.dip2px(ActivityAllteam.this, 68));

        InputMethodManager inputManager =
                (InputMethodManager) ActivityAllteam.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void onClickSend() {
        loadFind();
    }


}
