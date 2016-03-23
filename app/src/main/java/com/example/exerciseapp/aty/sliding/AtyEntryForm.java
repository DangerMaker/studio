package com.example.exerciseapp.aty.sliding;
/**
 * 报名主界面
 */
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.example.exerciseapp.fragment.PersonalEntryFormFragment;
import com.example.exerciseapp.view.ryg.fragment.ui.IndicatorFragmentActivity;
import com.umeng.message.PushAgent;

public class AtyEntryForm extends IndicatorFragmentActivity {
	 public static final int FRAGMENT_ONE = 0;
	 public static final int FRAGMENT_TWO = 1;
	 public static Activity instance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		PushAgent.getInstance(this).onAppStart();
		
	}

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
	        tabs.add(new TabInfo(FRAGMENT_ONE, "个人报名",
	                PersonalEntryFormFragment.class));
//	        tabs.add(new TabInfo(FRAGMENT_TWO, "团队报名",
//	                TeamOrCompanyEntryFormFragment.class));

	        return FRAGMENT_ONE;
	    }
}
