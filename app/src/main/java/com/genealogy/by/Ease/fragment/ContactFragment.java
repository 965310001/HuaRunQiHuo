//package com.genealogy.by.Ease.fragment;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.LocalBroadcastManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.genealogy.by.Ease.FriendInforActivity;
//import com.genealogy.by.Ease.Logs.Logs;
//import com.genealogy.by.Ease.adapter.ContactExpandableListAdapter;
//import com.genealogy.by.Ease.model.Model;
//import com.genealogy.by.Ease.model.bean.FriendInfo;
//import com.genealogy.by.Ease.model.dao.FriendTable;
//import com.genealogy.by.R;
//
//import java.util.List;
//import java.util.Map;
//
//import static com.genealogy.by.Ease.model.dao.FriendTable.FRIEND_ID;
//import static com.genealogy.by.Ease.model.dao.FriendTable.USER_ID;
//
//
///**
// * ContactFragment: 好友列表界面
// * Created by wjh on 17-5-6.
// */
//
//public class ContactFragment extends Fragment implements View.OnClickListener{
//
//    private View view;      // 当前fragment的视图
//    private View headerView;    // expandableListView的表头
//    private ExpandableListView contactExpandableListView;     // 通讯录扩展列表
//    private ContactExpandableListAdapter contactExpandableListAdapter;
//
//    private ImageView imgNewFriend;     // exoandableListView表头当中的新朋友
//    private ImageView imgGroupChat;     // exoandableListView表头当中的群聊
//    private ImageView imgRelation;      // exoandableListView表头当中的关系
//
//    private List<String> parentList;
//    private List<List<Map<String, Object>>> childList;
//
//    private int userId;
//
//
//    private final String NEW_FRIEND_INVITATION = "com.zhbit.lw.NEW_FRIEND_INVITATION";  //广播信号
//    private LocalBroadcastManager localBroadcastManager; //广播管理者对象
//    private BroadcastReceiver NEW_FRIEND_INVITATION_RECEIVER = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, final Intent intent) {
//
//            if (intent.getIntExtra("type",0) == 0){
//                Logs.d("NEW_FRIEND_INVITATION", " 更新好友列表");
//                //更新好友列表
//                parentList = Model.getInstance().getDbManager().getFriendTableDao().getGroupList();
//                childList = Model.getInstance().getDbManager().getFriendTableDao().getGrouopChildList(parentList);
//                contactExpandableListAdapter.notifyDataSetChanged();
//            }
//        }
//    };
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.fragment_contact, container, false);
//        }
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        initView();        // 初始化视图
//        initData();        // 初始化数据
//        initEvent();       // 初始化点击事件
//
//        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
//        //注册广播
//        localBroadcastManager.registerReceiver(NEW_FRIEND_INVITATION_RECEIVER, new IntentFilter(NEW_FRIEND_INVITATION));
//    }
//
//    // 初始化视图
//    private void initView() {
//        // 通讯录分组列表
//        contactExpandableListView = (ExpandableListView) view.findViewById(R.id.contactExpandableListView);
//        // 设置分组的指示图标
////        contactExpandableListView.setGroupIndicator(null);
//
//        // 设置通讯录列表的表头
//        headerView = View.inflate(getActivity(), R.layout.expandlistview_header, null);
//        contactExpandableListView.addHeaderView(headerView, null, true);
//        contactExpandableListView.smoothScrollToPosition(contactExpandableListView.getMaxScrollAmount());
//
//        // 通讯录列表表头当中的三个图片
//        imgNewFriend = (ImageView) headerView.findViewById(R.id.expandLvHeader_new_frined);
//        imgGroupChat = (ImageView) headerView.findViewById(R.id.expandLvHeader_group_chat);
//        imgRelation = (ImageView) headerView.findViewById(R.id.expandLvHeader_relation);
//
//    }
//
//    // 初始化数据
//    private void initData() {
//
//        // 设置父列表和子列表的数据
//        parentList = Model.getInstance().getDbManager().getFriendTableDao().getGroupList();
//        childList = Model.getInstance().getDbManager().getFriendTableDao().getGrouopChildList(parentList);
//        if (parentList == null) {
//            Toast.makeText(getActivity(), "Parent Null", Toast.LENGTH_SHORT).show();
//        }
//        if (childList == null) {
//            Toast.makeText(getActivity(), "Child Null", Toast.LENGTH_SHORT).show();
//        }
////        parentList = Model.getInstance().getDbManager().getFriendTableDao().getGroupList();
////        childList = Model.getInstance().getDbManager().getFriendTableDao().getGrouopChildList(parentList);
//
//        // 获取用户ID
//        userId = Model.getInstance().getDbManager().getUserTableDao().getUserId();
//        contactExpandableListAdapter= new ContactExpandableListAdapter(getActivity(), parentList, childList);
//        // 设置通讯录扩展列表的适配器
//        contactExpandableListView.setAdapter(contactExpandableListAdapter);
//
//    }
//
//    // 初始化点击事件
//    private void initEvent() {
//
//        // 设置通讯录列表表头三个图片的点击事件
//        imgNewFriend.setOnClickListener(this);
//        imgGroupChat.setOnClickListener(this);
//        imgRelation.setOnClickListener(this);
//
//        // 通讯录扩展列表的点击事件
//        contactExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                // 如果为表头则返回
//                if (position == 0) {
//                    return false;
//                }
//                // 如果contact_name的Tag为-1, 代表其为通讯录列表
//                int parentPosition = (int) view.getTag(R.id.expandLvRow_group_name);
//                int childPosition = (int) view.getTag(R.id.expandLvRow_contact_name);
//                if (childPosition == -1) {
//                    Toast.makeText(getActivity(), "I am group. " + parentList.get(parentPosition), Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getActivity(), "I am child. " + childList.get(parentPosition).get(childPosition), Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        });
//
//        // 设置通讯录列表的点击事件
//        contactExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                // 跳转到用户信息的界面
//                Intent intent = new Intent(getActivity(), FriendInforActivity.class);
//                FriendInfo friendInfo = Model.getInstance().getDbManager().getFriendTableDao().getFriendInfoById(Integer.parseInt(childList.get(groupPosition).get(childPosition).get(FRIEND_ID).toString()));
//                intent.putExtra(FriendTable.FRIEND_NAME,friendInfo.getFriendName());
//                intent.putExtra(FriendTable.FRIEND_ACCOUNT,friendInfo.getFriendAccount());
//                intent.putExtra(FriendTable.FRIEND_SEX,friendInfo.getFriendSex());
//                intent.putExtra(FriendTable.FRIEND_HEAD,friendInfo.getFriendHead());
//                intent.putExtra(FRIEND_ID,friendInfo.getFriendId());
//                intent.putExtra(USER_ID, userId);
//                startActivity(intent);
//                return true;
//            }
//        });
//
//    }
//
//    // Fragment摧毁视图的生命周期
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        // 当view被销毁时去掉headView
//        contactExpandableListView.removeHeaderView(headerView);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.expandLvHeader_new_frined:
////                Intent intent = new Intent(getActivity(), NewFriendActivity.class);
////                intent.putExtra(USER_ID, userId);
////                startActivity(intent);
//                break;
//            case R.id.expandLvHeader_group_chat:
//                Toast.makeText(getActivity(), "Group Chat", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.expandLvHeader_relation:
//                Toast.makeText(getActivity(), "Relation", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//
//}
