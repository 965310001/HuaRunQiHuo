package com.genealogy.by.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.activity.EditContentActivity;
import com.genealogy.by.activity.EditCoverActivity;
import com.genealogy.by.activity.ReleasePictureActivity;
import com.genealogy.by.activity.SearchMainActivity;
import com.genealogy.by.adapter.LineagekAdapter;
import com.genealogy.by.adapter.onClickAlbumItem;
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.entity.FamilyPhoto;
import com.genealogy.by.entity.SearchNearInBlood;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.DateUtil;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

public class TabZuCeFragment extends BaseTitleFragment implements onClickAlbumItem {
    //viewpage管理
    private ViewPager mViewPager;
    //popwindow
    private PopupWindow popupWindowEdit;
    private TextView homeTextView;
    private TextView editCoverTextView;
    //传送参数
    private int curUpdatePager;
    private final ArrayList<View> views = new ArrayList<>();
    //封面参数
//    private String name, person, time, address;
    //水平滚动条
    private SeekBar seekBar;

    private Intent intent;
    private FamilyBook familyBook;
    private LineagekAdapter lineagekAdapter;
    private int familyAlbum = 0;
    private TextView mTv;
    private View.OnClickListener mEditConvectingClick;
    private PagerAdapter mPagerAdapter;

    @Override
    public void onResume() {
        super.onResume();
        if (SPHelper.getStringSF(mContext, "ImgUrl", null) != null) {
            String str = SPHelper.getStringSF(mContext, "ImgUrl", null);
            SPHelper.removeSF(mContext, "ImgUrl");
            upLoadPic(str, 0);
        }
    }

    public static TabZuCeFragment newInstance() {
        return new TabZuCeFragment();
    }

    @Override
    public int getContentLayout() {
        return R.layout.zuce;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //滑动册谱派系
        mViewPager = mContentView.findViewById(R.id.contentView);
        seekBar = mContentView.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //停止滑动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mViewPager.setCurrentItem(progress);
            }

            //开始滑动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //记录每个进程的刻度
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        lineagekAdapter = new LineagekAdapter(R.layout.item_lineagek);

        lineagekAdapter.setOnPage((adapter, view, position, page) -> {
            Log.i(TAG, "initView: " + page);
            mViewPager.setCurrentItem(Integer.valueOf(page) + 1);
        });


////        List<String> lists = new ArrayList<>();
////        for (FamilyBook.LineageTableBean bean : familyBook.getLineageTable()) {
////            lists.add(String.format("%s%s", bean.getSurname(), bean.getName()));
////        }
//        /*lineagekAdapter.setNewData(familyBook.getLineageTable());*/
//        lineagekAdapter.setOnItemChildClickListener((adapter, view, position) -> {
////            Bundle bundle = new Bundle();
////            FamilyBook.LineageTableBean bean = (FamilyBook.LineageTableBean) adapter.getData().get(position);
////                bundle.putSerializable("data", );
//
//            Log.i(TAG, "initView: " + position);
//            List<FamilyBook.LineageTableBean> lineageTable1 = SPHelper.getDeviceData(mContext, "LineageTable");
//            FamilyBook.LineageTableBean bean = lineageTable1.get(position);
//            Log.i(TAG, "initView: " + bean.getSurname() + bean.getName());
//        });

