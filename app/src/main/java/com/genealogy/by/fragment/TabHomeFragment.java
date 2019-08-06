package com.genealogy.by.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.genealogy.by.R;
import com.genealogy.by.fragment.shupu.ShuPuFragment;
import com.genealogy.by.utils.DisplayUtil;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.base.BaseFragment;

public class TabHomeFragment extends BaseFragment {

    private ViewPager contentShupuView;
    private RadioGroup shupuRadioButton;
    private List<Fragment> mFragments;

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#464854"), false);
        mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);

        shupuRadioButton = mContentView.findViewById(R.id.shupuRadioButton);
        contentShupuView = mContentView.findViewById(R.id.contentShupuView);

        mFragments = new ArrayList<>(3);
        mFragments.add(ShuPuFragment.newInstance("父系"));
        mFragments.add(ShuPuFragment.newInstance("近亲"));
        mFragments.add(ShuPuFragment.newInstance("全部"));

        contentShupuView.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments));
        contentShupuView.setOffscreenPageLimit(3);
        contentShupuView.addOnPageChangeListener(mPageChangeListener);
        shupuRadioButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    contentShupuView.setCurrentItem(i);
                    return;
                }
                ((ShuPuFragment) mFragments.get(i)).hidePopupWindow();
            }
        }
    };
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) shupuRadioButton.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
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

    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab_home;
    }
}