package com.example.EssentialClasses;

import com.example.R;

/**
 * Created by XPS on 11/4/2016.
 */

public class Gameplay extends Field {
    private String gameplayText;

    public Gameplay(int position) {
        super(position);
    }

    @Override
    public void execute() {
        //TODO
    }

    @Override
    public void visualize(String[] data) {
        //TODO
    }

    //TODO CHANGE THIS NAMES! No magic
    public String getGameplayText() {
        return gameplayText;
    }

    public void setGameplayText(String gameplayText) {
        this.gameplayText = gameplayText;
    }

    public int getType() {
        return 5;
    }

    public int getDrawableId() {
        return R.drawable.gameplay;
    }
}
