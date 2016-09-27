package com.example.exerciseapp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.activityrun.ActivityConfig;
import com.example.exerciseapp.aty.activityrun.ActivityMessage;
import com.example.exerciseapp.aty.activityrun.ActivityMyClubAndAssociation;
import com.example.exerciseapp.aty.activityrun.ActivityMyGrades;
import com.example.exerciseapp.aty.activityrun.ActivityMypoints;
import com.example.exerciseapp.aty.activityrun.ActivityRunningRecord;
import com.example.exerciseapp.aty.activityrun.ActivityScoreManager;
import com.example.exerciseapp.aty.activityrun.ActivityUserInformation;
import com.example.exerciseapp.aty.activityrun.CreditActivity;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.aty.organzie.PersonalMsgActivity;
import com.example.exerciseapp.aty.sliding.AtyMyEntryForm;
import com.example.exerciseapp.aty.sliding.AtyUserPreferedProject;
import com.example.exerciseapp.aty.team.MyTeamActivity;
import com.example.exerciseapp.model.UpTeamAvatar;
import com.example.exerciseapp.myutils.UploadImageUtils;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.utils.SpeedConvert;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

@SuppressLint("NewApi")
public class SportTabFiveFragment extends Fragment implements OnClickListener {
    SpotsDialog spotsDialog;
    private static CircleImageView ibUserHWPersonalCenter;//用户头像
    private TextView tvUserNamePersonalCenter;        //用户名字
    private TextView tvUserPointPersonalCenter;//用户积分
    private TextView tvUserAttrPersonalCenter;        //用户会员
    private TextView tvUserPreferProjectPersonalCenter;//用户喜爱的项目
    private TextView tvSelectUserPreferProjectPersonalCenter;//用户已经选择的项目
    private TextView tvExerciseRecordPersonalCenter;    //运动记录
    private TextView tvExerciseTimePersonalCenter;    //用户锻炼时间
    private TextView tvExerciseDistancePersonalCenter;//用户锻炼距离
    private RelativeLayout UserConfigPersonalCenter;    //个人设置
    private RelativeLayout MyEntryFormPersonalCenter;    //我的报名
    private RelativeLayout MyClubAndAssocPersonalCenter;//我的协会/俱乐部
    private RelativeLayout MyTeamAndAssocPersonalCenter;
    private RelativeLayout MyGradesPersonalCenter;//我的成绩
    private RelativeLayout MyPersonalMsg;
    private RelativeLayout ScoresManagerPersonalCenter;//积分管理，add by sonchcng
    private AlertDialog alertDialog = null;
    private List<Map<String, String>> alertItem = new ArrayList<Map<String, String>>();
    private RequestQueue mRequestQueue;
    private OnChangeHWCallBack onChangeHWListener;
    Context context;
    ImageView gotoconfig;
    @Bind(R.id.layout_of_my_favor)
    LinearLayout goMyFavor;
    @Bind(R.id.layout_of_my_recode)
    LinearLayout goMyRecode;
    @Bind(R.id.layout_of_my_points)
    LinearLayout goMyPoint;
    TextView gotomessage;

