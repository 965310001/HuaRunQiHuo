package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.PhotosAdapter;
import com.genealogy.by.adapter.onClickAlbumItem;
import com.genealogy.by.entity.Album;
import com.genealogy.by.entity.MyAlbum;
import com.genealogy.by.entity.Photo;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/25 图片上传接口 一直报错 
public class PhotosDetailsActivity extends BaseTitleActivity implements onClickAlbumItem, View.OnClickListener {

    private TextView managerAlbum;

    private Button releasePhoto;
    private TextView deleteAlbum;
    private Album album;
    private LinearLayout messageRelativeLayout;

    private String TAG = "PhotosDetailsActivity";
    private ArrayList<Photo> photos;
    private RecyclerView recyclerview;
    private PhotosAdapter photosadapter;
    private List<String> list;
    private int familyAlbum = 0;


    private TextView tvDownload, tvDel, tvBatchManage, tvEditManage, tvUploadPhoto;

    private List<MyAlbum.AlbumsBean> beans;
    private List<MyAlbum.AlbumsBean> mAlbums;
    private boolean mIsHide;

    @Override
    public void jumpActivity(Intent intent) {
        startActivityForResult(intent, 0);
    }

    public void initView() {
        tvDownload = findViewById(R.id.tv_download);
        tvDel = findViewById(R.id.tv_delete);
        tvBatchManage = findViewById(R.id.tv_batch_manage);
        tvEditManage = findViewById(R.id.tv_edit_manage);
        tvUploadPhoto = findViewById(R.id.tv_upload_photo);

        photos = new ArrayList<>();
        album = new Album();
        releasePhoto = findViewById(R.id.release_photo);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(PhotosDetailsActivity.this, 3));
        photosadapter = new PhotosAdapter(R.layout.item_photo_album);
        recyclerview.setAdapter(photosadapter);
        if (mAlbums.size() > 0) {
            photosadapter.setNewData(mAlbums);
        }
        photosadapter.setOnItemClickListener((adapter, view, position) -> {
            ArrayList<LocalMedia> medias = new ArrayList<>();
            LocalMedia media;
            for (String s : list) {
                media = new LocalMedia();
                media.setPath(s);
                medias.add(media);
            }
            PictureSelector.create(mContext)
                    .themeStyle(R.style.picture_default_style)
                    .openExternalPreview(position, medias);
        });
        messageRelativeLayout = findViewById(R.id.message);
        if (list.size() != 0) {
            recyclerview.setVisibility(View.VISIBLE);
            messageRelativeLayout.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.GONE);
            messageRelativeLayout.setVisibility(View.VISIBLE);
        }
        //获取传来的参数
        Intent intent = getIntent();
        familyAlbum = Integer.parseInt(intent.getStringExtra("Id"));
        album.setText(intent.getStringExtra("Title"));
        album.setId(Integer.valueOf(intent.getStringExtra("Id")));
        album.setSrc(intent.getStringExtra("src"));
        album.setContent(intent.getStringExtra("content"));
        //下拉刷新
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                toolUtil.dip2px(this, 40), toolUtil.dip2px(this, 40));
        mTitleBar.setTitleMainText(album.getText());
        //发布照片
        releasePhoto.setOnClickListener(v ->
                //图片选择器
                photoAndCamera()
        );

        beans = new ArrayList<>();
    }

    //Intent  回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + "" + resultCode);

        //获取图片路径
        switch (resultCode) {
            case RESULT_OK:
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                //相册集 等于0
                if (photos.size() == 0 && selectList.size() > 0) {
                    messageRelativeLayout.setVisibility(View.GONE);
                    MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    File mFile;
                    for (LocalMedia localMedia : selectList) {
                        mFile = new File(localMedia.getPath());
                        body.addFormDataPart("imgs", mFile.getName(), RequestBody.create(MediaType.parse("image/*"), mFile));
                    }
                    upLoadPic(body);
                }
               /* for (int i = 0; i < selectList.size(); i++) {
                    upLoadPic(selectList.get(i).getPath(), 0);
                }*/
                break;
            case 200:
                //编辑设置标题
                mTitleBar.setTitleMainText(data.getStringExtra("photoTitle"));
                break;


            case 202:
                String title = data.getStringExtra("title");
                album.setText(title);
                mTitleBar.setTitleMainText(title);
                break;
        }
    }

    /*删除相册*/
    private void delImg(String ids) {
        showLoading();
        HashMap<String, String> params = new HashMap<>();
        params.put("ids", ids);
        ViseHttp.POST(ApiConstant.album_delImgs)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .setJson(new JSONObject(params))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        hideLoading();
                        if (data.isSuccess()) {
                            SPHelper.setBooleanSF(mContext, "REFRESH_PHOTO", true);
                            String[] strings = ids.split(",");
                            for (String string : strings) {
                                for (MyAlbum.AlbumsBean bean : photosadapter.getData()) {
                                    if (bean.getUrl().equals(string)) {
                                        photosadapter.getData().remove(bean);
                                    }
                                }
                            }
                            Log.i(TAG, "onSuccess: 你好");
                            photosadapter.notifyDataSetChanged();
                        }
                        ToastUtil.show(data.msg);
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                        hideLoading();
                    }
                });
    }

    private void upLoadPic(MultipartBody.Builder body) {
        showLoading();
        body.addFormDataPart("aId", String.valueOf(familyAlbum));
        ViseHttp.POST(ApiConstant.album_uploadImgs)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .setRequestBody(body.build())
                .request(new ACallback<BaseTResp2<List<MyAlbum.AlbumsBean>>>() {
                    @Override
                    public void onSuccess(BaseTResp2<List<MyAlbum.AlbumsBean>> data) {
                        hideLoading();
//                        if (data.isSuccess()) {
//                            Log.e(TAG, "onSuccess: 提交成功" + data.msg);
//                            Log.i(TAG, "onSuccess: " + data.data);
//                        } else {
//                            Log.e(TAG, "onSuccess: 提交失败" + data.msg);
//                        }
//                        photosadapter.addData(data.data);
                        Log.i(TAG, "onSuccess: " + photosadapter.getData().size());

                        if (null != data.data) {
                            beans.addAll(data.data);
                            if (photosadapter.getData().size() == 0) {
                              /*  for (MyAlbum.AlbumsBean bean : data.data) {
                                    list.add(bean.getUrl());
                                }*/
                                photosadapter.setNewData(data.data);
                            } else {
                               /* for (MyAlbum.AlbumsBean bean : data.data) {
                                }*/
                                photosadapter.addData(data.data);
                            }
                        }

                        ToastUtil.show(data.msg);
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        Log.i(TAG, "onFail: " + errMsg + " " + errCode);
                        ToastUtil.show("请求失败: " + errMsg + "，errCode: " + errCode);
                    }
                });
    }

    //启动相册、拍照选择器
    private void photoAndCamera() {
        PictureSelector.create(PhotosDetailsActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(9)// 最大图片选择数量 int
                .theme(R.style.picture_default_style)
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)// 是否可预览图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setRightText("上传")
                .setOnLeftTextClickListener(v -> {
                    if (mIsHide) {
                        mIsHide = !mIsHide;
                        hideBottom(mIsHide);

                        photosadapter.setCheck(false);
                        photosadapter.notifyDataSetChanged();
                    } else {
                        if (beans.size() > 0) {
                            SPHelper.setBooleanSF(mContext, "REFRESH_PHOTO", true);
                        }
                        finish();
                    }

                }).setDividerVisible(true).setOnRightTextClickListener(v -> photoAndCamera());
    }

    @Override
    public int getContentLayout() {
        return R.layout.photos_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("Urls");
        mAlbums = (List<MyAlbum.AlbumsBean>) intent.getSerializableExtra("data");
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_batch_manage:/*批量管理*/
                hideBottom(true);
                photosadapter.setCheck(true);
                photosadapter.notifyDataSetChanged();
                break;
            case R.id.tv_edit_manage:/*编辑相册*/
                Intent intent = new Intent(mContext, PhotosEditActivity.class);
                intent.putExtra("data", album);
                jumpActivity(intent);
                break;
            case R.id.tv_upload_photo:
                photoAndCamera();
                break;
            case R.id.tv_download:/*下载图片*/
                break;

            case R.id.tv_delete:/*删除图片*/
                StringBuilder sb = new StringBuilder();
                for (MyAlbum.AlbumsBean bean : mAlbums) {
                    if (bean.isSelect()) {
                        sb.append(bean.getId()).append(",");
                    }
                }
                if (sb.length() > 0) {
                    delImg(sb.toString());
                } else {
                    ToastUtil.show("请选择删除的图片");
                }

                break;
        }
    }

    private void hideBottom(boolean isHide) {
        if (isHide) {
            tvBatchManage.setVisibility(View.GONE);
            tvEditManage.setVisibility(View.GONE);
            tvUploadPhoto.setVisibility(View.GONE);

            tvDownload.setVisibility(View.VISIBLE);
            tvDel.setVisibility(View.VISIBLE);
        } else {
            tvBatchManage.setVisibility(View.VISIBLE);
            tvEditManage.setVisibility(View.VISIBLE);
            tvUploadPhoto.setVisibility(View.VISIBLE);

            tvDownload.setVisibility(View.GONE);
            tvDel.setVisibility(View.GONE);
        }

        this.mIsHide = isHide;

    }


}