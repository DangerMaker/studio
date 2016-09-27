package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/4.
 */
public class ApplyList extends ErrorMsg {

    /**
     * per_page : 8
     * apply_list : [{"user_id":309,"msg":"是不是","avatar":"http://101.200.214.68/Uploads/head.jpg","change_date":"2016-08-27 19:33","nickname":"小跑男","id":11},{"user_id":308,"msg":"是不是","avatar":"http://101.200.214.68/Uploads/head.jpg","change_date":"2016-08-27 19:33","nickname":"小跑男","id":10},{"user_id":307,"msg":"是不是","avatar":"http://101.200.214.68/Uploads/head.jpg","change_date":"2016-08-27 19:33","nickname":"小跑男","id":9},{"user_id":306,"msg":"是不是","avatar":"http://101.200.214.68/Uploads/head.jpg","change_date":"2016-08-27 19:33","nickname":"小跑男","id":8},{"user_id":303,"msg":"是不是","avatar":"http://101.200.214.68/Uploads/head.jpg","change_date":"2016-08-27 19:33","nickname":"小跑男","id":6},{"user_id":302,"msg":"是不是","avatar":"http://101.200.214.68/Uploads/head.jpg","change_date":"2016-08-27 19:33","nickname":"小跑男","id":5},{"user_id":325,"msg":"是不是","avatar":"http://101.200.214.68/Uploads/head.jpg","change_date":"2016-08-27 19:33","nickname":"小跑男","id":7}]
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int per_page;
        /**
         * user_id : 309
         * msg : 是不是
         * avatar : http://101.200.214.68/Uploads/head.jpg
         * change_date : 2016-08-27 19:33
         * nickname : 小跑男
         * id : 11
         */

        private List<ApplyListBean> apply_list;

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public List<ApplyListBean> getApply_list() {
            return apply_list;
        }

        public void setApply_list(List<ApplyListBean> apply_list) {
            this.apply_list = apply_list;
        }

        public static class ApplyListBean {
            private int user_id;
            private String msg;
            private String avatar;
            private String change_date;
            private String nickname;
            private int id;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getChange_date() {
                return change_date;
            }

            public void setChange_date(String change_date) {
                this.change_date = change_date;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
