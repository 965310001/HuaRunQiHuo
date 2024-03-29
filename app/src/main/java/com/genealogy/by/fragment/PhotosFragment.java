package com.genealogy.by.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.activity.PhotosAddActivity;
import com.genealogy.by.activity.PhotosDetailsActivity;
import com.genealogy.by.adapter.AlbumAdapter;
import com.genealogy.by.adapter.onClickAlbumItem;
import com.genealogy.by.entity.MyAlbum;
import com.genealogy.by.utils.DisplayUtil;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.githang.statusbar.StatusBarCompat;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tech.com.commoncore.base.BaseTitleFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

public class PhotosFragment extends BaseTitleFragment implements onClickAlbumItem, View.OnClickListener {
    private String TAG = "PhotosFragment";
    private Button addPhotos;
    private AppCompatImageView addImage;
    private LinearLayout messageRelativeLayout;
    private ArrayList<MyAlbum> albums;
    private AlbumAdapter albumadapter, albumadapter1;
    private RecyclerView layout;
    private RadioGroup mPhotosRadio;

    @Override
    public void jumpActivity(Intent intent) {
        startActivityForResult(intent, 0);
    }

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SPHelper.getBooleanSF(mContext, "REFRESH_PHOTO", false)) {
            doit();
            SPHelper.setBooleanSF(mContext, "REFRESH_PHOTO", false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //200 添加图库相册集
        //201 删除图库相册集
        if (requestCode == 0) {
            switch (resultCode) {
                case 200:
                    //相册集 等于0
                    if (albums.size() == 0) {
                        messageRelativeLayout.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                    }
                    MyAlbum album = new MyAlbum();
                    albums.add(album);
                    //更新list view视图
                    break;
                case 201:
                    int id = Integer.valueOf(data.getStringExtra("id"));
                    for (int i = 0; i < albums.size(); i++) {
                        if (albums.get(i).getId() == id) {
                            albums.remove(i);
                        }
                    }
                    //相册集没了显示 提示信息
                    if (albums.size() == 0) {
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                    }
                    //更新list view视图
                    break;

                case 202:
                    /*data.getStringExtra("id");
                    Serializable data1 = data.getSerializableExtra("data");*/
                    doit();
                    break;
            }
        }
    }

    public void doit() {
        if (!isAllFamily) {
            layout.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            layout.setAdapter(albumadapter);
            HashMap<String, String> params = new HashMap<>();
            params.put("userId", SPHelper.getStringSF(getContext(), "UserId", ""));
            Log.e(TAG, "execute: 参数：" + params.toString());
            ViseHttp.POST(ApiConstant.album_searchMyAlbum)
                    .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                    .cacheMode(CacheMode.FIRST_REMOTE)
                    .setJson(new JSONObject(params))
                    .request(new ACallback<BaseTResp2<List<MyAlbum>>>() {
                        @Override
                        public void onSuccess(BaseTResp2<List<MyAlbum>> data) {
                            if (data.isSuccess()) {

                                List<MyAlbum> myAlbums = data.data;
                                Log.e(TAG, "onSuccess: 我的相册查询请求成功  msg= " + data.msg);
                                if (myAlbums.size() == 0) {
                                    layout.setVisibility(View.GONE);
                                    messageRelativeLayout.setVisibility(View.VISIBLE);
                                } else {
                                    layout.setVisibility(View.VISIBLE);
                                    messageRelativeLayout.setVisibility(View.GONE);
                                }
                                for (int i = 0; i < myAlbums.size(); i++) {
                                    albumadapter.setNewData(myAlbums);
                                    albumadapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.e(TAG, "onSuccess: 我的相册查询请求成功  msg= " + data.msg);
                                ToastUtil.show("请求失败 " + data.msg);
                            }
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show("失败: " + errMsg);
                            Log.e(TAG, "errMsg: " + errMsg + "errCode:  " + errCode);
                        }
                    });
        } else {
            layout.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            layout.setAdapter(albumadapter1);
            HashMap<String, String> params = new HashMap<>();
            params.put("gId", SPHelper.getStringSF(mContext, "GId", " "));//Gid
            ViseHttp.POST(ApiConstant.album_searchFamilyAlbum)
                    .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                    .cacheMode(CacheMode.FIRST_REMOTE)
                    .setJson(new JSONObject(params))
                    .request(new ACallback<BaseTResp2<List<MyAlbum>>>() {
                        @Override
                        public void onSuccess(BaseTResp2<List<MyAlbum>> data) {
                            if (data.isSuccess()) {
                                List<MyAlbum> myAlbums = data.data;
                                Log.e(TAG, "onSuccess: 我的相册查询请求成功  msg= " + data.msg);
                                if (myAlbums.size() == 0) {
                                    layout.setVisibility(View.GONE);
                                    messageRelativeLayout.setVisibility(View.VISIBLE);
                                } else {
                                    layout.setVisibility(View.VISIBLE);
                                    messageRelativeLayout.setVisibility(View.GONE);
                                }
                                for (int i = 0; i < myAlbums.size(); i++) {
                                    albumadapter1.setNewData(myAlbums);
                                    albumadapter1.notifyDataSetChanged();
                                }
                            } else {
                                Log.e(TAG, "onSuccess: 我的相册查询请求成功  msg= " + data.msg);
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

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.photos;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#464854"), false);
        mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
        albumadapter = new AlbumAdapter(R.layout.item_photo_album);
        albumadapter.setOnItemChildClickListener((adapter, view, position) -> {
            MyAlbum item = (MyAlbum) adapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("Id", String.valueOf(item.getId()));
            bundle.putString("Title", item.getTitle());
            bundle.putString("IsTrue", item.getIsTrue());
            bundle.putSerializable("data", (Serializable) item.getAlbums());
            FastUtil.startActivity(mContext, PhotosDetailsActivity.class, bundle);
        });

        albumadapter1 = new AlbumAdapter(R.layout.item_photo_album);
        albumadapter1.setOnItemChildClickListener((adapter, view, position) -> {
            MyAlbum item = (MyAlbum) adapter.getData().get(position);
            Bundle bundle = new Bundle();
            bundle.putString("Id", String.valueOf(item.getId()));
            bundle.putString("Title", item.getTitle());
            bundle.putString("IsTrue", item.getIsTrue());
            bundle.putSerializable("data", (Serializable) item.getAlbums());
            bundle.putBoolean("HIDE", true);
            FastUtil.startActivity(mContext, PhotosDetailsActivity.class, bundle);
        });
        albums = new ArrayList<>();
        addImage = mContentView.findViewById(R.id.add_image);
        addPhotos = mContentView.findViewById(R.id.add_photos);
        messageRelativeLayout = mContentView.findViewById(R.id.message);
        layout = mContentView.findViewById(R.id.layout);
        mPhotosRadio = mContentView.findViewById(R.id.photos_radio);
        if (albums.size() > 0) {
            messageRelativeLayout.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
        layout.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        layout.setAdapter(albumadapter);
        mPhotosRadio.setOnCheckedChangeListener((radioGroup, i) -> {
            // TODO: 2019/7/22 接口调试  添加下拉刷新
            if (i == R.id.family) {
                isAllFamily = false;
                Log.i(TAG, "onActivityCreated: ");
            } else if (i == R.id.all_family) {
                Log.i(TAG, "onActivityCreated: ");
                isAllFamily = true;

            }

            doit();
        });

        addImage.setOnClickListener(this);
        addPhotos.setOnClickListener(this);

        doit();
    }

    private boolean isAllFamily;

    @Override
    public void onClick(View v) {
        jumpActivity(new Intent(getActivity(), PhotosAddActivity.class));
    }

}