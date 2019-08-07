package com.genealogy.by.activity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.EditText;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.MainActivity;
import com.genealogy.by.R;
import com.genealogy.by.entity.PhoneLogin;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DataUtils;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.RegUtils;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.widget.CountDownTextView;

// TODO: 2019/7/22 调试接口
public class RegisterActivity extends BaseTitleActivity {
    private EditText etPhone;
    private EditText etVarifyCode;
    private CountDownTextView tvVerifyCode;
    private EditText etPassword;

    private String phone = "";
    private String varify_code = "";

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("注册");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        etPhone = findViewById(R.id.et_phone);
        etVarifyCode = findViewById(R.id.et_varify_code);
        tvVerifyCode = findViewById(R.id.tv_verify_code);
        etPassword = findViewById(R.id.et_password);
        tvVerifyCode.setCountDownColor(R.color.C_E48B81, R.color.C_E48B81);
        tvVerifyCode.setOnClickListener(v -> senVerifyCode());
        findViewById(R.id.btn_regist).setOnClickListener(v -> doRegistered());
    }

    private void doRegistered() {
        if (verifyPhone()) {
            return;
        }
        if (DataUtils.isEmpty(etVarifyCode.getText())) {
            ToastUtil.show("请输入验证码");
            return;
        }
        varify_code = etVarifyCode.getText().toString().trim();
        JSONObject json = new JSONObject();
        try {
            json.put("phone", phone);
            json.put("code", varify_code);
        } catch (JSONException e) {
            ToastUtil.show("参数设置失败");
        }
        ViseHttp.POST(ApiConstant.Account_register)
                .baseUrl(ApiConstant.BASE_URL_ZP)
                .setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(json)
                .request(new ACallback<BaseTResp2<PhoneLogin>>() {
                    @Override
                    public void onSuccess(BaseTResp2<PhoneLogin> data) {
                        hideLoading();
                        if (data.isSuccess()) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("userId", data.data.getUserId());
//                            bundle.putString("gId", data.data.getGId() + "");
                            SPHelper.setStringSF(mContext, "profilePhoto", String.valueOf(data.data.getProfilePhoto()));//头像
                            SPHelper.setStringSF(mContext, "nickName", String.valueOf(data.data.getNickName()));//昵称
                            SPHelper.setStringSF(mContext, "GId", String.valueOf(data.data.getGId()));
                            SPHelper.setStringSF(mContext, "Phone", phone);
                            SPHelper.setStringSF(mContext, "UserId", data.data.getUserId());
                            SPHelper.setStringSF(mContext, "Authorization", data.data.getAuthorization());
                            Map<String, String> map = new HashMap<>();
                            map.put("Authorization", data.data.getAuthorization());
                            ViseHttp.CONFIG().globalHeaders(map);
                            register();
//                            FastUtil.startActivity(mContext, MainActivity.class, bundle);
                            FastUtil.startActivity(mContext, MainActivity.class);
                        } else if (data.status == 202) {
                            showLoading();
//                            SPHelper.setStringSF(mContext, "GId", String.valueOf(data.data.getGId()));
//                            SPHelper.setStringSF(mContext, "Phone", phone);
//                            SPHelper.setStringSF(mContext, "UserId", data.data.getUserId());
//                            SPHelper.setStringSF(mContext, "Authorization", data.data.getAuthorization());
//                            Map<String, String> map = new HashMap<>();
//                            map.put("Authorization", data.data.getAuthorization());
//                            ViseHttp.CONFIG().globalHeaders(map);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("title", "无");
                            bundle.putBoolean("isRegister", true);
                            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                        } else {
                            showLoading();
                            ToastUtil.show(data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show("注册失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
                    }
                });
    }

    private boolean verifyPhone() {
        //手机号码空校验
        if (DataUtils.isEmpty(etPhone.getText())) {
            ToastUtil.show("请输入手机号");
            return true;
        }
        phone = etPhone.getText().toString().trim();

        //手机号有效性校验
        if (!RegUtils.isMobile(phone)) {
            ToastUtil.show("请输入有效手机号");
            return true;
        }
        return false;
    }

    //获取验证码
    private void senVerifyCode() {
        if (verifyPhone()) {
            return;
        }
        showLoading();
        ViseHttp.GET(ApiConstant.Account_Send_messages).baseUrl(ApiConstant.BASE_URL_ZP)
                .addParam("phone", phone)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        hideLoading();
                        ToastUtil.show(data.msg);
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show("请求失败");
                        Log.e(TAG, "onFail: " + errMsg + ";errCode=" + errCode);
                    }
                });
    }

    private void register() {
        new Thread(() ->
                EMClient.getInstance().login(SPHelper.getStringSF(mContext, "UserId"), "zupu123456", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Looper.prepare();
                        showLoading();
                        Log.e(TAG, "onSuccess: 环信登录成功");
                        EMClient.getInstance().chatManager().loadAllConversations();
                    }

                    @Override
                    public void onError(int i, String s) {
                        showLoading();
                        ToastUtil.show(s);
                        Log.i(TAG, "环信登录失败:" + s + i);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Log.e(TAG, "onProgress: 正在请求 : " + s);
                    }
                })
        ).start();
    }
}