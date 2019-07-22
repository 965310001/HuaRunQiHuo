package com.genealogy.by;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.genealogy.by.entity.FutureInternatinal;
import com.genealogy.by.fragment.PhotosFragment;
import com.genealogy.by.fragment.TabHomeFragment;
import com.genealogy.by.fragment.TabWoDeFragment;
import com.genealogy.by.fragment.TabZuCeFragment;
import com.genealogy.by.utils.SPHelper;
import com.githang.statusbar.StatusBarCompat;
import com.vise.xsnow.event.IEvent;
import com.vise.xsnow.event.Subscribe;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.event.SwitchEvent;

/**
 * Created by dell on 2019/4/17.
 */

public class MainActivity extends BaseActivity {

    public static final String TAG_F_HOME = "TAG_HOME";
    public static final String TAG_F_HANGQING = "TAG_F_HANGQING";
    public static final String TAG_F_FAXIAN = "TAG_F_FAXIAN";
    public static final String TAG_F_NEW = "TAG_F_NEW";
    public static final String TAG_F_WODE = "TAG_F_WODE";

    private CommonTabLayout mainTab;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private int mCurrentIndex;

    private Fragment mainHomeFragment;
    private Fragment mainZuCeFragment;
    private Fragment mainFaXianFragment;
    private Fragment mainPhotosFragment;
    private Fragment mainWoDeFragment;

    private String[] mTitles = {"树谱", "族册", "相册", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.icon_shouye, R.mipmap.icon_hq,
            R.mipmap.icon_quanzi, R.mipmap.icon_wode
    };
    private int[] mIconSelectIds = {
            R.mipmap.icon_shouye_s, R.mipmap.icon_hq_s,
            R.mipmap.icon_quanzi_s, R.mipmap.icon_wode_s};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;
    //    private ToolUtil toolUtil;
    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    public String userId = "";
    public String gId = "";

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mainTab = mContentView.findViewById(R.id.main_tab);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        gId = intent.getStringExtra("gId");
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new FutureInternatinal.TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mainTab.setTabData(mTabEntities);
        mainTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.i(TAG, "onTabSelect: " + position);
                switchToFragment(position);
            }

            @Override
            public void onTabReselect(int position) {
                Log.i(TAG, "onTabSelect: " + position);
            }
        });

        mFragmentManager = getSupportFragmentManager();

        //如果是从崩溃中恢复，还需要加载之前的缓存
        if (savedInstanceState != null) {
            restoreFragment(savedInstanceState);
        }
//        else {
//            switchToFragment(0);
//        }
        switchToFragment(0);

        // find view
        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        // init fragment
        mFragments = new ArrayList<>(4);
        mFragments.add(TabHomeFragment.newInstance());
        mFragments.add(TabZuCeFragment.newInstance());
        mFragments.add(PhotosFragment.newInstance());
        mFragments.add(TabWoDeFragment.newInstance());
        // init view pager
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        // register listener
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);

