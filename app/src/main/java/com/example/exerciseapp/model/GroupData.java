package com.example.exerciseapp.model;

/**
 * Created by lyjq on 2016/4/5.
 */
public class GroupData extends  ErrorMsg{

    private SingleGroup data;

    public void setData(SingleGroup data) {
        this.data = data;
    }

    public SingleGroup getData() {
        return data;
    }
}
