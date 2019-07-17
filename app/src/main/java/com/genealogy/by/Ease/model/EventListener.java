package com.genealogy.by.Ease.model;

import android.content.Context;
import android.content.Intent;

import com.genealogy.by.Ease.Logs.Logs;
import com.genealogy.by.Ease.model.bean.FriendInfo;
import com.genealogy.by.Ease.model.bean.InvitationInfo;
import com.genealogy.by.Ease.model.dao.ChatTable;
import com.genealogy.by.Ease.model.dao.FriendTable;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by fl5900 on 2017/5/19.
 * 全局事件监听类
 */

public class EventListener {
    private Context mContext;
    private int UserId;
    private final String NEW_FRIEND_INVITATION = "com.zhbit.lw.NEW_FRIEND_INVITATION";
    private final String MESSAGE_CHANGE = "com.zhbit.lw.MESSAGE_CHANGE"; //信号
    //    private final LocalBroadcastManager localBroadcastManager; //广播管理者对象
    private final EMContactListener emContactListener = new EMContactListener() {
        @Override
        public void onContactAdded(final String s) {
            //联系人添加后执行的方法
        }

        @Override
        public void onContactDeleted(String s) {
            //联系人删除后执行的方法
        }

        @Override
        public void onContactInvited(String account, String reason) {
            //接收到联系人的新邀请
            InvitationInfo invitationInfo = new InvitationInfo();
            invitationInfo.setAccount(account);
            invitationInfo.setReason(reason);
            Model.getInstance().getDbManager().getInvitationTableDao().addInvitation(invitationInfo);
            Intent newFriendInvitedIntent = new Intent(NEW_FRIEND_INVITATION);
            newFriendInvitedIntent.putExtra("account", account);
            newFriendInvitedIntent.putExtra("reason", reason);
            newFriendInvitedIntent.putExtra("type", 1);
        }

        @Override
        public void onFriendRequestAccepted(final String username) {
        }

        @Override
        public void onFriendRequestDeclined(String s) {

        }

        EMMessageListener msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    // 获取当前时间
                    Logs.d("MESSAGE", " " + message.getFrom());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置时间显示格式
                    // 将当前时间转化成字符串格式, 以便于存入数据库
                    String currentTime = sdf.format(new Date());
                    String msgContent = ((EMTextMessageBody) message.getBody()).getMessage();
                    int showTimeFlag = ChatTable.HIDE_TIME;
                    Logs.d("RECEIVER_NAME", message.getFrom());
                    FriendInfo friendInfo = Model.getInstance().getDbManager().getFriendTableDao().getFriendInforByAccount(message.getFrom());
                    Intent addContactIntent = new Intent(MESSAGE_CHANGE);
                    //发送广播
                    addContactIntent.putExtra(ChatTable.CHAT_MSG_CONTENT, "" + msgContent);
                    addContactIntent.putExtra(ChatTable.CHAT_MSG_TIME, currentTime);
                    if (friendInfo != null) {
                        addContactIntent.putExtra(FriendTable.FRIEND_ID, friendInfo.getFriendId());
//                        localBroadcastManager.sendBroadcast(addContactIntent);
                        // 将聊天记录插入数据库当中
                        Model.getInstance().getDbManager().getChatTableDao().insertNewChatMsg(UserId, friendInfo.getFriendId(), msgContent, currentTime, ChatTable.CHAT_MSG_TYPE_RECEIVER, showTimeFlag);
                    }

                }
                //消息存储在环信数据库中
                EMClient.getInstance().chatManager().importMessages(list);
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {
                Logs.d("MESSAGE", "透传信息");
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                Logs.d("MESSAGE", "已读回执");
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                Logs.d("MESSAGE", "已送达回执");
            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {
                Logs.d("MESSAGE", "信息状态改变");
            }
        };
    };

    public EventListener(Context context) {
    }
}

