package com.example.exerciseapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.aty.activityrun.ActivityBindphone;
import com.example.exerciseapp.aty.activityrun.ActivityScoreManager;
import com.example.exerciseapp.aty.login.ActivityWeb;
import com.example.exerciseapp.aty.login.AtyAdvertisement;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.fragment.SportTabFirstFragment;
import com.example.exerciseapp.fragment.SportTabFiveFragment;
import com.example.exerciseapp.fragment.SportTabFourFragment;
import com.example.exerciseapp.fragment.SportTabThirdFragment;
import com.example.exerciseapp.fragment.SportTableSecondFragment;
import com.example.exerciseapp.myutils.HintDialog;
import com.example.exerciseapp.utils.LocationPro;
import com.example.exerciseapp.utils.SharedPreferencesHelper;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * 主页面
 *
 * @author Sonchcng
 */
@SuppressLint("NewApi")
public class TabMainActivity extends FragmentActivity implements OnClickListener, SportTabFiveFragment.OnChangeHWCallBack {


    private long exitTime = 0;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private int index = 2; //当前 fragment
    public static Activity instance;
    private RequestQueue mRequestQueue;
    private static IWXAPI api;
    //五个tab

    private ArrayList<Fragment> fragmentList;
    private ImageView image;
    public ImageView goback;

    private LinearLayout firstPage;
    private TextView firstTv;
    private ImageView firstIv;

    private LinearLayout secondPage;
    private TextView secondTv;
    private ImageView secondIv;

    private LinearLayout thirdPage;
    private TextView thirdTv;
    private ImageView thirdIv;

    private LinearLayout fourPage;
    private TextView fourTv;
    private ImageView fourIv;

    private LinearLayout fivePage;
    private TextView fiveTv;
    private ImageView fiveIv;
    //五个fragment
    private SportTabFirstFragment firstFragment = null;
    private SportTableSecondFragment secondFragment = null;
    private SportTabThirdFragment thirdFragment = null;
    private SportTabFourFragment fourFragment = null;
    private SportTabFiveFragment fiveFragment = null;
    public int bind_layer_show;


    RelativeLayout adLayout;
    ImageView adsImage;
    TextView adsText;

