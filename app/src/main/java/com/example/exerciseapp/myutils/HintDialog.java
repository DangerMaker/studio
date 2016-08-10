package com.example.exerciseapp.myutils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

public class HintDialog extends Dialog {

	private static int default_width = LayoutParams.MATCH_PARENT;
	private static int default_height = LayoutParams.WRAP_CONTENT;

	public HintDialog(Context context, View layout, int style) {
		this(context, default_width, default_height, layout, style);
	}

	public HintDialog(Context context, int width, int height, View layout,
			int style) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = width;
		params.height = height;
		window.setAttributes(params);
	}

}