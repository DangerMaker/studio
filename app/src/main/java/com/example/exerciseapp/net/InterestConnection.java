package com.example.exerciseapp.net;

/*
 * getInterest和getOtherInterest
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InterestConnection {

	public InterestConnection(String url, String method,
			final SuccessCallBack successCallBack,
			final FailCallBack failCallBack, String... kvs) {
		new BaseNetConnection(url, method,
				new BaseNetConnection.SuccessCallback() {

					@SuppressWarnings("null")
					@Override
					public void onSuccess(String result) {
						try {
							List<Map<String, String>> list = null;
							JSONArray jsonArr = new JSONArray(result);
							if (jsonArr!=null) {
								JSONObject jsonObj;
								if (successCallBack != null) {
									List<Map<String, String>> list2 = list;
									for (int i = 0; i < jsonArr.length(); i++) {
										Map<String, String> map = new HashMap<String, String>();
										jsonObj = (JSONObject) jsonArr.get(i);
										map.put("iid",jsonObj.getString("iid"));
										map.put("iname", jsonObj.getString("iname"));
										list2.add(map);
									}
									successCallBack.onSuccess(list2);
								}
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
		// TODO Auto-generated constructor stub
	}

	public static interface SuccessCallBack {
		void onSuccess(List<Map<String, String>> list);
	}

	public static interface FailCallBack {
		void onFail(String result);
	}
}