    /**
     * from other activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmain);
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        instance = this;
        mRequestQueue = Volley.newRequestQueue(this);
        PushAgent.getInstance(getApplicationContext()).onAppStart();
        Intent intent = getIntent();
        try {
            if (null != intent.getStringExtra("bind_layer_show")) {
                bind_layer_show = Integer.parseInt(intent.getStringExtra("bind_layer_show"));
                if (1 == bind_layer_show) {
                    myshowDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
        initData();

        boolean isMessagePush = false;// 不开启就设置为false;
        if (isMessagePush) {
            startService(new Intent(this, com.example.exerciseapp.service.MessageService.class));
        }

        if(intent.getBooleanExtra("showAd",false)){
            showAd();
        }
    }

    public void myshowDialog() {
        LayoutInflater inflater = LayoutInflater.from(TabMainActivity.this);
        final View dialogView = inflater.inflate(R.layout.dialog_show_binddia, null);
        final HintDialog dialog = new HintDialog(TabMainActivity.this, dialogView, R.style.MyDialogStyle);
        dialog.setCancelable(false);
        final TextView gotobind = (TextView) dialogView.findViewById(R.id.gotobind);
        final TextView nobindmore = (TextView) dialogView.findViewById(R.id.nobindmore);
        final TextView nexttime = (TextView) dialogView.findViewById(R.id.nexttime);

        gotobind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TabMainActivity.this, ActivityBindphone.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        nobindmore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                donobindmore();
            }
        });
        nexttime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void donobindmore() {
        String url = "http://101.200.214.68/py/bind?action=refuse_show_bind_layer" +
                "&version=3.0" +
                "&uid=" + MyApplication.getInstance().getUid() +
                "&token=" + MyApplication.getInstance().getToken();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            Toast.makeText(TabMainActivity.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }) {
        };
        mRequestQueue.add(stringRequest);
    }

    public void initwindow() {

        Window window = TabMainActivity.this.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbarbg));
        ViewGroup mContentView = (ViewGroup) TabMainActivity.this.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    public void initView() {
        adsText = (TextView) findViewById(R.id.count);
        adsImage = (ImageView) findViewById(R.id.ad);
        adLayout = (RelativeLayout) findViewById(R.id.ad_layout);
        adLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!SharedPreferencesHelper.getInstance(TabMainActivity.this).getValue("html_url").equals("")){
                    String url = SharedPreferencesHelper.getInstance(TabMainActivity.this).getValue("html_url");
                    Intent intent = new Intent(TabMainActivity.this, ActivityScoreManager.class);
                    intent.putExtra("navColor", "#0acbc1");    //配置导航条的背景颜色，请用#ffffff长格式。
                    intent.putExtra("titleColor", "#ffffff");    //配置导
                    intent.putExtra("url",url);
                    startActivity(intent);
                }
            }
        });
        // TODO Auto-generated method stub
        frameLayout = (FrameLayout) findViewById(R.id.mainFrame);
//        mPager=(ViewPager) findViewById(R.id.viewpager);
//        fragmentManager = getFragmentManager();
        fragmentManager = getSupportFragmentManager();
        //初始化tab
        firstPage = (LinearLayout) findViewById(R.id.firstPage);
        firstTv = (TextView) findViewById(R.id.firstTv);
        firstIv = (ImageView) findViewById(R.id.firstIv);

        secondPage = (LinearLayout) findViewById(R.id.secondPage);
        secondTv = (TextView) findViewById(R.id.secondTv);
        secondIv = (ImageView) findViewById(R.id.secondIv);

        thirdPage = (LinearLayout) findViewById(R.id.thirdPage);
        thirdTv = (TextView) findViewById(R.id.thirdTv);
        thirdIv = (ImageView) findViewById(R.id.thirdIv);

        fourPage = (LinearLayout) findViewById(R.id.fourPage);
        fourTv = (TextView) findViewById(R.id.fourTv);
        fourIv = (ImageView) findViewById(R.id.fourIv);
        fivePage = (LinearLayout) findViewById(R.id.fivePage);
        fiveTv = (TextView) findViewById(R.id.fiveTv);
        fiveIv = (ImageView) findViewById(R.id.fiveIv);
        //添加监听
        firstPage.setOnClickListener(this);
        secondPage.setOnClickListener(this);
        thirdPage.setOnClickListener(this);
        fourPage.setOnClickListener(this);
        fivePage.setOnClickListener(this);
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
    }

    public void initData() {
        // TODO Auto-generated method stub

        initFragment();

    }

    private void initFragment() {
        //初始化五个fragment
        thirdFragment = new SportTabThirdFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.mainFrame, thirdFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        thirdIv.setImageResource(R.drawable.tab3_pressed);
        thirdTv.setTextColor(getResources().getColor(R.color.tab_blue));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        initColor();
        changeFragment(v);
    }

    /**
     * 切换fragment
     *
     * @param v
     */
    private void changeFragment(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragments(transaction);
        if (v == firstPage && index != 0) {
            if (firstFragment == null) {
                firstFragment = new SportTabFirstFragment();
                transaction.add(R.id.mainFrame, firstFragment);
                transaction.addToBackStack(null);
            } else
                transaction.show(firstFragment);
            index = 0;
            transaction.commit();

        } else if (v == secondPage && index != 1) {
            if (secondFragment == null) {
                secondFragment = new SportTableSecondFragment();
                transaction.add(R.id.mainFrame, secondFragment);
                transaction.addToBackStack(null);

            } else
                transaction.show(secondFragment);

            index = 1;
            transaction.commit();

        } else if (v == thirdPage && index != 2) {
            if (thirdFragment == null) {
                thirdFragment = new SportTabThirdFragment();
                transaction.add(R.id.mainFrame, thirdFragment);
                transaction.addToBackStack(null);
            } else
                transaction.show(thirdFragment);

            index = 2;
            transaction.commit();

        } else if (v == fourPage && index != 3) {
            if ("0".equals(MyApplication.getInstance().getUid())) {
                hintLogin();
                index = 3;
            } else {
                if (fourFragment == null) {
                    fourFragment = new SportTabFourFragment();

                    transaction.add(R.id.mainFrame, fourFragment);
                    transaction.addToBackStack(null);
                } else
                    transaction.show(fourFragment);

                index = 3;
                transaction.commit();
            }


        } else if (v == fivePage && index != 4) {
            if ("0".equals(MyApplication.getInstance().getUid())) {
                hintLogin();
                index = 4;
            } else {
                if (fiveFragment == null) {
                    fiveFragment = new SportTabFiveFragment();
                    fiveFragment.setOnChangeHWCallBack(this);
                    transaction.add(R.id.mainFrame, fiveFragment);
                    transaction.addToBackStack(null);
                } else
                    transaction.show(fiveFragment);
                index = 4;
                transaction.commit();

            }

        }
        changeColor(index);

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (firstFragment != null) {
            transaction.hide(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);
        }
        if (thirdFragment != null) {
            transaction.hide(thirdFragment);
        }
        if (fourFragment != null) {
            transaction.hide(fourFragment);
        }
        if (fiveFragment != null) {
            transaction.hide(fiveFragment);
        }
    }

