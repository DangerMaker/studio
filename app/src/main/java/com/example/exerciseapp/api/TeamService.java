package com.example.exerciseapp.api;

import com.example.exerciseapp.model.CreateSuc;
import com.example.exerciseapp.model.ErrorMsg;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by lyjq on 2016/3/24.
 */
public interface TeamService{

    @FormUrlEncoded
    @POST("/py/group")
    void createTeam(@Field("uid") String uid,
                    @Field("group_name") String group_name,
                    @Field("group_intro") String group_intro,
                    @Field("group_tag_id") String group_tag_id,
                    @Field("action") String create_group,
                    Callback<CreateSuc> callback);

    /**
     *
     * @param id 团队id
     * @param new_groupname 新的团队名称
     * @param param 要更改的参数为团队名称
     * @param change_param
     * @param callback
     */

    @FormUrlEncoded
    @POST("/py/group")
    void alterTeamName(@Field("id") String id,
                    @Field("new_groupname") String new_groupname,
                    @Field("param") String param,
                    @Field("action") String change_param,
                    Callback<ErrorMsg> callback);

    /**
     *
     * @param id 团队id
     * @param tel_str 多个手机号码中间用逗号连接
     * @param invite_friends
     * @param callback
     */

    @FormUrlEncoded
    @POST("/py/invite")
    void inviteFriends(@Field("id") String id,
                       @Field("tel_str") String tel_str,
                       @Field("action") String invite_friends,
                       Callback<ErrorMsg> callback);

    /**
     *
     * @param uid 用户uid
     */
    @GET("/py/invite")
    void getAllInvite(@Query("uid") String uid,
                      @Query("action") String get_all_invite,
                      Callback<ErrorMsg> callback);

    /**
     *
     * @param id 该次邀请的id (索引）
     */

    @GET("/py/invite")
    void passInvite(@Query("id") String id,
                    @Query("action") String pass_invite,
                    Callback<ErrorMsg> callback);

    /**
     *
     * @param id 该次邀请的id (索引）
     */

    @GET("/py/invite")
    void refuseInvite(@Query("id") String id,
                      @Query("action") String refuse_invite,
                      Callback<ErrorMsg> callback);

    /**
     *
     * @param id 要加入的团队的id
     * @param excuse 申请理由
     * @param uid 用户uid
     */

    @FormUrlEncoded
    @POST("/py/apply")
    void postApply(@Field("id") String id,
                   @Field("excuse") String excuse,
                   @Field("uid") String uid,
                   Callback<ErrorMsg> callback);


}
