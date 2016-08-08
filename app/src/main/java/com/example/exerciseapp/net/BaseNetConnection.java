package com.example.exerciseapp.net;

import android.os.AsyncTask;

import com.example.exerciseapp.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BaseNetConnection {
	

		public BaseNetConnection(final String url, final String method,final SuccessCallback successcallback, final FailCallback failcallback,
				final String ...kvs){
			//避免阻塞主线程，运用AsyncTask
			new AsyncTask<Void, Void, String>() {

				@Override
				protected String doInBackground(Void... arg0) {
					//配置上传的参数
					StringBuffer params = new StringBuffer();
					if(kvs.length>=2){
						for (int i = 0; i < kvs.length; i+=2) {
							//这种形式向服务器请求数据tel=18283&key=value
							params.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
						}
					}
					try {
						HttpURLConnection uc;
						
						if(method.equals("GET")){
							uc = (HttpURLConnection) new URL(url).openConnection();
							uc.setDoOutput(true);
							uc.setRequestMethod("GET");
							uc.setUseCaches(false);
							uc.setRequestProperty("Connection", "Keep-Alive");
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),Config.CHARSET));
							bw.write(params.toString());
							bw.flush();
							bw.close();
						}else{
							uc = (HttpURLConnection) new URL(url).openConnection();
							uc.setDoOutput(true);
							uc.setRequestMethod("POST");
							uc.setUseCaches(false);
							uc.setRequestProperty("Connection", "Keep-Alive");
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(),Config.CHARSET));
							bw.write(params.toString());
							bw.flush();
							bw.close();
						}
						
						System.out.println("request url:"+uc.getURL());
						System.out.println("request data:"+params.toString());
						
						BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(),Config.CHARSET));
						String line =  null;
						StringBuffer result = new StringBuffer();
						while((line = br.readLine())!=null){
							result.append(line);
						}
						System.out.println("result:"+result);
						return result.toString();
					} catch (MalformedURLException e) {
						e.printStackTrace();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
					
				}
				
				@Override
				protected void onPostExecute(String result) {
					if(result!=null){
						if(successcallback!=null){
							successcallback.onSuccess(result);
						}
					}else{
						if(failcallback!=null){
							failcallback.onFail();
						}
					}
					
					super.onPostExecute(result);
					
				}
			}.execute();
		}
		
		//通知调用者
		public static interface SuccessCallback{
			void onSuccess(String result);
		}
		public static interface FailCallback{
			void onFail();
		}
	}

