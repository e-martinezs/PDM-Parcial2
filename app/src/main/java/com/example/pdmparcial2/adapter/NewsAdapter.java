package com.example.pdmparcial2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.model.New;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private List<New> news;
    private Context context;

    public NewsAdapter(Context context, List<New> news){
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        New mNew = news.get(position);
        holder.newsTitleTextView.setText(mNew.getTitle());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        private TextView newsTitleTextView;

        public NewsViewHolder(View view){
            super(view);
            newsTitleTextView = view.findViewById(R.id.newsTitleTextView);
        }
    }
}
