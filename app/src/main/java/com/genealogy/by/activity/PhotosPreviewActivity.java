package com.genealogy.by.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.genealogy.by.R;
import com.genealogy.by.utils.ToolUtil;
import com.githang.statusbar.StatusBarCompat;

public class PhotosPreviewActivity extends AppCompatActivity {

    private ToolUtil toolUtil;
    private ImageView previewImageView;
    private String TAG = "PhotosPreviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_preview);

        initView();

        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this,toolUtil.hex2Int("#000000"));
    }

    public void initView(){
        toolUtil = new ToolUtil();
        previewImageView = (ImageView) findViewById(R.id.preview);

        final Intent intent = getIntent();
        String path = intent.getStringExtra("photo");
        Log.d(TAG,path);
        previewImageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }
}
