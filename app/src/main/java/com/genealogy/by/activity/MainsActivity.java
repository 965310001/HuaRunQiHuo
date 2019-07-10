package com.genealogy.by.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.genealogy.by.R;

public class MainsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 父布局容器
        LinearLayout lineLayout = new LinearLayout(this);
        lineLayout.setOrientation(LinearLayout.VERTICAL);
        lineLayout.setLayoutParams(params);
        lineLayout.setGravity(Gravity.TOP);
        // 把View添加到父布局容器中
        addView(lineLayout);
        setContentView(lineLayout);
    }

    @SuppressLint("ResourceType")
    private void addView(final LinearLayout lineLayout){
        // 创建TextView  -  "我是北京 - Darren"
        final TextView showText = new TextView(this);
        showText.setTextColor(Color.GREEN);
        showText.setTextSize(30);
        showText.setId(10001);//设置 id
        showText.setText("我是北京 - Darren");
        showText.setBackgroundResource(R.mipmap.boy);
        showText.setBackgroundColor(Color.BLUE);
        // 设置 大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        // 设置 四周距离
        params.setMargins(10, 10, 10, 10);
        // 把上边的参数设置给TextView
        showText.setLayoutParams(params);
        // 把文本添加到父布局
        lineLayout.addView(showText);
        // 创建Button按钮
        Button btn = new Button(this);
        btn.setText("点击删除文本");
        btn.setBackgroundColor(Color.GRAY) ;
        // 设置 大小
        LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置四周距离
        btn_params.setMargins(0, 60, 60, 0);
        // 设置方向
        btn_params.gravity = Gravity.CENTER_HORIZONTAL;
        // 把上边设置的参数给Button按钮
        btn.setLayoutParams(btn_params);
        // 把按钮动态添加到父布局
        lineLayout.addView(btn);
        //点击按钮 动态删除文本
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(null != lineLayout.findViewById(10001)){
                    lineLayout.removeView(showText);
                }else{
                    Toast.makeText(MainsActivity.this, "文本已被删除", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //动态创建一个相对布局
        RelativeLayout relaLayout = new RelativeLayout(this);
        relaLayout.setBackgroundColor(Color.BLUE);
        RelativeLayout.LayoutParams lp11 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 120);
        relaLayout.setLayoutParams(lp11);
        //动态创建一个文本
        final TextView RelaText = new TextView(this);
        RelaText.setTextColor(Color.GREEN);
        RelaText.setTextSize(20);
        RelaText.setText("我是在程序中添加的第二个文本，在相对布局中");
        RelaText.setBackgroundColor(Color.GRAY);

        //设置文本的布局
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        lp2.setMargins(10, 10, 10, 10);
        //将文本添加到相对布局中
        relaLayout.addView(RelaText,lp2);
        //将这个布局添加到主布局中
        lineLayout.addView(relaLayout);

    }
}
