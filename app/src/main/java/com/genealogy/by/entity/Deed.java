package com.genealogy.by.entity;

public class Deed {

    /**
     * id : 16
     * title : 人在广州漂泊到失联
     * deedsTime : 2009-06-30
     * location : 广州
     * details : 人人都在广州是靓仔
     * userId : 131618
     * createTime : 1561965070000
     * urls :
     */

    private int id;
    private String title;
    private String deedsTime;
    private String location;
    private String details;
    private int userId;
    private long createTime;
    private String urls;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeedsTime() {
        return deedsTime;
    }

    public void setDeedsTime(String deedsTime) {
        this.deedsTime = deedsTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
