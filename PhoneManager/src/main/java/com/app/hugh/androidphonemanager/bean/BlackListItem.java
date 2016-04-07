package com.app.hugh.androidphonemanager.bean;

/**
 * Created by hs on 2016/4/5.
 */
public class BlackListItem
{
    String number;
    int type;

    public BlackListItem(String number, int type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
