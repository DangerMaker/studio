package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by lyjq on 2016/4/5.
 */
public class AllTag extends ErrorMsg{
    List<TagModel> data;

    public List<TagModel> getList() {
        return data;
    }

    public void setList(List<TagModel> list) {
        this.data = list;
    }
}
