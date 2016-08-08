package com.example.exerciseapp.aty.sliding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.activityrun.ActivityUserInformation;
import com.umeng.message.PushAgent;

public class AtyUserInformationHome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.page_user_information_home);
		
		findViewById(R.id.btnMove).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AtyUserInformationHome.this, ActivityUserInformation.class));
			}
		});
	}
}
