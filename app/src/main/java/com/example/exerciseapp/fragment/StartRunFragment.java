package com.example.exerciseapp.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
//import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtySaveRunData;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.example.exerciseapp.aty.sliding.Atyyijianfankui;
import com.example.exerciseapp.fragment.zuobiao;
import com.amap.api.maps2d.model.CameraPosition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.amap.api.maps2d.AMapUtils;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
public class StartRunFragment extends Fragment implements com.amap.api.maps2d.LocationSource,
AMapLocationListener, OnCheckedChangeListener{
	
	
	
	static AMap mMap;
	MapView mMapView;  
	OnLocationChangedListener mListener;
	LocationManagerProxy mAMapLocationManager;
	static PolylineOptions polylineops=new PolylineOptions();
	static double []polyjing=new double [20000];static double []polywei=new double[20000];static int z;
//	AMapLocationClientOption mLocationOption;
//	AMapLocationClient mlocationClient;
	TextView mLocationErrText;
	static zuobiao newll=new zuobiao(-100,-100);
	static zuobiao oldll=new zuobiao();
	LinearLayout linear2mix,linearbuttonline1,linearbuttonline2,linearbuttonline3,linearbuttonline4;
	RelativeLayout linearxiangshang;
	TextView textjuli,textTime,textSpeed,textshunshisudu,textkaluli,textTime_,textSpeed_,textshunshisudu_,textkaluli_;
	LinearLayout linear3choose,linearchoosetargeticon,linearchoosemapicon;
	TextView textchoosetargetiocn,textchoosemapicon,textjieshuyundong;
	RelativeLayout relativegpsxinhao;
	RelativeLayout relativejixupaobu;
	RelativeLayout relativegexiangcanshu,relativemappma;;
	RelativeLayout relativechoosemap,relativechoosetarget;
	LinearLayout linearchoosemap,linearchoosetarget_,linearchoosetarget__,linearchoosetarget___;
	TextView textchoosemap,textpingmianmap,textweixingmap,textquedingmap,textchoosetargetmeiyou,textchoosetargetqueding;
	EditText edittextjuli,edittextkaluli,edittextjishi;
	Button btnduanlian;Button btnjieshuyundong,btnjixuduanlian;
	Button btntongjijuli,btntongjishijian,btnpingjunsudu,btnshunshisudu,btnkaluli;
	ImageButton imgbtnchoosetarget,imgbtnchoosemusic,imgbtnchoosemap,imgbtnshouqi;
	ImageView imgshouqi;
	TimeCount timecount;
	private RequestQueue mRequestQueue;  
	long shijian,shijiantarget;float juli,julitarget,kalulitarget;int tizhong=70;
//	public LatLng oldlatlng,newlatlng;
	int maptag=0;
	final int kaishiduanlian=1;
	final int zantingduanlian=2;
	int runtag=0;//0为初始，1为开始点了开始跑步，2为点了暂停跑步,3为结束
	boolean flagInitial = false;
	/* 定义一个倒计时的内部类 */
	class TimeCount extends AdvancedCountdownTimer{
	
	public TimeCount(long millisInFuture, long countDownInterval) {
	super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
	}
	@Override
	public void onFinish() {//计时完毕时触发
//		Toast.makeText(getActivity(), "计时结束了", Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	public void onTick(long millisUntilFinished, int percent) {
		// TODO Auto-generated method stub//计时过程显示
		shijian=(86400000-millisUntilFinished)/1000;
		textTime.setText(shijian/3600+":"+(shijian-(shijian/3600)*3600)/60+
				":"+(shijian-shijian/3600*3600-(shijian-(shijian/3600)*3600)/60*60));//以1：2：3的形式显示计时
		juli=   (float)(Math.round(juli*100))/100;//取小数点后两位
		textjuli.setText(juli+"");
		textSpeed.setText((float)(Math.round((3.6*juli/shijian)*100))/100+" ");
		textkaluli.setText((float)(Math.round(juli*tizhong*1.036/1000*100))/100+" ");
		mMap.addPolyline(polylineops);
	}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mMapView.destroyDrawingCache();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view=inflater.inflate(R.layout.page_start_running, container, false);
		mMapView=(MapView) view.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		setUpMapIfNeeded();
		init();
//		mMap.setMapType(AMap.MAP_TYPE_SATELLITE);卫星地图，标准地图是MAP_TYPE_NORMAL
		
//		mMapController=mMapView.getController();
//		mMap.addPolyline((new PolylineOptions()).
//				add(new LatLng(27.5987511651650165,99.99916041604604),new LatLng(37.123514064049840,111.11119014901560)));
		
		
//		Config.getCachedUserUid(getActivity());     //获取UID
		mRequestQueue =  Volley.newRequestQueue(getActivity());
		StringRequest  stringRequest = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL+"Users/briefUserInfoNew",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
//                        	progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(s);
                            if(jsonObject.getString("result").equals("1")){
//                            	Toast.makeText(getApplicationContext(), "上传成功", 2).show();
                            	if(jsonObject.getJSONObject("data").getString("weight")!=null)
                            	    tizhong=Integer.parseInt(jsonObject.getJSONObject("data").getString("weight"));
                            	else tizhong=75;
//                            	Toast.makeText(getActivity(), tizhong+" ", 2).show();
//                            	Config.cacheUserUid(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_UID));
//                            	startActivity(new Intent(AtyLogin.this,AtyPersonalize.class));
//                            	Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), 2).show();
//                            	Config.STATUS_FINISH_ACTIVITY = 1;
//                            	finish();
                            }else{
                            	Toast.makeText(getActivity(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                            }
                            
                        } catch (JSONException e) {
//                        	progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                    	progressDialog.dismiss();
//                    	Toast.makeText(getApplicationContext(), "error", 2).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(Config.KEY_UID, "1");
//                map.put(Config.KEY_CONTENT, edittextyijianfankui.getText().toString());
//                map.put(Config.KEY_TEL,etPhoneNum.getText().toString());
//                map.put(Config.KEY_PASSWORD,etPassword.getText().toString());
                return map;
            }
        };
        mRequestQueue.add(stringRequest);
//        Toast.makeText(getActivity(), tizhong+" ", 2).show();
		relativegexiangcanshu=(RelativeLayout) view.findViewById(R.id.relativegexiangcanshu);
//		relativemappma=(RelativeLayout) view.findViewById(R.id.relativemappam);

		textjuli=(TextView) view.findViewById(R.id.texttongjijuli);
		linearbuttonline1=(LinearLayout) view.findViewById(R.id.linearbuttonline1);
		linearbuttonline2=(LinearLayout) view.findViewById(R.id.linearbuttonline2);
//		linearbuttonline3=(LinearLayout) view.findViewById(R.id.linearbuttonline3);
//		linearbuttonline4=(LinearLayout) view.findViewById(R.id.linearbuttonline4);
		textTime=(TextView) view.findViewById(R.id.texttongjishijian);
//		textTime_=(TextView) view.findViewById(R.id.texttongjishijian_);
//		timecount=new TimeCount(86400000, 1000);
		textSpeed=(TextView) view.findViewById(R.id.textpingjunsudu);
		textSpeed_=(TextView) view.findViewById(R.id.textpingjunsudu_);
//		textshunshisudu=(TextView) view.findViewById(R.id.textshunshisudu);
//		textshunshisudu_=(TextView) view.findViewById(R.id.textshunshisudu_);
		textkaluli=(TextView) view.findViewById(R.id.textkaluli);
		textkaluli_=(TextView) view.findViewById(R.id.textkaluli_);
//		imgbtnshouqi=(ImageButton) view.findViewById(R.id.imgbtnshouqi);
		linearxiangshang=(RelativeLayout) view.findViewById(R.id.linearxiangshang);
		imgshouqi=(ImageView) view.findViewById(R.id.imgshouqi);
		imgshouqi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				relativegexiangcanshu.setVisibility(View.INVISIBLE);
				linear3choose.setVisibility(View.VISIBLE);
				relativegpsxinhao.setVisibility(View.VISIBLE);
			}
		});
		linear3choose=(LinearLayout) view.findViewById(R.id.linear3choose);
		linearchoosemapicon=(LinearLayout) view.findViewById(R.id.linearchoosemapicon);
		textchoosemapicon=(TextView) view.findViewById(R.id.textchoosemapicon);
		linearchoosetargeticon=(LinearLayout) view.findViewById(R.id.linearchoosetargeticon);
		textchoosetargetiocn=(TextView) view.findViewById(R.id.textchoosetargeticon);
	//	imgbtnchoosetarget=(ImageButton) view.findViewById(R.id.imgbtnchoosetarget);
		linearchoosetargeticon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				relativechoosetarget.setVisibility(View.VISIBLE);
			}
		});
