package com.example.EssentialClasses;

import com.example.R;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by XPS on 11/4/2016.
 */

public class Dare extends Field {
    private String dareText;

    private DatabaseReference mDatabase;

    public Dare(){}

    public Dare(int position) {
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
/*
    public void requestDareFromDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Random rng = new Random();
        int randomDareIdResult = rng.nextInt((int) Player.get().getTotalDareCount()) + 1;

        mDatabase.child("Dare").child(String.valueOf(randomDareIdResult)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
//                Log.d("Dare", "the text from db is: " + String.valueOf(dataSnapshot.getValue()));
                dareText = String.valueOf(dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void requestDareFromDatabaseById(int dareId) {
        //gets a dare question with a specific id from the database.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Dare").child(String.valueOf(dareId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                dareText = String.valueOf(dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    */

    public String getDareText() {
        return dareText;
    }

    public void setDareText(String dareText) {
        this.dareText = dareText;
    }

    public int getType() {
        return 2;
    }

    public int getDrawableId() {
        return R.drawable.dare;
    }
}
