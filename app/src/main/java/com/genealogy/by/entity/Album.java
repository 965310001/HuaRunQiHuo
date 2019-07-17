package com.genealogy.by.entity;

import java.io.Serializable;

public class Album implements Serializable {

    private int id;
    private String src;
    private String text;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", src='" + src + '\'' +
                ", text='" + text + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
