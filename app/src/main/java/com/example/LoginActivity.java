package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EssentialClasses.DatabaseUser;
import com.example.EssentialClasses.Player;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText emailLogin;
    EditText passwordLogin;
    Button buttonLogin;
    Button buttonCreateAcc;
    Button buttonForgotPass;

    private DatabaseReference mDatabase;
    private static final String TAG = "AUTH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization of firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //field initialization
        emailLogin = (EditText) findViewById(R.id.loginEmail);
        passwordLogin = (EditText) findViewById(R.id.loginPassword);
        buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonCreateAcc = (Button) findViewById(R.id.createAccountButton);
        buttonForgotPass = (Button) findViewById(R.id.forgotPasswordButton);

        Player.get().getUsers().clear();
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

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String typedEmail = emailLogin.getText().toString();
                String typedPassword = passwordLogin.getText().toString();

                //TODO: Internet connection check by pinging google!

                //Fields Validation
                if(typedEmail.isEmpty() || typedPassword.isEmpty()) {
                    //For testing purposes if the fields are empty it would log you with test@test.com
                    typedEmail = "test@test.com";
                    typedPassword = "test";
                    SignInUser(typedEmail, typedPassword);
                    Toast.makeText(getApplicationContext(), "Loged in with test@test.com", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(), "Please fill in both email and password!", Toast.LENGTH_LONG).show();

                } else if(!isValidEmail(typedEmail)){
                    Toast.makeText(getApplicationContext(), "The email is not valid!", Toast.LENGTH_LONG).show();
                }/* else if (typedPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "This password is too short", Toast.LENGTH_LONG).show(); }*/ //so it is easier to develop
                 else if (typedPassword.length() > 19) {
                    Toast.makeText(getApplicationContext(), "This password is too long", Toast.LENGTH_LONG).show();
                } else {
                    //check if the user is registered or not
                    SignInUser(typedEmail, typedPassword);

                }

            }
        });

        buttonCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ii = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(ii);

            }
        });

    }

    private void SignInUser(String typedEmail, String typedPassword) {
        boolean userExists = false;
        for (DatabaseUser currUser : Player.get().getUsers()) {
            if (typedEmail.equals(currUser.getEmail()) && typedPassword.equals(currUser.getPassword())) {
                Player.get().setUserId(currUser.getUserId());         // User initialized from database user-data
                Player.get().setUserName(currUser.getUserName());
                Player.get().setEmailAddress(currUser.getEmail());
                //Player.get().setPassword(currUser.getPassword());

                userExists = true;
                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), GameListActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
        if (!userExists) {
            Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }



}
