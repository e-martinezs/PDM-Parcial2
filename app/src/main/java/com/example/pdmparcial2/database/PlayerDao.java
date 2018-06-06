package com.example.pdmparcial2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.pdmparcial2.model.Player;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlayer(Player player);

    @Query("DELETE FROM player_table")
    void deleteAllPlayers();

    @Query("SELECT * FROM player_table")
    LiveData<List<Player>> getAllPlayers();
}
