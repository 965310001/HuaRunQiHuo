package com.genealogy.by.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.activity.PerfectingInformationActivity;
import com.genealogy.by.activity.PhotosDetailsActivity;
import com.genealogy.by.entity.Journal;
import com.genealogy.by.entity.MyAlbum;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;

public class AlbumAdapter extends BaseQuickAdapter<MyAlbum, BaseViewHolder> {
    public AlbumAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyAlbum item) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i <item.getAlbums().size() ; i++) {
            list.add(item.getAlbums().get(i).getUrl());
        }
        ImageView ivImg = helper.getView(R.id.imgs);
        TextView albumname = helper.getView(R.id.albumname);
        TextView imgnumber = helper.getView(R.id.imgnumber);
        RelativeLayout llall = helper.getView(R.id.llall);
        llall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Id", String.valueOf(item.getId()));
                bundle.putString("Title",item.getTitle());
                bundle.putString("IsTrue",item.getIsTrue());
                bundle.putStringArrayList("Urls", (ArrayList<String>) list);
                FastUtil.startActivity(mContext, PhotosDetailsActivity.class,bundle);
            }
        });
        if(item.getAlbums().size()>0){
            GlideManager.loadImg(item.getAlbums().get(0).getUrl(), ivImg);
        }
        albumname.setText(item.getTitle());
        imgnumber.setText(item.getAlbums().size()+"张");
    }
}