package com.example.apple.calculatorservice;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
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

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {
       /* try {
            Object object = context.getSystemService("appops");
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return true;
        }
    }
}
