package com.genealogy.by.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.dinuscxj.refresh.RefreshView;
import com.genealogy.by.R;
import com.genealogy.by.adapter.AlbumDetailsAdapter;
import com.genealogy.by.adapter.onClickAlbumItem;
import com.genealogy.by.entity.Album;
import com.genealogy.by.entity.Photo;
import com.genealogy.by.utils.BitmapCut;
import com.genealogy.by.utils.ToolUtil;
import com.githang.statusbar.StatusBarCompat;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class PhotosDetailsActivity extends AppCompatActivity implements onClickAlbumItem {

    private ImageView back;
    private ToolUtil toolUtil;
    private TextView title;
    private ImageView gallery;
    private Button releasePhoto;
    private TextView photosAbout;
    private TextView managerAlbum;
    private TextView deleteAlbum;
    private Album album;
    private RelativeLayout messageRelativeLayout;
    private ListView albumsDetailsLv;
    private RecyclerRefreshLayout layout;
    private AlbumDetailsAdapter adapter;
    private ArrayList<Photo> photos;
    private BitmapCut bitmapCut;
    private String TAG = "PhotosDetailsActivity";

    @Override
    public void jumpActivity(Intent intent){
        startActivityForResult(intent,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏标题栏
//        ActionBar actionBar = getSupportActionBar();
        //ActionBar actionBar = getActionBar();
//        actionBar.hide();
        setContentView(R.layout.photos_details);

        initView();

        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this,toolUtil.hex2Int("#f5f5f5"));
    }

    public void initData(){
        Photo photo = new Photo();
        photo.setId(1);
        photo.setPath("/storage/emulated/0/DCIM/Screenshots/Screenshot_2019-03-07-10-05-44-727_com.taobao.taobao.png");
        photo.setBmp(BitmapFactory.decodeFile(photo.getPath()));
        photo.setMinBmp(bitmapCut.ImageCrop(photo.getBmp(),true));
        photos.add(photo);
        photo = new Photo();
        photo.setPath("/storage/emulated/0/DCIM/Screenshots/Screenshot_2019-03-07-10-05-44-727_com.taobao.taobao.png");
        photo.setBmp(BitmapFactory.decodeFile(photo.getPath()));
        photo.setMinBmp(bitmapCut.ImageCrop(photo.getBmp(),true));
        photo.setId(2);
        photos.add(photo);
        photo = new Photo();
        photo.setPath("/storage/emulated/0/DCIM/Screenshots/Screenshot_2019-03-07-10-05-44-727_com.taobao.taobao.png");
        photo.setBmp(BitmapFactory.decodeFile(photo.getPath()));
        photo.setMinBmp(bitmapCut.ImageCrop(photo.getBmp(),true));
        photo.setId(3);
        photos.add(photo);
    }

    public void initView(){
        toolUtil = new ToolUtil();
        bitmapCut = new BitmapCut();
        photos = new ArrayList<Photo>();
        album = new Album();
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        gallery = (ImageView) findViewById(R.id.gallery);
        releasePhoto = (Button) findViewById(R.id.release_photo);
        photosAbout = (TextView) findViewById(R.id.photos_about);
        messageRelativeLayout = (RelativeLayout)findViewById(R.id.message);
        albumsDetailsLv = (ListView)findViewById(R.id.albumsDetailsLv);
        layout = (RecyclerRefreshLayout)findViewById(R.id.layout);

        //获取传来的参数
        final Intent intent = getIntent();
        album.setText(intent.getStringExtra("text"));
        album.setId(Integer.valueOf(intent.getStringExtra("id")));
        album.setSrc(intent.getStringExtra("src"));
        album.setContent(intent.getStringExtra("content"));

        //下拉刷新
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                (int) toolUtil.dip2px(this, 40), (int) toolUtil.dip2px(this, 40));
        layout.setRefreshView(new RefreshView(this), layoutParams);//RefreshViewEg为下拉刷新控件中的自定义头部类，详细用法参考此控件用法
        layout.setRefreshStyle(RecyclerRefreshLayout.RefreshStyle.NORMAL);

        //initData();

        //相册集 list view
        adapter = new AlbumDetailsAdapter(this,R.layout.moban_details,photos,this);
        albumsDetailsLv.setAdapter(adapter);

        //判断是否有相册集 进行显示隐藏
        if(photos.size() > 0){
            messageRelativeLayout.setVisibility(View.GONE);
//            albumsDetailsLv.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
        }

        layout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {//刷新
                //取消刷新动画
                layout.setRefreshing(false);
                //清空数据
                //albums.clear();
                //重新获取数据
                photos = photos;
            }
        });

        //设置标题
        title.setText(album.getText());

        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotosDetailsActivity.this.finish();
            }
        });

        //发布照片
        releasePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //图片选择器
                photoAndCamera();
            }
        });

        //图标选取照片
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //图片选择器
                photoAndCamera();
            }
        });

        //菜单
        photosAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View inflate = LayoutInflater.from(PhotosDetailsActivity.this).inflate(R.layout.popwindow_photo, null);

                final PopupWindow popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //popupWindow.setAnimationStyle(R.style.take_photo_anim);
                //关闭事件
                popupWindow.setOnDismissListener(new popupDismissListener());
                //设置背景半透明
                backgroundAlpha(0.5f);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(v);

                managerAlbum = (TextView) inflate.findViewById(R.id.manager_album);
                deleteAlbum = (TextView) inflate.findViewById(R.id.delete_album);

                deleteAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭弹窗
                        popupWindow.dismiss();
                        //发送参数 回调
                        Intent intent = new Intent();
                        intent.putExtra("id",album.getId()+"");
                        setResult(201,intent);
                        PhotosDetailsActivity.this.finish();
                    }
                });

                managerAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭弹窗
                        popupWindow.dismiss();
                        //跳转
                        Intent i = new Intent(PhotosDetailsActivity.this,PhotosAddActivity.class);
                        i.putExtra("id",album.getId()+"");
                        startActivityForResult(i,0);
                    }
                });

            }
        });
    }

    //Intent  回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,requestCode+"");
        Log.d(TAG,resultCode+"");
//        if (resultCode == RESULT_OK) {
            //获取图片路径
            switch (resultCode) {
                case RESULT_OK:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    //相册集 等于0
                    if(photos.size() == 0 && selectList.size() > 0){
                        messageRelativeLayout.setVisibility(View.GONE);
                        //albumsDetailsLv.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.VISIBLE);
                    }
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    Photo p = null;
                    String path = null;
                    for (int i = 0;i < selectList.size();i++){
                        p = new Photo();
                        path = selectList.get(i).getPath();
                        if(path != null && !path.equals("")){
                            Log.d(TAG,path);
                            p.setBmp(BitmapFactory.decodeFile(path));
                            p.setMinBmp(bitmapCut.ImageCrop(p.getBmp(),true));
                            p.setPath(path);
                        }
                        photos.add(p);
                    }
                    adapter.refresh(photos);
                    break;
                case 200:
                    //编辑设置标题
                    title.setText(data.getStringExtra("photoTitle"));
                    break;
            }
//        }
    }

    //启动相册、拍照选择器
    public void photoAndCamera(){
        PictureSelector.create(PhotosDetailsActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(9)// 最大图片选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     */
    class popupDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}
