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
    updating info in the database would automatically fire the event in the service for the other player and also for the current one,
    this is why we have the boolean to check whose turn it is, then different shit should happen

    at a later stage we need to think about saving and retrieving truths and dares player answers
     */
    private String gameId;
    private String player1Id;
    private String player2Id;
    private int player1Position;
    private int player2Position;
    private List<Integer> boardFields;
    private boolean isPlayer1Turn;
    private ArrayList<String> usedTruthsId;
    private ArrayList<String> usedDaresId;
    private boolean hasPlayerRolledTheDice;
    private boolean isPlayer1AdditionalActionNeeded;
    private boolean isPlayer2AdditionalActionNeeded;
    private String textPlaceholder1;
    private String textPlaceholder2;
    private String textPlaceholder3;

    public DatabaseGame (){
        //empty constructor for firebase
    }

    public DatabaseGame (String gameId, String player1Id, String player2Id, List<Integer> boardFields){
        this.gameId = gameId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player1Position = 0;
        this.player2Position = 0;
        this.boardFields = boardFields;
        this.isPlayer1Turn = true;
        this.usedTruthsId = new ArrayList<>();
        this.usedDaresId =new ArrayList<>();
        this.hasPlayerRolledTheDice = false;
        this.isPlayer1AdditionalActionNeeded = false;
        this.isPlayer2AdditionalActionNeeded = false;
        this.textPlaceholder1 = "";
        this.textPlaceholder2 = "";
        this.textPlaceholder3 = "";
        //when a new Game is created it should be saved in the database

    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public boolean getHasPlayerRolledTheDice() {
        return this.hasPlayerRolledTheDice;
    }

    public void setHasPlayerRolledTheDice(boolean hasPlayerRolledTheDice) {
        this.hasPlayerRolledTheDice = hasPlayerRolledTheDice;
    }

    public boolean getIsPlayer1AdditionalActionNeeded() {
        return this.isPlayer1AdditionalActionNeeded;
    }

    public void setIsPlayer1AdditionalActionNeeded(boolean isPlayer1AdditionalActionNeeded) {
        this.isPlayer1AdditionalActionNeeded = isPlayer1AdditionalActionNeeded;
    }

    public boolean getIsPlayer2AdditionalActionNeeded() {
        return this.isPlayer2AdditionalActionNeeded;
    }

    public void setIsPlayer2AdditionalActionNeeded(boolean isPlayer2AdditionalActionNeeded) {
        this.isPlayer2AdditionalActionNeeded = isPlayer2AdditionalActionNeeded;
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

    public int getPlayer1Position() {
        return player1Position;
    }

    public void setPlayer1Position(int player1Position) {
        this.player1Position = player1Position;
    }

    public int getPlayer2Position() {
        return player2Position;
    }

    public void setPlayer2Position(int player2Position) {
        this.player2Position = player2Position;
    }

    public List<Integer> getBoardFields() {
        return boardFields;
    }

    public void setBoardFields(List<Integer> boardFields) {
        this.boardFields = boardFields;
    }

    public boolean isPlayer1Turn() {
        return isPlayer1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        isPlayer1Turn = player1Turn;
    }

    public String getTextPlaceholder1() {
        return textPlaceholder1;
    }

    public void setTextPlaceholder1(String textPlaceholder1) {
        this.textPlaceholder1 = textPlaceholder1;
    }

    public String getTextPlaceholder2() {
        return textPlaceholder2;
    }

    public void setTextPlaceholder2(String textPlaceholder2) {
        this.textPlaceholder2 = textPlaceholder2;
    }

    public String getTextPlaceholder3() {
        return textPlaceholder3;
    }

    public void setTextPlaceholder3(String textPlaceholder3) {
        this.textPlaceholder3 = textPlaceholder3;
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
