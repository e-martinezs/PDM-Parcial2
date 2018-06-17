package com.example.pdmparcial2.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.api.APIRequest;
import com.example.pdmparcial2.database.viewmodels.NewViewModel;
import com.example.pdmparcial2.model.New;
import com.example.pdmparcial2.utils.ImageLoader;

public class NewDetail extends AppCompatActivity {

    public static String NEW = "NEW";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        ImageView newImageView = findViewById(R.id.detail_newImageView);
        TextView titleTextView = findViewById(R.id.detail_newTitleTextView);
        TextView descriptionTextView = findViewById(R.id.detail_newDescriptionTextView);
        TextView bodyTextView = findViewById(R.id.detail_newBodyTextView);
        TextView dateTextView = findViewById(R.id.detail_newDateTextView);
        final CheckBox favoriteButton = findViewById(R.id.detail_buttonFavorite);

        //Obtiene la noticia que se mostrara
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final New mNew = (New) bundle.getSerializable(NEW);

        titleTextView.setText(mNew.getTitle());
        descriptionTextView.setText(mNew.getDescription());
        bodyTextView.setText(mNew.getBody());
        dateTextView.setText(mNew.getCreate_date().subSequence(0, 10));

        //Carga la imagen con Picasso
        ImageLoader.LoadImage(mNew.getCoverImage(), newImageView);

        //Verifica si la noticia esta en favoritos
        if (mNew.isFavorite()) {
            favoriteButton.setChecked(true);
        } else {
            favoriteButton.setChecked(false);
        }

        //Agrega o elimina la noticia de favoritos al presionar el boton
        final NewViewModel newViewModel = ViewModelProviders.of(this).get(NewViewModel.class);
        final APIRequest apiRequest = new APIRequest(this, null, newViewModel, null, null);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!favoriteButton.isChecked()) {
                    mNew.setFavorite(false);
                    apiRequest.deleteFavorite(mNew.getId());
                } else {
                    mNew.setFavorite(true);
                    apiRequest.saveFavorite(mNew.getId());
                }
            }
        });
    }
}