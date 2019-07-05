package com.genealogy.by.db;

public class User {

    private int iconId;
    private String name;
    private String content;
    private int sex;
    //第几代
    private int generation;
    private String profilePhoto;
    private String userid;
    private String gid;
    public User(){
    }

    public User(int iconId, String name, String content, int sex,
                int generation,String profilePhoto,String userid,String gid) {
        this.iconId = iconId;
        this.name = name;
        this.content = content;
        this.sex = sex;
        this.generation = generation;
        this.profilePhoto=profilePhoto;
        this.userid=userid;
        this.gid=gid;
    }
    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getGeneration() {
        return generation;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSex(){
        return sex;
    }

    public void setSex(int sex){
        this.sex = sex;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
    public String getSplitName(){
        String str = "";
        for(int i = 0;i < name.length();i++){
            if(name.length() == 2){
                if(i == 1){
                    str += "\n" + name.substring(i,i+1) + "\n";
                }else{
                    str +=  name.substring(i,i+1) + "\n";
                }
            }else{
                str += name.substring(i,i+1) + "\n";
            }
        }
        return str;
    }
    @Override
    public String toString() {
        return "User{" +
                "iconId=" + iconId +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", sex=" + sex +
                ", generation=" + generation +
                '}';
    }

}
