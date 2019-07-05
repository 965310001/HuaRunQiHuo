package com.genealogy.by.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.Deed;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.utils.ScreenUtils;

public class DeedsAdapter extends BaseQuickAdapter<Deed, BaseViewHolder> {
    public DeedsAdapter(int layoutResId) {
        super(layoutResId);
    }

    int width;

    @Override
    protected void convert(BaseViewHolder helper, Deed item) {
        if (item.getTitle() != null) {
            helper.setText(R.id.tv_name, item.getTitle());
        }
        if (item.getDeedsTime() != null) {
            helper.setText(R.id.time, item.getDeedsTime());
        }
        if (item.getLocation() != null) {
            helper.setText(R.id.location, item.getLocation());
        }
        RecyclerView imgRecycleView = helper.getView(R.id.imgRecycleView);
        width = (ScreenUtils.getScreenWidth(imgRecycleView.getContext()) - ScreenUtils.dip2px(32 + 7 * 2)) / 3;
        GridSpacingItemDecoration decoration = new GridSpacingItemDecoration(3, 7, false);
        imgRecycleView.addItemDecoration(decoration);
        imgRecycleView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ImgAdapter adapter = new ImgAdapter(R.layout.item_img, width);
        imgRecycleView.setAdapter(adapter);
        String imgs = item.getUrls();
        String[] a = imgs.split(",");
        List<String> urls = new ArrayList<>();
        if (imgs != null && !"".equals(imgs) && a != null) {
            for (String url : a) {
                urls.add(url);
            }
            adapter.setNewData(urls);
        }
        helper.addOnClickListener(R.id.tv_follow);
    }
}
