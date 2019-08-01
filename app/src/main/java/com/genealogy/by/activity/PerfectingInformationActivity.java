package com.genealogy.by.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.MainActivity;
import com.genealogy.by.R;
import com.genealogy.by.db.User;
import com.genealogy.by.entity.PhoneLogin;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.genealogy.by.utils.my.MyGlideEngine;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/22 调试接口
public class PerfectingInformationActivity extends BaseTitleActivity {
    private Spinner spNation, spRanking, spAlive, spEducation, spBloodType, spAcceptInvitation;
    private TextView tvBirthday, tvArea, tvAncestral, tvOriginArea, tvResidence, tvTimeDeath, tvAreaDeath, tvPreservation, tvRetract, tv_preservation;
    private ImageView ivJeadPortrait;
    private LinearLayout MoreInformation;
    private EditText evName, evAlias, evTelephone, evIntroduce, evLocation, evId_number, evCommonNames, evWord, evNumber, evDesignation, evNoun, evUsedName, evWordGeneration, evMark, tvSchool, tvIndustry, tvCompany, tvPosition, tvMailbox, tvLink, tvAreaBury, tvAreaBuryBetailed, tvLifeYear, tvHeight, tvHereditaryDiseases, evPhone_number, evSurname;
    private CityPickerView mCityPickerView;
    private String title = " ";
    private int relationship_type = 1;
    private String url = "";
    private boolean more = true;
    private RadioButton rb_gender1, rb_gender2, rb_celebrity1, rb_celebrity2;
    private int type = 1;
    private String mUserId;
    private String Gid = "";
    private User user;
    private CityConfig mCityConfig;
    private File mFile;
    private ACallback<BaseTResp2<PhoneLogin>> mCallback;
    private ACallback<BaseTResp2> mCallback2;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        mUserId = intent.getStringExtra("mUserId");
        Gid = intent.getStringExtra("Gid");

