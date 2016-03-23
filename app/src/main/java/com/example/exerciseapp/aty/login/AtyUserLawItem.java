package com.example.exerciseapp.aty.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;

public class AtyUserLawItem extends Activity {

	private WebView webView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  
        //透明导航栏  
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); 
		setContentView(R.layout.aty_user_law_item);
		initActionBar();
		webView = (WebView) findViewById(R.id.wvUserLawItem);
		webView.getSettings().setSupportZoom(true); 
		// 设置出现缩放工具 
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.loadUrl("http://101.200.214.68/index.php/Api/System/usingClause");
	}
	private void initActionBar(){
		int currentapiVersion=android.os.Build.VERSION.SDK_INT;
        if(currentapiVersion<20){
        	findViewById(R.id.titlebar).setBackgroundResource(R.drawable.actionbarbg);
        	findViewById(R.id.titlebar).setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }else{
        	findViewById(R.id.titlebar).getLayoutParams().height = Config.getDimensionMiss(getApplicationContext())+80;
        }
//		ActionBar actionBar = getActionBar();  
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.actionbar_start_running, null);
//        TextView text = (TextView) view.findViewById(R.id.tvPageTitleOfAll);
//        text.setText("使用条款");
        findViewById(R.id.ivBackBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        TextView title = (TextView)findViewById(R.id.tvTitle);
        title.setText("使用条款");
        
	}
}
