package com.example.exerciseapp.aty.team;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.GroupInstance;
import com.example.exerciseapp.model.UpTeamAvatar;
import com.example.exerciseapp.myutils.UploadImageUtils;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by lyjq on 2016/3/30.
 */
public class TeamSettingActivity extends BackBaseActivity {


    @Bind(R.id.team_setting_id)
    TextView teamSettingId;
    @Bind(R.id.team_setting_name)
    RelativeLayout teamSettingName;
    @Bind(R.id.team_setting_des)
    RelativeLayout teamSettingDes;
    @Bind(R.id.team_setting_manager)
    RelativeLayout teamSettingManager;
    @Bind(R.id.team_setting_invate)
    RelativeLayout teamSettingInvate;
    @Bind(R.id.team_setting_disappear)
    Button teamSettingDisappear;

    @Bind(R.id.team_setting_name_text)
    TextView nameTextView;
    @Bind(R.id.team_setting_des_text)
    TextView desTextView;
    @Bind(R.id.team_setting_img)
    SimpleDraweeView img;
    @Bind(R.id.team_setting_alter_img)
    RelativeLayout alterImg;

    Intent intent;
    private int teamId;
    private String name;
    private String des;
    private String avatar;
    String token = MyApplication.getInstance().getToken();
    String uid = MyApplication.getInstance().getUid();
    String version = "3.0";
    String type;

    public static Intent getTeamSettingIntent(Context context, String type) {
        Intent intent = new Intent(context, TeamSettingActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addData();
    }

    private void addData() {
        teamId = GroupInstance.getInstance().getId();
        name = GroupInstance.getInstance().getGroup_name();
        des = GroupInstance.getInstance().getIntro();
        avatar = GroupInstance.getInstance().getAvatar();

        teamSettingId.setText("团队ID:" + teamId);
        nameTextView.setText(name);
        desTextView.setText(des);
        img.setImageURI(Uri.parse(avatar));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_setting);
        setTitleBar("团队设置");

        intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("group_info_return")) {
            teamSettingName.setClickable(false);
            teamSettingDes.setClickable(false);
            alterImg.setClickable(false);
            teamSettingDisappear.setText("退出团队");
        } else {
            teamSettingDisappear.setText("解散团队");
        }
//        teamId = intent.getIntExtra("teamId", -1);
//        name = intent.getStringExtra("name");
//        des = intent.getStringExtra("des");
    }

    @OnClick({R.id.team_setting_name, R.id.team_setting_des, R.id.team_setting_invate, R.id.team_setting_disappear, R.id.team_setting_manager})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.team_setting_name:
                startActivity(AlterTeamNameActivity.getAlterNameIntent(this, teamId, name));
                break;
            case R.id.team_setting_des:
                startActivity(AlterTeamDesActivity.getAlterDesIntent(this, teamId, des));
                break;
            case R.id.team_setting_invate:
                startActivity(AddMemberActivity.getAddMemberIntent(this, teamId));
                break;
            case R.id.team_setting_disappear:
                if (type.equals("group_info_return")) {
                    exitGroup();
                } else {
                    breakGroup();
                }
                break;
            case R.id.team_setting_manager:
                if (type.equals("group_info_return")) {
                    startActivity(CheckMembersActivity.getCheckMembersIntent(this, teamId));
                } else {
                    startActivity(CheckMembersActivity.getManagerMembersIntent(this, teamId));
                }
                break;
        }
    }

    public static final int IMG_FROM_PICTURE = 0x1001;
    public static final int IMG_FROM_CAMERA = 0x1002;

    @OnClick(R.id.team_setting_alter_img)
    public void img() {
        new AlertDialog.Builder(this)
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

    private void breakGroup() {
        RestAdapterUtils.getTeamAPI().breakGroup(teamId, getUid(), "break_group", token, version, new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("解散成功");
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(TeamSettingActivity.this, "解散成功");
                    setResult(2);
                    finish();
                } else {
                    ScreenUtils.show_msg(TeamSettingActivity.this, errorMsg.getDesc());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("解散失败");
                ScreenUtils.show_msg(TeamSettingActivity.this, "添加失败");
            }
        });
    }

    private void exitGroup() {
        RestAdapterUtils.getTeamAPI().exitGroup(teamId, getUid(), "exit_group", token, version, new Callback<ErrorMsg>() {
            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("退出成功");
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(TeamSettingActivity.this, "退出成功");
                    setResult(2);
                    finish();
                } else {
                    ScreenUtils.show_msg(TeamSettingActivity.this, errorMsg.getDesc());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("退出失败");
                ScreenUtils.show_msg(TeamSettingActivity.this, "退出失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        outputAvatarPath = UploadImageUtils.getAvatarPath(this);
        if (!TextUtils.isEmpty(outputAvatarPath)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File tmpFile = new File(outputAvatarPath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));
            startActivityForResult(intent, IMG_FROM_CAMERA);
        } else {
            ScreenUtils.show_msg(this, "未插入SD卡");
        }
    }


    void fromPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_FROM_PICTURE);
    }

    private String outputAvatarPath = "";

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
                    path = UploadImageUtils.writeUriToSDcard(TeamSettingActivity.this, uri);
                } else {
                    path = UploadImageUtils.writeStringToSDcard(TeamSettingActivity.this, outputAvatarPath);
                }

            } catch (IOException e) {
                ScreenUtils.show_msg(TeamSettingActivity.this, "对不起,无法找到此图片!");
            }
            return path;
        }

        @Override
        protected void onPostExecute(Object result) {
            String path = (String) result;
            if (!TextUtils.isEmpty(path)) {
                submitUserIcon(new File(path));
            } else {
                ScreenUtils.show_msg(TeamSettingActivity.this, "对不起,无法找到此图片!");
            }
        }
    }

    public void submitUserIcon(File file) {
        TypedFile typedFile = new TypedFile("application/octet-stream", file);
        RestAdapterUtils.getTeamAPI().uploadTeamAvatar(typedFile, teamId + "", new Callback<UpTeamAvatar>() {
            @Override
            public void success(UpTeamAvatar upTeamAvatar, Response response) {
                if (upTeamAvatar != null && upTeamAvatar.getResult() == 1) {
//                    String avatar = errorMessage.getErrorMessage();
//                    if (adapter != null && adapter.getHeadImageView() != null && avatar != null)
//                        adapter.getHeadImageView().setImageURI(Uri.parse(avatar));

//                        list.set(0, new UserCenterEdit("头像", avatar));
                    img.setImageURI(Uri.parse(upTeamAvatar.getData()));
                    ScreenUtils.show_msg(TeamSettingActivity.this, "上传头像成功!");
                    GroupInstance.getInstance().setAvatar(upTeamAvatar.getData());
//                    if (userService != null) userService.updateAccountAvatar(avatar);
                } else {
                    // 上传 异常
                    ScreenUtils.show_msg(TeamSettingActivity.this, "上传头像失败!");
                }
                closeDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                closeDialog();
                ScreenUtils.show_msg(TeamSettingActivity.this, "上传头像失败!");
            }
        });
    }
}