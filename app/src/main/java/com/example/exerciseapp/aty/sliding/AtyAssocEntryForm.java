package com.example.exerciseapp.aty.sliding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.aty.login.AtyUserLawItem;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyRegisterHomePage.OnTextViewClickListener;
import com.example.exerciseapp.listener.TextClickSpan;
import com.umeng.message.PushAgent;

import butterknife.Bind;
/**
 * 协会报名表
 */

public class AtyAssocEntryForm extends BaseActivity {

    private ActionBar actionBar;

    private TextView tvGameNameAssocEntryForm;
    private TextView etApplyUserNameAssocEntryForm;
    private TextView tvSexAssocEntryForm;
    private TextView tvPayFeeAssocEntryForm;
    private EditText etUserAgeAssocEntryForm;
    private EditText etUserNumAssocEntryForm;
    private EditText etUserIdCardAssocEntryForm;
    private EditText etUserEmailAssocEntryForm;
    private EditText etLocationAssocEntryForm;

    private CheckBox cbAgreeRulesAssocEntryForm;
    private TextView tvRules;

    private RequestQueue mRequestQueue;
    private Button btnCommitPersonalEntry;
    private String agreement;

    private static final Pattern agreeRulePattern = Pattern.compile(Config.AGREE_RULE);
    private AlertDialog alertDialogNoCompleteInformation = null;