//        initData();

        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#464854"));

    }

    /**
     * 切换fragment
     *
     * @param position
     */
    private void switchToFragment(int position) {
        mTransaction = mFragmentManager.beginTransaction();
        hideAllFragments(mTransaction);
        switch (position) {
            case 0:
                showHomeFragment();
                break;
            case 1:
                showZuCeFragment();
                break;
            case 2:
                showNewFragment();
                break;
            case 3:
                showWodeFragment();
                break;
        }
        mCurrentIndex = position;
        mTransaction.commit();
    }

    //隐藏所有的 fragment
    private void hideAllFragments(FragmentTransaction mTransaction) {
        if (mainHomeFragment != null) {
            mTransaction.hide(mainHomeFragment);
        }
        if (mainZuCeFragment != null) {
//            mTransaction.hide(mainZuCeFragment);
            mTransaction.remove(mainZuCeFragment);
        }
        if (mainFaXianFragment != null) {
            mTransaction.hide(mainFaXianFragment);
        }
        if (mainPhotosFragment != null) {
            mTransaction.hide(mainPhotosFragment);
        }
        if (mainWoDeFragment != null)
            mTransaction.hide(mainWoDeFragment);
    }

    private void showHomeFragment() {
        if (mainHomeFragment == null) {
            mainHomeFragment = TabHomeFragment.newInstance();
            mTransaction.add(R.id.fl_main_content, mainHomeFragment, TAG_F_HOME);
        } else {
            mTransaction.show(mainHomeFragment);
        }
    }

    private void showZuCeFragment() {
        if (mainZuCeFragment == null) {
            mainZuCeFragment = TabZuCeFragment.newInstance();
            mTransaction.add(R.id.fl_main_content, mainZuCeFragment, TAG_F_HANGQING);
            mTransaction.hide(mainZuCeFragment);
        } else {
            mTransaction.show(mainZuCeFragment);
        }
    }

    private void showNewFragment() {
        if (mainPhotosFragment == null) {
            mainPhotosFragment = PhotosFragment.newInstance();
            mTransaction.add(R.id.fl_main_content, mainPhotosFragment, TAG_F_FAXIAN);
            mTransaction.hide(mainPhotosFragment);
        } else {
            mTransaction.show(mainPhotosFragment);
        }
    }

    private void showWodeFragment() {
        if (mainWoDeFragment == null) {
            mainWoDeFragment = TabWoDeFragment.newInstance();
            mTransaction.add(R.id.fl_main_content, mainWoDeFragment, TAG_F_WODE);
        } else {
            mTransaction.show(mainWoDeFragment);
        }
    }

    /**
     * 如果fragment因为内存不够或者其他原因被销毁掉，在这个方法中执行恢复操作
     */
    private void restoreFragment(Bundle savedInstanceState) {
        mCurrentIndex = savedInstanceState.getInt("index");

        mainHomeFragment = mFragmentManager.findFragmentByTag(TAG_F_HOME);
        mainFaXianFragment = mFragmentManager.findFragmentByTag(TAG_F_FAXIAN);
        mainPhotosFragment = mFragmentManager.findFragmentByTag(TAG_F_NEW);
        mainZuCeFragment = mFragmentManager.findFragmentByTag(TAG_F_HANGQING);
        mainWoDeFragment = mFragmentManager.findFragmentByTag(TAG_F_WODE);

        switchToFragment(mCurrentIndex);
        mainTab.setCurrentTab(mCurrentIndex);
    }

    @Override
    public boolean isRegisterEvent() {
        return true;
    }

    @Subscribe
    public void switchTab(IEvent event) {
        if (event instanceof SwitchEvent) {
            mCurrentIndex = ((SwitchEvent) event).position;
            switchToFragment(mCurrentIndex);
            mainTab.setCurrentTab(mCurrentIndex);
        }
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        Fragment currentFragment;
        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (Fragment) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    Log.d("page", i + "");
                    return;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            Log.e(TAG, "onActivityResult: 这里是相册返回数据" + data.getData());
            SPHelper.setStringSF(mContext, "ImgUrl", data.getData().toString());
        }
    }

    /**
     * 获取小于api19时获取相册中图片真正的uri
     * 对于路径是：content://media/external/images/media/33517这种的，需要转成/storage/emulated/0/DCIM/Camera/IMG_20160807_133403.jpg路径，也是使用这种方法
     *
     * @param context
     * @param uri
     * @return
     */
//    public static String getFilePath_below19(Context context, Uri uri) {
//        //这里开始的第二部分，获取图片的路径：低版本的是没问题的，但是sdk>19会获取不到
//        Cursor cursor = null;
//        String path = "";
//        try {
//            String[] proj = {MediaStore.Images.Media.DATA};
//            //好像是android多媒体数据库的封装接口，具体的看Android文档
//            cursor = context.getContentResolver().query(uri, proj, null, null, null);
//            //获得用户选择的图片的索引值
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            //将光标移至开头 ，这个很重要，不小心很容易引起越界
//            cursor.moveToFirst();
//            //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
//            path = cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return path;
//    }
}