//		imgbtnchoosemusic=(ImageButton) view.findViewById(R.id.imgbtnchoosemusic);
//		imgbtnchoosemusic.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getActivity().getApplicationContext(), "选择音乐", Toast.LENGTH_SHORT).show();
//			} 
//		});
	//	imgbtnchoosemap=(ImageButton) view.findViewById(R.id.imgbtnchoosemap);
		linearchoosemapicon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				relativechoosemap.setVisibility(View.VISIBLE);
			}
		});
		relativegpsxinhao=(RelativeLayout) view.findViewById(R.id.relativegpsxinhao);
		relativegpsxinhao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linear3choose.setVisibility(View.INVISIBLE);
				relativegpsxinhao.setVisibility(View.INVISIBLE);
				relativegexiangcanshu.setVisibility(View.VISIBLE);
			}
		});
		relativejixupaobu=(RelativeLayout) view.findViewById(R.id.relativejixuliebiao);
//		linear2mix=(LinearLayout) view.findViewById(R.id.linear2mix);
		btnduanlian=(Button) view.findViewById(R.id.btnduanlian);
		btnduanlian.setTag(kaishiduanlian);
		btnduanlian.setText("开始锻炼");
		btnduanlian.setBackgroundResource(R.drawable.shape_btn_blue_radius_5dip);
		btnduanlian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tag=(Integer) v.getTag();
				switch(tag){
				case kaishiduanlian:
					runtag=1;
					juli=0;
                    z=0;
                    flagInitial = true;
					btnduanlian.setTag(zantingduanlian);
					btnduanlian.setText("暂停锻炼");
					btnduanlian.setBackgroundResource(R.drawable.shape_btn_red_radius_5dip);
					relativegexiangcanshu.setVisibility(View.VISIBLE);
					linear3choose.setVisibility(View.INVISIBLE);
					relativegpsxinhao.setVisibility(View.INVISIBLE);
					timecount=new TimeCount(86400000, 1000);
					timecount.start();
					break;
				case zantingduanlian:
					runtag=2;
					relativejixupaobu.setVisibility(View.VISIBLE);
					btnduanlian.setVisibility(View.INVISIBLE);
					timecount.pause();
					break;
				}
			}
		});
		textjieshuyundong=(TextView) view.findViewById(R.id.textjieshuyundong);
