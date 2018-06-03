package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pdmparcial2.model.New;

import java.util.List;

public class NewsRepository {
    private NewDao newDao;
    private LiveData<List<New>> news;

    public NewsRepository(Application application){
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        newDao = db.newsDao();
        news = newDao.getAllNews();
    }

    public LiveData<List<New>> getAllNews(){
        return news;
    }

    public void insertNew(New mNew){
        new insertAsyncTask(newDao).execute(mNew);
    }

    private static class insertAsyncTask extends AsyncTask<New, Void, Void>{

        private NewDao newDao;

        public insertAsyncTask(NewDao newDao){
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(New... news) {
            newDao.insertNew(news[0]);
            return null;
        }
    }
}
