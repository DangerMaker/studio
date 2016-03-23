package com.example.exerciseapp.aty.sliding;
/**
 * 积分管理页面
 */
import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AtyScoresManager extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_scores_manager);
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.actionbar_start_running);
		TextView title = (TextView) getActionBar().getCustomView().findViewById(R.id.tvPageTitleOfAll);
		title.setText("积分管理");
	}
}
