package com.genealogy.by.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.genealogy.by.R;
import com.genealogy.by.utils.ToolUtil;
import com.githang.statusbar.StatusBarCompat;

public class PhotosAddActivity extends AppCompatActivity {

    private ImageView back;
    private TextView savePhotos;
    private ToolUtil toolUtil;
    private EditText photoDescription;
    private EditText photoTitle;
    private String id;

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
        back = (ImageView) findViewById(R.id.back);
        savePhotos = (TextView) findViewById(R.id.savePhotos);
        photoDescription = (EditText) findViewById(R.id.photo_description);
        photoTitle = (EditText) findViewById(R.id.photo_title);

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
}
