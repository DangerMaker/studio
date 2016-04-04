package com.example.exerciseapp.aty.team;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;

import com.example.exerciseapp.R;
import com.example.exerciseapp.fragment.TeamFragment;

import butterknife.Bind;

/**
 * Created by lyjq on 2016/4/4.
 */
public class MyTeamActivity extends BackBaseActivity {

    @Bind(R.id.content)
    RelativeLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_fragment);
        setTitleBar("我的团队");
        addFragment(TeamFragment.newInstance(),R.id.content);
    }

    public void addFragment(Fragment newFragment,int layoutId) {
        if(!this.isFinishing()) {
            getSupportFragmentManager().beginTransaction()
                    .add(layoutId, newFragment)
                    .commitAllowingStateLoss();
        }
    }
}
