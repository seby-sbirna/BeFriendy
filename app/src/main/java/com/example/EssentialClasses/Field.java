package com.example.EssentialClasses;

/**
 * Created by XPS on 11/4/2016.
 */

public abstract class Field {
    protected int databaseId;
    protected int position;

    public Field(){}

    public Field(int position) {
        this.position = position;
    }

    protected static void closeField() {
        //TODO Close the field
    }

    public int getPositionOnTheBoard() {
        return position;
    }

    public abstract void execute();

    public abstract void visualize(String[] data);

    public abstract int getType();

    public abstract int getDrawableId();

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }
}
