package com.genealogy.by.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.utils.SPHelper;

import java.util.ArrayList;
import java.util.List;

public class LineagekAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private OnPageItemClick onPageItemClick;

    int lineage = 0;
    int indx = 1;

    int index = 0;

    public LineagekAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        lineage = Integer.parseInt(item);
        FamilyBook familyBook = SPHelper.getDeviceData(mContext, "familyBook");
        /****************************************************************************/
        List<FamilyBook.LineageTableBean> lineageTable1 = SPHelper.getDeviceData(mContext, "LineageTable");
        lineage = lineageTable1.get(index).getLineage();
        List<FamilyBook.LineageTableBean> lineageTable = new ArrayList<>();
        for (int i = index; i < lineageTable1.size(); i++) {
            FamilyBook.LineageTableBean bean = lineageTable1.get(index);
            if (bean.getLineage() == lineage) {
                lineageTable.add(familyBook.getLineageTable().get(i));
                index++;
                indx = i;

                Log.i(TAG, "convert: " + index + "==" + indx);
            } else {
                break;
            }
        }
        /****************************************************************************/

//        /*List<FamilyBook.LineageTableBean> lineageTable = new ArrayList<>();*/
////        for (int i = 0; i < familyBook.getLineageTable().size(); i++) {
////            if (familyBook.getLineageTable().get(i).getLineage() == lineage) {
////                lineageTable.add(familyBook.getLineageTable().get(i));
////                indx = i;
////            }
////        }
        int pages;
        String str;
        if (indx % 3 == 0) {
            pages = indx / 3;
        } else {
            pages = indx / 3;
        }

        str = String.valueOf(pages + 16);

        Log.i(TAG, "convert: " + str);
        SPHelper.setStringSF(mContext, "pages", str);

        helper.setText(R.id.number, "第" + item + "世")
                .setText(R.id.page, str);
        RecyclerView lineagename = helper.getView(R.id.lineagename);
        LineagekdetailsAdapter lineagekdetailsAdapter = new LineagekdetailsAdapter(R.layout.item_lineagek_details);
        lineagename.setLayoutManager(new LinearLayoutManager(mContext));
        lineagename.setAdapter(lineagekdetailsAdapter);
        lineagekdetailsAdapter.setNewData(lineageTable);

        helper.setOnClickListener(R.id.lin, v -> onPageItemClick.onPageItemClick(null, null, 0, str));

        lineagekdetailsAdapter
                .setOnItemChildClickListener((adapter, view, position) -> onPageItemClick.onPageItemClick(adapter, view, position, str));
    }

    public interface OnPageItemClick {
        void onPageItemClick(BaseQuickAdapter adapter, View view, int position, String page);
    }


    public void setOnPage(OnPageItemClick onPageItemClick) {
        this.onPageItemClick = onPageItemClick;
    }
}