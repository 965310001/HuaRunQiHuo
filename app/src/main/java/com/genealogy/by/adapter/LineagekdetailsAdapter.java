package com.genealogy.by.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.utils.SPHelper;

public class LineagekdetailsAdapter extends BaseQuickAdapter<FamilyBook.LineageTableBean, BaseViewHolder> {
    public LineagekdetailsAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyBook.LineageTableBean item) {
        helper.setText(R.id.name, String.format("%s%s", item.getSurname(), item.getName()))
                .setText(R.id.page, SPHelper.getStringSF(mContext, "pages"));

//        helper.setOnClickListener(R.id.lin, v -> {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("data", item);
//            Log.i(TAG, "onClick: " + item);
//        });

        helper.addOnClickListener(R.id.lin);
    }
}
