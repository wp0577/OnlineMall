package com.wp.common.pojo;

import java.io.Serializable;

/**
 * @program: WpMall
 * @description: return json from category object
 * @author: Pan wu
 * @create: 2018-09-24 11:16
 **/
public class CatResult implements Serializable {
    private long id;
    private String text;
    //open or closed
    private String state;

    public CatResult() {
    }

    public CatResult(long id, String text, String state) {
        this.id = id;
        this.text = text;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
