package com.example;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.DatabaseUser;
import com.example.EssentialClasses.Player;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;
import java.util.Map;

public class RetrieveInfoFromDatabase extends Service {

    private static final String TAG = "Service";
    private DatabaseReference mDatabase;
    private int gameCount = 0;

    public RetrieveInfoFromDatabase() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //do logic here
        Log.d(TAG, "Service is working");

        //initialization of firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Player.get().getUsers().clear();
        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            // Retrieve all users as they are in the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                DatabaseUser user = snapshot.getValue(DatabaseUser.class); //this is how you parse information from firebase
                boolean userAlreadyAdded = Player.get().getUser(user.getUserId()) != null;
                if (!userAlreadyAdded) {
                    Player.get().getUsers().add(user);                 // add registered user in Reg.Users list

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //check if there is any change in the user info. If there is change, update it
                //TODO Player.get().getUser() is a super slow operation as it goes through all the players everytime
                //think of a way to avoid that!!!
                DatabaseUser user = dataSnapshot.getValue(DatabaseUser.class);
                if (Player.get().getUser(user.getUserId()) != null && Player.get().getUser(user.getUserId()).getUserName() != null &&
                        Player.get().getUser(user.getUserId()).getEmail() != null && Player.get().getUser(user.getUserId()).getPassword() != null &&
                        Player.get().getUser(user.getUserId()).getUserGames() != null) {
                    boolean userNameChanged = (Player.get().getUser(user.getUserId()).getUserName()).equals(user.getUserName());
                    boolean userEmailChanged = (Player.get().getUser(user.getUserId()).getEmail()).equals(user.getEmail());
                    boolean userPasswordChanged = (Player.get().getUser(user.getUserId()).getPassword()).equals(user.getPassword());
                    boolean userGamesChanged = (Player.get().getUser(user.getUserId()).getUserGames()).equals(user.getUserGames());

                    if (!userNameChanged) {
                        Player.get().getUser(user.getUserId()).setUserName(user.getUserName());
                    }
                    if (!userEmailChanged) {
                        Player.get().getUser(user.getUserId()).setEmail(user.getEmail());
                    }
                    if (!userPasswordChanged) {
                        Player.get().getUser(user.getUserId()).setPassword(user.getPassword());
                    }
                    if (!userGamesChanged) {
                        Player.get().getUser(user.getUserId()).setUserGames(user.getUserGames());
                    }

                }

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TODO implement later
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
        //we can get users from here

        mDatabase.child("games").addChildEventListener(new ChildEventListener() {
            // Retrieve all games as they are in the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                DatabaseGame game = snapshot.getValue(DatabaseGame.class); //this is how you parse information from firebase
                //Hristo: i will do it in a idiotic way for now, but it is faster for coding

                //this is how we iterate the HashMap. Long live stackoverflow
                Iterator it = Player.get().getUsergameListIds().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    if (pair.getKey().equals(game.getGameId()) ) {
                        //adds only the current player games
                        Player.get().addGameToGameList(game);                 // add game to the player
                        gameCount++;
                        Log.d(TAG, "There are " + gameCount + " games now");
                        break;
                    }
                    //it.remove(); // avoids a ConcurrentModificationException
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DatabaseGame snapshotGame = dataSnapshot.getValue(DatabaseGame.class);

                //when something is changed the event fires and updates game information
                for (DatabaseGame currGame : Player.get().getGameList()){
                    if(snapshotGame.getGameId().equals(currGame.getGameId())){
                        currGame.setPlayer1Position(snapshotGame.getPlayer1Position());
                        currGame.setPlayer2Position(snapshotGame.getPlayer2Position());
                        currGame.setPlayer1Turn(snapshotGame.isPlayer1Turn());
                        currGame.setUsedDaresId(snapshotGame.getUsedDaresId());
                        currGame.setUsedTruthsId(snapshotGame.getUsedTruthsId());

                        break;
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TODO implement later
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        stopSelf();
    }
}
