package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

public class EditContentActivity extends BaseTitleActivity {
    private String mIndex;
    private EditText mEtContent;
    private String mId;
    public String mFieldName;

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        mEtContent = findViewById(R.id.content);

        String title = intent.getStringExtra("title");
        mIndex = intent.getStringExtra("mIndex");
        if (title != null) {
            mFieldName = getFieldName(title);
            mTitleBar.setTitleMainText(title);
        }

        String content = intent.getStringExtra("content");
        mEtContent.setText(content);
    }

    private String getFieldName(String title) {
        if (title.contains("编委会")) {
            mFieldName = "editorialCommittee";
        } else if (title.contains("家族序言")) {
            mFieldName = "genealogyPreface";
        } else if (title.contains("姓氏来源")) {
            mFieldName = "lastNameSource";
        } else if (title.contains("家规家训")) {
            mFieldName = "familyRule";
        } else if (title.contains("人物传")) {
            mFieldName = "characterBiography";
        } else if (title.contains("大事记")) {
            mFieldName = "bigNote";
        } else if (title.contains("后记")) {
            mFieldName = "postscript";
        }
        return mFieldName;
    }

    private void execute() {
        String content = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show("请填写内容");
            return;
        }
        if (TextUtils.isEmpty(mId)) {
            ToastUtil.show("未知错误，请重新登录");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(mFieldName, content);
        params.put("id", mId);
        ViseHttp.POST(ApiConstant.familyBook_edit)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.status == 200) {
                            ToastUtil.show(data.msg);
                            Intent intent = new Intent();
                            intent.putExtra("content", content);
                            intent.putExtra("index", mIndex);
                            setResult(201, intent);  // 201表示成功
                            EditContentActivity.this.finish();
                            setResult(200, intent);  // 200表示成功
                        } else {
                            ToastUtil.show("请求失败 " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: " + errMsg);
                    }
                });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("编委会")
                .setRightText("提交")
                .setOnRightTextClickListener(view -> execute());
    }

    @Override
    public int getContentLayout() {
        return R.layout.edit_content;
    }
}