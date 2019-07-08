package com.genealogy.by.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.genealogy.by.entity.FamilyBook;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.ToolUtil;
import com.genealogy.by.utils.my.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment implements onClickAlbumItem {
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
        doit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photos, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolUtil = new ToolUtil();
        mPhotosRadio =  getActivity().findViewById(R.id.photos_radio);
        mPhotosRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.family){

                }else if(i==R.id.all_family){

                }
            }
        });

        albums = new ArrayList<Album>();
        addImage =  getActivity().findViewById(R.id.add_image);
        addPhotos =  getActivity().findViewById(R.id.add_photos);
        messageRelativeLayout = getActivity().findViewById(R.id.message);
        albumsLv = getActivity().findViewById(R.id.albumsLv);
        layout = getActivity().findViewById(R.id.layout);
        //下拉刷新
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                toolUtil.dip2px(getActivity(), 40), (int) toolUtil.dip2px(getActivity(), 40));
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
                        layout.setVisibility(View.GONE);
                    }
                    //更新list view视图
                    adapter.refresh(albums);
                    break;
            }
        }
    }

    public void doit(){
        String userId =  SPHelper.getStringSF(getContext(),"UserId","");
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        Log.e(TAG, "doit: 参数："+params.toString() );
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS= MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.album_searchMyAlbum)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS,jsonObject.toString()))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if(data.status==200){
                            Log.e(TAG, "onSuccess: 我的相册查询请求成功  msg= "+data.msg );
                            if(data.data.toString().equals("[]")){
                                layout.setVisibility(View.GONE);
                            }else{

                            }
                        }else{
                            Log.e(TAG, "onSuccess: 我的相册查询请求成功  msg= "+data.msg );
                            ToastUtil.show("请求失败 "+data.msg);
                        }
                    }
                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: "+errMsg);
                        Log.e(TAG, "errMsg: "+errMsg +"errCode:  "+errCode);
                    }
                });
    }

}
