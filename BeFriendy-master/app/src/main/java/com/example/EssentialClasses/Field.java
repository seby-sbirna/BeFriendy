package com.example.EssentialClasses;

/**
 * Created by XPS on 11/4/2016.
 */

public abstract class Field {
    protected int id;
    protected int position;

    public Field(){}

    public Field(int id, int position) {
        this.id = id;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    protected static void closeField() {
        //TODO Close the field
    }

    public abstract void execute();

    public abstract void visualize(String[] data);

    public abstract boolean checkForNewDataInBackground(int id);

    public abstract int getType();

    public abstract int getDrawableId();
}
