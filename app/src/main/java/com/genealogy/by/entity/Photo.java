package com.genealogy.by.entity;

import android.graphics.Bitmap;

import java.util.Date;

public class Photo {

    private int id;
    private Date crateTime;
    private String path;
    private Bitmap bmp;
    private Bitmap minBmp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCrateTime() {
        return crateTime;
    }

    public void setCrateTime(Date crateTime) {
        this.crateTime = crateTime;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public Bitmap getMinBmp() {
        return minBmp;
    }

    public void setMinBmp(Bitmap minBmp) {
        this.minBmp = minBmp;
    }

    public String getPath() {
        return path;

    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", crateTime=" + crateTime +
                ", path='" + path + '\'' +
                ", bmp=" + bmp +
                ", minBmp=" + minBmp +
                '}';
    }
}
