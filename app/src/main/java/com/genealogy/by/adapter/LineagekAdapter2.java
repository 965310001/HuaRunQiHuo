//package com.genealogy.by.adapter;
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.genealogy.by.R;
//import com.genealogy.by.entity.FamilyBook;
//import com.genealogy.by.utils.SPHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//// TODO: 2019/7/22 调试接口
//public class LineagekAdapter2 extends BaseQuickAdapter<String, BaseViewHolder> {
//    public LineagekAdapter2(int layoutResId) {
//        super(layoutResId);
//    }
//    int lineage =  0;
//    int indx = 1;
//    @Override
//    protected void convert(BaseViewHolder helper,String item) {
//        lineage= Integer.parseInt(item);
//        FamilyBook familyBook = SPHelper.getDeviceData(mContext,"familyBook");
//        List<FamilyBook.LineageTableBean> lineageTable = new ArrayList<>();
//        for (int i = 0; i <familyBook.getLineageTable().size() ; i++) {
//            if(familyBook.getLineageTable().get(i).getLineage()==lineage){
//                lineageTable.add(familyBook.getLineageTable().get(i));
//                indx= i;
//            }
//        }
//        TextView number = helper.getView(R.id.number);
//        TextView page = helper.getView(R.id.page);
//        int pages;
//        String str;
//        if(indx%3==0){
//            pages = indx/3;
//        }else{
//            pages = indx/3;
//        }
//        str = String.valueOf(pages+16);
//        SPHelper.setStringSF(mContext,"pages",str);
//        page.setText(str);
//        number.setText("第"+item+"世");
//        RecyclerView lineagename = helper.getView(R.id.lineagename);
//        LineagekdetailsAdapter lineagekdetailsAdapter = new LineagekdetailsAdapter(R.layout.item_lineagek_details);
//        lineagename.setLayoutManager(new LinearLayoutManager(mContext));
//        lineagename.setAdapter(lineagekdetailsAdapter);
//        lineagekdetailsAdapter.setNewData(lineageTable);
//        helper.setOnClickListener(R.id.lin, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", item);
//            }
//        });
//    }
//    void getdata(){
//
//    }
//
//}
