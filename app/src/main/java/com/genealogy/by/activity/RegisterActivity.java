package com.genealogy.by.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.progress.UIProgressDialog;
import com.genealogy.by.MainActivity;
import com.genealogy.by.R;
import com.genealogy.by.entity.PhoneLogin;
import com.genealogy.by.utils.CacheData;
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
    private String password = "";

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
                        if (data.status == 200) {
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", data.data.getUserId());
                            bundle.putString("gId", data.data.getGId() + "");
                            register();
                            SPHelper.setStringSF(mContext, "profilePhoto", String.valueOf(data.data.getProfilePhoto()));//头像
                            SPHelper.setStringSF(mContext, "nickName", String.valueOf(data.data.getNickName()));//昵称
                            SPHelper.setStringSF(mContext, "GId", String.valueOf(data.data.getGId()));
                            SPHelper.setStringSF(mContext, "Phone", phone);
                            SPHelper.setStringSF(mContext, "UserId", data.data.getUserId());
                            SPHelper.setStringSF(mContext, "Authorization", data.data.getAuthorization());
                            Map<String, String> map = new HashMap<>();
                            map.put("Authorization", data.data.getAuthorization());
                            ViseHttp.CONFIG().globalHeaders(map);
                            FastUtil.startActivity(mContext, MainActivity.class, bundle);
                        } else if (data.status == 202) {
                            SPHelper.setStringSF(mContext, "GId", String.valueOf(data.data.getGId()));
                            SPHelper.setStringSF(mContext, "Phone", phone);
                            SPHelper.setStringSF(mContext, "UserId", data.data.getUserId());
                            SPHelper.setStringSF(mContext, "Authorization", data.data.getAuthorization());
                            Map<String, String> map = new HashMap<>();
                            map.put("Authorization", data.data.getAuthorization());
                            ViseHttp.CONFIG().globalHeaders(map);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("title", "无");
                            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                        } else {
                            ToastUtil.show(" " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("注册失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
                    }
                });
    }


    /**
     * 保存用户登录资料
     */
    private void saveUserInfo(String username, String password) {
        CacheData.initLoginAccount(mContext, username, password);
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
                        ToastUtil.show("请求成功");

                        hideLoading();
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("请求失败");
                        Log.e(TAG, "onFail: " + errMsg + ";errCode=" + errCode);
                        hideLoading();
                    }
                });
    }


    private UIProgressDialog mProgressDialog;

    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new UIProgressDialog.WeBoBuilder(this)
                    .setMessage("进行中...")
                    .setCancelable(false)
                    .create();
        }
        mProgressDialog.setDimAmount(0.6f)
                .show();
    }

    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
    }

    private void register() {
        final String username = etPhone.getText().toString().trim();
        final String pwd = "zupu123456";
        String confirm_pwd = "zupu123456";
        if (TextUtils.isEmpty(username)) {
            etPhone.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            return;
        } else if (TextUtils.isEmpty(confirm_pwd)) {
            return;
        } else if (!pwd.equals(confirm_pwd)) {
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.show();
            new Thread(() -> {
                // call method in SDK
                EMClient.getInstance().login(username, pwd, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e(TAG, "onSuccess: 环信登录成功");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "onError: 环信登录失败 : " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Log.e(TAG, "onProgress: 正在请求 : " + s);
                    }
                });
            }).start();
        }
    }
}