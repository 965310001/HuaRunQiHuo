package com.genealogy.by.activity;

import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.view.FamilyTreeView8;

import tech.com.commoncore.base.BaseTitleActivity;

public class RelationshipChainActivity extends BaseTitleActivity {

    private SearchNearInBlood data;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("查看关系链").setOnLeftTextClickListener(v -> finish());
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_relationship_chain;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        data = (SearchNearInBlood) getIntent().getSerializableExtra("data");

        FamilyTreeView8 familyTreeView = findViewById(R.id.ftv_tree8);
        familyTreeView.setFamilyMember(data);
    }

}
