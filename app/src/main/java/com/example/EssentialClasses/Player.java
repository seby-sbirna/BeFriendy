package com.example.EssentialClasses;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by XPS on 11/3/2016.
 */

public class Player {
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private List<DatabaseGame> gameList;
    private boolean rememberCredentials;
    private List<Player> friendList;
    private PlayToken token;
    private Map<String, String> usergameListIds;


    private ArrayList<DatabaseUser> userList;

    private static Player currPlayer;

    //uses Singelton -> only 1 Player object possible;
    public static Player get(){
        if(currPlayer == null){
            currPlayer = new Player();
        }
        return currPlayer;
    }

    public Player(){
        userList = new ArrayList<DatabaseUser>();
        gameList = new ArrayList<DatabaseGame>();
    }

    public Player(String firstName, String emailAddress) {
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.token = PlayToken.DEFAULT_TOKEN;
        this.userId = UUID.randomUUID().toString();
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

    public void setUserId(String userId){
        this.userId = userId;
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

    public void removeGameToGameList(int id) {
        this.gameList.remove(id);
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

    public PlayToken getToken() {
        return token;
    }

    public void setToken(int shapeId, int colourId) {
        this.token.setShape(shapeId);
        this.token.setColor(colourId);
    }

    //Hristo: i don't think this should be here
    /*
    public Game newGame(String remotePlayerName, String remotePlayerEmailAddress) {
        Game g =  new Game(gameList.size(), this,  new Player(remotePlayerName, remotePlayerEmailAddress));
        gameList.add(g);
        return g;
    }
    */

    public void inviteFriend(String friendEmailAddress){
        //TODO !!!
    }

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

    public Map<String, String> getUsergameListIds() {
        return usergameListIds;
    }

    public void setUsergameListIds(Map<String, String> usergameListIds) {
        this.usergameListIds = usergameListIds;
    }
}
