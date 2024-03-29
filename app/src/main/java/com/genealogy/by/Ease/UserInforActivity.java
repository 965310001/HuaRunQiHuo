package com.genealogy.by.Ease;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.genealogy.by.Ease.model.Model;
import com.genealogy.by.Ease.model.bean.UserInfo;
import com.genealogy.by.Ease.model.dao.UserTable;
import com.genealogy.by.Ease.ui.CustomToolbar;
import com.genealogy.by.R;

public class UserInforActivity extends AppCompatActivity {

    private int userId;
    private UserInfo userInfor;

    private TextView userName, userAccount, userLocation, userSex, userSign;
    private CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inofr);

        initView();
        initData();
        initEvent();

    }

    public void initView() {

        toolbar = (CustomToolbar) findViewById(R.id.userInfor_toolbar);
        toolbar.setTitle("详细信息");

        userName = (TextView) findViewById(R.id.userInfor_userName);
        userAccount = (TextView) findViewById(R.id.userInfor_userAccount);
        userLocation = (TextView) findViewById(R.id.userInfor_userLocation);
        userSex = (TextView) findViewById(R.id.userInfor_userSex);
        userSign = (TextView) findViewById(R.id.userInfor_userSign);
    }

    public void initData() {

        userId = getIntent().getIntExtra(UserTable.USER_ID, -1);
        userInfor = Model.getInstance().getDbManager().getUserTableDao().getUserInfor();

        userName.setText(userInfor.getUserName());
        userAccount.setText(userInfor.getUserAccount());
        userLocation.setText(userInfor.getUserLocation());
        userSex.setText(userInfor.getUserSex());
        userSign.setText(userInfor.getUserSign());

    }


    public void initEvent() {
    }

}
