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
import com.genealogy.by.entity.FamilyPhoto;
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
    private int mIndex;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");

        if (mIntent.hasExtra("index")) {
            mIndex = mIntent.getIntExtra("index", 0);
        }

        titleBar.setTitleMainText("录入简介")
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
        helper.setMaxCount(1);
        if (type != null && type.contains("录入")) {
            recyclerView.setVisibility(View.GONE);
        }
        if (mIntent.hasExtra("id")) {
            familyAlbum = mIntent.getIntExtra("id", 0);
        }

        if (mIntent.hasExtra("Introduction")) {
            etText.setText(mIntent.getStringExtra("Introduction"));
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
//                .addFormDataPart("introduction", introduction)
                .addFormDataPart("id", String.valueOf(familyAlbum))
                .build();
        ViseHttp.POST(ApiConstant.familyBook_uploadImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(requestBody)
                .request(new ACallback<BaseTResp2<FamilyPhoto>>() {
                    @Override
                    public void onSuccess(BaseTResp2<FamilyPhoto> data) {
                        hideLoading();
                        if (data.status == 200) {
                            ToastUtil.show("提交成功: " + data.msg);
                            finish();
                        } else {
                            ToastUtil.show(data.msg + "");
                        }
                        mIntent = new Intent();
                        mIntent.putExtra("index", mIndex);
                        mIntent.putExtra("data", data.data);
                        mIntent.putExtra("content", data.data.getIntroduction());
                        setResult(202, mIntent);
                        finish();
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
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                .addFormDataPart("imgs", url, image)
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
                        mIntent = new Intent();
                        mIntent.putExtra("content", introduction);
                        mIntent.putExtra("index", mIndex);
                        setResult(202, mIntent);
                        finish();
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show("失败: " + errMsg);
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