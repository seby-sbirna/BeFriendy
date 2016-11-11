package com.example.EssentialClasses;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by XPS on 11/4/2016.
 */

public class Truth extends Field {
    private String question;
    private String answer;

    private DatabaseReference mDatabase;

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

    private void requestTruthFromDatabase(int truthId) {
        //gets a question with a specific id from the database.
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Truth").child(String.valueOf(truthId)).child("question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                question = String.valueOf(snapshot.getValue());

            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
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
