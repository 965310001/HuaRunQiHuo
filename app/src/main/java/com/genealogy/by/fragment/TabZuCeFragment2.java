package com.genealogy.by.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.genealogy.by.entity.FamilyBook;
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
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/23 调试接口
public class TabZuCeFragment2 extends BaseTitleFragment {

    //日志提示
    private static final String TAG = "TabZuCeFragment";
    //viewpage管理
    private ViewPager mViewPager;
    //搜索 其他按钮
    private TextView searchButton;
    private TextView aboutButton;
    //popwindow
    //    private TextView exportCoverTextView;
    PopupWindow popupWindowEdit;
    private TextView homeTextView;
    private TextView editCoverTextView;
    //传送参数
    private int curUpdatePager;
    private final ArrayList<View> views = new ArrayList<View>();
    //封面参数
    private String name, person, time, address;
    //水平滚动条
    private SeekBar seekBar;

    //标题 编辑点击
//    private TextView title;
    private ImageView editText;
    private Intent intent;
    private FamilyBook familyBook;
    private LineagekAdapter lineagekAdapter;
    private int familyAlbum = 0;

    public static TabZuCeFragment newInstance() {
        Bundle args = new Bundle();
        TabZuCeFragment fragment = new TabZuCeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.zuce;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        //滑动册谱派系
        mViewPager = mContentView.findViewById(R.id.contentView);
        //搜索 其他
        searchButton = mContentView.findViewById(R.id.zuce_search);
        aboutButton = mContentView.findViewById(R.id.zuce_about);
        //水平滚动条
        seekBar = mContentView.findViewById(R.id.seekBar);
        searchButton.setOnClickListener(v ->
                //从fragment跳转到activity中
                startActivity(new Intent(getActivity(), SearchMainActivity.class))
        );

        aboutButton.setOnClickListener(v -> {
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_zuce, null);
            final PopupWindow popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //popupWindow.setAnimationStyle(R.style.take_photo_anim);
            //关闭事件
            popupWindow.setOnDismissListener(new popupDismissListener());
            //设置背景半透明
            backgroundAlpha(0.5f);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(v);
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
//                Intent i = new Intent(getActivity(), EditCoverActivity.class);
//                i.putExtra("ID", String.valueOf(familyBook.getId()));
//                i.putExtra("name", name);
//                i.putExtra("person", person);
//                i.putExtra("time", time);
//                i.putExtra("address", address);
//                startActivityForResult(i, 0);

                Map<String, Object> map = new HashMap<>();
                map.put("ID", String.valueOf(familyBook.getId()));
                map.put("name", name);
                map.put("person", person);
                map.put("time", time);
                map.put("address", address);
                goActivity(map, EditCoverActivity.class, true);
            });

        });

        //默认布局
//        initFuxiViewPager();
        //水平滚动条 监听
        seekBar.setMax(views.size());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //停止滑动
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mViewPager.setCurrentItem(progress);
                Log.d(TAG, progress + "");
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
                    //获取封面布局
                    LayoutInflater mLi = LayoutInflater.from(getActivity());
                    View view = mLi.inflate(R.layout.zuce_cover, null);
                    TextView nameTextView = view.findViewById(R.id.title);
                    TextView personTextView = view.findViewById(R.id.person);
                    TextView timeTextView = view.findViewById(R.id.time);
                    TextView addressTextView = view.findViewById(R.id.address);

                    //设置返回参数
                    name = data.getStringExtra("name");
                    person = data.getStringExtra("person");
                    time = data.getStringExtra("time");
                    address = data.getStringExtra("address");

                    //设置封面布局 参数
                    if (name != null && !name.equals("")) {
                        nameTextView.setText(name);
                    }
                    if (person != null && !person.equals("")) {
                        personTextView.setText(person);
                    }
                    if (time != null && !time.equals("")) {
                        timeTextView.setText(time);
                    }
                    if (address != null && !address.equals("")) {
                        addressTextView.setText(address);
                    }

                    //更新页面
//                    updateViewPagerItem(view, 0);
                    break;
                case 201:
//                    String content = data.getStringExtra("content");
                    // = Integer.valueOf(data.getStringExtra("index"));
                    int index = mViewPager.getCurrentItem();
                    View v = views.get(index);
                    TextView contentTextView = v.findViewById(R.id.content);
                    contentTextView.setText(data.getStringExtra("content"));

                    Log.d(TAG, v.getId() + "");
//                  contentTextView.setText(content);
                    //更新页面
                    updateViewPagerItem(v, index);
                    break;
            }
        }
    }

    public void initPopwindowView(View view) {
        homeTextView = view.findViewById(R.id.home);
        editCoverTextView = view.findViewById(R.id.edit_cover);
//        exportCoverTextView = view.findViewById(R.id.export_cover);
    }

    boolean isFamilyBook() {
        return null != familyBook;
    }

    private void setOnClickListener(View view, int id, View.OnClickListener listener) {
        view.findViewById(id).setOnClickListener(listener);
    }

    public void initFuxiViewPager() {
        //将要分页显示的View装入数组中
        //每个页面的Title数据
        LayoutInflater mLi = LayoutInflater.from(mContext);
        View view = mLi.inflate(R.layout.zuce_cover, null);
        if (isFamilyBook() && !familyBook.getFamilybookName().isEmpty()) {
            TextView titlese = view.findViewById(R.id.title);
            titlese.setText(familyBook.getFamilybookName());
        }
        if (isFamilyBook() && !familyBook.getSponsor().isEmpty()) {
            TextView person = view.findViewById(R.id.person);
            person.setText(familyBook.getSponsor());
        }
        if (isFamilyBook() && !familyBook.getEditingTimeCN().isEmpty()) {
            TextView time = view.findViewById(R.id.time);
            time.setText(familyBook.getEditingTimeCN());
        }
        views.add(view);
        view = mLi.inflate(R.layout.zuce_directory, null);
        android.support.v7.widget.RecyclerView lineage = view.findViewById(R.id.lineage);
        lineage.setLayoutManager(new LinearLayoutManager(mContext));
        lineage.setAdapter(lineagekAdapter);

//        view.findViewById(R.id.committee).setOnClickListener(view116 -> updateViewPagerItem(view116, 2));
//        view.findViewById(R.id.preface).setOnClickListener(view115 -> updateViewPagerItem(view115, 3));
//        view.findViewById(R.id.source).setOnClickListener(view114 -> updateViewPagerItem(view114, 4));
//        view.findViewById(R.id.instruction).setOnClickListener(view112 -> updateViewPagerItem(view112, 5));
//        view.findViewById(R.id.photo).setOnClickListener(view113 -> updateViewPagerItem(view113, 6));
//        view.findViewById(R.id.surface).setOnClickListener(view111 -> updateViewPagerItem(view111, 16));
//        view.findViewById(R.id.biography).setOnClickListener(view110 -> updateViewPagerItem(view110, views.size() - 2));
//        view.findViewById(R.id.epilogue).setOnClickListener(view19 -> updateViewPagerItem(view19, views.size() - 1));
//        view.findViewById(R.id.events).setOnClickListener(view18 -> updateViewPagerItem(view18, views.size()));

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
        view = mLi.inflate(R.layout.zuce_bwh, null);
        TextView content = view.findViewById(R.id.content);
        TextView pageNumber = view.findViewById(R.id.pageNumber);
        TextView editorialtitle = view.findViewById(R.id.editorialtitle);
        if (isFamilyBook()) {
            String contentstr = familyBook.getEditorialCommittee();
            if (contentstr != null) {
                content.setText(contentstr);
            }
        }
        views.add(view);
        pageNumber.setText((views.size()) + "");
        //点击事件
        editText = view.findViewById(R.id.editText);
        editText.setOnClickListener(v -> {
//            intent = new Intent();
//            View view17 = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_bwh, null);
//            String str = String.valueOf(familyBook.getId());
//            String str2 = editorialtitle.getText().toString();

            Map<String, Object> map = new HashMap<>();
            map.put("title", editorialtitle.getText().toString());
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            goActivity(map, EditContentActivity.class, true);
        });
        /*家族序言*/
        view = mLi.inflate(R.layout.zuce_jzxy, null);
        TextView prefacecontent = view.findViewById(R.id.content);
        TextView prefacepageNumber = view.findViewById(R.id.pageNumber);
        TextView prefacetitle = view.findViewById(R.id.prefacetitle);
        if (isFamilyBook()) {
            if (familyBook.getGenealogyPreface() != null) {
                prefacecontent.setText(familyBook.getGenealogyPreface());
            }
        }
        views.add(view);
        prefacepageNumber.setText((views.size() + 1) + "");
        //点击事件
        editText = view.findViewById(R.id.editText);
        editText.setOnClickListener(v -> {
//            intent = new Intent();
//            View view16 = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_jzxy, null);
//            String str = String.valueOf(familyBook.getId());
//            intent.putExtra("id", String.valueOf(familyBook.getId()));
//            intent.putExtra("title", prefacetitle.getText().toString());
//            intent.putExtra("index", String.format("%d", mViewPager.getCurrentItem()));
//            intent.setClass(getActivity(), EditContentActivity.class);
//            startActivityForResult(intent, 0);

            Map<String, Object> map = new HashMap<>();
            map.put("title", prefacetitle.getText().toString());
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            goActivity(map, EditContentActivity.class, true);
        });
        /*姓氏来源*/
        view = mLi.inflate(R.layout.zuce_xsly, null);
        TextView sourcecontent = view.findViewById(R.id.content);
        TextView sourcepageNumber = view.findViewById(R.id.pageNumber);
        TextView sourcetitle = view.findViewById(R.id.sourcetitle);
        if (isFamilyBook() && familyBook.getLastNameSource() != null) {
            sourcecontent.setText(familyBook.getLastNameSource());
        }
        views.add(view);
        sourcepageNumber.setText((views.size() + 1) + "");
        //点击事件
        editText = view.findViewById(R.id.editText);
        editText.setOnClickListener(v -> {
//            intent = new Intent();
//            View view15 = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_xsly, null);
//            String str = String.valueOf(familyBook.getId());
//            intent.putExtra("title", sourcetitle.getText().toString());
//            intent.putExtra("id", String.valueOf(familyBook.getId()));
//            intent.putExtra("index", String.format("%d", mViewPager.getCurrentItem()));
//            intent.setClass(getActivity(), EditContentActivity.class);
//            startActivityForResult(intent, 0);

            Map<String, Object> map = new HashMap<>();
            map.put("title", sourcetitle.getText().toString());
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            goActivity(map, EditContentActivity.class, true);
        });
        /*族规家训*/
        view = mLi.inflate(R.layout.zuce_jgjx, null);
        TextView rulescontent = view.findViewById(R.id.content);
        TextView rulespageNumber = view.findViewById(R.id.pageNumber);
        TextView rulestitle = view.findViewById(R.id.rulestitle);
        if (isFamilyBook() && familyBook.getFamilyRule() != null) {
            rulescontent.setText(familyBook.getFamilyRule());
        }
        views.add(view);
        rulespageNumber.setText((views.size() + 1) + "");
        //点击事件
        editText = view.findViewById(R.id.editText);
        editText.setOnClickListener(v -> {
//            intent = new Intent();
//            View view14 = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_jgjx, null);
//            String str = String.valueOf(familyBook.getId());
//            intent.putExtra("title", rulestitle.getText().toString());
//            intent.putExtra("id", String.valueOf(familyBook.getId()));
//            intent.putExtra("index", mViewPager.getCurrentItem() + "");
//            intent.setClass(getActivity(), EditContentActivity.class);
//            startActivityForResult(intent, 0);


            Map<String, Object> map = new HashMap<>();
            map.put("title", rulestitle.getText().toString());
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            goActivity(map, EditContentActivity.class, true);
        });
        /*家族照封面*/
        view = mLi.inflate(R.layout.zuce_jzz, null);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz1, null);
        doOnClick(view);
        dosetdata(view, 0, 1, 2, 3);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz2, null);
        doOnClick(view);
        dosetdata(view, 4, 5, 6, 7);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz3, null);
        doOnClick(view);
        dosetdata(view, 8, 9, 10, 11);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz4, null);
        doOnClick(view);
        dosetdata(view, 12, 13, 14, 15);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz5, null);
        doOnClick(view);
        dosetdata(view, 16, 17, 18, 19);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz6, null);
        doOnClick(view);
        dosetdata(view, 20, 21, 22, 23);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz7, null);
        doOnClick(view);
        dosetdata(view, 24, 25, 26, 27);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz8, null);
        doOnClick(view);
        dosetdata(view, 28, 29, 30, 31);
        views.add(view);
        /*家族照*/
        view = mLi.inflate(R.layout.zuce_jzz9, null);
        doOnClick(view);
        dosetdata(view, 32, 33, 34, 35);
        views.add(view);

        /*世系表*/
        view = mLi.inflate(R.layout.zuce_sxb, null);
        views.add(view);

        /*世系表*/
        view = mLi.inflate(R.layout.zuce_sxb_xq, null);
        views.add(view);

        /*人物传*/
        view = mLi.inflate(R.layout.zuce_person, null);
        views.add(view);
        //点击事件
        editText = view.findViewById(R.id.editText);
        TextView charactertitle = view.findViewById(R.id.charactertitle);
        editText.setOnClickListener(v -> {
//            intent = new Intent();
//            View view13 = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_person, null);
//            String str = String.valueOf(familyBook.getId());
//            intent.putExtra("title", charactertitle.getText().toString());
//            intent.putExtra("id", String.valueOf(familyBook.getId()));
//            intent.putExtra("index", String.format("%d", mViewPager.getCurrentItem()));
//            intent.setClass(getActivity(), EditContentActivity.class);
//            startActivityForResult(intent, 0);

            Map<String, Object> map = new HashMap<>();
            map.put("title", charactertitle.getText().toString());
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            goActivity(map, EditContentActivity.class, true);
        });
        /*大事记*/
        view = mLi.inflate(R.layout.zuce_bignote, null);
        views.add(view);
        editText = view.findViewById(R.id.editText);
        TextView bignotetitle = view.findViewById(R.id.bignotetitle);
        editText.setOnClickListener(v -> {
//            intent = new Intent();
//            View view12 = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_bignote, null);
//            String str = String.valueOf(familyBook.getId());
//            intent.putExtra("title", bignotetitle.getText().toString());
//            intent.putExtra("id", String.valueOf(familyBook.getId()));
//            intent.putExtra("index", mViewPager.getCurrentItem() + "");
//            intent.setClass(getActivity(), EditContentActivity.class);
//            startActivityForResult(intent, 0);


            Map<String, Object> map = new HashMap<>();
            map.put("title", bignotetitle.getText().toString());
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            goActivity(map, EditContentActivity.class, true);
        });

        view = mLi.inflate(R.layout.zuce_postscript, null);
        views.add(view);
        editText = view.findViewById(R.id.editText);
        TextView epiloguetitle = view.findViewById(R.id.epiloguetitle);
        editText.setOnClickListener(v -> {
//            intent = new Intent();
//            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_postscript, null);
//            String str = String.valueOf(familyBook.getId());
//            intent.putExtra("title", epiloguetitle.getText().toString());
//            intent.putExtra("id", String.valueOf(familyBook.getId()));
//            intent.putExtra("index", mViewPager.getCurrentItem() + "");
//            intent.putExtra("index", familyBook.getId() + "");
//            intent.setClass(getActivity(), EditContentActivity.class);
//            startActivityForResult(intent, 0);


            Map<String, Object> map = new HashMap<>();
            map.put("title", epiloguetitle.getText().toString());
            map.put("index", String.format("%d", mViewPager.getCurrentItem()));
            map.put("id", String.valueOf(familyBook.getId()));
            goActivity(map, EditContentActivity.class, true);
        });

        //标题集合
        final ArrayList<String> titles = new ArrayList<String>();
        titles.add("tab1");

        PagerAdapter mPagerAdapter = new PagerAdapter() {
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
                .setLeftTextDrawable(R.mipmap.search_icon).setLeftTextDrawableHeight(48).setLeftTextDrawableWidth(48)
                .setRightText("...").setRightTextSize(30)
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
//                        Intent intent = new Intent(getActivity(), EditCoverActivity.class);
//                        intent.putExtra("ID", String.valueOf(familyBook.getId()));
//                        intent.putExtra("name", name);
//                        intent.putExtra("person", person);
//                        intent.putExtra("time", time);
//                        intent.putExtra("address", address);
//                        startActivityForResult(intent, 0);

                        Map<String, Object> map = new HashMap<>();
                        map.put("index", String.format("%d", mViewPager.getCurrentItem()));
                        map.put("ID", String.valueOf(familyBook.getId()));
                        map.put("name", name);
                        map.put("person", person);
                        map.put("time", time);
                        map.put("address", address);
                        goActivity(map, EditCoverActivity.class, true);
                    });

                });
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

    public void doit() {
        String userId = SPHelper.getStringSF(mContext, "UserId", "");
        String gid = SPHelper.getStringSF(mContext, "GId", "");
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", gid);
        params.put("userId", userId);
        Log.e(TAG, "execute: 参数：" + params.toString());
        JSONObject jsonObject = new JSONObject(params);
//        final MediaType JSONS = MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.familyBook)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setJson(jsonObject)
//                .setRequestBody(RequestBody.create(JSONS, jsonObject.toString()))
                .request(new ACallback<BaseTResp2<FamilyBook>>() {
                    @Override
                    public void onSuccess(BaseTResp2<FamilyBook> data) {
                        if (data.status == 200) {
                            SPHelper.saveDeviceData(mContext, "familyBook", familyBook);
                            SPHelper.saveDeviceData(mContext, "LineageTable", data.data.getLineageTable());
                            Log.e(TAG, "onSuccess: 族册查询请求成功  msg= " + data.msg);
                            familyBook = data.data;
                            List<String> list = new ArrayList();
                            for (int i = 0; i < data.data.getLineageTable().size(); i++) {
                                list.add(String.valueOf(data.data.getLineageTable().get(i).getLineage()));
                            }
                            for (int i = 0; i < list.size(); i++) {
                                for (int j = list.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
                                {
                                    if (list.get(i) == list.get(j)) {
                                        list.remove(j);
                                    }
                                }
                            }
                            list.toString();
                            //把每个世系的数据遍历出来
                            lineagekAdapter.setNewData(list);
                            initFuxiViewPager();
                            SPHelper.saveDeviceData(mContext, "familyBook", familyBook);
                        } else {
                            Log.e(TAG, "onSuccess: 族册查询请求失败  msg= " + data.msg);
                            ToastUtil.show("请求失败 " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + ",errCode:  " + errCode);
                    }
                });
    }

    private void doOnClick(View view) {
        View.OnClickListener clickListener = v -> FastUtil.startActivity(getContext(), ReleasePictureActivity.class);
        setOnClickListener(view, R.id.image1, clickListener);
        setOnClickListener(view, R.id.image2, clickListener);
        setOnClickListener(view, R.id.image3, clickListener);
        setOnClickListener(view, R.id.image4, clickListener);

//        view.findViewById(R.id.image1).setOnClickListener(clickListener);
//        view.findViewById(R.id.image2).setOnClickListener(clickListener);
//        view.findViewById(R.id.image3).setOnClickListener(clickListener);
//        view.findViewById(R.id.image4).setOnClickListener(clickListener);
    }

    public void dosetdata(View view, int idx1, int idx2, int idx3, int idx4) {
//        TextView text1 = view.findViewById(R.id.text1);
//        TextView text2 = view.findViewById(R.id.text2);
//        TextView text3 = view.findViewById(R.id.text3);
//        TextView text4 = view.findViewById(R.id.text4);

        TextView tv_1 = view.findViewById(R.id.tv_1);
        TextView tv_2 = view.findViewById(R.id.tv_2);
        TextView tv_3 = view.findViewById(R.id.tv_3);
        TextView tv_4 = view.findViewById(R.id.tv_4);
        TextView image1 = view.findViewById(R.id.image1);
        TextView image2 = view.findViewById(R.id.image2);
        TextView image3 = view.findViewById(R.id.image3);
        TextView image4 = view.findViewById(R.id.image4);
        ImageView iv_1 = view.findViewById(R.id.iv_1);
        ImageView iv_2 = view.findViewById(R.id.iv_2);
        ImageView iv_3 = view.findViewById(R.id.iv_3);
        ImageView iv_4 = view.findViewById(R.id.iv_4);
        if (familyBook.getFamilyPhoto().size() > idx1 + 1) {
            String url_1 = familyBook.getFamilyPhoto().get(idx1).getUrl();
            TextView text1 = view.findViewById(R.id.text1);
            if (familyBook.getFamilyPhoto().get(idx1).getIntroduction() != null
                    && familyBook.getFamilyPhoto().get(idx1).getIntroduction().trim().length() != 0) {
                text1.setText(familyBook.getFamilyPhoto().get(idx1).getIntroduction());
                text1.setOnClickListener(view116 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putString("Introduction", familyBook.getFamilyPhoto().get(idx1).getIntroduction());
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx1).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx1).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            } else {
                text1.setText("编辑简介");
                text1.setOnClickListener(view115 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx1).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx1).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            }
            image1.setVisibility(View.GONE);
            tv_1.setVisibility(View.GONE);
            iv_1.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_1);
            iv_1.setOnClickListener(view114 -> showPopupWindowEdit(view114, 1, familyBook.getFamilyPhoto().get(idx1).getId(),
                    familyBook.getFamilyPhoto().get(idx1).getUrl()));
        } else {
            image1.setVisibility(View.VISIBLE);
            tv_1.setVisibility(View.VISIBLE);
            iv_1.setVisibility(View.GONE);
            image1.setOnClickListener(view113 -> showPopupWindowEdit(view113, 0, familyBook.getFamilyPhoto().get(idx1).getId(),
                    familyBook.getFamilyPhoto().get(idx1).getUrl()));
        }
        if (familyBook.getFamilyPhoto().size() > idx2) {
            String url_1 = familyBook.getFamilyPhoto().get(idx2).getUrl();
            TextView text2 = view.findViewById(R.id.text2);

            if (familyBook.getFamilyPhoto().get(idx2).getIntroduction() != null
                    && familyBook.getFamilyPhoto().get(idx2).getIntroduction().trim().length() != 0) {
                text2.setText(familyBook.getFamilyPhoto().get(idx2).getIntroduction());
                text2.setOnClickListener(view112 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putString("Introduction", familyBook.getFamilyPhoto().get(idx2).getIntroduction());
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx2).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx2).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            } else {
                text2.setText("编辑简介");
                text2.setOnClickListener(view111 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx2).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx2).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            }
            image2.setVisibility(View.GONE);
            tv_2.setVisibility(View.GONE);
            iv_2.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_2);
            iv_2.setOnClickListener(view110 -> showPopupWindowEdit(view110, 1, familyBook.getFamilyPhoto().get(idx2).getId(),
                    familyBook.getFamilyPhoto().get(idx2).getUrl()));
        } else {
            image2.setVisibility(View.VISIBLE);
            tv_2.setVisibility(View.VISIBLE);
            iv_2.setVisibility(View.GONE);
            image2.setOnClickListener(view19 -> showPopupWindowEdit(view19, 0, familyBook.getFamilyPhoto().get(idx2).getId(),
                    familyBook.getFamilyPhoto().get(idx2).getUrl()));
        }
        if (familyBook.getFamilyPhoto().size() > idx3) {
            String url_1 = familyBook.getFamilyPhoto().get(idx3).getUrl();
            TextView text3 = view.findViewById(R.id.text3);
            if (familyBook.getFamilyPhoto().get(idx3).getIntroduction() != null
                    && familyBook.getFamilyPhoto().get(idx3).getIntroduction().trim().length() != 0) {
                text3.setText(familyBook.getFamilyPhoto().get(idx3).getIntroduction());
                text3.setOnClickListener(view18 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putString("Introduction", familyBook.getFamilyPhoto().get(idx3).getIntroduction());
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx3).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx3).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            } else {
                text3.setText("编辑简介");
                text3.setOnClickListener(view17 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx3).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx3).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            }
            image3.setVisibility(View.GONE);
            tv_3.setVisibility(View.GONE);
            iv_3.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_3);
            iv_3.setOnClickListener(view15 -> showPopupWindowEdit(view15, 1, familyBook.getFamilyPhoto().get(idx3).getId(),
                    familyBook.getFamilyPhoto().get(idx3).getUrl()));
        } else {
            image3.setVisibility(View.VISIBLE);
            tv_3.setVisibility(View.VISIBLE);
            iv_3.setVisibility(View.GONE);
            image3.setOnClickListener(view16 -> showPopupWindowEdit(view16, 0, familyBook.getFamilyPhoto().get(idx3).getId(),
                    familyBook.getFamilyPhoto().get(idx3).getUrl()));
        }
        if (familyBook.getFamilyPhoto().size() > idx4) {
            String url_1 = familyBook.getFamilyPhoto().get(idx4).getUrl();
            TextView text4 = view.findViewById(R.id.text4);
            if (familyBook.getFamilyPhoto().get(idx4).getIntroduction() != null
                    && familyBook.getFamilyPhoto().get(idx4).getIntroduction().trim().length() != 0) {
                text4.setText(familyBook.getFamilyPhoto().get(idx4).getIntroduction());
                text4.setOnClickListener(view14 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putString("Introduction", familyBook.getFamilyPhoto().get(idx4).getIntroduction());
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx4).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx4).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            } else {
                text4.setText("编辑简介");
                text4.setOnClickListener(view13 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type", "录入");
                    bundle.putSerializable("Imgid", familyBook.getFamilyPhoto().get(idx4).getId());
                    bundle.putSerializable("Url", familyBook.getFamilyPhoto().get(idx4).getUrl());
                    FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
                });
            }
            image4.setVisibility(View.GONE);
            tv_4.setVisibility(View.GONE);
            iv_4.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_4);
            iv_4.setOnClickListener(view12 -> showPopupWindowEdit(view12, 1, familyBook.getFamilyPhoto().get(idx4).getId(),
                    familyBook.getFamilyPhoto().get(idx4).getUrl()));
        } else {
            image1.setVisibility(View.VISIBLE);
            tv_4.setVisibility(View.VISIBLE);
            iv_4.setVisibility(View.GONE);
            image4.setOnClickListener(view1 -> showPopupWindowEdit(view1, 0, familyBook.getFamilyPhoto().get(idx4).getId(),
                    familyBook.getFamilyPhoto().get(idx4).getUrl()));
        }
    }

    private void showPopupWindowEdit(View view, int aog, int id, String url) {
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
        edit.setOnClickListener(v -> {
//                openimgs();
            popupWindowEdit.dismiss();
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", "上传");
            bundle.putSerializable("id", id);
            FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
        });
        input.setOnClickListener(v -> {
//                openimgs();
            popupWindowEdit.dismiss();
            Bundle bundle = new Bundle();
            bundle.putSerializable("type", "录入");
            bundle.putSerializable("Imgid", id);
            bundle.putSerializable("Imgid", url);
            FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
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

    @Override
    public void onResume() {
        super.onResume();
        if (SPHelper.getStringSF(mContext, "ImgUrl", null) != null) {
            String str = SPHelper.getStringSF(mContext, "ImgUrl", null);
//            String url = str;
            SPHelper.removeSF(mContext, "ImgUrl");
            upLoadPic(str, 0);
        }
    }

    public void deleteDoit(int id) {
        ViseHttp.GET(ApiConstant.familyBook_delImg)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .addParam("id", String.valueOf(id))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.status == 200) {
                            Log.e(TAG, "onSuccess: 照片删除成功  msg= " + data.msg);
                        } else {
                            Log.e(TAG, "onSuccess:  照片删除失败  msg= " + data.msg);
                            ToastUtil.show("请求失败 " + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: " + errMsg);
                        Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
                    }
                });
    }

//    public void openimgs() {
//        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
//        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intentToPickPic, 0);
//    }

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