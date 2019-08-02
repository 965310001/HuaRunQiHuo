//package com.genealogy.by.Ease;
//
//import android.support.v4.app.Fragment;
//
//import com.genealogy.by.R;
//import com.hyphenate.EMMessageListener;
//import com.hyphenate.chat.EMCmdMessageBody;
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.util.EMLog;
//
//import java.util.List;
//
//public class EaseChatFragment extends Fragment implements EMMessageListener {
//
//    @Override
//    public void onMessageReceived(List<EMMessage> messages) {
//        for (EMMessage message : messages) {
//            String username = null;
//            // group message
//            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
//                username = message.getTo();
//            } else {
//                // single chat message
//                username = message.getFrom();
//            }
//
//            // if the message is for current conversation
//            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
//                messageList.refreshSelectLast();
//                conversation.markMessageAsRead(message.getMsgId());
//            }
//            EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
//        }
//    }
//
//    @Override
//    public void onCmdMessageReceived(List<EMMessage> messages) {
//        for (final EMMessage msg : messages) {
//            final EMCmdMessageBody body = (EMCmdMessageBody) msg.getBody();
//            EMLog.i(TAG, "Receive cmd message: " + body.action() + " - " + body.isDeliverOnlineOnly());
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (ACTION_TYPING_BEGIN.equals(body.action()) && msg.getFrom().equals(toChatUsername)) {
//                        titleBar.setTitle(getString(R.string.alert_during_typing));
//                    } else if (ACTION_TYPING_END.equals(body.action()) && msg.getFrom().equals(toChatUsername)) {
//                        titleBar.setTitle(toChatUsername);
//                    }
//                }
//            });
//        }
//    }
//
//    private static final String TAG = "EaseChatFragment";
//
//    @Override
//    public void onMessageRead(List<EMMessage> messages) {
//        if(isMessageListInited) {
//            messageList.refresh();
//        }
//    }
//
//    @Override
//    public void onMessageDelivered(List<EMMessage> messages) {
//        if(isMessageListInited) {
//            messageList.refresh();
//        }
//    }
//
//    @Override
//    public void onMessageRecalled(List<EMMessage> messages) {
//        if(isMessageListInited) {
//            messageList.refresh();
//        }
//    }
//
//
//    @Override
//    public void onMessageChanged(EMMessage emMessage, Object change) {
//        if(isMessageListInited) {
//            messageList.refresh();
//        }
//    }
//}
