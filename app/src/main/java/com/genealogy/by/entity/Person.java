package com.genealogy.by.entity;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {
    /**
     * id : 131585
     * gId : 1
     * fatherId :
     * motherId :
     * inviteId :
     * account :
     * spouseId : 131586
     * surname : 张
     * name : 芳澜
     * sex : 1
     * profilePhoto :
     * birthday : -490176000000
     * phone :
     * password :
     * health : 0
     * height : 167
     * bloodGroup : b
     * ancestralHome : 广西罗成
     * currentResidence : 广西罗城仫佬族自治县小长安镇崖宜村
     * wordGeneration : 芳
     * school : 广西理工职业技术学校
     * education : 专科
     * email : 1001@qq.com
     * unit : 苏宁易购
     * position : 运维
     * mark :
     * createTime :
     * ranking :
     * commonName :
     * word :
     * number :
     * designation :
     * noun :
     * usedName :
     * minName :
     * birthArea :
     * birthPlace :
     * deathTime :
     * dieAddress :
     * buriedArea :
     * deathPlace :
     * yearOfLife :
     * nationality :
     * moveOut :
     * industry :
     * url :
     * isCelebrity :
     * genealogyImage :
     * idCard :
     * latestUpdatetime :
     * branch :
     * remark :
     * oldId : 0
     * nickName : 张芳澜
     * relationship : 妈妈-----m
     * geneticDisease :
     * children : []
     * spouse : []
     * deeds :
     */
    private int id;
    private int gId;
    private String fatherId;
    private String motherId;
    private String inviteId;
    private String account;
    private String spouseId;
    private String surname;
    private String name;
    private int sex;
    private String profilePhoto;
    private long birthday;
    private String phone;
    private String password;
    private int health;
    private int height;
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
    private String createTime;
    private String ranking;
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
    private String yearOfLife;
    private String nationality;
    private String moveOut;
    private String industry;
    private String url;
    private String isCelebrity;
    private String genealogyImage;
    private String idCard;
    private String latestUpdatetime;
    private String branch;
    private String remark;
    private int oldId;
    private String nickName;
    private String relationship;
    private String geneticDisease;
    private String deeds;
    private List<Person> children;
    private List<Person> spouse;

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

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

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

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
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

    public String getYearOfLife() {
        return yearOfLife;
    }

    public void setYearOfLife(String yearOfLife) {
        this.yearOfLife = yearOfLife;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMoveOut() {
        return moveOut;
    }

    public void setMoveOut(String moveOut) {
        this.moveOut = moveOut;
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

    public String getIsCelebrity() {
        return isCelebrity;
    }

    public void setIsCelebrity(String isCelebrity) {
        this.isCelebrity = isCelebrity;
    }

    public String getGenealogyImage() {
        return genealogyImage;
    }

    public void setGenealogyImage(String genealogyImage) {
        this.genealogyImage = genealogyImage;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLatestUpdatetime() {
        return latestUpdatetime;
    }

    public void setLatestUpdatetime(String latestUpdatetime) {
        this.latestUpdatetime = latestUpdatetime;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOldId() {
        return oldId;
    }

    public void setOldId(int oldId) {
        this.oldId = oldId;
    }

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

    public String getGeneticDisease() {
        return geneticDisease;
    }

    public void setGeneticDisease(String geneticDisease) {
        this.geneticDisease = geneticDisease;
    }

    public String getDeeds() {
        return deeds;
    }

    public void setDeeds(String deeds) {
        this.deeds = deeds;
    }

    public List<Person> getchildren() {
        return children;
    }

    public void setchildren(List<Person> children) {
        this.children = children;
    }

    public List<Person> getspouse() {
        return spouse;
    }

    public void setspouse(List<Person> spouse) {
        this.spouse = spouse;
    }
}
