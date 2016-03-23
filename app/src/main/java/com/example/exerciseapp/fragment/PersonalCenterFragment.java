package com.example.exerciseapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.HttpClientUploadManager;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.login.AtyLogin;
import com.example.exerciseapp.aty.login.AtyPersonalize;
import com.example.exerciseapp.aty.sliding.AtyMessage;
import com.example.exerciseapp.aty.sliding.AtyMyClubAndAssociation;
import com.example.exerciseapp.aty.sliding.AtyMyEntryForm;
import com.example.exerciseapp.aty.sliding.AtyMyFocus;
import com.example.exerciseapp.aty.sliding.AtyMyGrades;
import com.example.exerciseapp.aty.sliding.AtyRunningRecord;
import com.example.exerciseapp.aty.sliding.AtyScoresManager;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.example.exerciseapp.aty.sliding.AtyUserInformationConfig;
import com.example.exerciseapp.aty.sliding.AtyUserPreferedProject;
import com.example.exerciseapp.aty.sliding.AtyVenuesReservation;
import com.example.exerciseapp.net.GetInterestConnection;
import com.example.exerciseapp.net.UserGetCodeConnection;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PersonalCenterFragment extends Fragment implements OnClickListener {

	SpotsDialog spotsDialog;
	private static CircleImageView ibUserHWPersonalCenter;//用户头像
	private TextView tvUserNamePersonalCenter;		//用户名字
	private TextView tvUserBirthdayPersonalCenter;	//用户生日
	private ImageView ivMessageButtonPersonalCenter;//消息按钮
	private TextView tvUserPointPersonalCenter;//用户积分
	private TextView tvUserAttrPersonalCenter;		//用户会员
	private TextView tvUserPreferProjectPersonalCenter;//用户喜爱的项目
	private TextView tvSelectUserPreferProjectPersonalCenter;//用户已经选择的项目
	
	private TextView tvExerciseRecordPersonalCenter;	//运动记录
	private TextView tvExerciseTimePersonalCenter;	//用户锻炼时间
	private TextView tvExerciseDistancePersonalCenter;//用户锻炼距离
	
	private TableRow UserConfigPersonalCenter;	//个人设置
	private TableRow MyEntryFormPersonalCenter;	//我的报名
//	private TableRow VenuesReservationPersonalCenter;//场馆预约
	private TableRow MyClubAndAssocPersonalCenter;//我的协会/俱乐部
	private TableRow MyGradesPersonalCenter;//我的成绩
//	private TableRow ScoresManagerPersonalCenter;//积分管理
//	private TableRow MyFocusPersonalCenter;		//我的关注
	
	private AlertDialog alertDialog = null;
	private List<Map<String,String>> alertItem = new ArrayList<Map<String,String>>();
	
	private RequestQueue mRequestQueue;  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page_personal_center, null);
		context = getActivity();
		mRequestQueue =  Volley.newRequestQueue(getActivity());
		//控件初始化
		ibUserHWPersonalCenter = (CircleImageView) view.findViewById(R.id.ibUserHWPersonalCenter);
		tvUserNamePersonalCenter = (TextView) view.findViewById(R.id.tvUserNamePersonalCenter);
		tvUserBirthdayPersonalCenter = (TextView) view.findViewById(R.id.tvUserBirthdayPersonalCenter);
		ivMessageButtonPersonalCenter = (ImageView) view.findViewById(R.id.ivMessageButtonPersonalCenter);
		tvUserAttrPersonalCenter = (TextView) view.findViewById(R.id.tvUserAttrPersonalCenter);
		tvUserPointPersonalCenter = (TextView) view.findViewById(R.id.tvUserPointPersonalCenter);
		tvUserPreferProjectPersonalCenter = (TextView) view.findViewById(R.id.tvUserPreferProjectPersonalCenter);
		tvSelectUserPreferProjectPersonalCenter = (TextView) view.findViewById(R.id.tvSelectUserPreferProjectPersonalCenter);
		tvExerciseTimePersonalCenter = (TextView) view.findViewById(R.id.tvExerciseTimePersonalCenter);
		tvExerciseDistancePersonalCenter = (TextView) view.findViewById(R.id.tvExerciseDistancePersonalCenter);
		tvExerciseRecordPersonalCenter = (TextView) view.findViewById(R.id.tvExerciseRecordPersonalCenter);
		
		
		UserConfigPersonalCenter = (TableRow) view.findViewById(R.id.UserConfigPersonalCenter);
		MyEntryFormPersonalCenter = (TableRow) view.findViewById(R.id.MyEntryFormPersonalCenter);
