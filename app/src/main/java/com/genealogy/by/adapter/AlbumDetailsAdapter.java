package com.genealogy.by.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.genealogy.by.R;
import com.genealogy.by.activity.PhotosPreviewActivity;
import com.genealogy.by.entity.Photo;
import com.genealogy.by.utils.BitmapCut;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailsAdapter extends BaseAdapter {

    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    public ArrayList<Photo> list;
    private BitmapCut bitmapCut = new BitmapCut();
    private onClickAlbumItem onClickAlbumItem;
    private String TAG = "AlbumDetailsAdapter";

    public AlbumDetailsAdapter(Context context, int resource, ArrayList<Photo> list, onClickAlbumItem listener) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        this.onClickAlbumItem = listener;
        if (list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
    }

    @Override
    public int getCount() {
        if (list.size() % 4 > 0) {
            return list.size() / 4 + 1;
        } else {
            return list.size() / 4;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            vh = new ViewHolder();
            vh.img1 =  convertView.findViewById(R.id.img1);
            vh.img2 =  convertView.findViewById(R.id.img2);
            vh.img3 =  convertView.findViewById(R.id.img3);
            vh.img4 =  convertView.findViewById(R.id.img4);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        int distance = list.size() - position * 4;
        int cellCount = distance >= 4 ? 4 : distance;
        final List<Photo> itemList = list.subList(position * 4, position * 4 + cellCount);
        Log.d(TAG + "itemList:", itemList.toString());
        if (itemList.size() > 0) {
            vh.img1.setImageBitmap(itemList.get(0).getMinBmp());
            vh.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotosPreviewActivity.class);
                    Log.d(TAG, itemList.get(0).getPath());
                    intent.putExtra("photo", itemList.get(0).getPath());
                    intent.putExtra("id", itemList.get(0).getId() + "");
                    onClickAlbumItem.jumpActivity(intent);
                }
            });
            if (itemList.size() > 1) {
                vh.img2.setVisibility(View.VISIBLE);
                vh.img2.setImageBitmap(itemList.get(1).getMinBmp());
                vh.img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PhotosPreviewActivity.class);
                        Log.d(TAG, itemList.get(1).getPath());
                        intent.putExtra("photo", itemList.get(1).getPath());
                        intent.putExtra("id", itemList.get(1).getId() + "");
                        onClickAlbumItem.jumpActivity(intent);
                    }
                });
            } else {
                vh.img2.setVisibility(View.INVISIBLE);
            }
            if (itemList.size() > 2) {
                vh.img3.setVisibility(View.VISIBLE);
                vh.img3.setImageBitmap(itemList.get(2).getMinBmp());
                vh.img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PhotosPreviewActivity.class);
                        Log.d(TAG, itemList.get(2).getPath());
                        intent.putExtra("photo", itemList.get(2).getPath());
                        intent.putExtra("id", itemList.get(2).getId() + "");
                        onClickAlbumItem.jumpActivity(intent);
                    }
                });
            } else {
                vh.img3.setVisibility(View.INVISIBLE);
            }
            if (itemList.size() > 3) {
                vh.img4.setVisibility(View.VISIBLE);
                vh.img4.setImageBitmap(itemList.get(3).getMinBmp());
                vh.img4.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PhotosPreviewActivity.class);
                    Log.d(TAG, itemList.get(3).getPath());
                    intent.putExtra("photo", itemList.get(3).getPath());
                    intent.putExtra("id", itemList.get(3).getId() + "");
                    onClickAlbumItem.jumpActivity(intent);
                });
            } else {
                vh.img4.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    public void refresh(ArrayList<Photo> photos) {
        this.list = photos;
        this.notifyDataSetChanged();
    }

    /**
     * 封装ListView中item控件以优化ListView
     *
     * @author tongleer
     */
    public static class ViewHolder {
        ImageView img1;
        ImageView img2;
        ImageView img3;
        ImageView img4;
    }
}
