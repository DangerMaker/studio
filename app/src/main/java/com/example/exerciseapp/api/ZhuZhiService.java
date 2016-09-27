package com.example.exerciseapp.api;

import com.example.exerciseapp.model.AlbumId;
import com.example.exerciseapp.model.AlbumInfo;
import com.example.exerciseapp.model.ApplyList;
import com.example.exerciseapp.model.ErrorMsg;
import com.example.exerciseapp.model.Judge;
import com.example.exerciseapp.model.OrgPubInfo;
import com.example.exerciseapp.model.PhotoModel;
import com.example.exerciseapp.model.Role;
import com.example.exerciseapp.model.UpTeamAvatar;
import com.example.exerciseapp.model.UploadInteraction;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;

/**
 * Created by Administrator on 2016/9/1.
 */
public interface ZhuZhiService {
    /**
     * @param uid 用户uid
     */
    @GET("/py/orgpri")
    void apply1(@Query("uid") String uid,
                @Query("action") String apply_club_org,
                @Query("version") String version,
                @Query("contacts") String contacts,
                @Query("contact_phone") String contact_phone,
                @Query("address") String address,
                @Query("name") String name,
                @Query("type") int type,
                @Query("token") String token,
                @Query("qq") String qq,
                @Query("email") String email,
                @Query("athletics") String athletics,
                Callback<ErrorMsg> callback);

    @GET("/py/orgpub")
    void getOrgInfo(@Query("uid") String uid,
                    @Query("action") String get_brief,
                    @Query("version") String version,
                    @Query("id") String id,
                    Callback<OrgPubInfo> callback);

    @GET("/py/orgpri")
    void changeField(@Query("uid") String uid,
                     @Query("action") String set_field,
                     @Query("version") String version,
                     @Query("id") String id,
                     @Query("token") String token,
                     @Query("field") String field,
                     @Query("new_value") String value,
                     Callback<ErrorMsg> callback);

    @GET("/py/orgpri")
    void getApplyList(@Query("uid") String uid,
                      @Query("action") String get_apply_list,
                      @Query("version") String version,
                      @Query("id") String id,
                      @Query("token") String token,
                      @Query("page") String page,
                      Callback<ApplyList> callback);

    @GET("/py/orgpub")
    void getAlbumList(
            @Query("uid") String uid,
            @Query("action") String get_album_pic_list,
            @Query("version") String version,
            @Query("org_id") String org_id,
            @Query("album_id") String album_id,
            @Query("current_date") String current_date,
            @Query("last_id") String last_id,
            Callback<PhotoModel> callback
    );

    @GET("/py/orgpub")
    void isManager(
            @Query("uid") String uid,
            @Query("action") String judge_is_admin,
            @Query("version") String version,
            @Query("id") String id,
            Callback<Judge> callback
    );

    @GET("/py/orgpub")
    void getUserRole(
            @Query("uid") String uid,
            @Query("action") String get_user_role,
            @Query("version") String version,
            @Query("id") String id,
            @Query("album_id") String album_id,
            @Query("organ_id") String organ_id,
            Callback<Role> callback
    );

//    @Multipart
    @POST("/index.php/Api/Users/uploadMultiAlbumPic")
    void updateFile(
//            @Part("uid") String uid,
//            @Part("version") String version,
//            @Part("org_id") String org_id,
//            @Part("album_id") String album_id,
//            @Part("token") String token,
            @Body MultipartTypedOutput multipartTypedOutput,
            Callback<ErrorMsg> callback);

//    @Multipart
//    @POST("/index.php/Api/Users/uploadMultiAlbumPic")
//    void updateFile(
//            @Part("uid") String uid,
//            @Part("version") String version,
//            @Part("org_id") String org_id,
//            @Part("album_id") String album_id,
//            @Part("token") String token,
//            @Part("pic1") TypedFile avatar,
//            Callback<ErrorMsg> callback
//    );

    @GET("/py/orgpri")
    void createAlbum(
            @Query("uid") String uid,
            @Query("action") String create_album,
            @Query("version") String version,
            @Query("token") String token,
            @Query("org_id") String org_id,
            @Query("album_name") String album_name,
            Callback<AlbumId> callback
    );

    @GET("/py/orgpri")
    void updateAlbum(
            @Query("uid") String uid,
            @Query("action") String release_album,
            @Query("version") String version,
            @Query("token") String token,
            @Query("pic_str") String pic_str,
            @Query("album_id") String album_id,
            @Query("org_id") String org_id,
            Callback<ErrorMsg> callback
    );

    @GET("/py/orgpri")
    void passApply(
            @Query("uid") String uid,
            @Query("action") String pass_apply,
            @Query("version") String version,
            @Query("token") String token,
            @Query("apply_id") String apply_id,
            @Query("oper") String oper,
            Callback<ErrorMsg> callback
    );

    @GET("/py/orgpub")
    void getAlbumInfo(
            @Query("uid") String uid,
            @Query("action") String get_album_info,
            @Query("version") String version,
            @Query("album_id") String album_id,
            Callback<AlbumInfo> callback
    );

    @Multipart
    @POST("/index.php/Api/Users/uploadAlbumPicIos")
    void uploadPicture(@Part("albumPic") TypedFile avatar, Callback<UploadInteraction> callback);


}
