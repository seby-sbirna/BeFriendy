package com.example.EssentialClasses;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XPS on 11/3/2016.
 */


// THIS IS ME :3
public class Board {
    private DatabaseGame databaseGame;
    private Context context;

    private List<Field> fields =new ArrayList<>();
    private List<Integer> fieldsInt = new ArrayList<>();

    public Board(DatabaseGame databaseGame, Context context) {
        this.databaseGame = databaseGame;
        this.context = context;
    }

    public void addField(Field field) {
        fields.add(field);

        //LEGEND:
        /*
          Truth: 1
          Dare: 2
          Minigame: 3
          Wildcard: 4
          Gameplay: 5
         */
        String fieldName = field.getClass().getName();
        switch (fieldName) {
            case "com.example.EssentialClasses.Truth":
                fieldsInt.add(1);
                break;
            case "com.example.EssentialClasses.Dare":
                fieldsInt.add(2);
                break;
            case "com.example.EssentialClasses.Gameplay":
                fieldsInt.add(5);
                break;
            case "com.example.EssentialClasses.Minigame":
                fieldsInt.add(3);
                break;
            case "com.example.EssentialClasses.Wildcard":
                fieldsInt.add(4);
                break;
            default:
                //unknown field
                fieldsInt.add(-1);
                break;
        }

    }

    public List<Integer> getListFieldInts(){
        return fieldsInt;
    }

    public Field getFieldByPositionOnTheBoard(int positionOnTheBoard) {
        for (Field field : fields) {
            if (field.getPositionOnTheBoard() == positionOnTheBoard) return field;
        }
        throw new IllegalArgumentException("The positionOnTheBoard " + positionOnTheBoard + " is not defined.");
    }

    public int getBoardFieldTypeByPositionOnTheBoard(int positionOnTheBoard) {
        if (positionOnTheBoard >= 1)
            return fields.get(positionOnTheBoard - 1).getType();
        return -1;
    }
}
