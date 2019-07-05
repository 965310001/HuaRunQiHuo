package com.genealogy.by.fragment;

import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.genealogy.by.R;

import tech.com.commoncore.base.BaseTitleRefreshLoadFragment;

public class DemoFragment extends BaseTitleRefreshLoadFragment {
    public static DemoFragment newInstance() {
        Bundle args = new Bundle();
        DemoFragment fragment = new DemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public BaseQuickAdapter getAdapter() {
        return null;
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab_wode;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }
}
