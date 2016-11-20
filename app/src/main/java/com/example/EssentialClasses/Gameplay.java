package com.example.EssentialClasses;

import com.example.R;

/**
 * Created by XPS on 11/4/2016.
 */

public class Gameplay extends Field {
    private String magicText;

    public Gameplay(int id, int position) {
        super(id, position);
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

    //TODO CHANGE THIS NAMES! No magic
    public String getMagicText() {
        return magicText;
    }

    public void setMagicText(String magicText) {
        this.magicText = magicText;
    }

    public int getType() {
        return 3;
    }

    public int getDrawableId() {
        return R.drawable.gameplay;
    }
}
