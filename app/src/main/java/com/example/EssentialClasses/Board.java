package com.example.EssentialClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XPS on 11/3/2016.
 */


// THIS IS ME :3
public class Board {
    private int localPlayerPosition;
    private int remotePlayerPosition;
    private PlayToken localPlayerToken;
    private PlayToken remotePlayerToken;
    private boolean yourTurn;

    private List<Field> fields =new ArrayList<>();
    private List<Integer> fieldsInt = new ArrayList<>();

    public Board() {
        localPlayerPosition = 0;
        remotePlayerPosition = 0;
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
        switch (field.getClass().getName()){
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

    public Field getFieldByPosition(int position) {
        for (Field field : fields) {
            if(field.getPosition() == position) return field;
        }
        throw new IllegalArgumentException("The position " + position + " is not defined." );
    }

    public int getBoardFieldType(int index){
        return fields.get(index).getType();
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


    public void moveOnBoard(Player player, int steps) {
        if (yourTurn) {
            //TODO execute animations
            setLocalPlayerPosition(localPlayerPosition + steps);
        } else if (!yourTurn) {
            //TODO execute animations
            setRemotePlayerPosition(remotePlayerPosition + steps);
        }
    }
}
