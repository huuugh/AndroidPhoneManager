package com.app.hugh.androidphonemanager.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by hs on 2016/3/31.
 */
public class AppInfo
{
    String label;
    Drawable icon;
    boolean isInSDcard;
    boolean isSystemApp;
    String packagename;

    public AppInfo(String label, Drawable icon, boolean isInSDcard, boolean isSystemApp, String packagename) {
        this.label = label;
        this.icon = icon;
        this.isInSDcard = isInSDcard;
        this.isSystemApp = isSystemApp;
        this.packagename = packagename;
    }

    public String getPackagename() {

        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public AppInfo(String label, Drawable icon, boolean isInSDcard, boolean isSystemApp) {
        this.label = label;
        this.icon = icon;
        this.isInSDcard = isInSDcard;
        this.isSystemApp = isSystemApp;
    }

    public String getLabel() {
        return label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public boolean isInSDcard() {
        return isInSDcard;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIsInSDcard(boolean isInSDcard) {
        this.isInSDcard = isInSDcard;
    }

    public void setIsSystemApp(boolean isSystemApp) {
        this.isSystemApp = isSystemApp;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "label='" + label + '\'' +
                ", icon=" + icon +
                ", isInSDcard=" + isInSDcard +
                ", isSystemApp=" + isSystemApp +
                '}';
    }
}
