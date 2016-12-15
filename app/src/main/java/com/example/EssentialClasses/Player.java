package com.example.EssentialClasses;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by XPS on 11/3/2016.
 */

public class Player {
    private static Player currPlayer;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private List<DatabaseGame> gameList;
    private boolean rememberCredentials;
    private List<Player> friendList;
    private int token;
    private Map<String, String> usergameListIds;
    private long totalTruthCount;
    private long totalDareCount;
    private long totalMinigameCount;
    private String dormantTruthQuestion;
    private String dormantDareQuestion;
    private ArrayList<DatabaseUser> userList;

    public Player(){
        userList = new ArrayList<DatabaseUser>();
        gameList = new ArrayList<DatabaseGame>();
    }

    public Player(String firstName, String emailAddress) {
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.userId = UUID.randomUUID().toString();
    }

    //uses Singleton -> only 1 Player object possible;
    public static Player get() {
        if (currPlayer == null) {
            currPlayer = new Player();
        }
        return currPlayer;
    }

    public ArrayList<DatabaseUser> getUsers() {                         // get all Users
        return userList;
    }        // get all Users

    public DatabaseUser getUser(String id) {                                    // get a User by ID
        for (DatabaseUser u : userList) {
            if (u.getUserId().equals(id))
                return u;
        }
        return null;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<DatabaseGame> getGameList() {
        Log.d("Player", "There are " + gameList.size() + " games in the gameList");
        return gameList;
    }

    public void addGameToGameList(DatabaseGame game) {
        this.gameList.add(game);
    }

    public void removeGameFromGameList(int index) {
        this.gameList.remove(index);
    }

    public boolean isRememberCredentials() {
        return rememberCredentials;
    }

    public void setRememberCredentials(boolean rememberCredentials) {
        this.rememberCredentials = rememberCredentials;
    }

    public List<Player> getFriendList() {
        return friendList;
    }

    public void addFriendToFriendList(Player friend) {
        this.friendList.add(friend);
    }

    public void removeFriendFromFriendList(Player friend) {
        this.friendList.remove(friend);
    }

    public int getTokenDrawableId() {
        return token;
    }

    public void setToken(int id) {
        this.token = id;
    }

    public void inviteFriend(String friendEmailAddress) {
        //TODO !!!
    }

    //Hristo: i don't think this should be here
    /*
    public Game newGame(String remotePlayerName, String remotePlayerEmailAddress) {
        Game g =  new Game(gameList.size(), this,  new Player(remotePlayerName, remotePlayerEmailAddress));
        gameList.add(g);
        return g;
    }
    */

    public void showFriendList() {
        //TODO !!!
    }

    public void showGameHistory() {
        //TODO Here, first put a new layout representing the list, afterwards use a for-each loop to traverse the game history
        //TODO Not possible yet, we don't have a game history list
    }

    public void showAchievements() {
        //TODO !!!
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getUsergameListIds() {
        return usergameListIds;
    }

    public void setUsergameListIds(Map<String, String> usergameListIds) {
        this.usergameListIds = usergameListIds;
    }

    public long getTotalTruthCount() {
        return totalTruthCount;
    }

    public void setTotalTruthCount(long totalTruthCount) {
        this.totalTruthCount = totalTruthCount;
    }

    public long getTotalDareCount() {
        return totalDareCount;
    }

    public void setTotalDareCount(long totalDareCount) {
        this.totalDareCount = totalDareCount;
    }

    public long getTotalMinigameCount() {
        return totalMinigameCount;
    }

    public void setTotalMinigameCount(long totalMinigameCount) {
        this.totalMinigameCount = totalMinigameCount;
    }

    public String getDormantTruthQuestion() {
        return dormantTruthQuestion;
    }

    public String getDormantDareQuestion() {
        return dormantDareQuestion;
    }

    public void retrieveNewDormantTruthQuestion() {
        Random rng = new Random();
        if (Player.get().getTotalTruthCount() > 0) {
            int randomTruthIdResult = rng.nextInt((int) Player.get().getTotalTruthCount()) + 1;

            mDatabase.child("Truth").child(String.valueOf(randomTruthIdResult)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        return;
                    }
                    dormantTruthQuestion = String.valueOf(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    dormantTruthQuestion = null;
                }
            });
        }
    }

    public void retrieveNewDormantDareQuestion() {
        Random rng = new Random();
        if (Player.get().getTotalDareCount() > 0) {
            int randomDareIdResult = rng.nextInt((int) Player.get().getTotalDareCount()) + 1;

            mDatabase.child("Dare").child(String.valueOf(randomDareIdResult)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        return;
                    }
                    dormantDareQuestion = String.valueOf(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    dormantDareQuestion = null;
                }
            });
        }
    }

    public void invalidateDormantTruthQuestion() {
        dormantTruthQuestion = null;
    }

    public void invalidateDormantDareQuestion() {
        dormantDareQuestion = null;
    }

    public boolean isDormantTruthQuestionInvalidated() {
        return dormantTruthQuestion == null;
    }

    public boolean isDormantDareQuestionInvalidated() {
        return dormantDareQuestion == null;
    }
}
