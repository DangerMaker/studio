package com.example.exerciseapp.fragment;

import java.io.File;
import java.math.BigDecimal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.MainActivity;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtyAboutUs;
import com.example.exerciseapp.aty.sliding.AtyChangePassw;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;
import com.example.exerciseapp.aty.sliding.Atyyijianfankui;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

public class ConfigFragment extends Fragment{
	 ImageView imgReceivePush;
	 LinearLayout linearreceivepush;
	 LinearLayout linearcleanmemory;
	 TextView texthuancun;
	 ImageView imgClenMemory;
	 LinearLayout linearjiancexinbanben;
	 LinearLayout linearyijianfankui;
	 ImageView imgyijianfankui;
	 LinearLayout linearaboutus;
	 ImageView imgAboutUs;
//	 ToggleButton toggleBtn;
	 Button btntuichudenglu;

	private LinearLayout linearchangepass;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view=inflater.inflate(R.layout.page_config, container, false);
		linearreceivepush=(LinearLayout) view.findViewById(R.id.linearreceivepush);
//		toggleBtn=(ToggleButton) view.findViewById(R.id.togglebtntuisong);
//		toggleBtn.toggle();
		//开关切换事

//	    toggleBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
//	            @Override
//	            public void onToggle(boolean on) {
//	            	if(on){
//	            		MainActivity.mPushAgent.disable();
//	            	}else{
//	            		MainActivity.mPushAgent.enable();
//	            		Toast.makeText(getActivity(), "已关闭", Toast.LENGTH_SHORT).show();
//	            	}
//	            }
//
//	    });
//		linearreceivepush.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), "推送", Toast.LENGTH_SHORT).show();
//			}
//		});
		imgReceivePush=(ImageView)view.findViewById(R.id.imgreceivepush);
		imgReceivePush.setImageDrawable(getResources().getDrawable(R.drawable.receivepush));
		linearcleanmemory=(LinearLayout) view.findViewById(R.id.linearcleanmemory);
		linearcleanmemory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				new  AlertDialog.Builder(getActivity())    
//				.setTitle("                          提示" )  
//				
//				.setMessage("                  缓存清除完毕" )  
//				.setPositiveButton("确定" ,  null )  
//				.show();  
				qinglihuancunDialog dl=new qinglihuancunDialog(getActivity(), "提示", new qinglihuancunDialog.OnCustomDialogListener() {
					
					@Override
					public void back(String name) {
						// TODO Auto-generated method stub
						
					}
				});
				dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dl.show();
				
				texthuancun.setText("0.0M");
				DataCleanManager.clearAllCache(getActivity().getApplicationContext());
			}
		});
		texthuancun=(TextView) view.findViewById(R.id.texthuancun);
//		try {
//			texthuancun.setText(DataCleanManager.getTotalCacheSize(getActivity()));//DataCleanManager.getTotalCacheSize(getActivity());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		imgClenMemory=(ImageView) view.findViewById(R.id.imgcleanmemory);
		imgClenMemory.setImageDrawable(getResources().getDrawable(R.drawable.cleanmemory));
		
		
		linearjiancexinbanben = (LinearLayout) view.findViewById(R.id.linearjiancexinbanben);
		linearjiancexinbanben.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UpdateConfig.setDebug(true);
				UmengUpdateAgent.setDefault();
				//请在调用update,forceUpdate,silentUpdate函数之前设置推广id
				UmengUpdateAgent.setSlotId("123456");
				UmengUpdateAgent.forceUpdate(getActivity());
			}
		});
		
		
		imgyijianfankui=(ImageView) view.findViewById(R.id.imgyijianfankui);
		imgyijianfankui.setImageDrawable(getResources().getDrawable(R.drawable.yijianfankui));
		linearyijianfankui=(LinearLayout) view.findViewById(R.id.linearyijianfankui);
		linearyijianfankui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), "意见反馈", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getActivity(), Atyyijianfankui.class);
				startActivity(i);
			}
		});
		imgAboutUs=(ImageView) view.findViewById(R.id.imgaboutus);
		imgAboutUs.setImageDrawable(getResources().getDrawable(R.drawable.aboutus));
		linearaboutus=(LinearLayout) view.findViewById(R.id.linearaboutus);
		linearaboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), AtyAboutUs.class);
				startActivity(i);
			}
		});
		btntuichudenglu=(Button) view.findViewById(R.id.btntuichudenglu);
		btntuichudenglu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(getActivity(), "退出", 2).show();
				startActivity(new Intent(getActivity(),MainActivity.class));
				DataCleanManager.quitCurrentUser(getActivity().getApplicationContext());
				AtySlidingHome.instance.finish();
//				DataCleanManager.clearAllCache(getActivity().getApplicationContext());
				
			}
		});

		linearchangepass = (LinearLayout) view.findViewById(R.id.linearchangepass);
		linearchangepass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ConfigFragment.this.getContext(), AtyChangePassw.class);
				startActivity(intent);
			}
		});
		return view;
	}
	public static class DataCleanManager {
	     
	     public static String getTotalCacheSize(Context context) throws Exception {
	            long cacheSize = getFolderSize(context.getCacheDir());
	            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
	                cacheSize += getFolderSize(context.getExternalCacheDir());
	            }  
	            return getFormatSize(cacheSize);
	        }
	   
	  
	    public static void quitCurrentUser(Context context){
	    	File dir = context.getCacheDir();
	    	if (dir != null && dir.isDirectory()) {
	            String[] children = dir.list();
	            for (int i = 0; i < children.length; i++) {
	            	if(children[i].equals("GameList")){
	            	}else if(children[i].equals("AssocList")){
	            	}else if(children[i].equals("NewsList")){
	            	}else if(children[i].equals("Uid")){
	            		Config.cacheUserUid(context, "");
	            	}else{
	            		deleteDir(new File(dir, children[i]));
	            	}
	            }
	        }
	    }
	    
	    public static void clearOtheCache(Context context){
	    	File dir = context.getCacheDir();
	    	if (dir != null && dir.isDirectory()) {
	            String[] children = dir.list();
	            for (int i = 0; i < children.length; i++) {
	            	if(children[i].equals("Uid")){
	            	}else if(children[i].equals("UserHwURL")){
	            	}else if(children[i].equals("BriefUserInformation")){
	            	}else if(children[i].equals("UserInformation")){
	            	}else if(children[i].equals("WeightAndHeight")){
	            	}else if(children[i].equals("Weight")){
	            	}else if(children[i].equals("UserHw")){
	            	}else{
	            		deleteDir(new File(dir, children[i]));
	            	}
	            }
	        }
	    } 
	    
	    public static void clearAllCache(Context context) {
	    	clearOtheCache(context);
//	        deleteDir(context.getCacheDir());
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
	       
	    // 获取文件  
	    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据  
	    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据  
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
//	            return size + "Byte";  
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
			texthuancun.setText(DataCleanManager.getTotalCacheSize(getActivity()));//DataCleanManager.getTotalCacheSize(getActivity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
