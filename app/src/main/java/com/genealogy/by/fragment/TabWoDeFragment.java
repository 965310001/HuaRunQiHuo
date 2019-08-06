package com.genealogy.by.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.genealogy.by.Ease.EaseMainActivity;
import com.genealogy.by.R;
import com.genealogy.by.activity.JournalActivity;
import com.genealogy.by.activity.PerfectingInformationActivity;
import com.genealogy.by.activity.PersonalHomePageActivity;
import com.genealogy.by.activity.RegisterActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.DialogUtils;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

public class TabWoDeFragment extends BaseFragment implements View.OnClickListener {
    private ImageView ivUser;
    private TextView tvName, phone;

    public static TabWoDeFragment newInstance() {
        return new TabWoDeFragment();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ivUser = mContentView.findViewById(R.id.iv_user);
        tvName = mContentView.findViewById(R.id.tv_name);
        phone = mContentView.findViewById(R.id.phone);

        mContentView.findViewById(R.id.lin_user_info).setOnClickListener(this);
        mContentView.findViewById(R.id.personal).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.quit).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.tv_my_follow).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.tv_my_fabulous).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.tv_release).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.journal).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.tv_user_info).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.tv_setting).setOnClickListener(this::onClick);
        mContentView.findViewById(R.id.tv_message).setOnClickListener(this::onClick);

        initData();
    }

    private void initData() {
        if (SPHelper.getStringSF(mContext, "profilePhoto") != null) {
            GlideManager.loadCircleImg(SPHelper.getStringSF(mContext, "profilePhoto"), ivUser);
        }
        if (SPHelper.getStringSF(mContext, "nickName") != null) {
            tvName.setText(SPHelper.getStringSF(mContext, "nickName"));
        }
        if (SPHelper.getStringSF(mContext, "Phone") != null) {
            phone.setText(SPHelper.getStringSF(mContext, "Phone"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_info:
            case R.id.tv_my_follow:
            case R.id.tv_my_fabulous:
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class);
                break;
            case R.id.tv_release:
                FastUtil.startActivity(mContext, EaseMainActivity.class);
                break;
            case R.id.journal:
                FastUtil.startActivity(mContext, JournalActivity.class);
                break;
            case R.id.personal:
                FastUtil.startActivity(mContext, PersonalHomePageActivity.class);
                break;
            case R.id.tv_message:
                ToastUtil.show("该功能暂未开发，敬请期待", new ToastUtil.Builder().setGravity(Gravity.CENTER));
                break;
            case R.id.quit:
                DialogUtils.showCommonDialog(mContext, "确认退出？", (dialogInterface, i) -> {
                    showLoading();
                    EMClient.getInstance().logout(true, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            mHandler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onError(int i, String s) {
                            Log.i(TAG, "onError: " + i + s);
                            mHandler.hasMessages(2, s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                        }
                    });
                });
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideLoading();
            switch (msg.what) {
                case 0:
                    SPHelper.removeSF(mContext, "GId");
                    SPHelper.removeSF(mContext, "Phone");
                    SPHelper.removeSF(mContext, "UserId");
                    FastUtil.startActivity(mContext, RegisterActivity.class);
                    getActivity().finish();
                    break;
                case 2:
                    ToastUtil.show(msg.obj.toString(), new ToastUtil.Builder().setGravity(Gravity.CENTER));
                    break;
            }
        }
    };

    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab_wode;
    }
}