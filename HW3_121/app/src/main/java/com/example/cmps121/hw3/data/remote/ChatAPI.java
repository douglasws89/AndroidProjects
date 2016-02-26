package com.example.cmps121.hw3.data.remote;

import com.example.cmps121.hw3.data.model.Chat;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by douglasws89 on 2/13/16.
 */
public interface ChatAPI {

    String BASE_URL = "https://luca-teaching.appspot.com";

//    @GET("/localmessages/default/get_messages?lat=9.99999&lng=10.0001&user_id=39")
//    Call <Chat> getChat();

    @GET("/localmessages/default/get_messages")
    Call<Chat> getChat(@Query("lat") float lat,
                       @Query("lng") float lng,
                       @Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("/localmessages/default/post_message")
    Call<Chat> setChat(@Field("lat") float lat,
                       @Field("lng") float lng,
                       @Field("user_id") String user_id,
                       @Field("nickname") String nickname,
                       @Field("message") String message,
                       @Field("message_id") String message_id);

    class Factory{
        public static ChatAPI service;
        public static ChatAPI getInstance(){
            if(service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(ChatAPI.class);
                return service;
            } else{
                return service;
            }
        }
    }
}