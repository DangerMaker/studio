package com.example.exerciseapp.aty.activityrun;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.myutils.HintDialog;
import com.example.exerciseapp.myutils.UploadImageUtils;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

public class ActivityRunCircle extends Activity {
    private static final String BASEURL = "http://101.200.214.68/RunCircle/index.html?";
    private String dataUrl;
    private String webUrl;
    private boolean isfirst = true;
    TextView pagetitle;
    ImageView goback;
    private static IWXAPI api;
    private Context context;
    private WebView webView;
    private RequestQueue mRequestQueue;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_friend_runcircle);
        api = WXAPIFactory.createWXAPI(this, Config.WxAPP_ID);
        api.registerApp(Config.WxAPP_ID);
        mRequestQueue = Volley.newRequestQueue(this);
        context = ActivityRunCircle.this;
        ActionBar actionBar = getActionBar();
        if (null != actionBar)
            actionBar.hide();
        dataUrl = "uid=" + MyApplication.getInstance().getUid()
                + "&token=" + MyApplication.getInstance().getToken()
                + "&cat=android"
                + "#/app/myRunCircle";
        goback = (ImageView) findViewById(R.id.goback);
        pagetitle = (TextView) findViewById(R.id.pagetitle);
        webView = (WebView) findViewById(R.id.runcirclewebview);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    if (webView.getUrl().equals(webUrl)) {
                        finish();
                    } else {
                        webView.goBack();
                    }
                } else {
                    finish();
                }
            }
        });
        webUrl = BASEURL + dataUrl;
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, final WebResourceRequest request) {
//                if (request.getUrl().toString().startsWith("http://101.200.214.68/ky/share?action=share_post")) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Looper.prepare();
//                            showPopupWindow(request.getUrl().toString());
//                            Looper.loop();
//                        }
//                    }).start();
//                } else if (request.getUrl().toString().startsWith("http://101.200.214.68/ky/uploadCirBack")) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Looper.prepare();
//                            changebacksport(request.getUrl().toString());
//                            Looper.loop();
//                        }
//                    }).start();
//                } else if (request.getUrl().toString().startsWith("http://101.200.214.68/RunCircle/?uid=")
//                        && request.getUrl().toString().contains("tiezixiangqing") && !isfirst) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Looper.prepare();
//                            Intent intent = new Intent();
//                            intent.setClass(ActivityRunCircle.this, ActivityHybrid.class);
//                            intent.putExtra("url", request.getUrl().toString());
//                            startActivity(intent);
//                            Looper.loop();
//                        }
//                    }).start();
//                }
//                return super.shouldInterceptRequest(view, request);
//            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {
                if (url.startsWith("http://101.200.214.68/ky/share?action=share_post")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            showPopupWindow(url);
                            Looper.loop();
                        }
                    }).start();
                } else if (url.startsWith("http://101.200.214.68/ky/uploadCirBack")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            changebacksport(url);
                            Looper.loop();
                        }
                    }).start();
                } else if (url.startsWith("http://101.200.214.68/RunCircle/?uid=")
                        && url.contains("tiezixiangqing") && !isfirst) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            Intent intent = new Intent();
                            intent.setClass(ActivityRunCircle.this, ActivityHybrid.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                            Looper.loop();
                        }
                    }).start();
                }
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!webUrl.equals(webView.getUrl()) || !isfirst) {
                    pagetitle.setText(title);
                    isfirst = false;
                }
                super.onReceivedTitle(view, title);
            }
        });
        webView.loadUrl(webUrl);
    }

    private void changebacksport(final String url) {
        LayoutInflater inflater = LayoutInflater
                .from(ActivityRunCircle.this);
        final View dialogView = inflater.inflate(
                R.layout.dialog_choose_photo, null);
        final HintDialog dialog = new HintDialog(
                ActivityRunCircle.this, dialogView,
                R.style.MyDialogStyle);
        dialog.setCancelable(true);
        View btnTakePhoto = dialogView.findViewById(R.id.llTakePhoto);
        View btnSelectImage = dialogView
                .findViewById(R.id.llSelectImage);
        View btnCancel = dialogView.findViewById(R.id.llCancel_photo);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromCamera();
                dialog.dismiss();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromPicture();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // 分享函数
    class MyTask extends AsyncTask<JSONObject, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(JSONObject... params) {
            JSONObject json = params[0];
            Bitmap bitmap = null;
            try {
                String url = json.getString("image");
                // 加载一个网络图片
                InputStream is = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(is);
                wechatShare(json.getString("url"), json.getString("title"), json.getString("content"), bitmap,
                        json.getInt("flag"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }
    // 分享函数

    /**
     * @param webPageUrl  需要跳转的链接
     * @param title       分享标题
     * @param description 分享内容
     * @param bitmap      图片地址
     * @param flag        分享到朋友还是朋友圈的flag
     */

    public void wechatShare(String webPageUrl, String title, String description, Bitmap bitmap, int flag)
            throws IOException {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webPageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
        msg.setThumbImage(thumbBmp);
        thumbBmp.recycle();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    @SuppressWarnings("deprecation")
    private void showPopupWindow(String url) {
        final String shareurl = url;
        PopupWindow popupWindow = null;
        final View view;
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ActivityRunCircle.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.share_layout, null);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 分享到朋友圈
            view.findViewById(R.id.btnWxFriends).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(shareurl, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getString("result").equals("1")) {
                                    JSONObject json = jsonObject.getJSONObject("data");
                                    json.put("flag", 1);
                                    new MyTask().execute(json);

                                } else {
                                    Toast.makeText(ActivityRunCircle.this.getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ActivityRunCircle.this.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("type", "4");
                            map.put("uid", Config.getCachedUserUid(ActivityRunCircle.this.getApplicationContext()));
                            return map;
                        }
                    };
                    mRequestQueue.add(stringRequest);
                }
            });
            // 分享到朋友
            view.findViewById(R.id.btnWxTimeLine).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    StringRequest stringRequest = new StringRequest(shareurl, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getString("result").equals("1")) {
                                    JSONObject json = jsonObject.getJSONObject("data");
                                    json.put("flag", 0);
                                    new MyTask().execute(json);
                                } else {
                                    Toast.makeText(ActivityRunCircle.this.getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ActivityRunCircle.this.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("type", "4");
                            map.put("uid", Config.getCachedUserUid(ActivityRunCircle.this.getApplicationContext()));
                            return map;
                        }
                    };
                    mRequestQueue.add(stringRequest);
                }
            });
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(webView, Gravity.RIGHT | Gravity.BOTTOM, 0, 0);

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
                submitPicture(new File(path));
            } else {
                ScreenUtils.show_msg(context, "对不起,无法找到此图片!");
            }
        }
    }

    public void submitPicture(File file) {
        TypedFile typedFile = new TypedFile("application/octet-stream", file);
        RestAdapterUtils.getTeamAPI().uploadbgPicture(typedFile, MyApplication.getInstance().getToken(),
                "3.2", MyApplication.getInstance().getUid(), new Callback<ErrorMsg>() {
                    @Override
                    public void success(ErrorMsg errorMsg, retrofit.client.Response response) {
                        if (errorMsg != null && errorMsg.getResult() == 1) {
                            ScreenUtils.show_msg(context, "上传成功!");
                            webView.loadUrl("JavaScript:getBackPic()");
                        } else {
                            // 上传 异常
                            ScreenUtils.show_msg(context, errorMsg.getDesc());
                        }
                        closeDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        closeDialog();
                        ScreenUtils.show_msg(context, "上传失败!");
                    }
                });
    }

    @Override
    protected void onResume() {

        webView.loadUrl("JavaScript:getBackPic()");

        super.onResume();

    }
}
