package com.example.pdmparcial2.api;

import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GameNewsAPI {
    String BASE_URL = "http://gamenewsuca.herokuapp.com";

    @GET("/news")
    Call<List<New>> getAllNews();

    @GET("/news/type/list")
    Call<List<Category>> getAllCategories();

    @FormUrlEncoded
    @POST("/login")
    Call<String> login(@Field("user") String username, @Field("password") String password);
}
