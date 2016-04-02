package com.example.exerciseapp.aty.login;
/*
 * 个性化界面
 */
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.exerciseapp.volley.toolbox.HttpClientUploadManager;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.example.exerciseapp.net.UserGetCodeConnection;
import com.umeng.message.PushAgent;


public class AtyPersonalize extends Activity {

	private static final int CAMERA_REQUEST_CODE = 222;
	private static final int PICTURE_REQUEST_CODE = 444;
	private EditText etHeight,etWeight;
	private String UserHwPath = null;
	private ImageView ivUserHW;
	private Button btnDropPersonalize;
	private Button btnJoinNowPersonalize;
	private int mWH = 90;// 单位dp
	Bitmap bitmap = null;
	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushAgent.getInstance(this).onAppStart();
		setContentView(R.layout.aty_personalize);
		
		context = this;
		etHeight = (EditText) findViewById(R.id.etHeightPersonalize);
		etWeight = (EditText) findViewById(R.id.etWeightPersonalize);
		ivUserHW = (ImageView) findViewById(R.id.ivUserHwPersonalize);
		btnDropPersonalize = (Button) findViewById(R.id.btnDropPersonalize);
		btnJoinNowPersonalize = (Button) findViewById(R.id.btnJoinNowPersonalize);
		
		
		ivUserHW.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(AtyPersonalize.this)
				.setItems(new String[] { "拍照", "从相册中选取"},
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
					int which) {
						dialog.dismiss();
						Intent intent;
						switch (which) {
						
						case 0:
							intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 请求拍照的Action
							// 在onActivityResult中处理拍照结果
							startActivityForResult(intent, CAMERA_REQUEST_CODE);
							break;
						case 1:
							intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");// 选择图片类型
							// 在onActivityResult中处理选择结果
							startActivityForResult(intent, PICTURE_REQUEST_CODE);
							break;
						default:
							break;
						}
						Toast.makeText(AtyPersonalize.this,
						"你选择了: " + which, Toast.LENGTH_SHORT).show();
					}
		
				}).setNegativeButton("取消", null).show();
			}
		});
		
		
		btnJoinNowPersonalize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(etHeight.getText())){
					Toast.makeText(AtyPersonalize.this, "请输入身高", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(etWeight.getText())){
					Toast.makeText(AtyPersonalize.this, "请输入体重", Toast.LENGTH_SHORT).show();
					return;
				}
				if (null != bitmap)
				{
					upload(bitmap,etHeight.getText().toString(),etWeight.getText().toString());
				}else{
					Toast.makeText(AtyPersonalize.this, "请选择头像", Toast.LENGTH_SHORT).show();
					return;
				}
				Config.cacheHeightAndWeight(getApplicationContext(), etHeight.getText().toString(), etWeight.getText().toString());
			}
				
		});
		
		btnDropPersonalize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AtyPersonalize.this,AtySlidingHome.class));
				finish();
			}
		});
		

		
		
		
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null)
		{
			Uri uri = data.getData();
			if (null != uri)
			{
				String imgPath = null;
				ContentResolver resolver = this.getContentResolver();
				String[] columns = { MediaStore.Images.Media.DATA };
				Cursor cursor = null;
				cursor = resolver.query(uri, columns, null, null, null);
				if (Build.VERSION.SDK_INT > 18)// 4.4以后文件选择发生变化，判断处理
				{
					if (requestCode == PICTURE_REQUEST_CODE)// 选择图片
					{
						imgPath = uri.getPath();
						if (!TextUtils.isEmpty(imgPath)
							&& imgPath.contains(":"))
						{
							String imgIndex = imgPath.split(":")[1];
							cursor = resolver.query(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								columns, "_id=?", new String[] { imgIndex },
								null);
						}
					}
				}
				if (null != cursor && cursor.moveToFirst())
				{
					int columnIndex = cursor.getColumnIndex(columns[0]);
					imgPath = cursor.getString(columnIndex);
					cursor.close();
				}
				if (!TextUtils.isEmpty(imgPath))
				{
					UserHwPath = imgPath;
					bitmap = genBitmap(imgPath);
				}
			}
			else if (requestCode == CAMERA_REQUEST_CODE)// 拍照
			{
				// 拍照时，注意小米手机不会保存图片到本地，只可以从intent中取出bitmap, 要特殊处理
				Object object = data.getExtras().get("data");
				if (null != object && object instanceof Bitmap)
				{
					bitmap = (Bitmap) object;
				}
			}
			ivUserHW.setImageBitmap(bitmap);
			HttpClientUploadManager.saveBitmap2file(context,bitmap, "myAvatar.png");
			
		}
	}
	
	/**上传图片到服务器*/
	private void upload(final Bitmap bitmap,final String height, final String weight)
	{

		new AsyncTask<Bitmap, Void, String>()
		{
			ProgressDialog progressDialog;

			protected void onPreExecute()
			{
				progressDialog = new ProgressDialog(AtyPersonalize.this);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setMessage("正在和服务器通信中……");
				progressDialog.show();
			};

			@Override
			protected String doInBackground(Bitmap... params)
			{
				new UserGetCodeConnection(Config.SERVER_URL+"Users/updateHWNew", Config.POST, new UserGetCodeConnection.SuccessCallBack() {
					
					@Override
					public void onSuccess(String result) {
						Toast.makeText(AtyPersonalize.this, result, Toast.LENGTH_LONG).show();
						return;
					}
				}, new UserGetCodeConnection.FailCallBack() {
					
					@Override
					public void onFail(String result) {
						Toast.makeText(AtyPersonalize.this, result,Toast.LENGTH_LONG).show();
						return;
					}
				}, Config.KEY_UID,Config.getCachedUserUid(getApplicationContext()),
				Config.KEY_HEIGHT,height,Config.KEY_WEIGHT,
				weight);
				HashMap<String,String> map = new HashMap<String, String>();
				map.put(Config.KEY_UID, Config.getCachedUserUid(getApplicationContext()));
				return HttpClientUploadManager.upload(Config.SERVER_URL+"Users/updateHeaderNew", context.getFilesDir()+"/myAvatar.png", "myAvatar", map);
			}

			protected void onPostExecute(String result)
			{
				progressDialog.dismiss();
				if (result!=null&&!result.equals(""))
				{
					try
					{
						JSONObject jsonObject = new JSONObject(result);
						if (null != jsonObject
							&& "1".equals(jsonObject.getString("result")))
						{// 上传成功
							Toast.makeText(AtyPersonalize.this, "上传成功",
								Toast.LENGTH_SHORT).show();
							Config.cacheUserHwURL(getApplicationContext(), jsonObject.getString(Config.KEY_FILE_URL));
							startActivity(new Intent(AtyPersonalize.this,AtySlidingHome.class));
							finish();
						}
						Toast.makeText(getApplicationContext(), jsonObject.getString("desc"), Toast.LENGTH_SHORT).show();
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					Toast.makeText(AtyPersonalize.this, "上传失败",
						Toast.LENGTH_SHORT).show();
				}
			};
		}.execute(bitmap);
	}
	
	
	
	/**通过给定的图片路径生成对应的bitmap*/
	private Bitmap genBitmap(String imgPath)
	{
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;

		int widthSample = (int) (imageWidth / mWH);
		int heightSample = (int) (imageHeight / mWH);
		// 计算缩放比例
		options.inSampleSize = widthSample < heightSample ? heightSample
			: widthSample;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imgPath, options);
	}
}
