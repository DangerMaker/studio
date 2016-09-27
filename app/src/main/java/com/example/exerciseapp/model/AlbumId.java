package com.example.exerciseapp.model;

/**
 * Created by Administrator on 2016/9/9.
 */
public class AlbumId extends ErrorMsg {

    /**
     * id : 37
     * name : 北邮二日
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
