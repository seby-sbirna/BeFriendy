package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class RollDiceActivity extends Activity {
    TextView youRolledTextView;
    Button rollButton;
    Button continueButton;
    int rollResult;
    boolean hasPressedOnce = false;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_dice);

        if (savedInstanceState != null && savedInstanceState.containsKey("rollResult")) {
            rollResult = savedInstanceState.getInt("rollResult");
            hasPressedOnce = true;
        }

        youRolledTextView = (TextView) findViewById(R.id.you_rolled_text_view);
        rollButton = (Button) findViewById(R.id.button_roll);
        continueButton = (Button) findViewById(R.id.button_continue);

        youRolledTextView.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.INVISIBLE);

        rollButton.setBackground(this.getResources().getDrawable(R.drawable.roll_image));
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPressedOnce == false && rollResult == 0) {
                    youRolledTextView.setVisibility(View.VISIBLE);
                    Random rng = new Random();
                    switch (rollResult = (rng.nextInt(6) + 1)) {
                        case 1:
                            rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_1));
                            break;
                        case 2:
                            rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_2));
                            break;
                        case 3:
                            rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_3));
                            break;
                        case 4:
                            rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_4));
                            break;
                        case 5:
                            rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_5));
                            break;
                        case 6:
                            rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_6));
                            break;
                        default:
                            break;
                    }
                    continueButton.setVisibility(View.VISIBLE);
                    hasPressedOnce = true;
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RollDiceActivity.this, GameActivity.class);
                i.putExtra("rollResult", rollResult);
                i.putExtra("remotePlayerId", getIntent().getExtras().getString("remotePlayerId"));
                i.putExtra("gameId", getIntent().getExtras().getString("gameId"));
                startActivity(i);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (hasPressedOnce == true) {
            Intent i = new Intent(RollDiceActivity.this, GameActivity.class);
            i.putExtra("rollResult", rollResult);
            i.putExtra("remotePlayerId", getIntent().getExtras().getString("remotePlayerId"));
            i.putExtra("gameId", getIntent().getExtras().getString("gameId"));
            startActivity(i);

            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        if (rollResult != 0)
            savedInstanceState.putInt("rollResult", rollResult);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        if (rollResult != 0) {
            hasPressedOnce = true;
            rollResult = savedInstanceState.getInt("rollResult");
            youRolledTextView.setVisibility(View.VISIBLE);
            switch (rollResult) {
                case 1:
                    rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_1));
                    break;
                case 2:
                    rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_2));
                    break;
                case 3:
                    rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_3));
                    break;
                case 4:
                    rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_4));
                    break;
                case 5:
                    rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_5));
                    break;
                case 6:
                    rollButton.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.dice_6));
                    break;
                default:
                    break;
            }
            continueButton.setVisibility(View.VISIBLE);
        }
    }
}
