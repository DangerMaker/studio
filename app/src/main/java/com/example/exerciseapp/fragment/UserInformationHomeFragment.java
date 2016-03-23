package com.example.exerciseapp.fragment;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.exerciseapp.R;
import com.example.exerciseapp.aty.sliding.AtySlidingHome;

public class UserInformationHomeFragment extends Fragment {

	private Adapter adapter;
	private List<Map<String,String>> lovedProjectData ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("LoveProjectItem", "item1,");
//		lovedProjectData.add(map);
//		String[] lovedProjectItemFrom = {"LoveProjectItem"};
//		int[] lovedProjectTo = {R.id.tvLovedProjectItem};
//		
//		if (getActivity() instanceof AtySlidingHome) {
//			AtySlidingHome fca = (AtySlidingHome) getActivity();
//			adapter = new SimpleAdapter(fca, lovedProjectData, android.R.layout.simple_list_item_1, lovedProjectItemFrom, lovedProjectTo);
//		}
//		
//		
		View view = inflater.inflate(R.layout.page_user_information_home, null);
//		GridView gd = (GridView)view.findViewById(R.id.gdLovedProject);
//		gd.setAdapter((ListAdapter) adapter);
//		
//		//添加喜欢的项目
//		view.findViewById(R.id.btnAddLovedProject_UserInformationHome).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Map<String,String> map = new HashMap<String, String>();
//				map.put("LoveProjectItem", "item1,");
//				lovedProjectData.add(map);
//			}
//		});
		view.findViewById(R.id.btnMove).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getActivity() == null)
					return;

				if (getActivity() instanceof AtySlidingHome) {
					AtySlidingHome fca = (AtySlidingHome) getActivity();
//					fca.switchContent(new UserInformationConfigFragment());
				}
			}
		});
		return view;
	}
	}
