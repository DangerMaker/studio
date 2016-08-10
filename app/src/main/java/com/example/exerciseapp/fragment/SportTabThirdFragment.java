package com.example.exerciseapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.myutils.HintDialog;

import java.util.Iterator;

import butterknife.ButterKnife;

public class SportTabThirdFragment extends Fragment implements com.amap.api.maps2d.LocationSource,
        AMapLocationListener, OnCheckedChangeListener {
    AMap mMap;
    MapView mMapView;
    OnLocationChangedListener mListener;
    LocationManagerProxy mAMapLocationManager;
    ImageView startsport;
    TextView sports_mode;
    int sportsmode = 0;
    RelativeLayout choosesportsmode;
    AMapLocation amapLocation;
    RatingBar myrat;
    LocationManager locationManager;
    float mynum = 0;
    static zuobiao newll = new zuobiao(-100, -100);
    private FragmentManager fragmentManager;
    SportTabThirdFragment thirdinstance;
    private boolean firstGetLocation = true;
    public boolean isinsport = false;

    @Override
    public void onHiddenChanged(boolean hidden) {
        isinsport = hidden == true;
        super.onHiddenChanged(hidden);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_third_sport, container, false);
        ButterKnife.bind(this, view);
        thirdinstance = this;
        mMapView = (MapView) view.findViewById(R.id.mapView);
        startsport = (ImageView) view.findViewById(R.id.startsport);
        sports_mode = (TextView) view.findViewById(R.id.sports_mode);
        myrat = (RatingBar) view.findViewById(R.id.myrat);
        myrat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        choosesportsmode = (RelativeLayout) view.findViewById(R.id.choosesportmode);
        choosesportsmode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isinsport == true) {
                } else {
                    choosetypedialog();
                }
            }
        });
        mMapView.onCreate(savedInstanceState);
        startsport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if ("0".equals(MyApplication.getInstance().getUid())) {
                    hintLogin();
                } else {
                    if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                        new AlertDialog.Builder(getActivity()).setMessage("请打开GPS以获取位置信息").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, 0); //设置完成后返回到原来的界面
                            }
                        }).show();
                    } else {
                        fragmentManager = getActivity().getSupportFragmentManager();

                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        Fragment MyThirdFragmentStartRun = new SportTabRunningThirdFragment();
                        transaction.replace(R.id.lrun_record_content, MyThirdFragmentStartRun);
                        transaction.show(MyThirdFragmentStartRun);
                        transaction.commit();
                        isinsport = true;
                    }
                }
            }
        });
        init();

        setUpMapIfNeeded();

        dogetgps();
        return view;
    }

    public void hintLogin() {

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
        tvHintDialogMessage.setText(this.getString(
                R.string.hintLoginMessage));
        btnCancel.setText(getActivity().getString(R.string.cancel));
        btnToLogin.setText(getActivity().getString(R.string.toLogin));
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
                intent.setClass(getActivity(), AtyWelcome.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    public void dogetgps() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            //GPS模块打开，可以定位操作
            // 通过GPS定位
            String LocateType = LocationManager.GPS_PROVIDER;
            Location location = locationManager.getLastKnownLocation(LocateType);
            // 设置监听器，设置自动更新间隔这里设置1000ms，移动距离：0米。
            locationManager.addGpsStatusListener(statusListener);

            //另外给出 通过network定位设置
        }


    }

    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            // GPS状态变化时的回调，获取当前状态
            GpsStatus status = locationManager.getGpsStatus(null);
            //自己编写的方法，获取卫星状态相关数据
            GetGPSStatus(event, status);
        }
    };

    private void GetGPSStatus(int event, GpsStatus status) {
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            //获取最大的卫星数（这个只是一个预设值）
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            //记录实际的卫星数目
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                //保存卫星的数据到一个队列，用于刷新界面
                GpsSatellite s = it.next();
                count++;
            }
            if (count >= 5)
                mynum = 5;
            else
                mynum = count;
            myrat.setRating(mynum);

        }
    }

    public void choosetypedialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.dialog_fragmet_sport_mode, null);
        final HintDialog dialog = new HintDialog(getActivity(), dialogView, R.style.MyDialogStyle);
        dialog.setCancelable(true);
        final TextView tyepone = (TextView) dialogView.findViewById(R.id.tyepone);
        final TextView typetwo = (TextView) dialogView.findViewById(R.id.typetwo);
        final TextView typethree = (TextView) dialogView.findViewById(R.id.typethree);
        final TextView typefour = (TextView) dialogView.findViewById(R.id.typefour);
        final TextView typefive = (TextView) dialogView.findViewById(R.id.typefive);
        final TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);

        tyepone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sports_mode.setText("模式：跑步");
                sportsmode = 0;
                dialog.dismiss();
            }
        });
        typetwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sports_mode.setText("模式：健步走");
                sportsmode = 1;
                dialog.dismiss();
            }
        });
        typethree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sports_mode.setText("模式：骑行");
                sportsmode = 2;
                dialog.dismiss();
            }
        });
        typefour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sports_mode.setText("模式：登山");
                sportsmode = 3;
                dialog.dismiss();
            }
        });
        typefive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sports_mode.setText("模式：自由模式");
                sportsmode = 4;
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void init() {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        isinsport = false;
        mMapView.onResume();
        setUpMapIfNeeded();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.destroyDrawingCache();
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
            newll.jingdu = amapLocation.getLatitude();
            newll.weidu = amapLocation.getLongitude();
            LatLng newlatlng = new LatLng(newll.jingdu, newll.weidu);
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(newll.jingdu, newll.weidu)));

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
