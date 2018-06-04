package com.example.pdmparcial2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.model.New;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    private List<New> news;
    private Context context;

    public NewsAdapter(Context context){
        this.context = context;
        news = new ArrayList<>();
    }

    public void setNews(List<New> news){
        this.news = news;
        notifyDataSetChanged();
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
        holder.newsDescriptionTetView.setText(mNew.getDescription());

        try{
            if (!mNew.getCoverImage().isEmpty()) {
                Picasso.get().load(mNew.getCoverImage()).into(holder.newsImageView);
            }else{
                Picasso.get().load(R.mipmap.ic_launcher).into(holder.newsImageView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        private ImageView newsImageView;
        private TextView newsTitleTextView;
        private TextView newsDescriptionTetView;

        public NewsViewHolder(View view){
            super(view);
            newsImageView = view.findViewById(R.id.newsImageView);
            newsTitleTextView = view.findViewById(R.id.newsTitleTextView);
            newsDescriptionTetView = view.findViewById(R.id.newsDescriptionTextView);
        }
    }
}