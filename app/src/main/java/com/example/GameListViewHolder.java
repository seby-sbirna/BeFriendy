package com.example;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by XPS on 11/5/2016
 */

public class GameListViewHolder extends RecyclerView.ViewHolder {
    protected ImageView player_image;
    protected TextView player_name;
    protected Button play_button;

    public GameListViewHolder(View view) {
        super(view);
        this.player_image = (ImageView) view.findViewById(R.id.player_image);
        this.player_name = (TextView) view.findViewById(R.id.player_name);
        this.play_button = (Button) view.findViewById(R.id.play_button);
    }
}
