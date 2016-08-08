package com.example.exerciseapp.aty.team;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.example.exerciseapp.fragment.TeamFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lyjq on 2016/4/4.
 */
public class MyTeamActivity extends BackBaseActivity {

    @Bind(R.id.content)
    RelativeLayout content;
    @Bind(R.id.toolbar_text_right)
    TextView mToolbarRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_fragment);
        setTitleBar("我的团队");
        mToolbarRight.setVisibility(View.VISIBLE);
        mToolbarRight.setText("创建团队");
        addFragment(TeamFragment.newInstance(), R.id.content);
    }

    public void addFragment(Fragment newFragment, int layoutId) {
        if (!this.isFinishing()) {
            getSupportFragmentManager().beginTransaction()
                    .add(layoutId, newFragment)
                    .commitAllowingStateLoss();
        }
    }

    @OnClick(R.id.toolbar_text_right)
    public void createTeam() {
        startActivity(CreateMyTeamActivity.getCreateMyTeamIntent(this));
    }
}
