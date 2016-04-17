package com.example.exerciseapp.api;

import com.example.exerciseapp.model.AllGroup;
import com.example.exerciseapp.model.AllMember;
import com.example.exerciseapp.model.AllRank;
import com.example.exerciseapp.model.AllTag;
import com.example.exerciseapp.model.CreateSuc;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.GroupData;
import com.example.exerciseapp.model.GroupDetail;
import com.example.exerciseapp.model.GroupList;
import com.example.exerciseapp.model.SingleGroup;
import com.example.exerciseapp.model.UpTeamAvatar;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by lyjq on 2016/3/24.
 */
public interface TeamService {

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
     * @param id
     * @param tel
     * @param invite_friends
     * @param callback
     */

    @FormUrlEncoded
    @POST("/py/invite")
    void inviteFriends(@Field("id") String id,
                       @Field("tel") String tel,
                       @Field("action") String invite_friends,
                       Callback<ErrorMsg> callback);

    /**
     * @param uid 用户uid
     */
    @GET("/py/invite")
    void getAllInvite(@Query("uid") String uid,
                      @Query("action") String get_all_invite,
                      Callback<ErrorMsg> callback);

    /**
     * @param id 该次邀请的id (索引）
     */

    @GET("/py/invite")
    void passInvite(@Query("id") String id,
                    @Query("action") String pass_invite,
                    Callback<ErrorMsg> callback);

    /**
     * @param id 该次邀请的id (索引）
     */

    @GET("/py/invite")
    void refuseInvite(@Query("id") String id,
                      @Query("action") String refuse_invite,
                      Callback<ErrorMsg> callback);

    /**
     * @param id     要加入的团队的id
     * @param excuse 申请理由
     * @param uid    用户uid
     */

    @FormUrlEncoded
    @POST("/py/apply")
    void postApply(@Field("id") String id,
                   @Field("excuse") String excuse,
                   @Field("uid") String uid,
                   @Field("action") String post_apply,
                   Callback<ErrorMsg> callback);

    /**
     * 自己的group
     * @param id
     * @param get_all_group
     * @param callback
     */

    @GET("/py/group")
    void getAllGroup(@Query("uid") String id,
                     @Query("action") String get_all_group,
                     Callback<AllGroup> callback);

    @GET("/py/group")
    void getDetailInfo(@Query("id") String id,
                       @Query("uid") String uid,
                       @Query("action") String get_detail_group_info,
                       Callback<GroupDetail> callback);

    @FormUrlEncoded
    @POST("/py/group")
    void changeTeamName(@Field("id") String id,
                        @Field("new_groupname") String new_groupname,
                        @Field("param") String param,
                        @Field("action") String change_param,
                        Callback<ErrorMsg> callback);

    @FormUrlEncoded
    @POST("/py/group")
    void changeTeamDes(@Field("id") String id,
                        @Field("new_group_intro") String new_group_intro,
                        @Field("param") String param,
                        @Field("action") String change_param,
                        Callback<ErrorMsg> callback);

    @Multipart
    @POST("/index.php/Api/Users/updateHeaderNew")
    void uploadAvatar(@Part("myAvatar") TypedFile avatar, @Part("uid") String uid, Callback<UpTeamAvatar> callback);

    @Multipart
    @POST("/index.php/Api/Users/updateGroupAvatarAnd")
    void uploadTeamAvatar(@Part("group_avatar") TypedFile avatar, @Part("id") String id, Callback<UpTeamAvatar> callback);


    @GET("/py/group")
    void showAllMembers(@Query("id") String id,
                       @Query("action") String show_all_members,
                       Callback<AllMember> callback);

    @GET("/py/group")
    void showAllRank(@Query("id") String id,
                        @Query("action") String show_all_rank,
                        Callback<AllRank> callback);

    @GET("/py/group")
    void breakGroup(@Query("id") int id,
                        @Query("uid") String uid,
                        @Query("action") String break_group,
                        Callback<ErrorMsg> callback);

    @GET("/py/group")
    void exitGroup(@Query("id") int id,
                    @Query("uid") String uid,
                    @Query("action") String exit_group,
                    Callback<ErrorMsg> callback);

    /**
     * 全部
     * @param callback
     */
    @GET("/py/group?action=get_group_list")
    void getGroupList(Callback<GroupList> callback);

    @GET("/py/group?action=get_group_find")
    void getGroupFind(@Query("find_name") String find_name,
            Callback<GroupList> callback);

    @GET("/py/apply")
    void passApply(@Query("id") String id,
                   @Query("action") String pass_apply,
                   Callback<ErrorMsg> callback);

    @GET("/py/apply")
    void refuseApply(@Query("id") String id,
                   @Query("action") String refuse_apply,
                   Callback<ErrorMsg> callback);

    @GET("/py/tag")
    void getTag(Callback<AllTag> callback);

    @GET("/py/group")
    void getGroupInfo(@Query("id") String id,
                @Query("action") String get_group_info,
            Callback<GroupData> callback);

}
