package com.genealogy.by.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.genealogy.by.entity.AddUser;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.genealogy.by.utils.my.MyGlideEngine;
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

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/22 调试接口
public class PerfectingInformationActivity2 extends BaseTitleActivity {
    private Spinner spNation, spRanking, spAlive, spEducation, spBloodType, spAcceptInvitation;
    private TextView tvBirthday, tvArea, tvAncestral, tvOriginArea, tvResidence, tvTimeDeath, tvAreaDeath, tvPreservation, tvRetract, tv_preservation;
    private ImageView ivJeadPortrait;
    private LinearLayout recommender, phonenumbe, acceptance, MoreInformation;
    private EditText evName, evAlias, evTelephone, evIntroduce, evLocation, evId_number, evCommonNames, evWord, evNumber, evDesignation, evNoun, evUsedName, evWordGeneration, evMark, tvSchool, tvIndustry, tvCompany, tvPosition, tvMailbox, tvLink, tvAreaBury, tvAreaBuryBetailed, tvLifeYear, tvHeight, tvHereditaryDiseases, evPhone_number, evSurname;
    private CityPickerView mCityPickerView = new CityPickerView();
    private String defaultProvinceName = "广东";
    private String defaultCityName = "广州";
    private String defaultDistrict = "天河区";
    private boolean isProvinceCyclic = true;
    private boolean isCityCyclic = true;
    private boolean isDistrictCyclic = true;
    private boolean isShowGAT = true;
    private int relationship_type = 1;

    //    private RadioGroup rgGender, rgCelebrity;
    //    private String sex = "";
//    private String isCelebrity = "";
    private Intent intent;
    private String title = " ";
    private String url = "";
    private boolean more = true;
    private RadioButton rb_gender1, rb_gender2, rb_celebrity1, rb_celebrity2;
    private AddUser addUser;
    private int type = 1;
    private String mUserId;
    private String Gid = "";
    private User user;
    private CityConfig mCityConfig;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        intent = getIntent();
        title = intent.getStringExtra("title");
        mUserId = intent.getStringExtra("mUserId");
        Gid = intent.getStringExtra("Gid");

