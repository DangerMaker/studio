package com.example.exerciseapp.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.exerciseapp.Config;
import com.example.exerciseapp.R;

public class PageFragment extends Fragment {

    private int pageTag = Config.PAGE_TAG_START_RUNNING;

    public PageFragment() {
        this(Config.PAGE_TAG_START_RUNNING);
    }

    @SuppressLint("ValidFragment")
    public PageFragment(int pageTag) {
        this.pageTag = pageTag;
        setRetainInstance(true);
    }


    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null)
            pageTag = savedInstanceState.getInt("pageTag");
        switch (pageTag) {
            case Config.PAGE_TAG_COMPETETION_ACTIVIES:
                return inflater.inflate(R.layout.page_competetion_activities, null);
            case Config.PAGE_TAG_START_RUNNING:
                return inflater.inflate(R.layout.page_start_running, null);
//		case Config.PAGE_TAG_MY_LIST:
//			return inflater.inflate(R.layout.page_my_list, null);
            case Config.PAGE_TAG_CLUB:
                return inflater.inflate(R.layout.page_club, null);
            case Config.PAGE_TAG_PERSONAL_CENTER:
                return inflater.inflate(R.layout.page_personal_center, null);
            case Config.PAGE_TAG_NEWS:
                return inflater.inflate(R.layout.page_news, null);
            case Config.PAGE_TAG_CONFIG:
                return inflater.inflate(R.layout.page_config, null);

//		case Config.PAGE_TAG_ABOUT_US:
//			return inflater.inflate(R.layout.page_about_us, null);
//		case Config.PAGE_TAG_EXERCISE_EQUIPMENT:
//			return inflater.inflate(R.layout.page_exercies_equipment, null);
//		case Config.PAGE_TAG_PERSONAL_CENTER:
//			return inflater.inflate(R.layout.page_personal_center, null);
//		case Config.PAGE_TAG_SCORE_QUERY:
//			return inflater.inflate(R.layout.page_score_query, null);
//		case Config.PAGE_TAG_RUNNING_RECORD:
//			return inflater.inflate(R.layout.page_running_record, null);
//		case Config.PAGE_USER_INFORMATION_HOME:
//			View view = inflater.inflate(R.layout.page_user_information_home, null);
//			view.findViewById(R.id.btnMove).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					switchFragment(new PageFragment());
//				}
//			});
//			return view;
            default:
                return inflater.inflate(R.layout.page_start_running, null);
        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pageTag", pageTag);
    }

//	public void switchFragment(Fragment fragment){
//		if (getActivity() == null)
//			return;
//
//		if (getActivity() instanceof AtySlidingHome) {
//			AtySlidingHome fca = (AtySlidingHome) getActivity();
//			fca.setTheme(R.style.hastitle);
//			fca.switchContent(fragment);
//			
//		}
//	}
}

