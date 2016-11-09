package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EssentialClasses.DatabaseUser;
import com.example.EssentialClasses.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity {

    EditText regUserName;
    EditText regEmail;
    EditText regPassword;
    EditText regRepeatPassword;
    Button regButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;
    private DatabaseUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        regUserName = (EditText) findViewById(R.id.userNameReg);
        regEmail = (EditText) findViewById(R.id.emailReg);
        regPassword = (EditText) findViewById(R.id.passwordReg);
        regRepeatPassword = (EditText) findViewById(R.id.repPasswordReg);
        regButton = (Button) findViewById(R.id.regButton);

        Player.get().getUsers().clear(); //clears the list just in case

        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            // Retrieve all users as they are in the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                DatabaseUser user = snapshot.getValue(DatabaseUser.class); //this is how you parse information from firebase

                Player.get().getUsers().add(user);                 // add registered user in Reg.Users list
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chosenUserName = regUserName.getText().toString();
                String chosenEmail = regEmail.getText().toString();
                String chosenPassword = regPassword.getText().toString();
                String repeatChosenPassword = regRepeatPassword.getText().toString();

                if(chosenUserName.isEmpty() || chosenEmail.isEmpty() || chosenPassword.isEmpty() || repeatChosenPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields!", Toast.LENGTH_LONG).show();
                } else if(! chosenPassword.equals(repeatChosenPassword)) {
                    Toast.makeText(getApplicationContext(), "The passwords does not match!", Toast.LENGTH_LONG).show();
                }
                // more if statements to validate registration...
                else {

                    // Code to register user in Database goes here...
                    boolean userExists = false;
                    for (DatabaseUser currUser : Player.get().getUsers()) {
                        if (chosenEmail.equals(currUser.getEmail())) {
                            Toast.makeText(getApplicationContext(), "This email already exists", Toast.LENGTH_LONG).show();
                            userExists = true;
                            break;
                        }
                    }
                    if(!userExists){
                        newUser = new DatabaseUser();
                        newUser.setEmail(chosenEmail);
                        newUser.setUserName(chosenUserName);
                        newUser.setPassword(chosenPassword);
                        newUser.setUserId(UUID.randomUUID().toString());
                        mDatabase.child("users").child(newUser.getUserId()).setValue(newUser);

                        Player.get().setUserName(newUser.getUserName());
                        Player.get().setEmailAddress(newUser.getEmail());
                        Player.get().setUserId(newUser.getUserId());

                        //Log.d("AUTH", Player.get().getUsers().contains());
                        Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), GameListActivity.class);
                        startActivity(intent);
                        finish(); //so the user can't go back;
                    }

                }


            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