        doit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //200 修改封面状态返回码
        //201 编辑编委会等文本内容
        if (requestCode == 0) {
            switch (resultCode) {
                case 200:
                    //设置返回参数
                    View view = views.get(0);
                    String name = data.getStringExtra("name");
                    String person = data.getStringExtra("person");
                    String time = data.getStringExtra("time");
                    String address = data.getStringExtra("address");
                    setHomeView(view, name, person, time, true);
                    familyBook.setCatalogingAddress(address);
                    //更新页面
//                    updateViewPagerItem(view, 0);
                    break;
                case 201:
                    int index = mViewPager.getCurrentItem();
                    View v = views.get(index);
                    String content = data.getStringExtra("content");
                    String title = data.getStringExtra("title");
                    if (title.contains("编委会")) {
                        familyBook.setEditorialCommittee(content);
                    } else if (title.contains("家族序言")) {
                        familyBook.setGenealogyPreface(content);
                    } else if (title.contains("姓氏来源")) {
                        familyBook.setLastNameSource(content);
                    } else if (title.contains("家规家训")) {
                        familyBook.setFamilyRule(content);
                    } else if (title.contains("人物传")) {
                        familyBook.setCharacterBiography(content);
                    } else if (title.contains("大事记")) {
                        familyBook.setBigNote(content);
                    } else if (title.contains("后记")) {
                        familyBook.setPostscript(content);
                    }
                    setText(v, R.id.content, content);
                    //contentTextView.setText(content);
                    //更新页面
                    updateViewPagerItem(v, index);
                    break;

                case 202:/*更新图片*/
                    v = views.get(data.getIntExtra("index", 0));
                    content = data.getStringExtra("content");
                    String url = data.getStringExtra("url");
                    Log.i(TAG, "onActivityResult: " + content);

                    if (data.hasExtra("data")) {
                        FamilyPhoto familyPhoto = (FamilyPhoto) data.getSerializableExtra("data");
                        url = familyPhoto.getUrl();
                        content = familyPhoto.getIntroduction();
                    }

                    ImageView iv;
                    switch (this.index) {
                        case 0:
                            iv = v.findViewById(R.id.iv_1);
                            setText(v, R.id.text1, content);
                            GlideManager.loadImg(url, iv);
                            break;
                        case 1:
                            iv = v.findViewById(R.id.iv_2);
                            setText(v, R.id.text2, content);
                            GlideManager.loadImg(url, iv);
                            break;
                        case 2:
                            iv = v.findViewById(R.id.iv_3);
                            setText(v, R.id.text3, content);
                            GlideManager.loadImg(url, iv);
                            break;
                        case 3:
                            iv = v.findViewById(R.id.iv_4);
                            setText(v, R.id.text4, content);
                            GlideManager.loadImg(url, iv);
                            break;
                    }
                    break;
            }
        }
    }

    public void initPopwindowView(View view) {
        homeTextView = view.findViewById(R.id.home);
        editCoverTextView = view.findViewById(R.id.edit_cover);
//        exportCoverTextView = view.findViewById(R.id.export_cover);
    }

    private boolean isFamilyBook() {
        return null != familyBook;
    }

    private void setOnClickListener(View view, int id, View.OnClickListener listener) {
        view.findViewById(id).setOnClickListener(listener);
    }

    private void addHomeView() {
        if (isFamilyBook()) {
            LayoutInflater mLi = LayoutInflater.from(mContext);
            View view = mLi.inflate(R.layout.zuce_cover, null);

            setHomeView(view, familyBook.getFamilybookName(),
                    familyBook.getSponsor(),
                    familyBook.getEditingTimeCN(),
                    false);

            if (null == mEditConvectingClick) {
                mEditConvectingClick = v -> goEditCoverActivity();
            }

            setClick(view, R.id.tv_title, mEditConvectingClick);
            setClick(view, R.id.tv_name, mEditConvectingClick);
            setClick(view, R.id.tv_time, mEditConvectingClick);

            views.add(view);
        }
    }

    private void setHomeView(View view, String title, String name, String time, boolean isSet) {
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        if (TextUtils.isEmpty(name)) {
            name = "";
        }
        if (TextUtils.isEmpty(time)) {
            time = "";
        }
        setText(view, R.id.tv_title, title);
        setText(view, R.id.tv_name, name);
        setText(view, R.id.tv_time, time);

        if (isSet) {
            familyBook.setFamilybookName(title);
            familyBook.setSponsor(name);
            familyBook.setEditingTime(time);
            setText(view, R.id.tv_time, DateUtil.dateToCnDate(time));
        }
    }

    private void addEditView(String title, String strContent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_edit_person, null);
        setText(view, R.id.tv_title, title);

        if (isFamilyBook() && !TextUtils.isEmpty(strContent)) {
            setText(view, R.id.content, strContent);
        }
        views.add(view);
        setClick(view, R.id.editText, v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            map.put("content", strContent);
            goActivity(map, EditContentActivity.class, true);
        });
    }

    private void addTableView(String title) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_family_book, null);
        setText(view, R.id.title, title);
        views.add(view);
    }

    private void addPhoto(int index1, int index2, int index3, int index4) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_add_photo, null);
        doOnClick(view);
        dosetdata(view, index1, index2, index3, index4);
        views.add(view);
    }

    private void goEditCoverActivity() {
        Map<String, Object> map = new HashMap<>();
        map.put("index", String.format("%d", mViewPager.getCurrentItem()));
        map.put("ID", String.valueOf(familyBook.getId()));
        map.put("name", familyBook.getFamilybookName());
        map.put("person", familyBook.getSponsor());
        map.put("time", familyBook.getEditingTime());
        map.put("address", familyBook.getCatalogingAddress());
        goActivity(map, EditCoverActivity.class, true);
    }

    private void setText(View view, int id, String content) {
        if (!TextUtils.isEmpty(content)) {
            mTv = view.findViewById(id);
            mTv.setText(content);
        }
    }

    private void setClick(View view, int id, View.OnClickListener click) {
        view.findViewById(id).setOnClickListener(click);
    }

    public void initFuxiViewPager() {
        //将要分页显示的View装入数组中
        //每个页面的Title数据
        LayoutInflater mLi = LayoutInflater.from(mContext);
        addHomeView();/*添加首页*/

        View view = mLi.inflate(R.layout.zuce_directory, null);
        RecyclerView lineage = view.findViewById(R.id.lineage);
        lineage.setLayoutManager(new LinearLayoutManager(mContext));
        lineage.setAdapter(lineagekAdapter);

        setOnClickListener(view, R.id.committee, v -> updateViewPagerItem(v, 2));
        setOnClickListener(view, R.id.preface, v -> updateViewPagerItem(v, 3));
        setOnClickListener(view, R.id.source, v -> updateViewPagerItem(v, 4));
        setOnClickListener(view, R.id.instruction, v -> updateViewPagerItem(v, 5));
        setOnClickListener(view, R.id.photo, v -> updateViewPagerItem(v, 6));
        setOnClickListener(view, R.id.surface, v -> updateViewPagerItem(v, 16));

        setOnClickListener(view, R.id.biography, v -> updateViewPagerItem(v, views.size() - 2));
        setOnClickListener(view, R.id.epilogue, v -> updateViewPagerItem(v, views.size() - 1));
        setOnClickListener(view, R.id.events, v -> updateViewPagerItem(v, views.size()));

        views.add(view);
        /*编委会*/
        addEditView("编委会", familyBook.getEditorialCommittee());
        addEditView("家族序言", familyBook.getGenealogyPreface());
        addEditView("姓氏来源", familyBook.getLastNameSource());
        addEditView("家规家训", familyBook.getFamilyRule());
        addTableView("家族照");
        for (int i = 0; i < 36; i = i + 4) {
            addPhoto(i, i + 1, i + 2, i + 3);
        }
        addTableView("世系表");

        addLineageTable();

        addEditView("人物传", familyBook.getCharacterBiography());
        addEditView("大事记", familyBook.getBigNote());
        addEditView("后记", familyBook.getPostscript());
        ArrayList<String> titles = new ArrayList<>();
        titles.add("tab1");
        mPagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }

            @Override
            public Object instantiateItem(View container, int position) {
                views.get(position).setTag(position);
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void startUpdate(View arg0) {
            }

            @Override
            public int getItemPosition(Object object) {
                View view = (View) object;
                if (curUpdatePager == (Integer) view.getTag()) {
                    return POSITION_NONE;
                } else {
                    return POSITION_UNCHANGED;
                }
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                seekBar.setProgress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        seekBar.setMax(views.size() - 1);
    }

    private void addLineageTable() {
        List<FamilyBook.LineageTableBean> lineageTable = familyBook.getLineageTable();
        if (null != lineageTable && lineageTable.size() > 0) {
            LayoutInflater mLi = LayoutInflater.from(mContext);

            View view;
            for (int i = 0; i < lineageTable.size(); i = i + 3) {
                view = mLi.inflate(R.layout.zuce_sxb_xq, null);
                setLineageTable(lineageTable, view, i, R.id.include_layout1);

                if (i + 1 < lineageTable.size()) {
                    setLineageTable(lineageTable, view, i + 1, R.id.include_layout2);
                }
                if (i + 2 < lineageTable.size()) {
                    setLineageTable(lineageTable, view, i + 2, R.id.include_layout3);
                }
                views.add(view);
            }
        }
    }

    private void setLineageTable(List<FamilyBook.LineageTableBean> lineageTable, View view, int i, int p) {
        FamilyBook.LineageTableBean bean;
        StringBuffer fatherName;
        List<SearchNearInBlood> spouses;
        AppCompatImageView iv, iv1;

        bean = lineageTable.get(i);
        fatherName = new StringBuffer();
        for (String s : bean.getParents()) {
            fatherName.append(s).append("\n");
        }
        setText(view.findViewById(p), R.id.tv_generation, changeToBig(bean.getLineage()));
        setText(view.findViewById(p), R.id.tv_name, String.format("%s%s", bean.getSurname(), bean.getName()));
        setText(view.findViewById(p), R.id.tv_father_name, fatherName.toString());

        spouses = bean.getSpouses();
        if (null != spouses && spouses.size() > 0) {
            SearchNearInBlood searchNearInBlood = spouses.get(0);
            setText(view.findViewById(p), R.id.tv_spouses_name,
                    String.format("%s%S", searchNearInBlood.getSurname(), spouses.get(0).getName()));

            if (!TextUtils.isEmpty(searchNearInBlood.getProfilePhoto())) {
                iv1 = view.findViewById(p).findViewById(R.id.iv_spouses_icon);
                GlideManager.loadImg(searchNearInBlood.getProfilePhoto(), iv1);
            }

            setText(view.findViewById(p), R.id.tv_spouses_birthday, String.format("%s", searchNearInBlood.getBirthday()));
            setText(view.findViewById(p), R.id.tv_spouses_deathTime, String.format("%s", searchNearInBlood.getDeathTime()));
        }
        /*头像*/
        if (!TextUtils.isEmpty(bean.getProfilePhoto())) {
            iv = view.findViewById(p).findViewById(R.id.iv_icon);
            GlideManager.loadImg(bean.getProfilePhoto(), iv);
        }

        List<String> child = bean.getChild();
        if (null != child && child.size() > 0) {
            fatherName = new StringBuffer();
            for (String s : child) {
                fatherName.append(s).append("\n");
            }
            setText(view.findViewById(p), R.id.tv_children_name, fatherName.toString());
        }

        setText(view.findViewById(p), R.id.tv_ranking, String.format("%s排行", bean.getRanking()));
        setText(view.findViewById(p), R.id.tv_introduction, String.format("%s", bean.getIntroduction()));/*简介*/
        setText(view.findViewById(p), R.id.tv_family, String.format("兄妹%s人", bean.getFamily()));/*简介*/
        setText(view.findViewById(p), R.id.tv_birthday, String.format("%s", bean.getBirthday()));
        setText(view.findViewById(p), R.id.tv_deathTime, String.format("%s", bean.getDeathTime()));


    }

    private String changeToBig(int value) {
        char[] hunit = {'十', '百', '千'};                                               //段内位置表示
        char[] vunit = {'万', '亿'};                                                     //段名表示
        char[] digit = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};  //数字表示
        long midVal = (long) (value * 100);                                      //转化成整形
        String valStr = String.valueOf(midVal);                                //转化成字符串
        String head = valStr.substring(0, valStr.length() - 2);               //取整数部分
        String prefix = "";                                                                 //整数部分转化的结果
        //处理小数点前面的数
        char[] chDig = head.toCharArray();                                                         //把整数部分转化成字符数组
        char zero = '0';                                                                                          //标志'0'表示出现过0
        byte zeroSerNum = 0;                                                                            //连续出现0的次数
        for (int i = 0; i < chDig.length; i++) {                                                               //循环处理每个数字
            int idx = (chDig.length - i - 1) % 4;                                                                //取段内位置
            int vidx = (chDig.length - i - 1) / 4;                                                                //取段位置
            if (chDig[i] == '0') {                                                                                  //如果当前字符是0
                zeroSerNum++;                                                                                 //连续0次数递增
                if (zero == '0') {                                                                                    //标志
                    zero = digit[0];
                }
                if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
                    prefix += vunit[vidx - 1];
                    zero = '0';
                }
                continue;
            }
            zeroSerNum = 0;                                                                                    //连续0次数清零
            if (zero != '0') {                                                                                        //如果标志不为0,则加上,例如万,亿什么的
                prefix += zero;
                zero = '0';
            }
            prefix += digit[chDig[i] - '0'];                                                                        //转化该数字表示
            if (idx > 0) prefix += hunit[idx - 1];
            if (idx == 0 && vidx > 0) {
                prefix += vunit[vidx - 1];                                                                             //段结束位置应该加上段名如万,亿
            }
        }
        if (prefix.startsWith("一十")) {
            String[] strings = prefix.split("一十");
            System.out.println(strings.length);
            if (strings.length == 0) {
                prefix = "十";
            } else {
                prefix = "十" + strings[1];
            }
        }
        return String.format("%s\n世", prefix);                                                                                     //返回正确表示

    }

    private void goActivity(Map<String, Object> map, Class clazz, boolean isResult) {
        intent = new Intent();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object o = entry.getValue();
            if (o instanceof Integer) {
                intent.putExtra(entry.getKey(), (Integer) o);
            } else if (o instanceof String) {
                try {
                    intent.putExtra(entry.getKey(), (String) o);
                } catch (Exception e) {
                    intent.putExtra(entry.getKey(), "");
                }
            }
        }
        intent.setClass(getActivity(), clazz);
        if (isResult) {
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("族册")
                .setLeftTextDrawable(R.drawable.icon_search_svg).setLeftTextDrawableHeight(48).setLeftTextDrawableWidth(48)
                .setRightTextDrawable(R.drawable.icon_three_points_svg).setRightTextSize(30)
//                .setRightTextDrawable(R.mipmap.gengduo1).setRightTextDrawableHeight(48).setRightTextDrawableWidth(48)
                .setOnLeftTextClickListener(view -> startActivity(new Intent(getActivity(), SearchMainActivity.class)))
                .setOnRightTextClickListener(view -> {
                    View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_zuce, null);
                    final PopupWindow popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //popupWindow.setAnimationStyle(R.style.take_photo_anim);
                    //关闭事件
                    popupWindow.setOnDismissListener(new popupDismissListener());
                    //设置背景半透明
                    backgroundAlpha(0.5f);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAsDropDown(view);
                    initPopwindowView(inflate);
                    homeTextView.setOnClickListener(v12 -> {
                        //关闭弹窗
                        popupWindow.dismiss();
                        //返回首页
                        if (mViewPager.getCurrentItem() != 0) {
                            mViewPager.setCurrentItem(0);
                        }
                    });

                    editCoverTextView.setOnClickListener(v1 -> {
                        //关闭弹窗
                        popupWindow.dismiss();
                        goEditCoverActivity();
                    });
                });
    }

    @Override
    public void jumpActivity(Intent intent) {
        startActivityForResult(intent, 0);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 更换pager的方法
     *
     * @param view  新的pager
     * @param index 第几页
     *              示例：updateViewPagerItem(fragment2_parentctrl_changepwd,1);
     */
    private void updateViewPagerItem(View view, int index) {
        curUpdatePager = index;
//        views.remove(index);
//        views.add(index, view);
        mViewPager.setCurrentItem(index);
//        mViewPager.getAdapter().notifyDataSetChanged();
    }

    private void doit() {
        String userId = SPHelper.getStringSF(mContext, "UserId", "");
        String gid = SPHelper.getStringSF(mContext, "GId", "");
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", gid);
        params.put("userId", userId);
        ViseHttp.POST(ApiConstant.familyBook)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2<FamilyBook>>() {
                    @Override
                    public void onSuccess(BaseTResp2<FamilyBook> data) {
                        if (data.isSuccess()) {
                            SPHelper.saveDeviceData(mContext, "familyBook", familyBook);
                            SPHelper.saveDeviceData(mContext, "LineageTable", data.data.getLineageTable());
                            familyBook = data.data;
                            List<String> list = new ArrayList();

                            int index = 0;
                            for (int i = 0; i < data.data.getLineageTable().size(); i++) {
                                if (index != data.data.getLineageTable().get(i).getLineage()) {
                                    list.add(String.valueOf(data.data.getLineageTable().get(i).getLineage()));
                                    index = data.data.getLineageTable().get(i).getLineage();
                                }
                            }
//                            for (int i = 0; i < list.size(); i++) {
//                                for (int j = list.size() - 1; j > i; j--) {
//                                    if (list.get(i) == list.get(j)) {
//                                        list.remove(j);
//                                    }
//                                }
//                            }
//                            int index = -1;
//                            List<FamilyBook.LineageTableBean> lineageTable = data.data.getLineageTable();
//                            FamilyBook.LineageTableBean tableBean = null;
//                            for (FamilyBook.LineageTableBean bean : lineageTable) {
//                                if (index != bean.getLineage()) {
//                                    if (null == tableBean) {
//                                        tableBean = new FamilyBook.LineageTableBean();
//                                        index = bean.getLineage();
//                                        tableBean.setLineage(index);
//                                    }
//                                    tableBean.setName(bean.getName() + bean.getSurname());
//                                } else {
//                                    Log.i(TAG, "onSuccess: " + bean.getSurname() + bean.getName());
//                                }
//                            }
//                            List<Object> objectList = new ArrayList<>();
//                            index = 0;
//                            for (FamilyBook.LineageTableBean bean : lineageTable) {
//                                if (index != bean.getLineage()) {
//                                    index = bean.getLineage();
//                                    objectList.add(Integer.valueOf(bean.getLineage()));
//                                }
//                                objectList.add(bean.getSurname() + bean.getName());
//                            }
//
//                            for (Object o : objectList) {
//                                if (o instanceof Integer) {
//
//                                } else {
//
//                                }
//                            }
//                            Log.i(TAG, "onSuccess: " + objectList);
//                            List<MyLineageTableBean> lineageTableBeans = new ArrayList<>();
//                            List<FamilyBook.LineageTableBean> beanList = new ArrayList<>();
//                            int index = 0;
//                            MyLineageTableBean myLineageTableBean;
//                            for (FamilyBook.LineageTableBean bean : data.data.getLineageTable()) {
//                                if (index != bean.getLineage()) {
//                                    myLineageTableBean = new MyLineageTableBean();
//                                    myLineageTableBean.setIndex(bean.getLineage());
//                                    beanList.add(bean);
//                                    myLineageTableBean.setLineageTableBeans(beanList);
//                                    index = bean.getLineage();
//                                    lineageTableBeans.add(myLineageTableBean);
//                                    beanList = new ArrayList<>();
//                                } else {
//                                    beanList.add(bean);
//                                }
//                            }
//                            Log.i(TAG, "onSuccess: " + lineageTableBeans);


                            //把每个世系的数据遍历出来
                            lineagekAdapter.setNewData(list);
                            initFuxiViewPager();
                            SPHelper.saveDeviceData(mContext, "familyBook", familyBook);
                        } else {
                            ToastUtil.show("请求失败 " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: " + errMsg);
                    }
                });
    }

    private void doOnClick(View view) {
        View.OnClickListener clickListener = v -> FastUtil.startActivity(getContext(), ReleasePictureActivity.class);
        setOnClickListener(view, R.id.image1, clickListener);
        setOnClickListener(view, R.id.image2, clickListener);
        setOnClickListener(view, R.id.image3, clickListener);
        setOnClickListener(view, R.id.image4, clickListener);
    }

    void setGone(View view, int... ids) {
        if (ids.length > 0) {
            for (int id : ids) {
                view.findViewById(id).setVisibility(View.GONE);
            }
        }
    }

    void setVisible(View view, int... ids) {
        if (ids.length > 0) {
            for (int id : ids) {
                view.findViewById(id).setVisibility(View.VISIBLE);
            }
        }
    }

    private String url;

    public void dosetdata(View view, int idx1, int idx2, int idx3, int idx4) {
        List<FamilyPhoto> familyPhoto = familyBook.getFamilyPhoto();
        if (null != familyPhoto) {
            int size = familyBook.getFamilyPhoto().size();
            if (size > idx1 + 1) {
                ImageView iv_1 = view.findViewById(R.id.iv_1);
                setTablePhoto(view, idx1, iv_1, familyPhoto, R.id.text1, R.id.image1, R.id.tv_1, R.id.iv_1);
            }
            if (size > idx2) {
                ImageView iv_2 = view.findViewById(R.id.iv_2);
                setTablePhoto(view, idx2, iv_2, familyPhoto, R.id.text2, R.id.image2, R.id.tv_2, R.id.iv_2);
            }
            if (size > idx3) {
                ImageView iv_3 = view.findViewById(R.id.iv_3);
                setTablePhoto(view, idx3, iv_3, familyPhoto, R.id.text3, R.id.image3, R.id.tv_3, R.id.iv_3);
            }
            if (size > idx4) {
                ImageView iv_4 = view.findViewById(R.id.iv_4);
                setTablePhoto(view, idx4, iv_4, familyPhoto, R.id.text4, R.id.image4, R.id.tv_4, R.id.iv_4);
            }
        }
    }

    private void setTablePhoto(View view, int idx1, ImageView iv_1, List<FamilyPhoto> familyPhoto, int p, int p2, int p3, int p4) {
        String introduction;
        url = familyPhoto.get(idx1).getUrl();
        introduction = familyPhoto.get(idx1).getIntroduction();
        if (TextUtils.isEmpty(introduction)) {
            introduction = "编辑简介";
            setClick(view, p, v -> {
              /*  Bundle bundle = new Bundle();
                bundle.putString("type", "录入");
                bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx1).getId());
                bundle.putSerializable("Url", url);
                bundle.putString("index", String.format("%d", mViewPager.getCurrentItem()));
                FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);*/

                index = idx1 % 4;
                goActivity(idx1);
            });
        } else {
            setClick(view, p, v -> {
                index = idx1 % 4;
                goActivity(idx1);
            });
        }
        setText(view, p, introduction);
        setGone(view, p2, p3);
        GlideManager.loadImg(url, iv_1);
        setVisible(view, p4);
        setClick(view, p4, v -> {
            index = idx1 % 4;
            showPopupWindowEdit(v, 1, familyBook.getFamilyPhoto().get(idx1).getId(),
                    familyBook.getFamilyPhoto().get(idx1).getUrl(), idx1);

        });
    }

    private int index;

    private void goActivity(int index) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "录入简介");
        bundle.putInt("id", familyBook.getFamilyPhoto().get(index).getId());
        bundle.putString("Introduction", familyBook.getFamilyPhoto().get(index).getIntroduction());
        bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(index).getId());
        bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(index).getUrl());
        bundle.putInt("index", mViewPager.getCurrentItem());
        Intent intent = new Intent(mContext, ReleasePictureActivity.class);
        intent.putExtras(bundle);
        jumpActivity(intent);
    }

