package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by lyjq on 2016/4/4.
 */
public class GroupList extends ErrorMsg {
    List<SingleGroup> data;

    public List<SingleGroup> getData() {
        return data;
    }

    public void setData(List<SingleGroup> data) {
        this.data = data;
    }
}
