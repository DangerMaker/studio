package com.example.exerciseapp.aty.activityrun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MainActivity;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.TabMainActivity;
import com.example.exerciseapp.aty.login.AtyWelcome;
import com.example.exerciseapp.aty.sliding.AtyAboutUs;
import com.example.exerciseapp.aty.sliding.AtyChangePassw;
import com.example.exerciseapp.aty.sliding.Atyyijianfankui;
import com.example.exerciseapp.fragment.qinglihuancunDialog;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;

public class ActivityConfig extends Activity {
    ImageView imgReceivePush;
    LinearLayout linearreceivepush;
    LinearLayout linearcleanmemory;
    TextView texthuancun;
    ImageView imgClenMemory;
    LinearLayout linearjiancexinbanben;
    LinearLayout linearyijianfankui;
    ImageView imgyijianfankui;
    LinearLayout linearaboutus;
    LinearLayout lineargetbind;
    ImageView imgAboutUs;
    //	 ToggleButton toggleBtn;
    Button btntuichudenglu;
    private CheckBox cbReceivePush;
    private RequestQueue mRequestQueue;
    private LinearLayout linearchangepass;
    ImageView goback;
    String uid = MyApplication.getInstance().getUid();
    String token = MyApplication.getInstance().getToken();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_config);
        linearreceivepush = (LinearLayout) findViewById(R.id.linearreceivepush);
        lineargetbind = (LinearLayout) findViewById(R.id.lineargetbind);
        cbReceivePush = (CheckBox) findViewById(R.id.cbReceivePush);
        mRequestQueue = Volley.newRequestQueue(this);
        goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cbReceivePush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    MainActivity.mPushAgent.enable();
                } else {
                    MainActivity.mPushAgent.disable();
                    Toast.makeText(ActivityConfig.this, "已关闭", Toast.LENGTH_SHORT).show();
                }
            }

        });
        lineargetbind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ActivityConfig.this, ActivityBind.class);
                startActivity(intent);
            }
        });

        imgReceivePush = (ImageView) findViewById(R.id.imgreceivepush);
        imgReceivePush.setImageDrawable(getResources().getDrawable(R.drawable.receivepush));
        linearcleanmemory = (LinearLayout) findViewById(R.id.linearcleanmemory);
        linearcleanmemory.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                qinglihuancunDialog dl = new qinglihuancunDialog(ActivityConfig.this, "提示", new qinglihuancunDialog.OnCustomDialogListener() {

                    @Override
                    public void back(String name) {
                        // TODO Auto-generated method stub

                    }
                });
                dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dl.show();

                texthuancun.setText("0.0M");
                DataCleanManager.clearAllCache(ActivityConfig.this.getApplicationContext());
            }
        });
        texthuancun = (TextView) findViewById(R.id.texthuancun);

        imgClenMemory = (ImageView) findViewById(R.id.imgcleanmemory);
        imgClenMemory.setImageDrawable(getResources().getDrawable(R.drawable.cleanmemory));


        linearjiancexinbanben = (LinearLayout) findViewById(R.id.linearjiancexinbanben);
        linearjiancexinbanben.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateConfig.setDebug(true);
                UmengUpdateAgent.setDefault();
                UmengUpdateAgent.setSlotId("123456");
                UmengUpdateAgent.forceUpdate(ActivityConfig.this);
            }
        });


        imgyijianfankui = (ImageView) findViewById(R.id.imgyijianfankui);
        imgyijianfankui.setImageDrawable(getResources().getDrawable(R.drawable.yijianfankui));
        linearyijianfankui = (LinearLayout) findViewById(R.id.linearyijianfankui);
        linearyijianfankui.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(ActivityConfig.this, Atyyijianfankui.class);
                startActivity(i);
            }
        });
        imgAboutUs = (ImageView) findViewById(R.id.imgaboutus);
        imgAboutUs.setImageDrawable(getResources().getDrawable(R.drawable.aboutus));
        linearaboutus = (LinearLayout) findViewById(R.id.linearaboutus);
        linearaboutus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(ActivityConfig.this, AtyAboutUs.class);
                startActivity(i);
            }
        });
        btntuichudenglu = (Button) findViewById(R.id.btnlogout);
        btntuichudenglu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dologout();
            }
        });

        linearchangepass = (LinearLayout) findViewById(R.id.linearchangepass);
        linearchangepass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ActivityConfig.this, AtyChangePassw.class);
                startActivity(intent);
            }
        });
    }

    public void dologout() {
        String logouturl = "http://101.200.214.68/py/logout?action=logout&uid=" + uid + "&token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, logouturl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String reason = jsonObject.getString("desc");
                            if (jsonObject.getInt("result") == 0) {
                                Toast.makeText(ActivityConfig.this, reason, Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getInt("result") == 1) {
                                Config.STATUS_FINISH_ACTIVITY = 0;
                                startActivity(new Intent(ActivityConfig.this, AtyWelcome.class));
                                DataCleanManager.quitCurrentUser(ActivityConfig.this.getApplicationContext());
                                TabMainActivity.instance.finish();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ActivityConfig.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(stringRequest);
    }

    public static class DataCleanManager {

        public static String getTotalCacheSize(Context context) throws Exception {
            long cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
            return getFormatSize(cacheSize);
        }


        public static void quitCurrentUser(Context context) {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    if (children[i].equals("GameList")) {
                    } else if (children[i].equals("AssocList")) {
                    } else if (children[i].equals("NewsList")) {
                    } else if (children[i].equals("Uid")) {
                        Config.cacheUserUid(context, "");
                    } else {
                        deleteDir(new File(dir, children[i]));
                    }
                }
            }
        }

        public static void clearOtheCache(Context context) {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    if (children[i].equals("Uid")) {
                    } else if (children[i].equals("UserHwURL")) {
                    } else if (children[i].equals("BriefUserInformation")) {
                    } else if (children[i].equals("UserInformation")) {
                    } else if (children[i].equals("WeightAndHeight")) {
                    } else if (children[i].equals("Weight")) {
                    } else if (children[i].equals("UserHw")) {
                    } else {
                        deleteDir(new File(dir, children[i]));
                    }
                }
            }
        }

        public static void clearAllCache(Context context) {
            clearOtheCache(context);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                deleteDir(context.getExternalCacheDir());
            }
        }

        private static boolean deleteDir(File dir) {
            if (dir != null && dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        }

        public static long getFolderSize(File file) throws Exception {
            long size = 0;
            try {
                File[] fileList = file.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return size;
        }

        /**
         * 格式化单位
         *
         * @param size
         * @return
         */
        public static String getFormatSize(double size) {
            double kiloByte = size / 1024;
            if (kiloByte < 1) {
                return "0K";
            }

            double megaByte = kiloByte / 1024;
            if (megaByte < 1) {
                BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
                return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toPlainString() + "KB";
            }

            double gigaByte = megaByte / 1024;
            if (gigaByte < 1) {
                BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
                return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toPlainString() + "MB";
            }

            double teraBytes = gigaByte / 1024;
            if (teraBytes < 1) {
                BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
                return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .toPlainString() + "GB";
            }
            BigDecimal result4 = new BigDecimal(teraBytes);
            return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                    + "TB";
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try {
            texthuancun.setText(DataCleanManager.getTotalCacheSize(ActivityConfig.this));//DataCleanManager.getTotalCacheSize(getActivity());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
