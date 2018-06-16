package com.example.pdmparcial2.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.pdmparcial2.database.NewViewModel;
import com.example.pdmparcial2.model.User;
import com.example.pdmparcial2.utils.ActivityManager;
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

    private static String TOKEN = "TOKEN";
    private Context context;
    private View loadingLayout;
    private GameNewsAPI gameNewsAPI;
    private NewViewModel newViewModel;
    private SharedPreferences sharedPreferences;
    private User user = new User();
    private boolean loading = false;
    private boolean logged = false;
    private String message = "";

    public APIRequest(Context context, View loadingLayout, NewViewModel newViewModel) {
        this.context = context;
        this.loadingLayout = loadingLayout;
        this.newViewModel = newViewModel;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        checkLogged();
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

    public void checkLogged(){
        if (sharedPreferences.contains(TOKEN)){
            setLogged(true);
            user.setToken(sharedPreferences.getString(TOKEN, null));
        }else{
            setLogged(false);
            user.setToken("");
        }
    }

    public void login(String username, String password) {
        setLoading(true);
        /*Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new TokenDeserializer()).create();
        createAPIClient(gson);*/
        Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new TokenDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        System.out.println("LOGIN?");
        final Call<String> login = gameNewsAPI.login(username, password);
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("RESPONSE "+response.toString());
                if (response.code() == 200) {
                    user.setToken(response.body());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TOKEN, user.getToken());
                    editor.apply();

                    /*if (news.getValue() != null) {
                        for (New n : news.getValue()) {
                            if (n.isFavorite()) {
                                saveFavorite(n.getId());
                            } else {
                                deleteFavorite(n.getId());
                            }
                        }
                    }*/
                    setLogged(true);
                    ActivityManager.openMainActivity(context);

                } else if (response.code() == 401) {
                    setMessage("Wrong username or password");
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
               setLoading(false);
               setMessage("Could not connect to server");
               t.printStackTrace();
            }
        });

    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        if (loading){
            loadingLayout.setVisibility(View.VISIBLE);
        }else{
            loadingLayout.setVisibility(View.GONE);
        }
        this.loading = loading;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        ActivityManager.showToast(context, message);
        this.message = message;
    }
}
