package com.genealogy.by.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.DeedsAdapter;
import com.genealogy.by.db.User;
import com.genealogy.by.entity.PersonalHome;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.genealogy.by.view.dialog.BottomDialog;
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
    private AppCompatImageView ivImg;
    private TextView tvName;
    private TextView Relationship;
    private TextView Area;
    private TextView Telephone;
    private TextView Birthday;
    private TextView mTvRanking;
    private String id;
    private RecyclerView rvDeeds;
    private DeedsAdapter deedAdapter;
    private BottomDialog mBottomDialog;

    private PersonalHome mPerson;

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
        Log.i(TAG, "initView: " + id);
        if (TextUtils.isEmpty(id)) {
            Log.i(TAG, "initView: " + id);
            id = SPHelper.getStringSF(mContext, "UserId");
        }
        Log.i(TAG, "initView: " + id);
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
                            mPerson = data.data;
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
        mTvRanking.setText(String.valueOf(data.getRanking()));/*排行*/
        GlideManager.loadCircleImg(data.getProfilePhoto(), ivImg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBottomDialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_deeds:// TODO: 2019/7/23 添加事迹 
                Log.i(TAG, "onClick: 添加事迹");
                break;
            default:
                Log.i(TAG, "onClick: " + id);
//                if (null == mBottomDialog) {
                mBottomDialog = new BottomDialog();
                Log.i(TAG, "onClick: " + id);
                if (!TextUtils.isEmpty(id) && id.equals(SPHelper.getStringSF(mContext, "UserId"))) {
                    mBottomDialog.setGone(R.id.tv_delete, R.id.tv_delete);
                    Log.i(TAG, "onClick: tv_delete");
                }
                mBottomDialog.setOnClick(view -> {
                    switch (view.getId()) {
                        case R.id.t_translation:/*编辑信息*/
                            User mUser = null;
                            if (null != mPerson) {
                                mUser = new User();
                                mUser.setPhone(mPerson.getPhone());
                                mUser.setNumber(mPerson.getNumber());
                                mUser.setGid(String.valueOf(mPerson.getGId()));
                                mUser.setBirthday(mPerson.getBirthday());
                                mUser.setEmail(mPerson.getEmail());
                                mUser.setUserid(String.valueOf(mPerson.getId()));
                                mUser.setNoun(mPerson.getNoun());
                                mUser.setRelationship(mPerson.getRelationship());
                                mUser.setIsCelebrity(Integer.valueOf(mPerson.getIsCelebrity()));
                                mUser.setWord(mPerson.getWord());
                                mUser.setWordGeneration(mPerson.getWordGeneration());
                                mUser.setSchool(mPerson.getSchool());
                                mUser.setPosition(mPerson.getPosition());
                            }
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("title", "编辑信息");
                            bundle.putSerializable("user", mUser);
                            FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                            break;
                        case R.id.tv_relational:
                            /*SearchNearInBlood data = mPerson;*/
                            bundle = new Bundle();
                            bundle.putSerializable("data", mPerson);
                            FastUtil.startActivity(mContext, RelationshipChainActivity.class, bundle);
                            break;

                        case R.id.tv_delete:
                            deleteUser();
                            break;
                    }
                    mBottomDialog.dismiss();
                });
//                }
                mBottomDialog.show(getSupportFragmentManager(), "PERSONALITY");
                break;
        }
    }

    private void deleteUser() {
        showLoading();
        ViseHttp.GET(ApiConstant.delUser)
                .baseUrl(ApiConstant.BASE_URL_ZP)
                .addParam("id", id)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        ToastUtil.show(data.msg);
                        if (data.isSuccess()) {
                            finish();
                        }
                        hideLoading();
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("请求失败");
                        hideLoading();
                    }
                });
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