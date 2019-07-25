package com.genealogy.by;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.genealogy.by.Ease.Logs.Logs;
import com.genealogy.by.Ease.model.Model;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.mob.MobSDK;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.interceptor.HttpLogInterceptor;

import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import tech.com.commoncore.app.FastManager;
import tech.com.commoncore.base.BaseApplication;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.Utils;


/**
 * @Author: AriesHoo on 2018/7/31 10:43
 * @E-Mail: AriesHoo@126.com
 * Function:基础配置Application
 * Description:
 */
public class MyApplication extends BaseApplication {

    private String TAG = "BaseApp";
    private static MyApplication application;

    public static MyApplication getApplication() {
        return application;
    }

    @Override
    public void init() {
        application = this;
        MobSDK.init(this);//分享
        EMOptions();
        initLog();
        initNet();
        OkGo.getInstance();
        initOkGo();
        initBLChat();//数据库工具初始化
        Utils.init(this);
        //最简单UI配置模式-必须进行初始化
        FastManager.init(this);
        //全局UI配置参数-按需求设置
        AppImpl impl = new AppImpl(getInstance());
        FastManager.getInstance()
                //设置Adapter加载更多视图--默认设置了FastLoadMoreView
                .setLoadMoreFoot(impl)
                //设置RecyclerView加载过程多布局属性
                .setMultiStatusView(impl)
                //设置全局网络请求等待Loading提示框如登录等待loading--观察者必须为FastLoadingObserver及其子类
                .setLoadingDialog(impl)
                //设置SmartRefreshLayout刷新头-自定加载使用BaseRecyclerViewAdapterHelper
                .setDefaultRefreshHeader(impl)
                //设置全局TitleBarView相关配置
                .setTitleBarViewControl(impl)
                //设置Activity滑动返回控制-默认开启滑动返回功能不需要设置透明主题
                .setSwipeBackControl(impl)
                //设置Activity/Fragment相关配置(横竖屏+背景+虚拟导航栏+状态栏+生命周期)
                .setActivityFragmentControl(impl)
                //设置BasisActivity 子类按键监听
                .setActivityKeyEventControl(impl)
                //设置http请求结果全局控制
                .setHttpRequestControl(impl)
                //设置主页返回键控制-默认效果为2000 毫秒时延退出程序
                .setQuitAppControl(impl);

    }

    @Override
    public void initModuleApp(Application application) {

    }

    @Override
    public void initModuleData(Application application) {

    }

    private void initLog() {
        ViseLog.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(false);//是否排版显示
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }

    private void initNet() {
        ViseHttp.init(this);
        ViseHttp.CONFIG()
                //配置请求主机地址
                .baseUrl(ApiConstant.BASE_URL_ZP)
                .setCookie(true)
                .connectTimeout(15)
                //配置日志拦截器
                .interceptor(new HttpLogInterceptor()
                        .setLevel(HttpLogInterceptor.Level.BODY));

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public void initOkGo() {
        //okGo网络框架初始化和全局配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));      //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));//使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));      //使用内存保持cookie，app退出后，cookie消失
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        //设置请求参数
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);
    }

    public void EMOptions() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        //初始化
        EMClient.getInstance().init(application, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private void initBLChat() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(this.getPackageName())) {
            Logs.e("App", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        //初始化数据模型
        Model.getInstance().init(this);

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
//        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Logs.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
