package com.example.exerciseapp.net;
/**
 * 获取用户已经选过了的喜爱的健身项目Users/getInterest
 * 获取用户未选过的喜爱的健身项目Users/getOtherInterest
 * 
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetInterestConnection {

	public GetInterestConnection(String url, String method,
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
											jsonObj.getJSONArray("data"));
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
		void onSuccess(JSONArray jsonArr);
	}

	public static interface FailCallBack {
		void onFail(String result);
	}
}
