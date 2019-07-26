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
import com.genealogy.by.utils.my.BaseTResp2;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

// TODO: 2019/7/25 图片上传接口 一直报错 
public class PhotosDetailsActivity extends BaseTitleActivity implements onClickAlbumItem {

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

    @Override
    public void jumpActivity(Intent intent) {
        startActivityForResult(intent, 0);
    }

    public void initView() {
        photos = new ArrayList<>();
        album = new Album();
        releasePhoto = findViewById(R.id.release_photo);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(PhotosDetailsActivity.this, 3));
        photosadapter = new PhotosAdapter(R.layout.item_photo_album);
        recyclerview.setAdapter(photosadapter);
        photosadapter.setNewData(list);
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
        }
    }

    private void upLoadPic(MultipartBody.Builder body) {
        body.addFormDataPart("aId", String.valueOf(familyAlbum));
        ViseHttp.POST(ApiConstant.album_uploadImgs)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .setRequestBody(body.build())
                .request(new ACallback<BaseTResp2<List<MyAlbum.AlbumsBean>>>() {
                    @Override
                    public void onSuccess(BaseTResp2<List<MyAlbum.AlbumsBean>> data) {
                        if (data.isSuccess()) {
                            Log.e(TAG, "onSuccess: 提交成功" + data.msg);
                            // TODO: 2019/7/26 图片上传成功需要刷新
                            Log.i(TAG, "onSuccess: " + data.data);
                        } else {
                            Log.e(TAG, "onSuccess: 提交失败" + data.msg);
                        }
                        ToastUtil.show(data.msg);
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
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
        titleBar.setRightText("上传").setDividerVisible(true).setOnRightTextClickListener(v -> photoAndCamera());
    }

    @Override
    public int getContentLayout() {
        return R.layout.photos_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        list = intent.getStringArrayListExtra("Urls");
        initView();
    }

    //上传图片
//    private void upLoadPic(final String url, final int position) {
//        Log.i(TAG, "upLoadPic: " + familyAlbum);
//        file = new File(url);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("imgs", url, RequestBody.create(MediaType.parse("image/*"), file))
//                .addFormDataPart("aId", String.valueOf(familyAlbum))
//                .build();
//        ViseHttp.POST(ApiConstant.album_uploadImgs)
//                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
//                .setRequestBody(requestBody)
//                .request(new ACallback<BaseTResp2>() {
//                    @Override
//                    public void onSuccess(BaseTResp2 data) {
//                        if (data.isSuccess()) {
//                            Log.e(TAG, "onSuccess: 提交成功" + data.msg);
//                        } else {
//                            Log.e(TAG, "onSuccess: 提交失败" + data.msg);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(int errCode, String errMsg) {
//                        ToastUtil.show("请求失败: " + errMsg + "，errCode: " + errCode);
//                    }
//                });
//    }
}