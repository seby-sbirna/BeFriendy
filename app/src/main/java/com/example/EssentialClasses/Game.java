package com.example.EssentialClasses;

/**
 * Created by XPS on 11/3/2016.
 */

public class Game {
    private int id;
    private Board board;
    private Player localPlayer;
    private Player remotePlayer;
    private boolean isTurnEnded;

    public Game(int id, Player localPlayer, Player remotePlayer) {
        this.id = id;
        this.localPlayer = localPlayer;
        this.remotePlayer = remotePlayer;
        createBoard();
    }

    private int play() {
        //TODO play mechanics
        return -1; // DELETE THIS
    }

    public Board getBoard() {
        return this.board;
    }

    private void createBoard() {
        this.board = new Board(localPlayer, remotePlayer);
    }
}
