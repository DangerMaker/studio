package com.example.exerciseapp.aty.sliding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;

import com.example.exerciseapp.alipay.sdk.pay.PayResult;
import com.example.exerciseapp.alipay.sdk.pay.SignUtils;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.umeng.message.PushAgent;

public class AtyPay extends BaseActivity {

	public static Activity instance;
	// 商户PID
	public static final String PARTNER = "2088121458287895";
	// 商户收款账号
	public static final String SELLER = "2088121458287895";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = 
		"MIICXgIBAAKBgQC6Uf3+klO2U3/HJDKWcoZTM6p53xhuwcC8nzFrsPqnyXLqzUvM\n"+
		"w6PKEQZBB1ORSgUO+jLKfUqR+xJZFddcL+Sxa6RuvPjhP6brp7w+75hktSotX1bm\n"+
		"7qX07TeS2ItL+ckluKPzOHvK7C6CH15GtbXbSC5GNlM8Q0Flt75Ume2rhwIDAQAB\n"+
		"AoGBAKztUIooEV4VnXCc3f7YiGdMeprmPuz9Ev992QaUme+Efw4CQKnpomj26BEO\n"+
		"1bbxNkSyXtWk98MJffwE038STHPvJSlrmXHAI1sFgr7wUOVRYnKkr0p9BORkuJ6p\n"+
		"NaO9zSlHQU+5H6jSTwlJPuL8DCZhMpOvVJV9UKTpjP77Xz5hAkEA82CwFDRZ0VB4\n"+
		"QDa5PA5fcZcsqGw3nQrBHjNk66byRfhP5Z1P+D+EMV26I6vN4QQz38giu0qbT2Vv\n"+
		"MJcaO+a9SQJBAMP7wZ52CfzKPqx+wtuqdF1f7PIRA1LaylB49RMY+OmXUHKxsWyQ\n"+
		"ee7paZA9PbyDppgUb2GUBK7YDmydByu5Mk8CQQCLwOy7XWhqIpw6u3HYw6QY9SdG\n"+
		"u4Wf3yoP7pE4JJCgjztJ7fCa69Y8Bsc5bpF1cdVb3m/bPrc6e1PDAjGlThVJAkAC\n"+
		"hVuBOU15lfRmbZ7+ClxC0zgOCQ/84dmWXWfxnfGdt5eOUiDRH66h1xjHhTIlGaBr\n"+
		"9l9UZv4Ebb0Jwp9T/abbAkEAujIXroVcFyK6i3I99Trx0v+H5TKVOgRECSAVpZqz\n"+
		"MuktyCgAys3Ej8o5XkyekwgJzOY53eNMOV/rLfakOyfPdQ==\n";
		// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	
	private String subject = "名称";	//商品名称
	private String body = "描述";	//商品描述
	private String total_fee = "0.01";	//商品价格
	
	private String out_trade_no;	//编号id
	private String notify_url;		//回调地址
	private String paytype = "0";	//支付类型
	
	private ActionBar actionBar;
	
	private TextView tvGameNameAtyPay;
	private TextView tvGameItemAtyPay;
	private TextView tvPayFeeAtyPay;
	private ImageView tvZhiFuPayAtyPay;
	private Intent intent;
	private RequestQueue mRequestQueue; 
	
	private Toolbar toolbar;
	private TextView pageTitle;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(AtyPay.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					instance.finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(AtyPay.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(AtyPay.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(AtyPay.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
	
	protected void pay() {
		StringRequest  stringRequest = new StringRequest(
	              Request.Method.POST,
	              Config.SERVER_URL+"Users/payInter",
	              new Response.Listener<String>() {
	
	                  @Override
	                  public void onResponse(String s) {
	                	  try {
								JSONObject jsonObj = new JSONObject(s);
								if(jsonObj.getInt("result") == 1){
									JSONObject json = jsonObj.getJSONObject("data");
									subject = json.getString("subject");
									body = json.getString("body");
									total_fee = json.getString("total_fee");
									out_trade_no = json.getString("out_trade_no");
									notify_url = json.getString("notify_url");
									// 订单
									String orderInfo = getOrderInfo(subject, body, total_fee);

									// 对订单做RSA 签名
									String sign = sign(orderInfo);
									try {
										// 仅需对sign 做URL编码
										sign = URLEncoder.encode(sign, "UTF-8");
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									}

									// 完整的符合支付宝参数规范的订单信息
									final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
											+ getSignType();

									Runnable payRunnable = new Runnable() {

										@Override
										public void run() {
											// 构造PayTask 对象
											PayTask alipay = new PayTask(AtyPay.this);
											// 调用支付接口，获取支付结果
											String result = alipay.pay(payInfo);

											Message msg = new Message();
											msg.what = SDK_PAY_FLAG;
											msg.obj = result;
											mHandler.sendMessage(msg);
										}
									};

									// 必须异步调用
									Thread payThread = new Thread(payRunnable);
									payThread.start();
								}
								
							} catch (JSONException e) {
								Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
	              }},
	              new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
					}
	              }){
	
	          @Override
	          protected Map<String, String> getParams() throws AuthFailureError {
	              Map<String,String> map = new HashMap<String,String>();
	              map.put(Config.KEY_ID, intent.getStringExtra(Config.KEY_ID));
	              map.put("type",intent.getStringExtra("type"));
	              map.put("paytype", paytype);
	              return map;
	          }
	      };
	      mRequestQueue.add(stringRequest);
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_pay);
		mRequestQueue =  Volley.newRequestQueue(this);
		tvGameNameAtyPay = (TextView) findViewById(R.id.tvGameNameAtyPay);
		tvGameItemAtyPay = (TextView) findViewById(R.id.tvGameItemAtyPay);
		tvPayFeeAtyPay = (TextView) findViewById(R.id.tvPayFeeAtyPay);
		tvZhiFuPayAtyPay = (ImageView) findViewById(R.id.tvZhiFuPayAtyPay);
		
		intent = getIntent();
		
		tvGameNameAtyPay.setText(intent.getStringExtra(Config.KEY_GAME_NAME));
		tvGameItemAtyPay.setText("项目："+intent.getStringExtra(Config.KEY_USER_ATTEND_ENAME));
		tvPayFeeAtyPay.setText("费用："+intent.getStringExtra("apayfee")+"元人民币");
		
//		subject = intent.getStringExtra(Config.KEY_GAME_NAME);
//		body = intent.getStringExtra(Config.KEY_USER_ATTEND_ENAME);
//		total_fee = intent.getStringExtra(Config.KEY_USER_ATTEND_EPAYFEE);
//		out_trade_no = intent.getStringExtra("out_trade_no");
//		notify_url = intent.getStringExtra("notify_url");
		tvZhiFuPayAtyPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
						|| TextUtils.isEmpty(SELLER)) {
					return;
				}
				//支付宝支付
				paytype = "0";
				pay();
			}
		});
		
//		actionBar = getActionBar();  
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(R.layout.actionbar_pay);//自定义ActionBar布局
		setTitleBar();
	
	}
	
	
	private void setTitleBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		pageTitle = (TextView) findViewById(R.id.toolbar_text);
		toolbar.setPadding(0, getDimensionMiss(), 0, 0);
		toolbar.setTitle("");
		pageTitle.setText("支付");
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.backbtn);
		toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AtyPay.this.finish();
			}
		});
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notify_url
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
