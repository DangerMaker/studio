package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by lyjq on 2016/4/6.
 */
public class AllRank extends ErrorMsg{
    List<Rank> data;

    public List<Rank> getData() {
        return data;
    }

    public void setData(List<Rank> data) {
        this.data = data;
    }
}
