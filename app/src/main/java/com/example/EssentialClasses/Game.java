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
        //TODO CHANGE HERE
        this.board = new Board();
        board.addField(new Minigame(1, 1));
        board.addField(new Truth(2, 2));
        board.addField(new Dare(3, 3));
        board.addField(new Minigame(4, 4));
        board.addField(new Truth(5, 5));
        board.addField(new Dare(6, 6));
        board.addField(new Truth(7, 7));
        board.addField(new Dare(8, 8));
        board.addField(new Wildcard(9, 9));
        board.addField(new Dare(10, 10));
        board.addField(new Gameplay(11, 11));
        board.addField(new Dare(12, 12));
        board.addField(new Truth(13, 13));
        board.addField(new Minigame(14, 14));
        board.addField(new Dare(15, 15));
        board.addField(new Gameplay(16, 16));
        board.addField(new Wildcard(17, 17));
        board.addField(new Truth(18, 18));
        board.addField(new Dare(19, 19));
        board.addField(new Truth(20, 20));
        board.addField(new Wildcard(21, 21));
        board.addField(new Minigame(22, 22));
        board.addField(new Dare(23, 23));
        board.addField(new Gameplay(24, 24));
        board.addField(new Truth(25, 25));
        board.addField(new Wildcard(26, 26));
        board.addField(new Dare(27, 27));
        board.addField(new Truth(28, 28));
        board.addField(new Wildcard(29, 29));
    }
}
