package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
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
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.my.BaseTResp2;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ToastManage;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/25 图片上传接口 一直报错 
public class PhotosDetailsActivity extends BaseTitleActivity implements onClickAlbumItem, View.OnClickListener {

    private Album album;
    private Button releasePhoto;
    private LinearLayout messageRelativeLayout;
    private String TAG = "PhotosDetailsActivity";
    private PhotosAdapter photosadapter;
    private int familyAlbum;
    private TextView tvDownload, tvDel, tvBatchManage, tvEditManage, tvUploadPhoto;
    private ConstraintLayout clBottom;
    private List<MyAlbum.AlbumsBean> mAlbums;
    private boolean mIsHide;
    private boolean mRefresh;
    private loadDataThread loadDataThread;

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
        clBottom = findViewById(R.id.cl_bottom);

        releasePhoto = findViewById(R.id.release_photo);
        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(PhotosDetailsActivity.this, 3));
        photosadapter = new PhotosAdapter(R.layout.item_photo_album);
        recyclerview.setAdapter(photosadapter);
        if (mAlbums.size() > 0) {
            clBottom.setVisibility(View.VISIBLE);
            photosadapter.setNewData(mAlbums);
        } else {
            clBottom.setVisibility(View.GONE);
        }
        photosadapter.setOnItemClickListener((adapter, view, position) -> {
            ArrayList<LocalMedia> medias = new ArrayList<>();
            LocalMedia media;
            for (MyAlbum.AlbumsBean bean : mAlbums) {
                media = new LocalMedia();
                media.setPath(bean.getUrl());
                medias.add(media);
            }
            PictureSelector.create(mContext)
                    .themeStyle(R.style.picture_default_style)
                    .openExternalPreview(position, medias);
        });
        messageRelativeLayout = findViewById(R.id.message);
        if (mAlbums.size() != 0) {
            recyclerview.setVisibility(View.VISIBLE);
            messageRelativeLayout.setVisibility(View.GONE);
        } else {
            recyclerview.setVisibility(View.GONE);
            messageRelativeLayout.setVisibility(View.VISIBLE);
        }
        //获取传来的参数
        Intent intent = getIntent();
        familyAlbum = Integer.parseInt(intent.getStringExtra("Id"));

        album = new Album();
        album.setText(intent.getStringExtra("Title"));
        album.setId(familyAlbum);
        album.setSrc(intent.getStringExtra("src"));
        album.setContent(intent.getStringExtra("content"));
        mTitleBar.setTitleMainText(album.getText());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + "" + resultCode);

        //获取图片路径
        switch (resultCode) {
            case RESULT_OK:
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.size() > 0) {
                    messageRelativeLayout.setVisibility(View.GONE);
                    MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    File mFile;
                    for (LocalMedia localMedia : selectList) {
                        mFile = new File(localMedia.getPath());
                        body.addFormDataPart("imgs", mFile.getName(), RequestBody.create(MediaType.parse("image/*"), mFile));
                    }
                    upLoadPic(body);
                }
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
                            Iterator<MyAlbum.AlbumsBean> iterator = mAlbums.iterator();
                            MyAlbum.AlbumsBean bean;
                            while (iterator.hasNext()) {
                                bean = iterator.next();
                                if (bean.isSelect()) {
                                    iterator.remove();
                                }
                            }
                            for (MyAlbum.AlbumsBean albumsBean : mAlbums) {
                                albumsBean.setSelect(false);
                            }
                            photosadapter.setNewData(mAlbums);
                            if (mAlbums.size() == 0) {
                                clBottom.setVisibility(View.GONE);
                            }
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
                        if (null != data.data) {
                            mRefresh = true;
                            if (photosadapter.getData().size() == 0) {
                                clBottom.setVisibility(View.VISIBLE);
                                photosadapter.setNewData(data.data);
                            } else {
                                photosadapter.addData(data.data);
                            }
                        }
                        ToastUtil.show(data.msg);
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
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
                        if (mRefresh) {
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
            case R.id.tv_download:/*下载图片*/
                showDownLoadDialog();
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
            case R.id.tv_upload_photo:
            case R.id.release_photo:
                photoAndCamera();
                break;
        }
    }


    private void showDownLoadDialog() {
        showLoading();
        String path;
        for (MyAlbum.AlbumsBean mAlbum : mAlbums) {
            if (mAlbum.isSelect()) {
                path = mAlbum.getUrl();
                Log.i(TAG, "showDownLoadDialog: " + path);
                boolean isHttp = PictureMimeType.isHttp(path);
                if (isHttp) {
                    loadDataThread = new loadDataThread(path);
                    loadDataThread.start();
                } else {
                    // 有可能本地图片
                    try {
                        String dirPath = PictureFileUtils.createDir(this,
                                System.currentTimeMillis() + ".png", getIntent().getStringExtra(PictureConfig.DIRECTORY_PATH));
                        PictureFileUtils.copyFile(path, dirPath);
                        Log.i(TAG, "showDownLoadDialog:下载成功 ");
                    } catch (IOException e) {
                        Log.i(TAG, "showDownLoadDialog:下载失败" + e.toString());
                        e.printStackTrace();
                    }
                }
            }
        }
        Log.i(TAG, "showDownLoadDialog: ");
        hideLoading();
        for (MyAlbum.AlbumsBean bean : mAlbums) {
            bean.setSelect(false);
        }
        photosadapter.notifyDataSetChanged();

        /*dialog.dismiss();*/
    }

    // 进度条线程
    public class loadDataThread extends Thread {
        private String path;

        public loadDataThread(String path) {
            super();
            this.path = path;
        }

        @Override
        public void run() {
            try {
                showLoadingImage(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 下载图片保存至手机
    public void showLoadingImage(String urlPath) {
        try {
            URL u = new URL(urlPath);
            String path = PictureFileUtils.createDir(this,
                    System.currentTimeMillis() + ".png", getIntent().getStringExtra(PictureConfig.DIRECTORY_PATH));
            byte[] buffer = new byte[1024 * 8];
            int read;
            int ava = 0;
            long start = System.currentTimeMillis();
            BufferedInputStream bin;
            bin = new BufferedInputStream(u.openStream());
            BufferedOutputStream bout = new BufferedOutputStream(
                    new FileOutputStream(path));
            while ((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
                ava += read;
                /*long speed = ava / (System.currentTimeMillis() - start);*/
            }
            bout.flush();
            bout.close();
            Message message = handler.obtainMessage();
            message.what = 200;
            message.obj = path;
            handler.sendMessage(message);
        } catch (IOException e) {
            ToastManage.s(mContext, getString(R.string.picture_save_error) + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    String path = (String) msg.obj;
                    ToastManage.s(mContext, getString(R.string.picture_save_success));
                    break;
            }
        }
    };

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