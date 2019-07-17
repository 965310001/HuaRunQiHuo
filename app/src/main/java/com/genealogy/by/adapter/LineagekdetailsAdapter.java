package com.genealogy.by.adapter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.utils.SPHelper;

public class LineagekdetailsAdapter extends BaseQuickAdapter<FamilyBook.LineageTableBean , BaseViewHolder> {
    public LineagekdetailsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyBook.LineageTableBean item) {
        TextView name = helper.getView(R.id.name);
        TextView page = helper.getView(R.id.page);
        page.setText(SPHelper.getStringSF(mContext,"pages"));
        name.setText(item.getSurname()+item.getName());
        helper.setOnClickListener(R.id.lin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", item);
            }
        });
    }
}
