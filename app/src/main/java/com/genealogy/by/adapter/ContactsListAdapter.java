package com.genealogy.by.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.SearchNearInBlood;

import tech.com.commoncore.manager.GlideManager;

public class ContactsListAdapter extends BaseQuickAdapter<SearchNearInBlood, BaseViewHolder> {
    public ContactsListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchNearInBlood item) {
        if (item.getProfilePhoto() != null) {
            ImageView ivImg = helper.getView(R.id.iv_img);
            GlideManager.loadImg(item.getProfilePhoto(), ivImg);
        }
        helper.setText(R.id.tv_name, String.format("%s%s", item.getSurname(), item.getName()));
        helper.addOnClickListener(R.id.ll_item_contacts);
    }
}