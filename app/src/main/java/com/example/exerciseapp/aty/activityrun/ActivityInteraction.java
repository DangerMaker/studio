package com.example.exerciseapp.aty.activityrun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.exerciseapp.MyApplication;
import com.example.exerciseapp.R;
import com.example.exerciseapp.model.Pictureup;
import com.example.exerciseapp.model.UploadInteraction;
import com.example.exerciseapp.myutils.HintDialog;
import com.example.exerciseapp.myutils.UploadImageUtils;
import com.example.exerciseapp.net.rest.RestAdapterUtils;
import com.example.exerciseapp.utils.ScreenUtils;
import com.example.exerciseapp.view.SportFriendGirdView;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

/**
 * Created by sonchcng on 16/7/30.
 */
public class ActivityInteraction extends Activity implements View.OnClickListener, com.amap.api.maps2d.LocationSource,
        AMapLocationListener {
    private EditText hotcardtitle;
    private EditText hotcardcontent;
    private TextView hotcardcommit;
    private ImageView goback;
    Context context;
    private TextView sportlocation;
    private RequestQueue mRequestQueue;
    private static final String BASEURL = "http://101.200.214.68/index.php/Api/Users/uploadMultiPostPic";
    private static final String BASEACTION = "http://101.200.214.68/py/postpri?action=release_post";
    private String intercationContent;
    private SportFriendGirdView gridview_answer;
    private Bitmap bmp;
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;
    private List<Bitmap> list = new ArrayList<Bitmap>();
    private String pathImage;
    private boolean isTake = true;
    List<String> serverimage = new ArrayList<String>();
    private View rootView;
    private MapView mMapView;
    private AMap mMap;
    LocationSource.OnLocationChangedListener mListener;
    LocationManagerProxy mAMapLocationManager;
    private boolean firstGetLocation = true;
    private double latitude = 0.0;
    private double longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_friend_commit_interaction);
        InitView();
        mRequestQueue = Volley.newRequestQueue(this);
    }

    private void InitView() {
        mMapView = (MapView) findViewById(R.id.mapView);
        hotcardtitle = (EditText) findViewById(R.id.hotcardtitle);
        sportlocation = (TextView) findViewById(R.id.sportlocation);
        hotcardcontent = (EditText) findViewById(R.id.hotcardcontent);
        hotcardcommit = (TextView) findViewById(R.id.hotcardcommit);
        goback = (ImageView) findViewById(R.id.goback);
        hotcardcommit.setOnClickListener(this);
        goback.setOnClickListener(this);
        context = ActivityInteraction.this;
        gridview_answer = (SportFriendGirdView) findViewById(R.id.noScrollgridview);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridview_answer.setAdapter(simpleAdapter);
        gridview_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == imageItem.size() - 1) // 点在加号图片上，没有反应
                {
                    return;
                }
                dialog(position);
            }
        });
        gridview_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 10 && position == 0) { //
                    Toast.makeText(ActivityInteraction.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (position == 0) {
                    LayoutInflater inflater = LayoutInflater
                            .from(ActivityInteraction.this);
                    final View dialogView = inflater.inflate(
                            R.layout.dialog_choose_photo, null);
                    final HintDialog dialog = new HintDialog(
                            ActivityInteraction.this, dialogView,
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
                            // 拍照
                            fromCamera();
                            dialog.dismiss();

                        }

                    });

                    btnSelectImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (imageItem.size() == 10) {
                                Toast.makeText(ActivityInteraction.this,
                                        "最多只能添加九张图片", Toast.LENGTH_SHORT).show();
                            } else {
                                fromPicture();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    dialog(position);
                }

            }

        });
        rootView = gridview_answer.getRootView();
        InitMapLoc();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goback:
                finish();
                break;
            case R.id.hotcardcommit:

                TextView tvAnswer = (TextView) findViewById(R.id.etAnswer);
                intercationContent = tvAnswer.getText().toString();
                if (!intercationContent.isEmpty()) {
                    try {
                        doCommitinteraction();
                        hotcardcommit.setClickable(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_SHORT)
                            .show();

                }
                break;
            default:
                break;
        }
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityInteraction.this);
        builder.setMessage("确定删除吗");
        builder.setTitle("删除图片");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                serverimage.remove(position);
                serverimage.notifyAll();
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
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
                    Log.e("*****",""+uri.getPath().toString());

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

            pathImage = path;
            Bitmap addmp = getSmallBitmap(pathImage);
            //拍照成功判断
            if (!TextUtils.isEmpty(pathImage) && isTake && !"".equals(pathImage)) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("itemImage", addmp);
                imageItem.add(map);
                simpleAdapter = new SimpleAdapter(context,
                        imageItem, R.layout.griditem_addpic,
                        new String[]{"itemImage"}, new int[]{R.id.imageView1});
                simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        if (view instanceof ImageView && data instanceof Bitmap) {
                            ImageView i = (ImageView) view;
                            i.setImageBitmap((Bitmap) data);
                            return true;
                        }
                        return false;
                    }
                });
                gridview_answer.setAdapter(simpleAdapter);
                simpleAdapter.notifyDataSetChanged();
                pathImage = null;
            }
            if (!TextUtils.isEmpty(path)) {
                submitPicture(new File(path));
            } else {
                ScreenUtils.show_msg(context, "对不起,无法找到此图片!");
            }
        }
    }

    public void submitPicture(File file) {
        TypedFile typedFile = new TypedFile("application/octet-stream", file);
        RestAdapterUtils.getTeamAPI().uploadPicture(typedFile, new Callback<UploadInteraction>() {
            @Override
            public void success(UploadInteraction uploadInteraction, retrofit.client.Response response) {
                if (uploadInteraction != null && uploadInteraction.getResult() == 1) {
                    Pictureup pictureup = uploadInteraction.getData();
                    serverimage.add(pictureup.getoriPic());
                } else {
                    // 上传 异常
                    ScreenUtils.show_msg(context, uploadInteraction.getDesc());
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
        super.onResume();
        mMapView.onResume();
        setUpMapIfNeeded();
        Bitmap addmp = getSmallBitmap(pathImage);
        //拍照成功判断
        if (!TextUtils.isEmpty(pathImage) && isTake && !"".equals(pathImage)) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[]{"itemImage"}, new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridview_answer.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            pathImage = null;
        }

    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            baos.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bm;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    private void doCommitinteraction() {

        String picstrings = "";
        for (int i = 0; i < serverimage.size(); i++) {
            if (i != serverimage.size() - 1) {
                picstrings = picstrings + serverimage.get(i) + ",";
            } else {
                picstrings = picstrings + serverimage.get(i);
            }
        }
        String url = BASEACTION + "&uid=" + MyApplication.getInstance().getUid()
                + "&token=" + MyApplication.getInstance().getToken()
                + "&version=3.2"
                + "&pic_str=" + picstrings
                + "&content=" + intercationContent
                + "&address="+ sportlocation.getText().toString()
                + "&longitude=" + longitude
                + "&latitude=" + latitude;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("1")) {
                        ScreenUtils.show_msg(ActivityInteraction.this,"发布成功");
                        finish();
                    } else {
                        Toast.makeText(ActivityInteraction.this.getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(ActivityInteraction.this.getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }) {
        };
        mRequestQueue.add(stringRequest);
    }

    void InitMapLoc() {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }

        mMap.setLocationSource(this);// 设置定位监听
        mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (mListener != null && amapLocation != null) {
            latitude = amapLocation.getLatitude();
            longitude = amapLocation.getLongitude();
            GeocodeSearch geocoderSearch = new GeocodeSearch(this);
            geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (i == 0) {
                        sportlocation.setText("" + regeocodeResult.getRegeocodeAddress().getFormatAddress());

                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
            //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
            LatLonPoint qulat = new LatLonPoint(latitude, longitude);

            RegeocodeQuery query = new RegeocodeQuery(qulat, 200, GeocodeSearch.AMAP);

            geocoderSearch.getFromLocationAsyn(query);

            firstGetLocation = false;
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(ActivityInteraction.this);
            /*
             * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mListener=null;
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = mMapView.getMap();
        }
    }

}
