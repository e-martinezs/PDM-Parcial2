package com.example.pdmparcial2.database.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.pdmparcial2.database.repositories.NewsRepository;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.User;

import java.util.List;

public class NewViewModel extends AndroidViewModel{

    private NewsRepository repository;
    private LiveData<List<New>> news;
    private static MutableLiveData<String> category = new MutableLiveData<>();

    public NewViewModel(@NonNull Application application){
        super(application);
        repository = new NewsRepository(application);
        news = repository.getNews();
        setCategory("all");
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

    public void saveFavorite(String newId){
        repository.setFavorite(newId, true);
    }

    public void deleteFavorite(String newId){
        repository.setFavorite(newId, false);
    }

    public void setCategory(String category){
        this.category.setValue(category);
    }

    public MutableLiveData<String> getCategory() {
        return category;
    }
}