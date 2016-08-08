package com.example.exerciseapp.aty.sliding;
/**
 * 场馆预约页面
 */

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

public class AtyVenuesReservation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_venues_reservation);
		
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.actionbar_start_running);
		TextView title = (TextView) getActionBar().getCustomView().findViewById(R.id.tvPageTitleOfAll);
		title.setText("场馆预约");
	}
}
