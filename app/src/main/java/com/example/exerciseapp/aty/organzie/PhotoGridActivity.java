package com.example.exerciseapp.aty.organzie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.organzie.PhotoListAdapter.ListAdapterListener;
import com.example.exerciseapp.aty.organzie.mutipick.ImageGridActivity;
import com.example.exerciseapp.aty.organzie.mutipick.Photo;
import com.example.exerciseapp.aty.team.BackBaseActivity;
import com.example.exerciseapp.model.AlbumId;
import com.example.exerciseapp.model.AlbumInfo;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.PhotoModel;
import com.example.exerciseapp.model.Role;
import com.example.exerciseapp.myutils.SelectPopupWindow;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PhotoGridActivity extends BindActivity implements View.OnClickListener, ListAdapterListener {

    @Bind(R.id.recycler)
    RecyclerView recyclerView;

    @Bind(R.id.oranzie_sub)
    TextView setting;

    @Bind(R.id.update_photo)
    Button mUpdate;
    PhotoListAdapter adapter;
    private SelectPopupWindow menuWindow;

    String orgId;
    String albumId;
    String albumName;

    String lastId = "0";
    boolean end = false;
    List<PhotoModel.DataBean> mData = new ArrayList<>();

    List<PhotoModel.DataBean> mTemp;

    @Bind(R.id.toolbar_text)
    TextView toolbarText;

    @OnClick(R.id.goback)
    public void goback(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);


        orgId = getIntent().getStringExtra("orgid");
        albumId = getIntent().getStringExtra("albumid");

        adapter = new PhotoListAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter.setListener(this);

        RestAdapterUtils.getZhuAPI().getUserRole(MyApplication.uid, "get_user_role", "3.3.0", MyApplication.uid, albumId, orgId, new Callback<Role>() {
            @Override
            public void success(Role role, Response response) {
                if (role.getData().getRole_id() == 0) {
                    setting.setVisibility(View.GONE);
                    mUpdate.setEnabled(false);
                    mUpdate.setBackgroundColor(getResources().getColor(R.color.gray));
                } else if (role.getData().getRole_id() == 1) {
                    setting.setVisibility(View.VISIBLE);
                } else if (role.getData().getRole_id() == 2) {
                    setting.setVisibility(View.VISIBLE);
                } else if (role.getData().getRole_id() == 3) {
                    setting.setVisibility(View.GONE);
                    mUpdate.setEnabled(false);
                    mUpdate.setBackgroundColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        getInfo();
        load();
    }

    private void merge() {

        for (int i = 0; i < mData.size() - 1; i++) {
            for (int j = mData.size() - 1; j > i; j--) {
                if (mData.get(i).getDate().equals(mData.get(j).getDate())) {
                    mData.get(i).getPic_list().addAll(mData.get(j).getPic_list());
                    mData.remove(j);
                }
            }
        }
    }

    private void getInfo() {
        RestAdapterUtils.getZhuAPI().getAlbumInfo(MyApplication.uid, "get_album_info", "3.3.0", albumId, new Callback<AlbumInfo>() {
            @Override
            public void success(AlbumInfo albumInfo, Response response) {
                if (albumInfo != null) {
                    albumName = albumInfo.getData().getName();
                    toolbarText.setText(albumName);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void load() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ymd = dateFormat.format(now);

        RestAdapterUtils.getZhuAPI().getAlbumList(MyApplication.uid, "get_album_pic_list", "3.3.0", orgId, albumId, ymd, lastId, new Callback<PhotoModel>() {
            @Override
            public void success(PhotoModel photoModel, Response response) {
                if (photoModel != null && photoModel.getData().size() != 0) {
                    mData.addAll(photoModel.getData());
                    merge();

                    adapter.updateItems(mData);

                    PhotoModel.DataBean bean = mData.get(mData.size() - 1);
                    lastId = Integer.toString(bean.getPic_list().get(bean.getPic_list().size() - 1).getId());
                } else {
                    end = true;
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @OnClick(R.id.oranzie_sub)
    public void createAlbum(View view) {
        showSelectorPopup();
    }

    @OnClick(R.id.update_photo)
    public void updatePhoto(View view) {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra("orgid", orgId);
        intent.putExtra("albumid", albumId);
        startActivityForResult(intent, 300);
    }

    private void showSelectorPopup() {
        View v = LayoutInflater.from(this).inflate(R.layout.item_create_album, null);
        menuWindow = new SelectPopupWindow(this.getWindow().getDecorView(), this, v);
        v.findViewById(R.id.create_album).setOnClickListener(this);
        v.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        adapter.clear();
        end = false;
        lastId = "0";
        mData.clear();
        if (intent != null) {
            albumId = intent.getStringExtra("albumId");
            albumName = intent.getStringExtra("albumName");
            toolbarText.setText(albumName);
            setting.setVisibility(View.VISIBLE);
            mUpdate.setEnabled(true);
            mUpdate.setBackgroundResource(R.drawable.shape_btn_blue_radius_5dip);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 300) {
            adapter.clear();
            lastId = "0";
            end = false;
            mData.clear();
            load();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_album:
                menuWindow.dismiss();
                final AlbumDialog.Builder builder = new AlbumDialog.Builder(this);
                builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {
                        String name = ((EditText) builder.getRootView().findViewById(R.id.edit)).getText().toString();
                        if (name == null) {
                            Toast.makeText(PhotoGridActivity.this, "请输入相册名称", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        RestAdapterUtils.getZhuAPI().createAlbum(MyApplication.uid, "create_album", "3.3.0", MyApplication.token, orgId, name, new Callback<AlbumId>() {
                            @Override
                            public void success(AlbumId errorMsg, Response response) {
                                if (errorMsg.getResult() == 1) {
                                    Intent intent = new Intent(PhotoGridActivity.this, PhotoGridActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.putExtra("albumId", errorMsg.getData().getId());
                                    intent.putExtra("albumName", errorMsg.getData().getName());
                                    startActivity(intent);

                                } else {
                                    ScreenUtils.show_msg(PhotoGridActivity.this, errorMsg.getDesc());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                builder.create().show();
                break;
            case R.id.btn_cancel:
                menuWindow.dismiss();
                break;

        }
    }

    @Override
    public void onListEnded() {
        if (!end) {
            load();
        }
    }
}
