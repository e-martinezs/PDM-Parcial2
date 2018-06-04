package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pdmparcial2.api.GameNewsAPI;
import com.example.pdmparcial2.api.NewsDeserializer;
import com.example.pdmparcial2.api.TokenDeserializer;
import com.example.pdmparcial2.model.New;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsRepository {

    private String token;
    private NewDao newDao;
    private LiveData<List<New>> news;

    public NewsRepository(Application application){
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        login();
        newDao = db.newsDao();
        news = newDao.getAllNews();
    }

    public LiveData<List<New>> getAllNews(){
        return news;
    }

    public void insertNew(New mNew){
        new insertAsyncTask(newDao).execute(mNew);
    }

    public void insertAllNews(List<New> news){
        for (New n:news){
            new insertAsyncTask(newDao).execute(n);
        }
    }

    public void refresh(){
        if (token == null){
            login();
        }else{
            getNews();
        }
    }

    private static class insertAsyncTask extends AsyncTask<New, Void, Void>{

        private NewDao newDao;

        public insertAsyncTask(NewDao newDao){
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(New... news) {
            newDao.insertNew(news[0]);
            return null;
        }
    }

    private void login(){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").registerTypeAdapter(String.class, new TokenDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<String> login = gameNewsAPI.login("00116316", "00116316");
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                token = response.body();
                getNews();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getNews(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").registerTypeAdapter(New.class, new NewsDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<List<New>> getNews = gameNewsAPI.getAllNews();
        getNews.enqueue(new Callback<List<New>>() {
            @Override
            public void onResponse(Call<List<New>> call, Response<List<New>> response) {
                List<New> news = response.body();
                if (news != null) {
                    insertAllNews(news);
                }
            }

            @Override
            public void onFailure(Call<List<New>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
