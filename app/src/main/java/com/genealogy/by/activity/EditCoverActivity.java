package com.genealogy.by.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.entity.BookEdit;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/22 调试接口
public class EditCoverActivity extends BaseTitleActivity {

    protected Context mContext;
    private String TAG = "EditCoverActivity";

    private String id;
    private EditText nameEditText, personEditText, timeEditText, addressEditText;
    //封面参数
    private String name, person, time, address;

    @Override
    public void initView(Bundle savedInstanceState) {
        mContext = this;

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        //设置返回参数
        name = intent.getStringExtra("name");
        person = intent.getStringExtra("person");
        time = intent.getStringExtra("time");
        address = intent.getStringExtra("address");

        nameEditText = findViewById(R.id.name);
        personEditText = findViewById(R.id.person);
        timeEditText = findViewById(R.id.time);
        addressEditText = findViewById(R.id.address);
        //设置封面布局 参数
        if (name != null && !name.equals("")) {
            nameEditText.setText(name);
        }
        if (person != null && !person.equals("")) {
            personEditText.setText(person);
        }
        if (time != null && !time.equals("")) {
            timeEditText.setText(time);
        }
        if (address != null && !address.equals("")) {
            addressEditText.setText(address);
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("册谱封面信息").setRightText("提交").setOnRightTextClickListener(v -> doit());
    }

    public void doit() {
        name = nameEditText.getText().toString();
        person = personEditText.getText().toString();
        time = timeEditText.getText().toString();
        address = addressEditText.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        if (name != null && name.trim().length() != 0) {
            params.put("familybookName", name);
        } else {
            ToastUtil.show("请填写家谱名称");
            return;
        }
        if (person != null && person.trim().length() != 0) {
            params.put("sponsor", person);
        } else {
            ToastUtil.show("请填写发起人");
            return;
        }
        if (time != null && time.trim().length() != 0) {
            params.put("editingTime", time);
        } else {
            ToastUtil.show("请填写编谱日期");
            return;
        }
        if (address != null && address.trim().length() != 0) {
            params.put("catalogingAddress", address);
        } else {
            ToastUtil.show("请填写编谱地址");
            return;
        }
        if (id != null && id.trim().length() != 0) {
            params.put("id", id);
        } else {
            ToastUtil.show("未知错误，请重新登录");
            return;
        }
        ViseHttp.POST(ApiConstant.familyBook_edit)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2<BookEdit>>() {
                    @Override
                    public void onSuccess(BaseTResp2<BookEdit> data) {
                        if (data.status == 200) {
                            ToastUtil.show(" 请求成功" + data.msg);
                            Intent intent = new Intent();
                            intent.putExtra("name", name);
                            intent.putExtra("person", person);
                            intent.putExtra("time", time);
                            intent.putExtra("address", address);
                            setResult(200, intent);  // 200表示成功
                            EditCoverActivity.this.finish();

//                            if (nameEditText.getText().toString().trim().length() != 0 &&
//                                    nameEditText.getText().toString() != null) {
//                                intent.putExtra("name", nameEditText.getText().toString());
//                            } else {
//                                intent.putExtra("name", name);
//                            }
//                            if (personEditText.getText().toString().trim().length() != 0 &&
//                                    personEditText.getText().toString() != null) {
//                                intent.putExtra("person", personEditText.getText().toString());
//                            } else {
//                                intent.putExtra("person", person);
//                            }
//                            if (timeEditText.getText().toString().trim().length() != 0 &&
//                                    timeEditText.getText().toString() != null) {
//                                intent.putExtra("time", timeEditText.getText().toString());
//                            } else {
//                                intent.putExtra("time", time);
//                            }
//                            if (addressEditText.getText().toString().trim().length() != 0 &&
//                                    addressEditText.getText().toString() != null) {
//                                intent.putExtra("address", addressEditText.getText().toString());
//                            } else {
//                                intent.putExtra("address", address);
//                            }
                        } else {
                            Log.e(TAG, "请求失败: " + data.status);
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

    @Override
    public int getContentLayout() {
        return R.layout.edit_cover;
    }
}