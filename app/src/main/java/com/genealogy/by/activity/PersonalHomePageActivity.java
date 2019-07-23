package com.genealogy.by.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.DeedsAdapter;
import com.genealogy.by.entity.PersonalHome;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.githang.statusbar.StatusBarCompat;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/23 调试接口  个人详情页
public class PersonalHomePageActivity extends BaseTitleActivity implements View.OnClickListener {
    private ImageView ivImg;
    private TextView tvName;
    private TextView Relationship;
    private TextView Area;
    private TextView Telephone;
    private TextView Birthday;
    private TextView mTvRanking;
    private String id;
    private RecyclerView rvDeeds;
    private DeedsAdapter deedAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
//        titleBar.setTitleMainText("个人详情页").setTextColor(Color.WHITE)
//                .setDividerVisible(true).setDividerHeight(1).setDividerBackgroundColor(Color.parseColor("#464854"))
//
//                .setRightTextDrawable(R.mipmap.gengduo1).setOnRightTextClickListener(this).setBackgroundColor(Color.parseColor("#464854"));
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#464854"));
        Intent intent = getIntent();
        id = intent.getStringExtra("userId");
        if (TextUtils.isEmpty(id)) {
            id = SPHelper.getStringSF(mContext, "UserId");
        }
        loadComment();
        ivImg = findViewById(R.id.portrait);
        tvName = findViewById(R.id.tv_name);
        Relationship = findViewById(R.id.tv_relationship);
        Area = findViewById(R.id.area);
        Telephone = findViewById(R.id.tv_telephone);
        Birthday = findViewById(R.id.birthday);
        mTvRanking = findViewById(R.id.tv_ranking);
        rvDeeds = findViewById(R.id.deeds);
        rvDeeds.setLayoutManager(new LinearLayoutManager(mContext));
        deedAdapter = new DeedsAdapter(R.layout.item_personalhomepage);
        rvDeeds.setAdapter(deedAdapter);

        findViewById(R.id.iv_back).setOnClickListener(view -> finish());
    }

    private void loadComment() {
        if (!TextUtils.isEmpty(id)) {
            ViseHttp.GET(ApiConstant.searchUserDeatil)
                    .baseUrl(ApiConstant.BASE_URL_ZP)
                    .addParam("id", id)
                    .request(new ACallback<BaseTResp2<PersonalHome>>() {
                        @Override
                        public void onSuccess(BaseTResp2<PersonalHome> data) {
                            setUser(data.data);
                            deedAdapter.setNewData(data.data.getDeeds());
                            hideLoading();
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show("请求失败");
                            Log.e(TAG, "onFail: " + errMsg + ";errCode=" + errCode);
                            hideLoading();
                        }
                    });
        } else {
            ToastUtil.show("请重新登录");
            FastUtil.startActivity(mContext, RegisterActivity.class);
        }
    }

    private void setUser(PersonalHome data) {
        if (!TextUtils.isEmpty(data.getNickName())) {
            tvName.setText(data.getNickName());
        } else {
            tvName.setText("");
        }
        if (!TextUtils.isEmpty(data.getRelationship())) {
            Relationship.setText(data.getRelationship());
        } else {
            Relationship.setText("");
        }
        if (!TextUtils.isEmpty(data.getAncestralHome())) {
            Area.setText(data.getAncestralHome());
        } else {
            Area.setText("");
        }
        if (!TextUtils.isEmpty(data.getPhone())) {
            Telephone.setText(String.format("电话:%s", data.getPhone()));
        } else {
            Telephone.setText("");
        }
        if (!TextUtils.isEmpty(data.getBirthday())) {
            Birthday.setText(data.getBirthday());
        } else {
            Birthday.setText("");
        }
        mTvRanking.setText(data.getRanking());/*排行*/
        GlideManager.loadCircleImg(data.getProfilePhoto(), ivImg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_deeds:// TODO: 2019/7/23 添加事迹 
                Log.i(TAG, "onClick: 添加事迹");
                break;
            default:
                // TODO: 2019/7/23 popwindow 弹窗的点击事件
                Log.i(TAG, "onClick: popwindow 弹窗的点击事件");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                // TODO: 2019/7/23 编辑信息 添加事件以后的回调 是否更新数据
                loadComment();
            }
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_see_details;
    }


}