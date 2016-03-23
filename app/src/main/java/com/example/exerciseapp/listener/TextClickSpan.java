package com.example.exerciseapp.listener;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.exerciseapp.aty.login.AtyRegisterHomePage.OnTextViewClickListener;

public class TextClickSpan extends ClickableSpan {

	private OnTextViewClickListener mListener;
	
	public TextClickSpan(OnTextViewClickListener listener) {
		this.mListener = listener;
	}
	@Override
	public void onClick(View widget) {
		
	}
	
	@Override
	public void updateDrawState(TextPaint ds) {
		mListener.setStyle(ds);
	}


}
