package com.genealogy.by.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;

import tech.com.commoncore.manager.GlideManager;

public class PhotosAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PhotosAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        ImageView ivImg = helper.getView(R.id.imgs);
        helper.setGone(R.id.albumname, false)
                .setGone(R.id.imgnumber, false);
        if (item != null) {
            GlideManager.loadImg(item, ivImg);
        }
    }
}