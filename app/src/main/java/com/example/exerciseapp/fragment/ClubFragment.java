package com.example.exerciseapp.fragment;

/**
 * 协会俱乐部列表
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.ClubAndAssocListAdapter;
import com.example.exerciseapp.aty.sliding.AtyAssocOrClubInformation;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClubFragment extends Fragment {

    private LinkedList<Map<String, String>> list = new LinkedList<Map<String, String>>();

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    View view;

    private TextView tvAssociationTitlePageClub;
    private TextView tvClubTitlePageClub;

    private ImageView cursorOnePageClub;
    private ImageView cursorTwoPageClub;

    private PullToRefreshListView mPullToRefreshClubListView;
    private PullToRefreshListView mPullToRefreshAssocListView;
    private LinkedList<JSONObject> mClubListItems;
    private LinkedList<JSONObject> mAssocListItems;

    private ClubAndAssocListAdapter mClubAdapter;
    private ClubAndAssocListAdapter mAssocAdapter;
    private RequestQueue mRequestQueue;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRequestQueue = Volley.newRequestQueue(getActivity());
        view = inflater.inflate(R.layout.page_club, null);
        tvAssociationTitlePageClub = (TextView) view.findViewById(R.id.tvAssociationTitlePageClub);
        tvClubTitlePageClub = (TextView) view.findViewById(R.id.tvClubTitlePageClub);
        cursorOnePageClub = (ImageView) view.findViewById(R.id.cursorOnePageClub);
        cursorTwoPageClub = (ImageView) view.findViewById(R.id.cursorTwoPageClub);
        cursorTwoPageClub.setVisibility(View.INVISIBLE);
        InitTextView();
        InitViewPager();

        mPullToRefreshAssocListView.setRefreshing(false);
        return view;
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) view.findViewById(R.id.tvAssociationTitlePageClub);
//        t2 = (TextView) view.findViewById(R.id.tvClubTitlePageClub);
        t1.setBackgroundColor(Color.rgb(40, 144, 178));
//    	t2.setBackgroundColor(Color.rgb(104, 177, 201));
        t1.setOnClickListener(new MyOnClickListener(0));
//        t2.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     * 初始化ViewPager
     */
    @SuppressLint("ResourceAsColor")
    private void InitViewPager() {
        mPager = (ViewPager) view.findViewById(R.id.vPagerPageClub);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getActivity().getLayoutInflater();

        //协会列表初始化
        View viewAssociation = mInflater.inflate(R.layout.club_list, null);
        //TODO

        mPullToRefreshAssocListView = (PullToRefreshListView) viewAssociation.findViewById(R.id.pull_to_refresh_listview);
        mPullToRefreshAssocListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel  
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.  
                new AssocGetDataTask().execute();
            }

        });

        ListView assocListView = mPullToRefreshAssocListView.getRefreshableView();
        //去缓存数据
        mAssocListItems = new LinkedList<JSONObject>();
        try {
            if (Config.getCachedAssocList(getActivity().getApplicationContext()) != null) {
                JSONArray jsonArr = Config.getCachedAssocList(getActivity().getApplicationContext());
                for (int i = 0; i < jsonArr.length(); i++) {
                    mAssocListItems.addFirst(jsonArr.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAssocAdapter = new ClubAndAssocListAdapter(getActivity(), mAssocListItems);
        assocListView.setAdapter(mAssocAdapter);
        assocListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), AtyAssocOrClubInformation.class);
                intent.putExtra(Config.KEY_ASSOC_INFO, view.getTag().toString());
                startActivity(intent);
                return;
            }
        });

        listViews.add(viewAssociation);
        mPager.setAdapter(new ClubPagePagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    String result = new String();

    private class AssocGetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            StringRequest stringRequestUserInformation = new StringRequest(
                    Request.Method.POST,
                    Config.SERVER_URL + "Assoc/assocList",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("result") == 1) {
                                    result = s;
                                    JSONArray jsonArr = new JSONObject(s).getJSONArray("data");
                                    Config.cacheAssocList(getActivity().getApplicationContext(), jsonArr);
                                    jsonArr = Config.getCachedAssocList(getActivity().getApplicationContext());
                                    mAssocListItems.clear();
                                    for (int i = 0; i < jsonArr.length(); i++) {
                                        mAssocListItems.addFirst(jsonArr.getJSONObject(i));
                                    }
                                    mAssocAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    return map;
                }
            };
            mRequestQueue.add(stringRequestUserInformation);
            return new String[]{result};
        }

        @Override
        protected void onPostExecute(String[] result) {
            mAssocAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.  
            mPullToRefreshAssocListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    private class ClubGetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.  
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mClubAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.  
            mPullToRefreshClubListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        cursor.setBackgroundColor(Color.argb(0, 0, 0, 0));
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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

