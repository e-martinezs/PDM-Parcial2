package com.example.pdmparcial2.database.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.pdmparcial2.activities.MainActivity;
import com.example.pdmparcial2.database.repositories.PlayerRepository;
import com.example.pdmparcial2.model.Player;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel {

    private PlayerRepository repository;
    private LiveData<List<Player>> players;
    private static MutableLiveData<String> category = new MutableLiveData<>();
    private static MutableLiveData<String> search = new MutableLiveData<>();

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        repository = new PlayerRepository(application);
        players = repository.getPlayers();
        setCategory(MainActivity.ALL);
        setSearch("");
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public void insertPlayers(List<Player> players) {
        repository.insertPlayers(players);
    }

    public void setCategory(String newCategory) {
        category.setValue(newCategory);
    }

    public MutableLiveData<String> getCategory() {
        return category;
    }

    public void setSearch(String newSearch) {
        search.setValue(newSearch);
    }

    public MutableLiveData<String> getSearch() {
        return search;
    }
}
