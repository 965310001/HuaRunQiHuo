package com.genealogy.by.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.DataUtils;
import tech.com.commoncore.utils.ToastUtil;

public class ChangePasswordActivity extends BaseTitleActivity {
    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etNewPasswordConfirm;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("修改密码");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etNewPasswordConfirm = findViewById(R.id.et_new_password_confirm);


        findViewById(R.id.btn_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPasswordChange();
            }
        });
    }


    private void doPasswordChange() {
        if (DataUtils.isEmpty(etOldPassword.getText())) {
            ToastUtil.show("请输入原始密码");
            return;
        }
        if (DataUtils.isEmpty(etNewPassword.getText())) {
            ToastUtil.show("请输入新密码");
            return;
        }
        if (DataUtils.isEmpty(etNewPasswordConfirm.getText())) {
            ToastUtil.show("请输入确认新密码");
            return;
        }
        String oldPass = etOldPassword.getText().toString();
        final String newpass = etNewPassword.getText().toString();
        String newPassConfirm = etNewPasswordConfirm.getText().toString();

        if (!newpass.equals(newPassConfirm)) {
            ToastUtil.show("密码不一致");
            return;
        }
        //请求网络的操作；

    }
}
