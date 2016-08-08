package com.example.exerciseapp.myutils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.exerciseapp.R;


/**
 * Created by 谷俊 on 8/15/2014.
 */
public class SelectPopupWindow extends PopupWindow {


    private Button btn_cancel;
    private View mMenuView;
    private Context context;
    private int preHeight = 0;
    private int navigationBarHeight = 0;
    private View view_parent;
    private ViewTreeObserver.OnGlobalLayoutListener heightListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int currentHeight = view_parent.getHeight();
            if(preHeight - currentHeight == navigationBarHeight){
                SelectPopupWindow.this.update(LayoutParams.MATCH_PARENT, mMenuView.getHeight() - navigationBarHeight);
                preHeight = currentHeight;
            }else if (preHeight - currentHeight == -navigationBarHeight){
                SelectPopupWindow.this.update(LayoutParams.MATCH_PARENT, mMenuView.getHeight() + navigationBarHeight);
                preHeight = currentHeight;
            }
        }
    };

    public SelectPopupWindow(final View view_parent, final Activity context, View view) {
        super(context);
        this.context = context;
        this.view_parent = view_parent;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = view;
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        preHeight = view_parent.getHeight();
        navigationBarHeight = getNavigationBarHeight();
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //设置layout在PopupWindow中显示的位置
        showAtLocation(view_parent, Gravity.TOP, 0, 0);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                view_parent.getViewTreeObserver().removeGlobalOnLayoutListener(heightListener);
            }
        });
        view_parent.getViewTreeObserver().addOnGlobalLayoutListener(heightListener);
//        mMenuView.setAnimation(AnimationUtils.loadAnimation(mMenuView.getContext(), R.anim.popup_in));
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        v.performClick();
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    private int getNavigationBarHeight() {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = 0;
        if(resourceId > 0){
            resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

}
