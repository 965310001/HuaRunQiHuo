package com.genealogy.by.entity;

public class Journal {

    /**
     * id : 27
     * gId : 1
     * profilePhoto : http://www.baidu.com/123.jpg
     * uId : 131593
     * uName : 扬波
     * operatingType : 删除了
     * beId : 131598
     * beName : 林心晓
     * user : 
     * content : 
     * beUser : 
     * createTime : 1561707010000
     */
    private int id;
    private int gId;
    private String profilePhoto;
    private int uId;
    private String uName;
    private String operatingType;
    private int beId;
    private String beName;
    private String user;
    private String content;
    private String beUser;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public String getUName() {
        return uName;
    }

    public void setUName(String uName) {
        this.uName = uName;
    }

    public String getOperatingType() {
        return operatingType;
    }

    public void setOperatingType(String operatingType) {
        this.operatingType = operatingType;
    }

    public int getBeId() {
        return beId;
    }

    public void setBeId(int beId) {
        this.beId = beId;
    }

    public String getBeName() {
        return beName;
    }

    public void setBeName(String beName) {
        this.beName = beName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBeUser() {
        return beUser;
    }

    public void setBeUser(String beUser) {
        this.beUser = beUser;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
