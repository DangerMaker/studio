package com.example.exerciseapp.model;

/**
 * Created by lyjq on 2016/3/28.
 */
public class CreateSuc extends ErrorMsg{

    /**
     * flag : 0
     * data : {"id":57}
     * result : 1
     * desc : success
     */

    /**
     * id : 57
     */

    private DataEntity data;


    public void setData(DataEntity data) {
        this.data = data;
    }


    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private int id;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
