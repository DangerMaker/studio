package com.example.exerciseapp.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/4.
 */
public class OrgPubInfo extends ErrorMsg{

    /**
     * notice : ["我们是Hige豪机构","加入我们把","健康生活，油腻没我"]
     * need_check : 1
     * name : 北京房山机构
     * status_name : 加入机构
     * has_focus : 0
     * stars : http://101.200.214.68/staticPic/stars/5.png
     * join_type : 0
     * img_path : http://101.200.214.68/Uploads/pic1.jpg
     * score : 152633
     * create_time : 2016-8-23
     * members : 59636963
     * address : 北京房山区
     * athletics : ["足球","篮球"]
     * can_attend : 1
     * intro : 这个是简介，这是一个很多啊的自二回，单价法金佛激发非积分hi爱上就覅安徽省覅和覅活法法海胡覅按时
     * id : 3
     * desc : 新的宣言
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int need_check;
        private String name;
        private String status_name;
        private int has_focus;
        private String stars;
        private int join_type;
        private String img_path;
        private int score;
        private String create_time;
        private int members;
        private String address;
        private int can_attend;
        private String intro;
        private int id;
        private String desc;
        private List<String> notice;
        private List<String> athletics;

        public int getNeed_check() {
            return need_check;
        }

        public void setNeed_check(int need_check) {
            this.need_check = need_check;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus_name() {
            return status_name;
        }

        public void setStatus_name(String status_name) {
            this.status_name = status_name;
        }

        public int getHas_focus() {
            return has_focus;
        }

        public void setHas_focus(int has_focus) {
            this.has_focus = has_focus;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public int getJoin_type() {
            return join_type;
        }

        public void setJoin_type(int join_type) {
            this.join_type = join_type;
        }

        public String getImg_path() {
            return img_path;
        }

        public void setImg_path(String img_path) {
            this.img_path = img_path;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getMembers() {
            return members;
        }

        public void setMembers(int members) {
            this.members = members;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCan_attend() {
            return can_attend;
        }

        public void setCan_attend(int can_attend) {
            this.can_attend = can_attend;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<String> getNotice() {
            return notice;
        }

        public void setNotice(List<String> notice) {
            this.notice = notice;
        }

        public List<String> getAthletics() {
            return athletics;
        }

        public void setAthletics(List<String> athletics) {
            this.athletics = athletics;
        }
    }
}
