package com.example.EssentialClasses;

import com.example.R;

/**
 * Created by XPS on 11/4/2016.
 */

public class Minigame extends Field {
    private String minigameText;
    private String answer;

    public Minigame(int position) {
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

    private void requestMinigameFromDatabase() {
        //TODO
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
        return 3;
    }

    public int getDrawableId() {
        return R.drawable.minigame;
    }
}
