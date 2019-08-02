package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
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

/**
 * 添加相册
 */
public class PhotosAddActivity extends BaseTitleActivity implements View.OnClickListener {

    private TextView mTvDescries;
    private EditText mEtTitle;
    private TextView mSwitchShare;
    private boolean isTrue;//是否分享的开关参数

    @Override
    public void initView(Bundle savedInstanceState) {
        mTvDescries = findViewById(R.id.photo_description);
        mEtTitle = findViewById(R.id.photo_title);
        mSwitchShare = findViewById(R.id.switch_share);

        mSwitchShare.setOnClickListener(view -> isTrue = !isTrue);
        findViewById(R.id.tv_create).setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        String title = mEtTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.show("请输入标题...", new ToastUtil.Builder().setGravity(Gravity.CENTER));
        } else {
            execute(title);
            Intent intent = new Intent();
            intent.putExtra("photoDescription", mTvDescries.getText().toString());
            intent.putExtra("photoTitle", title);
            setResult(200, intent);
            PhotosAddActivity.this.finish();
        }
    }

    private void execute(String title) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", SPHelper.getStringSF(PhotosAddActivity.this, "UserId", ""));
        params.put("gId", SPHelper.getStringSF(PhotosAddActivity.this, "GId", ""));
        params.put("title", title);
        params.put("isTrue", String.valueOf(isTrue ? 1 : 0));
        ViseHttp.POST(ApiConstant.album_create)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.isSuccess()) {
                            ToastUtil.show("创建相册请求成功");
                        } else {
                            ToastUtil.show("创建相册请求成功: " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("请求失败: " + errMsg);
                    }
                });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("添加相册");
    }

    @Override
    public int getContentLayout() {
        return R.layout.photos_add;
    }
}