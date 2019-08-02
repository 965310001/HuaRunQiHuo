package com.genealogy.by.Ease;

import android.content.Intent;
import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.Ease.model.dao.FriendTable;
import com.genealogy.by.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.Map;

import tech.com.commoncore.base.BaseTitleActivity;

public class EaseMainActivity extends BaseTitleActivity {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("私信列表").setRightTextDrawable(R.drawable.ic_lianxiren_svg)
                .setOnRightTextClickListener(v -> startActivity(new Intent(mContext, ContactsListActivity.class)));
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main1;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.hideTitleBar();
        conversationListFragment.setConversationListItemClickListener(conversation -> {
            Intent intent = new Intent(mContext, ChatMsgActivity2.class);
            try {
                Map<String, Object> map = conversation.getLastMessage().ext();
                intent.putExtra(FriendTable.FRIEND_NAME, map.get("otherUserNickName").toString());
                intent.putExtra(FriendTable.FRIEND_HEAD, map.get("otherUserPortrait").toString());

                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());

            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });

        getSupportFragmentManager().beginTransaction().add(R.id.container, conversationListFragment).commit();
    }
}