package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TruthActivity extends AppCompatActivity {

    TextView truthQuestion;
    EditText truthAnswer;
    Button submitAnswer;
    Button declineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truth);
    }

    @Override
    protected void onStart() {
        super.onStart();

        truthQuestion = (TextView) findViewById(R.id.truth_question);
        truthAnswer = (EditText) findViewById(R.id.truth_answer);
        submitAnswer = (Button) findViewById(R.id.submit_truth);
        declineButton = (Button) findViewById(R.id.decline_truth_button);

        submitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String writtenAnswer = truthAnswer.getText().toString();
                if(writtenAnswer.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please type an answer before submitting!", Toast.LENGTH_LONG).show();
                } else {
                    // Code to submit answer
                    Intent i = new Intent(getApplicationContext(), BoardActivity.class);
                    startActivity(i);
                }
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(ii);
            }
        });
    }
}
