package com.example.exerciseapp.aty.activityrun;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.utils.CheckInput;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ActivityUserInformation extends BaseActivity {

    static PopupWindow popupWindow;
    private EditText etNickNameUserInformationConfig;
    private TextView tvSex;
    private EditText etHeightUserInformationConfig;
    private EditText etWeightUserInformationConfig;
    private EditText etBirthdayUserInformationConfig;
    private EditText etLocationUserInformationConfig;
    private EditText etUserNameUserInformationConfig;
    private TextView tvBlood;
    private EditText etUserIDCardUserInformationConfig;
    private EditText etUserEmailUserInformationConfig;
    private EditText etUserTelAddressUserInformation;
    private EditText etTelCodeUserInformationConfig;
    private EditText etUserNationUserInformationConfig;
    private EditText etAssocUserInformationConfig;
    private EditText etEmergenceContactPhoneUserInformationConfig;
    private EditText etEmerContactNameUserInformationConfig;

    private Button btnSubmitUserInformationConfig;

    private AlertDialog alertDialogNoCompleteInformation = null;
    private String[] sexItems = new String[]{"男", "女"};
    private String[] bloodItems = new String[]{"A", "B", "O", "AB"};
    SpotsDialog spotsDialog;
    private RequestQueue mRequestQueue;
    private JSONObject json;
    String uid = MyApplication.getInstance().getUid();
    String token = MyApplication.getInstance().getToken();
    private Toolbar toolbar;
    private TextView pageTitle;

    private Toast mToast;

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(ActivityUserInformation.this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void onBackPressed() {
        cancelToast();
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        spotsDialog = new SpotsDialog(this);
        spotsDialog.show();
        String url = "http://101.200.214.68/py/user?action=get_user_info&uid=" + uid + "&token=" + token;
        StringRequest stringRequestUserInformation = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("result") == 1) {
                        json = jsonObject;
                        fillOldData();
                        spotsDialog.dismiss();
                    } else {

                    }

                } catch (JSONException e) {
                    spotsDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                spotsDialog.dismiss();
                Toast.makeText(ActivityUserInformation.this, Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        mRequestQueue.add(stringRequestUserInformation);
    }

    // 填写旧数据
    public void fillOldData() throws JSONException {
        if (json != null) {
            JSONObject data = json.getJSONObject("data");
            if (data != null) {
                etNickNameUserInformationConfig.setText(data.getString(Config.KEY_NICK_NAME));

                tvSex.setText(data.getString(Config.KEY_SEX));
                etHeightUserInformationConfig.setText(data.getString(Config.KEY_HEIGHT));
                etWeightUserInformationConfig.setText(data.getString(Config.KEY_WEIGHT));
                etBirthdayUserInformationConfig.setText(data.getString(Config.KEY_USER_BIRTHDAY));
                etLocationUserInformationConfig.setText(data.getString(Config.KEY_AREA));
                etUserNameUserInformationConfig.setText(data.getString(Config.KEY_USER_NAME));
                tvBlood.setText(data.getString(Config.KEY_BLOOD));
                etUserIDCardUserInformationConfig.setText(data.getString(Config.KEY_ID_CARD));
                etUserEmailUserInformationConfig.setText(data.getString(Config.KEY_EMAIL));
                etUserTelAddressUserInformation.setText(data.getString(Config.KEY_TEL_ADDRESS));
                etTelCodeUserInformationConfig.setText(data.getString(Config.KEY_ZIP_CODE));
                etUserNationUserInformationConfig.setText(data.getString(Config.KEY_COUNTRY));
                etAssocUserInformationConfig
                        .setText(data.getString(Config.KEY_APOSITION) + "/" + data.getString(Config.KEY_AREA));
                etEmergenceContactPhoneUserInformationConfig.setText(data.getString(Config.KEY_EMER_TEL));
                etEmerContactNameUserInformationConfig.setText(data.getString(Config.KEY_EMER_NAME));

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        mRequestQueue = Volley.newRequestQueue(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.aty_user_information_config);

        // 控件初始化
        etNickNameUserInformationConfig = (EditText) findViewById(R.id.etNickNameUserInformationConfig);
        tvSex = (TextView) findViewById(R.id.tvSex);
        etHeightUserInformationConfig = (EditText) findViewById(R.id.etHeightUserInformationConfig);
        etWeightUserInformationConfig = (EditText) findViewById(R.id.etWeightUserInformationConfig);
        etBirthdayUserInformationConfig = (EditText) findViewById(R.id.etBirthdayUserInformationConfig);
        etLocationUserInformationConfig = (EditText) findViewById(R.id.etLocationUserInformationConfig);
        etUserNameUserInformationConfig = (EditText) findViewById(R.id.etUserNameUserInformationConfig);
        tvBlood = (TextView) findViewById(R.id.tvBlood);
        etUserIDCardUserInformationConfig = (EditText) findViewById(R.id.etUserIDCardUserInformationConfig);

        etUserEmailUserInformationConfig = (EditText) findViewById(R.id.etUserEmailUserInformationConfig);
        etUserTelAddressUserInformation = (EditText) findViewById(R.id.etUserTelAddressUserInformation);
        etTelCodeUserInformationConfig = (EditText) findViewById(R.id.etTelCodeUserInformationConfig);
        etUserNationUserInformationConfig = (EditText) findViewById(R.id.etUserNationUserInformationConfig);
        etAssocUserInformationConfig = (EditText) findViewById(R.id.etAssocUserInformationConfig);

        etEmergenceContactPhoneUserInformationConfig = (EditText) findViewById(
                R.id.etEmergenceContactPhoneUserInformationConfig);
        etEmerContactNameUserInformationConfig = (EditText) findViewById(R.id.etEmerContactNameUserInformationConfig);

        btnSubmitUserInformationConfig = (Button) findViewById(R.id.btnSubmitUserInformationConfig);

        // 生日输入自动添加-
        etBirthdayUserInformationConfig.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (count == 1) {
                    if (s.length() == 4 || s.length() == 7) {
                        etBirthdayUserInformationConfig.setText(s + "-");
                        etBirthdayUserInformationConfig.setSelection(s.length() + 1);
                    }
                } else if (count == 0) {
                    if (s.length() > 0 && (s.length() == 5 || s.length() == 8)) {
                        etBirthdayUserInformationConfig.setText(s.subSequence(0, s.length() - 1));
                        etBirthdayUserInformationConfig.setSelection(s.length() - 1);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        // 性别下拉菜单
        // ArrayAdapter<String> adapterSexItems = new ArrayAdapter<String>(this,
        // R.layout.spinner_list_item,sexItems);
        // tvSex.setAdapter(adapterSexItems);
        // spSexUserInformationConfig.setSelection(0);
        tvSex.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow = null;
                View view = null;
                if (popupWindow == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.sex_item, null);
                    popupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                view.findViewById(R.id.tvMan).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tvSex.setText("男");
                        popupWindow.dismiss();
                    }
                });
                view.findViewById(R.id.tvWoman).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tvSex.setText("女");
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(v);
            }
        });

        // 血型下拉菜单
        // ArrayAdapter<String> adapterBloodItems = new
        // ArrayAdapter<String>(this, R.layout.spinner_list_item,bloodItems);
        // tvBlood.setAdapter(adapterBloodItems);
        // spUserBloodTypeUserInformationConfig.setSelection(0);
        tvBlood.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow = null;
                View view = null;
                if (popupWindow == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.blood_item, null);
                    popupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                            android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                view.findViewById(R.id.tvA).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tvBlood.setText("A");
                        popupWindow.dismiss();
                    }
                });
                view.findViewById(R.id.tvB).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tvBlood.setText("B");
                        popupWindow.dismiss();
                    }
                });
                view.findViewById(R.id.tvO).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tvBlood.setText("O");
                        popupWindow.dismiss();
                    }
                });
                view.findViewById(R.id.tvAB).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        tvBlood.setText("AB");
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(v);
            }
        });

        // try {
        // fillOldData();
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }

        // 提交按钮点击事件
        btnSubmitUserInformationConfig.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (
                    // TextUtils.isEmpty(etNickNameUserInformationConfig.getText())
                    // ||
                        TextUtils.isEmpty(tvSex.getText().toString())
                                || TextUtils.isEmpty(etHeightUserInformationConfig.getText())
                                || TextUtils.isEmpty(etWeightUserInformationConfig.getText())
                                || TextUtils.isEmpty(etBirthdayUserInformationConfig.getText())
                    // ||
                    // TextUtils.isEmpty(etUserNameUserInformationConfig.getText())||
                    // TextUtils.isEmpty(etLocationUserInformationConfig.getText())||
                    // TextUtils.isEmpty(spUserBloodTypeUserInformationConfig.getSelectedItem().toString())||
                    // TextUtils.isEmpty(etUserIDCardUserInformationConfig.getText())||
                    // TextUtils.isEmpty(etUserEmailUserInformationConfig.getText())||
                    // TextUtils.isEmpty(etUserTelAddressUserInformation.getText())||
                    // TextUtils.isEmpty(etTelCodeUserInformationConfig.getText())||
                    // TextUtils.isEmpty(etUserNationUserInformationConfig.getText())||
                    // TextUtils.isEmpty(etAssocUserInformationConfig.getText())||
                    // TextUtils.isEmpty(etEmergenceContactPhoneUserInformationConfig.getText())||
                    // TextUtils.isEmpty(etEmerContactNameUserInformationConfig.getText())
                        ) {
                    alertDialogNoCompleteInformation = new AlertDialog.Builder(ActivityUserInformation.this).create();
                    alertDialogNoCompleteInformation.show();
                    alertDialogNoCompleteInformation.getWindow()
                            .setContentView(R.layout.alert_dialog_no_complete_information);
                    alertDialogNoCompleteInformation.getWindow()
                            .findViewById(R.id.btnCertainAlertDialogNoCompleteInformation)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    alertDialogNoCompleteInformation.dismiss();
                                }
                            });
                    alertDialogNoCompleteInformation.getWindow()
                            .findViewById(R.id.btnCancelAlertDialogNoCompleteInformation)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    alertDialogNoCompleteInformation.dismiss();
                                }
                            });
                } else {
                    // 判断输入格式是否正确 TODO
                    try {
                        correctInputAndSubmit();
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
        setTitleBar();
    }

    @SuppressLint("InlinedApi")
    public void setTitleBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        pageTitle = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        pageTitle.setText("设置");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUserInformation.this.finish();
            }
        });
    }

    // 验证所有的输入是否正确并提交信息
    public void correctInputAndSubmit() throws UnsupportedEncodingException {
        if (!CheckInput.isIDCard(etUserIDCardUserInformationConfig.getText().toString())) {
            showToast("身份证格式错误！");
            return;
        }
        if (!CheckInput.isEmail(etUserEmailUserInformationConfig.getText().toString())) {
            showToast("邮箱格式错误！");
            return;
        }
//		if(!CheckInput.isPhoneNum(etEmergenceContactPhoneUserInformationConfig.getText().toString())){
//			showToast("联系方式格式错误！");
//			return;
//		}
        // 提交个人信息 通信
        String area = "";
        String assoc = "";
        if (!TextUtils.isEmpty(etAssocUserInformationConfig.getText())) {
            area = etAssocUserInformationConfig.getText().toString().substring(0,
                    etAssocUserInformationConfig.getText().toString().indexOf("/"));
            assoc = etAssocUserInformationConfig.getText().toString()
                    .substring(etAssocUserInformationConfig.getText().toString().lastIndexOf("/") + 1);
        }

        String url = Config.SERVER_URL + "Users/updateUserInfoNew?" + Config.KEY_UID + "="
                + Config.getCachedUserUid(getApplicationContext()) + "&" + Config.KEY_HEIGHT + "="
                + etHeightUserInformationConfig.getText().toString() + "&" + Config.KEY_SEX + "="
                + URLEncoder.encode(tvSex.getText().toString(), "utf-8") + "&" + Config.KEY_WEIGHT + "="
                + etWeightUserInformationConfig.getText().toString() + "&" + Config.KEY_USER_BIRTHDAY + "="
                + etBirthdayUserInformationConfig.getText().toString() + "&" + Config.KEY_BLOOD + "="
                + tvBlood.getText().toString() + "&" + Config.KEY_NICK_NAME + "="
                + URLEncoder.encode(etNickNameUserInformationConfig.getText().toString(), "utf-8") + "&"
                + Config.KEY_AREA + "="
                + URLEncoder.encode(etLocationUserInformationConfig.getText().toString(), "utf-8") + "&"
                + Config.KEY_USER_NAME + "="
                + URLEncoder.encode(etUserNameUserInformationConfig.getText().toString(), "utf-8") + "&"
                + Config.KEY_ID_CARD + "=" + etUserIDCardUserInformationConfig.getText().toString() + "&"
                + Config.KEY_EMAIL + "=" + etUserEmailUserInformationConfig.getText().toString() + "&"
                + Config.KEY_TEL_ADDRESS + "="
                + URLEncoder.encode(etUserTelAddressUserInformation.getText().toString(), "utf-8") + "&"
                + Config.KEY_ZIP_CODE + "=" + etTelCodeUserInformationConfig.getText().toString() + "&"
                + Config.KEY_COUNTRY + "="
                + URLEncoder.encode(etUserNationUserInformationConfig.getText().toString(), "utf-8") + "&"
                + Config.KEY_APOSITION + "=" + URLEncoder.encode(area, "utf-8") + "&" + Config.KEY_ASSOC + "="
                + URLEncoder.encode(assoc, "utf-8") + "&" + Config.KEY_EMER_NAME + "="
                + URLEncoder.encode(etEmerContactNameUserInformationConfig.getText().toString(), "utf-8") + "&"
                + Config.KEY_EMER_TEL + "=" + etEmergenceContactPhoneUserInformationConfig.getText().toString()
                + "token=" + token;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("result") == 1) {
                        Toast.makeText(ActivityUserInformation.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityUserInformation.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
        };
        mRequestQueue.add(stringRequest);

        StringRequest stringRequestUserBriefInformation = new StringRequest(Request.Method.POST,
                Config.SERVER_URL + "Users/briefUserInfoNew", new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("result") == 1) {
                        Toast.makeText(ActivityUserInformation.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                        Config.cacheBriefUserInformation(getApplicationContext(),
                                jsonObject.getJSONObject("data"));
                        Config.cacheUserHwURL(getApplicationContext(),
                                jsonObject.getJSONObject("data").getString(Config.KEY_AVATAR));
                        finish();
                    } else {
                        Toast.makeText(ActivityUserInformation.this, jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequestUserBriefInformation);
    }

}
