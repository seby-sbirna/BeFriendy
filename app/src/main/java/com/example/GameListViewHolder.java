package com.example;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by XPS on 11/5/2016
 */

public class GameListViewHolder extends RecyclerView.ViewHolder {
    protected CardView cardView;
    protected ImageView player_image;
    protected TextView player_name;
    protected Button play_button;
    protected TextView letter_initial;

    public GameListViewHolder(View view) {
        super(view);
        this.cardView = (CardView) view.findViewById(R.id.card_view);
        this.player_image = (ImageView) view.findViewById(R.id.player_image);
        this.player_name = (TextView) view.findViewById(R.id.player_name);
        this.play_button = (Button) view.findViewById(R.id.play_button);
        this.letter_initial = (TextView) view.findViewById(R.id.letter_initial);
    }
}
