package com.genealogy.by.utils.my;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * Created by dell on 2019/3/6.
 */

public class MyGlideEngine implements ImageEngine {
    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder);//占位图
        options.override(resize, resize);//图片大小
        options.centerCrop();
        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder);//占位图
        options.override(resize, resize);//图片大小
        options.centerCrop();

        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions();
        options.override(resizeX, resizeY);//图片大小
        options.fitCenter();
        options.priority(Priority.HIGH);

        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions options = new RequestOptions();
        options.override(resizeX, resizeX);//图片大小
        options.centerCrop();
        options.priority(Priority.HIGH);

        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
