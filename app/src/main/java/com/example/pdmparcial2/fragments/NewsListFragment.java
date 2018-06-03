package com.example.pdmparcial2.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
    private NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.newsListRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(), 4);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0){
                    return 4;
                }else{
                    return 2;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        newsAdapter = new NewsAdapter(container.getContext());
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    public void setNewsList(List<New> news){
        this.news = news;
        if (newsAdapter != null) {
            newsAdapter.setNews(news);
        }
    }
}
