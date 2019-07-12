package com.genealogy.by.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;

public class LineagekAdapter extends BaseQuickAdapter<String , BaseViewHolder> {
    public LineagekAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView number = helper.getView(R.id.number);
            number.setText("第"+item+"世");

        RecyclerView lineagename = helper.getView(R.id.lineagename);
        helper.setOnClickListener(R.id.lin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", item);
            }
        });
    }
}
