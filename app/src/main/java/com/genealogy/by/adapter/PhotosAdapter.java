package com.genealogy.by.adapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.activity.PhotosDetailsActivity;
import com.genealogy.by.entity.MyAlbum;

import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;

public class PhotosAdapter extends BaseQuickAdapter<String , BaseViewHolder> {
    public PhotosAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        ImageView ivImg = helper.getView(R.id.imgs);
        TextView albumname = helper.getView(R.id.albumname);
        TextView imgnumber = helper.getView(R.id.imgnumber);
        RelativeLayout llall = helper.getView(R.id.llall);
        albumname.setVisibility(View.GONE);
        imgnumber.setVisibility(View.GONE);
        if(item!=null){
            GlideManager.loadImg(item, ivImg);
        }
    }
}