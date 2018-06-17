package com.example.pdmparcial2.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.activities.MainActivity;
import com.example.pdmparcial2.adapter.NewsAdapter;
import com.example.pdmparcial2.api.APIRequest;
import com.example.pdmparcial2.database.viewmodels.NewViewModel;
import com.example.pdmparcial2.model.New;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsListFragment extends Fragment {

    private NewsAdapter newsAdapter;
    private APIRequest apiRequest;
    private NewViewModel newViewModel;
    private String selectedCategory = MainActivity.ALL;
    private String searchQuery = "";
    private List<New> newsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.newsListRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(), 4);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0) {
                    return 4;
                } else {
                    return 2;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        newsAdapter = new NewsAdapter(container.getContext(), apiRequest);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setHasFixedSize(true);

        newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        newViewModel.getNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                setNewsList(news, selectedCategory);
                newsList = news;
            }
        });
        newViewModel.getCategory().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String category) {
                selectedCategory = category;
            }
        });
        newViewModel.getSearch().observe(this, new Observer<String>() {
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
        newViewModel.setSearch("");
        setNewsList(newsList, selectedCategory);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                newViewModel.setSearch(query);
                setNewsList(newsList, selectedCategory);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                newViewModel.setSearch("");
                setNewsList(newsList, selectedCategory);
                searchView.clearFocus();
                item.collapseActionView();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setApiRequest(APIRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public void setNewsList(List<New> news, String filter) {
        List<New> filteredNews = new ArrayList<>();
        if (!filter.matches(MainActivity.ALL)) {
            if (filter.matches(MainActivity.FAVORITES)) {
                for (New n : news) {
                    if (n.isFavorite()) {
                        filteredNews.add(n);
                    }
                }
            } else {
                for (New n : news) {
                    if (n.getGame().matches(filter)) {
                        filteredNews.add(n);
                    }
                }
            }
        } else {
            filteredNews = news;
        }

        List<New> searchNews = new ArrayList<>();
        for (New n:filteredNews){
            if (n.getTitle().contains(searchQuery) || n.getDescription().contains(searchQuery)){
                searchNews.add(n);
            }
        }

        if (newsAdapter != null) {
            newsAdapter.setNews(searchNews);
        }
    }
}
