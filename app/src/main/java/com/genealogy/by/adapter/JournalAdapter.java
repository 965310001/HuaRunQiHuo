package com.genealogy.by.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.utils.my.BaseTResp;

import tech.com.commoncore.manager.GlideManager;

public class JournalAdapter extends BaseQuickAdapter<BaseTResp.DataBean, BaseViewHolder> {
    public JournalAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseTResp.DataBean item) {
        ImageView ivImg = helper.getView(R.id.iv_img);
        if (item.getProfilePhoto() != null) {
            GlideManager.loadImg(item.getProfilePhoto(), ivImg);
        }
        helper.setText(R.id.tv_log, String.format("%s%s%s", item.getUName(), item.getOperatingType(), item.getBeName(), item.getContent()));
    }
}