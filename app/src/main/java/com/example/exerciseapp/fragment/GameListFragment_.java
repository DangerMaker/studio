package com.example.exerciseapp.fragment;

/**
 * 赛事列表
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.AddressData;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.GameListAdapter;
import com.example.exerciseapp.aty.sliding.AtyGameInformation;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

public class GameListFragment_ extends Fragment {

    protected ListView mListView;
    PullToRefreshListView mSwipeLayout;
    private GameListAdapter mListAdapter;
    private Context context;
    private Button button, button_ok;
    private boolean scrolling = false;
    private TextView tv;
    // TODO delete 2016/3/16<
    // private TextView tvCitiesListPageCompetetion;
    // private TextView tvGameCatalogoPageCompetetion;
    // private TextView tvGameLevelPageCompetetion;
    // private TextView tvGameNeedPayPageCompetetion;
    // private LinearLayout shaiXuanLinearLayout;
    // >
    // *********************************
    private LinkedList<JSONObject> list = new LinkedList<JSONObject>();
    private LinkedList<JSONObject> tmpList, changeList;
    private JSONArray jsonArr = new JSONArray();
    private String gameId;
    //*********************************

    //屏幕高度和屏幕宽度
    private int height;
    private int width;

    private String aId = null;    //协会id 用于显示协会赛事活动时候的标识符

    private RequestQueue mRequestQueue;

    private JSONArray jsonArrOld = new JSONArray();
    private JSONArray jsonArrTmp = new JSONArray();

    //设置当前筛选Flag
    private int projectFlag = Config.FLAG_GAME_PROJECT_DEFAULT, levelFlag = Config.FLAG_GAME_LEVEL_DEFAULT, payFlag = Config.FLAG_GAME_PAY_DEFAULT;
    private String positionProvince = "不限", positionCity = "不限", positionArea = "不限";
    private long lastTime = 0;

    private ViewPager adViewPager;
    private List<SimpleDraweeView> imageViews;// 滑动的图片集合
    private List<View> dots;
    private List<View> dotsList;
    private List<String> adImg;
    private int currentItem = 0; // 当前图片的索引号
    // 定义的五个指示点
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;
    // 定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            adViewPager.setCurrentItem(currentItem);
        };
    };

    private MyAdapter myAdapter;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;  // 屏幕宽度（像素）
        height = metric.heightPixels;  // 屏幕高度（像素）

        //list初始化
        if (Config.getCachedGameList(getActivity().getApplicationContext()) != null) {
            JSONArray jsonArr = Config.getCachedGameList(getActivity().getApplicationContext());
            try {
                for (int i = 0; i < jsonArr.length(); i++) {
                    list.addFirst(jsonArr.getJSONObject(i));
                }
                if (tmpList == null) {
                    tmpList = new LinkedList<JSONObject>();
                }
                tmpList.clear();
                tmpList.addAll(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final View view = inflater
                .inflate(R.layout.page_competetion_activities, null);
        final RelativeLayout rootLayout = (RelativeLayout) view.findViewById(R.id.rootLayoutPageCompetetion);

        // TODO delete 2016/3/16<
        // shaiXuanLinearLayout = (LinearLayout)
        // view.findViewById(R.id.shaiXuanLinearLayout);
        // tvCitiesListPageCompetetion = (TextView)
        // view.findViewById(R.id.tvCitiesListPageCompetetionGames);
        // tvGameCatalogoPageCompetetion = (TextView)
        // view.findViewById(R.id.tvGameCatalogoPageCompetetion);
        // tvGameLevelPageCompetetion = (TextView)
        // view.findViewById(R.id.tvGameLevelPageCompetetion);
        // tvGameNeedPayPageCompetetion = (TextView)
        // view.findViewById(R.id.tvGameNeedPayPageCompetetion);
        // // 城市列表滚轮实现
        // tvCitiesListPageCompetetion.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View v) {
        // PopupWindow popupWindow = makePopupWindow(getActivity());
        // int[] xy = new int[2];
        // rootLayout.getLocationOnScreen(xy);
        // popupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
        // }
        // });
        //
        // // 项目 级别 费用弹出框
        // tvGameCatalogoPageCompetetion.setOnClickListener(new
        // OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // new AlertDialog.Builder(getActivity())
        // .setItems(
        // new String[] { Config.DEFAULT_PROJECT, Config.BALL, Config.RUN,
        // Config.ICEWATER,
        // Config.BICYCLE, Config.PUBLIC, Config.EXTRA },
        // new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int which) {
        // projectFlag = which;
        // switch (which) {
        // case Config.FLAG_GAME_PROJECT_DEFAULT:
        // tvGameCatalogoPageCompetetion.setText(Config.DEFAULT_PROJECT);
        // break;
        // case Config.FLAG_GAME_PROJECT_BALL:
        // tvGameCatalogoPageCompetetion.setText(Config.BALL);
        // break;
        // case Config.FLAG_GAME_PROJECT_BICYCLE:
        // tvGameCatalogoPageCompetetion.setText(Config.BICYCLE);
        // break;
        // case Config.FLAG_GAME_PROJECT_EXTRA:
        // tvGameCatalogoPageCompetetion.setText(Config.EXTRA);
        // break;
        // case Config.FLAG_GAME_PROJECT_ICE:
        // tvGameCatalogoPageCompetetion.setText(Config.ICEWATER);
        // break;
        // case Config.FLAG_GAME_PROJECT_PUBLIC:
        // tvGameCatalogoPageCompetetion.setText("大众");
        // break;
        // case Config.FLAG_GAME_PROJECT_RUN:
        // tvGameCatalogoPageCompetetion.setText("跑步");
        // break;
        // default:
        // tvGameCatalogoPageCompetetion.setText(Config.DEFAULT_PROJECT);
        // break;
        // }
        // try {
        // changeList();
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // dialog.dismiss();
        // }
        //
        // }).show();
        //
        // }
        // });
        //
        // tvGameLevelPageCompetetion.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // new AlertDialog.Builder(getActivity())
        // .setItems(
        // new String[] { Config.DEFAULT_LEVEL, Config.INTERNATIONAL,
        // Config.NATIONAL,
        // Config.PROVINCE, Config.CITY, Config.AREA },
        // new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int which) {
        // switch (which) {
        // case Config.FLAG_GAME_LEVEL_DEFAULT:
        // tvGameLevelPageCompetetion.setText(Config.DEFAULT_LEVEL);
        // break;
        // case Config.FLAG_GAME_LEVEL_AREA:
        // tvGameLevelPageCompetetion.setText(Config.AREA);
        // break;
        // case Config.FLAG_GAME_LEVEL_CITY:
        // tvGameLevelPageCompetetion.setText(Config.CITY);
        // break;
        // case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
        // tvGameLevelPageCompetetion.setText(Config.INTERNATIONAL);
        // break;
        // case Config.FLAG_GAME_LEVEL_NATIONAL:
        // tvGameLevelPageCompetetion.setText(Config.NATIONAL);
        // break;
        // case Config.FLAG_GAME_LEVEL_PROVINCE:
        // tvGameLevelPageCompetetion.setText(Config.PROVINCE);
        // break;
        // default:
        // tvGameLevelPageCompetetion.setText(Config.DEFAULT_LEVEL);
        // break;
        // }
        // levelFlag = which;
        // try {
        // changeList();
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // dialog.dismiss();
        // }
        //
        // }).show();
        // }
        // });
        //
        // tvGameNeedPayPageCompetetion.setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View v) {
        // new AlertDialog.Builder(getActivity())
        // .setItems(new String[] { Config.DEFAULT_FEE, Config.FREE_PAY,
        // Config.NEED_PAY },
        // new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int which) {
        // switch (which) {
        // case Config.FLAG_GAME_PAY_DEFAULT:
        // tvGameNeedPayPageCompetetion.setText(Config.DEFAULT_FEE);
        // break;
        // case Config.FLAG_GAME_PAY_FREE:
        // tvGameNeedPayPageCompetetion.setText(Config.FREE_PAY);
        // break;
        // case Config.FLAG_GAME_PAY_NEED_PAY:
        // tvGameNeedPayPageCompetetion.setText(Config.NEED_PAY);
        // break;
        //
        // default:
        // break;
        // }
        // payFlag = which;
        // try {
        // changeList();
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // dialog.dismiss();
        // }
        //
        // }).show();
        //
        // }
        // });
        // >

        mSwipeLayout = (PullToRefreshListView) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 每次刷新初始化筛选列表

                // TODO delete 2016/3/16<
                // projectFlag = Config.FLAG_GAME_PROJECT_DEFAULT;
                // levelFlag = Config.FLAG_GAME_LEVEL_DEFAULT;
                // payFlag = Config.FLAG_GAME_PAY_DEFAULT;
                // positionProvince = "不限";
                // positionCity = "不限";
                // positionArea = "不限";
                //
                // tvCitiesListPageCompetetion.setText("地区");
                // tvGameCatalogoPageCompetetion.setText("项目");
                // tvGameLevelPageCompetetion.setText("级别");
                // tvGameNeedPayPageCompetetion.setText("费用");
                // >

                // // Do work to refresh the list here.
                new GetDataTask().execute();
            }

        });
//		mListView = (ListView) view.findViewById(R.id.listGame);
        mListView = mSwipeLayout.getRefreshableView();
        mListAdapter = new GameListAdapter(getActivity(), list);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), AtyGameInformation.class);
                try {
                    intent.putExtra(Config.KEY_GAME_ID, list.get(position - 1).getString("gid"));
                    intent.putExtra(Config.KEY_GAME_H5_URL, list.get(position - 1).getString("gintro"));
                    intent.putExtra(Config.KEY_GAME_NAME, list.get(position - 1).getString("gname"));
                    intent.putExtra(Config.KEY_GAME_STATUS_ID, list.get(position - 1).getString("gstatusid"));
                    intent.putExtra("agreement",list.get(position - 1).getString("agreement"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
        });
//		mListView.setFocusableInTouchMode(true);
//		mSwipeLayout.setOnRefreshListener(this);
//		mSwipeLayout.setOnLoadListener(this);
//		mSwipeLayout.setColor(android.R.color.holo_blue_bright,
//				android.R.color.holo_green_light,
//				android.R.color.holo_orange_light,
//				android.R.color.holo_red_light);
//		mSwipeLayout.setMode(SwipeRefreshLayout.Mode.PULL_FROM_START);
//		mSwipeLayout.setLoadNoFull(false);
        mSwipeLayout.setRefreshing(false);

        imageViews = new ArrayList<>();
        // 点
        dots = new ArrayList<View>();
        dotsList = new ArrayList<>();
        dot0 = view.findViewById(R.id.v_dot0);
        dot1 = view.findViewById(R.id.v_dot1);
        dot2 = view.findViewById(R.id.v_dot2);
        dot3 = view.findViewById(R.id.v_dot3);
        dot4 = view.findViewById(R.id.v_dot4);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);
        adImg = new ArrayList<>();
        adViewPager = (ViewPager) view.findViewById(R.id.vp);
        myAdapter = new MyAdapter();
        adViewPager.setAdapter(myAdapter);// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        adViewPager.addOnPageChangeListener(new MyPageChangeListener());
        addDynamicView();
        return view;
    }

    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {
        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private void addDynamicView() {
        // 动态添加图片和下面指示的圆点
        // 初始化图片资源
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                "http://101.200.214.68/py/system?action=get_ad",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("result").equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    adImg.add(jsonArray.getJSONObject(i).getString("pic"));
                                }
                                for (int i = 0; i < adImg.size(); i++) {
                                    SimpleDraweeView imageView = new SimpleDraweeView(GameListFragment_.this.getActivity());
                                    imageView.setImageURI(Uri.parse(adImg.get(i)));
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    imageViews.add(imageView);
                                    dots.get(i).setVisibility(View.VISIBLE);
                                    dotsList.add(dots.get(i));
                                }
                                myAdapter.notifyDataSetChanged();
                                startAd();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return adImg.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
            // 在这个方法里面设置图片的点击事件
//            iv.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 处理跳转逻辑
//                }
//            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
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

        @Override
        public void finishUpdate(View arg0) {

        }

    }

//	@Override
//	public void onLoad() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeLayout.setLoading(false);
//                mListAdapter.notifyDataSetChanged();
//            }
//        }, 1000);
//	}
//
//	@Override
//	public void onRefresh() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            	new GetDataTask().execute();
////                mSwipeLayout.setRefreshing(false);
////                mListAdapter.notifyDataSetChanged();
//            }
//        }, 2000);
//	}

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.

            try {
                Thread.sleep(1000);
                if (aId == null) {
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            "http://101.200.214.68/py/game?action=front_page",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        if (jsonObject.getString("result").equals("1")) {
                                            jsonArr = jsonObject.getJSONArray("data");
                                            if (getActivity() != null) {
                                                Config.cacheGameList(getActivity().getApplicationContext(),
                                                        jsonArr);
                                            }
                                            list.clear();
                                            for (int i = 0; i < jsonArr.length(); i++) {
                                                list.add(jsonArr.getJSONObject(i));
                                            }
                                            mListAdapter.notifyDataSetChanged();
                                        } else {

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_LONG).show();
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            return map;
                        }
                    };
                    mRequestQueue.add(stringRequest);
                } else {
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Config.SERVER_URL + "Assoc/gameOfAssoc",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        if (jsonObject.getString("result").equals("1")) {
                                            jsonArr = jsonObject.getJSONArray("data");
                                        } else {

                                        }

                                        aId = null;
                                        list.clear();
                                        for (int i = 0; i < jsonArr.length(); i++) {
                                            list.addFirst(jsonArr.getJSONObject(i));
                                        }
                                        mListAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_LONG).show();
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(Config.KEY_AID, aId);
                            return map;
                        }
                    };
                    mRequestQueue.add(stringRequest);
                }
            } catch (InterruptedException e) {
            }
            return null;
        }

//		private boolean isExistedJSONObjectInJSONArray(JSONArray jsonArr,JSONObject jsonObj){
//			for (int i = 0; i < jsonArr.length(); i++) {
//				try {
//					if(jsonArr.getJSONObject(i).getString(Config.KEY_GAME_ID).equals(jsonObj.getString(Config.KEY_GAME_ID))){
//						return true;
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			return false;
//		}

        @Override
        protected void onPostExecute(String[] result) {

            // if(tmpList == null){
            // tmpList = new LinkedList<JSONObject>();
            // }
            // tmpList.clear();
            // tmpList.addAll(list);
            // try {
            // changeList();
            // } catch (JSONException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

            // TODO delete 2016/3/16<
            // if (list.size() >= 4) {
            // shaiXuanLinearLayout.setVisibility(View.VISIBLE);
            // } else {
            // shaiXuanLinearLayout.setVisibility(View.GONE);
            // }
            // >
            mListAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mSwipeLayout.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    // 创建一个包含自定义view的PopupWindow
    @SuppressWarnings("null")
    private PopupWindow makePopupWindow(Context cx) {
        final PopupWindow window;
        window = new PopupWindow(cx);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View contentView = inflater.inflate(R.layout.cities_layout, null);
        window.setContentView(contentView);


        tv = (TextView) contentView.findViewById(R.id.tv_cityName);

        final WheelView country = (WheelView) contentView.findViewById(R.id.country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(cx));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView.findViewById(R.id.city);
        city.setVisibleItems(0);

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateCities(city, cities, newValue);
                }
            }
        });

        country.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }

            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                updateCities(city, cities, country.getCurrentItem());

                tv.setText(AddressData.PROVINCES[country.getCurrentItem()]);
            }
        });

        // 地区选择
//	        final WheelView ccity = (WheelView) contentView.findViewById(R.id.ccity);
//	        ccity.setVisibleItems(0);

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    // updatecCities(ccity, ccities,
                    // country.getCurrentItem(),newValue);
                }
            }
        });

        city.addScrollingListener(new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }

            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                // updatecCities(ccity, ccities, country.getCurrentItem(),
                // city.getCurrentItem());

                tv.setText(AddressData.PROVINCES[country.getCurrentItem()] + "-"
                        + AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]);

            }
        });

        // ccity.addScrollingListener( new OnWheelScrollListener() {
        // public void onScrollingStarted(WheelView wheel) {
        // scrolling = true;
        // }
        // public void onScrollingFinished(WheelView wheel) {
        // scrolling = false;
        //
        // tv.setText(
        // AddressData.PROVINCES[country.getCurrentItem()] + "-" +
        // AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]
        //// + "-" +
        //// AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()]
        // );
        // }
        // });

        country.setCurrentItem(1);

        // 点击事件处理
        button_ok = (Button) contentView.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO delete 2016/3/16<
                // positionProvince =
                // AddressData.PROVINCES[country.getCurrentItem()];
                // positionCity =
                // AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()];
                // // positionArea =
                // //
                // AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()];
                // tvCitiesListPageCompetetion.setText(AddressData.PROVINCES[country.getCurrentItem()]
                // + "-"
                // +
                // AddressData.CITIES[country.getCurrentItem()][city.getCurrentItem()]
                // // + "-" +
                // //
                // AddressData.COUNTIES[country.getCurrentItem()][city.getCurrentItem()][ccity.getCurrentItem()]
                // );
                // window.dismiss(); // 隐藏
                // try {
                // changeList();
                // } catch (JSONException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // >

            }
        });

        window.setWidth(width);
        window.setHeight(height / 3);

        // 设置PopupWindow外部区域是否可触摸
        window.setFocusable(true); // 设置PopupWindow可获得焦点
        window.setTouchable(true); // 设置PopupWindow可触摸
        window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        return window;
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity().getApplicationContext(),
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cities[index].length / 2);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(WheelView city, String ccities[][][], int index, int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(getActivity().getApplicationContext(),
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(ccities[index][index2].length / 2);
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = AddressData.PROVINCES;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.country_layout, NO_RESOURCE);

            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }

    // 根据筛选条件依次筛选并刷新列表
    private void changeList() throws JSONException {
        list.clear();
        if (tmpList != null && !tmpList.isEmpty()) {
            list.addAll(tmpList);
        }
        if (changeList == null) {
            changeList = new LinkedList<JSONObject>();
        }
        changeList.clear();
        changeList.addAll(list);
        for (JSONObject json : list) {
            if (positionProvince.equals("不限")) {

            } else {
                if (positionProvince
                        .equals(json.getString(Config.KEY_GAME_POSITION).substring(0,
                                json.getString(Config.KEY_GAME_POSITION).indexOf("·")))
                        && positionCity.equals(json.getString(Config.KEY_GAME_POSITION)
                        .substring(json.getString(Config.KEY_GAME_POSITION).indexOf("·") + 1))) {

                } else {
                    changeList.remove(json);
                }

            }
        }
        switch (projectFlag) {
            //项目case
            case Config.FLAG_GAME_PROJECT_DEFAULT:
                switch (levelFlag) {
                    case Config.FLAG_GAME_LEVEL_DEFAULT:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-级别-费用
                                changeList(Config.DEFAULT_PROJECT, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-级别-免费
                                changeList(Config.DEFAULT_PROJECT, Config.DEFAULT_LEVEL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=级别-付费
                                changeList(Config.DEFAULT_PROJECT, Config.DEFAULT_LEVEL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.DEFAULT_PROJECT, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国际-费用
                                changeList(Config.DEFAULT_PROJECT, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国际-免费
                                changeList(Config.DEFAULT_PROJECT, Config.INTERNATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国际-付费
                                changeList(Config.DEFAULT_PROJECT, Config.INTERNATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.DEFAULT_PROJECT, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_NATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国家级-费用
                                changeList(Config.DEFAULT_PROJECT, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国家级-免费
                                changeList(Config.DEFAULT_PROJECT, Config.NATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国家级-付费
                                changeList(Config.DEFAULT_PROJECT, Config.NATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.DEFAULT_PROJECT, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_PROVINCE:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-省级-费用
                                changeList(Config.DEFAULT_PROJECT, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-省级-免费
                                changeList(Config.DEFAULT_PROJECT, Config.PROVINCE, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=省级-付费
                                changeList(Config.DEFAULT_PROJECT, Config.PROVINCE, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.DEFAULT_PROJECT, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_CITY:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-市级-费用
                                changeList(Config.DEFAULT_PROJECT, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-市级-免费
                                changeList(Config.DEFAULT_PROJECT, Config.CITY, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=市级-付费
                                changeList(Config.DEFAULT_PROJECT, Config.CITY, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.DEFAULT_PROJECT, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_AREA:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-地区-费用
                                changeList(Config.DEFAULT_PROJECT, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-地区-免费
                                changeList(Config.DEFAULT_PROJECT, Config.AREA, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=地区-付费
                                changeList(Config.DEFAULT_PROJECT, Config.AREA, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.DEFAULT_PROJECT, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;

                    default:
                        changeList(Config.DEFAULT_PROJECT, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                        break;
                }
                break;
            //***************************************************************
            //球类
            case Config.FLAG_GAME_PROJECT_BALL:
                switch (levelFlag) {
                    case Config.FLAG_GAME_LEVEL_DEFAULT:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-级别-费用
                                changeList(Config.BALL, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-级别-免费
                                changeList(Config.BALL, Config.DEFAULT_LEVEL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=级别-付费
                                changeList(Config.BALL, Config.DEFAULT_LEVEL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BALL, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国际-费用
                                changeList(Config.BALL, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国际-免费
                                changeList(Config.BALL, Config.INTERNATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国际-付费
                                changeList(Config.BALL, Config.INTERNATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BALL, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_NATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国家级-费用
                                changeList(Config.BALL, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国家级-免费
                                changeList(Config.BALL, Config.NATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国家级-付费
                                changeList(Config.BALL, Config.NATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BALL, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_PROVINCE:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-省级-费用IntroIntroIntro
                                changeList(Config.BALL, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-省级-免费
                                changeList(Config.BALL, Config.PROVINCE, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=省级-付费
                                changeList(Config.BALL, Config.PROVINCE, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BALL, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_CITY:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-市级-费用
                                changeList(Config.BALL, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-市级-免费
                                changeList(Config.BALL, Config.CITY, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=市级-付费
                                changeList(Config.BALL, Config.CITY, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BALL, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_AREA:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-地区-费用
                                changeList(Config.BALL, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-地区-免费
                                changeList(Config.BALL, Config.AREA, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=地区-付费
                                changeList(Config.BALL, Config.AREA, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BALL, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;

                    default:
                        changeList(Config.BALL, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                        break;
                }
                break;
            //***************************************************************
            //跑步
            case Config.FLAG_GAME_PROJECT_RUN:
                switch (levelFlag) {
                    case Config.FLAG_GAME_LEVEL_DEFAULT:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-级别-费用
                                changeList(Config.RUN, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-级别-免费
                                changeList(Config.RUN, Config.DEFAULT_LEVEL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=级别-付费
                                changeList(Config.RUN, Config.DEFAULT_LEVEL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.RUN, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国际-费用
                                changeList(Config.RUN, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国际-免费
                                changeList(Config.RUN, Config.INTERNATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国际-付费
                                changeList(Config.RUN, Config.INTERNATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.RUN, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_NATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国家级-费用
                                changeList(Config.RUN, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国家级-免费
                                changeList(Config.RUN, Config.NATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国家级-付费
                                changeList(Config.RUN, Config.NATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.RUN, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_PROVINCE:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-省级-费用
                                changeList(Config.RUN, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-省级-免费
                                changeList(Config.RUN, Config.PROVINCE, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=省级-付费
                                changeList(Config.RUN, Config.PROVINCE, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.RUN, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_CITY:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-市级-费用
                                changeList(Config.RUN, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-市级-免费
                                changeList(Config.RUN, Config.CITY, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=市级-付费
                                changeList(Config.RUN, Config.CITY, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.RUN, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_AREA:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-地区-费用
                                changeList(Config.RUN, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-地区-免费
                                changeList(Config.RUN, Config.AREA, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=地区-付费
                                changeList(Config.RUN, Config.AREA, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.RUN, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;

                    default:
                        changeList(Config.RUN, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                        break;
                }
                break;
            //***************************************************************
            //冰水
            case Config.FLAG_GAME_PROJECT_ICE:
                switch (levelFlag) {
                    case Config.FLAG_GAME_LEVEL_DEFAULT:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-级别-费用
                                changeList(Config.ICEWATER, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-级别-免费
                                changeList(Config.ICEWATER, Config.DEFAULT_LEVEL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=级别-付费
                                changeList(Config.ICEWATER, Config.DEFAULT_LEVEL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.ICEWATER, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国际-费用
                                changeList(Config.ICEWATER, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国际-免费
                                changeList(Config.ICEWATER, Config.INTERNATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国际-付费
                                changeList(Config.ICEWATER, Config.INTERNATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.ICEWATER, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_NATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国家级-费用
                                changeList(Config.ICEWATER, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国家级-免费
                                changeList(Config.ICEWATER, Config.NATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国家级-付费
                                changeList(Config.ICEWATER, Config.NATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.ICEWATER, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_PROVINCE:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-省级-费用
                                changeList(Config.ICEWATER, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-省级-免费
                                changeList(Config.ICEWATER, Config.PROVINCE, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=省级-付费
                                changeList(Config.ICEWATER, Config.PROVINCE, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.ICEWATER, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_CITY:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-市级-费用
                                changeList(Config.ICEWATER, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-市级-免费
                                changeList(Config.ICEWATER, Config.CITY, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=市级-付费
                                changeList(Config.ICEWATER, Config.CITY, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.ICEWATER, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_AREA:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-地区-费用
                                changeList(Config.ICEWATER, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-地区-免费
                                changeList(Config.ICEWATER, Config.AREA, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=地区-付费
                                changeList(Config.ICEWATER, Config.AREA, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.ICEWATER, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;

                    default:
                        changeList(Config.ICEWATER, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                        break;
                }
                break;
            //***************************************************************
            //自行车
            case Config.FLAG_GAME_PROJECT_BICYCLE:
                switch (levelFlag) {
                    case Config.FLAG_GAME_LEVEL_DEFAULT:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-级别-费用
                                changeList(Config.BICYCLE, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-级别-免费
                                changeList(Config.BICYCLE, Config.DEFAULT_LEVEL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=级别-付费
                                changeList(Config.BICYCLE, Config.DEFAULT_LEVEL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BICYCLE, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国际-费用
                                changeList(Config.BICYCLE, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国际-免费
                                changeList(Config.BICYCLE, Config.INTERNATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国际-付费
                                changeList(Config.BICYCLE, Config.INTERNATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BICYCLE, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_NATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国家级-费用
                                changeList(Config.BICYCLE, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国家级-免费
                                changeList(Config.BICYCLE, Config.NATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国家级-付费
                                changeList(Config.BICYCLE, Config.NATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BICYCLE, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_PROVINCE:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-省级-费用
                                changeList(Config.BICYCLE, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-省级-免费
                                changeList(Config.BICYCLE, Config.PROVINCE, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=省级-付费
                                changeList(Config.BICYCLE, Config.PROVINCE, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BICYCLE, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_CITY:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-市级-费用
                                changeList(Config.BICYCLE, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-市级-免费
                                changeList(Config.BICYCLE, Config.CITY, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=市级-付费
                                changeList(Config.BICYCLE, Config.CITY, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BICYCLE, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_AREA:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-地区-费用
                                changeList(Config.BICYCLE, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-地区-免费
                                changeList(Config.BICYCLE, Config.AREA, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=地区-付费
                                changeList(Config.BICYCLE, Config.AREA, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.BICYCLE, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;

                    default:
                        changeList(Config.BICYCLE, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                        break;
                }
                break;
            //***************************************************************
            //大众
            case Config.FLAG_GAME_PROJECT_PUBLIC:
                switch (levelFlag) {
                    case Config.FLAG_GAME_LEVEL_DEFAULT:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-级别-费用
                                changeList(Config.PUBLIC, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-级别-免费
                                changeList(Config.PUBLIC, Config.DEFAULT_LEVEL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=级别-付费
                                changeList(Config.PUBLIC, Config.DEFAULT_LEVEL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.PUBLIC, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国际-费用
                                changeList(Config.PUBLIC, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国际-免费
                                changeList(Config.PUBLIC, Config.INTERNATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国际-付费
                                changeList(Config.PUBLIC, Config.INTERNATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.PUBLIC, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_NATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国家级-费用
                                changeList(Config.PUBLIC, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国家级-免费
                                changeList(Config.PUBLIC, Config.NATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国家级-付费
                                changeList(Config.PUBLIC, Config.NATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.PUBLIC, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_PROVINCE:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-省级-费用
                                changeList(Config.PUBLIC, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-省级-免费
                                changeList(Config.PUBLIC, Config.PROVINCE, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=省级-付费
                                changeList(Config.PUBLIC, Config.PROVINCE, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.PUBLIC, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_CITY:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-市级-费用
                                changeList(Config.PUBLIC, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-市级-免费
                                changeList(Config.PUBLIC, Config.CITY, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=市级-付费
                                changeList(Config.PUBLIC, Config.CITY, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.PUBLIC, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_AREA:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-地区-费用
                                changeList(Config.PUBLIC, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-地区-免费
                                changeList(Config.PUBLIC, Config.AREA, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=地区-付费
                                changeList(Config.PUBLIC, Config.AREA, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.PUBLIC, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;

                    default:
                        changeList(Config.PUBLIC, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                        break;
                }
                break;
            //***************************************************************
            //其他
            case Config.FLAG_GAME_PROJECT_EXTRA:
                switch (levelFlag) {
                    case Config.FLAG_GAME_LEVEL_DEFAULT:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-级别-费用
                                changeList(Config.EXTRA, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-级别-免费
                                changeList(Config.EXTRA, Config.DEFAULT_LEVEL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=级别-付费
                                changeList(Config.EXTRA, Config.DEFAULT_LEVEL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.EXTRA, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_INTERNATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国际-费用
                                changeList(Config.EXTRA, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国际-免费
                                changeList(Config.EXTRA, Config.INTERNATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国际-付费
                                changeList(Config.EXTRA, Config.INTERNATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.EXTRA, Config.INTERNATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_NATIONAL:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-国家级-费用
                                changeList(Config.EXTRA, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-国家级-免费
                                changeList(Config.EXTRA, Config.NATIONAL, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=国家级-付费
                                changeList(Config.EXTRA, Config.NATIONAL, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.EXTRA, Config.NATIONAL, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_PROVINCE:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-省级-费用
                                changeList(Config.EXTRA, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-省级-免费
                                changeList(Config.EXTRA, Config.PROVINCE, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=省级-付费
                                changeList(Config.EXTRA, Config.PROVINCE, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.EXTRA, Config.PROVINCE, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_CITY:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-市级-费用
                                changeList(Config.EXTRA, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-市级-免费
                                changeList(Config.EXTRA, Config.CITY, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=市级-付费
                                changeList(Config.EXTRA, Config.CITY, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.EXTRA, Config.CITY, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;
                    case Config.FLAG_GAME_LEVEL_AREA:
                        switch (payFlag) {
                            case Config.FLAG_GAME_PAY_DEFAULT:
                                //项目-地区-费用
                                changeList(Config.EXTRA, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_FREE:
                                //项目-地区-免费
                                changeList(Config.EXTRA, Config.AREA, Config.FREE_PAY, list, changeList);
                                break;
                            case Config.FLAG_GAME_PAY_NEED_PAY:
                                //项目=地区-付费
                                changeList(Config.EXTRA, Config.AREA, Config.NEED_PAY, list, changeList);
                                break;
                            default:
                                changeList(Config.EXTRA, Config.AREA, Config.DEFAULT_FEE, list, changeList);
                                break;
                        }
                        break;

                    default:
                        changeList(Config.EXTRA, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                        break;
                }
                break;
            //********************************************************************************
            default:
                changeList(Config.DEFAULT_PROJECT, Config.DEFAULT_LEVEL, Config.DEFAULT_FEE, list, changeList);
                break;
        }
        list.clear();
        list.addAll(changeList);
        mListAdapter.notifyDataSetChanged();
//			mSwipeLayout.setRefreshing(false);
    }


    //改变changelist
    private void changeList(String type, String level, String pay, LinkedList<JSONObject> list, LinkedList<JSONObject> changeList) throws JSONException {
        if (type.equals(Config.DEFAULT_PROJECT)) {
            if (level.equals(Config.DEFAULT_LEVEL)) {
                if (pay.equals(Config.DEFAULT_FEE)) {
                    return;
                } else {
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_NEED_PAY).equals(pay)) {
                            changeList.remove(json);
                        }
                    }
                    return;
                }
            } else {
                if (pay.equals(Config.DEFAULT_FEE)) {
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_LEVEL).equals(level)) {
                            changeList.remove(json);
                        }
                    }
                    return;
                } else {
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_LEVEL).equals(level)) {
                            changeList.remove(json);
                        }
                    }
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_NEED_PAY).equals(pay)) {
                            changeList.remove(json);
                        }
                    }
                    return;
                }
            }
        } else {
            if (level.equals(Config.DEFAULT_LEVEL)) {
                if (pay.equals(Config.DEFAULT_FEE)) {
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_TYPE).equals(type)) {
                            changeList.remove(json);
                        }
                    }
                    return;
                } else {
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_TYPE).equals(type)) {
                            changeList.remove(json);
                        }
                    }
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_NEED_PAY).equals(pay)) {
                            changeList.remove(json);
                        }
                    }
                    return;
                }
            } else {
                if (pay.equals(Config.DEFAULT_FEE)) {
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_TYPE).equals(type)) {
                            changeList.remove(json);
                        }
                    }
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_LEVEL).equals(level)) {
                            changeList.remove(json);
                        }
                    }
                    return;
                } else {
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_TYPE).equals(type)) {
                            changeList.remove(json);
                        }
                    }
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_LEVEL).equals(level)) {
                            changeList.remove(json);
                        }
                    }
                    for (JSONObject json : list) {
                        if (!json.getString(Config.KEY_GAME_NEED_PAY).equals(pay)) {
                            changeList.remove(json);
                        }
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//	    	if(list.size()>=4){
//	    		shaiXuanLinearLayout.setVisibility(View.VISIBLE);
//	    	}else{
//	    		shaiXuanLinearLayout.setVisibility(View.GONE);
//	    	}
//	    	long currentTime = System.currentTimeMillis();
//	    	if(lastTime == 0){
//	    		lastTime  = currentTime;
////	    		mSwipeLayout.startRefresh();
//	    	}else{
//	    		if(currentTime-lastTime>100000){
////	    			mSwipeLayout.startRefresh();
//	    		}
//	    	}
//	    	lastTime = currentTime;
        if (Config.aId != null) {
            list.clear();
            mListAdapter.notifyDataSetChanged();
            aId = Config.aId;
            Config.aId = null;
        }
    }
}
