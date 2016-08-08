//package com.example.exerciseapp.pulltofresh.task;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//
//
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.BaseAdapter;
//
//import com.example.exerciseapp.pulltofresh.PullToRefreshListView;
//
//public class DiscountTask{
//
//	private PullToRefreshListView pullToRefreshListView;  //实现下拉刷新与上拉加载的ListView
//	private int pullState;               //记录判断，上拉与下拉动作
//	private BaseAdapter baseAdapter;     //ListView适配器，用于提醒ListView数据已经更新
//	private  LinkedList<HashMap<String,Object>> linkedList;
//	private String dealer_id = "";
//	private Context context;
//	private ProgressDialog progress;
//	public DiscountTask(PullToRefreshListView pullToRefreshListView, int pullState,
//			BaseAdapter baseAdapter, LinkedList<HashMap<String,Object>> linkedList) {
//
//		this.pullToRefreshListView = pullToRefreshListView;
//		this.pullState = pullState;
//		this.baseAdapter = baseAdapter;
//		this.linkedList = linkedList;
//	}
//	public DiscountTask(Context context,PullToRefreshListView pullToRefreshListView, int pullState,
//			BaseAdapter baseAdapter, LinkedList<HashMap<String,Object>> linkedList,String dealer_id) {
//		this.context = context;
//		this.pullToRefreshListView = pullToRefreshListView;
//		this.pullState = pullState;
//		this.baseAdapter = baseAdapter;
//		this.linkedList = linkedList;
//		this.dealer_id = dealer_id;
//	}
//	public void download(){
////		progress = ProgressDialog.show(context, "��ʾ", "������...");
//		AjaxParams params = new AjaxParams();
//		params.put("limit",""+DiscountActivity.limit);
//		if(pullState == 1){
//			//����ˢ��
//			params.put("fresh_type", ""+1);
//			params.put("flag_id", ""+DiscountActivity.max_id);
//		}else if(pullState == 2){
//			//��������
//			params.put("fresh_type", ""+0);
//			params.put("flag_id", ""+DiscountActivity.min_id);
//		}
//		if(!TextUtils.isEmpty(dealer_id)){
//			params.put("id", dealer_id);
//		}
//		RbfLog.log("ˢ�����",params.toString());
//		String url = "";
//		if(TextUtils.isEmpty(DiscountActivity.dealer_id)){
//			url = Common.getDiscountActivityList;
//		}else{
//			url = Common.getDealerActivityList;
//		}
//		Common.fhttp.post(url, params, new AjaxCallBack<String>() {
//			@Override
//			public void onFailure(Throwable t, int errorNo, String strMsg) {
//				// TODO Auto-generated method stub
//				pullToRefreshListView.onRefreshComplete();
//				if(progress!=null && progress.isShowing()){
//					progress.dismiss();
//				}
//				RbfLog.log("fail", ""+errorNo+t);
//				super.onFailure(t, errorNo, strMsg);
//			}
//
//			@Override
//			public void onSuccess(String t) {
//				// TODO Auto-generated method stub
//				JSONObject result = JSONObject.parseObject(t);
//				if(result.getBooleanValue("is_success")){
//					JSONObject resultObject = (JSONObject) result.get("resData");
//					JSONArray body = resultObject.getJSONArray("activity_list");
//					if(!body.isEmpty()){
//						JSONObject object = null;
//						HashMap<String, Object > map;
//						for(int i=0;i<body.size();i++){
//							object = body.getJSONObject(i);
//							map = new HashMap<String, Object>();
//							map.put("url", object.get("image"));
//							map.put("discountTitle",object.get("title"));
//							map.put("discountAddr",object.get("dealername"));
//							map.put("dealer_id", object.get("dealer_id"));
//							int starttime = object.getIntValue("starttime");
//							int endtime = object.getIntValue("endtime");
//							String time = CommonUtils.getDate(CommonUtils.getRealTime(starttime))+"-"+CommonUtils.getDate(CommonUtils.getRealTime(endtime));
//							map.put("discountTime", time);
//							map.put("discountScan",object.get("browse"));
//							map.put("discountId", object.get("id"));
//							int id = Integer.parseInt(object.get("id").toString());
//
//							DiscountActivity.max_id=(DiscountActivity.max_id<id)?id:DiscountActivity.max_id;
//							DiscountActivity.min_id=(DiscountActivity.min_id>id)?id:DiscountActivity.min_id;
//							if(pullState == 1){
//								linkedList.addFirst(map);
//							}else if(pullState == 2){
//								linkedList.addLast(map);
//							}
//						}
//						baseAdapter.notifyDataSetChanged();
//						pullToRefreshListView.onRefreshComplete();
//					}
//					pullToRefreshListView.onRefreshComplete();
//				}else{
//					pullToRefreshListView.onRefreshComplete();
//				}
//				if(progress!=null && progress.isShowing()){
//					progress.dismiss();
//				}
//			}
//		});
//	}
//}