        if (intent.hasExtra("user")) {
            user = (User) intent.getSerializableExtra("user");
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
//        rgGender.setOnCheckedChangeListener((radioGroup, i) -> {
//        rgGender = findViewById(R.id.rg_gender);
//            switch (i) {
//                case R.id.rb_gender1:
//                    sex = "0";
//                    break;
//                case R.id.rb_gender2:
//                    sex = "1";
//                    break;
//            }
//        });
//        rgCelebrity = findViewById(R.id.rg_celebrity);
//        rgCelebrity.setOnCheckedChangeListener((radioGroup, i) -> {
//            switch (i) {
//                case R.id.rb_celebrity1:
//                    isCelebrity = "0";
//                    break;
//                case R.id.rb_celebrity2:
//                    isCelebrity = "1";
//                    break;
//            }
//        });
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

        recommender = findViewById(R.id.recommender);
        phonenumbe = findViewById(R.id.phonenumbe);
        acceptance = findViewById(R.id.acceptance);
        if (!TextUtils.isEmpty(title) && !title.contains("无")) {
            recommender.setVisibility(View.GONE);
            phonenumbe.setVisibility(View.GONE);
        }
        tvPreservation.setOnClickListener(view -> Submission2(url));
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        tvBirthday.setOnClickListener(view -> showDatePickerDialog(PerfectingInformationActivity2.this, tvBirthday, calendar));
        tvTimeDeath.setOnClickListener(view -> showDatePickerDialog(PerfectingInformationActivity2.this, tvTimeDeath, calendar));
        setAdapter(spNation, R.array.plantes_04);//民族
        setAdapter(spRanking, R.array.plantes_03);//排行
        setAdapter(spAlive, R.array.plantes_02);//是否健在
        setAdapter(spEducation, R.array.plantes_05);//学历
        setAdapter(spBloodType, R.array.plantes_06);//血型
        setAdapter(spAcceptInvitation, R.array.plantes_02);//是否接受
        getView();//加载剩余控件
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
            tvHeight.setText(String.valueOf(user.getHeight()));

            index = 0;
            String bloodGroup = user.getBloodGroup();
            for (String s : getResources().getStringArray(R.array.plantes_06)) {
                if (bloodGroup.equals(s)) {
                    spBloodType.setSelection(index++);
                    break;
                }
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
                .province(defaultProvinceName)
                .city(defaultCityName)
                .district(defaultDistrict)
                .provinceCyclic(isProvinceCyclic)
                .cityCyclic(isCityCyclic)
                .districtCyclic(isDistrictCyclic)
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                .setCustomItemTextViewId(R.id.item_city_name_tv)
                .setShowGAT(isShowGAT)
                .build();
        mCityPickerView.setConfig(mCityConfig);
        mCityPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                StringBuilder sb = new StringBuilder();
                if (province != null) {
                    sb.append(province.getName() + " ");
                }

                if (city != null) {
                    sb.append(city.getName() + " ");
                }

                if (district != null) {
                    sb.append(district.getName() + " ");
                }

                switch (type) {
                    case 1:
                        tvArea.setText("" + sb.toString());
                        break;
                    case 2:
                        tvAreaDeath.setText("" + sb.toString());
                        break;
                    case 3:
                        tvAncestral.setText("" + sb.toString());
                        break;
                    case 4:
                        tvOriginArea.setText("" + sb.toString());
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

    void selectPhoto() {
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

    File file = new File("");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE) {
            List<String> result = Matisse.obtainPathResult(data);
            file = new File(result.get(0));
            GlideManager.loadCircleImg(result.get(0), ivJeadPortrait);
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
        String height = tvLifeYear.getText().toString();
        String bloodGroup = spBloodType.getSelectedItem().toString();
        String ancestralHome = tvAncestral.getText().toString();
        String currentResidence = tvResidence.getText().toString();
        String wordGeneration = evWordGeneration.getText().toString();
        String school = tvSchool.getText().toString();
        String education = spEducation.getSelectedItem().toString();
        String Birthday = tvBirthday.getText().toString().replace("年", "-");
        Birthday = Birthday.replace("月", "-");
        Birthday = Birthday.replace("日", "");
        String TimeDeath = tvTimeDeath.getText().toString().replace("年", "-");
        TimeDeath = TimeDeath.replace("月", "-");
        TimeDeath = TimeDeath.replace("日", "");
        addUser = new AddUser();
        Log.i(TAG, "Submission2: " + mUserId);
        if (mUserId != null) {
            addUser.setId(Integer.parseInt(mUserId));
        }
        if (Gid != null) {
            addUser.setgId(Integer.parseInt(Gid));
        }
        addUser.setSurname(surname);
        addUser.setName(name);
        addUser.setPhone(phone);
        addUser.setHeight(height);
        addUser.setHealth(health);
        addUser.setAncestralHome(ancestralHome);
        addUser.setYearOfLife(tvLifeYear.getText().toString());
        addUser.setWordGeneration(wordGeneration);
        addUser.setUsedName(evUsedName.getText().toString());
        addUser.setUrl(tvLink.getText().toString());
        addUser.setUnit(tvCompany.getText().toString());
        addUser.setSex(sex);
        addUser.setSchool(school);
        addUser.setRemark(evIntroduce.getText().toString());
        addUser.setRanking(String.valueOf(runking));
        addUser.setPosition(tvPosition.getText().toString());
        addUser.setNumber(evNumber.getText().toString());
        addUser.setNoun(evNoun.getText().toString());
        addUser.setNationality(spNation.getSelectedItem().toString());
        addUser.setMoveOut(tvOriginArea.getText().toString());
        addUser.setMinName(evAlias.getText().toString());
        addUser.setMark(evMark.getText().toString());
        addUser.setIsCelebrity(Integer.valueOf(isCelebrity));
        addUser.setIndustry(tvIndustry.getText().toString());
        addUser.setImgs(file);
        addUser.setIdCard(evId_number.getText().toString());
        addUser.setGeneticDisease(tvHereditaryDiseases.getText().toString());
        addUser.setEmail(tvMailbox.getText().toString());
        addUser.setEducation(education);
        addUser.setDieAddress(tvAreaDeath.getText().toString());
        addUser.setDesignation(evDesignation.getText().toString());
        addUser.setDeathTime(TimeDeath);
        addUser.setDeathPlace(tvAreaBury.getText().toString());
        addUser.setCurrentResidence(currentResidence);
        addUser.setCommonName(evCommonNames.getText().toString());
        addUser.setBuriedArea(tvAreaBury.getText().toString());
        addUser.setBloodGroup(bloodGroup);
        addUser.setBirthPlace(evLocation.getText().toString());/*出生地*/
        addUser.setBirthday(Birthday);
        addUser.setBirthArea(tvArea.getText().toString());

        Map<String, String> params = new HashMap<>();
//        params.put("type", String.valueOf(relationship_type));
        /*params.put("gId", Gid);*/
        params.put("surname", surname);//姓
        params.put("name", name);//名
        params.put("sex", sex);//性别
        params.put("birthday", Birthday);//生日
        params.put("imgs", "");//头像图片
        params.put("phone", phone);//手机号码
        params.put("health", String.valueOf(health));//健在(0:健在 1:过世)
        params.put("height", height);//身高
        params.put("bloodGroup", bloodGroup);//血型
        params.put("ancestralHome", ancestralHome);//籍贯
        params.put("currentResidence", currentResidence);//聚集地
        params.put("wordGeneration", wordGeneration);//字辈
        params.put("school", school);//学校
        params.put("isCelebrity", isCelebrity);//是否名人(0:是 1:不是)
        params.put("education", education);//学历
        params.put("email", tvMailbox.getText().toString());//邮箱
        params.put("unit", tvCompany.getText().toString());//单位
        params.put("position", tvPosition.getText().toString());//职务
        params.put("mark", evMark.getText().toString());//标记
        params.put("ranking", String.valueOf(runking));//排行
        params.put("commonName", evCommonNames.getText().toString());//常用名
        params.put("remark", evIntroduce.getText().toString());//备注
        params.put("geneticDisease", tvHereditaryDiseases.getText().toString());//遗传病
        params.put("word", evWord.getText().toString());//字
        params.put("number", evNumber.getText().toString());//号
        params.put("designation", evDesignation.getText().toString());//谥号
        params.put("noun", evNoun.getText().toString());//名讳
        params.put("usedName", evUsedName.getText().toString());//曾用名
        params.put("minName", evAlias.getText().toString());//小名
        params.put("birthArea", tvArea.getText().toString());//出生区域
        params.put("birthPlace", evLocation.getText().toString());//出生地
        params.put("deathTime", TimeDeath);//去世时间
        params.put("dieAddress", tvAreaDeath.getText().toString());//死亡地点
        params.put("buriedArea", tvAreaBury.getText().toString());//葬于区域
        params.put("deathPlace", tvAreaBuryBetailed.getText().toString());//葬于区域（详细地点）
        params.put("yearOfLife", tvLifeYear.getText().toString());//寿年
        params.put("nationality", spNation.getSelectedItem().toString());//民族
        params.put("moveOut", tvOriginArea.getText().toString());//迁出至
        params.put("industry", tvIndustry.getText().toString());//行业
        params.put("url", tvLink.getText().toString());//外部连接
        params.put("idCard", evId_number.getText().toString());//身份证

        Log.i(TAG, "Submission2: " + relationship_type);
        if (relationship_type != 0) {
            params.put("id", mUserId);
            params.put("gId", Gid);
            params.put("type", String.valueOf(relationship_type));

            ViseHttp.POST(url)
                    .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                    .cacheMode(CacheMode.FIRST_REMOTE)
                    .addParams(params)

//                    .addForm("type", relationship_type)
//                    .addForm("gId", Gid)
//                    .addForm("id", mUserId)
//                    .addForm("surname", surname)//姓
//                    .addForm("name", name)//名
//                    .addForm("sex", sex)//性别
//                    .addForm("birthday", Birthday)//生日
//                    .addForm("imgs", file)//头像图片
//                    .addForm("phone", phone)//手机号码
//                    .addForm("health", String.valueOf(health))//健在(0:健在 1:过世)
//                    .addForm("height", height)//身高
//                    .addForm("bloodGroup", bloodGroup)//血型
//                    .addForm("ancestralHome", ancestralHome)//籍贯
//                    .addForm("currentResidence", currentResidence)//聚集地
//                    .addForm("wordGeneration", wordGeneration)//字辈
//                    .addForm("school", school)//学校
//                    .addForm("isCelebrity", isCelebrity)//是否名人(0:是 1:不是)
//                    .addForm("education", education)//学历
//                    .addForm("email", tvMailbox.getText().toString())//邮箱
//                    .addForm("unit", tvCompany.getText().toString())//单位
//                    .addForm("position", tvPosition.getText().toString())//职务
//                    .addForm("mark", evMark.getText().toString())//标记
//                    .addForm("ranking", String.valueOf(runking))//排行
//                    .addForm("commonName", evCommonNames.getText().toString())//常用名
//                    .addForm("remark", evIntroduce.getText().toString())//备注
//                    .addForm("geneticDisease", tvHereditaryDiseases.getText().toString())//遗传病
//                    .addForm("word", evWord.getText().toString())//字
//                    .addForm("number", evNumber.getText().toString())//号
//                    .addForm("designation", evDesignation.getText().toString())//谥号
//                    .addForm("noun", evNoun.getText().toString())//名讳
//                    .addForm("usedName", evUsedName.getText().toString())//曾用名
//                    .addForm("minName", evAlias.getText().toString())//小名
//                    .addForm("birthArea", tvArea.getText().toString())//出生区域
//                    .addForm("birthPlace", evLocation.getText().toString())//出生地
//                    .addForm("deathTime", TimeDeath)//去世时间
//                    .addForm("dieAddress", tvAreaDeath.getText().toString())//死亡地点
//                    .addForm("buriedArea", tvAreaBury.getText().toString())//葬于区域
//                    .addForm("deathPlace", tvAreaBuryBetailed.getText().toString())//葬于区域（详细地点）
//                    .addForm("yearOfLife", tvLifeYear.getText().toString())//寿年
//                    .addForm("nationality", spNation.getSelectedItem().toString())//民族
//                    .addForm("moveOut", tvOriginArea.getText().toString())//迁出至
//                    .addForm("industry", tvIndustry.getText().toString())//行业
//                    .addForm("url", tvLink.getText().toString())//外部连接
//                    .addForm("idCard", evId_number.getText().toString())//身份证

                    .request(new ACallback<BaseTResp2>() {
                        @Override
                        public void onSuccess(BaseTResp2 data) {
                            if (data.isSuccess()) {
                                ToastUtil.show("提交成功: " + data.msg);
                                SPHelper.setBooleanSF(mContext, "isRefresh", true);
                                FastUtil.startActivity(mContext, MainActivity.class);
                                PerfectingInformationActivity2.this.finish();
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
                    });

        } else {
            params.put("id", user.getUserid());

            Log.i(TAG, "Submission2: 2" + mUserId);
            ViseHttp.POST(url)
                    .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                    .cacheMode(CacheMode.FIRST_REMOTE)
                    .addParams(params)

//                    .addForm("id", user.getUserid())
//                    .addForm("surname", surname)//姓
//                    .addForm("name", name)//名
//                    .addForm("sex", sex)//性别
//                    .addForm("birthday", Birthday)//生日
//                    .addForm("imgs", "")//头像图片
//                    .addForm("phone", phone)//手机号码
//                    .addForm("health", String.valueOf(health))//健在(0:健在 1:过世)
//                    .addForm("height", height)//身高
//                    .addForm("bloodGroup", bloodGroup)//血型
//                    .addForm("ancestralHome", ancestralHome)//籍贯
//                    .addForm("currentResidence", currentResidence)//聚集地
//                    .addForm("wordGeneration", wordGeneration)//字辈
//                    .addForm("school", school)//学校
//                    .addForm("isCelebrity", isCelebrity)//是否名人(0:是 1:不是)
//                    .addForm("education", education)//学历
//                    .addForm("email", tvMailbox.getText().toString())//邮箱
//                    .addForm("unit", tvCompany.getText().toString())//单位
//                    .addForm("position", tvPosition.getText().toString())//职务
//                    .addForm("mark", evMark.getText().toString())//标记
//                    .addForm("ranking", String.valueOf(runking))//排行
//                    .addForm("commonName", evCommonNames.getText().toString())//常用名
//                    .addForm("remark", evIntroduce.getText().toString())//备注
//                    .addForm("geneticDisease", tvHereditaryDiseases.getText().toString())//遗传病
//                    .addForm("word", evWord.getText().toString())//字
//                    .addForm("number", evNumber.getText().toString())//号
//                    .addForm("designation", evDesignation.getText().toString())//谥号
//                    .addForm("noun", evNoun.getText().toString())//名讳
//                    .addForm("usedName", evUsedName.getText().toString())//曾用名
//                    .addForm("minName", evAlias.getText().toString())//小名
//                    .addForm("birthArea", tvArea.getText().toString())//出生区域
//                    .addForm("birthPlace", evLocation.getText().toString())//出生地
//                    .addForm("deathTime", TimeDeath)//去世时间
//                    .addForm("dieAddress", tvAreaDeath.getText().toString())//死亡地点
//                    .addForm("buriedArea", tvAreaBury.getText().toString())//葬于区域
//                    .addForm("deathPlace", tvAreaBuryBetailed.getText().toString())//葬于区域（详细地点）
//                    .addForm("yearOfLife", tvLifeYear.getText().toString())//寿年
//                    .addForm("nationality", spNation.getSelectedItem().toString())//民族
//                    .addForm("moveOut", tvOriginArea.getText().toString())//迁出至
//                    .addForm("industry", tvIndustry.getText().toString())//行业
//                    .addForm("url", tvLink.getText().toString())//外部连接
//                    .addForm("idCard", evId_number.getText().toString())//身份证

                    .request(new ACallback<BaseTResp2>() {
                        @Override
                        public void onSuccess(BaseTResp2 data) {
                            if (data.isSuccess()) {
                                ToastUtil.show("提交成功: " + data.msg);
                                FastUtil.startActivity(mContext, MainActivity.class);
                                PerfectingInformationActivity2.this.finish();
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
                    });
        }
    }

    public static class PhotosEditActivity extends BaseTitleActivity {

        private TextView mTvDescries;
        private EditText mEtTitle;
        private String TAG = "PhotosAddActivity";
        private TextView mSwitchShare;
        private boolean isTrue;//是否分享的开关参数

        public void initView() {
            mTvDescries = findViewById(R.id.photo_description);
            mEtTitle = findViewById(R.id.photo_title);
            mSwitchShare = findViewById(R.id.switch_share);
            mSwitchShare.setOnClickListener(view -> isTrue = !isTrue);
            //取到id 暂时没用
            /*getIntent().getStringExtra("id");*/

            findViewById(R.id.tv_create).setOnClickListener(v -> {
                String title = mEtTitle.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.show("请输入标题！");
                } else {
                    execute(title, isTrue ? 1 : 0);
                    Intent intent = new Intent();
                    intent.putExtra("photoDescription", mTvDescries.getText().toString());
                    intent.putExtra("photoTitle", title);
                    setResult(200, intent);
                    PhotosEditActivity.this.finish();
                }
            });
        }

        public void execute(String title, int isTrue) {
            String userId = SPHelper.getStringSF(PhotosEditActivity.this, "UserId", "");
            String gid = SPHelper.getStringSF(PhotosEditActivity.this, "GId", "");
            HashMap<String, String> params = new HashMap<>();
            params.put("userId", userId);
            params.put("gId", gid);
            params.put("title", title);
            params.put("isTrue", String.valueOf(isTrue));
            Log.e(TAG, "请求参数: 参数：" + params.toString());
            JSONObject jsonObject = new JSONObject(params);
            ViseHttp.POST(ApiConstant.album_create)
                    .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                    .cacheMode(CacheMode.FIRST_REMOTE)
                    .setJson(jsonObject)
                    .request(new ACallback<BaseTResp2>() {
                        @Override
                        public void onSuccess(BaseTResp2 data) {
                            if (data.isSuccess()) {
                                ToastUtil.show("创建相册请求成功");
                            } else {
                                Log.e(TAG, "onSuccess: 创建相册请求成功  msg= " + data.msg);
                                ToastUtil.show("创建相册失败: " + data.msg);
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
        public void setTitleBar(TitleBarView titleBar) {
            titleBar.setTitleMainText("添加相册");
        }

        @Override
        public int getContentLayout() {
            return R.layout.photos_add;
        }

        @Override
        public void initView(Bundle savedInstanceState) {
            initView();
        }
    }
}