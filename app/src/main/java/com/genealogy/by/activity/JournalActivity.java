package com.genealogy.by.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.JournalAdapter;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

public class JournalActivity extends BaseTitleActivity {

    private int page = 1;
    private RecyclerView rv_contentFastLib;
    private JournalAdapter journalAdapter;
    private SmartRefreshLayout mRefreshLayout;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("日志");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_journal;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        journalAdapter = new JournalAdapter(R.layout.item_journal);
        rv_contentFastLib = findViewById(R.id.rv_contentFastLib);

        mRefreshLayout = findViewById(R.id.smartLayout_rootFastLib);
        mRefreshLayout.setOnRefreshListener(r -> {
            page = 1;
            loadLog(page);
            mRefreshLayout.setEnableLoadMore(true);
            r.finishRefresh();
        });
        mRefreshLayout.setOnLoadMoreListener(r -> {
            loadLog(++page);
            r.finishLoadMore();
        });
        rv_contentFastLib.setLayoutManager(new LinearLayoutManager(mContext));
        rv_contentFastLib.setAdapter(journalAdapter);
        mRefreshLayout.autoRefresh();
        loadLog(page);
    }

    void loadLog(int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", SPHelper.getStringSF(mContext, "GId"));
        params.put("pageNumber", String.valueOf(page));
        params.put("rows", "30");
        ViseHttp.POST(ApiConstant.log_search)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp>() {
                    @Override
                    public void onSuccess(BaseTResp data) {
                        if (data.getStatus() == 200) {
                            List<BaseTResp.DataBean> list = data.getData();
                            if (null != list && !list.isEmpty()) {
                                if (1 == page) {
                                    journalAdapter.setNewData(list);
                                } else {
                                    journalAdapter.addData(list);
                                }
                                journalAdapter.notifyDataSetChanged();
                            } else {
                                mRefreshLayout.setEnableLoadMore(false);
                            }
                        } else {
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        Log.e(TAG, "onFail: errMsg=" + errMsg);
                        ToastUtil.show(errMsg);
                    }
                });
    }
}