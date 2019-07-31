package com.genealogy.by.entity;

import java.util.List;

public class MyLineageTableBean {

    private int index;
    private List<FamilyBook.LineageTableBean> lineageTableBeans;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<FamilyBook.LineageTableBean> getLineageTableBeans() {
        return lineageTableBeans;
    }

    public void setLineageTableBeans(List<FamilyBook.LineageTableBean> lineageTableBeans) {
        this.lineageTableBeans = lineageTableBeans;
    }
}