package com.example.pdmparcial2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.adapter.PlayerAdapter;
import com.example.pdmparcial2.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerListFragment extends Fragment {

    private List<Player> players;
    private PlayerAdapter playerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.newsListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        playerAdapter = new PlayerAdapter(container.getContext());
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    public void setPlayerList(List<Player> players, String filter) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.getGame().matches(filter)) {
                filteredPlayers.add(p);
            }
        }

        this.players = filteredPlayers;
        if (playerAdapter != null) {
            playerAdapter.setPlayers(filteredPlayers);
        }
    }
}
