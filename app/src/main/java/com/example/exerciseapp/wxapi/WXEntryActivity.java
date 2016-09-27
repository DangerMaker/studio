package com.example.exerciseapp.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MainActivity;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.TabMainActivity;
import com.example.exerciseapp.aty.login.AtyAdvertisement;
import com.example.exerciseapp.utils.LocationPro;
import com.example.exerciseapp.utils.SharedPreferencesHelper;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.message.proguard.aa;

import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private Button gotoBtn, regBtn, launchBtn, checkBtn;
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	private RequestQueue mRequestQueue;
	public String openid;
	public String access_token;
	public String unionid;
	public String code;
	public String nickname;
	public String sex;
	public String province;
	public String city;
	public String country;
	public String headimgurl;
	public boolean isauthor = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID, false);


		api.registerApp(Config.WxAPP_ID);

        
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
			case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
				break;
			case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
				break;
			default:
				break;
		}
	}
	@Override
	public void onResp(BaseResp resp) {

		String result;
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			code = "" + ((SendAuth.Resp) resp).code;
			String state = ((SendAuth.Resp) resp).state;
			if ("myapp_for_readyauthor".equals(state))
				isauthor = true;
			else
				isauthor = false;
			dogetToken();
		} else {
			switch (resp.errCode) {
				case BaseResp.ErrCode.ERR_OK:
					result = "发送成功";
					break;
				case BaseResp.ErrCode.ERR_USER_CANCEL:
					result = "发送取消";
					break;
				case BaseResp.ErrCode.ERR_AUTH_DENIED:
					result = "权限不足";
					break;
				default:
					result = "未知错误";
					break;
			}
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
		finish();
	}

	public void dogetToken() {
		mRequestQueue = Volley.newRequestQueue(this);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + com.example.exerciseapp.wxapi.Constants.APP_ID +
				"&secret=" + com.example.exerciseapp.wxapi.Constants.APP_Secret +
				"&code=" + code +
				"&grant_type=authorization_code";
		StringRequest stringRequest = new StringRequest(
				Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (null != jsonObject) {
								openid = jsonObject.getString("openid");
								access_token = jsonObject.getString("access_token");
								unionid = jsonObject.getString("unionid");
								dogetuser();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				}) {

		};
		mRequestQueue.add(stringRequest);
	}

	public void dogetuser() {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ access_token + "&openid=" + openid;
		StringRequest stringRequest = new StringRequest(
				Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);

							nickname = jsonObject.getString("nickname");
							sex = jsonObject.getString("sex");
							province = jsonObject.getString("province");
							city = jsonObject.getString("city");
							country = jsonObject.getString("country");
							headimgurl = jsonObject.getString("headimgurl");
							unionid = jsonObject.getString("unionid");
							if (isauthor == false)

								dologin();
							else {
								dogetbind();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				}) {

		};
		mRequestQueue.add(stringRequest);
	}

	public void dogetbind() {
		String url = "http://101.200.214.68/py/bind?action=treat_bind_wecha" +
				"&nickname=" + nickname +
				"&sex=1" + sex +
				"&province=" + province +
				"&city=" + city +
				"&country=" + country +
				"&avatar=" + headimgurl +
				"&unionid=" + unionid +
				"&version=3.0" +
				"&uid=" + MyApplication.getInstance().getUid() +
				"&token=" + MyApplication.getInstance().getToken();
		StringRequest stringRequest = new StringRequest(
				Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.getString("result").equals("1")) {
								WXEntryActivity.this.finish();
							} else {
								Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				}) {

		};
		mRequestQueue.add(stringRequest);
	}

	public void dologin() {
		String url = "http://101.200.214.68/py/third?action=wecha" +
				"&nickname=" + nickname +
				"&sex=1" + sex +
				"&province=" + province +
				"&city=" + city +
				"&country=" + country +
				"&avatar=" + headimgurl +
				"&unionid=" + unionid;
		StringRequest stringRequest = new StringRequest(
				Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);

							if (jsonObject.getString("result").equals("1")) {
								Config.cacheUserUid(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_UID));
								Config.cacheUserToken(getApplicationContext(), jsonObject.getJSONObject("data").getString(Config.KEY_TOKEN));

								String uid = jsonObject.getJSONObject("data").getString(Config.KEY_UID);
								String token = jsonObject.getJSONObject("data").getString(Config.KEY_TOKEN);
								String bind_layer_show = jsonObject.getJSONObject("data").getString("bind_layer_show");
								MyApplication.getInstance().setUid(uid);
								MyApplication.getInstance().setToken(token);
								SharedPreferencesHelper.getInstance(WXEntryActivity.this).setValue("uid", uid);
								SharedPreferencesHelper.getInstance(WXEntryActivity.this).setValue("token", token);
								((MyApplication) getApplication()).setUid(uid);
								((MyApplication) getApplication()).setToken(token);
								finish();


								if (LocationPro.getInstances(WXEntryActivity.this).getLocation().equals("房山区")) {
									Intent intent = new Intent(WXEntryActivity.this, AtyAdvertisement.class);
									intent.putExtra("bind_layer_show", "" + bind_layer_show);
									startActivity(intent);
								} else {
									Intent intent = new Intent(WXEntryActivity.this, TabMainActivity.class);
									intent.putExtra("showAd",true);
									intent.putExtra("bind_layer_show", "" + bind_layer_show);
									startActivity(intent);
								}
								Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
								Config.STATUS_FINISH_ACTIVITY = 1;
								Config.TOURIST_MODE = false;
								MainActivity.mPushAgent.addAlias(jsonObject.getJSONObject("data").getString(Config.KEY_UID), "userId");
							} else {
								Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (aa.e e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				}) {

		};
		mRequestQueue.add(stringRequest);
	}
}