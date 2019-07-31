package com.genealogy.by.Ease;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.Ease.Logs.Logs;
import com.genealogy.by.Ease.adapter.ChatListAdapter;
import com.genealogy.by.Ease.model.Model;
import com.genealogy.by.Ease.model.bean.ChatInfo;
import com.genealogy.by.R;

import java.util.List;
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

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //注册广播
        localBroadcastManager.registerReceiver(MESSAGE_CHANGE_RECEIVER, new IntentFilter(MESSAGE_CHANGE));


        userId = Model.getInstance().getDbManager().getUserTableDao().getUserId();
        chatInfo = new ChatInfo();

        // 设置聊天列表数据
        chatListData = Model.getInstance().getDbManager().getChatTableDao().getChatList(userId);
        chatInfo.setRecentChatData(chatListData);

        // 设置列表监听器
        chatListAdapter = new ChatListAdapter(mContext, chatInfo);
        mRecyclerView = findViewById(R.id.rv_contentFastLib);
        /*mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));*/
        mRecyclerView.setAdapter(chatListAdapter);
    }

    private final String MESSAGE_CHANGE = "com.zhbit.lw.MESSAGE_CHANGE"; //信号
    private LocalBroadcastManager localBroadcastManager; //广播管理者对象
    private BroadcastReceiver MESSAGE_CHANGE_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logs.d("CHAT_FRAGMENT_CHANGE", " 聊天列表改变");
            // 更新聊天列表数据
            chatListData = Model.getInstance().getDbManager().getChatTableDao().getChatList(userId);
            chatInfo.setRecentChatData(chatListData);
            chatListAdapter.notifyDataSetChanged();
        }
    };

    private ChatInfo chatInfo;      // 聊天对象

    private List<Map<String, Object>> chatListData;     // 聊天列表数据
    private ChatListAdapter chatListAdapter;        // 聊天列表适配器
    private ListView mRecyclerView;

    private int userId;

}
