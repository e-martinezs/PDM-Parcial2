package com.example.pdmparcial2.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.api.deserializers.CategoryDeserializer;
import com.example.pdmparcial2.api.deserializers.NewsDeserializer;
import com.example.pdmparcial2.api.deserializers.PlayerDeserializer;
import com.example.pdmparcial2.api.deserializers.TokenDeserializer;
import com.example.pdmparcial2.api.deserializers.UserDeserializer;
import com.example.pdmparcial2.database.viewmodels.CategoryViewModel;
import com.example.pdmparcial2.database.viewmodels.NewViewModel;
import com.example.pdmparcial2.database.viewmodels.PlayerViewModel;
import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.model.User;
import com.example.pdmparcial2.utils.ActivityManager;
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

public class APIRequest {

    private static String TOKEN = "TOKEN";
    private static String USER = "USER";
    private static User user = new User();
    private Context context;
    private View loadingLayout;
    private GameNewsAPI gameNewsAPI;
    private NewViewModel newViewModel;
    private PlayerViewModel playerViewModel;
    private CategoryViewModel categoryViewModel;
    private SharedPreferences sharedPreferences;
    private boolean loading = false;
    private boolean logged = false;
    private String message = "";

    public APIRequest(Context context, View loadingLayout, NewViewModel newViewModel, PlayerViewModel playerViewModel, CategoryViewModel categoryViewModel) {
        this.context = context;
        this.loadingLayout = loadingLayout;
        this.newViewModel = newViewModel;
        this.playerViewModel = playerViewModel;
        this.categoryViewModel = categoryViewModel;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        checkLogged();
    }

    //Crea el cliente para acceder al API, lo pone el token de autorizacion y un gson para deserializar el resultado
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

    //Verifica si ya hay un token almacenado
    public void checkLogged() {
        if (sharedPreferences.contains(TOKEN)) {
            setLogged(true);
            user.setToken(sharedPreferences.getString(TOKEN, null));
        } else {
            setLogged(false);
            user.setToken("");
        }
    }

