package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class PhotosAddActivity extends BaseTitleActivity {

    private TextView mTvDescries;
    private EditText mEtTitle;
    private String TAG = "PhotosAddActivity";
    private TextView mSwitchShare;
    private boolean isTrue;//是否分享的开关参数

    public void initView() {
        mTvDescries = findViewById(R.id.photo_description);
        mEtTitle = findViewById(R.id.photo_title);
        mSwitchShare = findViewById(R.id.switch_share);
        mSwitchShare.setOnClickListener(view -> isTrue = !isTrue);
        //取到id 暂时没用
        /*getIntent().getStringExtra("id");*/

        findViewById(R.id.tv_create).setOnClickListener(v -> {
            String title = mEtTitle.getText().toString();
            if (TextUtils.isEmpty(title)) {
                Toast.makeText(PhotosAddActivity.this, "请输入标题！", Toast.LENGTH_SHORT).show();
            } else {
                execute(title, isTrue ? 1 : 0);
                Intent intent = new Intent();
                intent.putExtra("photoDescription", mTvDescries.getText().toString());
                intent.putExtra("photoTitle", title);
                setResult(200, intent);
                PhotosAddActivity.this.finish();
            }
        });
    }

    public void execute(String title, int isTrue) {
        String userId = SPHelper.getStringSF(PhotosAddActivity.this, "UserId", "");
        String gid = SPHelper.getStringSF(PhotosAddActivity.this, "GId", "");
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("gId", gid);
        params.put("title", title);
        params.put("isTrue", String.valueOf(isTrue));
        Log.e(TAG, "请求参数: 参数：" + params.toString());
        JSONObject jsonObject = new JSONObject(params);
        ViseHttp.POST(ApiConstant.album_create)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(jsonObject)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.isSuccess()) {
                            ToastUtil.show("创建相册请求成功");
                        } else {
                            Log.e(TAG, "onSuccess: 创建相册请求成功  msg= " + data.msg);
                            ToastUtil.show("创建相册失败: " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("请求失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
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

    @Override
    public void initView(Bundle savedInstanceState) {
        initView();
    }
}