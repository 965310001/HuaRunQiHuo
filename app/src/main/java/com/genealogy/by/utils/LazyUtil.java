package com.genealogy.by.utils;

import android.content.Context;

import com.vise.xsnow.common.GsonUtil;

import tech.com.commoncore.utils.SPUtil;


/**
 * 实例化工具类
 *
 * @author LinSq
 * @description:
 * @date :2019/4/1 16:24
 */
public class LazyUtil {
    public static <T> T getLazy(Context context, Class<T> tClass) {
        String data = (String) SPUtil.get(context, tClass.getName(), "");
        if (data != null) {
            return GsonUtil.gson().fromJson(data, tClass);
        }
        return null;
    }

    public static <T> void saveLazy(Context context, T t) {
        SPUtil.put(context, t.getClass().getName(), GsonUtil.gson().toJson(t));
    }
}
