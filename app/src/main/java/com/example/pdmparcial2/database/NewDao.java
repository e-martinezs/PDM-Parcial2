package com.example.pdmparcial2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.pdmparcial2.model.New;

import java.util.List;

@Dao
public interface NewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNew(New mNew);

    @Query("DELETE FROM news_table")
    void deleteAllNews();

    @Query("SELECT * FROM news_table")
    LiveData<List<New>> getAllNews();
}
