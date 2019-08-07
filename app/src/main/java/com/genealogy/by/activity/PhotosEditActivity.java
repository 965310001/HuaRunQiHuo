package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.entity.Album;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

public class PhotosEditActivity extends BaseTitleActivity {

    private TextView mTvDescries;
    private EditText mEtTitle;
    private String TAG = "PhotosAddActivity";
    private TextView mSwitchShare;
    private boolean isTrue;//是否分享的开关参数
    private Intent mIntent;
    private int mId;

    @Override
    public void initView(Bundle savedInstanceState) {
        mIntent = getIntent();

        mTvDescries = findViewById(R.id.photo_description);
        mEtTitle = findViewById(R.id.photo_title);
        mSwitchShare = findViewById(R.id.switch_share);
        mSwitchShare.setOnClickListener(view -> isTrue = !isTrue);
        //取到id 暂时没用

        if (mIntent.hasExtra("data")) {
            Album album = (Album) mIntent.getSerializableExtra("data");
            mId = album.getId();
            mEtTitle.setText(album.getText());
        }

        findViewById(R.id.tv_create).setOnClickListener(v -> {
            String title = mEtTitle.getText().toString();
            if (TextUtils.isEmpty(title)) {
                ToastUtil.show("请输入标题！");
            } else {
                execute(title, isTrue ? 1 : 0);
            }
        });
    }

    public void execute(String title, int isTrue) {
        showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(mId));
        params.put("title", title);
        params.put("isTrue", String.valueOf(isTrue));
        ViseHttp.POST(ApiConstant.album_edit)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        hideLoading();
                        if (data.isSuccess()) {
                            SPHelper.setBooleanSF(mContext, "REFRESH_PHOTO", true);
                            ToastUtil.show("编辑相册请求成功");
                            mIntent = new Intent();
                            mIntent.putExtra("title", title);
                            setResult(202, mIntent);
                            PhotosEditActivity.this.finish();
                        } else {
                            Log.e(TAG, "onSuccess: 编辑相册请求成功  msg= " + data.msg);
                            ToastUtil.show("创建相册失败: " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show("请求失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
                    }
                });

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("编辑相册");
    }

    @Override
    public int getContentLayout() {
        return R.layout.photos_edit;
    }


}