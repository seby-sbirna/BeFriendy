package com.example.EssentialClasses;

import java.util.List;

/**
 * Created by XPS on 11/3/2016.
 */

public class Player {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private List<Game> gameList;
    private boolean rememberCredentials;
    private List<Player> friendList;
    private PlayToken token;

    public Player(String firstName, String emailAddress) {
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.token = PlayToken.DEFAULT_TOKEN;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    public List<Game> retrieveGameList() {
        return gameList;
    }

    public void addGameToGameList(Game game) {
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

    public Game newGame(String remotePlayerName, String remotePlayerEmailAddress) {
        Game g =  new Game(gameList.size(), this,  new Player(remotePlayerName, remotePlayerEmailAddress));
        gameList.add(g);
        return g;
    }

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
}
