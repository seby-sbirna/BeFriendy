package com.example;

import android.content.Context;
import android.content.Intent;
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
        char nameInitial;
        String player_name;

        if (Player.get().isDormantTruthQuestionInvalidated())
            Player.get().retrieveNewDormantTruthQuestion();
        if (Player.get().isDormantDareQuestionInvalidated())
            Player.get().retrieveNewDormantDareQuestion();

        if (dg_list.get(position).getPlayer1Id().equals(Player.get().getUserId())) {
            player_name = Player.get().getUser(dg_list.get(position).getPlayer2Id()).getUserName();
            nameInitial = player_name.toUpperCase().charAt(0);

            if ((dg_list.get(position).isPlayer1Turn() == true && dg_list.get(position).getIsPlayer1AdditionalActionNeeded() == false && dg_list.get(position).getIsPlayer2AdditionalActionNeeded() == false)
                    || (dg_list.get(position).isPlayer1Turn() == true && dg_list.get(position).getIsPlayer1AdditionalActionNeeded() == true)
                    || (dg_list.get(position).isPlayer1Turn() == false && dg_list.get(position).getIsPlayer1AdditionalActionNeeded() == true)) {
                holder.play_button.setBackgroundColor(Color.parseColor("#64dd17"));
                holder.play_button.setText("PLAY");
            } else {
                holder.play_button.setBackgroundColor(Color.parseColor("#9c27b0"));
                holder.play_button.setText("VIEW");
                holder.play_button.setTextColor(Color.parseColor("#FFFFFF"));
            }
        } else {
            player_name = Player.get().getUser(dg_list.get(position).getPlayer1Id()).getUserName();
            nameInitial = player_name.toUpperCase().charAt(0);

            if ((dg_list.get(position).isPlayer1Turn() == false && dg_list.get(position).getIsPlayer1AdditionalActionNeeded() == false && dg_list.get(position).getIsPlayer2AdditionalActionNeeded() == false)
                    || (dg_list.get(position).isPlayer1Turn() == false && dg_list.get(position).getIsPlayer2AdditionalActionNeeded() == true)
                    || (dg_list.get(position).isPlayer1Turn() == true && dg_list.get(position).getIsPlayer2AdditionalActionNeeded() == true)) {
                holder.play_button.setBackgroundColor(Color.parseColor("#64dd17"));
                holder.play_button.setText("PLAY");
            } else {
                holder.play_button.setBackgroundColor(Color.parseColor("#9c27b0"));
                holder.play_button.setText("VIEW");
                holder.play_button.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }

        if(nameInitial >= 'A' && nameInitial <= 'E') {
            holder.player_image.setBackgroundColor(Color.parseColor("red"));
        } else if (nameInitial >= 'F' && nameInitial <= 'I') {
            holder.player_image.setBackgroundColor(Color.parseColor("green"));
            holder.letter_initial.setTextColor(Color.parseColor("#000000"));
        } else if (nameInitial >= 'J' && nameInitial <= 'N') {
            holder.player_image.setBackgroundColor(Color.parseColor("cyan"));
            holder.letter_initial.setTextColor(Color.parseColor("#000000"));
        } else if (nameInitial >= 'O' && nameInitial <= 'R') {
            holder.player_image.setBackgroundColor(Color.parseColor("purple"));
        } else if (nameInitial >= 'S' && nameInitial <= 'V') {
            holder.player_image.setBackgroundColor(Color.parseColor("blue"));
        } else if (nameInitial >= 'W' && nameInitial <= 'Z') {
            holder.player_image.setBackgroundColor(Color.parseColor("yellow"));
            holder.letter_initial.setTextColor(Color.parseColor("#000000"));
        }

        String name_initial = Character.toString(nameInitial);
        holder.letter_initial.setText(name_initial.toUpperCase());
        holder.player_name.setText(player_name);

        final GameListViewHolder finalHolder = holder;
        holder.play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recreateGameIntent = new Intent(context, GameActivity.class);
                recreateGameIntent.putExtra("gameId", dg_list.get(finalHolder.getAdapterPosition()).getGameId());
                recreateGameIntent.putExtra("hasPlayerRolledTheDice", dg_list.get(finalHolder.getAdapterPosition()).getHasPlayerRolledTheDice());
                recreateGameIntent.putExtra("isPlayer1AdditionalActionNeeded", dg_list.get(finalHolder.getAdapterPosition()).getIsPlayer1AdditionalActionNeeded());
                recreateGameIntent.putExtra("isPlayer2AdditionalActionNeeded", dg_list.get(finalHolder.getAdapterPosition()).getIsPlayer2AdditionalActionNeeded());
                recreateGameIntent.putExtra("player1Id", dg_list.get(finalHolder.getAdapterPosition()).getPlayer1Id());
                recreateGameIntent.putExtra("player1Position", dg_list.get(finalHolder.getAdapterPosition()).getPlayer1Position());
                recreateGameIntent.putExtra("player1Turn", dg_list.get(finalHolder.getAdapterPosition()).isPlayer1Turn());
                recreateGameIntent.putExtra("player2Id", dg_list.get(finalHolder.getAdapterPosition()).getPlayer2Id());
                recreateGameIntent.putExtra("player2Position", dg_list.get(finalHolder.getAdapterPosition()).getPlayer2Position());
                recreateGameIntent.putExtra("player1TokenId", Player.get().getUser(dg_list.get(finalHolder.getAdapterPosition()).getPlayer1Id()).getTokenId());
                recreateGameIntent.putExtra("player2TokenId", Player.get().getUser(dg_list.get(finalHolder.getAdapterPosition()).getPlayer2Id()).getTokenId());
                if (dg_list.get(finalHolder.getAdapterPosition()).getPlayer1Id().equals(Player.get().getUserId()))
                    recreateGameIntent.putExtra("remotePlayerId", dg_list.get(finalHolder.getAdapterPosition()).getPlayer2Id());
                else
                    recreateGameIntent.putExtra("remotePlayerId", dg_list.get(finalHolder.getAdapterPosition()).getPlayer1Id());
                context.startActivity(recreateGameIntent);
            }
        });
    }
}
