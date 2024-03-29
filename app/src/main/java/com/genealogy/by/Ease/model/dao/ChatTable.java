package com.genealogy.by.Ease.model.dao;

/**
 * Created by wjh on 17-5-15.
 */

public class ChatTable {

    public static final String TABLE_NAME = "chat_msg";

    public static final String USER_ID = "user_id";
    public static final String FRIEND_ID = "friend_id";
    public static final String CHAT_MSG_CONTENT = "chat_msg_content";
    public static final String CHAT_MSG_TIME = "chat_msg_time";
    public static final String CHAT_MSG_TYPE = "chat_msg_type";

    public static final String CHAT_MSG_TYPE_RECEIVER = "receive";
    public static final String CHAT_MSG_TYPE_SEND = "send";

    public static final String TARGET_EXPAND_RELATION_SWEET = "sweet";
    public static final String TARGET_EXPAND_RELATION_WORK = "work";
    public static final String TARGET_EXPAND_RELATION_FAMILY = "family";

    public static final String SHOW_TIME_FLAG = "show_time_flag";
    public static final int HIDE_TIME = 0;
    public static final int SHOW_TIME = 1;
    public static final int SHOW_DATE = 2;

    // ChatFragment需要的信息

    public static final String CREATE_TABLE = "create table "
            + TABLE_NAME + " ("
            + "[_id] integer autoinc primary key,"
            + USER_ID + " integer,"
            + FRIEND_ID + " integer,"
            + SHOW_TIME_FLAG + " integer,"
            + CHAT_MSG_CONTENT + " text,"
            + CHAT_MSG_TIME + " datetime,"
            + CHAT_MSG_TYPE + " varchar(10))";

}
