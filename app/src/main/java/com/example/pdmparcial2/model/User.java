package com.example.pdmparcial2.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String token;
    private String id;
    private String username;
    private String password;
    private List<String> favoriteNews = new ArrayList<>();

    public User() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFavoriteNews() {
        return favoriteNews;
    }

    public void setFavoriteNews(List<String> favoriteNews) {
        this.favoriteNews = favoriteNews;
    }
}
