package com.example.EssentialClasses;

import com.example.R;

/**
 * Created by XPS on 11/4/2016.
 */

public class Minigame extends Field {
    private String minigameText;
    private String answer;

    public Minigame(int id, int position) {
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

    private void requestMinigameFromDatabase() {
        //TODO
    }

    @Override
    public boolean checkForNewDataInBackground(int id) {
        //TODO
        return false; //DELETE
    }

    public String getMinigameText() {
        return minigameText;
    }

    public void setMinigameText(String minigameText) {
        this.minigameText = minigameText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getType() {
        return 4;
    }

    public int getDrawableId() {
        return R.drawable.minigame;
    }
}
