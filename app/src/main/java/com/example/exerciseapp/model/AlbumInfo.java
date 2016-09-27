package com.example.exerciseapp.model;

/**
 * Created by Administrator on 2016/9/18.
 */
public class AlbumInfo extends ErrorMsg{

    /**
     * count : 3
     * name : 北邮一日游777
     * label : null
     * organization_id : 3
     * create_user : 1
     * create_time : 2016-09-03 15:20:09
     * msg :
     * show_times : 4
     * id : 12
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int count;
        private String name;
        private Object label;
        private int organization_id;
        private int create_user;
        private String create_time;
        private String msg;
        private int show_times;
        private int id;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getLabel() {
            return label;
        }

        public void setLabel(Object label) {
            this.label = label;
        }

        public int getOrganization_id() {
            return organization_id;
        }

        public void setOrganization_id(int organization_id) {
            this.organization_id = organization_id;
        }

        public int getCreate_user() {
            return create_user;
        }

        public void setCreate_user(int create_user) {
            this.create_user = create_user;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getShow_times() {
            return show_times;
        }

        public void setShow_times(int show_times) {
            this.show_times = show_times;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