//		VenuesReservationPersonalCenter = (TableRow) view.findViewById(R.id.VenuesReservationPersonalCenter);
		MyClubAndAssocPersonalCenter = (TableRow) view.findViewById(R.id.MyClubAndAssocPersonalCenter);
		MyGradesPersonalCenter = (TableRow) view.findViewById(R.id.MyGradesPersonalCenter);
//		ScoresManagerPersonalCenter = (TableRow) view.findViewById(R.id.ScoresManagerPersonalCenter);
//		MyFocusPersonalCenter = (TableRow) view.findViewById(R.id.MyFocusPersonalCenter);
		
		//头像   用户名等初始化
		if(Config.getCachedUserHwURL(getActivity().getApplicationContext())!=null){
			Picasso.with(getActivity()).load(Config.getCachedUserHwURL(getActivity().getApplicationContext())).into(ibUserHWPersonalCenter);
		}
		if(Config.getCachedBriefUserInformation(getActivity().getApplicationContext())!=null){
			JSONObject json = Config.getCachedBriefUserInformation(getActivity().getApplicationContext());
			try {
				if(Config.TOURIST_MODE){
					tvUserNamePersonalCenter.setText("游客");
					ivMessageButtonPersonalCenter.setVisibility(View.INVISIBLE);
				}else{
					tvUserNamePersonalCenter.setText(json.getString(Config.KEY_USER_NAME));
					tvUserBirthdayPersonalCenter.setText(json.getString(Config.KEY_USER_BIRTHDAY));
					tvUserAttrPersonalCenter.setText(json.getString(Config.KEY_ASSOC));
					tvUserPointPersonalCenter.setText("积分"+json.getString("point"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(Config.STATUS_HAS_MESSAGE){
			ivMessageButtonPersonalCenter.setImageResource(R.drawable.has_message_icon);
		}else{
			ivMessageButtonPersonalCenter.setImageResource(R.drawable.no_message_icon);
		}
		
		//选择喜爱项目
		tvUserPreferProjectPersonalCenter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO 与服务器通信得到所有的项目和用户已经选择了的项目
				startActivity(new Intent(getActivity(),AtyUserPreferedProject.class));
			}
		});
		
		
		//消息按钮点击跳转（目前缺少状态判断）
		ivMessageButtonPersonalCenter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),AtyMessage.class));
			}
		});
		
		//更换头像
		view.findViewById(R.id.ibUserHWPersonalCenter).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Builder(getActivity())
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
//						Toast.makeText(getActivity(),
//						"你选择了: " + which, 5).show();
					}
		
				}).setNegativeButton("取消", null).show();
			}
		});
		//跳转历史记录
		tvExerciseRecordPersonalCenter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),AtyRunningRecord.class));
			}
		});
		
		//点击个人设置  我的报名 场馆预约 我的俱乐部 我的成绩 积分管理和我的关注跳转响应页面
		UserConfigPersonalCenter.setOnClickListener(this);
		MyEntryFormPersonalCenter.setOnClickListener(this);
//		VenuesReservationPersonalCenter.setOnClickListener(this);
		MyClubAndAssocPersonalCenter.setOnClickListener(this);
		MyGradesPersonalCenter.setOnClickListener(this);
