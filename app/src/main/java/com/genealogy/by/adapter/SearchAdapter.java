package com.genealogy.by.adapter;

import android.content.Context;

import com.genealogy.by.R;
import com.genealogy.by.db.User;
import com.genealogy.by.view.ViewHolder;

import java.util.List;

/**
 * Created by yetwish on 2015-05-11
 */

public class SearchAdapter extends CommonAdapter<User> {

    public SearchAdapter(Context context, List<User> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        //Log.d("SearchAdapter",""+ mData.get(position).getIconId());
        holder.setImageResource(R.id.item_search_iv_icon, mData.get(position).getIconId())
                .setText(R.id.name, mData.get(position).getName())
                .setText(R.id.content, mData.get(position).getContent());
    }
}
