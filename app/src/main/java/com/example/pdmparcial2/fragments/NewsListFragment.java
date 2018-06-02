package com.example.pdmparcial2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.adapter.NewsAdapter;
import com.example.pdmparcial2.model.New;

import java.util.List;

public class NewsListFragment extends Fragment{

    private List<New> news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.newsListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        NewsAdapter newsAdapter = new NewsAdapter(container.getContext(), news);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    public void setNewsList(List<New> news){
        this.news = news;
    }
}
