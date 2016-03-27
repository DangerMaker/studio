package com.example.exerciseapp.net.rest;


import com.example.exerciseapp.BuildConfig;
import com.example.exerciseapp.HttpConfig;
import com.example.exerciseapp.api.TeamService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 * Created by vclub on 15/6/29.
 */
public class RestAdapterUtils {

    public static <T> T getRestAPI(String endpoint, Class<T> service) {

        RestAdapter.LogLevel level;

        if (BuildConfig.DEBUG){
            level = RestAdapter.LogLevel.FULL;
        }else {
            level = RestAdapter.LogLevel.NONE;
        }

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(level)
                .setEndpoint(endpoint)
                .setClient(getHttpClient())
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                    }
                })
                .build();

        return restAdapter.create(service);
    }

    private static Client getHttpClient(){
        OkHttpClient httpClient = new OkHttpClient();
        return new OkClient(httpClient);
    }

    public static TeamService getTeamAPI(){
        return RestAdapterUtils.getRestAPI(HttpConfig.BASE_CMS_URL_NEW, TeamService.class);
    }

}