//		ScoresManagerPersonalCenter.setOnClickListener(this);
//		MyFocusPersonalCenter.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		spotsDialog = new SpotsDialog(getActivity());
		spotsDialog.show();
		StringRequest  stringRequestUserInformation = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL+"Users/getUserRun",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            	tvExerciseTimePersonalCenter.setText("时间："+jsonObject.getString("duration")+"h");
                            	tvExerciseDistancePersonalCenter.setText("距离："+jsonObject.getString("distance")+"km");
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
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(Config.KEY_UID,Config.getCachedUserUid(getActivity().getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequestUserInformation);
		//得到用户喜欢的健身项目
        StringRequest  stringRequest = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL+"Users/getUserInterest",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray jsonArr = jsonObject.getJSONArray("data");
                            String loved = new String();
                            for(int i=0;i<jsonArr.length();i++){
                            	loved = loved.concat(jsonArr.getJSONObject(i).getString("iname")+" ");
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
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(Config.KEY_UID,Config.getCachedUserUid(getActivity().getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequest);
        
		if(Config.STATUS_HAS_MESSAGE){
			ivMessageButtonPersonalCenter.setImageResource(R.drawable.has_message_icon);
		}else{
			ivMessageButtonPersonalCenter.setImageResource(R.drawable.no_message_icon);
		}
		
		StringRequest  stringRequestUserInformation1 = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL+"Users/userInfoNew",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json = new JSONObject(s);
                            if(json.getInt("result") == 1){
                        	    tvUserNamePersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_USER_NAME));
              					tvUserBirthdayPersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_USER_BIRTHDAY));
              					tvUserAttrPersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_ASSOC));
              					tvUserPointPersonalCenter.setText("积分"+json.getString("point"));
              					if(!Config.STATUS_SUBMIT_USER_HW){
              						Picasso.with(getActivity()).load(json.getJSONObject("data").getString(Config.KEY_AVATAR)).into(ibUserHWPersonalCenter);
              					}
              					
                          }else{
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
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(Config.KEY_UID,Config.getCachedUserUid(getActivity().getApplicationContext()));
                return map;
            }
        };
        mRequestQueue.add(stringRequestUserInformation1);
		//用户简介
//		 StringRequest  stringRequestUserBriefInformation = new StringRequest(
//	              Request.Method.POST,
//	              Config.SERVER_URL+"Users/briefUserInfoNew",
//	              new Response.Listener<String>() {
//	
//	                  @Override
//	                  public void onResponse(String s) {
//	                      try {
//	                          JSONObject json = new JSONObject(s);
//	                          if(json.getInt("result") == 1){
//	                        	    tvUserNamePersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_USER_NAME));
//	              					tvUserBirthdayPersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_USER_BIRTHDAY));
//	              					tvUserAttrPersonalCenter.setText(json.getJSONObject("data").getString(Config.KEY_ASSOC));
//	              					if(!Config.STATUS_SUBMIT_USER_HW){
//	              						Picasso.with(getActivity()).load(json.getJSONObject("data").getString(Config.KEY_AVATAR)).into(ibUserHWPersonalCenter);
//	              					}
//	                          }else{
//	                        	  Toast.makeText(getActivity(), json.getString("desc"), 2).show();
//	                          }
//	                          
//	                      } catch (JSONException e) {
//	                          e.printStackTrace();
//	                      }
//	                  }
//	              },
//	              new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						// TODO Auto-generated method stub
//						Toast.makeText(getActivity(), Config.CONNECTION_ERROR, 2).show();
//					}
//	              }){
//	
//	          @Override
//	          protected Map<String, String> getParams() throws AuthFailureError {
//	              Map<String,String> map = new HashMap<String,String>();
//	              map.put(Config.KEY_UID, Config.getCachedUserUid(getActivity().getApplicationContext()));
//	              return map;
//	          }
//	      };
//	      mRequestQueue.add(stringRequestUserBriefInformation);
		
	      if(Config.STATUS_SUBMIT_USER_HW){
				if(Config.getCachedUserHw(getActivity().getApplicationContext())!=null){
					ibUserHWPersonalCenter.setImageBitmap(Config.getCachedUserHw(getActivity().getApplicationContext()));
				}
				Config.STATUS_SUBMIT_USER_HW = false;
			}else{
			}
	      
		
