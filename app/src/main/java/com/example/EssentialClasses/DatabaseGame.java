package com.example.EssentialClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k0r on 16-Nov-16.
 */

public class DatabaseGame {

    //things to save in the database
    //Id of the game, player 1 id, player 2 id , their positions on the field, game field, whose turn it is, array with all the question id's we have passed
    //save information for one turn of the questions

    /*

    there should be a service running in the background all the time, that would be checking/extracting info about all the games of the current player
    maybe it would be a good idea to save the game ids into each player's database

    there should be a method when a game is created that should save all that info in the database
    there should be a method when each player plays their turn, it should update the info in the database,
    updating info in the database would automaticaly fire the event in the service for the other player and also for the current one,
    this is why we have the boolean to check whose turn it is, then different shit should happen

    at a later stage we need to think about saving and retriving truths and dares player answers
     */
    private String gameId;
    private String player1Id;
    private String player2Id;
    private String player1Position;
    private String player2Position;
    private ArrayList<Integer> boardFields;
    private boolean isPlayer1Turn;
    private ArrayList<String> usedTruthsId;
    private ArrayList<String> usedDaresId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(String player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(String player2Id) {
        this.player2Id = player2Id;
    }

    public String getPlayer1Position() {
        return player1Position;
    }

    public void setPlayer1Position(String player1Position) {
        this.player1Position = player1Position;
    }

    public String getPlayer2Position() {
        return player2Position;
    }

    public void setPlayer2Position(String player2Position) {
        this.player2Position = player2Position;
    }

    public ArrayList<Integer> getBoardFields() {
        return boardFields;
    }

    public void setBoardFields(ArrayList<Integer> boardFields) {
        this.boardFields = boardFields;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        isPlayer1Turn = player1Turn;
    }

    public ArrayList<String> getUsedTruthsId() {
        return usedTruthsId;
    }

    public void setUsedTruthsId(ArrayList<String> usedTruthsId) {
        this.usedTruthsId = usedTruthsId;
    }

    public ArrayList<String> getUsedDaresId() {
        return usedDaresId;
    }

    public void setUsedDaresId(ArrayList<String> usedDaresId) {
        this.usedDaresId = usedDaresId;
    }
    //TODO: save information for the last round



}
