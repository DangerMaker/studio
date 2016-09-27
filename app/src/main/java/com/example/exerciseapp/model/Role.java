package com.example.exerciseapp.model;

/**
 * Created by Administrator on 2016/9/11.
 */
public class Role extends ErrorMsg {

    /**
     * role_id : 1
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int role_id;

        public int getRole_id() {
            return role_id;
        }

        public void setRole_id(int role_id) {
            this.role_id = role_id;
        }
    }
}
