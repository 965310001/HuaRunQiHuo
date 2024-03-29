package com.genealogy.by.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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

/**
 * 个人详情页
 */
public class PersonalHomePageActivity extends BaseTitleActivity implements View.OnClickListener {

    private AppCompatImageView ivImg;
    private TextView tvName, Relationship, Area, Telephone, Birthday, mTvRanking;
    private String id;
    private RecyclerView rvDeeds;
    private DeedsAdapter mAdapter;
    private BottomDialog mBottomDialog;
    private PersonalHome mPerson;

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
        mAdapter = new DeedsAdapter(R.layout.item_personalhomepage);
        rvDeeds.setAdapter(mAdapter);

        findViewById(R.id.iv_back).setOnClickListener(this::onClick);
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
                            mAdapter.setNewData(data.data.getDeeds());
                            hideLoading();
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show(errMsg);
                            Log.e(TAG, "onFail: " + errMsg + ";errCode=" + errCode);
                            hideLoading();
                        }
                    });
        } else {
            ToastUtil.show("请重新登录", new ToastUtil.Builder().setGravity(Gravity.CENTER));
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
    protected void onResume() {
        super.onResume();
        if (SPHelper.getBooleanSF(mContext, "isRefresh", false)) {
            loadComment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBottomDialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_deeds:// TODO: 2019/7/23 添加事迹 
                Log.i(TAG, "onClick: 添加事迹");
                break;
            case R.id.iv_invite:/*邀请*/
                doInvite();
                break;
            default:
                if (null == mBottomDialog) {
                    mBottomDialog = new BottomDialog();
                    if (!TextUtils.isEmpty(id) && id.equals(SPHelper.getStringSF(mContext, "UserId"))) {
                        mBottomDialog.setGone("DELETE");
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
                                    mUser.setMinName(mPerson.getMinName());
                                    mUser.setUsedName(mPerson.getUsedName());
                                    mUser.setDesignation(mPerson.getDesignation());
                                    mUser.setName(mPerson.getName());
                                    mUser.setSurname(mPerson.getSurname());
                                    mUser.setProfilePhoto(mPerson.getProfilePhoto());
                                    mUser.setSex(mPerson.getSex());
                                    mUser.setNationality(mPerson.getNationality());
                                    mUser.setRanking(mPerson.getRanking());
                                    mUser.setRemark(mPerson.getRemark());
                                    mUser.setIdCard(mPerson.getIdCard());
                                    mUser.setCommonName(mPerson.getCommonName());
                                    mUser.setEducation(mPerson.getEducation());
                                    mUser.setMoveOut(mPerson.getMoveOut());
                                    mUser.setBloodGroup(mPerson.getBloodGroup());
                                    mUser.setHeight(mPerson.getHeight());
                                    mUser.setBirthArea(mPerson.getBirthArea());
                                    mUser.setBirthPlace(mPerson.getBirthPlace());
                                    mUser.setHealth(mPerson.getHealth());
                                    mUser.setEducation(mPerson.getEducation());
                                    mUser.setYearOfLife(mPerson.getYearOfLife());
                                    mUser.setDieAddress(mPerson.getDieAddress());
                                    mUser.setBuriedArea(mPerson.getBuriedArea());
                                    mUser.setDeathPlace(mPerson.getDeathPlace());
                                    mUser.setUnit(mPerson.getUnit());
                                    mUser.setCurrentResidence(mPerson.getCurrentResidence());
                                    mUser.setAncestralHome(mPerson.getAncestralHome());
                                    mUser.setIndustry(mPerson.getIndustry());
                                    mUser.setDeathTime(mPerson.getDeathTime());
                                    mUser.setGeneticDisease(mPerson.getGeneticDisease());
                                    mUser.setUrl(mPerson.getUrl());
                                    mUser.setMark(mPerson.getMark());
                                }
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("title", "编辑信息");
                                bundle.putSerializable("user", mUser);
                                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                                break;
                            case R.id.tv_relational:
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
                }
                mBottomDialog.show(getSupportFragmentManager(), "PERSONALITY");
                break;
        }
    }

    private void doInvite() {
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
                loadComment();
            }
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_see_details;
    }
}