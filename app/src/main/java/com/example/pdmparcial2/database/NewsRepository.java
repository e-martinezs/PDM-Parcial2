package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

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

    private static User user = new User();
    private NewDao newDao;
    private CategoryDao categoryDao;
    private PlayerDao playerDao;
    private LiveData<List<New>> news;
    private LiveData<List<Category>> categories;
    private LiveData<List<Player>> players;
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public NewsRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        newDao = db.newsDao();
        categoryDao = db.categoryDao();
        playerDao = db.playerDao();
        news = newDao.getNews();
        categories = categoryDao.getCategories();
        players = playerDao.getPlayers();
        loading.setValue(false);
        login();
    }

    public LiveData<List<New>> getNews() {
        return news;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void insertNews(List<New> news) {
        new insertNewsAsyncTask(newDao).execute(news);
    }

    public void insertCategories(List<Category> categories) {
        new insertCategoriesAsyncTask(categoryDao).execute(categories);
    }

    public void insertPlayers(List<Player> players) {
        new insertPlayersAsyncTask(playerDao).execute(players);
    }

    public void refresh() {
        if (user.getToken() == null) {
            login();
        } else {
            downloadNews();
            downloadCategories();
            downloadPlayers();
        }
    }

    private class insertNewsAsyncTask extends AsyncTask<List<New>, Void, Void> {

        private NewDao newDao;

        public insertNewsAsyncTask(NewDao newDao) {
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(List<New>... news) {
            for (New n : news[0]) {
                for (String id:user.getFavoriteNews()){
                    if (n.getId() == id){
                        n.setFavorite(true);
                    }else{
                        n.setFavorite(false);
                    }
                }
                newDao.insertNew(n);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            loading.setValue(false);
        }
    }

    private class insertCategoriesAsyncTask extends AsyncTask<List<Category>, Void, Void> {

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
    }

    private class insertPlayersAsyncTask extends AsyncTask<List<Player>, Void, Void> {

        private PlayerDao playerDao;

        public insertPlayersAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(List<Player>... players) {
            for (Player p : players[0]) {
                playerDao.insertPlayer(p);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            loading.setValue(false);
        }
    }

    private void login() {
        loading.setValue(true);
        Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new TokenDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        final Call<String> login = gameNewsAPI.login("00116316", "00116316");
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                user.setToken(response.body());
                getUserData();
                downloadNews();
                downloadCategories();
                downloadPlayers();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    private void getUserData() {
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
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    private void downloadNews() {
        loading.setValue(true);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + user.getToken()).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").registerTypeAdapter(New.class, new NewsDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<List<New>> getNews = gameNewsAPI.getNews();
        getNews.enqueue(new Callback<List<New>>() {
            @Override
            public void onResponse(Call<List<New>> call, Response<List<New>> response) {
                List<New> news = response.body();
                if (news != null) {
                    insertNews(news);
                }
            }

            @Override
            public void onFailure(Call<List<New>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    private void downloadCategories() {
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
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    private void downloadPlayers() {
        loading.setValue(true);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + user.getToken()).build();
                return chain.proceed(newRequest);
            }
        }).build();
        Gson gson = new GsonBuilder().registerTypeAdapter(Player.class, new PlayerDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        Call<List<Player>> getPlayers = gameNewsAPI.getPlayers();
        getPlayers.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                List<Player> players = response.body();
                if (players != null) {
                    insertPlayers(players);
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    public static void saveFavorite(final String newId){
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

        System.out.println("USER "+user.getId()+" NEW "+newId);
        Call<Void> saveFavorite = gameNewsAPI.saveFavorite(user.getId(), newId);
        saveFavorite.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("ADDED "+response.toString());
                user.getFavoriteNews().add(newId);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void deleteFavorite(final String newId){
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

        System.out.println("USER "+user.getId()+" NEW "+newId);
        Call<Void> deleteFavorite = gameNewsAPI.deleteFavorite(user.getId(), newId);
        deleteFavorite.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("DELETED "+response.toString());
                user.getFavoriteNews().remove(newId);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
