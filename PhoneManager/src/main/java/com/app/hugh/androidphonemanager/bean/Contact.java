package com.app.hugh.androidphonemanager.bean;

/**
 * Created by hs on 2016/3/28.
 */
public class Contact
{
    private String num;
    private String name;

    public Contact(String num, String name) {
        this.num = num;
        this.name = name;
    }

    public Contact() {
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
