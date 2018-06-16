package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.pdmparcial2.model.Player;

import java.util.List;

public class PlayerViewModel extends AndroidViewModel{

    private PlayerRepository repository;
    private LiveData<List<Player>> players;

    public PlayerViewModel(@NonNull Application application) {
        super(application);
        repository = new PlayerRepository(application);
        players = repository.getPlayers();
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public void insertPlayers(List<Player> players){
        repository.insertPlayers(players);
    }
}
