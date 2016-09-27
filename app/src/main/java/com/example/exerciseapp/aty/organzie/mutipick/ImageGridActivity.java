package com.example.exerciseapp.aty.organzie.mutipick;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.organzie.BindActivity;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.Pictureup;
import com.example.exerciseapp.model.UploadInteraction;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.PictureUtil;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.utils.UiUtils;
import com.ta.utdid2.android.utils.SystemUtils;

import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;


public class ImageGridActivity extends BindActivity implements OnClickListener {
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    List<ImageBucket> imageBuckets;
    List<ImageItem> dataList;
    ImageGridAdapter adapter;
    AlbumHelper helper;
    public static Bitmap bimap;
    GridView mGridview;
//    Button mFinish;

    private int maxImages;
    private int number;
    private List<String> list;
//    private TextView txtRight;

    String orgId;
    String albumId;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        findViewById();
        Intent intent = getIntent();
        maxImages = intent.getIntExtra("ezMaxImages", 9);
        number = intent.getIntExtra("number", 0);
//        list = intent.getStringArrayListExtra("initImage");
        list = new ArrayList<>();
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
//        EventBus.getDefault().register(this);
        initData();
        initView();

        orgId = getIntent().getStringExtra("orgid");
        albumId = getIntent().getStringExtra("albumid");
    }

    @OnClick(R.id.photo_sub)
    public void sub() {
        System.out.println("sub");
        showDialog();

        List<String> paths = adapter.getPaths();
//        for (int i = 0; i < paths.size(); i++) {
//            File file = new File(paths.get(i));
////            submitPicture(file);
//        }
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("uid", new TypedString(MyApplication.uid));
        multipartTypedOutput.addPart("version", new TypedString("3.3.0"));
        multipartTypedOutput.addPart("org_id", new TypedString(orgId));
        multipartTypedOutput.addPart("album_id", new TypedString(albumId));
        multipartTypedOutput.addPart("token", new TypedString(MyApplication.token));

        for (int i = 0; i < paths.size(); i++) {
//            File file = new File(paths.get(i));
            File file = PictureUtil.scal(paths.get(i));
            multipartTypedOutput.addPart("pic" + (i+1) , new TypedFile("multipart/form-data", file));
        }

        RestAdapterUtils.getZhuAPI().updateFile( multipartTypedOutput, new Callback<ErrorMsg>() {

            @Override
            public void success(ErrorMsg errorMsg, Response response) {
                System.out.println("成功");
                if (errorMsg != null && errorMsg.getResult() == 1) {
                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
                    setResult(300);
                    finish();

                } else {
                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                closeDialog();
                ScreenUtils.show_msg(ImageGridActivity.this, "上传失败!");
            }
        });


//        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
//        for (int i = 0; i < paths.size(); i++) {
//            File file = new File(paths.get(i));
//            multipartTypedOutput.addPart("pic" + i , new TypedFile("multipart/form-data", file));
//        }
//        TypedFile typeFile =  new TypedFile("multipart/form-data", file);
//        RestAdapterUtils.getZhuAPI().updateFile("3","3.3.0","3","40","871c845b50a98d647215bceaa87fee74",typeFile,new Callback<ErrorMsg>(){
//
//            @Override
//            public void success(ErrorMsg errorMsg, Response response) {
//                System.out.println("成功");
//                if (errorMsg != null && errorMsg.getResult() == 1) {
//                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
//                } else {
//                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });


    }

    List<String> serverimage = new ArrayList<String>();

    public void submitPicture(File file) {
//        TypedFile typedFile = new TypedFile("application/octet-stream", file);
//        RestAdapterUtils.getZhuAPI().updateFile(MyApplication.uid, "3.3.0", orgId, albumId, MyApplication.token, typedFile, new Callback<ErrorMsg>() {
//
//            @Override
//            public void success(ErrorMsg errorMsg, Response response) {
//                System.out.println("成功");
//                if (errorMsg != null && errorMsg.getResult() == 1) {
//                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
//                } else {
//                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });


//        RestAdapterUtils.getZhuAPI().uploadPicture(typedFile, new Callback<UploadInteraction>() {
//            @Override
//            public void success(UploadInteraction uploadInteraction, retrofit.client.Response response) {
//                if (uploadInteraction != null && uploadInteraction.getResult() == 1) {
//                    Pictureup pictureup = uploadInteraction.getData();
//                    serverimage.add(pictureup.getoriPic());
//
//                    if(adapter.getPaths().size() == serverimage.size()){
//                        String picstrings = "";
//                        for (int i = 0; i < serverimage.size(); i++) {
//                            if (i != serverimage.size() - 1) {
//                                picstrings = picstrings + serverimage.get(i) + ",";
//                            } else {
//                                picstrings = picstrings + serverimage.get(i);
//                            }
//                        }
//                        RestAdapterUtils.getZhuAPI().updateAlbum(MyApplication.uid, "release_album", "3.3.0", MyApplication.token, picstrings, albumId, orgId, new Callback<ErrorMsg>() {
//                            @Override
//                            public void success(ErrorMsg errorMsg, Response response) {
//                                System.out.println("成功");
//                                if (errorMsg != null && errorMsg.getResult() == 1) {
//                                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
//                                } else {
//                                    ScreenUtils.show_msg(ImageGridActivity.this, errorMsg.getDesc());
//                                }
//                            }
//
//                            @Override
//                            public void failure(RetrofitError error) {
//
//                            }
//                        });
//                    }
//
//                } else {
//                    // 上传 异常
//                    ScreenUtils.show_msg(context, uploadInteraction.getDesc());
//                }
//                closeDialog();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                closeDialog();
//                ScreenUtils.show_msg(context, "上传失败!");
//            }
//        });
    }

    private void findViewById() {
        mGridview = (GridView) findViewById(R.id.gridview);
//        mFinish = (Button) findViewById(R.id.btn_finish);
//        RelativeLayout goBack = (RelativeLayout) findViewById(R.id.btn_go_back);
//        goBack.setVisibility(View.VISIBLE);
//        goBack.setOnClickListener(this);
//        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
//        RelativeLayout newChat = (RelativeLayout) findViewById(R.id.btn_new_chat);
//        findViewById(R.id.img_add).setVisibility(View.GONE);
//        newChat.setVisibility(View.VISIBLE);
//        txtRight = (TextView) findViewById(R.id.txt_right);
//        txtRight.setTextColor(Color.WHITE);
//        txtRight.setVisibility(View.VISIBLE);
//        newChat.setOnClickListener(this);
//        toolbarTitle.setText("相机图库");
//        mFinish.setOnClickListener(this);
    }

    //    public void onEventMainThread(ImageMaxEvent event){
//        Toast.makeText(this,"最多选择"+ maxImages +"张图片",Toast.LENGTH_LONG).show();
//    }
    private void initData() {
        imageBuckets = helper.getImagesBucketList(false);
        dataList = new LinkedList<>();
        for (ImageBucket bucket : imageBuckets) {
            for (ImageItem item : bucket.imageList) {
                item.isSelected = false;
                if (list != null) {
                    if (list.contains(item.imagePath))
                        item.isSelected = true;
                }
                dataList.add(item);
            }
        }
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);
    }


    private void initView() {
        mGridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(this, dataList, maxImages, number);
//        txtRight.setText("完成");
        mGridview.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
//        if(i == R.id.btn_new_chat){
//            Intent intent = new Intent();
//            intent.putExtra("paths", adapter.mPaths);
//            setResult(0x0088,intent);
//            finish();
//        }else if(i == R.id.btn_go_back){
//            finish();
//        }
    }

    private ProgressDialog dialog;
    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
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

}
