package com.example.EssentialClasses;

import com.example.R;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by XPS on 11/4/2016.
 */

public class Truth extends Field {
    private String question;
    private String answer;

    private DatabaseReference mDatabase;

    public Truth(int position) {
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
    public void requestTruthQuestionFromDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Random rng = new Random();
        int randomTruthIdResult = rng.nextInt((int) Player.get().getTotalTruthCount()) + 1;

        mDatabase.child("Truth").child(String.valueOf(randomTruthIdResult)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                question = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void requestTruthQuestionFromDatabaseById(int truthId) {
        //gets a question with a specific id from the database.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Truth").child(String.valueOf(truthId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                question = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } */

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

    public int getType() {
        return 1;
    }

    public int getDrawableId() {
        return R.drawable.truth;
    }

}
