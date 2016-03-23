package com.example.exerciseapp.net;

import org.json.JSONException;
import org.json.JSONObject;


public class SubmitUserInformation {

	public SubmitUserInformation(String url, String method,
			final SuccessCallBack successCallBack,
			final FailCallBack failCallBack, String... kvs) {
		new BaseNetConnection(url, method,
				new BaseNetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObj = new JSONObject(result);
							if (jsonObj.getInt("result") == 1) {
								if (successCallBack != null) {
									successCallBack.onSuccess(
											jsonObj.getString("desc"));
								}
							} else {
								if (failCallBack != null)
									failCallBack.onFail(jsonObj.getString("desc"));
								System.out.println(jsonObj.getString("desc"));
							}
						} catch (JSONException e) {
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
		void onSuccess(String result);
	}

	public static interface FailCallBack {
		void onFail(String result);
	}
}
