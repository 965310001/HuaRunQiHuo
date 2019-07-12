package com.genealogy.by.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.genealogy.by.R;
import com.genealogy.by.activity.EditContentActivity;
import com.genealogy.by.activity.EditCoverActivity;
import com.genealogy.by.activity.ReleasePictureActivity;
import com.genealogy.by.activity.SearchMainActivity;
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseApplication;
import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

public class TabZuCeFragment extends BaseFragment {

    //日志提示
    private static final String TAG = "TabZuCeFragment";
    //viewpage管理
    private ViewPager mViewPager;
    //搜索 其他按钮
    private TextView searchButton;
    private TextView aboutButton;
    //popwindow
    private TextView homeTextView;
    private TextView editCoverTextView;
    private TextView exportCoverTextView;
    PopupWindow popupWindowEdit;
    //传送参数
    private int curUpdatePager;
    private final ArrayList<View> views = new ArrayList<View>();
    //封面参数
    private String name, person, time, address;
    //水平滚动条
    private SeekBar seekBar;

    //标题 编辑点击
    private TextView title;
    private ImageView editText;
    private Intent intent;
    private FamilyBook familyBook;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
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
        mViewPager = (ViewPager) mContentView.findViewById(R.id.contentView);
        //搜索 其他
        searchButton = mContentView.findViewById(R.id.zuce_search);
        aboutButton = mContentView.findViewById(R.id.zuce_about);
        //水平滚动条
        seekBar = (SeekBar) mContentView.findViewById(R.id.seekBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从fragment跳转到activity中
                startActivity(new Intent(getActivity(), SearchMainActivity.class));
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
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
                homeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭弹窗
                        popupWindow.dismiss();
                        //返回首页
                        if (mViewPager.getCurrentItem() != 0) {
                            mViewPager.setCurrentItem(0);
                        }

                    }
                });

                editCoverTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭弹窗
                        popupWindow.dismiss();
                        Intent i = new Intent(getActivity(), EditCoverActivity.class);
                        String str = String.valueOf(familyBook.getId());
                        i.putExtra("ID", str);
                        i.putExtra("name", name);
                        i.putExtra("person", person);
                        i.putExtra("time", time);
                        i.putExtra("address", address);
                        startActivityForResult(i, 0);
                    }
                });

            }
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
                    updateViewPagerItem(view, 0);
                    break;
                case 201:
                    String content = data.getStringExtra("content");
                    // = Integer.valueOf(data.getStringExtra("index"));
                    int index = mViewPager.getCurrentItem();
                    View v = views.get(index);
                    TextView contentTextView = v.findViewById(R.id.content);
                    contentTextView.setText(content);

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
        exportCoverTextView = view.findViewById(R.id.export_cover);
    }


    boolean isFamilyBook() {
        return null != familyBook;
    }

    public void initFuxiViewPager() {
        //将要分页显示的View装入数组中
        //每个页面的Title数据
        LayoutInflater mLi = LayoutInflater.from(mContext);
        View view = mLi.inflate(R.layout.zuce_cover, null);
        TextView titlese = view.findViewById(R.id.title);
        if (isFamilyBook() && !familyBook.getFamilybookName().isEmpty()) {
            titlese.setText(familyBook.getFamilybookName());
        }
        TextView person = view.findViewById(R.id.person);
        if (isFamilyBook() && !familyBook.getSponsor().isEmpty()) {
            person.setText(familyBook.getSponsor());
        }
        TextView time = view.findViewById(R.id.time);
        if (isFamilyBook() && !familyBook.getEditingTimeCN().isEmpty()) {
            time.setText(familyBook.getEditingTimeCN());
        }
        views.add(view);
        view = mLi.inflate(R.layout.zuce_directory, null);
        TextView committee = view.findViewById(R.id.committee);
        TextView preface = view.findViewById(R.id.preface);
        TextView source = view.findViewById(R.id.source);
        TextView instruction = view.findViewById(R.id.instruction);
        TextView photo = view.findViewById(R.id.photo);
        TextView surface = view.findViewById(R.id.surface);
        TextView events = view.findViewById(R.id.events);
        TextView epilogue = view.findViewById(R.id.epilogue);
        committee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新页面
                updateViewPagerItem(view, 0);
            }
        });
        preface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新页面
                updateViewPagerItem(view, 0);
            }
        });
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新页面
                updateViewPagerItem(view, 0);
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新页面
                updateViewPagerItem(view, 0);
            }
        });
        surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新页面
                updateViewPagerItem(view, 0);
            }
        });
        epilogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新页面
                updateViewPagerItem(view, 0);
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新页面
                updateViewPagerItem(view, 0);
            }
        });
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
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_bwh, null);
                String str = String.valueOf(familyBook.getId());
                intent.putExtra("title", editorialtitle.getText().toString());
                intent.putExtra("index", mViewPager.getCurrentItem() + "");
                intent.putExtra("id", str);
                intent.setClass(getActivity(), EditContentActivity.class);
                startActivityForResult(intent, 0);
            }
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
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_jzxy, null);
                String str = String.valueOf(familyBook.getId());
                intent.putExtra("id", str);
                intent.putExtra("title", prefacetitle.getText().toString());
                intent.putExtra("index", mViewPager.getCurrentItem() + "");
                intent.setClass(getActivity(), EditContentActivity.class);
                startActivityForResult(intent, 0);
            }
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
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_xsly, null);
                String str = String.valueOf(familyBook.getId());
                intent.putExtra("title", sourcetitle.getText().toString());
                intent.putExtra("id", str);
                intent.putExtra("index", mViewPager.getCurrentItem() + "");
                intent.setClass(getActivity(), EditContentActivity.class);
                startActivityForResult(intent, 0);
            }
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
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_jgjx, null);
                String str = String.valueOf(familyBook.getId());
                intent.putExtra("title", rulestitle.getText().toString());
                intent.putExtra("id", str);
                intent.putExtra("index", mViewPager.getCurrentItem() + "");
                intent.setClass(getActivity(), EditContentActivity.class);
                startActivityForResult(intent, 0);
            }
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
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_person, null);
                String str = String.valueOf(familyBook.getId());
                intent.putExtra("title", charactertitle.getText().toString());
                intent.putExtra("id", str);
                intent.putExtra("index", mViewPager.getCurrentItem() + "");
                intent.setClass(getActivity(), EditContentActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        /*大事记*/
        view = mLi.inflate(R.layout.zuce_bignote, null);
        views.add(view);
        editText = view.findViewById(R.id.editText);
        TextView bignotetitle = view.findViewById(R.id.bignotetitle);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_bignote, null);
                String str = String.valueOf(familyBook.getId());
                intent.putExtra("title", bignotetitle.getText().toString());
                intent.putExtra("id", str);
                intent.putExtra("index", mViewPager.getCurrentItem() + "");
                intent.setClass(getActivity(), EditContentActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        view = mLi.inflate(R.layout.zuce_postscript, null);
        views.add(view);
        editText = view.findViewById(R.id.editText);
        TextView epiloguetitle = view.findViewById(R.id.epiloguetitle);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.zuce_postscript, null);
                String str = String.valueOf(familyBook.getId());
                intent.putExtra("title", epiloguetitle.getText().toString());
                intent.putExtra("id", str);
                intent.putExtra("index", mViewPager.getCurrentItem() + "");
                intent.putExtra("index", familyBook.getId() + "");
                intent.setClass(getActivity(), EditContentActivity.class);
                startActivityForResult(intent, 0);
            }
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
        views.remove(index);
        views.add(index, view);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public void doit() {
        String userId = SPHelper.getStringSF(mContext, "UserId", "");
        String gid = SPHelper.getStringSF(mContext, "GId", "");
        HashMap<String, String> params = new HashMap<>();
        params.put("gId", gid);
        params.put("userId", userId);
        Log.e(TAG, "doit: 参数：" + params.toString());
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS = MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.familyBook)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS, jsonObject.toString()))
                .request(new ACallback<BaseTResp2<FamilyBook>>() {
                    @Override
                    public void onSuccess(BaseTResp2<FamilyBook> data) {
                        if (data.status == 200) {
                            Log.e(TAG, "onSuccess: 族册查询请求成功  msg= " + data.msg);
                            familyBook = data.data;
                            initFuxiViewPager();
//                            SPHelper.saveDeviceData(mContext, "familyBook", familyBook);
                            SPHelper.saveDeviceData(BaseApplication.getInstance(), "familyBook", familyBook);
                        } else {
                            Log.e(TAG, "onSuccess: 族册查询请求失败  msg= " + data.msg);
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

    public void doOnClick(View view) {
        TextView image1 = view.findViewById(R.id.image1);
        image1.setOnClickListener(view1 -> FastUtil.startActivity(getContext(), ReleasePictureActivity.class));
        TextView image2 = view.findViewById(R.id.image2);
        image2.setOnClickListener(view12 -> FastUtil.startActivity(getContext(), ReleasePictureActivity.class));
        TextView image3 = view.findViewById(R.id.image3);
        image3.setOnClickListener(view13 -> FastUtil.startActivity(getContext(), ReleasePictureActivity.class));
        TextView image4 = view.findViewById(R.id.image4);
        image4.setOnClickListener(view14 -> FastUtil.startActivity(getContext(), ReleasePictureActivity.class));
    }

    public void dosetdata(View view, int idx1, int idx2, int idx3, int idx4) {
        TextView text1 = view.findViewById(R.id.text1);
        TextView text2 = view.findViewById(R.id.text2);
        TextView text3 = view.findViewById(R.id.text3);
        TextView text4 = view.findViewById(R.id.text4);
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
        if (familyBook.getFamilyPhoto().size() > idx1) {
            String url_1 = familyBook.getFamilyPhoto().get(idx1).getUrl();
            if (familyBook.getFamilyPhoto().get(idx1).getIntroduction() != null) {
//                text1.setText(familyBook.getFamilyPhoto().get(idx1).getIntroduction());
            }
            image1.setVisibility(View.GONE);
            tv_1.setVisibility(View.GONE);
            iv_1.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_1);
            iv_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 1, familyBook.getFamilyPhoto().get(4).getId());
                }
            });
        } else {
            image1.setVisibility(View.VISIBLE);
            tv_1.setVisibility(View.VISIBLE);
            iv_1.setVisibility(View.GONE);
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 0, 0);
                }
            });
        }
        if (familyBook.getFamilyPhoto().size() > idx2) {
            String url_1 = familyBook.getFamilyPhoto().get(idx2).getUrl();
            if (familyBook.getFamilyPhoto().get(idx1).getIntroduction() != null) {
//                text2.setText(familyBook.getFamilyPhoto().get(idx1).getIntroduction());
            }
            image2.setVisibility(View.GONE);
            tv_2.setVisibility(View.GONE);
            iv_2.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_2);
            iv_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 1, familyBook.getFamilyPhoto().get(idx2).getId());
                }
            });
        } else {
            image2.setVisibility(View.VISIBLE);
            tv_2.setVisibility(View.VISIBLE);
            iv_2.setVisibility(View.GONE);
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 0, 0);
                }
            });
        }
        if (familyBook.getFamilyPhoto().size() > idx3) {
            String url_1 = familyBook.getFamilyPhoto().get(idx3).getUrl();
            if (familyBook.getFamilyPhoto().get(idx1).getIntroduction() != null) {
//                text3.setText(familyBook.getFamilyPhoto().get(idx1).getIntroduction());
            }
            image3.setVisibility(View.GONE);
            tv_3.setVisibility(View.GONE);
            iv_3.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_3);
            iv_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 1, familyBook.getFamilyPhoto().get(idx3).getId());
                }
            });
        } else {
            image3.setVisibility(View.VISIBLE);
            tv_3.setVisibility(View.VISIBLE);
            iv_3.setVisibility(View.GONE);
            image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 0, 0);
                }
            });
        }
        if (familyBook.getFamilyPhoto().size() > idx4) {
            String url_1 = familyBook.getFamilyPhoto().get(idx4).getUrl();
            if (familyBook.getFamilyPhoto().get(idx1).getIntroduction() != null) {
//                text4.setText(familyBook.getFamilyPhoto().get(idx1).getIntroduction());
            }
            image4.setVisibility(View.GONE);
            tv_4.setVisibility(View.GONE);
            iv_4.setVisibility(View.VISIBLE);
            GlideManager.loadImg(url_1, iv_4);
            iv_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 1, familyBook.getFamilyPhoto().get(idx4).getId());
                }
            });
        } else {
            image1.setVisibility(View.VISIBLE);
            tv_4.setVisibility(View.VISIBLE);
            iv_4.setVisibility(View.GONE);
            image4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindowEdit(view, 0, 0);
                }
            });
        }
    }

    private void showPopupWindowEdit(View view, int aog, int id) {
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
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowEdit.dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", "上传");
                FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
            }
        });
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("type", "录入");
                bundle.putSerializable("id", id);
                FastUtil.startActivity(mContext, ReleasePictureActivity.class, bundle);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDoit(id);
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
}
