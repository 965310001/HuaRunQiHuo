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

import com.genealogy.by.R;
import com.genealogy.by.entity.FamilyBook;
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

public class EditContentActivity extends AppCompatActivity {

    private ToolUtil toolUtil;
    private TextView title;
    private ImageView back;
    private EditText evcontent;
    private TextView saveText;
    private String index;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_content);
        Intent intent = getIntent();
        id=intent.getStringExtra("ID");
        initView();
        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this,toolUtil.hex2Int("#f5f5f5"));

    }

    public void initView(){
        toolUtil = new ToolUtil();

        saveText = (TextView)findViewById(R.id.saveText);
        evcontent = (EditText)findViewById(R.id.content);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditContentActivity.this.finish();
            }
        });

        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doit();
            }
        });

        //设置标题
        Intent intent = getIntent();
        String name = intent.getStringExtra("title");
        index = intent.getStringExtra("index");
        if(name != null){
            title = findViewById(R.id.title);
            title.setText(name);
        }
    }
    public void doit(){
        HashMap<String, String> params = new HashMap<>();
        if(evcontent.getText()!=null&&evcontent.getText().toString().trim().length()!=0){}
        params.put("editorialCommittee",evcontent.getText().toString() );
        if(id!=null&&id.trim().length()!=0){
            params.put("id", id);
        }else{
            ToastUtil.show("未知错误，请重新登录");
        }
        JSONObject jsonObject = new JSONObject(params);
        final MediaType JSONS= MediaType.parse("application/json; charset=utf-8");
        ViseHttp.POST(ApiConstant.familyBook_edit)
                .baseUrl(ApiConstant.BASE_URL_ZP).setHttpCache(true)
                .cacheMode(CacheMode.FIRST_REMOTE)
                .setRequestBody(RequestBody.create(JSONS,jsonObject.toString()))
                .request(new ACallback<BaseTResp2<FamilyBook>>() {
                    @Override
                    public void onSuccess(BaseTResp2<FamilyBook> data) {
                        if(data.status==200){
                            ToastUtil.show(" 请求成功"+data.msg);
                            Intent intent = new Intent();
                            intent.putExtra("content",evcontent.getText().toString());
                            intent.putExtra("index",index);
                            setResult(201, intent);  // 201表示成功
                            EditContentActivity.this.finish();
                            setResult(200, intent);  // 200表示成功
                        }else{
                            ToastUtil.show("请求失败 "+data.msg);
                        }
                    }
                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show("失败: "+errMsg);
                        Log.e("", "errMsg: "+errMsg +"errCode:  "+errCode);
                    }
                });
    }
}
