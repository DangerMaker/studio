package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by lyjq on 2016/3/29.
 */
public class AllGroup extends ErrorMsg {

    /**
     * leader_group_info_return : []
     * group_info_return : [{"leader_id":1,"createtime":"2016-03-24","tag_id":1,"group_name":"北邮跑步团","tag_name":"跑步","intro":"参加我们的团队吧","avatar":"/Uploads/Picture/UserHeader/nAxxBtch6tE3u.png","membernum":0,"id":24},{"leader_id":"1","tag_id":"1","group_name":"这是新的团队名称","intro":"这是新的团队简介","avatar":"/Uploads/Picture/UserHeader/nAxxBtch6tE3u.png","membernum":"0","id":"29","createtime":"1458809713"}]
     * group_num : 2
     */

    private DataEntity data;


    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private int group_num;
        private List<SingleGroup> leader_group_info_return;
        private List<SingleGroup> group_info_return;

        public int getGroup_num() {
            return group_num;
        }

        public void setGroup_num(int group_num) {
            this.group_num = group_num;
        }

        public List<SingleGroup> getLeader_group_info_return() {
            return leader_group_info_return;
        }

        public void setLeader_group_info_return(List<SingleGroup> leader_group_info_return) {
            this.leader_group_info_return = leader_group_info_return;
        }

        public List<SingleGroup> getGroup_info_return() {
            return group_info_return;
        }

        public void setGroup_info_return(List<SingleGroup> group_info_return) {
            this.group_info_return = group_info_return;
        }
    }
}
