package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.DeedsAdapter;
import com.genealogy.by.entity.Deed;
import com.genealogy.by.entity.PersonalHome;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

public class PersonalHomePageActivity extends BaseTitleActivity {
    private ImageView ivImg;
    private TextView tvName;
    private ImageView Invitation;
    private TextView Relationship;
    private TextView Area;
    private TextView Telephone;
    private TextView Birthday;
    private TextView Age;
    private String  id = "";
    private LinearLayout adddeeds;
    private android.support.v7.widget.RecyclerView rvDeeds;
    DeedsAdapter deedAdapter;
    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("个人详情页");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_see_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        id = intent.getStringExtra("userId");
        if(id==null||id.trim().length()==0){
            id= SPHelper.getStringSF(mContext,"UserId");
        }
        loadComment();
        ivImg = findViewById(R.id.portrait);
        adddeeds = findViewById(R.id.adddeeds);
        tvName = findViewById(R.id.tv_name);
        Invitation = findViewById(R.id.invitation);
        Relationship = findViewById(R.id.tv_relationship);
        Area = findViewById(R.id.area);
        Telephone = findViewById(R.id.tv_telephone);
        Birthday = findViewById(R.id.birthday);
        Age = findViewById(R.id.age);
        rvDeeds = findViewById(R.id.deeds);
        rvDeeds.setLayoutManager(new LinearLayoutManager(mContext));
        deedAdapter= new DeedsAdapter(R.layout.item_personalhomepage);
        rvDeeds.setAdapter(deedAdapter);
        adddeeds.setOnClickListener(view -> {

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void loadComment() {
        if(id!=null&&id.trim().length()!=0){
            ViseHttp.GET(ApiConstant.searchUserDeatil)
                    .baseUrl(ApiConstant.BASE_URL_ZP)
                    .addParam("id", id)
                    .request(new ACallback<BaseTResp2<PersonalHome>>() {
                        @Override
                        public void onSuccess(BaseTResp2<PersonalHome> data) {
                            ToastUtil.show("请求成功");
                            setdata(data.data);
                            List<Deed> list = new ArrayList();
                            for (int i = 0; i < data.data.getDeeds().size(); i++) {
                                list.add((Deed) data.data.getDeeds().get(i));
                            }
                            deedAdapter.setNewData(list);
                            hideLoading();
                        }
                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show("请求失败");
                            Log.e(TAG, "onFail: "+errMsg +";errCode="+errCode);
                            hideLoading();
                        }
                    });
        }else{
            ToastUtil.show("请重新登录");
        }

    }
    void setdata(PersonalHome data){
        if (!data.getSurname().isEmpty()&&data.getName()!=null){
            tvName.setText(data.getSurname()+data.getName());
        }else{
            tvName.setText("");
        }
        if (!data.getRelationship().isEmpty()&&data.getRelationship()!=null){
            Relationship.setText(data.getRelationship());
        }else{
            Relationship.setText("");
        }
        if (!data.getAncestralHome().isEmpty()&&data.getAncestralHome()!=null){
            Area.setText(data.getAncestralHome());
        }else{
            Area.setText("");
        }
        if (!data.getPhone().isEmpty()&&data.getAncestralHome()!=null){
            Telephone.setText(data.getPhone());
        }else{
            Telephone.setText("");
        }
        if (!data.getBirthday().isEmpty()&&data.getBirthday()!=null){
            Birthday.setText(data.getBirthday());
        }else{
            Birthday.setText("");
        }
        Age.setText("");
    }
}
