package com.example.exerciseapp.aty.sliding;

/**
 * 我的关注页面
 */
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

public class AtyMyFocus extends BaseActivity {
	private Toolbar toolbar;
	private TextView pageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_my_focus);
		setTitleBar();

	}

	private void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("我的关注");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyMyFocus.this.finish();
			}
		});
	}
}
