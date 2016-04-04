package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by lyjq on 2016/4/3.
 */
public class AllMember extends ErrorMsg{

    /**
     * username : 张睿
     * avatar : /Uploads/Picture/UserHeader/2015-11-29/rg9sGZ1TjyjJi.png
     * uid : 6
     */

    private List<Member> data;

    public void setData(List<Member> data) {
        this.data = data;
    }

    public List<Member> getData() {
        return data;
    }

}
