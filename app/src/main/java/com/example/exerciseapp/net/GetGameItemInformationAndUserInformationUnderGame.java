package com.example.exerciseapp.net;

import org.json.JSONException;
import org.json.JSONObject;


public class GetGameItemInformationAndUserInformationUnderGame {

	public GetGameItemInformationAndUserInformationUnderGame(String url,String method,final SuccessCallBack successCallBack,
			final FailCallBack failCallBack, String... kvs) {
		new BaseNetConnection(url, method, new BaseNetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject jsonObj = new JSONObject(result);
					if(jsonObj!=null){
						if(jsonObj.getInt("result") == 1){
							if (successCallBack != null) {
								successCallBack.onSuccess(jsonObj.getJSONObject("data"));
							}
						}
					}
				} catch (JSONException e) {
					if (failCallBack != null)
						failCallBack.onFail("Connection error!");
					e.printStackTrace();
				}
			}
		}, new BaseNetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				if (failCallBack != null)
					failCallBack.onFail("Connection error!");
			}
		}, kvs);
	}
	
	public static interface SuccessCallBack {
		void onSuccess(JSONObject result);
	}

	public static interface FailCallBack {
		void onFail(String result);
	}
}
