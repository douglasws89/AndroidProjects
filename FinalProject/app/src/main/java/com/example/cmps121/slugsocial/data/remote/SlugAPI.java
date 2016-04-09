package com.example.cmps121.slugsocial.data.remote;

import com.example.cmps121.slugsocial.data.model.AttendingInfo;
import com.example.cmps121.slugsocial.data.model.EventInfo;
import com.example.cmps121.slugsocial.data.model.MessageInfo;
import com.example.cmps121.slugsocial.data.model.UserInfo;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface SlugAPI {
    //https://douglasws89.pythonanywhere.com/backEndAndroid/default/index
//    String BASE_URL = "https://douglasws89.pythonanywhere.com/backEndAndroid";
    String BASE_URL = "http://doug-ucsc-cloud-backend-1243.appspot.com";


    @GET("/backEndAndroid/default/query_user")
    Call<UserInfo> getInfo(@Query("username") String username,
                           @Query("password") String password);

    @GET("/backEndAndroid/default/get_user_info")
    Call<UserInfo> getUserInfo(@Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("/backEndAndroid/default/register_user")
    Call<UserInfo> setInfo(@Field("user_id") String user_id,
                           @Field("profile_picture") String profile_picture,
                           @Field("name") String name,
                           @Field("age") String age,
                           @Field("bio") String bio,
                           @Field("username") String username,
                           @Field("password") String password);


    @GET("/backEndAndroid/default/query_events")
    Call<EventInfo> getEvent(@Query("event") String event);

    @FormUrlEncoded
    @POST("/backEndAndroid/default/create_event")
    Call<EventInfo> setEvent(@Field("event_id") String event_id,
                             @Field("event_image") String event_image,
                             @Field("event") String event,
                             @Field("user_id") String user_id,
                             @Field("title") String title,
                             @Field("description") String description,
                             @Field("time") String time,
                             @Field("place") String place);


    @GET("/backEndAndroid/default/query_attending")
    Call<AttendingInfo> getAttending(@Query("event_id") String event_id);

    @FormUrlEncoded
    @POST("/backEndAndroid/default/insert_attending")
    Call<AttendingInfo> setAttending(@Field("event_id") String event_id,
                                     @Field("user_id") String user_id,
                                     @Field("user_name") String user_name,
                                     @Field("user_image") String user_image);


    @GET("/backEndAndroid/default/get_messages")
    Call<MessageInfo> getChat(@Query("event_id") String event_id);

    @FormUrlEncoded
    @POST("/backEndAndroid/default/post_message")
    Call<MessageInfo> setChat(@Field("user_id") String user_id,
                              @Field("user_name") String user_name,
                              @Field("message") String message,
                              @Field("message_id") String message_id,
                              @Field("event_id") String event_id);



    class Factory{
        public static SlugAPI service;
        public static SlugAPI getInstance(){
            if(service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(SlugAPI.class);
                return service;
            } else{
                return service;
            }
        }
    }
}
