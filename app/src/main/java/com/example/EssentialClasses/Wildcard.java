package com.example.EssentialClasses;

import com.example.R;

/**
 * Created by XPS on 11/13/2016.
 */

public class Wildcard extends Field {
    private String question;
    private String answer;

    public Wildcard(int id, int position) {
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int getType() {
        return 5;
    }

    @Override
    public int getDrawableId() {
        return R.drawable.wildcard;
    }
}
