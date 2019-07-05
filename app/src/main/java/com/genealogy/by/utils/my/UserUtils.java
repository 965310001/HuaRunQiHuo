package com.genealogy.by.utils.my;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.genealogy.by.R;
import com.genealogy.by.utils.CacheData;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import tech.com.commoncore.utils.ToastUtil;

/**
 * Created by dell on 2019/4/1.
 */

public class UserUtils {
    private static final int REQUEST_CODE_CHOOSE = 1;

    public static void saveUserInfo(String username, String password, Context mContext) {
        CacheData.initLoginAccount(mContext, username, password);
    }




    public static void upDataUserHead(Context context) {
        RxPermissions rxPermission = new RxPermissions((FragmentActivity) context);
        rxPermission
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission) {
                        selectePhoto(context);
                    } else {
                        ToastUtil.show("请同意相关权限");
                    }
                });

    }

    private static void selectePhoto(Context mContext) {
        Matisse.from((Activity) mContext)
                .choose(MimeType.ofImage())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.fdcl.zy.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new MyGlideEngine())//图片加载引擎
                .theme(/*R.style.MyMatisse_Dracula*/  R.style.Matisse_Dracula)
                .forResult(REQUEST_CODE_CHOOSE);//
    }
}
