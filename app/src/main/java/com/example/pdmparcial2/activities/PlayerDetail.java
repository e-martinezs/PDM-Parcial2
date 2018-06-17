package com.example.pdmparcial2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.utils.ImageLoader;

public class PlayerDetail extends AppCompatActivity {

    public static String PLAYER = "PLAYER";

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_player_detail);

        ImageView avatarImageView = findViewById(R.id.detail_playerImageView);
        TextView nameTextView = findViewById(R.id.detail_playerNameTextView);
        TextView biographyTextView = findViewById(R.id.detail_playerBiographyTextView);

        //Obtiene el jugador que se mostrara
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Player player = (Player) bundle.getSerializable(PLAYER);

        nameTextView.setText(player.getName());
        biographyTextView.setText(player.getBiography());

        //Carga la imagen con Picasso
        ImageLoader.LoadImage(player.getAvatar(), avatarImageView);
    }
}
