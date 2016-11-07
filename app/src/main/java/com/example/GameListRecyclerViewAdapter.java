package com.example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EssentialClasses.Photo;

import java.util.List;

/**
 * Created by XPS on 11/5/2016.
 */
public class GameListRecyclerViewAdapter extends RecyclerView.Adapter<GameListViewHolder> {
    private List<Photo> playerPhotoList;
    private Context context;

    public GameListRecyclerViewAdapter(List<Photo> photoList, Context context) {
        this.playerPhotoList = photoList;
        this.context = context;
    }

    @Override
    public GameListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_recycler_view, null);
        GameListViewHolder gameListViewHolder = new GameListViewHolder(view);
        return gameListViewHolder;
    }


    @Override
    public int getItemCount() {
        return (null != playerPhotoList ? playerPhotoList.size() : 0);
    }

    @Override
    public void onBindViewHolder(GameListViewHolder holder, int position) {

    }
}
