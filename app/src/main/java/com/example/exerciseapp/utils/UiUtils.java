package com.example.exerciseapp.utils;

import android.content.Context;

/**
 * Created by xiansong on 2015/5/15.
 */
public class UiUtils {
    private static float density = 0;

    public static int dip2px(Context context, float dipValue) {
        if (density == 0)
            density = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    public static int getVideoWidth(Context ctx) {
        return Math.min(
                ctx.getResources().getDisplayMetrics().widthPixels,
                ctx.getResources().getDisplayMetrics().heightPixels);
    }

    public static int getVideoHeight(Context ctx) {
        return (getVideoWidth(ctx) * 9) / 16;
    }
}
