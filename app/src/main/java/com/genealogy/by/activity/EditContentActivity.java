package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TextView title;
    private ImageView back;
    private EditText evcontent;
    private TextView saveText;
    private String index;
    private String id;
    public String fieldname;


    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        saveText = findViewById(R.id.saveText);
        evcontent = findViewById(R.id.content);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        saveText.setOnClickListener(v -> execute());

        //设置标题
        String name = intent.getStringExtra("title");
        index = intent.getStringExtra("index");
        if (name != null) {
            title = findViewById(R.id.title);
            title.setText(name);
            if (name.contains("编委会")) {
                fieldname = "editorialCommittee";
            } else if (name.contains("家族序言")) {
                fieldname = "genealogyPreface";
            } else if (name.contains("姓氏来源")) {
                fieldname = "lastNameSource";
            } else if (name.contains("家规家训")) {
                fieldname = "familyRule";
            } else if (name.contains("人物传")) {
                fieldname = "characterBiography";
            } else if (name.contains("大记事")) {
                fieldname = "bigNote";
            } else if (name.contains("后记")) {
                fieldname = "postscript";
            }
        }
    }

    public void execute() {
        HashMap<String, String> params = new HashMap<>();
        String content = evcontent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show("请填写内容");
            return;
        }

        params.put(fieldname, content);
        if (id != null && id.trim().length() != 0) {
            params.put("id", id);
        } else {
            ToastUtil.show("未知错误，请重新登录");
        }
        JSONObject jsonObject = new JSONObject(params);
        ViseHttp.POST(ApiConstant.familyBook_edit)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(jsonObject)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.status == 200) {
                            ToastUtil.show(data.msg);
                            Intent intent = new Intent();
                            intent.putExtra("content", evcontent.getText().toString());
                            intent.putExtra("index", index);
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
                        Log.e("", "errMsg: " + errMsg + "errCode:  " + errCode);
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