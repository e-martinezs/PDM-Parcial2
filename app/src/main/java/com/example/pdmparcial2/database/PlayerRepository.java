package com.example.pdmparcial2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.pdmparcial2.model.Player;

import java.util.List;

public class PlayerRepository {

    private PlayerDao playerDao;
    private LiveData<List<Player>> players;

    public PlayerRepository(Application application){
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        playerDao = db.playerDao();
        players = playerDao.getPlayers();
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public void insertPlayers(List<Player> players) {
        new insertPlayersAsyncTask(playerDao).execute(players);
    }

    private class insertPlayersAsyncTask extends AsyncTask<List<Player>, Void, Void> {
        private PlayerDao playerDao;

        public insertPlayersAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(List<Player>... players) {
            for (Player p : players[0]) {
                playerDao.insertPlayer(p);
            }
            return null;
        }
    }
}
