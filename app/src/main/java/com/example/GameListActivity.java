package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.EssentialClasses.DatabaseUser;
import com.example.EssentialClasses.Player;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameListActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private RecyclerView gameListRecyclerView;
    private LinearLayoutManager gameListLayoutManager;
    private GameListRecyclerViewAdapter gameListRecyclerViewAdapter;

    private FloatingActionButton newGameFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        for (DatabaseUser currUser : Player.get().getUsers()) {
            if (Player.get().getUserId().equals(currUser.getUserId())) {
                Player.get().setUserName(currUser.getUserName());
                Player.get().setEmailAddress(currUser.getEmail());
                Player.get().setUsergameListIds(currUser.getUserGames());
            }
        }

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

        newGameFloatingButton = (FloatingActionButton) findViewById(R.id.new_game_floating_button);
        newGameFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GameListActivity.this, NewGamePlayerSelectionActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