//		btnjieshuyundong=(Button) view.findViewById(R.id.btnjieshuyundong);
		textjieshuyundong.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				runtag=3;
				btnduanlian.setTag(kaishiduanlian);
				btnduanlian.setText("开始锻炼");
				btnduanlian.setBackgroundResource(R.drawable.shape_btn_blue_radius_5dip);
				btnduanlian.setVisibility(View.VISIBLE);
				relativejixupaobu.setVisibility(View.INVISIBLE);
				relativegexiangcanshu.setVisibility(View.INVISIBLE);
				linear3choose.setVisibility(View.VISIBLE);
				relativegpsxinhao.setVisibility(View.VISIBLE);
				timecount.cancel();//onFinish();
				mMap.clear();
				MyLocationStyle myLocationStyle = new MyLocationStyle();
    			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
    					.fromResource(R.drawable.yuanquan_icon));// 设置小蓝点的图标
    			mMap.setMyLocationStyle(myLocationStyle);
				Intent i=new Intent(getActivity(), AtySaveRunData.class);
				i.putExtra("intentjuli", juli);
				i.putExtra("intentshijian", shijian);
				i.putExtra("intentkaluli", (float)(Math.round(juli*tizhong*1.036/1000*100))/100);
//				i.putExtra("intentpolyline", polylineops);
				i.putExtra("z", z);
				i.putExtra("intentpolyjing", polyjing);
				i.putExtra("intentpolywei", polywei);
				startActivity(i);
			}
		});
		btnjixuduanlian=(Button) view.findViewById(R.id.btnjixuduanlian);
		btnjixuduanlian.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				runtag=1;
				relativejixupaobu.setVisibility(View.INVISIBLE);
				btnduanlian.setVisibility(View.VISIBLE);
				timecount.resume();
			}
		});
		relativechoosemap=(RelativeLayout) view.findViewById(R.id.relativechoosemap);
		textchoosemap=(TextView) view.findViewById(R.id.textchoosemap);
		linearchoosemap=(LinearLayout) view.findViewById(R.id.linearchoosemap);
		textpingmianmap=(TextView) view.findViewById(R.id.textpingmianmap);
		textpingmianmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				maptag=1;
				textpingmianmap.setTextColor(Color.rgb(92, 174, 196));
				textweixingmap.setTextColor(Color.rgb(1, 1, 1));
			}
		});
		textweixingmap=(TextView) view.findViewById(R.id.textweixingmap);
		textweixingmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				maptag=2;
				textpingmianmap.setTextColor(Color.rgb(1, 1, 1));
				textweixingmap.setTextColor(Color.rgb(92, 174, 196));
			}
		});
		textquedingmap=(TextView) view.findViewById(R.id.textquedingmap);
		textquedingmap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(maptag==1){
					mMap.setMapType(AMap.MAP_TYPE_NORMAL);
					textchoosemapicon.setText("平面地图");
				}else if(maptag==2){
					mMap.setMapType(AMap.MAP_TYPE_SATELLITE);
					textchoosemapicon.setText("卫星地图");
				}
				relativechoosemap.setVisibility(View.INVISIBLE);
			}
		});
		relativechoosetarget=(RelativeLayout) view.findViewById(R.id.relativechoosetarget);
		linearchoosetarget_=(LinearLayout) view.findViewById(R.id.linearchoosetarget_);
		linearchoosetarget__=(LinearLayout) view.findViewById(R.id.linearchoosetarget__);
		edittextjuli=(EditText) view.findViewById(R.id.edittextjuli);
		edittextkaluli=(EditText) view.findViewById(R.id.edittextkaluli);
		edittextjishi=(EditText) view.findViewById(R.id.edittextjishi);
		linearchoosetarget___=(LinearLayout) view.findViewById(R.id.linearchoosetarget___);
		textchoosetargetmeiyou=(TextView) view.findViewById(R.id.textchoosetargetmeiyou);
		textchoosetargetmeiyou.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity().getApplicationContext(), "没有", Toast.LENGTH_SHORT).show();
				relativechoosetarget.setVisibility(View.INVISIBLE);
			}
		});
		textchoosetargetqueding=(TextView) view.findViewById(R.id.textchoosetargetqueding);
		textchoosetargetqueding.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity().getApplicationContext(), "确定", Toast.LENGTH_SHORT).show();
				String s1=edittextjuli.getText().toString();
				String s3=edittextjishi.getText().toString();
				String s2=edittextkaluli.getText().toString();
				String s4=" ";
				if(s1.length()!=0){
					s4+=s1+"km";
				}
				if(s2.length()!=0){
					s4+=s2+"大卡";
				}
				if(s3.length()!=0){
					s4+=s3+"分钟";
				}
				if(s4.length()>2){
					textchoosetargetiocn.setText(s4);
				}
				relativechoosetarget.setVisibility(View.INVISIBLE);
			}
		});
