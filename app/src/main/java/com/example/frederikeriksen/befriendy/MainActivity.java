package com.example.frederikeriksen.befriendy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText emailLogin;
    EditText passwordLogin;
    Button buttonLogin;
    Button buttonCreateAcc;
    Button buttonForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        emailLogin = (EditText) findViewById(R.id.loginEmail);
        passwordLogin = (EditText) findViewById(R.id.loginPassword);
        buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonCreateAcc = (Button) findViewById(R.id.createAccountButton);
        buttonForgotPass = (Button) findViewById(R.id.forgotPasswordButton);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String typedEmail = emailLogin.getText().toString();
                String typedPassword = passwordLogin.getText().toString();

                if(typedEmail.isEmpty() || typedPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in both email and password to login!", Toast.LENGTH_LONG).show();
                    // Some more statements to validate login attempt...

                } else {
                    Intent i = new Intent(getApplicationContext(), EntryScreen.class);
                    startActivity(i);
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

}