    private String aId = new String();
    private JSONObject info;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text)
    TextView toolBarTitle;

    private void initView() {
        tvGameNameAssocEntryForm = (TextView) findViewById(R.id.tvGameNameAssocEntryForm);
        tvPayFeeAssocEntryForm = (TextView) findViewById(R.id.tvPayFeeAssocEntryForm);
        try {
            info = new JSONObject(getIntent().getStringExtra(Config.KEY_ASSOC_INFO));
            tvGameNameAssocEntryForm.setText(info.getString(Config.KEY_ANAME));
            tvPayFeeAssocEntryForm.setText(info.getString(Config.KEY_APAY_FEE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        etApplyUserNameAssocEntryForm = (EditText) findViewById(R.id.etApplyUserNameAssocEntryForm);
        tvSexAssocEntryForm = (TextView) findViewById(R.id.tvSexAssocEntryForm);
        tvSexAssocEntryForm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 点击出现选择性别
                new AlertDialog.Builder(AtyAssocEntryForm.this)
                        .setItems(new String[]{"男", "女"},
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        switch (which) {
                                            case 0:
                                                tvSexAssocEntryForm.setText("男");
                                                break;
                                            case 1:
                                                tvSexAssocEntryForm.setText("女");
                                                break;
                                            default:
                                                break;
                                        }
                                    }

                                }).show();

            }
        });

        etUserAgeAssocEntryForm = (EditText) findViewById(R.id.etUserAgeAssocEntryForm);
        etUserNumAssocEntryForm = (EditText) findViewById(R.id.etUserNumAssocEntryForm);
        etUserIdCardAssocEntryForm = (EditText) findViewById(R.id.etUserIdCardAssocEntryForm);
        etUserEmailAssocEntryForm = (EditText) findViewById(R.id.etUserEmailAssocEntryForm);
        etLocationAssocEntryForm = (EditText) findViewById(R.id.etLocationAssocEntryForm);
        tvRules = (TextView) findViewById(R.id.tvRules);
        cbAgreeRulesAssocEntryForm = (CheckBox) findViewById(R.id.cbAgreeRulesAssocEntryForm);

        tvRules.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AtyAssocEntryForm.this, AtyUserLawItem.class);
                intent.putExtra("agreement", agreement);
                startActivity(intent);

            }
        });
        SpannableString ss = new SpannableString(tvRules.getText());
        setKeyworkClickable(tvRules, ss, agreeRulePattern, new TextClickSpan(new OnTextViewClickListener() {

            @Override
            public void setStyle(TextPaint ds) {
                ds.setColor(Color.rgb(58, 145, 173));
                ds.setUnderlineText(true);
            }

            @Override
            public void clickTextView() {
                //TODO
            }
        }));
        cbAgreeRulesAssocEntryForm.setChecked(true);
        cbAgreeRulesAssocEntryForm.setClickable(true);
        cbAgreeRulesAssocEntryForm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!cbAgreeRulesAssocEntryForm.isChecked()) {
                    cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_on_background);
                } else {
                    cbAgreeRulesAssocEntryForm.setButtonDrawable(android.R.drawable.checkbox_off_background);
                }
            }
        });
        cbAgreeRulesAssocEntryForm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO 勾选和不勾选的动作分别是？？？？
                if (isChecked) {
                    Toast.makeText(AtyAssocEntryForm.this, "请阅读协议", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCommitPersonalEntry = (Button) findViewById(R.id.btnCommitPersonalEntry);
        btnCommitPersonalEntry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO
                if (etApplyUserNameAssocEntryForm.getText().toString().equals("")
                        || etUserAgeAssocEntryForm.getText().toString().equals("")
                        || etUserNumAssocEntryForm.getText().toString().equals("")
                        || etUserIdCardAssocEntryForm.getText().toString().equals("")
                        || etUserEmailAssocEntryForm.getText().toString().equals("")
                        || etLocationAssocEntryForm.getText().toString().equals("")
                        ) {

                    alertDialogNoCompleteInformation = new AlertDialog.Builder(AtyAssocEntryForm.this).create();
                    alertDialogNoCompleteInformation.show();
                    alertDialogNoCompleteInformation.getWindow().setContentView(R.layout.alert_dialog_no_complete_information);
                    alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCertainAlertDialogNoCompleteInformation)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    alertDialogNoCompleteInformation.dismiss();
                                }
                            });
                    alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCancelAlertDialogNoCompleteInformation)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    alertDialogNoCompleteInformation.dismiss();
                                }
                            });
                } else {
                    alertDialogNoCompleteInformation = new AlertDialog.Builder(AtyAssocEntryForm.this).create();
                    alertDialogNoCompleteInformation.show();
                    alertDialogNoCompleteInformation.getWindow().setContentView(R.layout.alert_dialog_be_sure_submit);
                    alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCertainAlertDialogBeSureToSubmit)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    alertDialogNoCompleteInformation.dismiss();
                                    try {
                                        String url = Config.SERVER_URL + "Assoc/attendAssoc?"
                                                + Config.KEY_AID + "=" + aId + "&"
                                                + Config.KEY_UID + "=" + Config.getCachedUserUid(getApplicationContext()) + "&"
                                                + Config.KEY_USER_NAME + "=" + URLEncoder.encode(etApplyUserNameAssocEntryForm.getText().toString(), "utf-8") + "&"
                                                + Config.KEY_SEX + "=" + URLEncoder.encode(tvSexAssocEntryForm.getText().toString(), "utf-8") + "&"
                                                + Config.KEY_AGE + "=" + etUserAgeAssocEntryForm.getText().toString() + "&"
                                                + Config.KEY_TEL + "=" + etUserNumAssocEntryForm.getText().toString() + "&"
                                                + Config.KEY_EMAIL + "=" + etUserEmailAssocEntryForm.getText().toString() + "&"
                                                + Config.KEY_ID_CARD + "=" + etUserIdCardAssocEntryForm.getText().toString() + "&"
                                                + Config.KEY_USER_AREA + "=" + URLEncoder.encode(etLocationAssocEntryForm.getText().toString(), "utf-8") + "&";

                                        StringRequest stringRequest = new StringRequest(
                                                Request.Method.GET,
                                                url,
                                                new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String s) {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(s);
                                                            if (jsonObject.getString("result").equals("1")) {
                                                                Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                                                if (!tvPayFeeAssocEntryForm.getText().toString().equals("0")) {
                                                                    Intent intent = new Intent(AtyAssocEntryForm.this, AtyPay.class);
                                                                    intent.putExtra("id", String.valueOf(jsonObject.getJSONObject("data").getInt("id")));
                                                                    intent.putExtra("apayfee", tvPayFeeAssocEntryForm.getText().toString());
                                                                    intent.putExtra(Config.KEY_USER_ATTEND_ENAME, tvGameNameAssocEntryForm.getText().toString());
                                                                    intent.putExtra(Config.KEY_GAME_NAME, tvGameNameAssocEntryForm.getText().toString());
                                                                    intent.putExtra("type", "assoc");
                                                                    startActivity(intent);
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {

                                                    @Override
                                                    public void onErrorResponse(VolleyError volleyError) {
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
                                    } catch (UnsupportedEncodingException e1) {
                                        e1.printStackTrace();
                                    }
//								
//							if(tvPayFeeAssocEntryForm.getText().toString().equals("0")){
//								Intent intent = new Intent(AtyAssocEntryForm.this,AtyPay.class);
//								intent.putExtra(Config.KEY_USER_ATTEND_EPAYFEE, tvPayFeeAssocEntryForm.getText().toString());
//								intent.putExtra(Config.KEY_USER_ATTEND_ENAME, tvGameNameAssocEntryForm.getText().toString());
//								intent.putExtra(Config.KEY_GAME_NAME, tvGameNameAssocEntryForm.getText().toString());
//								startActivity(intent);
//							}
                                    finish();
                                }
                            });
                    alertDialogNoCompleteInformation.getWindow().findViewById(R.id.btnCancelAlertDialogBeSureToSubmit)
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    alertDialogNoCompleteInformation.dismiss();
                                }
                            });
                }


            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.aty_assoc_entry_form);
        mRequestQueue = Volley.newRequestQueue(this);
        aId = getIntent().getStringExtra(Config.KEY_AID);
        agreement = getIntent().getStringExtra("agreement");
        initView();
        setTitleBar();
    }

    public void setTitleBar() {
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        toolBarTitle.setText("申请表");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyAssocEntryForm.this.finish();
            }
        });
    }


    //	private void initActionBar(){
//		actionBar = getActionBar();  
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.actionbar_start_running, null);
//        TextView text = (TextView) view.findViewById(R.id.tvPageTitleOfAll);
//        text.setText("申请表");
//        view.findViewById(R.id.ivBackBtnStartRunning).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//        actionBar.setCustomView(view);
//	}
    public void setClickTextView(TextView textview, SpannableString ss, int start, int end, ClickableSpan cs) {
        ss.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(ss);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setKeyworkClickable(TextView textview, SpannableString ss, Pattern pattern, ClickableSpan cs) {
        Matcher matcher = pattern.matcher(ss.toString());
        while (matcher.find()) {
            String key = matcher.group();
            if (!"".equals(key)) {
                int start = ss.toString().indexOf(key);
                int end = start + key.length();
                setClickTextView(textview, ss, start, end, cs);
            }
        }
    }
}