//		getActivity().getActionBar().getCustomView().findViewById(R.id.tvPageTitleOfAll).setOnClickListener(new 
//				OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
		
		
		return view;
	}
	void init(){
    	if(mMap==null){
    		setUpMapIfNeeded();
    	}
   // 	mMapController=mMapView.getController();
    	// 自定义系统定位小蓝点
    			MyLocationStyle myLocationStyle = new MyLocationStyle();
    			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
    					.fromResource(R.drawable.yuanquan_icon));// 设置小蓝点的图标
    			mMap.setMyLocationStyle(myLocationStyle);
    	//		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
    	//		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
    			// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
    	//		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
    	mMap.setLocationSource(this);// 设置定位监听
		mMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示

		mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//		mMap.setMyLocationType(2);
		//设置zoom大小
		   mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
		

		
}
	@Override
	public void onResume() {
		super.onResume();
//		((SupportMapFragment) getFragmentManager()
//				.findFragmentById(R.id.map)).onResume();
		mMapView.onResume();
		setUpMapIfNeeded();
		
	}
	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	private void setUpMapIfNeeded() {
		if (mMap == null) {mMap=mMapView.getMap();
//			mMap = ((SupportMapFragment) getFragmentManager()
//					.findFragmentById(R.id.map)).getMap();
		}
	}
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (mListener != null && amapLocation != null) {
			if(flagInitial){
				oldll.jingdu = amapLocation.getLatitude();
	    		oldll.weidu = amapLocation.getLongitude();
	    		flagInitial = false;
			}else{
				oldll.jingdu = newll.jingdu;
	    		oldll.weidu = newll.weidu;
			}
			newll.jingdu=amapLocation.getLatitude();
			newll.weidu=amapLocation.getLongitude();
			LatLng newlatlng=new LatLng(newll.jingdu, newll.weidu);
			LatLng oldlatlng=new LatLng(oldll.jingdu, oldll.weidu);
			
			if(runtag==1&&oldll.jingdu!=0.0&&newll.jingdu!=0.0){
				juli+= AMapUtils.calculateLineDistance(oldlatlng, newlatlng);
				mMap.addPolyline((new PolylineOptions()).
						add(new LatLng(oldll.jingdu,oldll.weidu),new LatLng(newll.jingdu,newll.weidu)));
				polylineops.add(newlatlng);
				polyjing[z]=newll.jingdu;polywei[z]=newll.weidu;z++;
				 mMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(newll.jingdu, newll.weidu)));
			}
