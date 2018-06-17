package com.example.pdmparcial2.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pdmparcial2.database.NewsRoomDatabase;
import com.example.pdmparcial2.database.daos.NewDao;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.User;

import java.util.List;

public class NewsRepository {

    private NewDao newDao;
    private LiveData<List<New>> news;

    public NewsRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        newDao = db.newsDao();
        news = newDao.getNews();
    }

    public LiveData<List<New>> getNews() {
        return news;
    }

    public void insertNews(List<New> news, User user) {
        new insertNewsAsyncTask(newDao, user).execute(news);
    }

    public void deleteNews() {
        new deleteNewsAsyncTask(newDao).execute();
    }

    public void setFavorite(String newId, boolean favorite) {
        new setNewFavoriteAsyncTask(newDao, newId, favorite).execute();
    }

    //Elimina las noticias
    private static class deleteNewsAsyncTask extends AsyncTask<Void, Void, Void> {
        private NewDao newDao;

        public deleteNewsAsyncTask(NewDao newDao) {
            this.newDao = newDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newDao.deleteNews();
            return null;
        }
    }

    //Elimina las noticias e ingresa las nuevas
    private static class insertNewsAsyncTask extends AsyncTask<List<New>, Void, Void> {
        private NewDao newDao;
        private User user;

        public insertNewsAsyncTask(NewDao newDao, User user) {
            this.newDao = newDao;
            this.user = user;
        }

        @Override
        protected Void doInBackground(List<New>... news) {
            newDao.deleteNews();

            //Verifica si las noticias estan en favoritos
            for (New n : news[0]) {
                for (String id : user.getFavoriteNews()) {
                    if (n.getId().matches(id)) {
                        n.setFavorite(true);
                        break;
                    } else {
                        n.setFavorite(false);
                    }
                }
                newDao.insertNew(n);
            }
            return null;
        }
    }

    //Agrego o quita la noticia de favoritos
    private static class setNewFavoriteAsyncTask extends AsyncTask<Void, Void, Void> {
        private NewDao newDao;
        private String id;
        private boolean favorite;

        public setNewFavoriteAsyncTask(NewDao newDao, String id, boolean favorite) {
            this.newDao = newDao;
            this.id = id;
            this.favorite = favorite;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newDao.setFavorite(id, favorite);
            return null;
        }
    }
}
