package com.genealogy.by.entity;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchNearInBlood implements Serializable {

    private Integer id;
    private Integer gId;//族谱ID
    private Object fatherId;//父亲ID
    //    private Integer motherId;//母亲ID
    //    private Integer inviteId;
    private String account;
    private String spouseId;
    private String surname;
    private String name;
    private Integer sex;
    private String profilePhoto;
    private String phone;
    private String password;
//    private Integer health;//健在(0:健在 1:过世)
    //    private Double height;
//    private String bloodGroup;
//    private String ancestralHome;
//    private String currentResidence;
//    private String wordGeneration;
//    private String school;
//    private String education;
//    private String email;
//    private String unit;
//    private String position;
//    private String mark;
//    private Integer createTime;
//    private Integer ranking;
//    private String commonName;
//    private String word;
//    private String number;
//    private String designation;
//    private String noun;
//    private String usedName;
//    private String minName;
//    private String birthArea;
//    private String birthPlace;
//    private String deathTime;
//    private String dieAddress;
//    private String buriedArea;
//    private String deathPlace;
//    private Integer yearOfLife;
//    private String nationality;
//    private String moveOut;
//    private String industry;
//    private String url;
//    private Integer isCelebrity;
//    private String genealogyImage;
//    private String idCard;
//    private String latestUpdatetime;
//    private Integer branch;//分支 0:始祖 1:始迁祖 2:本支祖 3:分支祖

    //    private String remark;
//    private Integer oldId;
    private String nickName;
    private String relationship;
//    private String geneticDisease;
//    private Integer isReghx;//是否已经注册环信 0:否 1:是

    //    private String birthday;
//    private String deeds;
    private List<SearchNearInBlood> childrens;
    private List<SearchNearInBlood> spouses;

    private View mMineView;//我
    private List<View> mMySpouse;//配偶
    private List<View> mMyChildren;//子女

    public View getmMineView() {
        return mMineView;
    }

    public void setmMineView(View mMineView) {
        this.mMineView = mMineView;
    }

    public List<View> getmMySpouse() {
        return mMySpouse;
    }

    public void setmMySpouse(List<View> mMySpouse) {
        this.mMySpouse = mMySpouse;
    }

    public List<View> getmMyChildren() {
        return mMyChildren;
    }

    public void setmMyChildren(List<View> mMyChildren) {
        this.mMyChildren = mMyChildren;
    }

    public void addChildren(View view) {
        if (null == this.mMyChildren) {
            this.mMyChildren = new ArrayList<>();
        }
        if (null != view) {
            this.mMyChildren.add(view);
        }
    }

    public void addSpouses(View view) {
        if (null == this.mMySpouse) {
            this.mMySpouse = new ArrayList<>();
        }
        if (null != view) {
            this.mMySpouse.add(view);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGId() {
        return gId;
    }

    public void setGId(Integer gId) {
        this.gId = gId;
    }

    public Object getFatherId() {
        return fatherId;
    }

    public void setFatherId(Object fatherId) {
        this.fatherId = fatherId;
    }

//    public Integer getMotherId() {
//        return motherId;
//    }
//
//    public void setMotherId(Integer motherId) {
//        this.motherId = motherId;
//    }
//
//    public Integer getInviteId() {
//        return inviteId;
//    }
//
//    public void setInviteId(Integer inviteId) {
//        this.inviteId = inviteId;
//    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(String spouseId) {
        this.spouseId = spouseId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

//    public String getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(String birthday) {
//        this.birthday = birthday;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public Integer getHealth() {
//        return health;
//    }
//
//    public void setHealth(Integer health) {
//        this.health = health;
//    }

//    public Double getHeight() {
//        return height;
//    }
//
//    public void setHeight(Double height) {
//        this.height = height;
//    }

//    public String getBloodGroup() {
//        return bloodGroup;
//    }
//
//    public void setBloodGroup(String bloodGroup) {
//        this.bloodGroup = bloodGroup;
//    }
//
//    public String getAncestralHome() {
//        return ancestralHome;
//    }
//
//    public void setAncestralHome(String ancestralHome) {
//        this.ancestralHome = ancestralHome;
//    }
//
//    public String getCurrentResidence() {
//        return currentResidence;
//    }
//
//    public void setCurrentResidence(String currentResidence) {
//        this.currentResidence = currentResidence;
//    }
//
//    public String getWordGeneration() {
//        return wordGeneration;
//    }
//
//    public void setWordGeneration(String wordGeneration) {
//        this.wordGeneration = wordGeneration;
//    }
//
//    public String getSchool() {
//        return school;
//    }
//
//    public void setSchool(String school) {
//        this.school = school;
//    }
//
//    public String getEducation() {
//        return education;
//    }
//
//    public void setEducation(String education) {
//        this.education = education;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getUnit() {
//        return unit;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }

//    public String getPosition() {
//        return position;
//    }
//
//    public void setPosition(String position) {
//        this.position = position;
//    }
//
//    public String getMark() {
//        return mark;
//    }
//
//    public void setMark(String mark) {
//        this.mark = mark;
//    }

//    public Integer getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Integer createTime) {
//        this.createTime = createTime;
//    }

//    public Integer getRanking() {
//        return ranking;
//    }
//
//    public void setRanking(Integer ranking) {
//        this.ranking = ranking;
//    }

//    public String getCommonName() {
//        return commonName;
//    }
//
//    public void setCommonName(String commonName) {
//        this.commonName = commonName;
//    }
//
//    public String getWord() {
//        return word;
//    }
//
//    public void setWord(String word) {
//        this.word = word;
//    }

//    public String getNumber() {
//        return number;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }
//
//    public String getDesignation() {
//        return designation;
//    }
//
//    public void setDesignation(String designation) {
//        this.designation = designation;
//    }
//
//    public String getNoun() {
//        return noun;
//    }
//
//    public void setNoun(String noun) {
//        this.noun = noun;
//    }
//
//    public String getUsedName() {
//        return usedName;
//    }
//
//    public void setUsedName(String usedName) {
//        this.usedName = usedName;
//    }

//    public String getMinName() {
//        return minName;
//    }
//
//    public void setMinName(String minName) {
//        this.minName = minName;
//    }
//
//    public String getBirthArea() {
//        return birthArea;
//    }
//
//    public void setBirthArea(String birthArea) {
//        this.birthArea = birthArea;
//    }
//
//    public String getBirthPlace() {
//        return birthPlace;
//    }
//
//    public void setBirthPlace(String birthPlace) {
//        this.birthPlace = birthPlace;
//    }
//
//    public String getDeathTime() {
//        return deathTime;
//    }
//
//    public void setDeathTime(String deathTime) {
//        this.deathTime = deathTime;
//    }
//
//    public String getDieAddress() {
//        return dieAddress;
//    }
//
//    public void setDieAddress(String dieAddress) {
//        this.dieAddress = dieAddress;
//    }
//
//    public String getBuriedArea() {
//        return buriedArea;
//    }
//
//    public void setBuriedArea(String buriedArea) {
//        this.buriedArea = buriedArea;
//    }
//
//    public String getDeathPlace() {
//        return deathPlace;
//    }
//
//    public void setDeathPlace(String deathPlace) {
//        this.deathPlace = deathPlace;
//    }

//    public Integer getYearOfLife() {
//        return yearOfLife;
//    }
//
//    public void setYearOfLife(Integer yearOfLife) {
//        this.yearOfLife = yearOfLife;
//    }

    //    public String getNationality() {
//        return nationality;
//    }
//
//    public void setNationality(String nationality) {
//        this.nationality = nationality;
//    }
//
//    public String getMoveOut() {
//        return moveOut;
//    }
//
//    public void setMoveOut(String moveOut) {
//        this.moveOut = moveOut;
//    }
//
//    public String getIndustry() {
//        return industry;
//    }
//
//    public void setIndustry(String industry) {
//        this.industry = industry;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public Integer getIsCelebrity() {
//        return isCelebrity;
//    }
//
//    public void setIsCelebrity(Integer isCelebrity) {
//        this.isCelebrity = isCelebrity;
//    }
//
//    public String getGenealogyImage() {
//        return genealogyImage;
//    }
//
//    public void setGenealogyImage(String genealogyImage) {
//        this.genealogyImage = genealogyImage;
//    }
//
//    public String getIdCard() {
//        return idCard;
//    }
//
//    public void setIdCard(String idCard) {
//        this.idCard = idCard;
//    }
//
//    public String getLatestUpdatetime() {
//        return latestUpdatetime;
//    }
//
//    public void setLatestUpdatetime(String latestUpdatetime) {
//        this.latestUpdatetime = latestUpdatetime;
//    }
//
//    public Integer getBranch() {
//        return branch;
//    }
//
//    public void setBranch(Integer branch) {
//        this.branch = branch;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public int getOldId() {
//        return oldId;
//    }
//
//    public void setOldId(int oldId) {
//        this.oldId = oldId;
//    }
//
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

//    public String getGeneticDisease() {
//        return geneticDisease;
//    }
//
//    public void setGeneticDisease(String geneticDisease) {
//        this.geneticDisease = geneticDisease;
//    }
//
//    public Integer getIsReghx() {
//        return isReghx;
//    }
//
//    public void setIsReghx(Integer isReghx) {
//        this.isReghx = isReghx;
//    }
//
//    public String getDeeds() {
//        return deeds;
//    }
//
//    public void setDeeds(String deeds) {
//        this.deeds = deeds;
//    }

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


}

