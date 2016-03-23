package com.example.exerciseapp.net;
/*
 * 上传头像
 */
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateHWConnection {

	public UpdateHWConnection(String url, String method,
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
											jsonObj.getString("fileurl"),jsonObj.getString("desc"));
								}
							} else {
								if (failCallBack != null)
									failCallBack.onFail(jsonObj.getString("desc"));
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
		void onSuccess(String fileurl, String desc);
	}

	public static interface FailCallBack {
		void onFail(String result);
	}
}
