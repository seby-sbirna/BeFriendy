package com.example.EssentialClasses;

/**
 * Created by XPS on 11/3/2016.
 */

public class Board {
    private int[] boardFields = {-1, 4, 1, 2, 4, 1, 2, 1, 2, 5, 2, 3, 2, 1, 4, 2, 3, 5, 1, 2, 1, 5, 4, 2, 3, 1, 5, 2, 1, 5, 0}; // -1 - Start, 0 - Finish, 1- Truth, 2 - Dare, 3 - Gameplay, 4 - Minigame, 5 - Wildcard, 6 - Checkpoint(?)
    private Player localPlayer;
    private Player remotePlayer;
    private int localPlayerPosition;
    private int remotePlayerPosition;
    private PlayToken localPlayerToken;
    private PlayToken remotePlayerToken;

    public Board(Player localPlayer, Player remotePlayer) {
        this.localPlayer = localPlayer;
        this.remotePlayer = remotePlayer;
        this.localPlayerToken = this.localPlayer.getToken();
        this.remotePlayerToken = this.remotePlayer.getToken();
        localPlayerPosition = 0;
        remotePlayerPosition = 0;
    }

    public int getBoardFieldType(int index){
        return boardFields[index];
    }

    public int getLocalPlayerPosition() {
        return localPlayerPosition;
    }

    public int getRemotePlayerPosition() {
        return remotePlayerPosition;
    }

    private void setLocalPlayerPosition(int position) {
        this.localPlayerPosition = position;
    }

    private void setRemotePlayerPosition(int position) {
        this.remotePlayerPosition = position;
    }

    public void setLocalPlayer(Player player) {
        this.localPlayer = player;
    }

    public void setRemotePlayer(Player player) {
        this.remotePlayer = player;
    }

    public void moveOnBoard(Player player, int steps) {
        if (player.getEmailAddress().equals(localPlayer.getEmailAddress())) {
            //TODO execute animations
            setLocalPlayerPosition(localPlayerPosition + steps);
        } else if (player.getEmailAddress().equals(remotePlayer.getEmailAddress())) {
            //TODO execute animations
            setRemotePlayerPosition(remotePlayerPosition + steps);
        }
    }
}
