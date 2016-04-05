package com.example.exerciseapp.model;

/**
 * Created by lyjq on 2016/3/29.
 */
public class SingleGroup extends ErrorMsg{

    /**
     * leader_id : 1
     * createtime : 2016-03-24
     * tag_id : 1
     * group_name : 北邮跑步团
     * tag_name : 跑步
     * intro : 参加我们的团队吧
     * avatar : /Uploads/Picture/UserHeader/nAxxBtch6tE3u.png
     * membernum : 0
     * id : 24
     */

    private int leader_id;
    private String createtime;
    private int tag_id;
    private String group_name;
    private String sumrun;
    private String tag_name;
    private String intro;
    private String avatar;
    private int membernum;
    private int id;
    private String type = "";
    private String invite_id;
    private String applytime;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApplytime() {
        return applytime;
    }

    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getInvite_id() {
        return invite_id;
    }

    public void setInvite_id(String invite_id) {
        this.invite_id = invite_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSumrun() {
        return sumrun;
    }

    public void setSumrun(String sumrun) {
        this.sumrun = sumrun;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setMembernum(int membernum) {
        this.membernum = membernum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeader_id() {
        return leader_id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public int getTag_id() {
        return tag_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getTag_name() {
        return tag_name;
    }

    public String getIntro() {
        return intro;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getMembernum() {
        return membernum;
    }

    public int getId() {
        return id;
    }
}
