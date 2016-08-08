package com.example.exerciseapp.aty.activityrun;
/**
 * 我的俱乐部和协会
 */

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.MyClubAndAssocListAdapter;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ActivityMyClubAndAssociation extends BaseActivity {

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    private TextView tvAssociationTitlePageClub;
    private TextView tvClubTitlePageClub;

    private ImageView cursorOnePageClub;
    private ImageView cursorTwoPageClub;

    private ListView listClub;
    private MyClubAndAssocListAdapter clubAdapter;
    private LinkedList<JSONObject> clubList = new LinkedList<JSONObject>();
    private RequestQueue mRequestQueue;
    SpotsDialog spotsDialog;

    private Toolbar toolbar;
    private TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_my_club);
        mRequestQueue = Volley.newRequestQueue(this);
        tvAssociationTitlePageClub = (TextView) findViewById(R.id.tvAssociationTitlePageClub);
        tvClubTitlePageClub = (TextView) findViewById(R.id.tvClubTitlePageClub);
        cursorOnePageClub = (ImageView) findViewById(R.id.cursorOnePageClub);
        cursorTwoPageClub = (ImageView) findViewById(R.id.cursorTwoPageClub);
        cursorTwoPageClub.setVisibility(View.INVISIBLE);
        InitTextView();
        InitViewPager();
        setTitleBar();
    }

    private void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("组织机构");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityMyClubAndAssociation.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        spotsDialog = new SpotsDialog(this);
        spotsDialog.show();
        clubList.clear();
        StringRequest stringRequestMyClub = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL + "Users/myAssoc",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("result") == 1) {
                                try {
                                    JSONArray jsonArr = jsonObject.getJSONArray("data");
                                    for (int i = 0; i < jsonArr.length(); i++) {
                                        clubList.add(jsonArr.getJSONObject(i));
                                    }
                                    clubAdapter.notifyDataSetChanged();
                                    spotsDialog.dismiss();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    spotsDialog.dismiss();
                                    e.printStackTrace();
                                }
                            } else {
                                spotsDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            spotsDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        spotsDialog.dismiss();
                        Toast.makeText(ActivityMyClubAndAssociation.this, Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequestMyClub);
    }

    @SuppressLint("ResourceAsColor")
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPagerPageClub);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        View viewAssociation = mInflater.inflate(R.layout.my_club_and_association_list, null);
        //TODO
        listClub = (ListView) viewAssociation.findViewById(R.id.lvMyClubAndAssoc);
        clubAdapter = new MyClubAndAssocListAdapter(this, clubList);
        listClub.setAdapter(clubAdapter);
        listViews.add(viewAssociation);

        //clublistView 初始化  从缓存中读取已经存在的俱乐部列表TODO
//	        View viewClub = mInflater.inflate(R.layout.club_list, null);
//	        //TODO
//	        listViews.add(viewClub);


        mPager.setAdapter(new ClubPagePagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.tvAssociationTitlePageClub);
        t2 = (TextView) findViewById(R.id.tvClubTitlePageClub);
        t1.setBackgroundColor(Color.rgb(40, 144, 178));
        t2.setBackgroundColor(Color.rgb(104, 177, 201));
        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
//        cursor = (ImageView) view.findViewById(R.id.cursorPageClub);
        cursor.setBackgroundColor(Color.argb(0, 0, 0, 0));
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * ViewPager适配器
     */
    public class ClubPagePagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public ClubPagePagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (!cursorOnePageClub.isShown()) {
                        cursorOnePageClub.setVisibility(ImageView.VISIBLE);
                    }
                    t1.setBackgroundColor(Color.rgb(40, 144, 178));
                    t2.setBackgroundColor(Color.rgb(104, 177, 201));
                    cursorTwoPageClub.setVisibility(ImageView.INVISIBLE);
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    t2.setBackgroundColor(Color.rgb(40, 144, 178));
                    t1.setBackgroundColor(Color.rgb(104, 177, 201));
                    if (!cursorTwoPageClub.isShown()) {
                        cursorTwoPageClub.setVisibility(ImageView.VISIBLE);
                    }
                    cursorOnePageClub.setVisibility(ImageView.INVISIBLE);
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
//            case 2:
//                if (currIndex == 0) {
//                    animation = new TranslateAnimation(offset, two, 0, 0);
//                } else if (currIndex == 1) {
//                    animation = new TranslateAnimation(one, two, 0, 0);
//                }
//                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
//            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
