package com.example.EssentialClasses;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by XPS on 11/3/2016.
 */

public class Game {
    private String id;
    private Board board;
    private Player localPlayer;
    private DatabaseUser remotePlayer;
    private boolean isTurnEnded;
    private DatabaseReference mDatabase;

    private ArrayList<Integer> boardFieldInts;

    public Game( DatabaseUser remotePlayer) { //Was Player, but changed to DatabaseUser
        //TODO check if the 2 players don't have a game already
        //also remotePlayer != localPlayer
        this.id = UUID.randomUUID().toString();
        this.localPlayer = Player.get();
        this.remotePlayer = remotePlayer;
        createBoard();

        DatabaseGame newGame = new DatabaseGame(id, Player.get().getUserId(), remotePlayer.getUserId(), board.getListFieldInts());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("games").child(id).setValue(newGame);
        //for each game save the game id to both players and the other player's id, so we know who plays with whom.
        mDatabase.child("users").child(localPlayer.getUserId()).child("userGames").child(id).setValue(remotePlayer.getUserId());
        mDatabase.child("users").child(remotePlayer.getUserId()).child("userGames").child(id).setValue(localPlayer.getUserId());

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
        board.addField(new Minigame(1, 1)); //3
        board.addField(new Truth(2, 2)); //1
        board.addField(new Dare(3, 3));//2
        board.addField(new Minigame(4, 4));//3
        board.addField(new Truth(5, 5));//1
        board.addField(new Dare(6, 6));//2
        board.addField(new Truth(7, 7));//1
        board.addField(new Dare(8, 8));//2
        board.addField(new Wildcard(9, 9));//4
        board.addField(new Dare(10, 10));//2
        board.addField(new Gameplay(11, 11));//5
        board.addField(new Dare(12, 12));//2
        board.addField(new Truth(13, 13));//1
        board.addField(new Minigame(14, 14));//3
        board.addField(new Dare(15, 15));//2
        board.addField(new Gameplay(16, 16));//5
        board.addField(new Wildcard(17, 17));//4
        board.addField(new Truth(18, 18));//1
        board.addField(new Dare(19, 19));//2
        board.addField(new Truth(20, 20));//1
        board.addField(new Wildcard(21, 21));//4
        board.addField(new Minigame(22, 22));//3
        board.addField(new Dare(23, 23));//2
        board.addField(new Gameplay(24, 24));//5
        board.addField(new Truth(25, 25));//1
        board.addField(new Wildcard(26, 26));//4
        board.addField(new Dare(27, 27));//2
        board.addField(new Truth(28, 28));//1
        board.addField(new Wildcard(29, 29));//4

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
}
