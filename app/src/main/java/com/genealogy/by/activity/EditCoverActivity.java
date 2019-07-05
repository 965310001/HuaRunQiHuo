package com.genealogy.by.activity;

import android.content.Context;
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
import com.genealogy.by.entity.BookEdit;
import com.genealogy.by.entity.FamilyBook;
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

public class EditCoverActivity extends AppCompatActivity {
    protected Context mContext;
    private String TAG = "EditCoverActivity";
    private ToolUtil toolUtil;
    private ImageView back;
    private TextView saveTextView;
    private String id = "";
    private EditText nameEditText,personEditText,timeEditText,addressEditText;
    //封面参数
    private String name,person,time,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        //ActionBar actionBar = getActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        id=intent.getStringExtra("ID");
        //设置返回参数
        name = intent.getStringExtra("name");
        person = intent.getStringExtra("person");
        time = intent.getStringExtra("time");
        address = intent.getStringExtra("address");
        setContentView(R.layout.edit_cover);

        initView();


        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this,toolUtil.hex2Int("#f5f5f5"));
    }

    public void initView(){
        toolUtil = new ToolUtil();
        back = (ImageView) findViewById(R.id.back);
        saveTextView = (TextView) findViewById(R.id.saveText);
        nameEditText = (EditText) findViewById(R.id.name);
        personEditText = (EditText) findViewById(R.id.person);
        timeEditText = (EditText) findViewById(R.id.time);
        addressEditText = (EditText) findViewById(R.id.address);
        //设置封面布局 参数
        if(name != null && !name.equals("")){
            nameEditText.setText(name);
        }
        if(person != null && !person.equals("")){
            personEditText.setText(person);
        }
        if(time != null && !time.equals("")){
            timeEditText.setText(time);
        }
        if(address != null && !address.equals("")){
            addressEditText.setText(address);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCoverActivity.this.finish();
            }
        });
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doit();
            }
        });
    }
    public void doit(){
        name = nameEditText.getText().toString();
        person = personEditText.getText().toString();
        time = timeEditText.getText().toString();
        address = addressEditText.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        if(name != null && name.trim().length()!=0){
            params.put("familybookName",name );
        }
        if(person != null && person.trim().length()!=0){
            params.put("sponsor",person );
        }
        if(address != null && address.trim().length()!=0){
            params.put("catalogingAddress",address );
        }
        if(time != null && time.trim().length()!=0){
            params.put("editingTime",time );
        }
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
                .request(new ACallback<BaseTResp2<BookEdit>>() {
                    @Override
                    public void onSuccess(BaseTResp2<BookEdit> data) {
                        if(data.status==200){
                            ToastUtil.show(" 请求成功"+data.msg);
                            Intent i = new Intent();
                            if(nameEditText.getText().toString().trim().length()!=0&&
                                    nameEditText.getText().toString()!=null){
                                i.putExtra("name",nameEditText.getText().toString());
                            }else{
                                i.putExtra("name",name);
                            }
                            if(personEditText.getText().toString().trim().length()!=0&&
                                    personEditText.getText().toString()!=null){
                                i.putExtra("person",personEditText.getText().toString());
                            }else{
                                i.putExtra("person",person);
                            }
                            if(timeEditText.getText().toString().trim().length()!=0&&
                                    timeEditText.getText().toString()!=null){
                                i.putExtra("time",timeEditText.getText().toString());
                            }else{
                                i.putExtra("time",time);
                            }
                            if(addressEditText.getText().toString().trim().length()!=0&&
                                    addressEditText.getText().toString()!=null){
                                i.putExtra("address",addressEditText.getText().toString());
                            }else{
                                i.putExtra("address",address);
                            }
                            setResult(200, i);  // 200表示成功
                            EditCoverActivity.this.finish();
                        }else{
                            Log.e(TAG, "请求失败: status=  "+data.status );
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
    //看着哈，首先第一步，我们要提取公共封装，干嘛把版本弄那么高，回不兼容

}
