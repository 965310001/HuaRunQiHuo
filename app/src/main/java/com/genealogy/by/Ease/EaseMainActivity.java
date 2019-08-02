package com.genealogy.by.Ease;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.Ease.model.dao.FriendTable;
import com.genealogy.by.Ease.model.dao.UserTable;
import com.genealogy.by.Ease.util.EaseCommonUtils;
import com.genealogy.by.Ease.util.EaseSmileUtils;
import com.genealogy.by.R;
import com.genealogy.by.utils.SPHelper;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.manager.GlideManager;

public class EaseMainActivity extends BaseTitleActivity {

    private final static int MSG_REFRESH = 2;

    private RecyclerView mRecyclerView;
    private List<EMConversation> conversationList;
    private EaseConversationAdapter mAdapter;

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
        mRecyclerView = findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new EaseConversationAdapter(R.layout.ease_row_chat_history);
        mRecyclerView.setAdapter(mAdapter);
        conversationList = loadConversationList();
        mAdapter.setNewData(conversationList);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            EMConversation bean = (EMConversation) adapter.getData().get(position);
            try {
                Log.i(TAG, "onItemChildClick: " + bean.conversationId());

                EMMessage message = bean.getLastMessage();

                Intent intent = new Intent(mContext, ChatMsgActivity2.class);
                intent.putExtra(FriendTable.FRIEND_ID, Integer.valueOf(bean.conversationId()));
                intent.putExtra(FriendTable.FRIEND_NAME, message.getStringAttribute("otherUserNickName"));
                intent.putExtra(FriendTable.FRIEND_HEAD, message.getStringAttribute("otherUserPortrait"));

                intent.putExtra(UserTable.USER_ID, Integer.valueOf(SPHelper.getStringSF(mContext, "UserId", "")));
                intent.putExtra(UserTable.USER_NAME, message.getStringAttribute("nickName"));
                intent.putExtra(UserTable.USER_HEAD, message.getStringAttribute("UserPortrait"));

                intent.putExtra(EaseConstant.EXTRA_USER_ID, String.valueOf(bean.conversationId()));

                startActivity(intent);
            } catch (Exception e) {
                Log.i(TAG, "onItemChildClick: " + e.toString());
            }

        });
    }

    protected List<EMConversation> loadConversationList() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, (con1, con2) -> {
            if (con1.first.equals(con2.first)) {
                return 0;
            } else if (con2.first.longValue() > con1.first.longValue()) {
                return 1;
            } else {
                return -1;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (SPHelper.getBooleanSF(mContext, "isChat", false)) {
            SPHelper.setBooleanSF(mContext, "isChat", false);

            refresh();
        }
    }

    private void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    mAdapter.notifyDataSetChanged();
                    break;
                }
                default:
                    break;
            }
        }
    };

    private class EaseConversationAdapter extends BaseQuickAdapter<EMConversation, BaseViewHolder> {

        public EaseConversationAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, EMConversation item) {
            try {
                EMMessage message = item.getLastMessage();
                String nickName, portrait;
                if (item.conversationId().equals(SPHelper.getStringSF(mContext, "UserId", ""))) {
                    portrait = message.getStringAttribute("UserPortrait");
                    nickName = message.getStringAttribute("nickName");
                } else {
                    portrait = message.getStringAttribute("otherUserPortrait");
                    nickName = message.getStringAttribute("otherUserNickName");
                }
                helper.setText(R.id.name, nickName);
                GlideManager.loadImg(portrait, helper.getView(R.id.avatar));
                helper.setText(R.id.message, EaseSmileUtils.getSmiledText(mContext,
                        EaseCommonUtils.getMessageDigest(item.getLastMessage(), mContext)));
                helper.addOnClickListener(R.id.list_itease_layout);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
    }

}