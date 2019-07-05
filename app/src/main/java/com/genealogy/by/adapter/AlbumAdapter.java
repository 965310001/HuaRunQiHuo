package com.genealogy.by.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.genealogy.by.R;
import com.genealogy.by.activity.PhotosDetailsActivity;
import com.genealogy.by.entity.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends BaseAdapter{

    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    public ArrayList<Album> list;
    private onClickAlbumItem onClickAlbumItem;

    public AlbumAdapter(Context context, int resource, ArrayList<Album> list, onClickAlbumItem listener){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        this.onClickAlbumItem = listener;
        if(list==null){
            this.list = new ArrayList<>();
        }else{
            this.list = list;
        }
    }
    @Override
    public int getCount() {
        if(list.size()%2>0) {
            return list.size()/2+1;
        } else {
            return list.size()/2;
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
        if (convertView == null ) {
            convertView = inflater.inflate(resource, null);
            vh = new ViewHolder();
            vh.album = (RelativeLayout) convertView.findViewById(R.id.album);
            vh.img = (ImageView) convertView.findViewById(R.id.img);
            vh.photoName = (TextView) convertView.findViewById(R.id.photoName);

            vh.album1 = (RelativeLayout) convertView.findViewById(R.id.album1);
            vh.img1 = (ImageView) convertView.findViewById(R.id.img1);
            vh.photoName1 = (TextView) convertView.findViewById(R.id.photoName1);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        int distance =  list.size() - position*2;
        int cellCount = distance >= 2? 2:distance;
        final List<Album> itemList = list.subList(position*2,position*2+cellCount);
        if (itemList.size() >0) {
            vh.photoName.setText(itemList.get(0).getText());
            vh.album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, itemList.get(0).getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PhotosDetailsActivity.class);
                    intent.putExtra("id",itemList.get(0).getId()+"");
                    intent.putExtra("text",itemList.get(0).getText());
                    intent.putExtra("content",itemList.get(0).getContent());
                    intent.putExtra("src",itemList.get(0).getSrc());
                    onClickAlbumItem.jumpActivity(intent);
                }
            });
            if (itemList.size() >1){
                vh.photoName1.setText(itemList.get(1).getText());
                vh.album1.setVisibility(View.VISIBLE);
                vh.album1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(context, itemList.get(1).getText(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, PhotosDetailsActivity.class);
                        intent.putExtra("id",itemList.get(1).getId()+"");
                        intent.putExtra("text",itemList.get(1).getText());
                        intent.putExtra("content",itemList.get(1).getContent());
                        intent.putExtra("src",itemList.get(1).getSrc());
                        onClickAlbumItem.jumpActivity(intent);
                    }
                });
            }else{
                vh.album1.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    public void refresh(ArrayList<Album> albums){
        this.list = albums;
        this.notifyDataSetChanged();
    }

    /**
     * 封装ListView中item控件以优化ListView
     * @author tongleer
     *
     */
    public static class ViewHolder{
        RelativeLayout album;
        ImageView img;
        TextView photoName;

        RelativeLayout album1;
        ImageView img1;
        TextView photoName1;
    }
}
