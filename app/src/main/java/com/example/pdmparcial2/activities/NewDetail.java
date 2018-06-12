package com.example.pdmparcial2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.model.New;
import com.squareup.picasso.Picasso;

public class NewDetail extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        ImageView newImageView = findViewById(R.id.detail_newImageView);
        TextView titleTextView = findViewById(R.id.detail_newTitleTextView);
        TextView descriptionTextView = findViewById(R.id.detail_newDescriptionTextView);
        TextView bodyTextView = findViewById(R.id.detail_newBodyTextView);
        TextView dateTextView = findViewById(R.id.detail_newDateTextView);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        New mNew = (New)bundle.getSerializable("NEW");

        titleTextView.setText(mNew.getTitle());
        descriptionTextView.setText(mNew.getDescription());
        bodyTextView.setText(mNew.getBody());
        dateTextView.setText(mNew.getCreate_date());

        try{
            if (!mNew.getCoverImage().isEmpty()) {
                Picasso.get().load(mNew.getCoverImage()).error(R.drawable.ic_image).into(newImageView);
            }else{
                Picasso.get().load(R.drawable.ic_image).error(R.drawable.ic_image).into(newImageView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}