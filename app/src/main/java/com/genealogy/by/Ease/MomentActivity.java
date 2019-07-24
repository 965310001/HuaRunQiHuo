package com.genealogy.by.Ease;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.genealogy.by.Ease.adapter.MomentInfoAdapter;
import com.genealogy.by.Ease.model.Model;
import com.genealogy.by.Ease.model.bean.MomentInfo;
import com.genealogy.by.Ease.ui.CustomToolbar;
import com.genealogy.by.R;

import java.util.List;

public class MomentActivity extends Activity {

    private CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        toolbar = findViewById(R.id.moment_toolbar);
        toolbar.setTitle("朋友圈");

        ListView lv_moment = findViewById(R.id.lv_moment_info);
        List<MomentInfo> momentEntities = Model.getInstance().getDbManager().getMomentTableDao().getAllMomentInfo();
        MomentInfoAdapter momentInfoAdapter = new MomentInfoAdapter(MomentActivity.this, momentEntities);
        lv_moment.addHeaderView(this.getLayoutInflater().inflate(R.layout.layout_moment_heading, null),
                null, false);
        //设置Adapter显示
        lv_moment.setAdapter(momentInfoAdapter);
    }
}
