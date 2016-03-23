package com.example.exerciseapp.net;

/*
 * 获取验证码通信和将用户所选择的多个兴趣id和用户id返回给服务器做处理
 * 登录
 * 填写身高体重
 * updateuserinfonew 将用户填写的个人信息上传给服务器
 * 
 * 服务器传回用户曾经填写过的个人信息
 * 
 */
import org.json.JSONException;
import org.json.JSONObject;

public class UserGetCodeConnection {

	public UserGetCodeConnection(String url, String method,
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
