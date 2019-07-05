package com.genealogy.by.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.genealogy.by.R;
import com.genealogy.by.activity.JournalActivity;
import com.genealogy.by.activity.PerfectingInformationActivity;
import com.genealogy.by.activity.PersonalHomePageActivity;
import com.genealogy.by.activity.RegisterActivity;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

public class TabWoDeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView ivUser;
    private TextView tvName;
    private TextView tvMyFollow;
    private TextView tvMyFabulous;
    private TextView tvRelease;
    private TextView Journal;
    private TextView tvMessage;
    private TextView tvUserInfo;
    private TextView tvSetting;
    private TextView personal;
    private TextView quit;

    public static TabWoDeFragment newInstance() {
        Bundle args = new Bundle();

        TabWoDeFragment fragment = new TabWoDeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab_wode;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ivUser = mContentView.findViewById(R.id.iv_user);
        tvName = mContentView.findViewById(R.id.tv_name);
        tvName.setText("不在i");
        tvMyFollow = mContentView.findViewById(R.id.tv_my_follow);
        tvMyFabulous = mContentView.findViewById(R.id.tv_my_fabulous);
        tvRelease = mContentView.findViewById(R.id.tv_release);
        Journal = mContentView.findViewById(R.id.journal);
        tvMessage = mContentView.findViewById(R.id.tv_message);
        tvUserInfo = mContentView.findViewById(R.id.tv_user_info);
        tvSetting = mContentView.findViewById(R.id.tv_setting);
        personal = mContentView.findViewById(R.id.personal);
        quit = mContentView.findViewById(R.id.quit);

        mContentView.findViewById(R.id.lin_user_info).setOnClickListener(this);
        personal.setOnClickListener(this::onClick);
        quit.setOnClickListener(this::onClick);
        tvMyFollow.setOnClickListener(this::onClick);
        tvMyFabulous.setOnClickListener(this::onClick);
        tvRelease.setOnClickListener(this::onClick);
        Journal.setOnClickListener(this::onClick);
        tvUserInfo.setOnClickListener(this::onClick);
        tvSetting.setOnClickListener(this::onClick);
        tvMessage.setOnClickListener(this::onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_user_info:
            case R.id.tv_user_info:
                    FastUtil.startActivity(mContext, PerfectingInformationActivity.class);
                break;
            case R.id.tv_my_follow:
                    FastUtil.startActivity(mContext, PerfectingInformationActivity.class);
                break;
            case R.id.tv_my_fabulous:
                    FastUtil.startActivity(mContext, PerfectingInformationActivity.class);
                break;
            case R.id.tv_release:
                    FastUtil.startActivity(mContext, PerfectingInformationActivity.class);
                break;
            case R.id.journal:
                FastUtil.startActivity(mContext, JournalActivity.class);
                break;
            case R.id.personal:
                FastUtil.startActivity(mContext, PersonalHomePageActivity.class);
                break;
            case R.id.tv_message:
                break;
            case R.id.quit:
                ToastUtil.show("退出登录");
                SPHelper.removeSF(mContext,"GId");
                SPHelper.removeSF(mContext,"Phone");
                SPHelper.removeSF(mContext,"UserId");
                FastUtil.startActivity(mContext, RegisterActivity.class);
                getActivity().finish();
                break;

        }
    }
}
