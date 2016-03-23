package com.example.exerciseapp.aty.sliding;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;

public class AtyGradeDetail extends Activity{

	private WebView webView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_grade_detail);
//		initActionBar();
		setTitleBar(this);
		webView = (WebView) findViewById(R.id.wvGradeDetail);
		webView.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		TextView text = (TextView) findViewById(R.id.tvTitle);
		if(getIntent().getStringExtra("agreement")!=null){
			if(!getIntent().getStringExtra("agreement").equals("")){
				text.setText("赛事规程");
        		webView.loadUrl(getIntent().getStringExtra("agreement"));
			}
        }else{
        	text.setText("成绩详情");
        	webView.loadUrl(getIntent().getStringExtra("url"));
        }
	}
//	private void initActionBar(){
//		ActionBar actionBar = getActionBar();  
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.actionbar_start_running, null);
//        TextView text = (TextView) view.findViewById(R.id.tvPageTitleOfAll);
//        if(getIntent().getStringExtra("agreement")!=null||!getIntent().getStringExtra("agreement").equals("")){
//        	text.setText("赛事规程");
//        	webView.loadUrl(getIntent().getStringExtra("agreement"));
//        }else{
//        	text.setText("成绩详情");
//        	webView.loadUrl(getIntent().getStringExtra("url"));
//        }
//        view.findViewById(R.id.ivBackBtnStartRunning).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//        actionBar.setCustomView(view);
//	}
	
	@SuppressLint("InlinedApi") public static void setTitleBar(final Activity activity){
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
        //透明导航栏  
//		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 
		activity.findViewById(R.id.ivBackBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activity.finish();
			}
		});
        
        int currentapiVersion=android.os.Build.VERSION.SDK_INT;
        if(currentapiVersion<20){
        	activity.findViewById(R.id.titlebar).setBackgroundResource(R.drawable.actionbarbg);
        	activity.findViewById(R.id.titlebar).setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }else{
        	activity.findViewById(R.id.titlebar).getLayoutParams().height = Config.getDimensionMiss(activity.getApplicationContext())+80;
        }
	}
}
