package com.example.pdmparcial2.api;

import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GameNewsAPI {
    String BASE_URL = "http://gamenewsuca.herokuapp.com";

    @GET("/news")
    Call<List<New>> getNews();

    @GET("/news/type/list")
    Call<List<Category>> getCategories();

    @GET("/players")
    Call<List<Player>> getPlayers();

    @GET("/users/detail")
    Call<User> getUserData();

    @FormUrlEncoded
    @POST("/login")
    Call<String> login(@Field("user") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/users/{userId}/fav")
    Call<Void> saveFavorite(@Path("userId") String userId, @Field("new") String newId);

    @FormUrlEncoded
    @HTTP(method="DELETE", path="/users/{userId}/fav", hasBody = true)
    Call<Void> deleteFavorite(@Path("userId") String userId, @Field("new") String newId);

    @FormUrlEncoded
    @PUT("/users/{userId}")
    Call<Void> changePassword(@Path("userId") String userId, @Field("password") String newPassword);

}
