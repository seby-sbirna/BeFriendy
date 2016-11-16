package com.example.EssentialClasses;

import com.example.R;

/**
 * Created by XPS on 11/4/2016.
 */

public class Dare extends Field {
    private String dareText;
    private Photo darePhoto;
    private Video dareVideo;

    //private DatabaseReference mDatabase;


    public Dare(int id, int position) {
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

    private void requestDareFromDatabase() {
        /*
        //gets a question with a specific id from the database.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Dare").child(String.valueOf(dareId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dareText = String.valueOf(snapshot.getValue());

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        */
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

    public int getType() {
        return 2;
    }

    public int getDrawableId() {
        return R.drawable.dare;
    }
}
