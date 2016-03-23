package com.example.exerciseapp.volley.toolbox;

import android.content.Context;

import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.toolbox.Volley;

/**
 * Created by gyzhong on 15/3/1.
 */
public class VolleyUtil {

    private static RequestQueue mRequestQueue ;

    public static synchronized void initialize(Context context){
        if (mRequestQueue == null){
            synchronized (VolleyUtil.class){
                if (mRequestQueue == null){
                    mRequestQueue = Volley.newRequestQueue(context) ;
                }
            }
        }
        mRequestQueue.start();
    }

    public static RequestQueue getRequestQueue(){
        if (mRequestQueue == null)
            throw new RuntimeException("请先初始化mRequestQueue") ;
        return mRequestQueue ;
    }
}
