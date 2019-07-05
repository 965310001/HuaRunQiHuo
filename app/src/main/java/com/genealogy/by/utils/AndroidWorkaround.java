package com.genealogy.by.utils;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Method;

import tech.com.commoncore.utils.ScreenUtils;

/**
 * @author LinSq
 * @description:
 * @date :2019/3/28 9:25
 */

public class AndroidWorkaround {
    public static void assistActivity(View content) {
        new AndroidWorkaround(content);
    }

    /**
     *
     * @param content
     * @param containStuta 是否要状态栏高度  有TitleBar的时候 false 否则true
     */
    public static void assistActivity(View content, boolean containStuta) {
        new AndroidWorkaround(content, containStuta);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidWorkaround(View content) {
        this(content, false);
    }

    /**
     * @param content
     * @param containStuta 是否要减去状态栏
     */
    private AndroidWorkaround(View content, final boolean containStuta) {
        mChildOfContent = content;
        if (containStuta)
            stutaHeight = ScreenUtils.getStatusBarHeight(mChildOfContent.getContext());
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContentMain();
            }
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    private int stutaHeight;

    private void possiblyResizeChildOfContentMain() {
        int usableHeightNow = computeUsableHeight();

        if (usableHeightNow != usableHeightPrevious) {
            frameLayoutParams.height = usableHeightNow - stutaHeight;
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }



    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }

    public static void showNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void hideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }
}