package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pdmparcial2.api.CategoryDeserializer;
import com.example.pdmparcial2.api.GameNewsAPI;
import com.example.pdmparcial2.api.NewsDeserializer;
import com.example.pdmparcial2.api.TokenDeserializer;
import com.example.pdmparcial2.model.Category;
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
    private CategoryDao categoryDao;
    private LiveData<List<New>> news;
    private LiveData<List<Category>> categories;

    public NewsRepository(Application application){
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        newDao = db.newsDao();
        categoryDao = db.categoryDao();
        login();
        news = newDao.getAllNews();
        categories = categoryDao.getAllCategories();
    }

    public LiveData<List<New>> getAllNews(){
        return news;
    }

    public LiveData<List<Category>> getAllCategories(){
        return categories;
    }

    public void insertAllNews(List<New> news){
        new insertNewsAsyncTask(newDao).execute(news);
    }

    public void insertAllCategories(List<Category> categories){
        new insertCategoriesAsyncTask(categoryDao).execute(categories);
    }

    public void refresh(){
        if (token == null){
            login();
        }else{
            getNews();
            getCategories();
        }
    }

    private static class insertNewsAsyncTask extends AsyncTask<List<New>, Void, Void>{

        private NewDao newDao;

        public insertNewsAsyncTask(NewDao newDao){
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(List<New>... news) {
            for (New n:news[0]){
                newDao.insertNew(n);
            }
            return null;
        }
    }

    private static class insertCategoriesAsyncTask extends AsyncTask<List<Category>, Void, Void>{

        private CategoryDao categoryDao;

        public insertCategoriesAsyncTask(CategoryDao categoryDao){
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(List<Category>... categories) {
            for (Category c:categories[0]){
                categoryDao.insertCategory(c);
            }
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
                getCategories();
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

    private void getCategories(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Gson gson = new GsonBuilder().registerTypeAdapter(Category.class, new CategoryDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<List<Category>> getCategories = gameNewsAPI.getAllCategories();
        getCategories.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                if (categories != null){
                    insertAllCategories(categories);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
