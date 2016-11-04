package com.example.frederikeriksen.befriendy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    EditText regUserName;
    EditText regEmail;
    EditText regPassword;
    EditText regRepeatPassword;
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

    }

    @Override
    protected void onStart() {
        super.onStart();

        regUserName = (EditText) findViewById(R.id.userNameReg);
        regEmail = (EditText) findViewById(R.id.emailReg);
        regPassword = (EditText) findViewById(R.id.passwordReg);
        regRepeatPassword = (EditText) findViewById(R.id.repPasswordReg);
        regButton = (Button) findViewById(R.id.regButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chosenUserName = regUserName.getText().toString();
                String chosenEmail = regEmail.getText().toString();
                String chosenPassword = regPassword.getText().toString();
                String repeatChosenPassword = regRepeatPassword.getText().toString();

                if(chosenUserName.isEmpty() || chosenEmail.isEmpty() || chosenPassword.isEmpty() || repeatChosenPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields!", Toast.LENGTH_LONG).show();
                } else if(repeatChosenPassword != chosenPassword) {
                    Toast.makeText(getApplicationContext(), "The passwords does not match!", Toast.LENGTH_LONG).show();
                }
                // more if statements to validate registration...
                else {
                    // Code to register user in Database goes here...
                }
            }
        });
    }

}
