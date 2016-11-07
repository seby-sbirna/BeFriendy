package com.example.EssentialClasses;

/**
 * Created by XPS on 11/4/2016.
 */

public class Magic extends Field {
    private String magicText;

    public Magic(int id, Game game) {
        super(id, game);
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public void visualize(String[] data) {
        //TODO
    }

    @Override
    public boolean checkForNewDataInBackground(int id) {
        //TODO
        return false; //DELETE
    }

    public String getMagicText() {
        return magicText;
    }

    public void setMagicText(String magicText) {
        this.magicText = magicText;
    }
}
