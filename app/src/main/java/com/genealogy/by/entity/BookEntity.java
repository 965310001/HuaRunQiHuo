package com.genealogy.by.entity;

import java.io.Serializable;

public class BookEntity implements Serializable {

    /**
     * id : 32
     * title : 期权：就这么简单
     * description : 期权在发达资本市场是非常实用的风险对冲工具，期权的到来才真正意味着中国资本市场走向成熟，走向国际化。但期权对于中国投资者来说又是一个陌生的概念，有别于以往各种投资品种，对于没有接触过期权的人来说，期权的概念及交易方式很难理解。本书通过各种形象的描述、实际的案例、易于理解的方式，将期权交易的本质呈现出来，让中国投资者很容易找到轻松获利的期权交易方法。
     * author : 韩冬
     * images : http://wfqqreader-1252317822.image.myqcloud.com/cover/766/703766/b_703766.jpg
     * bid : 703766
     * type : 2
     * click_number : 2
     * create_time : 2019-04-13 09:50:52
     */

    private int id;
    private String title;
    private String description;
    private String author;
    private String images;
    private String bid;
    private int type;
    private int click_number;
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClick_number() {
        return click_number;
    }

    public void setClick_number(int click_number) {
        this.click_number = click_number;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
