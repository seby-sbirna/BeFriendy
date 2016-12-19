package com.example;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EssentialClasses.DatabaseUser;
import com.example.EssentialClasses.Player;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "AUTH";
    EditText emailLogin;
    EditText passwordLogin;
    Button buttonLogin;
    Button buttonCreateAcc;
    Button buttonForgotPass;
    private DatabaseReference mDatabase;

    private final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                boolean userAlreadyAdded = Player.get().getUser(user.getUserId()) != null;
                if (!userAlreadyAdded) {
                    Player.get().getUsers().add(user);                 // add registered user in Reg.Users list
                }
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

                NetworkTesting networkTesting = new NetworkTesting();

                try {
                    if (networkTesting.execute().get() == true) {
                        //TODO: Internet connection check by pinging google!

                        //Fields Validation
                        if (typedEmail.isEmpty() || typedPassword.isEmpty()) {
                            //For testing purposes if the fieldImageViews are empty it would log you with test@test.com
                            typedEmail = "test@test.com";
                            typedPassword = "test";
                            SignInUser(typedEmail, typedPassword);
                            Toast.makeText(getApplicationContext(), "Logged in with test@test.com", Toast.LENGTH_SHORT).show();
                            //                    Toast.makeText(getApplicationContext(), "Please fill in both email and password!", Toast.LENGTH_LONG).show();

                        } else if (!isValidEmail(typedEmail)) {
                            Toast.makeText(getApplicationContext(), "The email is not valid!", Toast.LENGTH_SHORT).show();
                        }/* else if (typedPassword.length() < 6) {
                            Toast.makeText(getApplicationContext(), "This password is too short", Toast.LENGTH_LONG).show(); }*/ //so it is easier to develop
                        else if (typedPassword.length() > 19) {
                            Toast.makeText(getApplicationContext(), "This password is too long", Toast.LENGTH_SHORT).show();
                        } else {
                            //check if the user is registered or not
                            SignInUser(typedEmail, typedPassword);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Your device does not have an internet connection", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
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
                Player.get().setUsergameListIds(currUser.getUserGames());
//                Player.get().setPassword(currUser.getPassword());


                //START SERVICE FOR GAME INFO AND USER INFO
                startService(new Intent(getApplicationContext(), RetrieveInfoFromDatabase.class));

                userExists = true;
                //Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                Intent intent = new Intent(getApplicationContext(), GameListActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
        if (!userExists) {
            Toast.makeText(getApplicationContext(), "Email or password incorrect", Toast.LENGTH_SHORT).show();
        }
    }



    class NetworkTesting extends AsyncTask<String, Void, Boolean> {

        private Exception exception;

        protected Boolean doInBackground(String... xyz) {
            if (isNetworkAvailable()) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(1500);
                    urlc.connect();
                    return (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    Log.e("LoginActivity", "Error checking internet connection", e);
                }
            } else {
                Log.d("LoginActivity", "No network available!");
            }
            return false;
        }


        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }
    }



}
