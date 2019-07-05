package com.genealogy.by.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.Journal;

import tech.com.commoncore.manager.GlideManager;

public class JournalAdapter extends BaseQuickAdapter<Journal, BaseViewHolder> {
    public JournalAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Journal item) {
        ImageView ivImg = helper.getView(R.id.iv_img);
        GlideManager.loadImg(item.getProfilePhoto(), ivImg);
        helper.setText(R.id.tv_log, item.getUName() + item.getOperatingType() + item.getBeName() + item.getContent() + "");

        helper.setOnClickListener(R.id.lin, v -> {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", item);
//                FastUtil.startActivity(mContext, BookDetailActivity.class, bundle);
        });
    }
}