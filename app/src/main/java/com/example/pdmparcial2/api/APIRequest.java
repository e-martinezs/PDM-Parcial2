package com.example.pdmparcial2.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.pdmparcial2.database.NewViewModel;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRequest {

    public static String TOKEN = "TOKEN";
    private GameNewsAPI gameNewsAPI;
    private NewViewModel newViewModel;
    private SharedPreferences sharedPreferences;
    private User user = new User();
    private boolean loading = false;
    private boolean logged = false;
    private String message = "";

    public APIRequest(Context context, NewViewModel newViewModel) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (sharedPreferences.contains(TOKEN)){
            logged = true;
            user.setToken(sharedPreferences.getString(TOKEN, null));
        }else{
            logged = false;
            user.setToken("");
        }

        this.newViewModel = newViewModel;
    }

    private void createAPIClient(Gson gson) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + user.getToken()).build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        gameNewsAPI = retrofit.create(GameNewsAPI.class);
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isLogged(){
        return logged;
    }

    public String getMessage() {
        return message;
    }

    public void login(String username, String password) {
        loading = true;
        Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new TokenDeserializer()).create();
        createAPIClient(gson);

        final Call<String> login = gameNewsAPI.login(username, password);
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    user.setToken(response.body());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TOKEN, user.getToken());
                    editor.apply();

                    /*f (news.getValue() != null) {
                        for (New n : news.getValue()) {
                            if (n.isFavorite()) {
                                saveFavorite(n.getId());
                            } else {
                                deleteFavorite(n.getId());
                            }
                        }
                    }*/
                    logged = true;

                } else if (response.code() == 401) {
                    message = "Wrong username or password";
                }
                loading = false;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loading = false;
            }
        });
    }
}
