package com.example.exerciseapp.fragment;

/**
 * Created by sonchcng on 16/6/7.
 */

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.activityrun.ActivitySaveRunData;
import com.example.exerciseapp.myutils.HintDialog;
import com.example.exerciseapp.utils.SpeedConvert;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class SportTabRunningThirdFragment extends Fragment implements com.amap.api.maps2d.LocationSource,
        AMapLocationListener, RadioGroup.OnCheckedChangeListener {
    AMap mMap;
    MapView mMapView;
    OnLocationChangedListener mListener;
    LocationManagerProxy mAMapLocationManager;
    PolylineOptions polylineops = new PolylineOptions();
    public LinkedList<PolylineOptions> mypolys = new LinkedList<PolylineOptions>();
    public LinkedList<Double> mypolylatitude = new LinkedList<Double>();
    public LinkedList<Double> mypolylongtitude = new LinkedList<Double>();
    public LinkedList<Double> mypolyalti = new LinkedList<Double>();
    double[] polyjing;//重要！上传数据！
    double[] polywei;//重要！上传数据！
    double[] polyalti;//重要！上传数据！
    private double max_speed = 0;//重要！上传数据！分钟/公里 由minSpeed求得 m/s
    private double minSpeed_onAverage = 0;//上传数据！分钟/公里 由minSpeed求得 m/s
    private double locSpeed = 0; //高德地图获取的速度 米/秒
    private double minSpeed = 0;//分钟/公里
    private double hourSpeed = 0;//公里/小时
    public double temps1 = 0.0;//临时距离
    public double temps2 = 0.0;//临时距离
    public double expected = 15.0;//期望距离
    public double altitude = 0.0;
    public zuobiao newll = new zuobiao(-100, -100);
    public zuobiao oldll = new zuobiao(-100, -100);
    private RequestQueue mRequestQueue;
    long shijian;
    public double distance = 0.0;
    int tizhong = 70;
    int runtag = 1;//0为初始，1为开始点了开始跑步，2为点了暂停跑步,3为结束
    boolean flagInitial = false;
    private boolean firstGetLocation = true;
    private int sportTag = 0;
    RelativeLayout myrunbig;
    TextView startrunbigdistance;
    TextView startrunbigtime;
    TextView startrunsmalldistance;
    TextView startrunsmalltime;
    TextView startrunheartrate;
    TextView startrunaltitude;
    TextView startrunspeed;
    TextView startruncal;
    ImageView startrun_pause;
    ImageView startrun_lock;
    ImageView startrun_end;
    ImageView startrun_unlock;
    ImageView startrun_continue;
    public DecimalFormat df = new DecimalFormat("#0.00");

    private FragmentManager fragmentManager;

    LinearLayout myrunsmall;
    TimeCount timecount;
    public boolean islock = false;
    public boolean istrue = true;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_start_run, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        myrunsmall = (LinearLayout) view.findViewById(R.id.myrunsmall);
        myrunbig = (RelativeLayout) view.findViewById(R.id.myrunbig);
        startrunbigdistance = (TextView) view.findViewById(R.id.startrunbigdistance);
        startrunbigtime = (TextView) view.findViewById(R.id.startrunbigtime);
        startrunsmalldistance = (TextView) view.findViewById(R.id.startrunsmalldistance);
        startrunsmalltime = (TextView) view.findViewById(R.id.startrunsmalltime);
        startrunheartrate = (TextView) view.findViewById(R.id.startrunheartrate);
        startrunaltitude = (TextView) view.findViewById(R.id.startrunaltitude);
        startrunspeed = (TextView) view.findViewById(R.id.startrunspeed);
        startruncal = (TextView) view.findViewById(R.id.startruncal);
        startrun_pause = (ImageView) view.findViewById(R.id.startrun_pause);
        startrun_lock = (ImageView) view.findViewById(R.id.startrun_lock);
        startrun_end = (ImageView) view.findViewById(R.id.startrun_end);
        startrun_unlock = (ImageView) view.findViewById(R.id.startrun_unlock);
        startrun_continue = (ImageView) view.findViewById(R.id.startrun_continue);

        flagInitial = true;
        mMapView.onCreate(savedInstanceState);
        setUpMapIfNeeded();
        init();
        startrun_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islock == false)
                    dofinish();
            }
        });
        startrun_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islock == false) {
                    timecount.pause();
                    runtag = 2;
                    startrun_continue.setVisibility(View.VISIBLE);
                    startrun_pause.setVisibility(View.GONE);

                }
            }
        });
        startrun_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islock == false) {
                    timecount.resume();
                    runtag = 1;
                    startrun_continue.setVisibility(View.GONE);
                    startrun_pause.setVisibility(View.VISIBLE);
                }
            }
        });
        startrun_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islock == false) {
                    islock = true;
                    startrun_lock.setVisibility(View.GONE);
                    startrun_unlock.setVisibility(View.VISIBLE);
                }
            }
        });
        startrun_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (islock == true) {
                    islock = false;
                    startrun_lock.setVisibility(View.VISIBLE);
                    startrun_unlock.setVisibility(View.GONE);
                }
            }
        });
        mMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (istrue) {
                    myrunbig.setVisibility(View.GONE);
                    myrunsmall.setVisibility(View.VISIBLE);
                    istrue = false;

                } else {
                    myrunbig.setVisibility(View.VISIBLE);
                    myrunsmall.setVisibility(View.GONE);
                    istrue = true;

                }
            }
        });
        mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL + "Users/briefUserInfoNew",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("result").equals("1")) {
                                if (jsonObject.getJSONObject("data").getString("weight") != null)
                                    if (jsonObject.getJSONObject("data").getString("weight").equals("")) {
                                        tizhong = 75;
                                    } else {
                                        tizhong = Integer.parseInt(jsonObject.getJSONObject("data").getString("weight"));
                                    }
                            } else {
                                Toast.makeText(getActivity(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                            }

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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_UID, Config.getCachedUserUid(getActivity()));
                return map;
            }
        };
        mRequestQueue.add(stringRequest);
        return view;
    }

    private void hintend() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.dialog_show_gotologin, null);
        final HintDialog dialog = new HintDialog(getActivity(), dialogView,
                R.style.MyDialogStyle);
        dialog.setCancelable(true);
        TextView tvHintDialogMessage = (TextView) dialogView
                .findViewById(R.id.tvHintDialogMessage);
        TextView btnCancel = (TextView) dialogView
                .findViewById(R.id.btnHintDialogNo);
        TextView btnToLogin = (TextView) dialogView
                .findViewById(R.id.btnHintDialogYes);
        tvHintDialogMessage.setText("是否结束本次运动？");
        btnCancel.setText(this.getString(R.string.cancel));
        btnToLogin.setText("确定");
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
                if (distance < 10 || mypolylatitude.size() < 10) {
                    Toast.makeText(getActivity(), "运动距离太短，没有数据记录", Toast.LENGTH_SHORT).show();
                } else {
                    SaveData();
                }
            }
        });
        dialog.show();
    }

    public void dofinish() {
        timecount.cancel();//onFinish();
        mMap.clear();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.yuanquan_icon));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        mMap.setMyLocationStyle(myLocationStyle);
        if (distance < 10 || mypolylatitude.size() < 10) {
            Toast.makeText(getActivity(), "运动距离太短，没有数据记录", Toast.LENGTH_SHORT).show();
            ChangeFragment();

        } else {
            SaveData();
        }
    }

    private void ChangeFragment() {
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment myThirdFragment = new SportTabThirdFragment();
        transaction.replace(R.id.run_record_content, myThirdFragment);
        transaction.commit();
        SportTabRunningThirdFragment.super.onDestroy();
    }

    private void SaveData() {
        Intent intent = new Intent(getActivity(), ActivitySaveRunData.class);
        intent.putExtra("intentjuli", distance);//m
        intent.putExtra("intentshijian", shijian);//s
        intent.putExtra("intentkaluli", (float) (Math.round(distance * tizhong * 1.036 / 1000 * 100)) / 100);
        int length = mypolylatitude.size();
        polyjing = new double[length];
        polywei = new double[length];
        polyalti = new double[length];
        for (int l = 0; l < length; l++) {
            polyjing[l] = mypolylatitude.get(l);
            polywei[l] = mypolylongtitude.get(l);
            polyalti[l] = mypolyalti.get(l);

        }
        intent.putExtra("intentpolyjing", polyjing);
        intent.putExtra("intentpolywei", polywei);
        intent.putExtra("intentpolyalti", polyalti);
        intent.putExtra("sport_type", sportTag);
        intent.putExtra("max_speed", (double) (Math.round(max_speed * 100)) / 100);
        intent.putExtra("minSpeed_onAverage", (double) (Math.round(minSpeed_onAverage * 100)) / 100);
        startActivity(intent);
//        ChangeFragment();
        getActivity().finish();
    }

    private void init() {
        if (mMap == null) {
            setUpMapIfNeeded();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.yuanquan_icon));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        mMap.setMyLocationStyle(myLocationStyle);
        mMap.setLocationSource(this);// 设置定位监听
        mMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

        mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        timecount = new TimeCount(86400000, 1000);
        timecount.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (mListener != null && amapLocation != null) {
            if (flagInitial) {
                oldll.jingdu = amapLocation.getLatitude();
                oldll.weidu = amapLocation.getLongitude();
                oldll.altit = amapLocation.getAltitude();
                flagInitial = false;
                temps1 = 0.0;
                PolylineOptions polys = new PolylineOptions();
                mypolys.add(polys);
            } else {
                oldll.jingdu = newll.jingdu;
                oldll.weidu = newll.weidu;
                oldll.altit = newll.altit;
            }
            newll.jingdu = amapLocation.getLatitude();
            newll.weidu = amapLocation.getLongitude();
            newll.altit = amapLocation.getAltitude();
            LatLng newlatlng = new LatLng(newll.jingdu, newll.weidu);
            LatLng oldlatlng = new LatLng(oldll.jingdu, oldll.weidu);
            locSpeed = amapLocation.getSpeed();//m/s
            hourSpeed = locSpeed * 3.6;//km/h
            if (0 == locSpeed) {
                minSpeed = 0;
            } else {
                minSpeed = SpeedConvert.oriToShow(locSpeed);//min/km
            }
            if (locSpeed > max_speed) {
                max_speed = locSpeed;
            }
            if (0 == distance) {
                if (0 == locSpeed) {
                    minSpeed_onAverage = 0;
                } else {
                    minSpeed_onAverage = locSpeed;
                }
            } else {
                if (0 != locSpeed && 0 != AMapUtils.calculateLineDistance(oldlatlng, newlatlng)) {
                    minSpeed_onAverage = (distance + AMapUtils.calculateLineDistance(oldlatlng, newlatlng)) * minSpeed_onAverage * locSpeed / (distance * locSpeed + AMapUtils.calculateLineDistance(oldlatlng, newlatlng) * minSpeed_onAverage);
                }
            }
            if (runtag == 1 && oldll.jingdu != 0.0 && newll.jingdu != 0.0) {
                distance += AMapUtils.calculateLineDistance(oldlatlng, newlatlng);
                temps2 = temps1;
                temps1 = AMapUtils.calculateLineDistance(oldlatlng, newlatlng);
                if (temps1 < expected || (temps1 - temps2 < expected && temps2 - temps1 < expected)) {
                    mMap.addPolyline((new PolylineOptions()).
                            add(new LatLng(oldll.jingdu, oldll.weidu), new LatLng(newll.jingdu, newll.weidu)));

                }
                polylineops.add(newlatlng);
                if (newll.jingdu == oldll.jingdu && newll.weidu == oldll.weidu) {

                } else {
                    mypolylatitude.add(newll.jingdu);
                    mypolylongtitude.add(newll.weidu);
                    mypolyalti.add(newll.altit);
                    altitude = newll.altit;
                    mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(newll.jingdu, newll.weidu)));
                }
            }
            mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            firstGetLocation = false;
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
            /*
             * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends AdvancedCountdownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
//		Toast.makeText(getActivity(), "计时结束了", Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onTick(long millisUntilFinished, int percent) {
            shijian = (86400000 - millisUntilFinished) / 1000;
            String hour;
            String minute;
            String second;
            if (shijian / 3600 < 10)
                hour = "0" + shijian / 3600;
            else
                hour = "" + shijian / 3600;
            if ((shijian - (shijian / 3600) * 3600) / 60 < 10)
                minute = "0" + (shijian - (shijian / 3600) * 3600) / 60;
            else
                minute = "" + (shijian - (shijian / 3600) * 3600) / 60;
            if (shijian - shijian / 3600 * 3600 - (shijian - (shijian / 3600) * 3600) / 60 * 60 < 10)
                second = "0" + (shijian - shijian / 3600 * 3600 - (shijian - (shijian / 3600) * 3600) / 60 * 60);
            else
                second = "" + (shijian - shijian / 3600 * 3600 - (shijian - (shijian / 3600) * 3600) / 60 * 60);
            startrunsmalltime.setText(hour + ":" + minute + ":" + second);//以1：2：3的形式显示计时
            startrunbigtime.setText(hour + ":" + minute + ":" + second);//以1：2：3的形式显示计时
            if (sportTag == 1) {
                startrunbigdistance.setText((int) (distance / 0.5) + " KM");
                startrunsmalldistance.setText((int) (distance / 0.5) + " KM");
            } else {
                startrunbigdistance.setText(df.format(distance / 1000) + " KM");
                startrunsmalldistance.setText(df.format(distance / 1000) + " KM");

            }
            startrunspeed.setText(df.format((float) (Math.round(SpeedConvert.oriToShow(minSpeed_onAverage) * 100)) / 100) + " ");//m/s to min/km
            startruncal.setText(df.format((float) (Math.round(distance * tizhong * 1.036 / 1000 * 100)) / 100) + " ");
            startrunaltitude.setText("" + df.format(altitude) + "米");
            mMap.addPolyline(polylineops);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

}