//		
//		if(Config.getCachedBriefUserInformation(getActivity().getApplicationContext())!=null){
//			JSONObject json = Config.getCachedBriefUserInformation(getActivity().getApplicationContext());
//			try {
//				tvUserNamePersonalCenter.setText(json.getString(Config.KEY_USER_NAME));
//				tvUserBirthdayPersonalCenter.setText(json.getString(Config.KEY_USER_BIRTHDAY));
//				tvUserAttrPersonalCenter.setText(json.getString(Config.KEY_ASSOC));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
		
		
	}

	
	
	@Override
	public void onClick(View v) {
//		spotsDialog = new SpotsDialog(getActivity());
//		spotsDialog.show();
		switch (v.getId()) {
		case R.id.UserConfigPersonalCenter:
//			StringRequest  stringRequestUserInformation = new StringRequest(
//                    Request.Method.POST,
//                    Config.SERVER_URL+"Users/userInfoNew",
//                    new Response.Listener<String>() {
// 
//                        @Override
//                        public void onResponse(String s) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                if(jsonObject.getInt("result") == 1){
//                                	spotsDialog.dismiss();
                                	Intent intent = new Intent(getActivity(),AtyUserInformationConfig.class);
                                	startActivity(intent);
//                                	intent.putExtra("information", s);
//                                }else{
//                                	
//                                }
//                                
//                            } catch (JSONException e) {
//                            	spotsDialog.dismiss();
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
// 
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                        	spotsDialog.dismiss();
//                        	Toast.makeText(getActivity(), Config.CONNECTION_ERROR, 2).show();
//                        }
//                    }){
// 
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String,String> map = new HashMap<String,String>();
//                    map.put(Config.KEY_UID,Config.getCachedUserUid(getActivity().getApplicationContext()));
//                    return map;
//                }
//            };
//            mRequestQueue.add(stringRequestUserInformation);
			break;
		case R.id.MyEntryFormPersonalCenter:
			
			//与服务器通信获得我的报名信息
//			StringRequest  stringRequestMyEntryForm = new StringRequest(
//                    Request.Method.POST,
//                    Config.SERVER_URL+"Users/myGames",
//                    new Response.Listener<String>() {
// 
//                        @Override
//                        public void onResponse(String s) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                if(jsonObject.getInt("result") == 1){
                                	Intent intent1 = new Intent(getActivity(),AtyMyEntryForm.class);
//                                	intent.putExtra("information", s);
                                	startActivity(intent1);
//                                }else{
//                                	
//                                }
//                                
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
// 
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                        	Toast.makeText(getActivity(), Config.CONNECTION_ERROR, 2).show();
//                        }
//                    }){
// 
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String,String> map = new HashMap<String,String>();
//                    map.put(Config.KEY_UID,Config.getCachedUserUid(getActivity().getApplicationContext()));
//                    return map;
//                }
//            };
//            mRequestQueue.add(stringRequestMyEntryForm);
			
			break;
//		case R.id.VenuesReservationPersonalCenter:
//			startActivity(new Intent(getActivity(),AtyVenuesReservation.class));
//			break;
		case R.id.MyClubAndAssocPersonalCenter:
//			StringRequest  stringRequestMyClub = new StringRequest(
//                    Request.Method.POST,
//                    Config.SERVER_URL+"Users/myAssoc",
//                    new Response.Listener<String>() {
// 
//                        @Override
//                        public void onResponse(String s) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                if(jsonObject.getInt("result") == 1){
                                	Intent i = new Intent(getActivity(),AtyMyClubAndAssociation.class);
//                                	intent.putExtra("information", s);
                                	startActivity(i);
//                                }else{
//                                	
//                                }
//                                
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
// 
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                        	Toast.makeText(getActivity(), Config.CONNECTION_ERROR, 2).show();
//                        }
//                    }){
// 
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String,String> map = new HashMap<String,String>();
//                    map.put(Config.KEY_UID,Config.getCachedUserUid(getActivity().getApplicationContext()));
//                    return map;
//                }
//            };
//            mRequestQueue.add(stringRequestMyClub);
			break;
		case R.id.MyGradesPersonalCenter:
			//与服务器通信获得我的报名信息
//			StringRequest  stringRequestMyGrade = new StringRequest(
//                    Request.Method.POST,
//                    Config.SERVER_URL+"Game/userGameScore",
//                    new Response.Listener<String>() {
// 
//                        @Override
//                        public void onResponse(String s) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                if(jsonObject.getInt("result") == 1){
                                	Intent intent2 = new Intent(getActivity(),AtyMyGrades.class);
//                                	intent.putExtra("information", s);
                                	startActivity(intent2);
//                                }else{
//                                	
//                                }
//                                
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
// 
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                        	Toast.makeText(getActivity(), Config.CONNECTION_ERROR, 2).show();
//                        }
//                    }){
// 
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String,String> map = new HashMap<String,String>();
//                    map.put(Config.KEY_UID,Config.getCachedUserUid(getActivity().getApplicationContext()));
//                    return map;
//                }
//            };
//            mRequestQueue.add(stringRequestMyGrade);
			break;
//		case R.id.ScoresManagerPersonalCenter:
//			startActivity(new Intent(getActivity(),AtyScoresManager.class));
//			break;
//		case R.id.MyFocusPersonalCenter:
//			startActivity(new Intent(getActivity(),AtyMyFocus.class));
//			break;
			
		}
	}
	
	
	private static final int CAMERA_REQUEST_CODE = 222;
	private static final int PICTURE_REQUEST_CODE = 444;
	private int mWH = 90;// 单位dp
	Bitmap bitmap = null;
	private static Context context;
	
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null)
		{
			Uri uri = data.getData();
			if (null != uri)
			{
				String imgPath = null;
				ContentResolver resolver = context.getContentResolver();
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
			Config.STATUS_SUBMIT_USER_HW = true;
			Config.cacheUserHw(getActivity().getApplicationContext(), bitmap);
			ibUserHWPersonalCenter.setImageBitmap(bitmap);
			HttpClientUploadManager.saveBitmap2file(getActivity(),bitmap, "myAvatar.png");
			upload(bitmap);
			
		}
	
	}
	
	/**上传图片到服务器*/
	@SuppressWarnings("unused")
	private void upload(final Bitmap bitmap)
	{

		new AsyncTask<Bitmap, Void, String>()
		{
			ProgressDialog progressDialog;

			protected void onPreExecute()
			{
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setMessage("上传中……");
				progressDialog.show();
			};

			@Override
			protected String doInBackground(Bitmap... params)
			{
				HashMap<String,String> map = new HashMap<String, String>();
				map.put(Config.KEY_UID, Config.getCachedUserUid(getActivity().getApplicationContext()));
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
							Toast.makeText(getActivity(), "上传成功",
								Toast.LENGTH_SHORT).show();
							Config.cacheUserHwURL(getActivity().getApplicationContext(), jsonObject.getString(Config.KEY_FILE_URL));
							if(Config.getCachedUserHwURL(getActivity().getApplicationContext())!=null){
								Picasso.with(getActivity()).load(jsonObject.getString(Config.KEY_FILE_URL)).into(ibUserHWPersonalCenter);
//								Picasso.with(getActivity()).load(jsonObject.getString(Config.KEY_FILE_URL)).into(SlidingMenuListFragment.ivUserIcon);
							}
						}
						Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("desc"),Toast.LENGTH_SHORT).show();
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "上传失败",
						Toast.LENGTH_SHORT).show();
				}
			};
		}.execute(bitmap);
	}
	
}
