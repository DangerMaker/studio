package com.example.exerciseapp.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyjq on 2016/3/27.
 */
public class ErrorMsg {

    /**
     * flag : 0
     * data : {}
     * result : 1
     * desc : success
     */

    private int flag;
    private int result;
    private String desc;

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getFlag() {
        return flag;
    }

    public int getResult() {
        return result;
    }

    public String getDesc() {
        return desc;
    }
}
