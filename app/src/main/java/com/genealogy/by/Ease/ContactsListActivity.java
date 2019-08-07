package com.genealogy.by.Ease;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.genealogy.by.Ease.model.dao.FriendTable;
import com.genealogy.by.Ease.model.dao.UserTable;
import com.genealogy.by.R;
import com.genealogy.by.adapter.ContactsListAdapter;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.hyphenate.easeui.EaseConstant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

public class ContactsListActivity extends BaseTitleActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView rv_contentFastLib;
    private ContactsListAdapter adapter;
    private SmartRefreshLayout mRefreshLayout;

    @Override
    public void initView(Bundle savedInstanceState) {
        adapter = new ContactsListAdapter(R.layout.item_contacts);
        adapter.setOnItemChildClickListener(this);
        rv_contentFastLib = findViewById(R.id.rv_contentFastLib);

        mRefreshLayout = findViewById(R.id.smartLayout_rootFastLib);

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));//设置Header
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));//设置Footer

        mRefreshLayout.setOnRefreshListener(onRefresh);
        mRefreshLayout.setOnLoadMoreListener(onLoadMore);
        rv_contentFastLib.setLayoutManager(new LinearLayoutManager(mContext));
        rv_contentFastLib.setAdapter(adapter);
        mRefreshLayout.autoRefresh();
    }

    private OnRefreshListener onRefresh = refreshLayout -> {
        loadData();
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.finishRefresh();
    };

    private OnLoadMoreListener onLoadMore = refreshLayout -> {
        loadData();
        refreshLayout.finishLoadMore();
    };

    @Override
    public void loadData() {
        super.loadData();
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", SPHelper.getStringSF(this, "UserId", ""));
        params.put("gId", SPHelper.getStringSF(this, "GId", ""));
        ViseHttp.POST(ApiConstant.searchContactPerson)
                .setHttpCache(true).cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2<List<SearchNearInBlood>>>() {
                    @Override
                    public void onSuccess(BaseTResp2<List<SearchNearInBlood>> data) {
                        if (data.isSuccess()) {
                            List<SearchNearInBlood> bean = data.data;
                            if (null != bean && bean.size() > 0) {
                                adapter.setNewData(data.data);
                            }
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("请求失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
                    }
                });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        //        /*intent.putExtra(FriendTable.FRIEND_ID, Integer.valueOf(String.valueOf(item.getId())));
//        intent.putExtra(FriendTable.FRIEND_ACCOUNT, String.valueOf(item.getAccount()));
//        intent.putExtra(FriendTable.FRIEND_NAME, String.format("%s%s", item.getSurname(), item.getName()));
//        intent.putExtra(FriendTable.FRIEND_HEAD, item.getProfilePhoto());
//
//        intent.putExtra(UserTable.USER_ID, Integer.valueOf(SPHelper.getStringSF(this, "UserId", "")));
//        intent.putExtra(UserTable.USER_NAME, SPHelper.getStringSF(this, "nickName", ""));
//        intent.putExtra(UserTable.USER_HEAD, SPHelper.getStringSF(this, "profilePhoto", ""));*/
        SearchNearInBlood item = (SearchNearInBlood) adapter.getData().get(position);
        Intent intent = new Intent(mContext, ChatMsgActivity2.class);
        intent.putExtra(UserTable.USER_NAME, SPHelper.getStringSF(this, "nickName", ""));
        intent.putExtra(UserTable.USER_HEAD, SPHelper.getStringSF(this, "profilePhoto", ""));

        intent.putExtra(FriendTable.FRIEND_NAME, String.format("%s%s", item.getSurname(), item.getName()));
        intent.putExtra(FriendTable.FRIEND_HEAD, item.getProfilePhoto());
        intent.putExtra(FriendTable.FRIEND_ID, Integer.valueOf(String.valueOf(item.getId())));

        intent.putExtra(EaseConstant.EXTRA_USER_ID, String.valueOf(item.getId()));

        startActivity(intent);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("联系人列表");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_contacts;
    }
}