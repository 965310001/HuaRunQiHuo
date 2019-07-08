package tech.com.commoncore.constant;

/**
 * Anthor:HeChuan
 * Time:2018/11/5
 * Desc:
 */
public class ApiConstant {

    public static final String BASE_URL_ZP= "http://39.98.47.107:9005/";
    public static final String Account_login = "user/login";//登录
    public static final String Account_Send_messages = "user/sendPhoneMessage";//发送注册或登录短信(获取验证码)
    public static final String Account_register = "user/phoneLogin";//短信登录或注册登陆
    public static final String UerfectUserDetail = "user/perfectUserDetail";//完善个人信息
    public static final String searchNearInBlood = "user/searchNearInBlood";//近亲列表
    public static final String delUser = "user/delUser";//删除个人信息
    public static final String editUser = "user/editUser";//编辑个人信息
    public static final String inviteUser = " user/inviteUser";//邀请加入
    public static final String setAsCenter = "user/setAsCenter";//设为中心，父系列表
    public static final String addUsers = "user/addUser";//添加关系

    public static final String searchUserDeatil = "user/searchUserDeatil";//查看详情
    public static final String getRelationshipChain = "user/getRelationshipChain";//查看关系链
    public static final String addDeeds = "deeds/addDeeds";//添加事迹
    public static final String searchClan = "user/searchClan";//搜索

    public static final String familyBook = "familyBook/search";//查询族册
    public static final String familyBook_edit = "familyBook/edit";//编辑族册
    public static final String familyBook_uploadImg = "familyBook/uploadImg";//家族照上传
    public static final String familyBook_delImg = "familyBook/delImg";//删除家族照
    public static final String familyBook_editImg = "familyBook/editImg";//录入简介-家族照重新上传

    public static final String album_searchMyAlbum = "album/searchMyAlbum";//我的相册
    public static final String album_searchFamilyAlbum = "album/searchFamilyAlbum";//家族相册
    public static final String album_create = "album/create";//创建相册
    public static final String album_edit = "album/edit";//修改相册
    public static final String album_del = "album/del";//删除相册
    public static final String album_uploadImgs = "album/uploadImgs";//图片上传
    public static final String album_delImgs = "album/delImgs";//删除照片

    public static final String log_search = "log/search";//日志

}
