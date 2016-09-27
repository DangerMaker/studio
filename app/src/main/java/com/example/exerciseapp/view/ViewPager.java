package com.example.exerciseapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/8/1.
 */
public class ViewPager extends android.support.v4.view.ViewPager {

    private boolean pagerEnabled = true;
    private boolean smoothScroll = true;

    public ViewPager(Context context) {
        super(context);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.pagerEnabled)
            return super.onInterceptTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.pagerEnabled)
            return super.onTouchEvent(ev);
        return false;
    }

    public void setCurrentItem(int paramInt)
    {
        if (this.smoothScroll)
        {
            super.setCurrentItem(paramInt);
            return;
        }
        super.setCurrentItem(paramInt, false);
    }

    public void setPagingEnabled(boolean paramBoolean)
    {
        this.pagerEnabled = paramBoolean;
    }

    public void setSmoothScroll(boolean paramBoolean)
    {
        this.smoothScroll = paramBoolean;
    }
}
