package com.genealogy.by.db;

import com.genealogy.by.entity.SearchNearInBlood;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private int iconId;
    private String name;
    private String content;
    private int sex;
    //第几代
    private int generation;
    private String profilePhoto;
    private String userid;
    private String gid;
    private String phone;
    private String birthday;
    private String bloodGroup;
    private String ancestralHome;
    private String currentResidence;
    private String wordGeneration;
    private String school;
    private String education;
    private String email;
    private String unit;
    private String position;
    private String mark;
    private String commonName;
    private String word;
    private String number;
    private String designation;
    private String noun;
    private String usedName;
    private String minName;
    private String birthArea;
    private String birthPlace;
    private String deathTime;
    private String dieAddress;
    private String buriedArea;
    private String deathPlace;
    private Object ranking;
    private Integer fatherId;//父亲ID
    private Integer motherId;//母亲ID
    private String nationality;
    private int isCelebrity;
    private String surname;
//    private String spouseId;//配偶ID


    public int getIsCelebrity() {
        return isCelebrity;
    }

    public void setIsCelebrity(int isCelebrity) {
        this.isCelebrity = isCelebrity;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public Object getRanking() {
        return ranking;
    }

    public void setRanking(Object ranking) {
        this.ranking = ranking;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    private List<SearchNearInBlood> childrens; //儿女

    private List<SearchNearInBlood> spouses;//配偶

    private boolean isSelect = false;//是否选中

    public User() {
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public User(String userid, String gid) {
        this.userid = userid;
        this.gid = gid;
    }

    public User(int iconId, String name, String content, int sex,
                int generation, String profilePhoto, String userid, String gid) {
        this.iconId = iconId;
        this.name = name;
        this.content = content;
        this.sex = sex;
        this.generation = generation;
        this.profilePhoto = profilePhoto;
        this.userid = userid;
        this.gid = gid;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
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

    public String getSplitName() {
        String str = "";
        for (int i = 0; i < name.length(); i++) {
            if (name.length() == 2) {
                if (i == 1) {
                    str += "\n" + name.substring(i, i + 1) + "\n";
                } else {
                    str += name.substring(i, i + 1) + "\n";
                }
            } else {
                str += name.substring(i, i + 1) + "\n";
            }
        }
        return str;
    }

    public List<SearchNearInBlood> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<SearchNearInBlood> childrens) {
        this.childrens = childrens;
    }

    public List<SearchNearInBlood> getSpouses() {
        return spouses;
    }

    public void setSpouses(List<SearchNearInBlood> spouses) {
        this.spouses = spouses;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAncestralHome() {
        return ancestralHome;
    }

    public void setAncestralHome(String ancestralHome) {
        this.ancestralHome = ancestralHome;
    }

    public String getCurrentResidence() {
        return currentResidence;
    }

    public void setCurrentResidence(String currentResidence) {
        this.currentResidence = currentResidence;
    }

    public String getWordGeneration() {
        return wordGeneration;
    }

    public void setWordGeneration(String wordGeneration) {
        this.wordGeneration = wordGeneration;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    public String getUsedName() {
        return usedName;
    }

    public void setUsedName(String usedName) {
        this.usedName = usedName;
    }

    public String getMinName() {
        return minName;
    }

    public void setMinName(String minName) {
        this.minName = minName;
    }

    public String getBirthArea() {
        return birthArea;
    }

    public void setBirthArea(String birthArea) {
        this.birthArea = birthArea;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(String deathTime) {
        this.deathTime = deathTime;
    }

    public String getDieAddress() {
        return dieAddress;
    }

    public void setDieAddress(String dieAddress) {
        this.dieAddress = dieAddress;
    }

    public String getBuriedArea() {
        return buriedArea;
    }

    public void setBuriedArea(String buriedArea) {
        this.buriedArea = buriedArea;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public Integer getMotherId() {
        return motherId;
    }

    public void setMotherId(Integer motherId) {
        this.motherId = motherId;
    }

    public boolean isSelect() {
        return isSelect;
    }
}
