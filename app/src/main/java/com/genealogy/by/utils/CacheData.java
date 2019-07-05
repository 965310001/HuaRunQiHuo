package com.genealogy.by.utils;

import android.content.Context;

import tech.com.commoncore.Config;
import tech.com.commoncore.constant.BussConstant;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.SignUtil;


/**
 * Anthor:HeChuan
 * Time:2018/11/21
 * Desc: 内容缓存类
 */
public class CacheData {


    public static void initLoginAccount(Context context, String phone, String passwrod) {
        setLoginAccount(context, phone);
        setLoginPassword(context, passwrod);
    }

    //设置登录账号
    public static void setLoginAccount(Context context, String phone) {
        String signPhone = SignUtil.getAESEncodeString(phone, Config.AES_KEY);
        SPHelper.setStringSF(context, BussConstant.LOGIN_ACCOUNT, signPhone);
    }

    //获取登录账号
    public static String getLoginAccount(Context context) {
        String signPhone = SPHelper.getStringSF(context, BussConstant.LOGIN_ACCOUNT);
        return SignUtil.getAESDecodeString(signPhone, Config.AES_KEY);
    }

    //设置登录密码
    public static void setLoginPassword(Context context, String password) {
        String signPassword = SignUtil.getAESEncodeString(password, Config.AES_KEY);
        SPHelper.setStringSF(context, BussConstant.LOGIN_PASSWORD, signPassword);
    }

    //获取登录密码
    public static String getLoginPassword(Context context) {
        String signPassword = SPHelper.getStringSF(context, BussConstant.LOGIN_PASSWORD);
        return SignUtil.getAESDecodeString(signPassword, Config.AES_KEY);
    }

    //消息开关
    public static boolean isOpenMessage(Context context) {
        return SPHelper.getBooleanSF(context, "openMeassage", true);
    }

    //消息开关
    public static void setMessageButton(Context context, boolean isOpen) {
        SPHelper.setBooleanSF(context, "openMeassage", isOpen);
    }

    //常亮开关
    public static boolean isOpenLight(Context context) {
        return SPHelper.getBooleanSF(context, "openLight", true);
    }

    //常亮开关
    public static void setLight(Context context, boolean isOpen) {
        SPHelper.setBooleanSF(context, "openLight", isOpen);
    }
    public static void setSignRecord(Context context, boolean sign) {
        SPHelper.setBooleanSF(context, com.vise.utils.assist.DateUtil.getYmd(System.currentTimeMillis()), sign);
    }

    public static boolean getSignRecord(Context context) {
        return SPHelper.getBooleanSF(context, com.vise.utils.assist.DateUtil.getYmd(System.currentTimeMillis()), false);
    }


}
