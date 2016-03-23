package com.example.exerciseapp.fragment;//package com.example.exerciseapp.fragment;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.SimpleAdapter.ViewBinder;
//import android.widget.Toast;
//
//import com.example.exerciseapp.Config;
//import com.example.exerciseapp.R;
//import com.example.exerciseapp.aty.sliding.AtyGameInformation;
//import com.example.exerciseapp.fragment.RefreshableView.PullToRefreshListener;
//import com.example.exerciseapp.net.GameListConnection;
//import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
//
//public class GameListFragment extends Fragment {
//	LinkedList<Map<String, Object>> list;
////	RefreshableView refreshableView;
//	ListView listView;
//	private SimpleAdapter adapter;
//	private LinkedList<String> mListItems;
//	private JSONArray jsonArr;
//	private String gameId;
//	private Bitmap bitmapFrontPage;
//
//	// String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
//	// "L" };
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater
//				.inflate(R.layout.page_competetion_activities, null);
//		
//		
////		refreshableView = (RefreshableView) view
////				.findViewById(R.id.refreshable_view);
////		listView = (ListView) view.findViewById(R.id.list_view);
//		mListItems = new LinkedList<String>();
//		adapter = new SimpleAdapter(view.getContext(), getData(),
//				R.layout.game_list_item, new String[] { Config.KEY_GAME_NAME,
//						Config.KEY_GAME_START_TIME, Config.KEY_GAME_FRONT_PAGE,
//						Config.KEY_GAME_MAX_ATTEND,
//						Config.KEY_GAME_BIREF_INTRO, Config.KEY_GAME_STATUS },
//				new int[] { R.id.tvGameTitleGameList, R.id.tvGameTimeGameList,
//						R.id.imageGameList, R.id.tvGamePersonNum,
//						R.id.tvGameBriefIntroGameList,
//						R.id.tvGameStatusGameList });
//			adapter.setViewBinder(new ViewBinder() {
//			 
//            @Override
//            public boolean setViewValue(View view, Object data,
//                    String textRepresentation) {
//                if (view instanceof ImageView && data instanceof Bitmap) {
//                    ImageView iv = (ImageView) view;
//                    iv.setImageBitmap((Bitmap) data);
//                    return true;
//                }
//                return false;
//            }
//        });
//		// new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//		// mListItems);
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				startActivity(new Intent(getActivity(),
//						AtyGameInformation.class));
//				Toast.makeText(getActivity(), "sfslkflk", Toast.LENGTH_SHORT)
//						.show();
//				return;
//			}
//		});
//		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
//			@Override
//			public void onRefresh() {
//				try {
//					Thread.sleep(500);
//					// mListItems.addFirst("add first");
//					// adapter.notifyDataSetChanged();
//					new GameListConnection(Config.SERVER_URL
//							+ "Game/gameListNew", Config.POST,
//							new GameListConnection.SuccessCallBack() {
//
//								@Override
//								public void onSuccess(JSONArray result){
//										jsonArr = result;
////										try {
//											//缓存所有Front图片
////										for (int i = 0; i < result.length(); i++) {
////											gameId = jsonArr.getJSONObject(i).getString(Config.KEY_GAME_ID);
////											new BaseNetConnection("", Config.POST, new BaseNetConnection.SuccessCallback() {
////												
////												@Override
////												public void onSuccess(String result) {
////													Config.cacheFrontPageBitmap(getActivity().getApplicationContext(), 
////															BitmapFactory.decodeByteArray(result.toString().getBytes(), 
////																	0, 
////																	result.toString().getBytes().length), 
////																	gameId);
////												}
////											}, new BaseNetConnection.FailCallback() {
////												
////												@Override
////												public void onFail() {
////													// TODO Auto-generated method stub
////													
////												}
////											}, "");
////										}
////										} catch (JSONException e) {
////											// TODO Auto-generated catch block
////											e.printStackTrace();
////										}
//								}
//							}, new GameListConnection.FailCallBack() {
//
//								@Override
//								public void onFail(String result) {
//
//								}
//							}, "");
////					Thread.sleep(1000);
////					for (int i = 0; i < jsonArr.length(); i++) {
////						gameId = jsonArr.getJSONObject(i).getString(Config.KEY_GAME_ID);
////						new BaseNetConnection("", Config.POST, new BaseNetConnection.SuccessCallback() {
////							
////							@Override
////							public void onSuccess(String result) {
////								Config.cacheFrontPageBitmap(getActivity().getApplicationContext(), 
////										BitmapFactory.decodeByteArray(result.toString().getBytes(), 
////												0, 
////												result.toString().getBytes().length), 
////												gameId);
////							}
////						}, new BaseNetConnection.FailCallback() {
////							
////							@Override
////							public void onFail() {
////								// TODO Auto-generated method stub
////								
////							}
////						}, "");
////					}
//					new GetDataTask().execute();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} 
//				refreshableView.finishRefreshing();
//			}
//		}, 0);
//		return view;
//	}
//
//	private List<Map<String, Object>> getData() {
//		list = new LinkedList<Map<String, Object>>();
//		// Map<String,Object> map = new HashMap<String, Object>();
//		// map.put("image", R.drawable.addphoto);
//		// map.put("text", "item1");
//		// list.add(map);
//		//
//		// map = new HashMap<String, Object>();
//		// map.put("image", R.drawable.addphoto);
//		// map.put("text", "item2");
//		// list.add(map);
//		if (Config.getCachedGameList(getActivity().getApplicationContext()) == null) {
//			return list;
//		} else {
//			JSONObject jsonObj;
//			JSONArray jsonArr = Config.getCachedGameList(getActivity()
//					.getApplicationContext());
//
//			try {
//				for (int i = 0; i < jsonArr.length(); i++) {
//
//					jsonObj = jsonArr.getJSONObject(i);
//					
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put(Config.KEY_GAME_NAME,
//							jsonObj.getString(Config.KEY_GAME_NAME));
//					
//					map.put(Config.KEY_GAME_ID,
//							jsonObj.getString(Config.KEY_GAME_ID));
//					map.put(Config.KEY_GAME_START_TIME,
//							jsonObj.getString(Config.KEY_GAME_START_TIME));
////					map.put(Config.KEY_GAME_FRONT_PAGE, Config.returnBitMap(jsonObj
////									.getString(Config.KEY_GAME_FRONT_PAGE)));
////					new BaseNetConnection(jsonObj.getString(Config.KEY_GAME_FRONT_PAGE).replaceAll("\\", ""), "POST", new BaseNetConnection.SuccessCallback() {
////						
////						@Override
////						public void onSuccess(String result) {
////							Config.cacheFrontPageBitmap(getActivity().getApplicationContext(),
////									BitmapFactory.decodeByteArray(result.toString().getBytes(), 0, result.toString().getBytes().length), gameId);
////						}
////					}, new BaseNetConnection.FailCallback() {
////						
////						@Override
////						public void onFail() {
////							// TODO Auto-generated method stub
////							
////						}
////					}, "");
////					if(Config.getCachedFrontPageBitmap(getActivity(), gameId)!=null){
////						map.put(Config.KEY_GAME_FRONT_PAGE, Config.getCachedFrontPageBitmap(getActivity().getApplicationContext(), gameId));
////					}
//					map.put(Config.KEY_GAME_MAX_ATTEND,
//							jsonObj.getString(Config.KEY_GAME_MAX_ATTEND));
//					map.put(Config.KEY_GAME_BIREF_INTRO,
//							jsonObj.getString(Config.KEY_GAME_BIREF_INTRO));
//					map.put(Config.KEY_GAME_STATUS,
//							jsonObj.getString(Config.KEY_GAME_STATUS));
//					list.add(map);
//
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return list;
//		}
//	}
//
//	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			// Simulates a background job.
//			try {
//
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String[] result) {
//
//			JSONObject jsonObj;
//			try {
//				for (int i = 0; i < jsonArr.length(); i++) {
//					if (!Config.isExistedJsonObjectInJsonArray(getActivity()
//							.getApplicationContext(), jsonArr.getJSONObject(i)
//							.getString(Config.KEY_GAME_ID))) {
//						jsonObj = jsonArr.getJSONObject(i);
//						gameId = jsonObj.getString(Config.KEY_GAME_ID);
//						Map<String, Object> map = new HashMap<String, Object>();
//						map.put(Config.KEY_GAME_NAME,
//								jsonObj.getString(Config.KEY_GAME_NAME));
//						map.put(Config.KEY_GAME_ID,
//								jsonObj.getString(Config.KEY_GAME_ID));
//						map.put(Config.KEY_GAME_START_TIME,
//								jsonObj.getString(Config.KEY_GAME_START_TIME));
////						new BaseNetConnection(jsonObj.getString(Config.KEY_GAME_FRONT_PAGE).replaceAll("\\", ""), "POST", new BaseNetConnection.SuccessCallback() {
////							new BaseNetConnection("http://101.200.214.68/Uploads/Picture/GamePic/2015-11-04/5639c7f545377.jpg", "POST", new BaseNetConnection.SuccessCallback() {
////							
////							@Override
////							public void onSuccess(String result) {
////								bitmapFrontPage = BitmapFactory.decodeByteArray(result.toString().getBytes(), 0, result.toString().getBytes().length);
////								Config.cacheFrontPageBitmap(getActivity().getApplicationContext(),
////										bitmapFrontPage, gameId);
////							}
////						}, new BaseNetConnection.FailCallback() {
////							
////							@Override
////							public void onFail() {
////								// TODO Auto-generated method stub
////								
////							}
////						}, "");
////						Thread.sleep(5000);
////						map.put(Config.KEY_GAME_FRONT_PAGE, Config.getCachedFrontPageBitmap(getActivity().getApplicationContext(), jsonObj.getString(Config.KEY_GAME_ID)));
//						map.put(Config.KEY_GAME_MAX_ATTEND,
//								jsonObj.getString(Config.KEY_GAME_MAX_ATTEND));
//						map.put(Config.KEY_GAME_BIREF_INTRO,
//								jsonObj.getString(Config.KEY_GAME_BIREF_INTRO));
//						map.put(Config.KEY_GAME_STATUS,
//								jsonObj.getString(Config.KEY_GAME_STATUS));
//						// list.add(map);
//						// Map<String, Object> map = new HashMap<String,
//						// Object>();
//						// map.put("image", R.drawable.addphoto);
//						// map.put("text", "item1");
//						list.addFirst(map);
//					}
//				}
//				Config.cacheGameList(getActivity()
//						.getApplicationContext(),
//						jsonArr);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			adapter.notifyDataSetChanged();
//
//			// Call onRefreshComplete when the list has been refreshed.
//
//			super.onPostExecute(result);
//		}
//	}
//}
