package com.genealogy.by.entity;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchNearInBlood implements Serializable {

    private String remark;
    private String idCard;
    private String moveOut;
    private int health;
    private String relationship;
    private String industry;
    private String url;
    private String geneticDisease;
    private Object yearOfLife;
    private Object height;
    private long id;
    private Integer gId;//族谱ID
    private String account;
    private String spouseId;
    private String surname;
    private String name;
    private int sex;
    private String profilePhoto;/*头像*/
    private String phone;
    private String password;
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
    private int ranking;
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
    private String nationality;
    private int isCelebrity;
    private String nickName;
    private String birthday;
    private List<SearchNearInBlood> childrens;
    private List<SearchNearInBlood> spouses;
    private transient View mMineView;//我
    private transient List<View> mSpouse;//配偶
    private transient List<View> mChildren;//子女
    private List<GenerationBean> generation;

    private SearchNearInBlood user;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMoveOut() {
        return moveOut;
    }

    public void setMoveOut(String moveOut) {
        this.moveOut = moveOut;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGeneticDisease() {
        return geneticDisease;
    }

    public void setGeneticDisease(String geneticDisease) {
        this.geneticDisease = geneticDisease;
    }

    public Object getYearOfLife() {
        return yearOfLife;
    }

    public void setYearOfLife(Object yearOfLife) {
        this.yearOfLife = yearOfLife;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Integer getgId() {
        return gId;
    }

    public void setgId(Integer gId) {
        this.gId = gId;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }

    public View getMineView() {
        return mMineView;
    }

    public void setMineView(View mMineView) {
        this.mMineView = mMineView;
    }

    public List<View> getSpouse() {
        return mSpouse;
    }

    public void setSpouse(List<View> mMySpouse) {
        this.mSpouse = mMySpouse;
    }

    public List<View> getChildren() {
        return mChildren;
    }

    public void setChildren(List<View> mMyChildren) {
        this.mChildren = mMyChildren;
    }

    public void addChildren(View view) {
        if (null == this.mChildren) {
            this.mChildren = new ArrayList<>();
        }
        if (null != view) {
            this.mChildren.add(view);
        }
    }

    public void addSpouses(View view) {
        if (null == this.mSpouse) {
            this.mSpouse = new ArrayList<>();
        }
        if (null != view) {
            this.mSpouse.add(view);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getGId() {
        return gId;
    }

    public void setGId(Integer gId) {
        this.gId = gId;
    }

//    public Object getFatherId() {
//        return fatherId;
//    }
//
//    public void setFatherId(Object fatherId) {
//        this.fatherId = fatherId;
//    }

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

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

    //
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

    //
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

//    public Integer getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Integer createTime) {
//        this.createTime = createTime;
//    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
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
    public int getIsCelebrity() {
        return isCelebrity;
    }

    public void setIsCelebrity(int isCelebrity) {
        this.isCelebrity = isCelebrity;
    }

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


    public class GenerationBean implements Serializable {
        /**
         * men : 1
         * women : 2
         */

        private int men;
        private int women;

        public int getMen() {
            return men;
        }

        public void setMen(int men) {
            this.men = men;
        }

        public int getWomen() {
            return women;
        }

        public void setWomen(int women) {
            this.women = women;
        }
    }

    public List<GenerationBean> getGeneration() {
        return generation;
    }

    public void setGeneration(List<GenerationBean> generation) {
        this.generation = generation;
    }

    public SearchNearInBlood getUser() {
        return user;
    }

    public void setUser(SearchNearInBlood user) {
        this.user = user;
    }

}

