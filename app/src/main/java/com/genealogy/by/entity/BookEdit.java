package com.genealogy.by.entity;

import java.io.Serializable;
import java.util.List;

public class BookEdit implements Serializable {

    /**
     * id : 19
     * familybookName : 嘿嘿嘿
     * sponsor :
     * catalogingAddress :
     * editorialCommittee :
     * genealogyPreface :
     * lastNameSource :
     * familyRule :
     * gId :
     * characterBiography :
     * bigNote :
     * postscript :
     * editingTime :
     * editingTimeCN :
     * lineageTable : []
     * familyPhoto : []
     */

    private int id;
    private String familybookName;
    private String sponsor;
    private String catalogingAddress;
    private String editorialCommittee;
    private String genealogyPreface;
    private String lastNameSource;
    private String familyRule;
    private String gId;
    private String characterBiography;
    private String bigNote;
    private String postscript;
    private String editingTime;
    private String editingTimeCN;
    private List<FamilyBook.LineageTableBean> lineageTable;
//    private List<String> familyPhoto;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFamilybookName() {
        return familybookName;
    }

    public void setFamilybookName(String familybookName) {
        this.familybookName = familybookName;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getCatalogingAddress() {
        return catalogingAddress;
    }

    public void setCatalogingAddress(String catalogingAddress) {
        this.catalogingAddress = catalogingAddress;
    }

    public String getEditorialCommittee() {
        return editorialCommittee;
    }

    public void setEditorialCommittee(String editorialCommittee) {
        this.editorialCommittee = editorialCommittee;
    }

    public String getGenealogyPreface() {
        return genealogyPreface;
    }

    public void setGenealogyPreface(String genealogyPreface) {
        this.genealogyPreface = genealogyPreface;
    }

    public String getLastNameSource() {
        return lastNameSource;
    }

    public void setLastNameSource(String lastNameSource) {
        this.lastNameSource = lastNameSource;
    }

    public String getFamilyRule() {
        return familyRule;
    }

    public void setFamilyRule(String familyRule) {
        this.familyRule = familyRule;
    }

    public String getGId() {
        return gId;
    }

    public void setGId(String gId) {
        this.gId = gId;
    }

    public String getCharacterBiography() {
        return characterBiography;
    }

    public void setCharacterBiography(String characterBiography) {
        this.characterBiography = characterBiography;
    }

    public String getBigNote() {
        return bigNote;
    }

    public void setBigNote(String bigNote) {
        this.bigNote = bigNote;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public String getEditingTime() {
        return editingTime;
    }

    public void setEditingTime(String editingTime) {
        this.editingTime = editingTime;
    }

    public String getEditingTimeCN() {
        return editingTimeCN;
    }

    public void setEditingTimeCN(String editingTimeCN) {
        this.editingTimeCN = editingTimeCN;
    }

    public List<?> getLineageTable() {
        return lineageTable;
    }

    public void setLineageTable(List<FamilyBook.LineageTableBean> lineageTable) {
        this.lineageTable = lineageTable;
    }

//    public List<String> getFamilyPhoto() {
//        return familyPhoto;
//    }
//
//    public void setFamilyPhoto(List<String> familyPhoto) {
//        this.familyPhoto = familyPhoto;
//    }
}
