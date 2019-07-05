package com.genealogy.by.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.dinuscxj.refresh.RefreshView;
import com.genealogy.by.R;
import com.genealogy.by.activity.PhotosAddActivity;
import com.genealogy.by.adapter.AlbumAdapter;
import com.genealogy.by.adapter.onClickAlbumItem;
import com.genealogy.by.entity.Album;
import com.genealogy.by.utils.ToolUtil;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment implements onClickAlbumItem {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String TAG = "PhotosFragment";
    private Button addPhotos;
    private ImageView addImage;
    private RelativeLayout messageRelativeLayout;
    private ListView albumsLv;
    private ArrayList<Album> albums;
    private AlbumAdapter adapter;
    private RecyclerRefreshLayout layout;
    private ToolUtil toolUtil;
    private RadioGroup mPhotosRadio;

    @Override
    public void jumpActivity(Intent intent){
        startActivityForResult(intent,0);
    }

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BlankFragment.
     */
    public static PhotosFragment newInstance() {
        PhotosFragment fragment = new PhotosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.photos, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolUtil = new ToolUtil();
        mPhotosRadio = (RadioGroup) getActivity().findViewById(R.id.photos_radio);
        mPhotosRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.family){

                }else if(i==R.id.all_family){

                }
            }
        });

        albums = new ArrayList<Album>();
        addImage = (ImageView) getActivity().findViewById(R.id.add_image);
        addPhotos = (Button) getActivity().findViewById(R.id.add_photos);
        messageRelativeLayout = (RelativeLayout)getActivity().findViewById(R.id.message);
        albumsLv = (ListView)getActivity().findViewById(R.id.albumsLv);
        layout = (RecyclerRefreshLayout) getActivity().findViewById(R.id.layout);

        initData();

        //下拉刷新
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                (int) toolUtil.dip2px(getActivity(), 40), (int) toolUtil.dip2px(getActivity(), 40));
        layout.setRefreshView(new RefreshView(getActivity()), layoutParams);//RefreshViewEg为下拉刷新控件中的自定义头部类，详细用法参考此控件用法
        layout.setRefreshStyle(RecyclerRefreshLayout.RefreshStyle.NORMAL);

        //相册集 list view
        adapter = new AlbumAdapter(getActivity(),R.layout.moban,albums,this);
        albumsLv.setAdapter(adapter);

        //判断是否有相册集  进行显示隐藏
        if(albums.size() > 0){
            messageRelativeLayout.setVisibility(View.GONE);
            //albumsLv.setVisibility(View.VISIBLE);
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
                albums = albums;
            }
        });

        //添加相册按钮
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PhotosAddActivity.class),0);
            }
        });
        //添加相册按钮
        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PhotosAddActivity.class),0);
            }
        });

    }

    public void initData(){
        Album album = new Album();
        album.setText("旅游照");
        album.setContent("很好看");
        album.setId(1);
        albums.add(album);
        album = new Album();
        album.setText("风景");
        album.setContent("很好看");
        album.setId(2);
        albums.add(album);
        album = new Album();
        album.setText("美女");
        album.setContent("很好看");
        album.setId(3);
        albums.add(album);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //200 添加图库相册集
        //201 删除图库相册集
        if(requestCode == 0){
            switch (resultCode){
                case 200:
                    //相册集 等于0
                    if(albums.size() == 0){
                        messageRelativeLayout.setVisibility(View.GONE);
                        //albumsLv.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.VISIBLE);
                    }
                    Album album = new Album();
                    album.setText(data.getStringExtra("photoTitle"));
                    album.setContent(data.getStringExtra("photoDescription"));
                    albums.add(album);
                    //更新list view视图
                    adapter.refresh(albums);
                    break;
                case 201:
                    int id = Integer.valueOf(data.getStringExtra("id"));
                    for (int i =0;i < albums.size();i++){
                        if(albums.get(i).getId() == id){
                            albums.remove(i);
                        }
                    }
                    //相册集没了显示 提示信息
                    if(albums.size() == 0){
                        messageRelativeLayout.setVisibility(View.VISIBLE);
                        //albumsLv.setVisibility(View.VISIBLE);
                        layout.setVisibility(View.GONE);
                    }

                    //更新list view视图
                    adapter.refresh(albums);
                    break;
            }
        }
    }

}
