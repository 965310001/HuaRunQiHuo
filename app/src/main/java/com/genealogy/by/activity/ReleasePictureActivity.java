package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.MainActivity;
import com.genealogy.by.R;
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.entity.PhoneLogin;
import com.genealogy.by.entity.PhoneUser;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.genealogy.by.utils.my.MyGlideEngine;
import com.genealogy.by.utils.my.PictureSelectorHelper;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.DataUtils;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

public class ReleasePictureActivity extends BaseTitleActivity {
    RecyclerView recyclerView;

    PictureSelectorHelper helper;
    EditText etText;
    String imgs;
    int id=0;
    String Imgid="";
    String type="";
    @Override
    public void setTitleBar(TitleBarView titleBar) {
        Intent intent =getIntent();
        Imgid = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        titleBar.setTitleMainText("发布图片").setRightTextColor(getResources().getColor(R.color.C_F47432)).setRightText("发布").setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.contains("录入")){
                    if (DataUtils.isEmpty(etText.getText())) {
                        ToastUtil.show("请输入评论内容");
                        return;
                    }
                    imgs = "";
                    InputDoit(helper.getPictureList(), 0);
                }else{
                    imgs = "";
                    upLoadPic(helper.getPictureList(), 0);
                }


            }
        });
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_release_pictures;
    }
    @Override
    public void initView(Bundle savedInstanceState) {
        FamilyBook familyBook =SPHelper.getDeviceData(mContext,"familyBook");
        id = familyBook.getId();
        etText = mContentView.findViewById(R.id.et_text);
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        helper = new PictureSelectorHelper(mContext, recyclerView);
    }
    //上传图片
    File file = new File("");
    private void upLoadPic(final List<String> urls, final int position) {
        String url= urls.get(0);
        file=new File(url);
        ViseHttp.POST(ApiConstant.familyBook_uploadImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .addParam("id", String.valueOf(id))//族册ID
                .addForm("imgs",file)//图片
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if(data.status==200){
                            ToastUtil.show("提交成功: "+data.msg);
                            finish();
                        }else{
                            ToastUtil.show(data.msg+"");
                        }
                    }
                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: "+errMsg+"，errCode: "+errCode);
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
    public void InputDoit(final List<String> urls,int id ){
        ViseHttp.POST(ApiConstant.familyBook_editImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .addParam("id", String.valueOf(id))
                .addForm("imgs", file)
                .addParam("introduction", etText.getText().toString())
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.status == 200) {
                            Log.e(TAG, "onSuccess: 照片删除成功  msg= " + data.msg);
                        } else {
                            Log.e(TAG, "onSuccess:  照片删除失败  msg= " + data.msg);
                            ToastUtil.show("请求失败 " + data.msg);
                        }
                    }
                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
                    }
                });
    }
}
