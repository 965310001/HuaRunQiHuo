package com.genealogy.by.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.entity.BookEdit;
import com.genealogy.by.utils.my.BaseTResp2;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 册谱封面信息
 */
public class EditCoverActivity extends BaseTitleActivity implements View.OnClickListener {

    private String id;
    private EditText nameEditText, personEditText;
    private TextView addressEditText, timeEditText;
    //封面参数
    private String name, person, time, address;
    private CityPickerView mCityPickerView;
    private CityConfig mCityConfig;

    @Override
    public void initView(Bundle savedInstanceState) {

        mCityPickerView = new CityPickerView();
        mCityPickerView.init(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        //设置返回参数
        name = intent.getStringExtra("name");
        person = intent.getStringExtra("person");
        time = intent.getStringExtra("time");
        address = intent.getStringExtra("address");

        nameEditText = findViewById(R.id.name);
        personEditText = findViewById(R.id.person);
        timeEditText = findViewById(R.id.time);
        addressEditText = findViewById(R.id.address);

        timeEditText.setOnClickListener(this);
        addressEditText.setOnClickListener(this);

        //设置封面布局 参数
        if (!TextUtils.isEmpty(name)) {
            nameEditText.setText(name);
        }
        if (!TextUtils.isEmpty(person)) {
            personEditText.setText(person);
        }
        if (!TextUtils.isEmpty(time)) {
            timeEditText.setText(time);
        }
        if (!TextUtils.isEmpty(address)) {
            addressEditText.setText(address);
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("册谱封面信息").setRightText("提交").setOnRightTextClickListener(v -> execute());
    }

    private void execute() {
        name = nameEditText.getText().toString();
        person = personEditText.getText().toString();
        time = timeEditText.getText().toString();
        address = addressEditText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请填写家谱名称");
            return;
        }
        if (TextUtils.isEmpty(person)) {
            ToastUtil.show("请填写发起人");
            return;
        }
        if (TextUtils.isEmpty(time)) {
            ToastUtil.show("请填写编谱日期");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtil.show("请填写编谱地址");
            return;
        }
        if (TextUtils.isEmpty(id)) {
            ToastUtil.show("未知错误，请重新登录");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("familybookName", name);
        params.put("sponsor", person);
        params.put("editingTime", time);
        params.put("catalogingAddress", address);
        params.put("id", id);
        ViseHttp.POST(ApiConstant.familyBook_edit)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2<BookEdit>>() {
                    @Override
                    public void onSuccess(BaseTResp2<BookEdit> data) {
                        if (data.isSuccess()) {
                            ToastUtil.show(" 请求成功" + data.msg, new ToastUtil.Builder().setGravity(Gravity.CENTER));
                            Intent intent = new Intent();
                            intent.putExtra("name", name);
                            intent.putExtra("person", person);
                            intent.putExtra("time", time);
                            intent.putExtra("address", address);
                            setResult(200, intent);  // 200表示成功
                            EditCoverActivity.this.finish();
                        } else {
                            ToastUtil.show("请求失败 " + data.msg, new ToastUtil.Builder().setGravity(Gravity.CENTER));
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: " + errMsg, new ToastUtil.Builder().setGravity(Gravity.CENTER));
                    }
                });
    }

    @Override
    public int getContentLayout() {
        return R.layout.edit_cover;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time:
                showDatePickerDialog(this, timeEditText, Calendar.getInstance(Locale.CHINA));
                break;

            case R.id.address:
                showAreapick();
                break;
        }
    }

    private void showDatePickerDialog(Activity activity, final TextView tv, Calendar calendar) {
        new DatePickerDialog(activity, (view, year, monthOfYear, dayOfMonth) ->
                tv.setText(String.format("%d-%s-%s", year, addZero((monthOfYear + 1)), addZero(dayOfMonth))),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    String addZero(int i) {
        return i <= 9 ? String.format("0%d", i) : String.valueOf(i);
    }


    private void showAreapick() {
        mCityConfig = new CityConfig.Builder()
                .title("选择城市")
                .province("广东")
                .city("广州")
                .district("天河区")
                .provinceCyclic(true)
                .cityCyclic(true)
                .districtCyclic(true)
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                .setCustomItemTextViewId(R.id.item_city_name_tv)
                .setShowGAT(true)
                .build();
        mCityPickerView.setConfig(mCityConfig);
        mCityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                StringBuilder sb = new StringBuilder();
                if (province != null) {
                    sb.append(province.getName());
                }
                if (city != null) {
                    sb.append(city.getName());
                }
                if (district != null) {
                    sb.append(district.getName());
                }
                addressEditText.setText(sb.toString());
            }

            @Override
            public void onCancel() {
            }
        });
        mCityPickerView.showCityPicker();
    }
}