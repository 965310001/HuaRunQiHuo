package com.genealogy.by.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.JournalAdapter;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp;
import com.genealogy.by.utils.my.BaseTResp2;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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

/**
 * 日志
 */
public class JournalActivity extends BaseTitleActivity implements OnRefreshListener, OnLoadMoreListener {

    private int page = 1;
    private RecyclerView mRecyclerView;
    private JournalAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;

    @Override
    public void initView(Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.rv_contentFastLib);
        mRefreshLayout = findViewById(R.id.smartLayout_rootFastLib);

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));//设置Header
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));//设置Footer

        mRefreshLayout.setOnRefreshListener(this::onRefresh);
        mRefreshLayout.setOnLoadMoreListener(this::onLoadMore);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new JournalAdapter(R.layout.item_journal);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        loadLog(page = 1);
        refreshLayout.finishRefresh();
        refreshLayout.setEnableLoadMore(true);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        loadLog(++page);
        refreshLayout.finishLoadMore();
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
                .request(new ACallback<BaseTResp2<List<BaseTResp.DataBean>>>() {
                    @Override
                    public void onSuccess(BaseTResp2<List<BaseTResp.DataBean>> data) {
                        if (data.isSuccess()) {
                            List<BaseTResp.DataBean> list = data.data;
                            if (null != list && !list.isEmpty()) {
                                if (page > 1) {
                                    mAdapter.addData(list);
                                } else {
                                    mAdapter.setNewData(list);
                                }
                            } else {
                                mRefreshLayout.setEnableLoadMore(false);
                            }
                        } else {
                            ToastUtil.show(data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("日志");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_journal;
    }
}