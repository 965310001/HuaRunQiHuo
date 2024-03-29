package tech.com.commoncore.basecomponent;


import tech.com.commoncore.basecomponent.empty_service.EmptyFragmentService;
import tech.com.commoncore.basecomponent.empty_service.EmptyLoginService;
import tech.com.commoncore.basecomponent.empty_service.EmptyZiXunService;
import tech.com.commoncore.basecomponent.service.IFragmentService;
import tech.com.commoncore.basecomponent.service.ILoginService;
import tech.com.commoncore.basecomponent.service.IZiXunService;

public class ServiceFactory {
    /**
     * 禁止外部创建 ServiceFactory 对象
     */
    private ServiceFactory() {
    }

    /**
     * 通过静态内部类方式实现 ServiceFactory 的单例
     */
    public static ServiceFactory getInstance() {
        return Inner.serviceFactory;
    }

    private static class Inner {
        private static ServiceFactory serviceFactory = new ServiceFactory();
    }

    private ILoginService loginService;
    private IFragmentService homeService;
    private IFragmentService circleService;
    private IFragmentService perconalService;
    private IFragmentService hangqingService;
    private IZiXunService zixunService;

    //--------------------登录-----------------
    /**
     * 接收 Login 组件实现的 Service 实例
     */
    public void setLoginService(ILoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 返回 Login 组件的 Service 实例
     */
    public ILoginService getLoginService() {
        if (loginService == null) {
            loginService = new EmptyLoginService();
        }
        return loginService;
    }

    //--------------------主页-----------------
    /**
     * 接收 home 组件实现的 Service 实例
     */
    public void setHomeService(IFragmentService homeService) {
        this.homeService = homeService;
    }

    /**
     * 返回 home 组件的 Service 实例
     */
    public IFragmentService getHomeService() {
        if (homeService == null) {
            homeService = new EmptyFragmentService();
        }
        return homeService;
    }

    //--------------------行情-----------------
    /**
     * 接收 行情 组件实现的 Service 实例
     */
    public void setHangqingService(IFragmentService hangqingService) {
        this.hangqingService = hangqingService;
    }
    /**
     * 返回 行情 组件的 Service 实例
     */
    public IFragmentService getHangqingService() {
        if (hangqingService == null) {
            hangqingService = new EmptyFragmentService();
        }
        return hangqingService;
    }

    //-------------------论坛----------------
    /**
     * 接收 圈子 组件实现的 Service 实例
     */
    public void setCircleService(IFragmentService circleService) {
        this.circleService = circleService;
    }
    /**
     * 返回 圈子 组件的 Service 实例
     */
    public IFragmentService getCircleService() {
        if (circleService == null) {
            circleService = new EmptyFragmentService();
        }
        return circleService;
    }


    //--------------------个人中心--------------------
    /**
     * 接收 个人中心 组件实现的 Service 实例
     */
    public void setPerconalService(IFragmentService perconalService) {
        this.perconalService = perconalService;
    }
    /**
     * 返回 个人中心 组件的 Service 实例
     */
    public IFragmentService getPerconalServiceService() {
        if (perconalService == null) {
            perconalService = new EmptyFragmentService();
        }
        return perconalService;
    }

    //-----------------------资讯------------------------
    public void setZixunService(IZiXunService zixunService){
        this.zixunService = zixunService;
    }
    public IZiXunService getZixunServiceService() {
        if (zixunService == null) {
            zixunService = new EmptyZiXunService();
        }
        return zixunService;
    }

}
