package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genealogy.by.R;
import com.genealogy.by.utils.SPHelper;
import com.genealogy.by.utils.ToolUtil;
import com.genealogy.by.utils.my.BaseTResp2;
import com.githang.statusbar.StatusBarCompat;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.http.mode.CacheMode;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.ToastUtil;

import static tech.com.commoncore.utils.Utils.getContext;

public class PhotosAddActivity extends AppCompatActivity {

    private ImageView back;
    private TextView savePhotos;
    private ToolUtil toolUtil;
    private TextView photoDescription;
    private EditText photoTitle;
    private String id;
    private String TAG = "PhotosAddActivity";
    private TextView shareswitch;
    public int isTrue = 0;//是否分享的开关参数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_add);
        initView();
        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this,toolUtil.hex2Int("#f5f5f5"));
    }

    public void initView(){
        toolUtil = new ToolUtil();
        back =  findViewById(R.id.back);
        savePhotos =  findViewById(R.id.savePhotos);
        photoDescription = findViewById(R.id.photo_description);
        photoTitle = findViewById(R.id.photo_title);
        shareswitch = findViewById(R.id.shareswitch);
        shareswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isTrue==0){
                    isTrue=1;
                }else{
                    isTrue=0;
                }
            }
        });
        //获取intent 传递参数
        Intent i = getIntent();
        //取到id 暂时没用
        id = i.getStringExtra("id");
        savePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = photoTitle.getText().toString();
                if(text.equals("") || text == null){
                    Toast.makeText(PhotosAddActivity.this, "请输入标题！", Toast.LENGTH_SHORT).show();
                }else{
                    String title = photoTitle.getText().toString();
                    doit(title,isTrue);
                    Intent intent = new Intent();
                    intent.putExtra("photoDescription",photoDescription.getText().toString());
                    intent.putExtra("photoTitle",photoTitle.getText().toString());
                    setResult(200,intent);
                    PhotosAddActivity.this.finish();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotosAddActivity.this.finish();
            }
        });
    }
    public  void doit(String title,int isTrue){
        String userId =  SPHelper.getStringSF(PhotosAddActivity.this,"UserId","");
        String gid =  SPHelper.getStringSF(PhotosAddActivity.this,"GID","");
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("gId", gid);
        params.put("title", title);
        params.put("isTrue", String.valueOf(isTrue));
        Log.e(TAG, "doit: 参数："+params.toString() );
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS= MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.album_create)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS,jsonObject.toString()))
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if(data.status==200){
                            Log.e(TAG, "onSuccess: 创建相册请求成功  msg= "+data.msg );
                        }else{
                            Log.e(TAG, "onSuccess: 创建相册请求成功  msg= "+data.msg );
                            ToastUtil.show("创建相册失败 "+data.msg);
                        }
                    }
                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("请求失败: "+errMsg);
                        Log.e(TAG, "errMsg: "+errMsg +"errCode:  "+errCode);
                    }
                });

    }
}
