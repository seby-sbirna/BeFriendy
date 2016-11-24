package com.example;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.Player;

import java.util.List;

/**
 * Created by XPS on 11/5/2016.
 */
public class GameListRecyclerViewAdapter extends RecyclerView.Adapter<GameListViewHolder> {
    private Context context;
    private List<DatabaseGame> dg_list;

    public GameListRecyclerViewAdapter(Context context) {
        this.context = context;
        dg_list = Player.get().getGameList();
        Log.d("GameListRVAdapter", "I have " + dg_list.size() + "in my adapter.");

    }

    @Override
    public GameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_recycler_view, parent, false);
        GameListViewHolder gameListViewHolder = new GameListViewHolder(view);
        return gameListViewHolder;
    }


    @Override
    public int getItemCount() {
        return (null != Player.get().getGameList() ? Player.get().getGameList().size() : 0);
    }

    @Override
    public void onBindViewHolder(GameListViewHolder holder, int position) {
        Log.d("", "Position is: " + position + ", ID is: " + dg_list.get(position).getPlayer2Id());
        String player2_name = Player.get().getUser(dg_list.get(position).getPlayer2Id()).getUserName();
        char nameInitial = player2_name.toUpperCase().charAt(0);

        if(nameInitial >= 'A' && nameInitial <= 'E') {
            holder.player_image.setBackgroundColor(Color.parseColor("red"));
        } else if (nameInitial >= 'F' && nameInitial <= 'I') {
            holder.player_image.setBackgroundColor(Color.parseColor("green"));
        } else if (nameInitial >= 'J' && nameInitial <= 'N') {
            holder.player_image.setBackgroundColor(Color.parseColor("cyan"));
        } else if (nameInitial >= 'O' && nameInitial <= 'R') {
            holder.player_image.setBackgroundColor(Color.parseColor("purple"));
        } else if (nameInitial >= 'S' && nameInitial <= 'V') {
            holder.player_image.setBackgroundColor(Color.parseColor("blue"));
        } else if (nameInitial >= 'W' && nameInitial <= 'Z') {
            holder.player_image.setBackgroundColor(Color.parseColor("yellow"));
        }
        String name_initial = player2_name.substring(0,1);
        holder.letter_initial.setText(name_initial.toUpperCase());
        holder.player_name.setText(player2_name);

        if (dg_list.get(position).isPlayer1Turn()) {
            holder.play_button.setBackgroundColor(Color.parseColor("#64dd17"));
            holder.play_button.setText("PLAY");
        } else {
            holder.play_button.setBackgroundColor(Color.parseColor("#9c27b0"));
            holder.play_button.setText("VIEW");
        }

        holder.play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Here it should get you to that specific instance of the GameActivity (I mean, to that specific game)
            }
        });
    }
}
