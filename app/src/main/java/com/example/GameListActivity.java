package com.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class GameListActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    private RecyclerView gameListRecyclerView;
    private LinearLayoutManager gameListLayoutManager;
    private GameListRecyclerViewAdapter gameListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        gameListRecyclerView = (RecyclerView) findViewById(R.id.game_list);
        gameListLayoutManager = new LinearLayoutManager(this);
        gameListRecyclerViewAdapter = new GameListRecyclerViewAdapter(this);

        gameListRecyclerView.setLayoutManager(gameListLayoutManager);
        gameListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        gameListRecyclerView.setAdapter(gameListRecyclerViewAdapter);
    }

    // TODO Here we need a class to show that, after the photos have been downloaded, the recycler view will be started


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
