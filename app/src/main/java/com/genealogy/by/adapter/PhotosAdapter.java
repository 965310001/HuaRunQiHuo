package com.genealogy.by.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.MyAlbum;

import tech.com.commoncore.manager.GlideManager;

public class PhotosAdapter extends BaseQuickAdapter<MyAlbum.AlbumsBean, BaseViewHolder> {

    private boolean isCheck;

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public PhotosAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyAlbum.AlbumsBean item) {
        ImageView ivImg = helper.getView(R.id.imgs);
        helper.setGone(R.id.albumname, false)
                .setGone(R.id.imgnumber, false);
        if (item != null) {
            GlideManager.loadImg(item.getUrl(), ivImg);
        }

        helper.setGone(R.id.cb_select, isCheck);
        helper.setChecked(R.id.cb_select, false);
        helper.setOnCheckedChangeListener(R.id.cb_select, (buttonView, isChecked) -> item.setSelect(isChecked));
    }

//    @Override
//    protected void convert(BaseViewHolder helper, String item) {
//        ImageView ivImg = helper.getView(R.id.imgs);
//        helper.setGone(R.id.albumname, false)
//                .setGone(R.id.imgnumber, false);
//        if (item != null) {
//            GlideManager.loadImg(item, ivImg);
//        }
//    }
}