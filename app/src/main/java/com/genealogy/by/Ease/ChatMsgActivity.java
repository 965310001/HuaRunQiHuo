package com.genealogy.by.Ease;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.Ease.Logs.Logs;
import com.genealogy.by.Ease.adapter.ChatMsgListAdapter;
import com.genealogy.by.Ease.model.Model;
import com.genealogy.by.Ease.model.bean.ChatInfo;
import com.genealogy.by.Ease.model.dao.ChatTable;
import com.genealogy.by.Ease.model.dao.FriendTable;
import com.genealogy.by.Ease.model.dao.UserTable;
import com.genealogy.by.Ease.util.DensityUtil;
import com.genealogy.by.Ease.util.EaseCommonUtils;
import com.genealogy.by.R;
import com.genealogy.by.utils.SPHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.genealogy.by.Ease.model.dao.ChatTable.CHAT_MSG_TYPE_SEND;

public class ChatMsgActivity extends ListActivity {

    private EditText etSendContent;     // 用户想要发送内容
    private Button btnSendMsg;      // 发送消息按钮
    private int btnSendMsgFlag;     // 判断发送消息按钮状态的Flag

    private ListView chatMsgListView;       // 聊天界面聊天记录列表试图
    private ChatMsgListAdapter chatMsgListAdapter;  // 聊天记录列表适配器
    private ChatInfo chatInfo;      // 聊天对象

    private int userId, friendId;       // 用户和好友的Id

    public static int BTN_SEND_STATU = 1;   // 发送按钮处于发送状态
    public static int BTN_ADD_STATU = 0;    // 发送按钮处于更多功能状态

    private final String MESSAGE_CHANGE = "com.zhbit.lw.MESSAGE_CHANGE"; //信号
    private LocalBroadcastManager localBroadcastManager; //广播管理者对象
    private BroadcastReceiver MESSAGE_CHANGE_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logs.d("MESSAGE_RECEIVER", " 接收到信息:" + intent.getIntExtra(FriendTable.FRIEND_ID, 0)
                    + " Message: " + intent.getStringExtra(ChatTable.CHAT_MSG_CONTENT)
                    + " Time: " + intent.getStringExtra(ChatTable.CHAT_MSG_TIME));
            if (friendId == intent.getIntExtra(FriendTable.FRIEND_ID, 0)) {
                List<Map<String, Object>> chatListData = chatInfo.getChatMsgData();
                Map<String, Object> map = new HashMap<>();
                map.put(ChatTable.CHAT_MSG_CONTENT, intent.getStringExtra(ChatTable.CHAT_MSG_CONTENT));
                map.put(ChatTable.CHAT_MSG_TIME, intent.getStringExtra(ChatTable.CHAT_MSG_TIME));
                map.put(ChatTable.CHAT_MSG_TYPE, ChatTable.CHAT_MSG_TYPE_RECEIVER);
                map.put(ChatTable.SHOW_TIME_FLAG, ChatTable.HIDE_TIME);
                chatListData.add(map);
                //更新信息
                chatInfo.setChatMsgData(chatListData);
                chatMsgListAdapter.notifyDataSetChanged();
                //滚动信息
                chatMsgListView.smoothScrollToPosition(chatMsgListView.getMaxScrollAmount());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg);

        TitleBarView titleBarView = findViewById(R.id.titleBar_headFastLib);
        titleBarView.setLeftTextDrawable(tech.com.commoncore.R.mipmap.back_black);
        if (getIntent().hasExtra(FriendTable.FRIEND_NAME)) {
            titleBarView.setOnLeftTextClickListener(v -> finish()).setTitleMainText(getIntent().getStringExtra(FriendTable.FRIEND_NAME));
        }

