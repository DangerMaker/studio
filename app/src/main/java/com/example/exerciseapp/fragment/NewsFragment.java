package com.example.exerciseapp.fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.example.exerciseapp.adapter.NewsListAdapter;
import com.example.exerciseapp.aty.sliding.AtyNewsDetails;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshBase;
import com.example.exerciseapp.view.handmark.pulltorefresh.library.PullToRefreshListView;
import com.example.exerciseapp.volley.AuthFailureError;
import com.example.exerciseapp.volley.Request;
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.Response;
import com.example.exerciseapp.volley.VolleyError;
import com.example.exerciseapp.volley.toolbox.StringRequest;
import com.example.exerciseapp.volley.toolbox.Volley;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsFragment extends Fragment {

	 private PullToRefreshListView mPullToRefreshNewsListView;
	 private LinkedList<JSONObject> mNewsListItems = new LinkedList<JSONObject>();  
	 private NewsListAdapter mNewsAdapter;
	 private RequestQueue mRequestQueue;
	 String result = new String();
	 private String aId = null;
	 
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page_news, null);
		mRequestQueue = Volley.newRequestQueue(getActivity());
		mPullToRefreshNewsListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview_news);
		mPullToRefreshNewsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override  
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {  
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),  
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
                  
                // Update the LastUpdatedLabel  
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  
                  
                // Do work to refresh the list here.  
                new GetDataTask().execute();  
            }

        });  
          
        ListView newsListView = mPullToRefreshNewsListView.getRefreshableView();  
        //去缓存数据
        try {
         if(Config.getCachedNewsList(getActivity().getApplicationContext())!=null){
        	 JSONArray jsonArr = Config.getCachedNewsList(getActivity().getApplicationContext());
        	 for(int i=0;i<jsonArr.length();i++){
					mNewsListItems.addFirst(jsonArr.getJSONObject(i));
        	 }
         }
        } catch (JSONException e) {
        	e.printStackTrace();
        }
        mNewsAdapter = new NewsListAdapter(getActivity(), mNewsListItems);  
        newsListView.setAdapter(mNewsAdapter); 
        newsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),AtyNewsDetails.class);
				intent.putExtra("newsDetails", mNewsListItems.get(position-1).toString());
				startActivity(intent);
				return;
			}
		});
		
        mPullToRefreshNewsListView.setRefreshing(false);
		return view;
	}
	
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {  
  	  
        @Override  
        protected String[] doInBackground(Void... params) {  
            // Simulates a background job.
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
        	
        	//根据aId存在判断是否是总的新闻资讯和协会新闻资讯
        	if(aId!=null){
        		aId = null;
        		StringRequest stringRequestUserInformation = new StringRequest(
                        Request.Method.POST,
                        Config.SERVER_URL+"Assoc/getAssocNews",
                        new Response.Listener<String>() {
     
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if(jsonObject.getInt("result") == 1){
                                    	result = s; 
                                    	JSONArray jsonArr = new JSONObject(s).getJSONArray("data");
//                                		Config.cacheNewsList(getActivity().getApplicationContext(), jsonArr);
//                                		jsonArr = Config.getCachedNewsList(getActivity().getApplicationContext());
                                		mNewsListItems.clear();
                                		for(int i =0;i<jsonArr.length();i++){
                                			mNewsListItems.addFirst(jsonArr.getJSONObject(i));
                                		}
                                		mNewsAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                	Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
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
                        map.put(Config.KEY_AID, aId);
                        return map;
                    }
                };
                mRequestQueue.add(stringRequestUserInformation);
        	}else{
        	StringRequest  stringRequestUserInformation = new StringRequest(
                    Request.Method.GET,
                    "http://101.200.214.68/py/game?action=get_all_lives",
                    new Response.Listener<String>() {
 
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if(jsonObject.getInt("result") == 1){
                                	result = s; 
                                	JSONArray jsonArr = new JSONObject(s).getJSONArray("data");
                            		Config.cacheNewsList(getActivity().getApplicationContext(), jsonArr);
                            		jsonArr = Config.getCachedNewsList(getActivity().getApplicationContext());
                            		mNewsListItems.clear();
                                    if(0==jsonArr.length()){
                                        Toast.makeText(NewsFragment.this.getContext(),"没有资讯",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                            		for(int i =0;i<jsonArr.length();i++){
                            			mNewsListItems.addFirst(jsonArr.getJSONObject(i));
                            		}
                            		mNewsAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                            	Toast.makeText(getActivity(), Config.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
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
                    return map;
                }
            };
            mRequestQueue.add(stringRequestUserInformation);
        	}
            return new String[]{result};  
        }  
        @Override  
        protected void onPostExecute(String[] result) {  
            mNewsAdapter.notifyDataSetChanged();  
            // Call onRefreshComplete when the list has been refreshed.  
            mPullToRefreshNewsListView.onRefreshComplete();
            super.onPostExecute(result);  
        }  
    }
	
	@Override
    public void onResume() {
    	super.onResume();
    	if(Config.aId!=null){
    		mNewsListItems.clear();
    		mNewsAdapter.notifyDataSetChanged();
    		aId = Config.aId;
    		Config.aId = null;
    	}
	    }
	
}
