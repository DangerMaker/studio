package com.example.exerciseapp.aty.sliding;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;

public class AtyMessageDetail extends Activity {

	
	private JSONObject jsonObj;
	private TextView tvContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_message_detail);
		tvContent = (TextView) findViewById(R.id.tvContent);
		try {
			jsonObj = new JSONObject(getIntent().getStringExtra("information"));
			tvContent.setText(jsonObj.getString(Config.KEY_CONTENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.actionbar_start_running);
		TextView title = (TextView) getActionBar()
				.getCustomView().findViewById(R.id.tvPageTitleOfAll);
		title.setText("消息详情");
		getActionBar().getCustomView().findViewById(R.id.ivBackBtnStartRunning).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
					startActivity(new Intent(AtyMessageDetail.this,AtyMessage.class));
				}
			});
	}
}
