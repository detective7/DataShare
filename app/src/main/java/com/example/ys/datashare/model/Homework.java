package com.example.ys.datashare.model;

/**
 * 作者： Ys
 * 日期： 2016/4/30
 * 功能：
 */
public class Homework {

    private int hw_id;
    private String teacher;
    private String toClass;
    private String title;
    private String content;
    private String material;
    private String time;



    public int getHw_id() {
        return hw_id;
    }

    public void setHw_id(int hw_id) {
        this.hw_id = hw_id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getToClass() {
        return toClass;
    }

    public void setToClass(String toClass) {
        this.toClass = toClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