    //Realiza el login
    public void login(final String username, String password) {
        setLoading(true);
        Gson gson = new GsonBuilder().registerTypeAdapter(String.class, new TokenDeserializer()).create();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(GameNewsAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        GameNewsAPI gameNewsAPI = retrofit.create(GameNewsAPI.class);

        final Call<String> login = gameNewsAPI.login(username, password);
        login.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                //Verifica si la respuesta fue OK
                if (response.code() == 200) {

                    //Almacena el token obtenido del API
                    user.setToken(response.body());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TOKEN, user.getToken());
                    editor.putString(USER, username);
                    editor.apply();

                    setLogged(true);
                    ActivityManager.openMainActivity(context);

                } else if (response.code() == 401) {
                    setMessage(context.getString(R.string.error_wrong_credentials));
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                connectionError();
                t.printStackTrace();
            }
        });
    }

    //Elimina el token almacenado y elimina las noticias
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN);
        editor.apply();
        newViewModel.deleteNews();
    }

    //Obtiene los datos para refrescar el app
    public void refresh() {
        getUserData();
    }

    //Descarga las noticias, jugadores y categorias del API
    public void downloadAll() {
        downloadNews();
        downloadPlayers();
        downloadCategories();
    }

    //Descarga las noticias
    public void downloadNews() {
        setLoading(true);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").registerTypeAdapter(New.class, new NewsDeserializer()).create();
        createAPIClient(gson);

        Call<List<New>> getNews = gameNewsAPI.getNews();
        getNews.enqueue(new Callback<List<New>>() {
            @Override
            public void onResponse(Call<List<New>> call, Response<List<New>> response) {
                List<New> news = response.body();
                if (news != null) {
                    newViewModel.insertNews(news, user);
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<List<New>> call, Throwable t) {
                connectionError();
                t.printStackTrace();
            }
        });
    }

    //Descarga los jugadores
    private void downloadPlayers() {
        setLoading(true);
        Gson gson = new GsonBuilder().registerTypeAdapter(Player.class, new PlayerDeserializer()).create();
        createAPIClient(gson);

        Call<List<Player>> getPlayers = gameNewsAPI.getPlayers();
        getPlayers.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                List<Player> players = response.body();
                if (players != null) {
                    playerViewModel.insertPlayers(players);
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                connectionError();
                t.printStackTrace();
            }
        });
    }

    //Descarga las categorias
    private void downloadCategories() {
        setLoading(true);
        Gson gson = new GsonBuilder().registerTypeAdapter(Category.class, new CategoryDeserializer()).create();
        createAPIClient(gson);

        Call<List<Category>> getCategories = gameNewsAPI.getCategories();
        getCategories.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                if (categories != null) {
                    categoryViewModel.insertCategories(categories);
                }
                downloadPlayers();
                setLoading(false);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                connectionError();
                t.printStackTrace();
            }
        });
    }

    //Obtiene los datos del usuario logeado
    private void getUserData() {
        setLoading(true);
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer()).create();
        createAPIClient(gson);

        Call<User> getUserData = gameNewsAPI.getUserData();
        getUserData.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    user.setId(response.body().getId());
                    user.setUsername(response.body().getUsername());
                    user.setPassword(response.body().getPassword());
                    user.setFavoriteNews(response.body().getFavoriteNews());
                    syncFavorites();
                    downloadAll();
                } else if (response.code() == 401) {
                    sessionExpired();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                connectionError();
                t.printStackTrace();
            }
        });
    }

    //Guarda la notcia indicada como favorito
    public void saveFavorite(final String newId) {
        createAPIClient(new Gson());
        Call<Void> saveFavorite = gameNewsAPI.saveFavorite(user.getId(), newId);
        saveFavorite.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                user.getFavoriteNews().add(newId);
                newViewModel.saveFavorite(newId);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                user.getFavoriteNews().add(newId);
                newViewModel.saveFavorite(newId);
            }
        });
    }

    //Elimina la noticia indicada de favoritos
    public void deleteFavorite(final String newId) {
        createAPIClient(new Gson());
        Call<Void> deleteFavorite = gameNewsAPI.deleteFavorite(user.getId(), newId);
        deleteFavorite.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                user.getFavoriteNews().remove(newId);
                newViewModel.deleteFavorite(newId);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                user.getFavoriteNews().remove(newId);
                newViewModel.deleteFavorite(newId);
            }
        });
    }

    //Actualiza los favoritos online con los que se cambiaron en modo offline
    private void syncFavorites() {
        List<New> news = newViewModel.getNews().getValue();
        if (news != null) {
            for (New n : news) {
                if (n.isFavorite()) {
                    saveFavorite(n.getId());
                    user.getFavoriteNews().add(n.getId());
                } else {
                    deleteFavorite(n.getId());
                    user.getFavoriteNews().remove(n.getId());
                }
            }
        }
    }

    //Cambia la contraseña del usuario
    public void changePassword(String oldPassword, String newPassword) {
        if (!oldPassword.matches(user.getPassword())) {
            ActivityManager.showToast(context, context.getString(R.string.error_wrong_password));
            return;
        }
        setLoading(true);
        createAPIClient(new Gson());
        Call<Void> changePassword = gameNewsAPI.changePassword(user.getId(), newPassword);
        changePassword.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    ActivityManager.showToast(context, context.getString(R.string.message_password_changed));
                } else {
                    ActivityManager.showToast(context, context.getString(R.string.error_default));
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                connectionError();
            }
        });
    }

    public boolean isLoading() {
        return loading;
    }

    //Si esta cargando muestra el ProgressBar
    public void setLoading(boolean loading) {
        if (loadingLayout != null) {
            if (loading) {
                loadingLayout.setVisibility(View.VISIBLE);
            } else {
                loadingLayout.setVisibility(View.GONE);
            }
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

    //Muestra el mensaje en un Toast
    public void setMessage(String message) {
        ActivityManager.showToast(context, message);
        this.message = message;
    }

    //Muestra el mensaje de error de coneccion
    public void connectionError() {
        setMessage(context.getString(R.string.error_connection));
        setLoading(false);
    }

    //Si el token esta vencido regresa al login
    public void sessionExpired() {
        setMessage(context.getString(R.string.error_session_expired));
        logout();
        ActivityManager.openLoginActivity(context);
        ActivityManager.closeActivity(context);
    }

    public String getUsername() {
        return sharedPreferences.getString(USER, null);
    }
}
