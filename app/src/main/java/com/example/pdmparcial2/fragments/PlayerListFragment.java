package com.example.pdmparcial2.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.adapter.PlayerAdapter;
import com.example.pdmparcial2.database.viewmodels.PlayerViewModel;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerListFragment extends Fragment {

    private List<Player> players;
    private PlayerAdapter playerAdapter;
    private PlayerViewModel playerViewModel;
    private String selectedCategory = "all";
    private String searchQuery = "";
    private List<Player> playerList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.newsListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        playerAdapter = new PlayerAdapter(container.getContext());
        recyclerView.setAdapter(playerAdapter);
        recyclerView.setHasFixedSize(true);

        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
        playerViewModel.getPlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(@Nullable List<Player> players) {
                setPlayerList(players, selectedCategory);
                playerList = players;
            }
        });
        playerViewModel.getCategory().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String category) {
                selectedCategory = category;
            }
        });
        playerViewModel.getSearch().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String query) {
                searchQuery = query;
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final MenuItem item = menu.findItem(R.id.searchView);
        final SearchView searchView = (SearchView) item.getActionView();
        playerViewModel.setSearch("");
        setPlayerList(playerList, selectedCategory);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                playerViewModel.setSearch(query);
                setPlayerList(playerList, selectedCategory);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                playerViewModel.setSearch("");
                setPlayerList(playerList, selectedCategory);
                searchView.clearFocus();
                item.collapseActionView();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setPlayerList(List<Player> players, String filter) {
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.getGame().matches(filter)) {
                filteredPlayers.add(p);
            }
        }

        List<Player> searchPlayers = new ArrayList<>();
        for (Player p:filteredPlayers){
            if (p.getName().contains(searchQuery)){
                searchPlayers.add(p);
            }
        }

        if (playerAdapter != null) {
            playerAdapter.setPlayers(searchPlayers);
        }
    }
}
