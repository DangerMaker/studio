package com.example.exerciseapp.net;

/*
 * 用戶信息簡介
 */

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class BriefUserInformaitonConnection {
	
	
	
	public BriefUserInformaitonConnection(String url, String method,
			final SuccessCallBack successCallBack,
			final FailCallBack failCallBack, String... kvs) {
		
		
		new BaseNetConnection(url, method,
				new BaseNetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
							Map<String,String> map = new HashMap<String, String>();
							try {
								JSONObject jsonObj = new JSONObject(result);
//								if(jsonObj!=null){
//									map.put("username", jsonObj.getString("username"));
//									map.put("assoc", jsonObj.getString("assoc"));
//									map.put("height", jsonObj.getString("height"));
//									map.put("weight", jsonObj.getString("weight"));
//									map.put("avatar", jsonObj.getString("avatar"));
//									map.put("sex", jsonObj.getString("sex"));
//									map.put("bmi", jsonObj.getString("bmi"));
//									map.put("birthday", jsonObj.getString("birthday"));
//									map.put("tz", jsonObj.getString("tz"));
//								}
								if (successCallBack != null) {
									successCallBack.onSuccess(jsonObj);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
				}}, new BaseNetConnection.FailCallback() {

					@Override
					public void onFail() {
						if (failCallBack != null)
							failCallBack.onFail("Connection error!");
					}
				}, kvs);
	}

	public static interface SuccessCallBack {
		void onSuccess(JSONObject jsonObj);
	}

	public static interface FailCallBack {
		void onFail(String result);
	}
}
