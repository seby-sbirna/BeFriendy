package com.example.EssentialClasses;

/**
 * Created by XPS on 11/4/2016.
 */

public class Dare extends Field {
    private String dareText;
    private Photo darePhoto;
    private Video dareVideo;

    public Dare(int id, Game game) {
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

    private void requestDareFromDatabase() {
        //TODO
    }

    @Override
    public boolean checkForNewDataInBackground(int id) {
        //TODO
        return false; //DELETE
    }

    public String getDareText() {
        return dareText;
    }

    public void setDareText(String dareText) {
        this.dareText = dareText;
    }

    private void sendPhoto() {
        //TODO
    }

    private void sendVideo() {
        //TODO
    }

    private Photo receivePhoto(String[] data) {
        return darePhoto; //DELETE
    }

    private Video receiveVideo(String[] data) {
        return dareVideo; //VIDEO
    }
}
