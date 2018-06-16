package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.model.User;

import java.util.List;

public class NewViewModel extends AndroidViewModel{

    private NewsRepository repository;
    private LiveData<List<New>> news;

    public NewViewModel(@NonNull Application application){
        super(application);
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