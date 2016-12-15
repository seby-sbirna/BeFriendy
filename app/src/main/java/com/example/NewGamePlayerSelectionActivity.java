package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.DatabaseUser;
import com.example.EssentialClasses.Game;
import com.example.EssentialClasses.Player;

public class NewGamePlayerSelectionActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private EditText newGameInputEditText;
    private Button buttonConfirm;
    private DatabaseUser remotePlayerDatabaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_player_selection);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_new_game_player_selection);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        newGameInputEditText = (EditText) findViewById(R.id.new_game_input_edit_text);
        buttonConfirm = (Button) findViewById(R.id.button_confirm_new_game);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String writtenText = newGameInputEditText.getText().toString();
                if (writtenText.isEmpty() || writtenText.equals(" ") || writtenText.equals("  ") || writtenText.equals("   ")) {
                    Toast.makeText(getApplicationContext(), "Please type an answer before submitting!", Toast.LENGTH_LONG).show();
                } else {
                    for (DatabaseUser databaseUser : Player.get().getUsers()) {
                        if (databaseUser.getUserName().equals(writtenText) || databaseUser.getEmail().equals(writtenText)) {
                            remotePlayerDatabaseUser = databaseUser;
                            break;
                        }
                    }

                    if (remotePlayerDatabaseUser != null) {
                        boolean alreadyHasAGameStartedWithUser = false;
                        for (DatabaseGame gameFromPlayerGameList : Player.get().getGameList()) {
                            if ((gameFromPlayerGameList.getPlayer1Id().equals(Player.get().getUserId()) && gameFromPlayerGameList.getPlayer2Id().equals(remotePlayerDatabaseUser.getUserId()))
                                    || (gameFromPlayerGameList.getPlayer1Id().equals(remotePlayerDatabaseUser.getUserId()) && gameFromPlayerGameList.getPlayer2Id().equals(Player.get().getUserId())))
                                alreadyHasAGameStartedWithUser = true;
                        }
                        if (alreadyHasAGameStartedWithUser == false) {
                            Game newGame = new Game(remotePlayerDatabaseUser, NewGamePlayerSelectionActivity.this);

                            Intent i = new Intent(NewGamePlayerSelectionActivity.this, GameListActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "We're sorry, but you already have an ongoing game with this player!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "We're very sorry, but we could not find any user with the specified data!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
