package com.example.EssentialClasses;

/**
 * Created by XPS on 11/4/2016.
 */

public abstract class Field {
    protected int id;
    protected Game game;

    public Field(int id, Game game) {
        this.id = id;
        this.game = game;
    }

    protected static void closeField() {
        //TODO Close the field
    }

    public abstract void execute();

    public abstract void visualize(String[] data);

    public abstract boolean checkForNewDataInBackground(int id);
}
