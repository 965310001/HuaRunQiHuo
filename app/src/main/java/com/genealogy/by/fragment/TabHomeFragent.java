package com.genealogy.by.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.genealogy.by.R;
import com.genealogy.by.activity.PerfectingInformationActivity;
import com.genealogy.by.fragment.shupu.ShuPuFragment;
import com.genealogy.by.view.FamilyTreeView;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

public class TabHomeFragent extends BaseFragment implements View.OnClickListener {

    protected String[] refuseTips = {};//拒绝请求后的对话框提示
    protected String[] permissions = {};//需要请求的权限
    private FamilyTreeView ftvTree;
    private TextView woziji;
    private InputMethodManager manager;
    private PopupWindow popupWindow2;
    PopupWindow popupWindowAdd;
    PopupWindow popupWindowEdit;
    private RadioGroup shupuRadioButton;
    private FragmentManager mFragmentManager;
    private android.support.v4.view.ViewPager contentShupuView;
    private boolean mIsShowing = false;
    private WindowManager.LayoutParams params;
    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;

    public static TabHomeFragent newInstance() {
        Bundle args = new Bundle();
        TabHomeFragent fragment = new TabHomeFragent();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        woziji = mContentView.findViewById(R.id.woziji);
        shupuRadioButton = mContentView.findViewById(R.id.shupuRadioButton);
        contentShupuView = mContentView.findViewById(R.id.contentShupuView);
        mFragments = new ArrayList<>(3);
        mFragments.add(ShuPuFragment.newInstance("父系"));
        mFragments.add(ShuPuFragment.newInstance("近亲"));
//        mFragments.add(ShuPuAllFragment.newInstance("全部"));
        mAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments);
        contentShupuView.setAdapter(mAdapter);
        contentShupuView.setOffscreenPageLimit(3);
        // register listener
        contentShupuView.addOnPageChangeListener(mPageChangeListener);
        shupuRadioButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
        String appName = getString(R.string.app_name);
        permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        refuseTips = new String[]{
                String.format("在设置-应用-%1$s-权限中开启存储权限，以正常使用该功能", appName),
        };
        setStatusBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_school:
                break;
        }
    }

    //layout改变监听
    private void setStatusBar() {
        Window window = mContext.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 导航栏透明
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        window.getDecorView().setFitsSystemWindows(true);
    }

    PopupWindow popupWindow;

    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.activity_popup, null);
        // 设置按钮的点击事件
        RelativeLayout ll = contentView.findViewById(R.id.ll1);//背景图
        TextView invitation = contentView.findViewById(R.id.invitation);
        TextView relationship = contentView.findViewById(R.id.relationship);
        TextView details = contentView.findViewById(R.id.details);
        TextView core = contentView.findViewById(R.id.core);
        TextView add = contentView.findViewById(R.id.add);
        TextView edit = contentView.findViewById(R.id.edit);
        ll.setOnClickListener(view1 -> popupWindow.dismiss());
        invitation.setOnClickListener(v -> {
            ToastUtil.show("点击邀请");
            popup(v);
            popupWindow.dismiss();
        });
        relationship.setOnClickListener(v -> {

        });
        details.setOnClickListener(v -> {

        });
        core.setOnClickListener(v -> {

        });
        add.setOnClickListener(v -> {
            ToastUtil.show("点击添加");
            popupWindow.dismiss();
            showPopupWindowAdd(v);
        });
        edit.setOnClickListener(v -> {
//            ToastUtil.show("点击编辑");
            popupWindow.dismiss();
            showPopupWindowEdit(v);
        });
        popupWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        backgroundAlpha(0.5f);
        // 设置好参数之后再show
        popupWindow.showAsDropDown(contentView);
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle("发起成功");
        normalDialog.setMessage("对方注册或登录APP即可看到您的邀请函。");
        normalDialog.setPositiveButton("分享下载链接到微信",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    public void popup(View view) {
        if (popupWindow2 == null) {
            initPopup();
        }
        if (!mIsShowing) {
//            params.alpha= 0.3f;
//            mContext.getWindow().setAttributes(params);
            popupWindow2.showAtLocation(mContentView.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
            mIsShowing = true;
        }
    }

    private void initPopup() {
        View pop = View.inflate(mContext, R.layout.keyboard, null);
        RelativeLayout ll = (RelativeLayout) pop.findViewById(R.id.ll1);//背景图
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow2.dismiss();
            }
        });
        popupWindow2 = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow2.setTouchable(true);
        popupWindow2.setOutsideTouchable(false);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mIsShowing = false;
    }

    public void dismiss(View view) {
        if (popupWindow2 != null && mIsShowing) {
            popupWindow2.dismiss();
            mIsShowing = false;
            params.alpha = 1f;
            mContext.getWindow().setAttributes(params);
        }
    }

    private void showPopupWindowAdd(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.activity_popupadd, null);
        // 设置按钮的点击事件
        RelativeLayout ll =  contentView.findViewById(R.id.ll1);//背景图
        LinearLayout fuqin =  contentView.findViewById(R.id.ll_fuqin);//父亲
        LinearLayout muqin =  contentView.findViewById(R.id.ll_muqin);//母亲
        LinearLayout peiou =  contentView.findViewById(R.id.ll_peiou);//配偶
        LinearLayout erzi =  contentView.findViewById(R.id.ll_erzi);//儿子
        LinearLayout nver =  contentView.findViewById(R.id.ll_nver);//女儿
        LinearLayout jiedi =  contentView.findViewById(R.id.ll_jiedi);//姐弟
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowAdd.dismiss();
            }
        });
        fuqin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", "父亲");
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                popupWindowAdd.dismiss();
            }
        });
        muqin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", "母亲");
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                popupWindowAdd.dismiss();
            }
        });
        peiou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", "配偶");
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                popupWindowAdd.dismiss();
            }
        });
        erzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", "儿子");
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                popupWindowAdd.dismiss();
            }
        });
        nver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", "女儿");
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                popupWindowAdd.dismiss();
            }
        });
        jiedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", "兄弟姐妹");
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                popupWindowAdd.dismiss();
            }
        });
        popupWindowAdd = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindowAdd.setOnDismissListener(new popupDismissListener());
        popupWindowAdd.setBackgroundDrawable(new BitmapDrawable());
        popupWindowAdd.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        // 设置好参数之后再show
        popupWindowAdd.showAsDropDown(view);
    }

    private void showPopupWindowEdit(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.activity_popupedit, null);
        // 设置按钮的点击事件
        RelativeLayout ll =  contentView.findViewById(R.id.ll1);//背景图
        TextView edit =  contentView.findViewById(R.id.edit);//编辑
        TextView delete =  contentView.findViewById(R.id.delete);//删除
        TextView cancel =  contentView.findViewById(R.id.cancel);//取消
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindowAdd != null) {
                    popupWindowAdd.dismiss();
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("title", "编辑信息");
                FastUtil.startActivity(mContext, PerfectingInformationActivity.class, bundle);
                popupWindowEdit.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhetherDelete();
                popupWindowEdit.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowEdit.dismiss();
            }
        });
        popupWindowEdit = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindowEdit.setOnDismissListener(new popupDismissListener());
        popupWindowEdit.setBackgroundDrawable(new BitmapDrawable());
        popupWindowEdit.setOutsideTouchable(true);
        // 设置好参数之后再show
        popupWindowEdit.showAsDropDown(view);
    }

    private void WhetherDelete() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(mContext);
        normalDialog.setTitle("您确定要删除嘛？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    //背景透明改变
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
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

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    contentShupuView.setCurrentItem(i);
                    Log.d("page", i + "");
                    return;
                }
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
}
