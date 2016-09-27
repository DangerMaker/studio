package com.example.exerciseapp.api;

import com.example.exerciseapp.model.Ads;
import com.example.exerciseapp.model.AllGroup;
import com.example.exerciseapp.model.AllMember;
import com.example.exerciseapp.model.AllRank;
import com.example.exerciseapp.model.AllTag;
import com.example.exerciseapp.model.CreateSuc;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.GameList;
import com.example.exerciseapp.model.GroupData;
import com.example.exerciseapp.model.GroupDetail;
import com.example.exerciseapp.model.GroupList;
import com.example.exerciseapp.model.OrganizeList;
import com.example.exerciseapp.model.UpTeamAvatar;
import com.example.exerciseapp.model.UploadInteraction;

import retrofit.Callback;
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
                    @Field("token") String token,
                    @Field("version") String version,
                    Callback<CreateSuc> callback);

    /**
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
                       @Field("token") String token,
                       @Field("version") String version,
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
                    @Query("token") String token,
                    @Query("version") String version,
                    @Query("uid") String uid,
                    Callback<ErrorMsg> callback);

    /**
     * @param id 该次邀请的id (索引）
     */

    @GET("/py/invite")
    void refuseInvite(@Query("id") String id,
                      @Query("action") String refuse_invite,
                      @Query("token") String token,
                      @Query("version") String version,
                      @Query("uid") String uid,
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
     *
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
                        @Query("token") String token,
                        @Query("version") String version,
                        @Query("uid") String uid,
                        Callback<ErrorMsg> callback);

    @FormUrlEncoded
    @POST("/py/group")
    void changeTeamDes(@Field("id") String id,
                       @Field("new_group_intro") String new_group_intro,
                       @Field("param") String param,
                       @Field("action") String change_param,
                       @Query("token") String token,
                       @Query("version") String version,
                       @Query("uid") String uid,
                       Callback<ErrorMsg> callback);

    @Multipart
    @POST("/index.php/Api/Users/updateHeaderNew")
    void uploadAvatar(@Part("myAvatar") TypedFile avatar, @Part("uid") String uid, Callback<UpTeamAvatar> callback);

    @Multipart
    @POST("/index.php/Api/Users/uploadPostPic")
    void uploadPicture(@Part("postPic") TypedFile avatar, Callback<UploadInteraction> callback);

    @Multipart
    @POST("/index.php/Api/Users/uploadCirBack")
    void uploadbgPicture(@Part("pic") TypedFile avatar, @Part("token") String token,
                         @Part("version") String version,
                         @Part("uid") String uid, Callback<ErrorMsg> callback);

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
                    @Query("token") String token,
                    @Query("version") String version,
                    Callback<ErrorMsg> callback);

    @GET("/py/group")
    void exitGroup(@Query("id") int id,
                   @Query("uid") String uid,
                   @Query("action") String exit_group,
                   @Query("token") String token,
                   @Query("version") String version,
                   Callback<ErrorMsg> callback);

    /**
     * 全部
     *
     * @param callback
     */
    @GET("/py/group?action=get_group_list")
    void getGroupList(@Query("action") String get_group_list,
                      Callback<GroupList> callback);

    @GET("/py/group?action=get_group_find")
    void getGroupFind(@Query("find_name") String find_name,
                      Callback<GroupList> callback);

    @GET("/py/group?action=get_group_list")
    void getGrouplists(@Query("action") String get_group_list,
                       @Query("page") int page,
                       @Query("version") String version,
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

    @GET("/py/attend?action=person_attend")
    void applyPersonal(@Query("gid") String gid,
                       @Query("eid") String eid,
                       @Query("etel") String etel,
                       @Query("esex") String esex,
                       @Query("eidcard") String eidcard,
                       @Query("eusername") String eusername,
                       @Query("org_name") String org_name,
                       @Query("uid") String uid,
                       @Query("token") String token,
                       @Query("version") String version,
                       Callback<CreateSuc> callback);

    @GET("/py/attend?action=group_attend")
    void applyTeam(@Query("id") String teamId,
                   @Query("mem_str") String mem_str,
                   @Query("gid") String gid,
                   @Query("eid") String eid,
                   @Query("leader_tel") String leader_tel,
                   @Query("leader_name") String leader_name,
                   @Query("leader_email") String leader_email,
                   @Query("org_name") String org_name,
                   @Query("token") String token,
                   @Query("version") String version,
                   Callback<CreateSuc> callback);

    @GET("/py/org?action=get_org")
    void getOrganizeName(@Query("gid") String gid,
                         @Query("word") String word,
                         Callback<OrganizeList> callback);

    @GET("/py/attend?action=get_first_level")
    void getFirstLevel(@Query("gid") String gid,
                       @Query("is_group") String is_group,
                       Callback<GameList> callback);

    @GET("/py/attend?action=get_second_level")
    void getSecondLevel(@Query("id") String gid,
                        @Query("is_group") String is_group,
                        Callback<GameList> callback);

    @GET("/py/attend?action=get_third_level")
    void getThirdLevel(@Query("id") String gid,
                       @Query("is_group") String is_group,
                       Callback<GameList> callback);

    @GET("/py/attend?action=checkUserInfo")
    void checkUserInfo(@Query("uid") String uid,
                       @Query("eid") String eid,
                       Callback<ErrorMsg> callback);

    @GET("/py/sn?action=justify_sn_valid")
    void checkValid(
            @Query("id") String id,
            @Query("uid") String uid,
            @Query("sn") String sn,
            Callback<ErrorMsg> callback
    );

    @GET("/py/adver?action=get_adver")
    void getAdver(Callback<Ads> callback);
}
