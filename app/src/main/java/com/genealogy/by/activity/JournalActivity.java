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
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;

public class JournalActivity extends BaseTitleActivity {

    int page = 1;
    RecyclerView rv_contentFastLib;
    JournalAdapter journalAdapter;

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
        rv_contentFastLib.setLayoutManager(new LinearLayoutManager(mContext));
        rv_contentFastLib.setAdapter(journalAdapter);
        loadLog(page + 1);
    }

    void loadLog(int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", SPHelper.getStringSF(mContext, "GId"));
        params.put("pageNumber", String.valueOf(page));
        params.put("rows", "30");
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS = MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.log_search)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS, jsonObject.toString()))
                .request(new ACallback<BaseTResp>() {
                    @Override
                    public void onSuccess(BaseTResp data) {
                        Log.e(TAG, "onSuccess: data=" + data);
                        if (data.getStatus() == 200) {
                            List<BaseTResp.DataBean> list = data.getData();
                            journalAdapter.setNewData(list);
                            journalAdapter.notifyDataSetChanged();
                        } else {
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        Log.e(TAG, "onFail: errMsg=" + errMsg);
                    }
                });
    }
}