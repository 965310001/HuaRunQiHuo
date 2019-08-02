package com.genealogy.by.Ease;

import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.Ease.model.dao.FriendTable;
import com.genealogy.by.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

import tech.com.commoncore.base.BaseTitleActivity;

public class ChatMsgActivity2 extends BaseTitleActivity {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText(getIntent().getStringExtra(FriendTable.FRIEND_NAME));
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_chat_msg;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        args.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        chatFragment.setArguments(args);
        chatFragment.hideTitleBar();
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }
}
