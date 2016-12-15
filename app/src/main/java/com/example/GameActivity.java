package com.example;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.EssentialClasses.DatabaseUser;
import com.example.EssentialClasses.Game;
import com.example.EssentialClasses.Player;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "Service";
    @BindViews({R.id.field_1, R.id.field_2, R.id.field_3, R.id.field_4,
            R.id.field_5, R.id.field_6, R.id.field_7, R.id.field_8, R.id.field_9,
            R.id.field_10, R.id.field_11, R.id.field_12, R.id.field_13, R.id.field_14,
            R.id.field_15, R.id.field_16, R.id.field_17, R.id.field_18, R.id.field_19,
            R.id.field_20, R.id.field_21, R.id.field_22, R.id.field_23, R.id.field_24,
            R.id.field_25, R.id.field_26, R.id.field_27, R.id.field_28, R.id.field_29})
    protected List<ImageView> fieldImageViews;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://befriendy-e8f50.appspot.com");
    private Game game;
    private int rollResult;
    private DatabaseUser remotePlayer;
    private int player1TokenId;
    private int player2TokenId;

    public static byte[] convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout_big);
        findViewById(R.id.snake_view_place).setBackgroundResource(0);
        findViewById(R.id.snake_view_place).setBackgroundResource(R.drawable.line);
        ButterKnife.bind(this);

        if (Player.get().isDormantTruthQuestionInvalidated())
            Player.get().retrieveNewDormantTruthQuestion();
        if (Player.get().isDormantDareQuestionInvalidated())
            Player.get().retrieveNewDormantDareQuestion();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            if (extras.containsKey("remotePlayerId")) {
                this.remotePlayer = Player.get().getUser(extras.getString("remotePlayerId"));
                this.game = new Game(this.remotePlayer, extras.getString("gameId"), this);
            }
            if (extras.containsKey("player1TokenId")) {
                this.player1TokenId = extras.getInt("player1TokenId");
            }
            if (extras.containsKey("player2TokenId")) {
                this.player2TokenId = extras.getInt("player2TokenId");
            }
            reconstructGame(extras);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);

        // Save the user's current game state
        if (game.amIPlayer1())
            savedInstanceState.putString("remotePlayerId", game.getDatabaseGame().getPlayer2Id());
        else
            savedInstanceState.putString("remotePlayerId", game.getDatabaseGame().getPlayer1Id());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        this.remotePlayer = Player.get().getUser(savedInstanceState.getString("remotePlayerId"));
    }

    private void initializeGame() {
        updateBoard(Player.get().getUserId(), 0);
        updateBoard(game.getDatabaseGame().getPlayer2Id(), 0);

        populateFieldViews();
        play();
    }

    private void reconstructGame(Bundle extras) {
        populateFieldViews();
        if (game.getDatabaseGame().getPlayer1Position() < 30 && game.getDatabaseGame().getPlayer2Position() < 30) {
            updateBoard(game.getDatabaseGame().getPlayer1Id(), game.getDatabaseGame().getPlayer1Position());
            updateBoard(game.getDatabaseGame().getPlayer2Id(), game.getDatabaseGame().getPlayer2Position());
//            updateBoard(Player.get().getUserId(), game.getDatabaseGame().getPlayer1Position(), game.getDatabaseGame().getPlayer1Position());
//            updateBoard(game.getDatabaseGame().getPlayer2Id(), game.getDatabaseGame().getPlayer2Position(), game.getDatabaseGame().getPlayer2Position());
        } else {
            if (game.getDatabaseGame().getPlayer1Position() >= 30) {
                updateBoard(game.getDatabaseGame().getPlayer1Id(), 30);
            } else {
                updateBoard(game.getDatabaseGame().getPlayer1Id(), game.getDatabaseGame().getPlayer1Position());
            }

            if (game.getDatabaseGame().getPlayer2Position() >= 30) {
                updateBoard(game.getDatabaseGame().getPlayer2Id(), 30);
            } else {
                updateBoard(game.getDatabaseGame().getPlayer2Id(), game.getDatabaseGame().getPlayer2Position());
            }
        }


        if (extras.containsKey("rollResult")) {
            rollResult = extras.getInt("rollResult");
            game.getDatabaseGame().setHasPlayerRolledTheDice(true);
            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("hasPlayerRolledTheDice").setValue(game.getDatabaseGame().getHasPlayerRolledTheDice());

            if (game.amIPlayer1() == true) {
                moveMyselfOnTheBoard("player1", rollResult);
            } else {
                moveMyselfOnTheBoard("player2", rollResult);
            }
        }
        play();
    }

    private void populateFieldViews() {
        int positionOnTheBoard = 1;
        for (ImageView fieldImageView : fieldImageViews) {
            fieldImageView.setImageResource(game.getBoard().getFieldByPositionOnTheBoard(positionOnTheBoard).getDrawableId());
            ++positionOnTheBoard;
        }
    }

    public void play() {
        if (game.getDatabaseGame().getPlayer1Position() >= 0) {
            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("player1Position").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (game.getDatabaseGame().getPlayer1Position() < 30)
                        updateBoard(game.getDatabaseGame().getPlayer1Id(), game.getDatabaseGame().getPlayer1Position());
                    else
                        updateBoard(game.getDatabaseGame().getPlayer1Id(), 30);
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

        if (game.getDatabaseGame().getPlayer2Position() >= 0) {
            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("player2Position").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (game.getDatabaseGame().getPlayer2Position() < 30)
                        updateBoard(game.getDatabaseGame().getPlayer2Id(), game.getDatabaseGame().getPlayer2Position());
                    else
                        updateBoard(game.getDatabaseGame().getPlayer2Id(), 30);
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

        if ((game.getDatabaseGame().getPlayer1Position() < 30 && game.getDatabaseGame().getPlayer2Position() < 30)) {
            if (game.isItMyTurn() == true) {
                if (game.getDatabaseGame().getHasPlayerRolledTheDice() == false) {
                    Intent i = new Intent(GameActivity.this, RollDiceActivity.class);
                    if (game.amIPlayer1())
                        i.putExtra("remotePlayerId", game.getDatabaseGame().getPlayer2Id());
                    else
                        i.putExtra("remotePlayerId", game.getDatabaseGame().getPlayer1Id());
                    i.putExtra("gameId", game.getDatabaseGame().getGameId());
                    startActivity(i);
                    finish();
                } else if (game.getDatabaseGame().getHasPlayerRolledTheDice() == true) {
                    if (game.amIPlayer1() == true) {
                        if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0)
                            fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setBackground(getResources().getDrawable(R.drawable.square_color));
                        switch (game.getBoard().getBoardFieldTypeByPositionOnTheBoard(game.getDatabaseGame().getPlayer1Position())) {
                            case 1:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {

                                    if (game.getDatabaseGame().getTextPlaceholder1() == null || game.getDatabaseGame().getTextPlaceholder1().equals(" ")) {
                                        game.getDatabaseGame().setTextPlaceholder1(Player.get().getDormantTruthQuestion());
                                        mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder1").setValue(game.getDatabaseGame().getTextPlaceholder1());

                                        Player.get().invalidateDormantTruthQuestion();
                                        if (Player.get().isDormantTruthQuestionInvalidated())
                                            Player.get().retrieveNewDormantTruthQuestion();
                                    }

                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final TruthDialogFragment truthDialogFragment = TruthDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            truthDialogFragment.show(getSupportFragmentManager(), "Truth Fragment");
                                        }
                                    });
                                }
                                break;
                            case 2:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {

                                    if (game.getDatabaseGame().getTextPlaceholder1() == null || game.getDatabaseGame().getTextPlaceholder1().equals(" ")) {
                                        game.getDatabaseGame().setTextPlaceholder1(Player.get().getDormantDareQuestion());
                                        mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder1").setValue(game.getDatabaseGame().getTextPlaceholder1());

                                        Player.get().invalidateDormantDareQuestion();
                                        if (Player.get().isDormantDareQuestionInvalidated())
                                            Player.get().retrieveNewDormantDareQuestion();
                                    }

                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final DareDialogFragment dareDialogFragment = DareDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            dareDialogFragment.show(getSupportFragmentManager(), "Dare Fragment");
                                        }
                                    });
                                }
                                break;
                            case 3:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final MinigameRockPaperScissorsDialogFragment minigameRockPaperScissorsDialogFragment = MinigameRockPaperScissorsDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            minigameRockPaperScissorsDialogFragment.show(getSupportFragmentManager(), "Minigame Rock-Paper-Scissors Fragment");
                                        }
                                    });
                                }
                                break;
                            case 4:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final WildcardDialogFragment wildcardDialogFragment = WildcardDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            wildcardDialogFragment.show(getSupportFragmentManager(), "Wildcard Fragment");
                                        }
                                    });
                                }
                                break;
                            case 5:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final GameplayDialogFragment gameplayDialogFragment = GameplayDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            gameplayDialogFragment.show(getSupportFragmentManager(), "Gameplay Fragment");
                                        }
                                    });
                                }
                                break;
                            default:
                                break;
                        }
                    } else if (game.amIPlayer1() == false) {
                        if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0)
                            fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setBackground(getResources().getDrawable(R.drawable.square_color));
                        switch (game.getBoard().getBoardFieldTypeByPositionOnTheBoard(game.getDatabaseGame().getPlayer2Position())) {
                            case 1:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {

                                    if (game.getDatabaseGame().getTextPlaceholder1() == null || game.getDatabaseGame().getTextPlaceholder1().equals(" ")) {
                                        game.getDatabaseGame().setTextPlaceholder1(Player.get().getDormantTruthQuestion());
                                        mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder1").setValue(game.getDatabaseGame().getTextPlaceholder1());

                                        Player.get().invalidateDormantTruthQuestion();
                                        if (Player.get().isDormantTruthQuestionInvalidated())
                                            Player.get().retrieveNewDormantTruthQuestion();
                                    }

                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final TruthDialogFragment truthDialogFragment = TruthDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            truthDialogFragment.show(getSupportFragmentManager(), "Truth Fragment");
                                        }
                                    });
                                }
                                break;
                            case 2:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {

                                    if (game.getDatabaseGame().getTextPlaceholder1() == null || game.getDatabaseGame().getTextPlaceholder1().equals(" ")) {
                                        game.getDatabaseGame().setTextPlaceholder1(Player.get().getDormantDareQuestion());
                                        mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder1").setValue(game.getDatabaseGame().getTextPlaceholder1());

                                        Player.get().invalidateDormantDareQuestion();
                                        if (Player.get().isDormantDareQuestionInvalidated())
                                            Player.get().retrieveNewDormantDareQuestion();
                                    }

                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final DareDialogFragment dareDialogFragment = DareDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            dareDialogFragment.show(getSupportFragmentManager(), "Dare Fragment");
                                        }
                                    });
                                }
                                break;
                            case 3:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final MinigameRockPaperScissorsDialogFragment minigameRockPaperScissorsDialogFragment = MinigameRockPaperScissorsDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            minigameRockPaperScissorsDialogFragment.show(getSupportFragmentManager(), "Minigame Rock-Paper-Scissors Fragment");
                                        }
                                    });
                                }
                                break;
                            case 4:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final WildcardDialogFragment wildcardDialogFragment = WildcardDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            wildcardDialogFragment.show(getSupportFragmentManager(), "Wildcard Fragment");
                                        }
                                    });
                                }
                                break;
                            case 5:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final GameplayDialogFragment gameplayDialogFragment = GameplayDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            gameplayDialogFragment.show(getSupportFragmentManager(), "Gameplay Fragment");
                                        }
                                    });
                                }
                                break;
                        }

                    }
                }
            } else if (game.isItMyTurn() == false) {
                if (game.getDatabaseGame().getHasPlayerRolledTheDice() == false) {
                    if (game.amIPlayer1() == true) {
                        if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0)
                            fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setBackgroundResource(0);
                    } else if (game.amIPlayer1() == false) {
                        if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0)
                            fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setBackgroundResource(0);
                    }
                } else if (game.getDatabaseGame().getHasPlayerRolledTheDice() == true) {
                    if (game.amIPlayer1() == true) {
                        if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0)
                            fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setBackground(getResources().getDrawable(R.drawable.square_color));
                        switch (game.getBoard().getBoardFieldTypeByPositionOnTheBoard(game.getDatabaseGame().getPlayer2Position())) {
                            case 1:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final TruthDialogFragment truthDialogFragment = TruthDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            truthDialogFragment.show(getSupportFragmentManager(), "Truth Fragment");
                                        }
                                    });
                                }
                                break;
                            case 2:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final DareDialogFragment dareDialogFragment = DareDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            dareDialogFragment.show(getSupportFragmentManager(), "Dare Fragment");
                                        }
                                    });
                                }
                                break;
                            case 3:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final MinigameRockPaperScissorsDialogFragment minigameRockPaperScissorsDialogFragment = MinigameRockPaperScissorsDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            minigameRockPaperScissorsDialogFragment.show(getSupportFragmentManager(), "Minigame Rock-Paper-Scissors Fragment");
                                        }
                                    });
                                }
                                break;
                            case 4:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final WildcardDialogFragment wildcardDialogFragment = WildcardDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            wildcardDialogFragment.show(getSupportFragmentManager(), "Wildcard Fragment");
                                        }
                                    });
                                }
                                break;
                            case 5:
                                if (game.getDatabaseGame().getPlayer2Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer2Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final GameplayDialogFragment gameplayDialogFragment = GameplayDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            gameplayDialogFragment.show(getSupportFragmentManager(), "Gameplay Fragment");
                                        }
                                    });
                                }
                                break;
                        }
                    } else if (game.amIPlayer1() == false) {
                        if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0)
                            fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setBackground(getResources().getDrawable(R.drawable.square_color));
                        switch (game.getBoard().getBoardFieldTypeByPositionOnTheBoard(game.getDatabaseGame().getPlayer1Position())) {
                            case 1:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final TruthDialogFragment truthDialogFragment = TruthDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            truthDialogFragment.show(getSupportFragmentManager(), "Truth Fragment");
                                        }
                                    });
                                }
                                break;
                            case 2:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final DareDialogFragment dareDialogFragment = DareDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            dareDialogFragment.show(getSupportFragmentManager(), "Dare Fragment");
                                        }
                                    });
                                }
                                break;
                            case 3:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final MinigameRockPaperScissorsDialogFragment minigameRockPaperScissorsDialogFragment = MinigameRockPaperScissorsDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            minigameRockPaperScissorsDialogFragment.show(getSupportFragmentManager(), "Minigame Rock-Paper-Scissors Fragment");
                                        }
                                    });
                                }
                                break;
                            case 4:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final WildcardDialogFragment wildcardDialogFragment = WildcardDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            wildcardDialogFragment.show(getSupportFragmentManager(), "Wildcard Fragment");
                                        }
                                    });
                                }
                                break;
                            case 5:
                                if (game.getDatabaseGame().getPlayer1Position() - 1 >= 0) {
                                    fieldImageViews.get(game.getDatabaseGame().getPlayer1Position() - 1).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final GameplayDialogFragment gameplayDialogFragment = GameplayDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                                            gameplayDialogFragment.show(getSupportFragmentManager(), "Gameplay Fragment");
                                        }
                                    });
                                }
                                break;
                        }

                    }
                }
            }
        } else {
            ImageView fieldFinishImageView = (ImageView) findViewById(R.id.field_finish);
            fieldFinishImageView.setBackground(getResources().getDrawable(R.drawable.square_color));
            if (game.getDatabaseGame().getPlayer1Position() >= 30) {
                fieldFinishImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final GameWinLoseDialogFragment gameWinLoseDialogFragment = GameWinLoseDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer1Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                        gameWinLoseDialogFragment.show(getSupportFragmentManager(), "GameWinLose Fragment");
                    }
                });
            }
            if (game.getDatabaseGame().getPlayer2Position() >= 30) {
                fieldFinishImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final GameWinLoseDialogFragment gameWinLoseDialogFragment = GameWinLoseDialogFragment.newInstance(game.getId(), game.getDatabaseGame().getPlayer2Position(), game.isItMyTurn(), game.amIPlayer1(), game.amIRequestedAdditionalAction(), game.isRemotePlayerRequestedAdditionalAction());
                        gameWinLoseDialogFragment.show(getSupportFragmentManager(), "GameWinLose Fragment");
                    }
                });
            }
        }
    }

    public void moveMyselfOnTheBoard(String whichPlayerAmI, int steps) {
        if (whichPlayerAmI.equals("player1")) {
            game.getDatabaseGame().setPlayer1Position(game.getDatabaseGame().getPlayer1Position() + steps);
            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("player1Position").setValue(game.getDatabaseGame().getPlayer1Position());

            if (game.getDatabaseGame().getPlayer1Position() >= 1 && game.getDatabaseGame().getPlayer1Position() <= 29) {
                updateBoard(Player.get().getUserId(), game.getDatabaseGame().getPlayer1Position());
            }
        } else if (whichPlayerAmI.equals("player2")) {
            game.getDatabaseGame().setPlayer2Position(game.getDatabaseGame().getPlayer2Position() + steps);
            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("player2Position").setValue(game.getDatabaseGame().getPlayer2Position());

            if (game.getDatabaseGame().getPlayer2Position() >= 1 && game.getDatabaseGame().getPlayer2Position() <= 29) {
                updateBoard(Player.get().getUserId(), game.getDatabaseGame().getPlayer2Position());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && getImagePath(data.getData()) != null) {
            String selectedImagePath = getImagePath(data.getData());
            Log.d("selectedImagePath", "The selectedImagePath is: " + selectedImagePath);

            File selectedImage = new File(selectedImagePath);

            if (selectedImage != null && selectedImage.exists()) {
                final File pictureDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                String pictureName = "imagePlaceholder1.jpg";

                final File imageFile = new File(pictureDirectory, pictureName);
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(imageFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream.write(convertFileToByteArray(selectedImage));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (imageFile != null && imageFile.exists()) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading image...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(false);


                    String fileNameInStorage = "imagePlaceholder1.jpg";
                    final StorageReference storageReferenceImage = mStorageRef.child(game.getDatabaseGame().getGameId()).child(fileNameInStorage);
                    final UploadTask uploadTask = storageReferenceImage.putFile(Uri.fromFile(imageFile));

                    progressDialog.show();
                    progressDialog.setProgress(0);

                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setProgress((int) progress);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();

                            if (game.getDatabaseGame().getIsPlayer1AdditionalActionNeeded() == false && game.getDatabaseGame().getIsPlayer2AdditionalActionNeeded() == false) {
                                game.getDatabaseGame().setTextPlaceholder2(downloadUrl.getPath());
                                mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder2").setValue(game.getDatabaseGame().getTextPlaceholder2());
                                imageFile.delete();
                            } else {
                                game.getDatabaseGame().setTextPlaceholder3(downloadUrl.getPath());
                                mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder3").setValue(game.getDatabaseGame().getTextPlaceholder3());
                                imageFile.delete();
                            }

                            if (game.amIPlayer1() == true) {
                                game.getDatabaseGame().setIsPlayer2AdditionalActionNeeded(true);
                                mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer2AdditionalActionNeeded());

                                game.getDatabaseGame().setIsPlayer1AdditionalActionNeeded(false);
                                mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer1AdditionalActionNeeded());
                            } else {
                                game.getDatabaseGame().setIsPlayer1AdditionalActionNeeded(true);
                                mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer1AdditionalActionNeeded());

                                game.getDatabaseGame().setIsPlayer2AdditionalActionNeeded(false);
                                mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer2AdditionalActionNeeded());
                            }
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("UploadTaskFailed", e.getMessage());
                            finish();
                        }
                    });
                } else {
                    finish();
                }
            }
        } else {
            final File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "imagePlaceholder1.jpg");
            if (imageFile != null && imageFile.exists()) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading image...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);


                String fileNameInStorage = "imagePlaceholder1.jpg";
                final StorageReference storageReferenceImage = mStorageRef.child(game.getDatabaseGame().getGameId()).child(fileNameInStorage);
                final UploadTask uploadTask = storageReferenceImage.putFile(Uri.fromFile(imageFile));

                progressDialog.show();
                progressDialog.setProgress(0);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setProgress((int) progress);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();

                        if (game.getDatabaseGame().getIsPlayer1AdditionalActionNeeded() == false && game.getDatabaseGame().getIsPlayer2AdditionalActionNeeded() == false) {
                            game.getDatabaseGame().setTextPlaceholder2(downloadUrl.getPath());
                            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder2").setValue(game.getDatabaseGame().getTextPlaceholder2());
                            imageFile.delete();
                        } else {
                            game.getDatabaseGame().setTextPlaceholder3(downloadUrl.getPath());
                            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("textPlaceholder3").setValue(game.getDatabaseGame().getTextPlaceholder3());
                            imageFile.delete();
                        }

                        if (game.amIPlayer1() == true) {
                            game.getDatabaseGame().setIsPlayer2AdditionalActionNeeded(true);
                            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer2AdditionalActionNeeded());

                            game.getDatabaseGame().setIsPlayer1AdditionalActionNeeded(false);
                            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer1AdditionalActionNeeded());
                        } else {
                            game.getDatabaseGame().setIsPlayer1AdditionalActionNeeded(true);
                            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer1AdditionalActionNeeded());

                            game.getDatabaseGame().setIsPlayer2AdditionalActionNeeded(false);
                            mDatabase.child("games").child(game.getDatabaseGame().getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(game.getDatabaseGame().getIsPlayer2AdditionalActionNeeded());
                        }
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UploadTaskFailed", e.getMessage());
                        finish();
                    }
                });
            } else {
                finish();
            }
        }
    }

    /*
    public void updateBoard(String playerId, int oldPosition, int newPosition) {
        if (playerId.equals(Player.get().getUserId())) {
            int oldPositionId = getViewId(oldPosition, true);
            int newPositionId = getViewId(newPosition, true);

            ImageView oldImageView = (ImageView) findViewById(oldPositionId);
            oldImageView.setVisibility(View.INVISIBLE);

            ImageView newImageView = (ImageView) findViewById(newPositionId);
            newImageView.setImageDrawable(getResources().getDrawable(game.retrieveToken(Player.get().getTokenDrawableId())));
            newImageView.setVisibility(View.VISIBLE);
        } else {
            int oldPositionId = getViewId(oldPosition, false);
            int newPositionId = getViewId(newPosition, false);

            ImageView oldImageView = (ImageView) findViewById(oldPositionId);
            oldImageView.setVisibility(View.INVISIBLE);

            ImageView newImageView = (ImageView) findViewById(newPositionId);
            newImageView.setImageDrawable(getResources().getDrawable(game.retrieveToken(Player.get().getUser(playerId).getTokenId())));
            newImageView.setVisibility(View.VISIBLE);
        }
    }
    */

    public void updateBoard(String playerId, int position) {
        if (playerId.equals(Player.get().getUserId())) {
            int positionId = getViewId(position, true);

            for (int i = 0; i <= 30; i++) {
                findViewById(getViewId(i, true)).setVisibility(View.INVISIBLE);
            }

            ImageView newImageView = (ImageView) findViewById(positionId);
            if (game.amIPlayer1() == true)
                newImageView.setImageDrawable(getResources().getDrawable(game.retrieveToken(player1TokenId)));
            else
                newImageView.setImageDrawable(getResources().getDrawable(game.retrieveToken(player2TokenId)));
            newImageView.setVisibility(View.VISIBLE);
        } else {
            int positionId = getViewId(position, false);

            for (int i = 0; i <= 30; i++) {
                findViewById(getViewId(i, false)).setVisibility(View.INVISIBLE);
            }

            ImageView newImageView = (ImageView) findViewById(positionId);
            if (game.amIPlayer1() == true)
                newImageView.setImageDrawable(getResources().getDrawable(game.retrieveToken(player1TokenId)));
            else
                newImageView.setImageDrawable(getResources().getDrawable(game.retrieveToken(player2TokenId)));
            newImageView.setVisibility(View.VISIBLE);
        }
    }

    private int getViewId(int position, boolean isPlayerTop) {
        if (isPlayerTop == true) {
            switch (position) {
                case 0:
                    return R.id.field_start_player1;
                case 1:
                    return R.id.field_1_player1;
                case 2:
                    return R.id.field_2_player1;
                case 3:
                    return R.id.field_3_player1;
                case 4:
                    return R.id.field_4_player1;
                case 5:
                    return R.id.field_5_player1;
                case 6:
                    return R.id.field_6_player1;
                case 7:
                    return R.id.field_7_player1;
                case 8:
                    return R.id.field_8_player1;
                case 9:
                    return R.id.field_9_player1;
                case 10:
                    return R.id.field_10_player1;
                case 11:
                    return R.id.field_11_player1;
                case 12:
                    return R.id.field_12_player1;
                case 13:
                    return R.id.field_13_player1;
                case 14:
                    return R.id.field_14_player1;
                case 15:
                    return R.id.field_15_player1;
                case 16:
                    return R.id.field_16_player1;
                case 17:
                    return R.id.field_17_player1;
                case 18:
                    return R.id.field_18_player1;
                case 19:
                    return R.id.field_19_player1;
                case 20:
                    return R.id.field_20_player1;
                case 21:
                    return R.id.field_21_player1;
                case 22:
                    return R.id.field_22_player1;
                case 23:
                    return R.id.field_23_player1;
                case 24:
                    return R.id.field_24_player1;
                case 25:
                    return R.id.field_25_player1;
                case 26:
                    return R.id.field_26_player1;
                case 27:
                    return R.id.field_27_player1;
                case 28:
                    return R.id.field_28_player1;
                case 29:
                    return R.id.field_29_player1;
                case 30:
                    return R.id.field_finish_player1;
            }
        } else {
            switch (position) {
                case 0:
                    return R.id.field_start_player2;
                case 1:
                    return R.id.field_1_player2;
                case 2:
                    return R.id.field_2_player2;
                case 3:
                    return R.id.field_3_player2;
                case 4:
                    return R.id.field_4_player2;
                case 5:
                    return R.id.field_5_player2;
                case 6:
                    return R.id.field_6_player2;
                case 7:
                    return R.id.field_7_player2;
                case 8:
                    return R.id.field_8_player2;
                case 9:
                    return R.id.field_9_player2;
                case 10:
                    return R.id.field_10_player2;
                case 11:
                    return R.id.field_11_player2;
                case 12:
                    return R.id.field_12_player2;
                case 13:
                    return R.id.field_13_player2;
                case 14:
                    return R.id.field_14_player2;
                case 15:
                    return R.id.field_15_player2;
                case 16:
                    return R.id.field_16_player2;
                case 17:
                    return R.id.field_17_player2;
                case 18:
                    return R.id.field_18_player2;
                case 19:
                    return R.id.field_19_player2;
                case 20:
                    return R.id.field_20_player2;
                case 21:
                    return R.id.field_21_player2;
                case 22:
                    return R.id.field_22_player2;
                case 23:
                    return R.id.field_23_player2;
                case 24:
                    return R.id.field_24_player2;
                case 25:
                    return R.id.field_25_player2;
                case 26:
                    return R.id.field_26_player2;
                case 27:
                    return R.id.field_27_player2;
                case 28:
                    return R.id.field_28_player2;
                case 29:
                    return R.id.field_29_player2;
                case 30:
                    return R.id.field_finish_player2;
            }
        }
        return 0;
    }

    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if (uri == null)
            return null;
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
