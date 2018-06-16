package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.pdmparcial2.api.CategoryDeserializer;
import com.example.pdmparcial2.api.GameNewsAPI;
import com.example.pdmparcial2.api.NewsDeserializer;
import com.example.pdmparcial2.api.PlayerDeserializer;
import com.example.pdmparcial2.api.TokenDeserializer;
import com.example.pdmparcial2.api.UserDeserializer;
import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.model.User;
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

    private NewDao newDao;
    private LiveData<List<New>> news;

    public NewsRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        newDao = db.newsDao();
        news = newDao.getNews();
    }

    public LiveData<List<New>> getNews() {
        return news;
    }

    /*public LiveData<List<Category>> getCategories() {
        return categories;
    }*/

    public void insertNews(List<New> news, User user) {
        new insertNewsAsyncTask(newDao, user).execute(news);
    }

    /*public void insertCategories(List<Category> categories) {
        new insertCategoriesAsyncTask(categoryDao).execute(categories);
    }*/

    public void deleteNews(){
        new deleteNewsAsyncTask(newDao).execute();
    }

    /*public void refresh() {
        //getUserData();
    }*/

    private class deleteNewsAsyncTask extends AsyncTask<Void, Void, Void>{
        private NewDao newDao;

        public deleteNewsAsyncTask(NewDao newDao){
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newDao.deleteNews();
            return null;
        }
    }

    private class insertNewsAsyncTask extends AsyncTask<List<New>, Void, Void> {
        private NewDao newDao;
        private User user;

        public insertNewsAsyncTask(NewDao newDao, User user) {
            this.newDao = newDao;
            this.user = user;
        }

        @Override
        protected Void doInBackground(List<New>... news) {
            for (New n : news[0]) {
                /*for (String id : user.getFavoriteNews()) {
                    if (n.getId().matches(id)) {
                        n.setFavorite(true);
                        break;
                    } else {
                        n.setFavorite(false);
                    }
                }*/
                newDao.insertNew(n);
            }
            return null;
        }
    }

    /*private class insertCategoriesAsyncTask extends AsyncTask<List<Category>, Void, Void> {
        private CategoryDao categoryDao;

        public insertCategoriesAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(List<Category>... categories) {
            categoryDao.deleteCategories();
            for (Category c : categories[0]) {
                categoryDao.insertCategory(c);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            loading.setValue(false);
        }
    }*/

    private class setNewFavoriteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NewDao newDao;
        private String id;
        private boolean favorite;

        public setNewFavoriteAsyncTask(NewDao newDao, String id, boolean favorite) {
            this.newDao = newDao;
            this.id = id;
            this.favorite = favorite;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newDao.setFavorite(id, favorite);
            return null;
        }
    }

    /*private void getUserData() {
        loading.setValue(true);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + user.getToken()).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<User> getUserData = gameNewsAPI.getUserData();
        getUserData.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("USER DATA "+response.toString());
                if (response.code() == 200) {
                    user.setId(response.body().getId());
                    user.setFavoriteNews(response.body().getFavoriteNews());
                    downloadNews();
                }else if (response.code() == 401){
                    message.setValue("Session expired");
                    //logout();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }*/

    /*private void downloadCategories() {
        loading.setValue(true);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + user.getToken()).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Gson gson = new GsonBuilder().registerTypeAdapter(Category.class, new CategoryDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<List<Category>> getCategories = gameNewsAPI.getCategories();
        getCategories.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                if (categories != null) {
                    insertCategories(categories);
                }
                downloadPlayers();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }*/

    /*public void saveFavorite(final String newId) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + user.getToken()).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(new Gson()));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<Void> saveFavorite = gameNewsAPI.saveFavorite(user.getId(), newId);
        saveFavorite.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                user.getFavoriteNews().add(newId);
                new setNewFavoriteAsyncTask(newDao, user.getId(), true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }*/

    /*public void deleteFavorite(final String newId) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + user.getToken()).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(new Gson()));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<Void> deleteFavorite = gameNewsAPI.deleteFavorite(user.getId(), newId);
        deleteFavorite.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                user.getFavoriteNews().remove(newId);
                new setNewFavoriteAsyncTask(newDao, user.getId(), false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }*/
}
