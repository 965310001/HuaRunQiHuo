package com.genealogy.by.entity;


public class PhoneLogin {


    /**
     * Authorization : 150f705c-1ad2-4149-b6e3-373763b48abc
     * userId : 21313
     * gId : 1
     * profilePhoto : http://baidu.com/1.jpg
     * nickName : 大头
     */

    private String Authorization;
    private String userId;
    private int gId;
    private String profilePhoto;
    private String nickName;
    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String Authorization) {
        this.Authorization = Authorization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGId() {
        return gId;
    }

    public void setGId(int gId) {
        this.gId = gId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
