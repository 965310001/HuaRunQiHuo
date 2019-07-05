package com.genealogy.by.utils.my;

import android.Manifest;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.genealogy.by.R;
import com.vise.xsnow.permission.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.ScreenUtils;
import tech.com.commoncore.utils.ToastUtil;

public class PictureSelectorHelper {
    private final String SELECT_TYPE = "select_type";
    private RecyclerView mRecyclerView;
    private Activity mActivity;
    private ArrayList<String> mList = new ArrayList<>();
    private PictrueAdapter mPictrueAdapter;
    private int maxCount = 6;

    private int image_height;
    private int image_padding;//图片间距

    public PictureSelectorHelper(Activity activity, RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mActivity = activity;
        GridLayoutManager mgr = new GridLayoutManager(mRecyclerView.getContext(), 4);
        mRecyclerView.setLayoutManager(mgr);
        mRecyclerView.setAdapter(mPictrueAdapter = new PictrueAdapter(R.layout.item_photo_selector));

        addSelectType();
        mPictrueAdapter.replaceData(mList);
        image_height = (ScreenUtils.getScreenWidth(activity)) / 4;
        image_padding = image_height / 10;
    }

    /**
     * 添加选择入口 在末尾 不超过最大值
     */
    private void addSelectType() {
        if (mList.contains(SELECT_TYPE)) {
            mList.remove(SELECT_TYPE);
        }
        if (mList.size() < maxCount) {
            mList.add(SELECT_TYPE);
        }

    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public List<String> getPictureList() {
        List<String> list = (List<String>) mList.clone();
        list.remove(SELECT_TYPE);
        return list;
    }


    public void setActivityResult(ArrayList<String> stringList) {
        if (stringList == null) {
            return;
        }
        if (mList.contains(SELECT_TYPE)) {
            mList.remove(SELECT_TYPE);
        }
//        mList.clear();
        mList.addAll(stringList);
        addSelectType();


        mPictrueAdapter.replaceData(mList);
        mPictrueAdapter.notifyDataSetChanged();

    }

    private class PictrueAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public PictrueAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {

            ImageView iv = helper.getView(R.id.iv_picture);
            ImageView iv_delete = helper.getView(R.id.iv_delete);
            if (SELECT_TYPE.equals(item)) {
                iv_delete.setVisibility(View.INVISIBLE);
            } else {
                iv_delete.setVisibility(View.VISIBLE);
            }
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(item);
                    addSelectType();
                    mPictrueAdapter.replaceData(mList);
                    mPictrueAdapter.notifyDataSetChanged();
                }
            });

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getPictureList().size() >= 2) {
                        ToastUtil.show("最多只能选择2张图片");
                        return;
                    }

                    requestPermissions();
                }
            });
            iv.setLayoutParams(new RelativeLayout.LayoutParams(image_height, image_height));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setPadding(image_padding, image_padding, image_padding, image_padding);
            if (SELECT_TYPE.equals(item)) {
                GlideManager.loadRoundImg(R.mipmap.icon_add, iv);
            } else {
                GlideManager.loadRoundImg(item, iv);
            }

        }

    }

    private ArrayList<String> getSelectList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            String s = mList.get(i);
            if (!SELECT_TYPE.equals(s)) {
                list.add(s);
            }
        }
        return list;
    }


    void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(mActivity);
        rxPermission
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                        // 用户已经同意全部权限
                        selectePhoto();
                    } else {
                        // At least one permission is denied
                    }
                });

    }

    void selectePhoto() {
        Matisse.from(mActivity)
                .choose(MimeType.ofImage())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(2 - getPictureList().size())//可选的最大数
                .capture(true)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.gc.qh.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new MyGlideEngine())//图片加载引擎
                .theme(/*R.style.MyMatisse_Dracula*/  R.style.Matisse_Dracula)
                .forResult(0);//
    }


}
