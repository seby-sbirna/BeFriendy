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

    //things to save in the database
    //Id of the game, player 1 id, player 2 id , their positions on the field, game field, whose turn it is, array with all the question id's we have passed
    //save information for one turn of the questions
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
