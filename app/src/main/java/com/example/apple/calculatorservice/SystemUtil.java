package com.example.apple.calculatorservice;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by leejw on 2017/9/13.
 */

class SystemUtil {
    /**
     * 获取屏幕的宽度或高度
     *
     * @return int;
     */

    public static int getScreenWidth() {
        DisplayMetrics metrics = getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics metrics = getDisplayMetrics();
        return metrics.heightPixels;
    }
    public static DisplayMetrics getDisplayMetrics() {
        return App.getInstance().getResources().getDisplayMetrics();
    }

    /**
     * 设置view的宽度或高度
     */

    public static boolean setViewHeight(View view, int height) {
        if (view == null){
            return false;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (params.height != height) {
                params.height = height;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    /**
     *设置view的宽度或高度
     */
    public static boolean setViewWidth(View view, int width) {
        if (view == null){
            return false;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (params.width != width) {
                params.width = width;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    /**
     * dp转pixel
     */
    public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }
}
