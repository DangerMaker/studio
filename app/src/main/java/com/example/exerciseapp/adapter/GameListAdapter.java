package com.example.exerciseapp.adapter;

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
import com.example.exerciseapp.volley.RequestQueue;
import com.example.exerciseapp.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class GameListAdapter extends BaseAdapter {
    private Activity context;
    private LinkedList<JSONObject> list;
    private RequestQueue mRequestQueue;

    public GameListAdapter(Activity context, LinkedList<JSONObject> list) {
        this.context = context;
        this.list = list;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View itemView = inflater.inflate(R.layout.game_list_item, null);
        TextView tvGameTitleGameList = (TextView) itemView.findViewById(R.id.tvGameTitleGameList);
        ImageView imageGameList = (ImageView) itemView.findViewById(R.id.imageGameList);
        TextView tvPersonNum = (TextView) itemView.findViewById(R.id.tvGamePersonNum);
        TextView tvGameTimeGameList = (TextView) itemView.findViewById(R.id.tvGameTimeGameList);
        TextView tvGameTimeEndGameList = (TextView) itemView.findViewById(R.id.tvGameTimeEndGameList);
        ImageView tvGameStatusGameList = (ImageView) itemView.findViewById(R.id.tvGameStatusGameList);
        // imageGameList.setDrawingCacheEnabled(true);
        // final TextView tvGameItem = (TextView)
        // itemView.findViewById(R.id.tvGameItem);

        // StringRequest stringRequest = new StringRequest(
        // Request.Method.POST,
        // Config.SERVER_URL+"Game/getEventNew",
        // new Response.Listener<String>() {
        //
        // @Override
        // public void onResponse(String s) {
        // try {
        // JSONObject jsonObject = new JSONObject(s);
        // if(jsonObject.getString("result").equals("1")){
        // JSONArray jsonArr = jsonObject.getJSONArray("eventInfo");
        // String str = "[";
        // for(int i=0;i<jsonArr.length();i++){
        // if(i == jsonArr.length()-1){
        // str.concat(jsonArr.getJSONObject(i).getString("ename")+"]");
        // break;
        // }
        // str.concat(jsonArr.getJSONObject(i).getString("ename")+",");
        // }
        // tvGameItem.setText(str);
        //
        // }else{
        //
        // }
        //
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
        // }
        // },
        // new Response.ErrorListener() {
        //
        // @Override
        // public void onErrorResponse(VolleyError volleyError) {
        // Toast.makeText(context, Config.CONNECTION_ERROR, 2).show();
        // }
        // }){
        //
        // @Override
        // protected Map<String, String> getParams() throws AuthFailureError {
        // Map<String,String> map = new HashMap<String,String>();
        // map.put(Config.KEY_UID,Config.getCachedUserUid(context.getApplicationContext()));
        // try {
        // map.put(Config.KEY_GAME_ID,list.get(position).getString(Config.KEY_GAME_ID));
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // return map;
        // }
        // };
        // mRequestQueue.add(stringRequest);

        try {
            tvGameTitleGameList.setText(list.get(position).getString(Config.KEY_GAME_NAME));
            // TODO
            // if(Config.getCachedFrontPage(context.getApplicationContext(),
            // list.get(position).get(Config.KEY_GAME_ID)) == null){
            Picasso.with(context).load(list.get(position).getString(Config.KEY_GAME_FRONT_PAGE)).into(imageGameList);
            // Config.cacheGameFrontPage(context.getApplicationContext(),
            // imageGameList.getDrawingCache(),
            // list.get(position).get(Config.KEY_GAME_ID));
            // }else{
            // imageGameList.setBackground(new
            // BitmapDrawable(Config.getCachedFrontPage(context.getApplicationContext(),
            // list.get(position).get(Config.KEY_GAME_ID))));
            // }
            tvPersonNum.setText(list.get(position).getString(Config.KEY_GAME_ATTEND));
            String startAttendT = list.get(position).getString(Config.KEY_GAME_ACCEPT_START_NEW);
            tvGameTimeGameList.setText(startAttendT.replace("-", "."));
            String endAttendT = list.get(position).getString(Config.KEY_GAME_ACCEPT_END_NEW);
            tvGameTimeEndGameList.setText(endAttendT);

            if (list.get(position).getString(Config.KEY_GAME_STATUS_ID).equals("1")) {
                tvGameStatusGameList.setImageResource(R.drawable.weibaoming);
            }
            if (list.get(position).getString(Config.KEY_GAME_STATUS_ID).equals("2")) {
                tvGameStatusGameList.setImageResource(R.drawable.weikaishi);
            }
            if (list.get(position).getString(Config.KEY_GAME_STATUS_ID).equals("3") || list.get(position).getString(Config.KEY_GAME_STATUS_ID).equals("0") || list.get(position).getString(Config.KEY_GAME_STATUS_ID).equals("4")) {
                tvGameStatusGameList.setImageResource(R.drawable.baomingjieshu);
            }
            //
            // if(list.get(position).get(Config.KEY_GAME_STATUS_ID).equals("1")){
            // tvGameStatusGameList.setImageResource(R.drawable.weibaoming);
            // }
            // if(list.get(position).get(Config.KEY_GAME_STATUS_ID).equals("0")){
            // tvGameStatusGameList.setImageResource(R.drawable.weikaishi);
            // }
            // if(list.get(position).get(Config.KEY_GAME_STATUS_ID).equals("2")){
            // tvGameStatusGameList.setImageResource(R.drawable.baomingjieshu);
            // }
            itemView.setTag(list.get(position).getString(Config.KEY_GAME_ID));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Config.currentClickedGameItem =
        // list.get(position).get(Config.KEY_GAME_ID);
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
