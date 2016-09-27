package com.example.exerciseapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.activityrun.ActivityAllteam;
import com.example.exerciseapp.aty.activityrun.ActivityCity;
import com.example.exerciseapp.aty.activityrun.ActivityMessage;
import com.example.exerciseapp.aty.activityrun.ActivityNews;
import com.example.exerciseapp.aty.activityrun.ActivityOrganize;
import com.example.exerciseapp.aty.activityrun.ActivityOrganzie1;
import com.example.exerciseapp.aty.activityrun.ActivityScoreManager;
import com.example.exerciseapp.aty.activityrun.CreditActivity;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.aty.organzie.PersonalMsgActivity;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelbiz.JumpToBizProfile;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class SportTableSecondFragment extends Fragment implements OnClickListener {
    ImageView gotomessage;
    LinearLayout linearnews;
    LinearLayout linearorganize;
    LinearLayout linearsportteam;
    LinearLayout linearcity;
    LinearLayout gofindergift;
    LinearLayout gofindershop;
    LinearLayout gotowxrank;
    LinearLayout gofinderexpect;
    private CheckBox cbReceivePush;
    private RequestQueue mRequestQueue;

    String uid = MyApplication.getInstance().getUid();
    private IWXAPI api;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_second_finder, container, false);
        mRequestQueue = Volley.newRequestQueue(getActivity());
        gotomessage = (ImageView) view.findViewById(R.id.gotomessage);
        linearnews = (LinearLayout) view.findViewById(R.id.linearnews);
        linearorganize = (LinearLayout) view.findViewById(R.id.linearorganize);
        linearsportteam = (LinearLayout) view.findViewById(R.id.linearsportteam);
        linearcity = (LinearLayout) view.findViewById(R.id.linearcity);
        gofindergift = (LinearLayout) view.findViewById(R.id.gofindergift);
        gofindershop = (LinearLayout) view.findViewById(R.id.gofindershop);
        gotowxrank = (LinearLayout) view.findViewById(R.id.gotowxrank);
        gofinderexpect = (LinearLayout) view.findViewById(R.id.gofinderexpect);
        gotomessage.setOnClickListener(this);
        linearnews.setOnClickListener(this);
        linearorganize.setOnClickListener(this);
        gotomessage.setOnClickListener(this);
        linearsportteam.setOnClickListener(this);
        linearcity.setOnClickListener(this);
        gofindergift.setOnClickListener(this);
        gofindershop.setOnClickListener(this);
        gotowxrank.setOnClickListener(this);
        gofinderexpect.setOnClickListener(this);
        api = WXAPIFactory.createWXAPI(getActivity(), Config.WxAPP_ID, false);
        api.registerApp(Config.WxAPP_ID);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v == gotomessage) {
            intent.setClass(getActivity(), PersonalMsgActivity.class);
            startActivity(intent);
        } else if (v == linearnews) {
            intent.setClass(getActivity(), ActivityNews.class);
            startActivity(intent);
        } else if (v == linearorganize) {
            intent.setClass(getActivity(), ActivityOrganzie1.class);
            startActivity(intent);
        } else if (v == linearsportteam) {
            intent.setClass(getActivity(), ActivityAllteam.class);
            startActivity(intent);
        } else if (v == linearcity) {
            intent.setClass(getActivity(), ActivityCity.class);
            startActivity(intent);
        } else if (v == gofindergift || v == gofindershop) {
            dogotoscore();
        } else if (v == gotowxrank) {
            JumpToBizProfile.Req req = new JumpToBizProfile.Req();
            req.toUserName = "bj-fio"; //公众号原始ID
            req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE;
            req.extMsg = "extMsg";
            api.sendReq(req);
        } else if (v == gofinderexpect) {

        }
    }

    public void dogotoscore() {
        final Intent intent = new Intent(getActivity(), ActivityScoreManager.class);
        String temp;
        if ("0".equals(MyApplication.getInstance().getUid()))
            temp = "not_login";
        else
            temp = MyApplication.getInstance().getUid();
        String url = "http://101.200.214.68/py/duiba?action=get_en_url" +
                "&uid=" + temp +
                "&token=" + MyApplication.getInstance().getToken() +
                "&version=3.0";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String myurl = jsonObject.getString("data");
                            intent.putExtra("url", myurl);
                            intent.putExtra("navColor", "#0acbc1");    //配置导航条的背景颜色，请用#ffffff长格式。
                            intent.putExtra("titleColor", "#ffffff");    //配置导航条标题的颜色，请用#ffffff长格式。
                            startActivity(intent);
                            CreditActivity.creditsListener = new CreditActivity.CreditsListener() {
                                /**
                                 * 当点击分享按钮被点击
                                 *
                                 * @param shareUrl       分享的地址
                                 * @param shareThumbnail 分享的缩略图
                                 * @param shareTitle     分享的标题
                                 * @param shareSubtitle  分享的副标题
                                 */
                                public void onShareClick(WebView webView, String shareUrl, String shareThumbnail, String shareTitle, String shareSubtitle) {
                                    //当分享按钮被点击时，会调用此处代码。在这里处理分享的业务逻辑。
                                    new AlertDialog.Builder(webView.getContext())
                                            .setTitle("分享信息")
                                            .setItems(new String[]{"标题：" + shareTitle, "副标题：" + shareSubtitle, "缩略图地址：" + shareThumbnail, "链接：" + shareUrl}, null)
                                            .setNegativeButton("确定", null)
                                            .show();
                                }

                                /**
                                 * 当点击“请先登录”按钮唤起登录时，会调用此处代码。
                                 * 用户登录后，需要将CreditsActivity.IS_WAKEUP_LOGIN变量设置为true。
                                 *
                                 * @param webView    用于登录成功后返回到当前的webview刷新登录状态。
                                 * @param currentUrl 当前页面的url
                                 */
                                public void onLoginClick(WebView webView, String currentUrl) {
                                    //当未登录的用户点击去登录时，会调用此处代码。
                                    //用户登录后，需要将CreditsActivity.IS_WAKEUP_LOGIN变量设置为true。
                                    //为了用户登录后能回到未登录前的页面（currentUrl）。
                                    //当用户登录成功后，需要重新请求一次服务端，带上currentUrl。
                                    //用该方法中的webview变量加载请求链接。
                                    //服务端收到请求后在生成免登录url时，将currentUrl放入redirect参数，通知客户端302跳转到新生成的免登录URL。
                                    new AlertDialog.Builder(webView.getContext())
                                            .setTitle("跳转登录")
                                            .setMessage("跳转到登录页面？")
                                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(getActivity(), AtyWelcome.class));
                                                    getActivity().finish();

                                                }
                                            })
                                            .setNegativeButton("否", null)
                                            .show();
                                }

                                /**
                                 * 当点击“复制”按钮时，触发该方法，回调获取到券码code
                                 *
                                 * @param webView webview对象。
                                 * @param code    复制的券码
                                 */
                                public void onCopyCode(WebView webView, String code) {
                                    //当未登录的用户点击去登录时，会调用此处代码。
                                    new AlertDialog.Builder(webView.getContext())
                                            .setTitle("复制券码")
                                            .setMessage("已复制，券码为：" + code)
                                            .setPositiveButton("是", null)
                                            .setNegativeButton("否", null)
                                            .show();
                                }

                                /**
                                 * 积分商城返回首页刷新积分时，触发该方法。
                                 */
                                public void onLocalRefresh(WebView mWebView, String credits) {
                                    //String credits为积分商城返回的最新积分，不保证准确。
                                    //触发更新本地积分，这里建议用ajax向自己服务器请求积分值，比较准确。
                                    Toast.makeText(getActivity().getApplicationContext(), "触发本地刷新积分：" + credits, Toast.LENGTH_SHORT).show();
                                }
                            };
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
}
