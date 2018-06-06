package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.pdmparcial2.model.Category;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;

import java.util.List;

public class NewViewModel extends AndroidViewModel{

    private NewsRepository repository;
    private LiveData<List<New>> news;
    private LiveData<List<Category>> categories;
    private LiveData<List<Player>> players;
    private MutableLiveData<Boolean> loading;

    public NewViewModel(Application application){
        super(application);
        repository = new NewsRepository(application);
        this.news = repository.getAllNews();
        this.categories = repository.getAllCategories();
        this.players = repository.getAllPlayers();
        this.loading = repository.getLoading();
    }

    public LiveData<List<New>> getNews(){
        return news;
    }

    public LiveData<List<Category>> getCategories(){
        return categories;
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public MutableLiveData<Boolean> getLoading(){
        return loading;
    }

    public void refresh(){
        repository.refresh();
    }
}