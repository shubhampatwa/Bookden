package com.den.bookden;

/**
 * Created by Shubhi on 5/9/2016.
 */
public class NavDrawerItem {
    private boolean showNotify;
    private String title,type;


    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title,String type) {
        this.showNotify = showNotify;
        this.title = title;
        this.type=type;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}