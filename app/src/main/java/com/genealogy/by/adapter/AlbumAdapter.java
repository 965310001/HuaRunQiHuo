package com.genealogy.by.adapter;

import android.os.Bundle;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.activity.PhotosDetailsActivity;
import com.genealogy.by.entity.MyAlbum;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;

public class AlbumAdapter extends BaseQuickAdapter<MyAlbum, BaseViewHolder> {
    public AlbumAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyAlbum item) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < item.getAlbums().size(); i++) {
            list.add(item.getAlbums().get(i).getUrl());
        }
        helper.getView(R.id.llall).setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("Id", String.valueOf(item.getId()));
            bundle.putString("Title", item.getTitle());
            bundle.putString("IsTrue", item.getIsTrue());
            bundle.putStringArrayList("Urls", (ArrayList<String>) list);
            FastUtil.startActivity(mContext, PhotosDetailsActivity.class, bundle);
        });
        if (item.getAlbums().size() > 0) {
            ImageView ivImg = helper.getView(R.id.imgs);
            GlideManager.loadImg(item.getAlbums().get(0).getUrl(), ivImg);
        }
        helper.setText(R.id.albumname, item.getTitle())
                .setText(R.id.imgnumber, String.format("%d张", item.getAlbums().size()));
    }
}