    /**
     * 切换fragment时，切换相应tab颜色
     *
     * @param i
     */
    private void changeColor(int i) {
        if (i == 0) {
            //修改颜色
            firstIv.setImageResource(R.drawable.tab1_pressed);
            firstTv.setTextColor(getResources().getColor(R.color.tab_blue));
        } else if (i == 1) {
            //修改颜色
            secondIv.setImageResource(R.drawable.tab2_pressed);
            secondTv.setTextColor(getResources().getColor(R.color.tab_blue));
        } else if (i == 2) {
            //修改颜色
            thirdIv.setImageResource(R.drawable.tab3_pressed);
            thirdTv.setTextColor(getResources().getColor(R.color.tab_blue));
        } else if (i == 3) {
            //修改颜色
            fourIv.setImageResource(R.drawable.tab4_pressed);
            fourTv.setTextColor(getResources().getColor(R.color.tab_blue));
        } else if (i == 4) {
            //修改颜色
            fiveIv.setImageResource(R.drawable.tab5_pressed);
            fiveTv.setTextColor(getResources().getColor(R.color.tab_blue));
        }
    }

    /*
     * 初始化tab的图标和颜色
     */
    private void initColor() {
        // TODO Auto-generated method stub
        firstIv.setImageResource(R.drawable.tab_one);
        firstTv.setTextColor(getResources().getColor(R.color.light_gray));

        secondIv.setImageResource(R.drawable.tab_two);
        secondTv.setTextColor(getResources().getColor(R.color.light_gray));

        thirdIv.setImageResource(R.drawable.tab_three);
        thirdTv.setTextColor(getResources().getColor(R.color.light_gray));

        fourIv.setImageResource(R.drawable.tab_four);
        fourTv.setTextColor(getResources().getColor(R.color.light_gray));

        fiveIv.setImageResource(R.drawable.tab_five);
        fiveTv.setTextColor(getResources().getColor(R.color.light_gray));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {

            return false;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            if (AtyWelcome.instance != null) {
                AtyWelcome.instance.finish();
            }
            finish();
            System.exit(0);
        }
    }

    public void hintLogin() {

        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.dialog_show_gotologin, null);
        final HintDialog dialog = new HintDialog(this, dialogView,
                R.style.MyDialogStyle);
        dialog.setCancelable(true);
        TextView tvHintDialogMessage = (TextView) dialogView
                .findViewById(R.id.tvHintDialogMessage);
        TextView btnCancel = (TextView) dialogView
                .findViewById(R.id.btnHintDialogNo);
        TextView btnToLogin = (TextView) dialogView
                .findViewById(R.id.btnHintDialogYes);
        tvHintDialogMessage.setText(this.getString(
                R.string.hintLoginMessage));
        btnCancel.setText(this.getString(R.string.cancel));
        btnToLogin.setText(this.getString(R.string.toLogin));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(TabMainActivity.this, AtyWelcome.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    @Override
    public void onChangeHWCallBack() {

    }

    private static final int INTERVAL_SECOND = 3000;
    private static final int SHOWAD_INTERVL_SECOND = 10 * 60 * 1000;
    private CountDownTimer timer = new CountDownTimer(INTERVAL_SECOND, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            adsText.setText((millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            adsText.setText("0s");
        }
    };

    public void showAd() {
        if (getLocal()) {
            handler.postDelayed(finishLoad, INTERVAL_SECOND);
            adLayout.setVisibility(View.VISIBLE);
            timer.start();
        } else {
            handler.post(finishLoad);
        }
    }

    Handler handler = new Handler();

    private Runnable finishLoad = new Runnable() {
        @Override
        public void run() {
           adLayout.setVisibility(View.GONE);
        }

    };

    String pic_url;
    String html_url;

    private boolean getLocal() {
        try {
            pic_url = SharedPreferencesHelper.getInstance(this).getValue("pic_url");
            html_url = SharedPreferencesHelper.getInstance(this).getValue("html_url");
            if (pic_url.equals("") || html_url.equals("")) {
                return false;
            }
            File file = new File(this.getCacheDir(), "splash.png");
            FileInputStream fis = new FileInputStream(file.getPath());
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            adsImage.setImageBitmap(bitmap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
