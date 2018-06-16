package com.example.pdmparcial2.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdmparcial2.R;
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
    private String selectedCategory = "all";

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

        NewViewModel newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        newViewModel.getNews().observe(this, new Observer<List<New>>() {
            @Override
            public void onChanged(@Nullable List<New> news) {
                setNewsList(news, selectedCategory);
            }
        });
        newViewModel.getCategory().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String category) {
                selectedCategory = category;
            }
        });

        return view;
    }

    public void setApiRequest(APIRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public void setNewsList(List<New> news, String filter) {
        List<New> filteredNews = new ArrayList<>();
        if (!filter.matches("all")) {
            if (filter.matches("favorites")) {
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

        Collections.sort(filteredNews);
        Collections.reverse(filteredNews);
        if (newsAdapter != null) {
            newsAdapter.setNews(filteredNews);
        }
    }
}
