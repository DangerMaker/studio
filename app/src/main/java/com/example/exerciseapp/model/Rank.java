package com.example.exerciseapp.model;

/**
 * Created by lyjq on 2016/4/6.
 */
public class Rank {

    /**
     * username : yinnananan
     * rank_string : the rank of 1
     * uid : 4
     * avatar :
     * point : 50
     */

    private String username;
    private String rank_string;
    private String uid;
    private String avatar;
    private String point;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRank_string(String rank_string) {
        this.rank_string = rank_string;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getUsername() {
        return username;
    }

    public String getRank_string() {
        return rank_string;
    }

    public String getUid() {
        return uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPoint() {
        return point;
    }
}
