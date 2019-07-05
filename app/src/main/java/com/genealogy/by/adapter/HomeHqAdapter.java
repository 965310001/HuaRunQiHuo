package com.genealogy.by.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.HqEntity;

public class HomeHqAdapter extends BaseQuickAdapter<HqEntity, BaseViewHolder> {
    public HomeHqAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HqEntity item) {
        helper.setText(R.id.tv_last, String.format("%.2f", Double.parseDouble(item.getLast()))).setText(R.id.tv_name, item.getName());
        TextView tvLast = helper.getView(R.id.tv_last);
        TextView tvChange = helper.getView(R.id.tv_change);
        TextView tvPriceChange = helper.getView(R.id.tv_price_change);
        ImageView ivImg = helper.getView(R.id.iv_img);

        if (Double.parseDouble(item.getPricechange()) >= 0) {
            tvLast.setTextColor(mContext.getResources().getColor(R.color.C_FF2A48));
            tvChange.setTextColor(mContext.getResources().getColor(R.color.C_FF2A48));
            tvPriceChange.setTextColor(mContext.getResources().getColor(R.color.C_FF2A48));
            helper.setText(R.id.tv_change, "+" + String.format("%.2f", Double.parseDouble(String.format("%.2f", Double.parseDouble(item.getPricechange()) * 100 / Double.parseDouble(item.getLast())))) + "%");
            tvPriceChange.setText("+" + String.format("%.2f", Double.parseDouble(item.getPricechange())));
            ivImg.setImageResource(R.mipmap.icon_hq_up);
        } else {
            tvLast.setTextColor(mContext.getResources().getColor(R.color.C_2DA82D));
            tvChange.setTextColor(mContext.getResources().getColor(R.color.C_2DA82D));
            tvPriceChange.setTextColor(mContext.getResources().getColor(R.color.C_2DA82D));
            helper.setText(R.id.tv_change, String.format("%.2f", Double.parseDouble(String.format("%.2f", Double.parseDouble(item.getPricechange()) * 100 / Double.parseDouble(item.getLast())))) + "%");
            tvPriceChange.setText(String.format("%.2f", Double.parseDouble(item.getPricechange())));
            ivImg.setImageResource(R.mipmap.icon_hq_down);
        }

        helper.setOnClickListener(R.id.lin, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
