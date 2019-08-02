package com.genealogy.by.utils.my;

import java.io.Serializable;
import java.util.List;

/**
 * Anthor:HeChuan
 * Time:2018/11/8
 * Desc: 数据加载的基类. 包含数据请求的最外层  字段: code、msg、data
 */
public class BaseTResp implements Serializable {
    private int status;
    private String msg;
    private int countSum;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCountSum() {
        return countSum;
    }

    public void setCountSum(int countSum) {
        this.countSum = countSum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean implements Serializable {
        private int id;
        private int gId;
        private String profilePhoto;
        private int uId;
        private String uName;
        private String operatingType;
        private int beId;
        private String beName;
        private String user;
        private String beUser;
        private String content;
        private String createTime;

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

        public String getBeUser() {
            return beUser;
        }

        public void setBeUser(String beUser) {
            this.beUser = beUser;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
