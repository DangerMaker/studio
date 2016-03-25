package com.example.exerciseapp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Cherie_No.47 on 2016/3/12 13:54.
 * Email jascal@163.com
 */
public class BaseActivity extends AppCompatActivity {
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void initWindow() {
         /**
          * 判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
          */
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//透明导航栏
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    
    /**
	 * 获取系统任务栏高度
	 */
    protected int getDimensionMiss() {
		Resources resources = BaseActivity.this.getResources();
		int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
		return resources.getDimensionPixelSize(resourceId);
	}

    public void addFragment(Fragment newFragment,int layoutId) {
        if(!this.isFinishing()) {
            getSupportFragmentManager().beginTransaction()
                    .add(layoutId, newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

}