        initView();      // 初始化视图
        initData();      // 初始化数据
        initEvent();     // 初始化点击事件

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        //注册广播
        localBroadcastManager.registerReceiver(MESSAGE_CHANGE_RECEIVER, new IntentFilter(MESSAGE_CHANGE));
    }

    // 初始化视图
    private void initView() {
        // 设置聊天信息列表
        chatMsgListView = getListView();
        chatMsgListView.setDivider(null);
        chatMsgListView.smoothScrollToPosition(chatMsgListView.getMeasuredHeightAndState());

        // 文本编辑框－发送内容
        etSendContent = findViewById(R.id.chatMsg_etSendContent);
        btnSendMsg = findViewById(R.id.chatMsg_btnSendMsg);
    }

    // 初始化数据
    private void initData() {

        // 设置ID
        userId = getIntent().getIntExtra(UserTable.USER_ID, -1);
        friendId = getIntent().getIntExtra(FriendTable.FRIEND_ID, -1);
        if (userId == -1 || friendId == -1) {
            Toast.makeText(this, "获取聊天记录失败, 请检查你的网络.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 默认发送消息按钮为更多功能的状态
        btnSendMsgFlag = BTN_ADD_STATU;

        // 实例化当前聊天对象
        chatInfo = Model.getInstance().getDbManager().getChatTableDao().getChatMsgInfo(userId, friendId);

        getConversation();

        // 设置聊天信息列表的适配器
        chatMsgListAdapter = new ChatMsgListAdapter(this, chatInfo);
        chatMsgListView.setAdapter(chatMsgListAdapter);
        //滚动信息到底层
        chatMsgListView.smoothScrollToPosition(chatMsgListView.getMaxScrollAmount());

        /*getConversation();*/
    }

    // 初始化点击事件
    private void initEvent() {
        // 设置输入框的点击监听事件, 点击后滚动聊天记录到底部
        etSendContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMsgListView.smoothScrollToPosition(chatMsgListView.getMaxScrollAmount());
            }
        });

        // 设置输入框的文本变化监听事件, 当用户输入内容后对按钮作变化
        etSendContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 判断内容改变后的值
                String content = String.valueOf(s);
                // 如果没有内容设置图标为更多
                // 5dp 10dp
                int pxFive = DensityUtil.dip2px(ChatMsgActivity.this, 5);
                int pxTen = DensityUtil.dip2px(ChatMsgActivity.this, 10);
                if (content.equals("")) {
                    btnSendMsg.getLayoutParams().width = DensityUtil.dip2px(ChatMsgActivity.this, 40);
                    btnSendMsg.getLayoutParams().height = DensityUtil.dip2px(ChatMsgActivity.this, 40);
                    ((LinearLayout.LayoutParams) btnSendMsg.getLayoutParams()).setMargins(pxFive, pxFive, pxFive, pxFive);
                    btnSendMsg.setBackgroundResource(R.mipmap.send_msg_add);
                    btnSendMsg.setText("");
                    btnSendMsgFlag = BTN_ADD_STATU;
                } else {
                    // 根据dp转px, 动态的修改按钮的布局
                    btnSendMsg.getLayoutParams().width = DensityUtil.dip2px(ChatMsgActivity.this, 45);
                    btnSendMsg.getLayoutParams().height = DensityUtil.dip2px(ChatMsgActivity.this, 30);
                    ((LinearLayout.LayoutParams) btnSendMsg.getLayoutParams()).setMargins(pxFive, pxTen, pxFive, pxTen);
                    btnSendMsg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnSendMsg.setText("发送");
                    btnSendMsgFlag = BTN_SEND_STATU;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 设置发送按钮的监听事件
        btnSendMsg.setOnClickListener(v -> {
            // 判断发送按钮的的状态
            if (btnSendMsgFlag == BTN_ADD_STATU) {

            } else {
                List<Map<String, Object>> chatListData = chatInfo.getChatMsgData();

                try {
                    // 设置的内容
                    String msgContent = etSendContent.getText().toString();
                    String currentTime;
                    int showTimeFlag;

                    // 上一跳聊天记录的时间
                    String lastMsgTime;

                    // 判断是否存在历史聊天记录
                    if (chatListData.size() == 0) {
                        // 如果不存在则默认需要显示时间, 设置成五分钟前的时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        long fiveMinsAgo = new Date().getTime() - 5 * 1000 * 60;
                        lastMsgTime = df.format(new Date(fiveMinsAgo));
                    } else {
                        // 获取上一条消息的时间
                        lastMsgTime = chatListData.get(chatListData.size() - 1).get(ChatTable.CHAT_MSG_TIME).toString();
                    }

                    // 获取当前时间
                    Date currentDate = new Date();     // 获取到当前的时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置时间显示格式
                    // 将当前时间转化成字符串格式, 以便于存入数据库
                    currentTime = sdf.format(new Date());

                    // 将上一条聊天记录的时间从字符串格式转化成Date格式
                    Date lastMsgDate = sdf.parse(lastMsgTime);
                    // 如果间隔时间大于一天, 则显示日期
                    if ((currentDate.getTime() - lastMsgDate.getTime()) / (24 * 60 * 60 * 1000) > 1) {
                        showTimeFlag = ChatTable.SHOW_DATE;
                    } else if ((currentDate.getTime() - lastMsgDate.getTime()) / (60 * 1000) > 3) {
                        // 如果间隔时间为大于三分钟, 则显示时间
                        showTimeFlag = ChatTable.SHOW_TIME;
                    } else {
                        // 如果间隔时间小于三分钟, 则隐藏时间
                        showTimeFlag = ChatTable.HIDE_TIME;
                    }
                    //发送给对方信息
                    //创建一条文本消息，msgContent为消息文字内容
                    EMMessage message = EMMessage.createTxtSendMessage(msgContent, String.valueOf(friendId));
                    Intent intent = getIntent();
                    message.setAttribute("UserPortrait", intent.getStringExtra(UserTable.USER_NAME));
                    message.setAttribute("nickName", intent.getStringExtra(UserTable.USER_NAME));

                    message.setAttribute("otherUserPortrait", intent.getStringExtra(FriendTable.FRIEND_HEAD));
                    message.setAttribute("otherUserNickName", intent.getStringExtra(FriendTable.FRIEND_NAME));

                    message.setAttribute(FriendTable.FRIEND_ID, friendId);
                    message.setAttribute(FriendTable.FRIEND_ACCOUNT, getIntent().getStringExtra(FriendTable.FRIEND_ACCOUNT));

                    //设置聊天模式
                    message.setChatType(EMMessage.ChatType.Chat);
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(message);
                    //设置发送消息的回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {

                            Logs.d("SendMessage", "发送信息成功");

                            SPHelper.setBooleanSF(ChatMsgActivity.this, "isChat", true);
                        }

                        @Override
                        public void onError(int i, String s) {
                            Logs.d("SendMessage", "发送信息失败");
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });

                    // 更新当前聊天界面的列表数据
                    Map<String, Object> map = new HashMap<>();
                    map.put(ChatTable.CHAT_MSG_CONTENT, msgContent);
                    map.put(ChatTable.CHAT_MSG_TIME, currentTime);
                    map.put(ChatTable.CHAT_MSG_TYPE, CHAT_MSG_TYPE_SEND);
                    map.put(ChatTable.SHOW_TIME_FLAG, showTimeFlag);

                    chatListData.add(map);
                    chatInfo.setChatMsgData(chatListData);
                    chatMsgListAdapter.notifyDataSetChanged();

                    // 将聊天记录插入数据库当中
                    Model.getInstance().getDbManager().getChatTableDao().insertNewChatMsg(userId, friendId, msgContent, currentTime, CHAT_MSG_TYPE_SEND, showTimeFlag);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 置空输入框
                etSendContent.setText("");
                chatMsgListView.smoothScrollToPosition(chatMsgListView.getMaxScrollAmount());
            }
        });
    }

    /**
     * 当页面加载完毕后，把信息滚动到底部
     *
     * @param hasFocus
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            //滚动信息
            chatMsgListView.smoothScrollToPosition(chatMsgListView.getMaxScrollAmount());
        }
    }

    private void getConversation() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(String.valueOf(friendId));
        //获取此会话的所有消息
        List<EMMessage> messages = conversation.getAllMessages();
        if (messages.size() > 0) {
            List<Map<String, Object>> chatListData = chatInfo.getChatMsgData();
            if (null == chatListData) {
                chatListData = new ArrayList<>();
            }
            Map<String, Object> map;
            for (EMMessage message : messages) {
                map = new HashMap<>();
                map.put(ChatTable.CHAT_MSG_CONTENT, EaseCommonUtils.getMessageDigest(message, this));
                map.put(ChatTable.CHAT_MSG_TIME, message.getMsgTime());
                map.put(ChatTable.CHAT_MSG_TYPE, CHAT_MSG_TYPE_SEND);
                map.put(ChatTable.SHOW_TIME_FLAG, ChatTable.HIDE_TIME);
                chatListData.add(map);
                Log.i(TAG, "getConversation: " + message);
            }

            chatInfo.setRecentChatData(chatListData);
            /*chatMsgListAdapter.notifyDataSetChanged();*/
        }


    }

    private static final String TAG = "ChatMsgActivity";
}
