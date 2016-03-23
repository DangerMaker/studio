package com.example.exerciseapp.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameListConnection {

	public GameListConnection(String url, String method,
			final SuccessCallBack successCallBack,
			final FailCallBack failCallBack, String... kvs) {
		new BaseNetConnection(url, method,
				new BaseNetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObj = new JSONObject(result);
							JSONArray jsonArr = jsonObj.getJSONArray("data");
							successCallBack.onSuccess(jsonArr);
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
		void onSuccess(JSONArray result);
	}

	public static interface FailCallBack {
		void onFail(String result);
	}
}
