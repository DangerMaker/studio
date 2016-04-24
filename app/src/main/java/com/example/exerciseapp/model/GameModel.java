package com.example.exerciseapp.model;

/**
 * User: lyjq(1752095474)
 * Date: 2016-04-22
 */
public class GameModel {

    /**
     * epayfee : 0
     * id : 29
     * is_event : 1
     * name : 篮球
     * group_max : 12
     */

    private String epayfee;
    private int id;
    private int is_event;
    private String name;
    private int group_max;
    private boolean flag = false;

    public String getEpayfee() {
        return epayfee;
    }

    public void setEpayfee(String epayfee) {
        this.epayfee = epayfee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_event() {
        return is_event;
    }

    public void setIs_event(int is_event) {
        this.is_event = is_event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup_max() {
        return group_max;
    }

    public void setGroup_max(int group_max) {
        this.group_max = group_max;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
