package com.example.pdmparcial2.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdmparcial2.R;
import com.example.pdmparcial2.activities.PlayerDetail;
import com.example.pdmparcial2.model.Player;
import com.example.pdmparcial2.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> players;
    private Context context;

    public PlayerAdapter(Context context) {
        this.context = context;
        players = new ArrayList<>();
    }

    //Actualiza la lista de jugadores
    public void setPlayers(List<Player> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        final Player player = players.get(position);
        holder.playerNameTextView.setText(player.getName());

        //Carga la imagen usando Picasso
        ImageLoader.LoadImage(player.getAvatar(), holder.playerImageView);

        //Abre la actividad detalle del jugador
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayerDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PlayerDetail.PLAYER, player);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView playerImageView;
        private TextView playerNameTextView;

        public PlayerViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.cardview_player);
            playerImageView = view.findViewById(R.id.playerImageView);
            playerNameTextView = view.findViewById(R.id.playerNameTextView);
        }
    }
}
