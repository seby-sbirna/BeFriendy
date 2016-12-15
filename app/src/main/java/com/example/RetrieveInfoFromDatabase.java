package com.example;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.google.firebase.database.ValueEventListener;

public class RetrieveInfoFromDatabase extends Service {

    private static final String TAG = "Service";
    NotificationManager notificationManager;
    private DatabaseReference mDatabase;
    private int gameCount = 0;

    public RetrieveInfoFromDatabase() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, int startId) {
        //do logic here
        Log.d(TAG, "Service is working");

        notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

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
                //TODO Player.get().getUser() is a super slow operation as it goes through all the players every time
                //think of a way to avoid that!!!
                DatabaseUser user = dataSnapshot.getValue(DatabaseUser.class);
                String userID = user.getUserId();
                if (Player.get().getUser(userID) != null) {
                    DatabaseUser foundUser = Player.get().getUser(userID);
                    if (foundUser.getUserName() != null &&
                            foundUser.getEmail() != null &&
                            foundUser.getPassword() != null &&
                            foundUser.getUserGames() != null) {

                        boolean userNameNotChanged = (Player.get().getUser(userID).getUserName()).equals(user.getUserName());
                        boolean userEmailNotChanged = (Player.get().getUser(userID).getEmail()).equals(user.getEmail());
                        boolean userPasswordNotChanged = (Player.get().getUser(userID).getPassword()).equals(user.getPassword());
                        boolean userGamesNotChanged = (Player.get().getUser(userID).getUserGames()).equals(user.getUserGames());
                        boolean userTokenNotChanged = (Player.get().getUser(userID).getTokenId() == user.getTokenId());

                        if (!userNameNotChanged) {
                            Player.get().getUser(userID).setUserName(user.getUserName());
                        }
                        if (!userEmailNotChanged) {
                            Player.get().getUser(userID).setEmail(user.getEmail());
                        }
                        if (!userPasswordNotChanged) {
                            Player.get().getUser(userID).setPassword(user.getPassword());
                        }
                        if (!userGamesNotChanged) {
                            Player.get().getUser(userID).setUserGames(user.getUserGames());
                        }
                        if (!userTokenNotChanged) {
                            Player.get().getUser(userID).setTokenId(user.getTokenId());
                        }

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

                if (game.getPlayer1Id().equals(Player.get().getUserId()) || game.getPlayer2Id().equals(Player.get().getUserId())) {
                    boolean exists = false;
                    for (DatabaseGame g : Player.get().getGameList()) {
                        if (g.getGameId().equals(game.getGameId())) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists == false) {
                        Player.get().addGameToGameList(game);
                    }
                }
                /*
                //this is how we iterate the HashMap. Long live stackoverflow
                if (Player.get().getUsergameListIds() != null) {
                    Iterator it = Player.get().getUsergameListIds().entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (entry.getKey().equals(game.getGameId())) {
                            boolean exists = false;
                            for (DatabaseGame g : Player.get().getGameList()) {
                                if (g.getGameId().equals(game.getGameId()))
                                    exists = true;
                            }
                            if (exists == false) {
                                //adds only the current player games
                                Player.get().addGameToGameList(game);                 // add game to the player
                                gameCount++;
                                Log.d(TAG, "There are " + gameCount + " games now");
                                break;
                            }
                        }
                        //it.remove(); // avoids a ConcurrentModificationException
                    }
                } */
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                DatabaseGame snapshotGame = dataSnapshot.getValue(DatabaseGame.class);

                //when something is changed the event fires and updates game information
                for (final DatabaseGame currGame : Player.get().getGameList()) {
                    if (snapshotGame.getGameId().equals(currGame.getGameId())) {
                        Intent intentToGame = new Intent(RetrieveInfoFromDatabase.this, GameActivity.class);
                        if (snapshotGame.getPlayer1Id().equals(Player.get().getUserId())) {
                            intentToGame.putExtra("remotePlayerId", snapshotGame.getPlayer2Id());
                        } else {
                            intentToGame.putExtra("remotePlayerId", snapshotGame.getPlayer1Id());
                        }
                        intentToGame.putExtra("gameId", snapshotGame.getGameId());
                        PendingIntent pendingIntentToGame = PendingIntent.getActivity(RetrieveInfoFromDatabase.this, (int) System.currentTimeMillis(), intentToGame, 0);
                        Notification notification;

                        if (((snapshotGame.getPlayer1Id().equals(Player.get().getUserId())) && (currGame.getPlayer2Position() != snapshotGame.getPlayer2Position())) || ((snapshotGame.getPlayer2Id().equals(Player.get().getUserId())) && (currGame.getPlayer1Position() != snapshotGame.getPlayer1Position()))) {
                            // Other player position changed
                            notification = new Notification.Builder(RetrieveInfoFromDatabase.this)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_check_circle_black_24px)
                                    .setContentTitle("New game update on game #" + String.valueOf(Player.get().getGameList().indexOf(currGame) + 1))
                                    .setContentText("Your friend has advanced on the game board!")
                                    .setContentIntent(pendingIntentToGame)
                                    .build();
                            notification.defaults |= Notification.DEFAULT_SOUND;
                            notificationManager.notify("Position changed notification", Player.get().getGameList().indexOf(currGame), notification);
                        }
                        if (currGame.isPlayer1Turn() != snapshotGame.isPlayer1Turn()) {
                            // Turn changed!
                            if ((snapshotGame.getPlayer1Id().equals(Player.get().getUserId()) && snapshotGame.isPlayer1Turn() == true) || (snapshotGame.getPlayer2Id().equals(Player.get().getUserId()) && snapshotGame.isPlayer1Turn() == false)) {
                                // Now it's my turn
                                notification = new Notification.Builder(RetrieveInfoFromDatabase.this)
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_check_circle_black_24px)
                                        .setContentTitle("New game update on game #" + String.valueOf(Player.get().getGameList().indexOf(currGame) + 1))
                                        .setContentText("It is now your turn!")
                                        .setContentIntent(pendingIntentToGame)
                                        .build();
                                notification.defaults |= Notification.DEFAULT_SOUND;
                                notificationManager.notify("Turn notification", Player.get().getGameList().indexOf(currGame), notification);
                            } else {
                                // Now it's the other player's turn
                                notification = new Notification.Builder(RetrieveInfoFromDatabase.this)
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_check_circle_black_24px)
                                        .setContentTitle("New game update on game #" + String.valueOf(Player.get().getGameList().indexOf(currGame) + 1))
                                        .setContentText("It is now your friend's turn!")
                                        .setContentIntent(pendingIntentToGame)
                                        .build();
                                notification.defaults |= Notification.DEFAULT_SOUND;
                                notificationManager.notify("Turn notification", Player.get().getGameList().indexOf(currGame), notification);
                            }
                        }

                        if ((currGame.getPlayer1Id().equals(Player.get().getUserId()) && snapshotGame.isPlayer1Turn() == true) || (currGame.getPlayer2Id().equals(Player.get().getUserId()) && snapshotGame.isPlayer1Turn() == false)) {
                            if (currGame.getHasPlayerRolledTheDice() == false && snapshotGame.getHasPlayerRolledTheDice() == true) {
                                // The other player has rolled the dice
                                notification = new Notification.Builder(RetrieveInfoFromDatabase.this)
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.ic_check_circle_black_24px)
                                        .setContentTitle("New game update on game #" + String.valueOf(Player.get().getGameList().indexOf(currGame) + 1))
                                        .setContentText("Your friend has rolled the dice!")
                                        .setContentIntent(pendingIntentToGame)
                                        .build();
                                notification.defaults |= Notification.DEFAULT_SOUND;
                                notificationManager.notify("Roll dice notification", Player.get().getGameList().indexOf(currGame), notification);
                            }
                        }

                        if ((currGame.getPlayer1Id().equals(Player.get().getUserId()) && currGame.getIsPlayer1AdditionalActionNeeded() == false && snapshotGame.getIsPlayer1AdditionalActionNeeded() == true) ||
                                (currGame.getPlayer2Id().equals(Player.get().getUserId()) && currGame.getIsPlayer2AdditionalActionNeeded() == false && snapshotGame.getIsPlayer2AdditionalActionNeeded() == true)) {
                            // You are requested additional action: the game continues!
                            notification = new Notification.Builder(RetrieveInfoFromDatabase.this)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_check_circle_black_24px)
                                    .setContentTitle("New game update on game #" + String.valueOf(Player.get().getGameList().indexOf(currGame) + 1))
                                    .setContentText("Something happened! :) Continue playing!")
                                    .setContentIntent(pendingIntentToGame)
                                    .build();
                            notification.defaults |= Notification.DEFAULT_SOUND;
                            notificationManager.notify("Additional action notification", Player.get().getGameList().indexOf(currGame), notification);
                        }

                        currGame.setPlayer1Position(snapshotGame.getPlayer1Position());
                        currGame.setPlayer2Position(snapshotGame.getPlayer2Position());
                        currGame.setPlayer1Turn(snapshotGame.isPlayer1Turn());
                        currGame.setUsedDaresId(snapshotGame.getUsedDaresId());
                        currGame.setUsedTruthsId(snapshotGame.getUsedTruthsId());
                        currGame.setHasPlayerRolledTheDice(snapshotGame.getHasPlayerRolledTheDice());
                        currGame.setIsPlayer1AdditionalActionNeeded(snapshotGame.getIsPlayer1AdditionalActionNeeded());
                        currGame.setIsPlayer2AdditionalActionNeeded(snapshotGame.getIsPlayer2AdditionalActionNeeded());
                        currGame.setTextPlaceholder1(snapshotGame.getTextPlaceholder1());
                        currGame.setTextPlaceholder2(snapshotGame.getTextPlaceholder2());
                        currGame.setTextPlaceholder3(snapshotGame.getTextPlaceholder3());
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

        mDatabase.child("truthCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player.get().setTotalTruthCount((long) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("dareCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player.get().setTotalDareCount((long) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("minigameCount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player.get().setTotalMinigameCount((long) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        stopSelf();
    }
}
