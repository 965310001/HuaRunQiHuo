package com.genealogy.by.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;

import tech.com.commoncore.base.BaseTitleActivity;

public class PopupActivity extends BaseTitleActivity implements View.OnClickListener {
    private TextView edit,add,core,details,relationship,invitation;
    private RelativeLayout ll1;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("修改密码");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_popup;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ll1 = findViewById(R.id.ll1);
        ll1.getBackground().setAlpha(255);
        edit = findViewById(R.id.edit);
        add = findViewById(R.id.add);
        core = findViewById(R.id.core);
        details = findViewById(R.id.details);
        relationship = findViewById(R.id.relationship);
        invitation = findViewById(R.id.invitation);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit:
                break;
            case R.id.add:
                break;
            case R.id.core:
                break;
            case R.id.details:
                break;
            case R.id.relationship:
                break;
            case R.id.invitation:
                break;
        }
    }
}
