package com.example.ys.stu.model;

import java.io.Serializable;

/**
 * 作者： Ys
 * 日期： 2016/4/30
 * 功能：Homework实例类，实现Serializable实例化接口，才可以用Bundle传值
 */
public class Data implements Serializable {

    private int data_id;
    private String userId;
    private String title;
    private String content;
    private String mPath;
    private String mTime;

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }


    public int getData_id() {
        return data_id;
    }

    public void setData_id(int data_id) {
        this.data_id = data_id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
