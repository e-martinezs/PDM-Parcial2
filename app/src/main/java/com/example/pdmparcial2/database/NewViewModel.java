package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.model.User;

import java.util.List;

public class NewViewModel extends AndroidViewModel{

    private static NewsRepository repository;
    private static LiveData<List<New>> news;

    public NewViewModel(Application application){
        super(application);
        /*if (repository == null) {
            repository = new NewsRepository(application);
            news = repository.getNews();
            categories = repository.getCategories();
            players = repository.getPlayers();
            loading = repository.getLoading();
            logged = repository.getLogged();
            message = repository.getMessage();
        }*/

        repository = new NewsRepository(application);
        news = repository.getNews();
    }

    public LiveData<List<New>> getNews(){
        return news;
    }

    public void deleteNews(){
        repository.deleteNews();
    }

    public void insertNews(List<New> news, User user){
        repository.insertNews(news, user);
    }

    /*public LiveData<List<Category>> getCategories(){
        return categories;
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public void login(String username, String password){
        repository.login(username, password);
    }

    public void refresh(){
        repository.refresh();
    }

    public void saveFavorite(String newId){
        repository.saveFavorite(newId);
    }

    public void deleteFavorite(String newId){
        repository.deleteFavorite(newId);
    }*/
}