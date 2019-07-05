package com.genealogy.by.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;

public class PersonalHomePageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PersonalHomePageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivUser = helper.getView(R.id.iv_user);
//        GlideManager.loadCircleImg("", ivUser, R.mipmap.icon_user);
//        helper.setText(R.id.tv_name, null, null).setText(R.id.tv_time, DateUtil.formatDate(null, DateUtil.FORMAT_4));
    }
}
