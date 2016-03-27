package com.example.exerciseapp.api;

import com.example.exerciseapp.model.ErrorMsg;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by lyjq on 2016/3/24.
 */
public interface TeamService{
//    @POST("/py/group")
//    void createTeam(@Field("uid") String uid,
//                    @Field("group_name") String group_name,
//                    @Field("group_intro") String group_intro,
//                    @Field("group_tag_id") String group_tag_id,
//                    @Field("action") String create_group,
//                    Callback<ErrorMsg> callback);
    @POST("/py/group")
    void createTeam(@Body String body,Callback<ErrorMsg> callback);
//    @POST("/py/group")
//    void createTeam(@Field("uid") String uid,
//                    @Field("group_name") String group_name,
//                    @Field("group_intro") String group_intro,
//                    @Field("group_tag_id") String group_tag_id,
//                    @Field("action") String create_group,
//                    Callback<ErrorMsg> callback);

}
