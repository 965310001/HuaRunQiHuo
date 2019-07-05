package com.genealogy.by.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;

import tech.com.commoncore.manager.GlideManager;

/**
 * Created by dell on 2019/4/18.
 */

public class ImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    int width;

    public ImgAdapter(int layoutResId) {
        super(layoutResId);
    }

    public ImgAdapter(int layoutResId, int w) {
        super(layoutResId);
        width = w;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView iv = helper.getView(R.id.iv_img);
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.width = width;
        params.height = width;
        iv.setLayoutParams(params);
        GlideManager.loadImg(item, iv);
    }
}
