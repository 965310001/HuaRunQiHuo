package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.genealogy.by.utils.my.PictureSelectorHelper;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DataUtils;
import tech.com.commoncore.utils.ToastUtil;

public class ReleasePictureActivity extends BaseTitleActivity {
    private Intent mIntent;
    private RecyclerView recyclerView;
    private PictureSelectorHelper helper;
    private EditText etText;
    private String imgs;
    private int familyAlbum = 0;
    private String type = "";
    //上传图片
    private File file;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");
        titleBar.setTitleMainText("发布图片")
                .setRightTextColor(getResources()
                        .getColor(R.color.C_F47432))
                .setRightText("发布")
                .setOnRightTextClickListener(v -> {
                    if (!TextUtils.isEmpty(type) && type.contains("录入")) {
                        if (DataUtils.isEmpty(etText.getText().toString())) {
                            ToastUtil.show("请输入评论内容");
                            return;
                        }
                        imgs = "";
                        InputDoit(helper.getPictureList());
                    } else {
                        imgs = "";
                        upLoadPic(helper.getPictureList());
                    }
                });
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_release_pictures;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        FamilyBook familyBook = SPHelper.getDeviceData(mContext, "familyBook");
        familyAlbum = familyBook.getId();
        etText = mContentView.findViewById(R.id.et_text);
        if (mIntent.getStringExtra("Introduction") != null) {
            etText.setText(mIntent.getStringExtra("Introduction"));
        }
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        helper = new PictureSelectorHelper(mContext, recyclerView);
        if (type != null && type.contains("录入")) {
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void upLoadPic(final List<String> urls) {
        showLoading();
        String url = urls.get(0);
        file = new File(url);
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imgs", url, image)
                .addFormDataPart("id", String.valueOf(familyAlbum))
                .build();
        ViseHttp.POST(ApiConstant.familyBook_uploadImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(requestBody)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        hideLoading();
                        if (data.status == 200) {
                            ToastUtil.show("提交成功: " + data.msg);
                            finish();
                        } else {
                            ToastUtil.show(data.msg + "");
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show("失败: " + errMsg + "，errCode: " + errCode);
                    }
                });
    }

    public void InputDoit(final List<String> urls) {
        showLoading();
        String introduction = etText.getText().toString();
        String url = "";
        if (urls.size() != 0) {
            url = urls.get(0);
        }
        if (url.length() > 0) {
            file = new File(url);
        } else {
            file = new File("");
        }
        RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imgs", url, image)
                .addFormDataPart("introduction", introduction)
                .addFormDataPart("id", String.valueOf(familyAlbum))
                .build();
        ViseHttp.POST(ApiConstant.familyBook_editImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(requestBody)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        hideLoading();
                        if (data.status == 200) {
                            Log.e(TAG, "onSuccess: 照片简介录入  msg= " + data.msg);
                            ToastUtil.show("录入成功 " + data.msg);
                        } else {
                            Log.e(TAG, "onSuccess:  简介录入错误  msg= " + data.msg);
                            ToastUtil.show("录入错误 " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show("失败: " + errMsg);
                        /*Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);*/
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = (ArrayList<String>) Matisse.obtainPathResult(data);
            helper.setActivityResult(result);
        }
    }

}