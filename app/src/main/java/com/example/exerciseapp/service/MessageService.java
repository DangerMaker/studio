package com.example.exerciseapp.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtyMessage;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;

public class MessageService extends Service {  
    
    //获取消息线程  
    private MessageThread messageThread = null;  
   
    //点击查看  
    private Intent messageIntent = null;  
    private PendingIntent messagePendingIntent = null;  
   
    //通知栏消息  
    private int messageNotificationID = 1000;  
    private Notification messageNotification = null;  
    private NotificationManager messageNotificatioManager = null;  
   
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
   
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        //初始化  
    	mRequestQueue =  Volley.newRequestQueue(this);
        messageNotification = new Notification();  
        messageNotification.icon = R.drawable.addphoto;  
        messageNotification.tickerText = "新消息";  
        messageNotification.defaults = Notification.DEFAULT_SOUND;  
        messageNotificatioManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);  
   
        messageIntent = new Intent(this, AtySlidingHome.class);  
        messagePendingIntent = PendingIntent.getActivity(this,0,messageIntent,0);  
   
        //开启线程  
        messageThread = new MessageThread();  
        messageThread.isRunning = true;  
        messageThread.start();  
   
        return super.onStartCommand(intent, flags, startId);  
    }  
       
    /** 
     * 从服务器端获取消息 
     * 
     */  
    class MessageThread extends Thread{  
        //运行状态，下一步骤有大用  
        public boolean isRunning = true;  
        public void run() {  
            while(isRunning){  
                try {  
                    //休息10分钟  
                    Thread.sleep(6000);  
                    //获取服务器消息  
                    String serverMessage = getServerMessage();  
                    if(serverMessage!=null&&!"".equals(serverMessage)){  
                        //更新通知栏  
//                        messageNotification.setLatestEventInfo(MessageService.this,  
//                            "您有一条新消息",serverMessage,messagePendingIntent);  
//                        messageNotificatioManager.notify(messageNotificationID, messageNotification);  
//                        //每次通知完，通知ID递增一下，避免消息覆盖掉  
//                        messageNotificationID++;  
                    }  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
   
    /** 
     * 这里以此方法为服务器Demo，仅作示例 
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送 
     */  
    private RequestQueue mRequestQueue;
    private String message;
    public String getServerMessage(){  
    	message = "";
    	StringRequest  stringRequestMyEntryForm = new StringRequest(
                Request.Method.POST,
                Config.SERVER_URL+"System/sysInfo",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if(jsonObject.getInt("result") == 1){
                            	message = "yes";
                            	Config.STATUS_HAS_MESSAGE = true;
                            }else{
                            	
                            }
                            
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                return map;
            }
        };
        mRequestQueue.add(stringRequestMyEntryForm);
        return message;  
    }  
    
    @Override  
    public void onDestroy() {  
                System.exit(0);  
                //或者，二选一，推荐使用System.exit(0)，这样进程退出的更干净  
                //messageThread.isRunning = false;  
                super.onDestroy();  
    }  
}  
