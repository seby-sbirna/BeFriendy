package com.example.EssentialClasses;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by XPS on 11/3/2016.
 */

public class Game {
    private Context context;
    private String id;
    private Board board;
    private Player localPlayer;
    private DatabaseUser remotePlayer;
    private DatabaseReference mDatabase;
    private DatabaseGame databaseGame;

    private ArrayList<Integer> boardFieldInts;

    public Game(DatabaseUser remotePlayer, Context context) { //Was Player, but changed to DatabaseUser
        //TODO check if the 2 players don't have a game already
        //also remotePlayer != localPlayer

        this.context = context;
        this.localPlayer = Player.get();
        this.remotePlayer = remotePlayer;

        createBoard();
        initializeGameToDatabase();
    }

    public Game(DatabaseUser remotePlayer, String id, Context context) { //Was Player, but changed to DatabaseUser
        //TODO check if the 2 players don't have a game already
        //also remotePlayer != localPlayer

        this.context = context;
        this.localPlayer = Player.get();
        this.remotePlayer = remotePlayer;
        this.id = id;


        for (DatabaseGame currGame : Player.get().getGameList()) {
            if (this.id.equals(currGame.getGameId())) {
                databaseGame = currGame;
                break;
            }
        }
        createBoard();
    }

    private void initializeGameToDatabase() {
        this.id = UUID.randomUUID().toString();

        databaseGame = new DatabaseGame(id, Player.get().getUserId(), remotePlayer.getUserId(), board.getListFieldInts());
        databaseGame.setHasPlayerRolledTheDice(false);
        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
        databaseGame.setPlayer1Position(0);
        databaseGame.setPlayer1Turn(true);
        databaseGame.setPlayer2Position(0);
        databaseGame.setTextPlaceholder1(" ");
        databaseGame.setTextPlaceholder2(" ");
        databaseGame.setTextPlaceholder3(" ");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("games").child(id).setValue(databaseGame);

        //for each game save the game id to both players and the other player's id, so we know who plays with whom.
        mDatabase.child("users").child(localPlayer.getUserId()).child("userGames").child(id).setValue(remotePlayer.getUserId());
        mDatabase.child("users").child(remotePlayer.getUserId()).child("userGames").child(id).setValue(localPlayer.getUserId());
    }

//    private void initializeGameToDatabase() {
//        this.id = UUID.randomUUID().toString();
//
//        databaseGame = new DatabaseGame(id, Player.get().getUserId(), remotePlayer.getUserId(), board.getListFieldInts());
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("games").child(id).setValue(databaseGame);
//        //for each game save the game id to both players and the other player's id, so we know who plays with whom.
//        mDatabase.child("users").child(localPlayer.getUserId()).child("userGames").child(id).setValue(remotePlayer.getUserId());
//        mDatabase.child("users").child(remotePlayer.getUserId()).child("userGames").child(id).setValue(localPlayer.getUserId());
//
//        this.databaseGame.setPlayer1Position(0);
//        this.databaseGame.setPlayer2Position(0);
//    }

    public boolean isItMyTurn() {
        return (databaseGame.getPlayer1Id().equals(Player.get().getUserId()) && databaseGame.isPlayer1Turn() == true) ||
                (databaseGame.getPlayer2Id().equals(Player.get().getUserId()) && databaseGame.isPlayer1Turn() == false);
    }

    public boolean amIPlayer1() {
        return databaseGame.getPlayer1Id().equals(Player.get().getUserId());
    }

    public boolean amIRequestedAdditionalAction() {
        return (amIPlayer1() == true && databaseGame.getIsPlayer1AdditionalActionNeeded() == true) || (amIPlayer1() == false && databaseGame.getIsPlayer2AdditionalActionNeeded() == true);
    }

    public boolean isRemotePlayerRequestedAdditionalAction() {
        return (amIPlayer1() == true && databaseGame.getIsPlayer2AdditionalActionNeeded() == true) || (amIPlayer1() == false && databaseGame.getIsPlayer1AdditionalActionNeeded() == true);
    }

    public int retrieveToken(int id) {
        switch (id) {
            case 0:
                return context.getResources().getIdentifier("befriendylogo", "drawable", context.getPackageName());
        }
        return 0;
    }

    public Board getBoard() {
        return this.board;
    }

    private void createBoard() {
        //TODO CHANGE HERE
        this.board = new Board(databaseGame, context);
        board.addField(new Minigame(1)); //3
        board.addField(new Truth(2)); //1
        board.addField(new Dare(3));//2
        board.addField(new Minigame(4));//3
        board.addField(new Truth(5));//1
        board.addField(new Dare(6));//2
        board.addField(new Truth(7));//1
        board.addField(new Dare(8));//2
        board.addField(new Wildcard(9));//4
        board.addField(new Dare(10));//2
        board.addField(new Gameplay(11));//5
        board.addField(new Dare(12));//2
        board.addField(new Truth(13));//1
        board.addField(new Minigame(14));//3
        board.addField(new Dare(15));//2
        board.addField(new Gameplay(16));//5
        board.addField(new Wildcard(17));//4
        board.addField(new Truth(18));//1
        board.addField(new Dare(19));//2
        board.addField(new Truth(20));//1
        board.addField(new Wildcard(21));//4
        board.addField(new Minigame(22));//3
        board.addField(new Dare(23));//2
        board.addField(new Gameplay(24));//5
        board.addField(new Truth(25));//1
        board.addField(new Wildcard(26));//4
        board.addField(new Dare(27));//2
        board.addField(new Truth(28));//1
        board.addField(new Wildcard(29));//4

        //LEGEND:
        /*
        Truth: 1
        Dare: 2
        Minigame: 3
        Wildcard: 4
        Gameplay: 5
         */
        /*
        implemented a smarter way in the board class but keep it here just in case
        boardFieldInts = new ArrayList<>();
        boardFieldInts.add(3);
        boardFieldInts.add(1);
        boardFieldInts.add(2);
        boardFieldInts.add(3);
        boardFieldInts.add(1);
        boardFieldInts.add(2);
        boardFieldInts.add(1);
        boardFieldInts.add(2);
        boardFieldInts.add(4);
        boardFieldInts.add(2);
        boardFieldInts.add(5);
        boardFieldInts.add(2);
        boardFieldInts.add(1);
        boardFieldInts.add(3);
        boardFieldInts.add(2);
        boardFieldInts.add(5);
        boardFieldInts.add(4);
        boardFieldInts.add(1);
        boardFieldInts.add(2);
        boardFieldInts.add(1);
        boardFieldInts.add(4);
        boardFieldInts.add(3);
        boardFieldInts.add(2);
        boardFieldInts.add(5);
        boardFieldInts.add(1);
        boardFieldInts.add(4);
        boardFieldInts.add(2);
        boardFieldInts.add(1);
        boardFieldInts.add(4);
        */
    }

    public String getId() {
        return id;
    }

    public DatabaseGame getDatabaseGame() {
        return databaseGame;
    }
}
