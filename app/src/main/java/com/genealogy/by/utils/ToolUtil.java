package com.genealogy.by.utils;

import android.content.Context;
import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolUtil {

    // 使用系统当前日期加以调整作为照片的名称
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**Color的16进制颜色值 转 Color的Int整型
     * colorHex - Color的16进制颜色值——#3FE2C5
     * return colorInt - -12590395
     * */
    public int hex2Int(String colorHex){
        int colorInt = 0;
        colorInt = Color.parseColor(colorHex);
        return colorInt;
    }

    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
