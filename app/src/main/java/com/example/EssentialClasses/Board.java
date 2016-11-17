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

    public Board() {
        localPlayerPosition = 0;
        remotePlayerPosition = 0;
    }

    public void addField(Field field) {
        fields.add(field);
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
