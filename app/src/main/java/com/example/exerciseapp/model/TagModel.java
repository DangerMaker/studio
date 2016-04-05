package com.example.exerciseapp.model;

/**
 * Created by lyjq on 2016/4/5.
 */
public class TagModel {

    /**
     * pic : http://101.200.214.68/Uploads/tagpic/paobu.png
     * id : 1
     * name : 跑步
     */
    private String pic;
    private int id;
    private String name;


    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
