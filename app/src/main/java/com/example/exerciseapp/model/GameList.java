package com.example.exerciseapp.model;

import java.util.List;

/**
 * User: lyjq(1752095474)
 * Date: 2016-04-22
 */
public class GameList extends ErrorMsg {
    List<GameModel> data;

    public List<GameModel> getData() {
        return data;
    }

    public void setData(List<GameModel> data) {
        this.data = data;
    }
}
