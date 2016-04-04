package com.example.exerciseapp.model;

/**
 * Created by lyjq on 2016/4/3.
 */
public class Member {

    /**
     * username : 张睿
     * avatar : /Uploads/Picture/UserHeader/2015-11-29/rg9sGZ1TjyjJi.png
     * uid : 6
     */

    private String username;
    private String avatar;
    private String uid;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUid() {
        return uid;
    }
}
