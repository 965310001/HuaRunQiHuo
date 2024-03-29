//package com.genealogy.by.Ease.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.genealogy.by.Ease.SystemSetting;
//import com.genealogy.by.Ease.UserInforActivity;
//import com.genealogy.by.Ease.model.Model;
//import com.genealogy.by.Ease.model.bean.UserInfo;
//import com.genealogy.by.Ease.model.dao.UserTable;
//import com.genealogy.by.R;
//
///**
// * Created by wjh on 17-5-6.
// */
//
//public class MeFragment extends Fragment {
//
//    private View view;
//    // 用户基本信息的界面
//    private ImageView ivUserHead;
//    private TextView tvUserName, tvUserAccount;
//    private LinearLayout layoutUserInfor;
//    private LinearLayout laySetting;
//
//    private int userId;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_me, container, false);
//        return view;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        initView();
//        initData();
//        initEvent();
//
//    }
//
//    private void initView() {
//        // 用户的基本信息
//        ivUserHead = (ImageView) view.findViewById(R.id.userInfor_userHead);
//        tvUserName = (TextView) view.findViewById(R.id.userInfor_userName);
//        tvUserAccount = (TextView) view.findViewById(R.id.userInfor_userAccount);
//        laySetting = (LinearLayout) view.findViewById(R.id.lay_setting);
//        layoutUserInfor = (LinearLayout) view.findViewById(R.id.lineLayoutEditMyInfo);
//    }
//
//    private void initData() {
//        // 获取用户ID
//        userId = Model.getInstance().getDbManager().getUserTableDao().getUserId();
//        UserInfo userInfo = Model.getInstance().getDbManager().getUserTableDao().getUserInfor();
//        if (userInfo == null) {
//            Toast.makeText(getActivity(), "请检查你的网络链接.", Toast.LENGTH_SHORT).show();
//        }else{
//            tvUserName.setText(userInfo.getUserName());
//            tvUserAccount.setText("帐号：" + userInfo.getUserAccount());
//        }
//
//
//    }
//
//    private void initEvent() {
//        // 个人详细信息的点击事件
//        layoutUserInfor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UserInforActivity.class);
//                intent.putExtra(UserTable.USER_ID, 1);
//                startActivity(intent);
//            }
//        });
//        // 设置按钮的监听事件
//        laySetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SystemSetting.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//}
