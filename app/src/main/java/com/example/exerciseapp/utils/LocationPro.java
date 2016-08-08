package com.example.exerciseapp.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

/**
 * Created by Cherie_No.47 on 2016/4/22 14:07.
 * Email jascal@163.com
 */
public class LocationPro {
    public static LocationPro local;//全局静态实例.
    private LocationManagerProxy mLocationManagerProxy;//高德地图api管理接口
    private LocationListener listener;
    private String location;//返回的位置字段和经纬度
    private Context context;

    public LocationPro(Context context) {
        super();
        this.context = context;
    }

    public static LocationPro getInstances(Context context) {
        // 获取定位插件的单实例
        if (null == local) {
            local = new LocationPro(context);
        }
        return local;
    }

    public String getLocal() {//定位方法,注意该方法并不一定能立刻获取位置信息.
        mLocationManagerProxy = LocationManagerProxy.getInstance(context);
        mLocationManagerProxy.setGpsEnable(true);
        listener = new LocationListener();
        location = "";//每次都初始化为空

        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        //在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除

        onGetLocation l = new onGetLocation() {//构造一个监听器类。
            @Override
            public void get() {//成功的时候将定位信息写入sp中
                SharedPreferencesHelper.getInstance(context).setValue("Location", location);
                mLocationManagerProxy.destroy();
            }

            @Override
            public void fail() {//失败的时候作为0默认值
                SharedPreferencesHelper.getInstance(context).setValue("Location", "");
                mLocationManagerProxy.destroy();
            }
        };

        listener.L = l;//设置定位服务的监听
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 17, listener);//开启定位服务
        return location;
    }

    public class LocationListener implements AMapLocationListener {//定位监听器,用于监听位置定位.
        public onGetLocation L;//设置一个监听对象,用于定位成功或者失败.

        @Override
        public void onLocationChanged(Location arg0) {
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }

        @Override
        public void onLocationChanged(AMapLocation arg0) {
            if (arg0 != null && arg0.getAMapException().getErrorCode() == 0) {
                // 只取城区
                location = arg0.getDistrict();
                mLocationManagerProxy.removeUpdates(listener);//注销监听器回收资源.
                L.get();//定位成功回调,用于把信息传递出去
            } else if (arg0 != null && arg0.getAMapException().getErrorCode() != 0) {
                L.fail();//定位失败.
            }
        }
    }

    public interface onGetLocation {//监听器,代码的关键部分.在定位失败或者成功的时候调用监听器类的回调方法
        public abstract void get();//定位成功时调用
        public abstract void fail();//定位失败
    }

    public String getLocation() {
        String lo = SharedPreferencesHelper.getInstance(context).getValue("Location");//位置.
        return lo;
    }
}