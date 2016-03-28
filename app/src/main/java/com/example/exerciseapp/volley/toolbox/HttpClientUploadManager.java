package com.example.exerciseapp.volley.toolbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.Message;

public class HttpClientUploadManager {  
	  
    public interface HttpClientUploadResponse {  
        int SUCCESS = 1;  
        int FAIL = 0;  
    }  
      
    /** 
     * 该方式是支持大文件上传的，如果用HttpURLConnection一般只能上传5M以内的，再大就OOM了 
     * @param handler activity宿主handler 
     * @param url  
     * @param filepath 文件路径 
     * @param fileKey 文件对应的key 
     * @param mapParams 字符参数的key和值封装好传入 
     */  
    public static String upload(String url, String filepath,  
            String fileKey, HashMap<String, String> mapParams) {  
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        client.getParams().setParameter(
                CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");

        try {
            MultipartEntity entity = new MultipartEntity();
            // 文件参数部分
            File file = new File(filepath);
            String url111 = file.getAbsolutePath();
            ContentBody fileBody = new FileBody(file); // file
            entity.addPart(fileKey, fileBody);
            // 字符参数部分
            Set<String> set = mapParams.keySet();
            for (String key : set) {
                entity.addPart(key, new StringBody(mapParams.get(key)));
            }
            httpPost.setEntity(entity);

            HttpResponse response = client.execute(httpPost);
//            Message message = handler.obtainMessage();
            if (response.getStatusLine().getStatusCode() == 200) { // 成功
                //获取服务器返回值
                HttpEntity responseEntity = response.getEntity();
                InputStream input = responseEntity.getContent();
                StringBuilder sb = new StringBuilder();
                int s;
                while ((s = input.read()) != -1) {
                    sb.append((char) s);
                }
                String result = sb.toString();
                return result;
//                message.what = HttpClientUploadResponse.SUCCESS;
//                message.obj = result;//将数据返回给activity
            }else {
//                message.what = HttpClientUploadResponse.FAIL;
            	return null;
            }
//            handler.sendMessage(message);
        } catch (Exception e) {
//            Message message = handler.obtainMessage();
//            message.what = HttpClientUploadResponse.FAIL;
//            handler.sendMessage(message);
        	e.printStackTrace();
        }
		return null;
    }  
    
    public static boolean saveBitmap2file(Context context,Bitmap bmp,String filename){
    	if(bmp == null){
    		return false;
    	}
    	
		CompressFormat format= CompressFormat.PNG;
		int quality = 100;
		FileOutputStream stream = null;
		try {
		stream = context.openFileOutput(filename,Context.MODE_WORLD_WRITEABLE|Context.MODE_WORLD_READABLE);
		bmp.compress(format, quality, stream);
		stream.flush();
		stream.close();
		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
		}
}  