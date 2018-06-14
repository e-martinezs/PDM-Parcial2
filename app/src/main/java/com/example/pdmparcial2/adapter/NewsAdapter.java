package com.example.pdmparcial2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.activities.NewDetail;
import com.example.pdmparcial2.database.NewViewModel;
import com.example.pdmparcial2.model.New;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<New> news;
    private Context context;
    private NewViewModel newViewModel;

    public NewsAdapter(Context context, NewViewModel newViewModel) {
        this.context = context;
        news = new ArrayList<>();
        this.newViewModel = newViewModel;
    }

    public void setNews(List<New> news) {
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
        final New mNew = news.get(position);
        holder.newsTitleTextView.setText(mNew.getTitle());
        holder.newsDescriptionTetView.setText(mNew.getDescription());

        try {
            if (!mNew.getCoverImage().isEmpty()) {
                Picasso.get().load(mNew.getCoverImage()).error(R.drawable.ic_image).into(holder.newsImageView);
            } else {
                Picasso.get().load(R.drawable.ic_image).error(R.drawable.ic_image).into(holder.newsImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final CheckBox buttonFavorite = holder.buttonFavorite;

        if (mNew.isFavorite()) {
            buttonFavorite.setChecked(true);
        } else {
            buttonFavorite.setChecked(false);
        }

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buttonFavorite.isChecked()) {
                    mNew.setFavorite(false);
                    newViewModel.deleteFavorite(mNew.getId());
                } else {
                    mNew.setFavorite(true);
                    newViewModel.saveFavorite(mNew.getId());
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NEW", mNew);
                intent.putExtras(bundle);

                ((Activity)context).startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView newsImageView;
        private TextView newsTitleTextView;
        private TextView newsDescriptionTetView;
        private CheckBox buttonFavorite;

        public NewsViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardview_new);
            newsImageView = view.findViewById(R.id.newsImageView);
            newsTitleTextView = view.findViewById(R.id.newsTitleTextView);
            newsDescriptionTetView = view.findViewById(R.id.newsDescriptionTextView);
            buttonFavorite = view.findViewById(R.id.buttonFavorite);
        }
    }
}
