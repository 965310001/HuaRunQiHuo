package com.genealogy.by.Ease;

import android.os.Bundle;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.Ease.model.dao.FriendTable;
import com.genealogy.by.R;
import com.genealogy.by.utils.SPHelper;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import tech.com.commoncore.base.BaseTitleActivity;

public class ChatMsgActivity2 extends BaseTitleActivity {

    @Override
    public void initView(Bundle savedInstanceState) {
        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        args.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        chatFragment.setArguments(args);
        chatFragment.hideTitleBar();
        chatFragment.setChatFragmentHelper(mHelper);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    private EaseChatFragment.EaseChatFragmentHelper mHelper = new EaseChatFragment.EaseChatFragmentHelper() {
        @Override
        public void onSetMessageAttributes(EMMessage message) {
            message.setAttribute("UserPortrait", SPHelper.getStringSF(mContext, "profilePhoto"));
            message.setAttribute("nickName", SPHelper.getStringSF(mContext, "nickName"));

            message.setAttribute("otherUserPortrait", getIntent().getStringExtra(FriendTable.FRIEND_HEAD));
            message.setAttribute("otherUserNickName", getIntent().getStringExtra(FriendTable.FRIEND_NAME));
        }

        @Override
        public void onEnterToChatDetails() {
        }

        @Override
        public void onAvatarClick(String username) {
        }

        @Override
        public void onAvatarLongClick(String username) {
        }

        @Override
        public boolean onMessageBubbleClick(EMMessage message) {
            return false;
        }

        @Override
        public void onMessageBubbleLongClick(EMMessage message) {
        }

        @Override
        public boolean onExtendMenuItemClick(int itemId, View view) {
            return false;
        }

        @Override
        public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
            return null;
        }
    };


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText(getIntent().getStringExtra(FriendTable.FRIEND_NAME));
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_chat_msg;
    }


}
