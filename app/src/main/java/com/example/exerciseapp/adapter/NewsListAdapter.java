package com.example.exerciseapp.adapter;

import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;
import com.squareup.picasso.Picasso;

    public class NewsListAdapter extends BaseAdapter {  
        private Activity context;  
        private LinkedList<JSONObject> list;  
      
        public NewsListAdapter(Activity context, LinkedList<JSONObject> list) {  
            this.context = context;  
            this.list = list;  
        }  
      
        @SuppressLint("ViewHolder") @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            LayoutInflater inflater = context.getLayoutInflater();  
            View itemView = inflater.inflate(R.layout.news_list_item, null);  
            TextView tvNewsTitle = (TextView) itemView.findViewById(R.id.tvNewsTitle);
            ImageView ivNewsPic = (ImageView) itemView.findViewById(R.id.ivNewsPic);
            TextView tvAssocName = (TextView) itemView.findViewById(R.id.tvAssocName);
            TextView tvDateNews = (TextView) itemView.findViewById(R.id.tvDateNews);
            
            try {
				tvNewsTitle.setText(list.get(position).getString(Config.KEY_TITLE));
				tvAssocName.setText(list.get(position).getString(Config.KEY_ANAME));
				tvDateNews.setText(list.get(position).getString(Config.KEY_DATE));
				Picasso.with(context).load(list.get(position).getString(Config.KEY_PIC)).into(ivNewsPic);
				itemView.setTag(list.get(position));
            } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            return itemView;  
        }  
      
        @Override  
        public int getCount() {  
            return list.size();  
        }  
      
        @Override  
        public Object getItem(int position) {  
            return list.get(position);  
        }  
      
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
        
        
    }  
