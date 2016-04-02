package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by lyjq on 2016/3/30.
 */
public class GroupDetail extends ErrorMsg{

    /**
     * my_rank : {"username":"yinnananan","rank_string":"the rank of 1","uid":"4","avatar":"","point":"50"}
     * user_info_some_return : [{"username":"张睿","rank_string":"the rank of 2","uid":"6","avatar":"/Uploads/Picture/UserHeader/2015-11-29/rg9sGZ1TjyjJi.png","point":"45"},{"username":"yinnananan","rank_string":"the rank of 1","uid":"4","avatar":"","point":"50"}]
     * group_info : {"leader_id":"1","tag_id":"1","group_name":"这是新的团队名称","intro":"这是新的团队简介","avatar":"/Uploads/Picture/UserHeader/nAxxBtch6tE3u.png","membernum":"0","id":"29","createtime":"1458809713"}
     * user_info_point_some_return : [{"username":"yinnananan","rank_string":"the rank of 1","uid":"4","avatar":"","point":"50"},{"username":"张睿","rank_string":"the rank of 2","uid":"6","avatar":"/Uploads/Picture/UserHeader/2015-11-29/rg9sGZ1TjyjJi.png","point":"45"}]
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private MyRank my_rank;
        private GroupInfo group_info;
        private List<UserInfoSome> user_info_some_return;
        private List<UserInfoSome> user_info_point_some_return;

        public MyRank getMy_rank() {
            return my_rank;
        }

        public void setMy_rank(MyRank my_rank) {
            this.my_rank = my_rank;
        }

        public GroupInfo getGroup_info() {
            return group_info;
        }

        public void setGroup_info(GroupInfo group_info) {
            this.group_info = group_info;
        }

        public List<UserInfoSome> getUser_info_some_return() {
            return user_info_some_return;
        }

        public void setUser_info_some_return(List<UserInfoSome> user_info_some_return) {
            this.user_info_some_return = user_info_some_return;
        }

        public List<UserInfoSome> getUser_info_point_some_return() {
            return user_info_point_some_return;
        }

        public void setUser_info_point_some_return(List<UserInfoSome> user_info_point_some_return) {
            this.user_info_point_some_return = user_info_point_some_return;
        }
    }
}