    public void setOnChangeHWCallBack(OnChangeHWCallBack onChangeHWListener) {
        this.onChangeHWListener = onChangeHWListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_five_person, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        mRequestQueue = Volley.newRequestQueue(getActivity());
        //控件初始化
        gotomessage = (TextView) view.findViewById(R.id.gotomessage);
        ibUserHWPersonalCenter = (CircleImageView) view.findViewById(R.id.ibUserHWPersonalCenter);
        tvUserNamePersonalCenter = (TextView) view.findViewById(R.id.tvUserNamePersonalCenter);
        tvUserAttrPersonalCenter = (TextView) view.findViewById(R.id.tvUserAttrPersonalCenter);
        tvUserPointPersonalCenter = (TextView) view.findViewById(R.id.tvUserPointPersonalCenter);
        tvUserPreferProjectPersonalCenter = (TextView) view.findViewById(R.id.tvUserPreferProjectPersonalCenter);
        tvSelectUserPreferProjectPersonalCenter = (TextView) view.findViewById(R.id.tvSelectUserPreferProjectPersonalCenter);
        tvExerciseTimePersonalCenter = (TextView) view.findViewById(R.id.tvExerciseTimePersonalCenter);
        tvExerciseDistancePersonalCenter = (TextView) view.findViewById(R.id.tvExerciseDistancePersonalCenter);
        tvExerciseRecordPersonalCenter = (TextView) view.findViewById(R.id.tvExerciseRecordPersonalCenter);
        ScoresManagerPersonalCenter = (RelativeLayout) view.findViewById(R.id.ScoresManagerPersonalCenter);
        UserConfigPersonalCenter = (RelativeLayout) view.findViewById(R.id.UserConfigPersonalCenter);
        MyEntryFormPersonalCenter = (RelativeLayout) view.findViewById(R.id.MyEntryFormPersonalCenter);
        MyClubAndAssocPersonalCenter = (RelativeLayout) view.findViewById(R.id.MyClubAndAssocPersonalCenter);
        MyTeamAndAssocPersonalCenter = (RelativeLayout) view.findViewById(R.id.MyTeamAndAssocPersonalCenter);
        MyPersonalMsg = (RelativeLayout) view.findViewById(R.id.personal_message);
        MyPersonalMsg.setOnClickListener(this);
        MyGradesPersonalCenter = (RelativeLayout) view.findViewById(R.id.MyGradesPersonalCenter);
        gotoconfig = (ImageView) view.findViewById(R.id.gotoconfig);
        gotoconfig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ActivityConfig.class);
                startActivity(intent);
            }
        });
        //头像   用户名等初始化
        if (Config.getCachedUserHwURL(getActivity().getApplicationContext()) != null) {
            Picasso.with(getActivity()).load(Config.getCachedUserHwURL(getActivity().getApplicationContext())).into(ibUserHWPersonalCenter);
        }
        if (Config.getCachedBriefUserInformation(getActivity().getApplicationContext()) != null) {
            JSONObject json = Config.getCachedBriefUserInformation(getActivity().getApplicationContext());

            try {
                if (Config.TOURIST_MODE) {
                    tvUserNamePersonalCenter.setText("游客");
                } else {
                    tvUserNamePersonalCenter.setText(json.getString(Config.KEY_NICK_NAME));
                    tvUserAttrPersonalCenter.setText(json.getString(Config.KEY_ASSOC));
                    tvUserPointPersonalCenter.setText(json.getString("point"));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //更换头像
        view.findViewById(R.id.ibUserHWPersonalCenter).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setItems(new String[]{"拍照", "从相册中选取"},
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        switch (which) {
                                            case 0:
                                                fromCamera();
                                                break;
                                            case 1:
                                                fromPicture();
                                                break;
                                            default:
                                                break;
                                        }
                                    }

                                }).setNegativeButton("取消", null).show();
            }
        });
        //点击个人设置  我的报名 场馆预约 我的俱乐部 我的成绩 积分管理和我的关注跳转响应页面
        UserConfigPersonalCenter.setOnClickListener(this);
        MyEntryFormPersonalCenter.setOnClickListener(this);
        MyClubAndAssocPersonalCenter.setOnClickListener(this);
        MyTeamAndAssocPersonalCenter.setOnClickListener(this);
        MyGradesPersonalCenter.setOnClickListener(this);
        goMyFavor.setOnClickListener(this);
        goMyRecode.setOnClickListener(this);
        goMyPoint.setOnClickListener(this);
        gotomessage.setOnClickListener(this);
        ScoresManagerPersonalCenter.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.show();
        StringRequest stringRequestUserInformation = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL + "Users/getUserRun",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            jsonObject = jsonObject.getJSONObject("data");
                            long t = (long) (jsonObject.getDouble("duration"));
                            tvExerciseTimePersonalCenter.setText(SpeedConvert.secToDeTime(t) + "h");
                            tvExerciseDistancePersonalCenter.setText((float) (Math.round(jsonObject.getDouble("distance") / 1000 * 10)) / 10 + "km");
                            spotsDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            spotsDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        spotsDialog.dismiss();
                        Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_UID, Config.getCachedUserUid(getActivity().getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequestUserInformation);
        //得到用户喜欢的健身项目
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL + "Users/getUserInterest",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray jsonArr = jsonObject.getJSONArray("data");
                            String loved = new String();
                            for (int i = 0; i < jsonArr.length(); i++) {
                                loved = loved.concat(jsonArr.getJSONObject(i).getString("name") + " ");
                            }
                            tvSelectUserPreferProjectPersonalCenter.setText(loved);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_UID, Config.getCachedUserUid(getActivity().getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequest);

        StringRequest stringRequestUserInformation1 = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL + "Users/userInfoNew",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json = new JSONObject(s);
                            if (json.getInt("result") == 1) {
                                tvUserNamePersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_USER_NAME));
                                tvUserAttrPersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_ASSOC));
                                tvUserPointPersonalCenter.setText(json.getJSONObject("data").getString("point").toString());
                                if (!Config.STATUS_SUBMIT_USER_HW) {
                                    Picasso.with(getActivity()).load(json.getJSONObject("data").getString(Config.KEY_AVATAR)).into(ibUserHWPersonalCenter);
                                }

                            } else {
                                Toast.makeText(getActivity(), json.getString("desc"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            spotsDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        spotsDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_UID, Config.getCachedUserUid(getActivity().getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequestUserInformation1);
        if (Config.STATUS_SUBMIT_USER_HW) {
            if (Config.getCachedUserHw(getActivity().getApplicationContext()) != null) {
                ibUserHWPersonalCenter.setImageBitmap(Config.getCachedUserHw(getActivity().getApplicationContext()));
            }
            Config.STATUS_SUBMIT_USER_HW = false;
        } else {
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_of_my_points:
                //TODO
                startActivity(new Intent(getActivity(), ActivityMypoints.class));
                break;
            case R.id.layout_of_my_recode:
                startActivity(new Intent(getActivity(), ActivityRunningRecord.class));
                break;
            case R.id.layout_of_my_favor:
                startActivity(new Intent(getActivity(), AtyUserPreferedProject.class));
                break;
            case R.id.UserConfigPersonalCenter:
                startActivity(new Intent(getActivity(), ActivityUserInformation.class));
                break;
            case R.id.MyEntryFormPersonalCenter:
                startActivity(new Intent(getActivity(), AtyMyEntryForm.class));
                break;
            case R.id.MyClubAndAssocPersonalCenter:
                startActivity(new Intent(getActivity(), ActivityMyClubAndAssociation.class));
                break;
            case R.id.MyGradesPersonalCenter:
                startActivity(new Intent(getActivity(), ActivityMyGrades.class));
                break;
            case R.id.MyTeamAndAssocPersonalCenter:
                startActivity(new Intent(getActivity(), MyTeamActivity.class));
                break;
            case R.id.ScoresManagerPersonalCenter:
                dogotoscore();
                break;
            case R.id.gotomessage:
                startActivity(new Intent(getActivity(), ActivityMessage.class));
                break;
            case R.id.personal_message:
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonalMsgActivity.class);
                startActivity(intent);
                break;
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

    public static final int IMG_FROM_PICTURE = 0x1001;
    public static final int IMG_FROM_CAMERA = 0x1002;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case IMG_FROM_CAMERA:
                    new WriteToSdcardTask(false, null).execute();
                    break;
                case IMG_FROM_PICTURE:
                    Uri uri = data.getData();
                    new WriteToSdcardTask(true, uri).execute();
                    break;
                default:
                    break;
            }
        }
    }

    void fromCamera() {
        outputAvatarPath = UploadImageUtils.getAvatarPath(context);
        if (!TextUtils.isEmpty(outputAvatarPath)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File tmpFile = new File(outputAvatarPath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));
            startActivityForResult(intent, IMG_FROM_CAMERA);
        } else {
            ScreenUtils.show_msg(context, "未插入SD卡");
        }
    }


    void fromPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_FROM_PICTURE);
    }

    private String outputAvatarPath = "";
    private ProgressDialog dialog;

    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(context);
            dialog.setMessage("正在加载，请稍等");
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    public void closeDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public class WriteToSdcardTask extends AsyncTask {
        private boolean isUri;
        private Uri uri;

        public WriteToSdcardTask(boolean isUri, Uri uri) {
            this.isUri = isUri;
            this.uri = uri;
            showDialog();
        }

        @Override
        protected String doInBackground(Object... params) {
            String path = "";
            try {
                if (isUri) {
                    path = UploadImageUtils.writeUriToSDcard(context, uri);
                } else {
                    path = UploadImageUtils.writeStringToSDcard(context, outputAvatarPath);
                }

            } catch (IOException e) {
                ScreenUtils.show_msg(context, "对不起,无法找到此图片!");
            }
            return path;
        }

        @Override
        protected void onPostExecute(Object result) {
            String path = (String) result;
            if (!TextUtils.isEmpty(path)) {
                submitUserIcon(new File(path));
            } else {
                ScreenUtils.show_msg(context, "对不起,无法找到此图片!");
            }
        }
    }

    public void submitUserIcon(File file) {
        TypedFile typedFile = new TypedFile("application/octet-stream", file);
        RestAdapterUtils.getTeamAPI().uploadAvatar(typedFile, Config.getCachedUserUid(getActivity().getApplicationContext()), new Callback<UpTeamAvatar>() {
            @Override
            public void success(UpTeamAvatar upTeamAvatar, retrofit.client.Response response) {
                if (upTeamAvatar != null && upTeamAvatar.getResult() == 1) {

                    Picasso.with(getActivity()).load(upTeamAvatar.getData()).into(ibUserHWPersonalCenter);
                    ScreenUtils.show_msg(context, "上传头像成功!");
                    onChangeHWListener.onChangeHWCallBack();
                } else {
                    // 上传 异常
                    ScreenUtils.show_msg(context, "上传头像失败!");
                }
                closeDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                closeDialog();
                ScreenUtils.show_msg(context, "上传头像失败!");
            }
        });
    }

    public interface OnChangeHWCallBack {
        void onChangeHWCallBack();
    }

}
