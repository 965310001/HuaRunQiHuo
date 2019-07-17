package com.genealogy.by.Ease;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.genealogy.by.Ease.Logs.Logs;
import com.genealogy.by.Ease.model.Model;
import com.genealogy.by.Ease.ui.CustomToolbar;
import com.genealogy.by.R;
import com.genealogy.by.activity.RegisterActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class SystemSetting extends AppCompatActivity {

    private Button btnLoginOut;
    private CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);

        toolbar = (CustomToolbar) findViewById(R.id.setting_toolbar);
        toolbar.setTitle("设置");

        btnLoginOut = (Button) findViewById(R.id.setting_login_out);
        btnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Logs.d("LOGIN_OUT","退出成功");
                                //删除用户数据库记录
                                Model.getInstance().getDbManager().getUserTableDao().LoginOut();
                                Intent intent = new Intent(SystemSetting.this, RegisterActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {

                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }

}