        if (intent.hasExtra("user")) {
            user = (User) intent.getSerializableExtra("user");
            if (TextUtils.isEmpty(mUserId)) {
                mUserId = user.getUserid();
            }
            if (TextUtils.isEmpty(Gid)) {
                Gid = user.getGid();
            }
        }
        if (!TextUtils.isEmpty(title)) {
            if (title.contains("无")) {
                setTitleAndType(titleBar, 0, ApiConstant.UerfectUserDetail, "完善您的个人信息");
            } else if (title.contains("父亲")) {
                setTitleAndType(titleBar, 1, ApiConstant.addUsers, "添加父亲");
            } else if (title.contains("母亲")) {
                setTitleAndType(titleBar, 2, ApiConstant.addUsers, "添加母亲");
            } else if (title.contains("兄弟姐妹")) {
                setTitleAndType(titleBar, 3, ApiConstant.addUsers, "添加兄弟姐妹");
            } else if (title.contains("配偶")) {
                setTitleAndType(titleBar, 4, ApiConstant.addUsers, "添加配偶");
            } else if (title.contains("儿子")) {
                setTitleAndType(titleBar, 5, ApiConstant.addUsers, "添加儿子");
            } else if (title.contains("女儿")) {
                setTitleAndType(titleBar, 6, ApiConstant.addUsers, "添加女儿");
            } else if (title.contains("编辑信息")) {
                setTitleAndType(titleBar, 0, ApiConstant.editUser, "编辑信息");
            }
        }
    }

    private void setTitleAndType(TitleBarView titleBar, int type, String uerfectUserDetail, String title) {
        relationship_type = type;
        url = uerfectUserDetail;
        titleBar.setTitleMainText(title);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_perfecting;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        spNation = findViewById(R.id.sp_nation);
        spRanking = findViewById(R.id.sp_ranking);
        spBloodType = findViewById(R.id.sp_blood_type);
        spAcceptInvitation = findViewById(R.id.sp_accept_invitation);
        tvBirthday = findViewById(R.id.tv_birthday);
        MoreInformation = findViewById(R.id.more_information);
        tvRetract = findViewById(R.id.tv_retract);
        tvRetract.setOnClickListener(view -> {
            if (more) {
                setRetract(View.VISIBLE, "收起详细资料", false);
            } else {
                setRetract(View.GONE, "展开详细资料", true);
            }
        });
        tvAncestral = findViewById(R.id.tv_ancestral);//祖籍
        tvAreaDeath = findViewById(R.id.tv_area_death);//死亡地点
        tvOriginArea = findViewById(R.id.tv_origin_area);//迁出地
        tvArea = findViewById(R.id.tv_area);//区域
        spEducation = findViewById(R.id.sp_education);//学历
        tvTimeDeath = findViewById(R.id.tv_time_death);//死亡时间
        tvResidence = findViewById(R.id.tv_residence);//现居地
        spAlive = findViewById(R.id.sp_alive);
        ivJeadPortrait = findViewById(R.id.iv_jeadportrait);//照片
        ivJeadPortrait.setOnClickListener(view -> selectPhoto());
        tvPreservation = findViewById(R.id.tv_preservation);//保存

        if (!TextUtils.isEmpty(title) && !title.contains("无")) {
            findViewById(R.id.recommender).setVisibility(View.GONE);
            findViewById(R.id.phonenumbe).setVisibility(View.GONE);
        }
        tvPreservation.setOnClickListener(view -> Submission2(url));
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        tvBirthday.setOnClickListener(view -> showDatePickerDialog(PerfectingInformationActivity.this, tvBirthday, calendar));
        tvTimeDeath.setOnClickListener(view -> showDatePickerDialog(PerfectingInformationActivity.this, tvTimeDeath, calendar));
        setAdapter(spNation, R.array.plantes_04);//民族
        setAdapter(spRanking, R.array.plantes_03);//排行
        setAdapter(spAlive, R.array.plantes_02);//是否健在
        setAdapter(spEducation, R.array.plantes_05);//学历
        setAdapter(spBloodType, R.array.plantes_06);//血型
        setAdapter(spAcceptInvitation, R.array.plantes_02);//是否接受
        getView();//加载剩余控件
        mCityPickerView = new CityPickerView();
        mCityPickerView.init(this);
        tvArea.setOnClickListener(view -> {
            type = 1;
            Areapick();
        });
        tvAreaDeath.setOnClickListener(view -> {
            type = 2;
            Areapick();
        });
        tvAncestral.setOnClickListener(view -> {
            type = 3;
            Areapick();
        });
        tvOriginArea.setOnClickListener(view -> {
            type = 4;
            Areapick();
        });
        initData();
    }

    private void initData() {
        if (null != user) {
            evSurname.setText(user.getSurname());
            evName.setText(user.getName());

            boolean isBox = user.getSex() == 0;
            setSexChecked(isBox, !isBox);

            evAlias.setText(user.getMinName());
            evNoun.setText(user.getNoun());
            evUsedName.setText(user.getUsedName());
            evWord.setText(user.getWord());
            evNumber.setText(user.getNumber());
            evTelephone.setText(String.format("%s", user.getPhone()));
            evDesignation.setText(user.getDesignation());

            boolean isCelebrity = user.getIsCelebrity() == 0;
            rb_celebrity1.setChecked(isCelebrity);
            rb_celebrity2.setChecked(!isCelebrity);

            String nationality = user.getNationality().replace(" ", "").trim();/*名族*/
            int index = 0;
            for (String s : getResources().getStringArray(R.array.plantes_04)) {
                if (s.equals(nationality)) {
                    spNation.setSelection(index);
                    break;
                }
                index++;
            }

            spRanking.setSelection(user.getRanking());
            if (!TextUtils.isEmpty(user.getProfilePhoto())) {
                GlideManager.loadImg(user.getProfilePhoto(), ivJeadPortrait);
            }
            evIntroduce.setText(user.getRemark());/*简介*/
            tvBirthday.setText(user.getBirthday());
            tvArea.setText(user.getBirthArea());
            evLocation.setText(user.getBirthPlace());
            evId_number.setText(user.getIdCard());
            spAlive.setSelection(user.getHealth());
            evCommonNames.setText(user.getCommonName());
            evWord.setText(user.getWord());
            evNumber.setText(user.getNumber());
            evDesignation.setText(user.getDesignation());
            evNoun.setText(user.getNoun());
            evWordGeneration.setText(user.getWordGeneration());
            evMark.setText(user.getMark());
            tvAncestral.setText(user.getAncestralHome());
            tvOriginArea.setText(user.getMoveOut());
            tvResidence.setText(user.getCurrentResidence());
            String education = user.getEducation();
            index = 0;
            for (String s : getResources().getStringArray(R.array.plantes_05)) {
                if (education.equals(s)) {
                    spEducation.setSelection(index++);
                    break;
                }
            }
            tvSchool.setText(user.getSchool());
            tvIndustry.setText(user.getIndustry());
            tvCompany.setText(user.getUnit());
            tvPosition.setText(user.getPosition());
            tvMailbox.setText(user.getEmail());
            tvLink.setText(user.getUrl());
            tvTimeDeath.setText(user.getDeathTime());
            tvAreaDeath.setText(user.getDieAddress());
            tvAreaBury.setText(user.getBuriedArea());
            tvAreaBuryBetailed.setText(user.getDeathPlace());
            if (!TextUtils.isEmpty(user.getYearOfLife().toString())) {
                tvLifeYear.setText(String.valueOf((int) (Double.parseDouble(user.getYearOfLife().toString()))));
            }
            tvHeight.setText(String.valueOf(user.getHeight().toString()));

            index = 0;
            String bloodGroup = user.getBloodGroup();
            for (String s : getResources().getStringArray(R.array.plantes_06)) {
                if (bloodGroup.equals(s)) {
                    spBloodType.setSelection(index);
                    break;
                }
                index++;
            }
            tvHereditaryDiseases.setText(user.getGeneticDisease());
        } else {
            if (!TextUtils.isEmpty(title)) {
                if (title.contains("父亲")) {
                    setSexChecked(true, false);
                } else if (title.contains("母亲")) {
                    setSexChecked(false, true);
                } else if (title.contains("配偶")) {
                    setSexChecked(false, true);
                } else if (title.contains("儿子")) {
                    setSexChecked(true, false);
                } else if (title.contains("女儿")) {
                    setSexChecked(false, true);
                }
            }
        }
    }

    private void setSexChecked(boolean isBox, boolean isGirl) {
        rb_gender1.setChecked(isBox);
        rb_gender2.setChecked(isGirl);
    }

    private void setRetract(int visible, String content, boolean isMore) {
        MoreInformation.setVisibility(visible);
        tvRetract.setText(content);
        more = isMore;
    }

    private void setAdapter(Spinner spinner, int arrayId) {
        spinner.setAdapter(getArrayAdapter(arrayId));
        spinner.setVisibility(View.VISIBLE);
    }

    private ArrayAdapter<?> getArrayAdapter(int layoutId) {
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, layoutId, android.R.layout.simple_spinner_item);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    /**
     * 日期选择
     *
     * @param activity
     * @param tv
     * @param calendar
     */
    private void showDatePickerDialog(Activity activity, final TextView tv, Calendar calendar) {
        new DatePickerDialog(activity, (view, year, monthOfYear, dayOfMonth) ->
                tv.setText(String.format("%d年%d月%d日", year, (monthOfYear + 1), dayOfMonth)),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void Areapick() {
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
                switch (type) {
                    case 1:
                        tvArea.setText(sb.toString());
                        break;
                    case 2:
                        tvAreaDeath.setText(sb.toString());
                        break;
                    case 3:
                        tvAncestral.setText(sb.toString());
                        break;
                    case 4:
                        tvOriginArea.setText(sb.toString());
                        break;
                }
            }

            @Override
            public void onCancel() {
            }
        });
        mCityPickerView.showCityPicker();
    }

    private static final int REQUEST_CODE_CHOOSE = 1;

    private void selectPhoto() {
        Matisse.from(mContext)
                .choose(MimeType.ofImage())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.gc.qh.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new MyGlideEngine())//图片加载引擎
                .theme(/*R.style.MyMatisse_Dracula*/  R.style.Matisse_Dracula)
                .forResult(REQUEST_CODE_CHOOSE);//
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE) {
            List<String> result = Matisse.obtainPathResult(data);
            mFile = new File(result.get(0));
            GlideManager.loadImg(result.get(0), ivJeadPortrait);
        }
    }

    public void getView() {
        evName = findViewById(R.id.ev_name);
        evAlias = findViewById(R.id.ev_alias);
        evTelephone = findViewById(R.id.ev_telephone);
        evIntroduce = findViewById(R.id.ev_introduce);
        evLocation = findViewById(R.id.ev_location);
        evId_number = findViewById(R.id.ev_id_number);
        evCommonNames = findViewById(R.id.ev_common_names);
        evWord = findViewById(R.id.ev_word);
        evNumber = findViewById(R.id.ev_number);
        evDesignation = findViewById(R.id.ev_designation);
        evNoun = findViewById(R.id.ev_noun);
        evUsedName = findViewById(R.id.ev_used_name);
        evWordGeneration = findViewById(R.id.ev_wordgeneration);
        evMark = findViewById(R.id.ev_mark);
        tvResidence = findViewById(R.id.tv_residence);
        tvSchool = findViewById(R.id.tv_school);
        tvIndustry = findViewById(R.id.tv_industry);
        tvCompany = findViewById(R.id.tv_company);
        tvPosition = findViewById(R.id.tv_position);
        tvMailbox = findViewById(R.id.tv_mailbox);
        tvLink = findViewById(R.id.tv_link);
        tvAreaBury = findViewById(R.id.tv_area_bury);
        tvAreaBuryBetailed = findViewById(R.id.tv_area_bury_detailed);
        tvLifeYear = findViewById(R.id.tv_life_year);
        tvHeight = findViewById(R.id.tv_height);
        tvHereditaryDiseases = findViewById(R.id.tv_hereditary_diseases);
        evPhone_number = findViewById(R.id.ev_phone_number);
        evSurname = findViewById(R.id.ev_surname);
        tvRetract = findViewById(R.id.tv_retract);
        tv_preservation = findViewById(R.id.tv_preservation);
        rb_gender1 = findViewById(R.id.rb_gender1);
        rb_gender2 = findViewById(R.id.rb_gender2);
        rb_celebrity1 = findViewById(R.id.rb_celebrity1);
        rb_celebrity2 = findViewById(R.id.rb_celebrity2);
    }

    private int runking(String str) {
        int index = 0;
        for (String s : getResources().getStringArray(R.array.plantes_03)) {
            if (str.contains(s)) {
                return index;
            }
            index++;
        }
        return 0;
    }

    public void Submission2(String url) {
        int runking = runking(spRanking.getSelectedItem().toString());
        if (runking == 0) {
            ToastUtil.show("请选择排行");
            return;
        }
        int health = spAlive.getSelectedItem().toString().contains("是") ? 0 : 1;
        String sex;
        if (rb_gender1.isChecked()) {
            sex = "0";
        } else if (rb_gender2.isChecked()) {
            sex = "1";
        } else {
            ToastUtil.show("请选择性别");
            return;
        }

        String isCelebrity = " ";
        if (rb_celebrity1.isChecked()) {
            isCelebrity = "0";
        } else if (rb_celebrity1.isChecked()) {
            isCelebrity = "1";
        }

        String surname = evSurname.getText().toString();
        String name = evName.getText().toString();
        String phone = evTelephone.getText().toString();
        String bloodGroup = spBloodType.getSelectedItem().toString();
        String ancestralHome = tvAncestral.getText().toString();
        String currentResidence = tvResidence.getText().toString();
        String wordGeneration = evWordGeneration.getText().toString();
        String school = tvSchool.getText().toString();
        String education = spEducation.getSelectedItem().toString();
        String Birthday = tvBirthday.getText().toString().replace("年", "-")
                .replace("月", "-")
                .replace("日", "");

        String TimeDeath = tvTimeDeath.getText().toString().replace("年", "-")
                .replace("月", "-")
                .replace("日", "");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("surname", surname)
                .addFormDataPart("name", name)
                .addFormDataPart("sex", sex)
                .addFormDataPart("birthday", Birthday)
                .addFormDataPart("profilePhoto", user.getProfilePhoto())
                .addFormDataPart("phone", phone)//手机号码
                .addFormDataPart("health", String.valueOf(health))//健在(0:健在 1:过世)
                .addFormDataPart("height", tvHeight.getText().toString())//身高
                .addFormDataPart("bloodGroup", bloodGroup)//血型
                .addFormDataPart("ancestralHome", ancestralHome)//籍贯
                .addFormDataPart("currentResidence", currentResidence)//聚集地
                .addFormDataPart("wordGeneration", wordGeneration)//字辈
                .addFormDataPart("school", school)//学校
                .addFormDataPart("isCelebrity", isCelebrity)//是否名人(0:是 1:不是)
                .addFormDataPart("education", education)//学历
                .addFormDataPart("email", tvMailbox.getText().toString())//邮箱
                .addFormDataPart("unit", tvCompany.getText().toString())//单位
                .addFormDataPart("position", tvPosition.getText().toString())//职务
                .addFormDataPart("mark", evMark.getText().toString())//标记
                .addFormDataPart("ranking", String.valueOf(runking))//排行
                .addFormDataPart("commonName", evCommonNames.getText().toString())//常用名
                .addFormDataPart("remark", evIntroduce.getText().toString())//备注
                .addFormDataPart("geneticDisease", tvHereditaryDiseases.getText().toString())//遗传病
                .addFormDataPart("word", evWord.getText().toString())//字
                .addFormDataPart("number", evNumber.getText().toString())//号
                .addFormDataPart("designation", evDesignation.getText().toString())//谥号
                .addFormDataPart("noun", evNoun.getText().toString())//名讳
                .addFormDataPart("usedName", evUsedName.getText().toString())//曾用名
                .addFormDataPart("minName", evAlias.getText().toString())//小名
                .addFormDataPart("birthArea", tvArea.getText().toString())//出生区域
                .addFormDataPart("birthPlace", evLocation.getText().toString())//出生地
                .addFormDataPart("deathTime", TimeDeath)//去世时间
                .addFormDataPart("dieAddress", tvAreaDeath.getText().toString())//死亡地点
                .addFormDataPart("buriedArea", tvAreaBury.getText().toString())//葬于区域
                .addFormDataPart("deathPlace", tvAreaBuryBetailed.getText().toString())//葬于区域（详细地点）
                .addFormDataPart("yearOfLife", tvLifeYear.getText().toString())//寿年
                .addFormDataPart("nationality", spNation.getSelectedItem().toString())//民族
                .addFormDataPart("moveOut", tvOriginArea.getText().toString())//迁出至
                .addFormDataPart("industry", tvIndustry.getText().toString())//行业
                .addFormDataPart("url", tvLink.getText().toString())//外部连接
                .addFormDataPart("idCard", evId_number.getText().toString())//身份证
                .addFormDataPart("id", mUserId)
                .addFormDataPart("gId", Gid)
                .addFormDataPart("type", String.valueOf(relationship_type));
        if (relationship_type != 0) {
//            mCallback = new ACallback<BaseTResp2>() {
//                @Override
//                public void onSuccess(BaseTResp2 data) {
//                    if (data.isSuccess()) {
//                        /*保存个人信息*/
//                        if (getIntent().hasExtra("isRegister")) {
//                            //同时登录
//                        }
//                        ToastUtil.show("提交成功: " + data.msg);
//                        SPHelper.setBooleanSF(mContext, "isRefresh", true);
//                        finish();
//                    } else {
//                        ToastUtil.show(data.msg);
//                    }
//                }
//
//                @Override
//                public void onFail(int errCode, String errMsg) {
//                    ToastUtil.show("注册失败: " + errMsg + "，errCode: " + errCode);
//                }
//            };


            mCallback = new ACallback<BaseTResp2<PhoneLogin>>() {
                @Override
                public void onSuccess(BaseTResp2<PhoneLogin> data) {
                    if (data.isSuccess()) {
                      /*  if (getIntent().hasExtra("isRegister")) {
                            //同时登录
                        }
                        ToastUtil.show("提交成功: " + data.msg);
                        SPHelper.setBooleanSF(mContext, "isRefresh", true);
                        finish();*/
                        if (getIntent().hasExtra("isRegister")) {
                            if (data.isSuccess()) {
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", data.data.getUserId());
                                bundle.putString("gId", data.data.getGId() + "");
                                SPHelper.setStringSF(mContext, "profilePhoto", String.valueOf(data.data.getProfilePhoto()));//头像
                                SPHelper.setStringSF(mContext, "nickName", String.valueOf(data.data.getNickName()));//昵称
                                SPHelper.setStringSF(mContext, "GId", String.valueOf(data.data.getGId()));
                                SPHelper.setStringSF(mContext, "Phone", phone);
                                SPHelper.setStringSF(mContext, "UserId", data.data.getUserId());
                                SPHelper.setStringSF(mContext, "Authorization", data.data.getAuthorization());
                                Map<String, String> map = new HashMap<>();
                                map.put("Authorization", data.data.getAuthorization());
                                ViseHttp.CONFIG().globalHeaders(map);
                                register();
                                FastUtil.startActivity(mContext, MainActivity.class, bundle);
                            } else if (data.status == 202) {
                                showLoading();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("title", "无");
                                bundle.putBoolean("isRegister", true);
                                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                            } else {
                                showLoading();
                                ToastUtil.show(data.msg);
                            }
                        } else {
                            ToastUtil.show("提交成功: " + data.msg);
                            SPHelper.setBooleanSF(mContext, "isRefresh", true);
                            finish();
                        }
                    } else {
                        ToastUtil.show(data.msg);
                    }
                }

                @Override
                public void onFail(int errCode, String errMsg) {
                    ToastUtil.show("注册失败: " + errMsg + "，errCode: " + errCode);
                }
//                @Override
//                public void onSuccess(BaseTResp2<PhoneLogin> data) {
//                    if (data.isSuccess()) {
//                        /*保存个人信息*/
//                        if (getIntent().hasExtra("isRegister")) {
//                            //同时登录
//                        }
//                        ToastUtil.show("提交成功: " + data.msg);
//                        SPHelper.setBooleanSF(mContext, "isRefresh", true);
//                        finish();
//                    } else {
//                        ToastUtil.show(data.msg);
//                    }
//                }
//                @Override
//                public void onFail(int errCode, String errMsg) {
//                    ToastUtil.show("注册失败: " + errMsg + "，errCode: " + errCode);
//                }
            };

            if (null != mFile) {
                MultipartBody requestBody = builder.addFormDataPart("imgs", mFile.getName(),
                        RequestBody.create(MediaType.parse("image/*"), mFile)).build();
                ViseHttp.POST(url)
                        .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                        .cacheMode(CacheMode.FIRST_REMOTE)
                        .setRequestBody(requestBody)
                        .addHeaders(ViseHttp.CONFIG().getGlobalHeaders())
                        .request(mCallback);
            } else {
                ViseHttp.POST(url)
                        .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                        .cacheMode(CacheMode.FIRST_REMOTE)
                        .setRequestBody(builder.build()).request(mCallback);
            }
        } else {
            mCallback2 = new ACallback<BaseTResp2>() {
                @Override
                public void onSuccess(BaseTResp2 data) {
                    if (data.isSuccess()) {
                        ToastUtil.show("提交成功: " + data.msg);
                        finish();
                    } else {
                        ToastUtil.show(data.msg);
                        Log.e(TAG, "onSuccess:msg = " + data.msg + ",status= " + data.status);
                    }
                }

                @Override
                public void onFail(int errCode, String errMsg) {
                    ToastUtil.show("注册失败: " + errMsg + "，errCode: " + errCode);
                    Log.e(TAG, "onFail:errMsg = " + errMsg + ",errCode: " + errCode);
                }
            };
            if (null != mFile) {
                MultipartBody requestBody = builder.addFormDataPart("imgs", mFile.getName(),
                        RequestBody.create(MediaType.parse("image/*"), mFile)).build();
                ViseHttp.POST(url)
                        .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                        .cacheMode(CacheMode.FIRST_REMOTE)
                        .setRequestBody(requestBody)
                        .request(mCallback2);
            } else {
                ViseHttp.POST(url)
                        .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                        .cacheMode(CacheMode.FIRST_REMOTE)
                        .setRequestBody(builder.build())
                        .request(mCallback2);
            }
        }
    }

    private void register() {
        new Thread(() ->
                EMClient.getInstance().login(SPHelper.getStringSF(mContext, "UserId"), "zupu123456", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Looper.prepare();
                        showLoading();
                        Log.e(TAG, "onSuccess: 环信登录成功");
                        EMClient.getInstance().chatManager().loadAllConversations();
                    }

                    @Override
                    public void onError(int i, String s) {
                        showLoading();
                        ToastUtil.show(s);
                        Log.i(TAG, "环信登录失败:" + s + i);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        Log.e(TAG, "onProgress: 正在请求 : " + s);
                    }
                })
        ).start();
    }

}