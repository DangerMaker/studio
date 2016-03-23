package com.example.exerciseapp.volley.toolbox;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.example.exerciseapp.volley.Request;

public class UploadApi {

    /**
     * 上传图片接口
     * @param bitmap 需要上传的图片
     * @param listener 请求回调
     */
    public static void uploadImg(Bitmap bitmap,ResponseListener listener,String url){
        List<FormImage> imageList = new ArrayList<FormImage>() ;
        imageList.add(new FormImage(bitmap)) ;
        Request request = new PostUploadRequest(url,imageList,listener) ;
        VolleyUtil.getRequestQueue().add(request) ;
    }
}