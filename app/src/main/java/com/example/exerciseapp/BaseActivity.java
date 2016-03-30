package com.example.exerciseapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;

import com.example.exerciseapp.utils.ScreenUtils;

import butterknife.ButterKnife;

/**
 * Created by Cherie_No.47 on 2016/3/12 13:54.
 * Email jascal@163.com
 */
public class BaseActivity extends AppCompatActivity {
    protected Context context;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private ProgressDialog dialog;
    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("正在加载，请稍等");
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    public void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public String getUid(){
        uid = ((MyApplication) getApplication()).getUid();
        return uid;
    }

    public boolean isUidAvailable(){
        uid = ((MyApplication) getApplication()).getUid();
        if (uid == null || uid.equals("")) {
            ScreenUtils.show_msg(this, "没有用户id,请登录");
            return false;
        }else{
            return true;
        }
    }
}
