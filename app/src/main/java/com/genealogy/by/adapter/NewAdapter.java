package com.genealogy.by.adapter;

import android.os.Bundle;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.TtEntity;

import tech.com.commoncore.manager.GlideManager;

public class NewAdapter extends BaseQuickAdapter<TtEntity, BaseViewHolder> {
    public NewAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, TtEntity item) {
        helper.setText(R.id.tv_title, item.getTitle()).setText(R.id.tv_author, item.getAuthor());
        ImageView iv = helper.getView(R.id.iv_thumb);
        GlideManager.loadRoundImg(item.getThumb(), iv);
        helper.setOnClickListener(R.id.lin, view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", item);
//                FastUtil.startActivity(mContext, NewDetailActivity.class, bundle);
        });
    }
}
