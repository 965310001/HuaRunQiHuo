package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.PhotosAdapter;
import com.genealogy.by.adapter.onClickAlbumItem;
import com.genealogy.by.entity.Album;
import com.genealogy.by.entity.Photo;
import com.genealogy.by.utils.my.BaseTResp2;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

public class PhotosDetailsActivity extends BaseTitleActivity implements onClickAlbumItem {

    private ImageView back;
    //    private ToolUtil toolUtil;
    //    private TextView title;
    private ImageView gallery;
    private Button releasePhoto;
    private TextView photosAbout;
    private TextView managerAlbum;
    private TextView deleteAlbum;
    private Album album;
    private LinearLayout messageRelativeLayout;
    //    private ListView albumsDetailsLv;
//    private AlbumDetailsAdapter adapter;
    //    private BitmapCut bitmapCut;
    private String TAG = "PhotosDetailsActivity";
    private ArrayList<Photo> photos;
    private RecyclerView recyclerview;
    private PhotosAdapter photosadapter;
    private List<String> list;
    private File file;// = new File("");
    int familyAlbum = 0;

    @Override
    public void jumpActivity(Intent intent) {
        startActivityForResult(intent, 0);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
//        list = intent.getStringArrayListExtra("Urls");
//        setContentView(R.layout.photos_details);
//        Intent intent = getIntent();
//        list = intent.getStringArrayListExtra("Urls");
//        initView();
//        StatusBarCompat.setStatusBarColor(this, toolUtil.hex2Int("#f5f5f5"));
//    }

    public void initData() {
    }

    public void initView() {
//        toolUtil = new ToolUtil();
//        bitmapCut = new BitmapCut();
//        title = findViewById(R.id.title);

        photos = new ArrayList<>();
        album = new Album();
        back = findViewById(R.id.back);
        gallery = findViewById(R.id.gallery);
        releasePhoto = findViewById(R.id.release_photo);
        photosAbout = findViewById(R.id.photos_about);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new GridLayoutManager(PhotosDetailsActivity.this, 3));
        photosadapter = new PhotosAdapter(R.layout.item_photo_album);
        recyclerview.setAdapter(photosadapter);
        photosadapter.setNewData(list);

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
        //返回
        back.setOnClickListener(v -> PhotosDetailsActivity.this.finish());
        //发布照片
        releasePhoto.setOnClickListener(v ->
                //图片选择器
                photoAndCamera()
        );
        //图标选取照片
        gallery.setOnClickListener(v -> {
            //图片选择器
            photoAndCamera();
        });

        //菜单
        photosAbout.setOnClickListener(v -> {
            photoAndCamera();
        });
    }

    //Intent  回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + "" + resultCode);
//        if (resultCode == RESULT_OK) {
        //获取图片路径
        switch (resultCode) {
            case RESULT_OK:
                // 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                //相册集 等于0
                if (photos.size() == 0 && selectList.size() > 0) {
                    messageRelativeLayout.setVisibility(View.GONE);
                }
                for (int i = 0; i < selectList.size(); i++) {
                    upLoadPic(selectList.get(i).getPath(), 0);
                    if (i == selectList.size() - 1) {
                        ToastUtil.show("提交成功 ");
                    }
                }
                break;
            case 200:
                //编辑设置标题
                mTitleBar.setTitleMainText(data.getStringExtra("photoTitle"));
                break;
        }
    }

    //启动相册、拍照选择器
    private void photoAndCamera() {
        PictureSelector.create(PhotosDetailsActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(9)// 最大图片选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)// 是否可预览图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setRightText("上传").setOnRightTextClickListener(v -> photoAndCamera());
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    //上传图片
    private void upLoadPic(final String url, final int position) {
        file = new File(url);
        RequestBody image = RequestBody.create(MediaType.parse("image/png"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imgs", url, image)
                .addFormDataPart("id", String.valueOf(familyAlbum))
                .build();
        ViseHttp.POST(ApiConstant.album_uploadImgs)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(requestBody)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.status == 200) {
                            Log.e(TAG, "onSuccess: 提交成功" + data.msg);
                        } else {
                            Log.e(TAG, "onSuccess: 提交失败" + data.msg);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("请求失败: " + errMsg + "，errCode: " + errCode);
                    }
                });
    }
}