//    /*private void showPopupWindowEdit(View view, int aog, int id, String url) {
//        // 一个自定义的布局，作为显示的内容
//        View contentView = LayoutInflater.from(mContext).inflate(
//                R.layout.activity_popupzcedit, null);
//        // 设置按钮的点击事件
//        RelativeLayout ll = contentView.findViewById(R.id.ll1);//背景图
//        TextView edit = contentView.findViewById(R.id.edit);//上传
//        TextView input = contentView.findViewById(R.id.input);//录入
//        TextView delete = contentView.findViewById(R.id.delete);//删除
//        TextView cancel = contentView.findViewById(R.id.cancel);//取消
//        if (aog == 0) {
//            input.setVisibility(View.GONE);
//            delete.setVisibility(View.GONE);
//        } else {
//            input.setVisibility(View.VISIBLE);
//            delete.setVisibility(View.VISIBLE);
//        }
//        ll.setOnClickListener(view1 -> popupWindowEdit.dismiss());
//        edit.setOnClickListener(v -> {*//*上传照片*//*
////                openimgs();
//           *//* popupWindowEdit.dismiss();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("type", "上传");
//            bundle.putSerializable("id", id);
////            FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);*//*
////            // TODO: 2019/7/30 单张图片上传
////        });
////        input.setOnClickListener(v -> {*//*录入简介*//*
////                openimgs();
//            popupWindowEdit.dismiss();
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "录入");
//            bundle.putSerializable("Imgid", id);
//            bundle.putSerializable("Imgid", url);
//            bundle.putInt("index", mViewPager.getCurrentItem());
//            FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
//        });
//        delete.setOnClickListener(v -> deleteDoit(id));
//        cancel.setOnClickListener(v -> popupWindowEdit.dismiss());
//        popupWindowEdit = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
//        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//        // 我觉得这里是API的一个bug
//        popupWindowEdit.setOnDismissListener(new popuDismissListener());
//        popupWindowEdit.setBackgroundDrawable(new BitmapDrawable());
//        popupWindowEdit.setOutsideTouchable(true);
//        // 设置好参数之后再show
//        popupWindowEdit.showAsDropDown(view);
//    }*/

    private void showPopupWindowEdit(View view, int aog, int id, String url, int index) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.activity_popupzcedit, null);
        // 设置按钮的点击事件
        RelativeLayout ll = contentView.findViewById(R.id.ll1);//背景图
        TextView edit = contentView.findViewById(R.id.edit);//上传
        TextView input = contentView.findViewById(R.id.input);//录入
        TextView delete = contentView.findViewById(R.id.delete);//删除
        TextView cancel = contentView.findViewById(R.id.cancel);//取消
        if (aog == 0) {
            input.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        } else {
            input.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
        ll.setOnClickListener(view1 -> popupWindowEdit.dismiss());
        edit.setOnClickListener(v -> {/*上传照片*/
//                openimgs();
            popupWindowEdit.dismiss();
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", "上传");
            bundle.putSerializable("id", id);
            FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
            // TODO: 2019/7/30 单张图片上传
        });
        input.setOnClickListener(v -> {/*录入简介*/
//                openimgs();
            popupWindowEdit.dismiss();
           /* Bundle bundle = new Bundle();
            bundle.putString("type", "录入");
            bundle.putSerializable("Imgid", id);
            bundle.putSerializable("Imgid", url);
            bundle.putInt("index", mViewPager.getCurrentItem());
            FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);*/
            goActivity(index);
        });
        delete.setOnClickListener(v -> deleteDoit(id));
        cancel.setOnClickListener(v -> popupWindowEdit.dismiss());
        popupWindowEdit = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindowEdit.setOnDismissListener(new popuDismissListener());
        popupWindowEdit.setBackgroundDrawable(new BitmapDrawable());
        popupWindowEdit.setOutsideTouchable(true);
        // 设置好参数之后再show
        popupWindowEdit.showAsDropDown(view);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class popuDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    public void deleteDoit(int id) {
        popupWindowEdit.dismiss();
        showLoading();
        ViseHttp.GET(ApiConstant.familyBook_delImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .addParam("id", String.valueOf(id))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        for (FamilyPhoto familyPhoto : familyBook.getFamilyPhoto()) {
                            if (familyPhoto.getId() == id) {
                                familyBook.getFamilyPhoto().remove(familyPhoto);
                                break;
                            }
                        }
                        int index = mViewPager.getCurrentItem();
                        updateViewPagerItem(views.get(index), index);
                        hideLoading();
                        ToastUtil.show(data.msg);
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show("失败: " + errMsg);
                    }
                });
    }

    //上传图片
    private void upLoadPic(final String url, final int position) {
        familyAlbum = familyBook.getId();
        File file = new File(url);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imgs", url, image)
                .addFormDataPart("id", String.valueOf(familyAlbum))
                .build();
        ViseHttp.POST(ApiConstant.familyBook_uploadImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(requestBody)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.status == 200) {
                            ToastUtil.show("提交成功: " + data.msg);
                        } else {
                            ToastUtil.show(data.msg + "");
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: " + errMsg + "，errCode: " + errCode);
                    }
                });
    }
}