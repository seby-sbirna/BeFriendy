package com.example.EssentialClasses;

/**
 * Created by XPS on 11/4/2016.
 */

public class Truth extends Field {
    private String question;
    private String answer;

    public Truth(int id, Game game) {
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

    private void requestTruthFromDatabase() {

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
}
