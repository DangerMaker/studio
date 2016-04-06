package com.example.exerciseapp.model;

/**
 * Created by lyjq on 2016/4/6.
 */
public class GroupInstance {

    private static SingleGroup group = new SingleGroup();

    public static SingleGroup getInstance(){
        return group;
    }
}
