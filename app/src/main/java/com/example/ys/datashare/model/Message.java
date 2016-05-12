package com.example.ys.datashare.model;

/**
 * 作者： Ys
 * 日期： 2016/5/11
 * 功能：
 */
public class Message {
    private int id;
    private String user_s;
    private String user_r;
    private String title;
    private String text;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_s() {
        return user_s;
    }

    public void setUser_s(String user_s) {
        this.user_s = user_s;
    }

    public String getUser_r() {
        return user_r;
    }

    public void setUser_r(String user_r) {
        this.user_r = user_r;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
