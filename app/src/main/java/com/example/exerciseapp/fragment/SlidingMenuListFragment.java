package com.example.exerciseapp.fragment;//package com.example.exerciseapp.fragment;
///**
// * 左滑菜单fragment
// */
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.ListFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.exerciseapp.Config;
//import com.example.exerciseapp.R;
//import com.example.exerciseapp.adapter.SlidingMenuAdapter;
//import com.example.exerciseapp.aty.sliding.AtySlidingHome;
//import com.squareup.picasso.Picasso;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class SlidingMenuListFragment extends ListFragment {
////	private SwitchFragment switchFragment;
//	private GameListFragment_ gameListFragment = null;
//	private ClubFragment clubFragment = null;
//	private ConfigFragment configFragment = null;
//	private MyListFragment myListFragment = null;
//	private NewsFragment newsFragment = null;
//	private PersonalCenterFragment personalCenterFragment = null;
//	private StartRunFragment startRunFragment = null;
//	
//	
//	static CircleImageView ivUserIcon;
//	private TextView tvUserName_UserInformationHome;
//	private TextView tvUserBirthday;
//	
//	private ImageView ivMessage;
//	private RequestQueue mRequestQueue;  
//	@SuppressLint("InflateParams")
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		mRequestQueue =  Volley.newRequestQueue(getActivity());
//		View view = inflater.inflate(R.layout.list, null);
//		ivUserIcon = (CircleImageView) view.findViewById(R.id.ivUserIcon);
//		tvUserName_UserInformationHome = (TextView) view.findViewById(R.id.tvUserName_UserInformationHome);
//		tvUserBirthday = (TextView) view.findViewById(R.id.tvUserBirthday);
//		ivMessage = (ImageView) view.findViewById(R.id.ivMessage);
//		if(Config.getCachedUserHwURL(getActivity().getApplicationContext())!=null){
//			Picasso.with(getActivity()).load(Config.getCachedUserHwURL(getActivity().getApplicationContext())).into(ivUserIcon);
//		}
//		if(Config.getCachedBriefUserInformation(getActivity().getApplicationContext())!=null){
//			JSONObject json = Config.getCachedBriefUserInformation(getActivity().getApplicationContext());
//			try {
//				if(Config.TOURIST_MODE){
//					tvUserName_UserInformationHome.setText("游客");
//				}else{
//					tvUserName_UserInformationHome.setText(json.getString(Config.KEY_USER_NAME));
//					tvUserBirthday.setText(json.getString(Config.KEY_USER_BIRTHDAY));
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		
//		//点击列表栏上方用户信息，执行的动作
//		view.findViewById(R.id.UserInformation_list).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if (getActivity() == null)
//					return;
//				if(Config.TOURIST_MODE){
//					Toast.makeText(getActivity(), "当前为游客登录", 2).show();
//				}else{
//				if (getActivity() instanceof AtySlidingHome) {
//					AtySlidingHome fca = (AtySlidingHome) getActivity();
//					fca.switchContent(new PersonalCenterFragment(), Config.PAGE_TAG_PERSONAL_CENTER);
////					fca.startActivity(new Intent(fca.getApplicationContext(),AtyUserInformationHome.class));
////					fca.getApplication().setTheme(R.style.notitle);
//				}
//				}
//				
////				switchFragment(new UserInformationHomeFragment());
//				
//			}
//		});
//		return view;
//	}
//
//	@Override
//		public void onResume() {
//			super.onResume();
//			if(Config.STATUS_HAS_MESSAGE){
//				ivMessage.setImageResource(R.drawable.has_message_icon);
//			}else{
//				ivMessage.setImageResource(R.drawable.no_message_icon);
//			}
//			if(Config.TOURIST_MODE){
//				ivMessage.setVisibility(View.INVISIBLE);
//			}
//			if(Config.STATUS_SUBMIT_USER_HW){
//				if(Config.getCachedUserHw(getActivity().getApplicationContext())!=null){
//					if(Config.TOURIST_MODE){
//						
//					}else{
//						ivUserIcon.setImageBitmap(Config.getCachedUserHw(getActivity().getApplicationContext()));
//					}
//				}
//			}else{
////				if(Config.getCachedUserHwURL(getActivity().getApplicationContext())!=null){
////					Picasso.with(getActivity()).load(Config.getCachedUserHwURL(getActivity().getApplicationContext())).into(ivUserIcon);
////				}
//			}
//			
//			
//			
//			if(Config.TOURIST_MODE){
//				tvUserName_UserInformationHome.setText("游客");
//				tvUserBirthday.setText("");
//			}else{
//				StringRequest  stringRequestUserBriefInformation = new StringRequest(
//			              Request.Method.POST,
//			              Config.SERVER_URL+"Users/briefUserInfoNew",
//			              new Response.Listener<String>() {
//			
//			                  @Override
//			                  public void onResponse(String s) {
//			                      try {
//			                          JSONObject json = new JSONObject(s);
//			                          if(json.getInt("result") == 1){
//			                        	  tvUserName_UserInformationHome.setText(json.getJSONObject("data").getString(Config.KEY_USER_NAME));
//			                        	  tvUserBirthday.setText(json.getJSONObject("data").getString(Config.KEY_USER_BIRTHDAY));
//			              					if(!Config.STATUS_SUBMIT_USER_HW){
//			              						Picasso.with(getActivity()).load(json.getJSONObject("data").getString(Config.KEY_AVATAR)).into(ivUserIcon);
//			              					}
//			                          }else{
//			                        	  Toast.makeText(getActivity(), json.getString("desc"), 2).show();
//			                          }
//			                          
//			                      } catch (JSONException e) {
//			                          e.printStackTrace();
//			                      }
//			                  }
//			              },
//			              new Response.ErrorListener() {
//							@Override
//							public void onErrorResponse(VolleyError error) {
//								// TODO Auto-generated method stub
//								Toast.makeText(getActivity(), Config.CONNECTION_ERROR, 2).show();
//							}
//			              }){
//			
//			          @Override
//			          protected Map<String, String> getParams() throws AuthFailureError {
//			              Map<String,String> map = new HashMap<String,String>();
//			              map.put(Config.KEY_UID, Config.getCachedUserUid(getActivity().getApplicationContext()));
//			              return map;
//			          }
//			      };
//			      mRequestQueue.add(stringRequestUserBriefInformation);
//			}
////			if(Config.getCachedBriefUserInformation(getActivity().getApplicationContext())!=null){
////				JSONObject json = Config.getCachedBriefUserInformation(getActivity().getApplicationContext());
////				try {
////					if(Config.TOURIST_MODE){
////						tvUserName_UserInformationHome.setText("游客");
////					}else{
////						tvUserName_UserInformationHome.setText(json.getString(Config.KEY_USER_NAME));
////						tvUserBirthday.setText(json.getString(Config.KEY_USER_BIRTHDAY));
////					}
////				} catch (JSONException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////			}
//		}
//	
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//
//		SlidingMenuAdapter adapter = new SlidingMenuAdapter(getActivity());
//
//		adapter.add(adapter.new SlidingMenuItem(R.drawable.activities_icon, "赛事活动"));
//		adapter.add(adapter.new SlidingMenuItem(R.drawable.run_icon, "开始跑步"));
////		adapter.add(adapter.new SlidingMenuItem(R.drawable.my_booklist_icon, "我的订单"));
//		adapter.add(adapter.new SlidingMenuItem(R.drawable.club_icon,
//				"协会俱乐部"));
//		adapter.add(adapter.new SlidingMenuItem(R.drawable.person_center_icon, "个人中心"));
//		adapter.add(adapter.new SlidingMenuItem(R.drawable.news_icon, "新闻资讯"));
//		adapter.add(adapter.new SlidingMenuItem(R.drawable.config_icon, "设置"));
////		adapter.add(adapter.new SlidingMenuItem(R.drawable.a,
////				"健身器材"));
////		adapter.add(adapter.new SlidingMenuItem(R.drawable.a, "关于我们"));
//		setListAdapter(adapter);
//		
//	}
//
//	/**
//	 * 点击菜单栏触发的事件
//	 */
//	@Override
//	public void onListItemClick(ListView lv, View v, int position, long id) {
//		Fragment newContent = null;
//		if (getActivity() == null)
//			return;
//		AtySlidingHome fca = null;
//		if (getActivity() instanceof AtySlidingHome) {
//			fca = (AtySlidingHome) getActivity();
//		}
//		switch (position) {
////		case Config.PAGE_TAG_ABOUT_US:
////			newContent = new PageFragment(Config.PAGE_TAG_ABOUT_US);
////			break;
//		case Config.PAGE_TAG_COMPETETION_ACTIVIES:
////			if(!fca.getActionBar().isShowing()){
////				fca.getActionBar().show();
////			}
////			fca.getActionBar().setCustomView(R.layout.actionbar_game);
//			if(gameListFragment == null){
//				gameListFragment = new GameListFragment_();
//			}
//			newContent = gameListFragment;
//			break;
//		case Config.PAGE_TAG_START_RUNNING:
////			if(!fca.getActionBar().isShowing()){
////				fca.getActionBar().show();
////			}
////			fca.getActionBar().setCustomView(R.layout.actionbar_start_running);
//			if(startRunFragment == null){
//				startRunFragment = new StartRunFragment();
//			}
//			newContent = startRunFragment;
//			break;
////		case Config.PAGE_TAG_MY_LIST:
//////			if(!fca.getActionBar().isShowing()){
//////				fca.getActionBar().show();
//////			}
//////			fca.getActionBar().setCustomView(R.layout.actionbar_my_list);
////			if(myListFragment == null){
////				myListFragment = new MyListFragment();
////			}
////			newContent = myListFragment;
////			break;
//		case Config.PAGE_TAG_PERSONAL_CENTER:
////			fca.getActionBar().hide();
////			fca.getActionBar().setCustomView(R.layout.actionbar_personal_center);
//			if(Config.TOURIST_MODE){
//				Toast.makeText(getActivity(), "当前为游客登录", 2).show();
//			}else{
//				if(personalCenterFragment == null){
//					personalCenterFragment = new PersonalCenterFragment();
//				}
//				newContent = personalCenterFragment;
//			}
//			break;
//		case Config.PAGE_TAG_CLUB:
////			if(!fca.getActionBar().isShowing()){
////				fca.getActionBar().show();
////			}
////			fca.getActionBar().setCustomView(R.layout.actionbar_club);
//			if(clubFragment == null){
//				clubFragment = new ClubFragment();
//			}
//			newContent = clubFragment;
//			break;
//		case Config.PAGE_TAG_NEWS:
////			if(!fca.getActionBar().isShowing()){
////				fca.getActionBar().show();
////			}
////			fca.getActionBar().setCustomView(R.layout.actionbar_news);
//			if(newsFragment == null){
//				newsFragment = new NewsFragment();
//			}
//			newContent = newsFragment;
//			break;
//		case Config.PAGE_TAG_CONFIG:
////			if(!fca.getActionBar().isShowing()){
////				fca.getActionBar().show();
////			}
////			fca.getActionBar().setCustomView(R.layout.actionbar_config);
//			if(configFragment == null){
//				configFragment = new ConfigFragment();
//			}
//			newContent = configFragment;
//			break;
////		default:
//////			if(!fca.getActionBar().isShowing()){
//////				fca.getActionBar().show();
//////			}
//////			fca.getActionBar().setCustomView(R.layout.actionbar_start_running);
////			newContent = new PageFragment(Config.PAGE_TAG_START_RUNNING);
////			break;
//		}
//		switchFragment(newContent,position);
//		
//	}
//
//	
////	public static interface SwitchFragment{
////		void switchFragment(Fragment fragment);
////	}
//	private void switchFragment(Fragment fragment,int clickPosition) {
//		if (getActivity() == null)
//			return;
//
//		if (getActivity() instanceof AtySlidingHome) {
//			AtySlidingHome fca = (AtySlidingHome) getActivity();
//			fca.switchContent(fragment, clickPosition);
//		}
//	}
//}
