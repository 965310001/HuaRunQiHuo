package com.genealogy.by.activity;

import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.genealogy.by.R;
import com.genealogy.by.adapter.JournalAdapter;
import com.genealogy.by.entity.Journal;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import tech.com.commoncore.app.FastManager;
import tech.com.commoncore.base.BaseRefreshLoadActivity;
import tech.com.commoncore.constant.ApiConstant;

public class JournalActivity extends BaseRefreshLoadActivity {
    @Override
    public BaseQuickAdapter getAdapter() {
        return new JournalAdapter(R.layout.item_journal);
    }

    @Override
    public void loadData(int page) {
        loadLog(page);
    }

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

    }

    void loadLog(int page) {
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", SPHelper.getStringSF(mContext,"GId"));
        params.put("pageNumber", SPHelper.getStringSF(mContext,page+""));
        params.put("rows", SPHelper.getStringSF(mContext,"30"));
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS= MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.log_search)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS,jsonObject.toString()))
                .request(new ACallback<BaseTResp2<List<Journal>>>() {
                    @Override
                    public void onSuccess(BaseTResp2<List<Journal>> data) {
                        if(data.status==200){
                            FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), data.data, null);
                        }else{
                            FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), data.data, null);
                        }
                    }
                    @Override
                    public void onFail(int errCode, String errMsg) {
                        FastManager.getInstance().getHttpRequestControl().httpRequestSuccess(getIHttpRequestControl(), null, null);
                    }
                });
    }
}