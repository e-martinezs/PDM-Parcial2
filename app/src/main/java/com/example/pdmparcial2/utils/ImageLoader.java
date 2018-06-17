package com.example.pdmparcial2.utils;

import android.widget.ImageView;

import com.example.pdmparcial2.R;
import com.squareup.picasso.Picasso;

public class ImageLoader {

    //Usa Picasso para descargar la imagen de la url y la coloca en el ImageView
    public static void LoadImage(String url, ImageView imageView){
        try {
            if (!url.isEmpty()) {
                Picasso.get().load(url).error(R.drawable.ic_image).into(imageView);
            } else {
                Picasso.get().load(R.drawable.ic_image).error(R.drawable.ic_image).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
