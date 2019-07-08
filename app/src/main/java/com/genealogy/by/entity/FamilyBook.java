package com.genealogy.by.entity;

import com.google.gson.Gson;

import java.util.List;

public class FamilyBook {


    /**
     * id : 19
     * familybookName :
     * sponsor :
     * catalogingAddress :
     * editorialCommittee :
     * genealogyPreface :
     * lastNameSource :
     * familyRule :
     * gId : 17
     * characterBiography :
     * bigNote :
     * postscript :
     * editingTime :
     * editingTimeCN :
     * lineageTable : [{"id":"","lineage":1,"surname":"","name":"","birthday":"","deathTime":"","spouses":[],"parents":[],"child":"","ranking":"","profilePhoto":"","family":"","introduction":""}]
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
    private int gId;
    private String characterBiography;
    private String bigNote;
    private String postscript;
    private String editingTime;
    private String editingTimeCN;
    private List<LineageTableBean> lineageTable;
    private List<String> familyPhoto;
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

    public int getGId() {
        return gId;
    }

    public void setGId(int gId) {
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

    public List<LineageTableBean> getLineageTable() {
        return lineageTable;
    }

    public void setLineageTable(List<LineageTableBean> lineageTable) {
        this.lineageTable = lineageTable;
    }

    public List<String> getFamilyPhoto() {
        return familyPhoto;
    }

    public void setFamilyPhoto(List<String> familyPhoto) {
        this.familyPhoto = familyPhoto;
    }

    public static class LineageTableBean {
        /**
         * id :
         * lineage : 1
         * surname :
         * name :
         * birthday :
         * deathTime :
         * spouses : []
         * parents : []
         * child :
         * ranking :
         * profilePhoto :
         * family :
         * introduction :
         */

        private String id;
        private int lineage;
        private String surname;
        private String name;
        private String birthday;
        private String deathTime;
        private String child;
        private String ranking;
        private String profilePhoto;
        private String family;
        private String introduction;
        private List<?> spouses;
        private List<?> parents;

        public static LineageTableBean objectFromData(String str) {

            return new Gson().fromJson(str, LineageTableBean.class);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getLineage() {
            return lineage;
        }

        public void setLineage(int lineage) {
            this.lineage = lineage;
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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getDeathTime() {
            return deathTime;
        }

        public void setDeathTime(String deathTime) {
            this.deathTime = deathTime;
        }

        public String getChild() {
            return child;
        }

        public void setChild(String child) {
            this.child = child;
        }

        public String getRanking() {
            return ranking;
        }

        public void setRanking(String ranking) {
            this.ranking = ranking;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public String getFamily() {
            return family;
        }

        public void setFamily(String family) {
            this.family = family;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public List<?> getSpouses() {
            return spouses;
        }

        public void setSpouses(List<?> spouses) {
            this.spouses = spouses;
        }

        public List<?> getParents() {
            return parents;
        }

        public void setParents(List<?> parents) {
            this.parents = parents;
        }
    }
}
