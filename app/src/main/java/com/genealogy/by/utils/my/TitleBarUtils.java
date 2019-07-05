package com.genealogy.by.utils.my;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.MyApplication;
import com.genealogy.by.R;

/**
 * Created by dell on 2019/3/27.
 */

public class TitleBarUtils {
    public static void setTitleBar(TitleBarView titleBar, String mainText, int mainTextColor) {
        titleBar.setTitleMainText(mainText).setLeftTextDrawable(R.mipmap.back_white).setTitleMainTextColor(MyApplication.getApplication().getResources().getColor(mainTextColor));
    }

    /*
    * 默认白色返回键  白色标题
    * */
    public static void setDefaultTitleBar(TitleBarView titleBar, String mainText) {
        titleBar.setTitleMainText(mainText).setLeftTextDrawable(R.mipmap.back_white).setTitleMainTextColor(MyApplication.getApplication().getResources().getColor(R.color.colorWhite));
    }

    public static void setDefaultTitleBarRightText(TitleBarView titleBar, String mainText, String rightText){
        titleBar.setTitleMainText(mainText)
                .setLeftTextDrawable(R.mipmap.back_white)
                .setTitleMainTextColor(MyApplication.getApplication().getResources().getColor(R.color.colorWhite))
                .setRightText(rightText)
                .setRightTextColor(MyApplication.getApplication().getResources().getColor(R.color.colorWhite));
    }

    public static void setDefaultBlackTitleBarRightText(TitleBarView titleBar, String mainText, String rightText){
        titleBar.setTitleMainText(mainText)
                .setRightText(rightText);
    }
}