//			if(runtag == 3){
//				mMapView.onResume();
//			}
			mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//			if (amapLocation != null
//					&& amapLocation.getErrorCode() == 0) {
//	//			Toast.makeText(getActivity(), "位置改變了", Toast.LENGTH_SHORT).show();
//				oldll.jingdu = newll.jingdu;
//	    		oldll.weidu = newll.weidu;
//				newll.jingdu=amapLocation.getLatitude();
//				newll.weidu=amapLocation.getLongitude();
//				LatLng newlatlng=new LatLng(newll.jingdu, newll.weidu);
//				LatLng oldlatlng=new LatLng(oldll.jingdu, oldll.weidu);
//				juli+=AMapUtils.calculateLineDistance(oldlatlng, newlatlng);
//				
//				if(runtag==1){
//					mMap.addPolyline((new PolylineOptions()).
//							add(new LatLng(oldll.jingdu,oldll.weidu),new LatLng(newll.jingdu,newll.weidu)));
//				}
//				mLocationErrText.setVisibility(View.GONE);
//				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//			} else {
//				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
//				Log.e("AmapErr",errText);
//				mLocationErrText.setVisibility(View.VISIBLE);
//				mLocationErrText.setText(errText);
//			}
		}
	}
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
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
		
//		if (mlocationClient == null) {
//			mlocationClient = new AMapLocationClient(getActivity());
//			mLocationOption = new AMapLocationClientOption();
//			//设置定位监听
//			mlocationClient.setLocationListener(this);
//			//设置为高精度定位模式
//			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
//			//设置定位参数
//			mlocationClient.setLocationOption(mLocationOption);
//			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//			// 在定位结束后，在合适的生命周期调用onDestroy()方法
//			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//			mlocationClient.startLocation();
//		}
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
//		if (mlocationClient != null) {
//			mlocationClient.stopLocation();
//			mlocationClient.onDestroy();
//		}
//		mlocationClient = null;
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}
