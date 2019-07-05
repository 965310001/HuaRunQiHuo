package com.genealogy.by.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.genealogy.by.MainActivity;
import com.genealogy.by.R;
import com.genealogy.by.utils.SPHelper;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPUtil;
import tech.com.commoncore.widget.NetErrorDialog;


public class SplashActivity extends BaseTitleActivity implements Runnable {
    public final int TIME_DELAY = 2000;

    @Override
    public void beforeSetContentView() {
        if (!isTaskRoot()) {//防止应用后台后点击桌面图标造成重启的假象---MIUI及Flyme上发现过(原生未发现)
            finish();
            return;
        }
        super.beforeSetContentView();
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setLeftTextDrawable(null).setTitleMainText("")
                .setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!StatusBarUtil.isSupportStatusBarFontChange()) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
//        nextJump(0);
        doit();

    }
    //自动登录
    void doit(){
        String userId =  SPHelper.getStringSF(mContext,"UserId",null);
        String gid = SPHelper.getStringSF(mContext,"GId",null);
        String phone = SPHelper.getStringSF(mContext,"Phone",null);
        if(userId!=null&&gid!=null&&phone!=null){
            FastUtil.startActivity(mContext, MainActivity.class);
            Log.e(TAG, "SplashActivity: 进主界面" );
            SplashActivity.this.finish();
        }else{
            FastUtil.startActivity(mContext, RegisterActivity.class);
            Log.e(TAG, "SplashActivity: 进登录界面" );
            SplashActivity.this.finish();
        }
}

    /**
     * 跳转统一延迟处理.
     *
     * @param type 0:guide页面; 1:主页; 其他:webview
     */
    private void nextJump(int type) {
        type=0;
        if (type == 0) {
            mContentView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //第一次就进GUIDE 页面
                    FastUtil.startActivity(mContext, RegisterActivity.class);
                    SplashActivity.this.finish();
                }
            }, TIME_DELAY);
        } else if (type == 1) {
            mContentView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //进入主页
                    FastUtil.startActivity(mContext, MainActivity.class);
                    SplashActivity.this.finish();
                }
            }, TIME_DELAY);

        }

    }
    @Override
    public void run() {
        FastUtil.startActivity(mContext, MainActivity.class);
        SplashActivity.this.finish();
    }
}
