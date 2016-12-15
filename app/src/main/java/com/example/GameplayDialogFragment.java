package com.example;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.Gameplay;
import com.example.EssentialClasses.Player;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class GameplayDialogFragment extends DialogFragment {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Gameplay gameplay;
    private DatabaseGame databaseGame;
    private boolean isItMyTurn;
    private boolean amIPlayer1;
    private boolean amIRequestedAdditionalAction;
    private boolean isRemotePlayerRequestedAdditionalAction;

    private FloatingActionButton buttonClose;

    private Button buttonAccept;
    private Button buttonDecline;

    private TextView gameplaySpellNameTextView;
    private TextView gameplaySpellDescriptionTextView;

    private Button buttonConfirm;

    public GameplayDialogFragment() {
        // Required empty public constructor
    }

    public static GameplayDialogFragment newInstance(String gameId, int positionOnTheBoard, boolean isItMyTurn, boolean amIPlayer1, boolean amIRequestedAdditionalAction, boolean isRemotePlayerRequestedAdditionalAction) {
        GameplayDialogFragment fragment = new GameplayDialogFragment();

        Bundle args = new Bundle();
        args.putString("gameId", gameId);
        args.putInt("position", positionOnTheBoard);
        args.putBoolean("isItMyTurn", isItMyTurn);
        args.putBoolean("amIPlayer1", amIPlayer1);
        args.putBoolean("amIRequestedAdditionalAction", amIRequestedAdditionalAction);
        args.putBoolean("isRemotePlayerRequestedAdditionalAction", isRemotePlayerRequestedAdditionalAction);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);

        if (getArguments().containsKey("isItMyTurn")) {
            isItMyTurn = getArguments().getBoolean("isItMyTurn");
        }
        if (getArguments().containsKey("amIPlayer1")) {
            amIPlayer1 = getArguments().getBoolean("amIPlayer1");
        }
        if (getArguments().containsKey("amIRequestedAdditionalAction")) {
            amIRequestedAdditionalAction = getArguments().getBoolean("amIRequestedAdditionalAction");
        }
        if (getArguments().containsKey("isRemotePlayerRequestedAdditionalAction")) {
            isRemotePlayerRequestedAdditionalAction = getArguments().getBoolean("isRemotePlayerRequestedAdditionalAction");
        }
        if (getArguments().containsKey("gameId")) {
            for (DatabaseGame currGame : Player.get().getGameList()) {
                if (this.getArguments().getString("gameId").equals(currGame.getGameId())) {
                    databaseGame = currGame;
                    break;
                }
            }
        }
        if (getArguments().containsKey("position")) {
            gameplay = new Gameplay(getArguments().getInt("position"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//      dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (isItMyTurn == true && amIRequestedAdditionalAction == false) {
            dialog.setContentView(R.layout.gameplay_dialog_1st_active_frame);
            buttonAccept = (Button) dialog.findViewById(R.id.button_accept_gameplay);
            buttonDecline = (Button) dialog.findViewById(R.id.button_decline_gameplay);

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Random rng = new Random();
                    int randomPick = rng.nextInt(6) + 1;

                    databaseGame.setTextPlaceholder1(Integer.toString(randomPick));
                    mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());

                    //Change the gameplay now
                    if (amIPlayer1 == true) {
                        databaseGame.setIsPlayer1AdditionalActionNeeded(true);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                    } else {
                        databaseGame.setIsPlayer2AdditionalActionNeeded(true);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());
                    }
                    dismiss();
                    getActivity().finish();
                }
            });

            buttonDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (amIPlayer1 == true) {
                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setPlayer1Turn(!databaseGame.isPlayer1Turn());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                        movePlayerOnTheBoard("player1", -4);
                    } else if (amIPlayer1 == false) {
                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setPlayer1Turn(!databaseGame.isPlayer1Turn());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                        movePlayerOnTheBoard("player2", -4);
                    }
                    dismiss();
                    getActivity().finish();
                }
            });

        } else if (isItMyTurn == true && amIRequestedAdditionalAction == true && isRemotePlayerRequestedAdditionalAction == false) {
            dialog.setContentView(R.layout.gameplay_dialog_2nd_active_frame);
            gameplaySpellNameTextView = (TextView) dialog.findViewById(R.id.gameplay_spell_name_text_view);
            gameplaySpellDescriptionTextView = (TextView) dialog.findViewById(R.id.gameplay_spell_description_text_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_gameplay);

            int randomPick = Integer.parseInt(databaseGame.getTextPlaceholder1());

            switch (randomPick) {
                case 1:
                    gameplaySpellNameTextView.setText("Time Reversal");
                    gameplaySpellDescriptionTextView.setText("Both players are set back\n3 positions on the board");
                    break;
                case 2:
                    gameplaySpellNameTextView.setText("Time Spiral");
                    gameplaySpellDescriptionTextView.setText("The choosing player is\nteleported either back or forth, to\na random place on the field, within maximum 5 fields of his/her current\nposition");
                    break;
                case 3:
                    gameplaySpellNameTextView.setText("Venom Trap");
                    gameplaySpellDescriptionTextView.setText("The awaiting player is pinned in\nplace for 1 turn");
                    break;
                case 4:
                    gameplaySpellNameTextView.setText("Time Skip");
                    gameplaySpellDescriptionTextView.setText("Both players advance\n2 positions on the board");
                    break;
                case 5:
                    gameplaySpellNameTextView.setText("World Shift");
                    gameplaySpellDescriptionTextView.setText("Both players switch their current positions between them");
                    break;
                case 6:
                    gameplaySpellNameTextView.setText("Void Peering");
                    gameplaySpellDescriptionTextView.setText("You have escaped untouched the hands of fate! Nothing has been gained or lost.\nThe game continues in its normal order.");
                    break;
            }

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Change the gameplay now
                    if (amIPlayer1 == true) {
                        databaseGame.setIsPlayer2AdditionalActionNeeded(true);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());
                    } else {
                        databaseGame.setIsPlayer1AdditionalActionNeeded(true);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                    }
                    dismiss();
                    getActivity().finish();
                }
            });


        } else if (isItMyTurn == false && amIRequestedAdditionalAction == true) {
            dialog.setContentView(R.layout.gameplay_dialog_2nd_active_frame);
            gameplaySpellNameTextView = (TextView) dialog.findViewById(R.id.gameplay_spell_name_text_view);
            gameplaySpellDescriptionTextView = (TextView) dialog.findViewById(R.id.gameplay_spell_description_text_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_gameplay);

            final int randomPick = Integer.parseInt(databaseGame.getTextPlaceholder1());

            switch (randomPick) {
                case 1:
                    gameplaySpellNameTextView.setText("Time Reversal");
                    gameplaySpellDescriptionTextView.setText("Both players are set back\n3 positions on the board");
                    break;
                case 2:
                    gameplaySpellNameTextView.setText("Time Spiral");
                    gameplaySpellDescriptionTextView.setText("The choosing player is\nteleported either back or forth, to\na random place on the field, within maximum 5 fields of his/her current\nposition");
                    break;
                case 3:
                    gameplaySpellNameTextView.setText("Venom Trap");
                    gameplaySpellDescriptionTextView.setText("The awaiting player is pinned in\nplace for 1 turn");
                    break;
                case 4:
                    gameplaySpellNameTextView.setText("Time Skip");
                    gameplaySpellDescriptionTextView.setText("Both players advance\n2 positions on the board");
                    break;
                case 5:
                    gameplaySpellNameTextView.setText("World Shift");
                    gameplaySpellDescriptionTextView.setText("Both players switch their current positions between them");
                    break;
                case 6:
                    gameplaySpellNameTextView.setText("Void Peering");
                    gameplaySpellDescriptionTextView.setText("Everyone has escaped untouched\nthe hands of fate!\n\nNothing has been gained or lost.\nThe game continues in its normal order.");
                    break;
            }

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (randomPick == 1) {
                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setPlayer1Turn(!databaseGame.isPlayer1Turn());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                        movePlayerOnTheBoard("player1", -3);
                        movePlayerOnTheBoard("player2", -3);

                    } else if (randomPick == 2) {
                        Random rng = new Random();
                        int generatedNumber = rng.nextInt(6);
                        int sign = rng.nextInt(2);

                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setPlayer1Turn(!databaseGame.isPlayer1Turn());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                        if (amIPlayer1 == true) {
                            if (sign == 1) {
                                movePlayerOnTheBoard("player2", generatedNumber);
                            } else {
                                movePlayerOnTheBoard("player2", (-1) * generatedNumber);
                            }
                        } else if (amIPlayer1 == false) {
                            if (sign == 1) {
                                movePlayerOnTheBoard("player1", generatedNumber);
                            } else {
                                movePlayerOnTheBoard("player1", (-1) * generatedNumber);
                            }
                        }
                    } else if (randomPick == 3) {
                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                    } else if (randomPick == 4) {
                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setPlayer1Turn(!databaseGame.isPlayer1Turn());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                        movePlayerOnTheBoard("player1", 2);
                        movePlayerOnTheBoard("player2", 2);

                    } else if (randomPick == 5) {
                        int differenceFromPlayer1ToPlayer2OnTheBoard = databaseGame.getPlayer1Position() - databaseGame.getPlayer2Position();

                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setPlayer1Turn(!databaseGame.isPlayer1Turn());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                        movePlayerOnTheBoard("player1", (-1) * differenceFromPlayer1ToPlayer2OnTheBoard);
                        movePlayerOnTheBoard("player2", differenceFromPlayer1ToPlayer2OnTheBoard);
                    } else if (randomPick == 6) {
                        databaseGame.setTextPlaceholder1(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                        databaseGame.setTextPlaceholder2(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                        databaseGame.setTextPlaceholder3(" ");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                        databaseGame.setPlayer1Turn(!databaseGame.isPlayer1Turn());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());

                        databaseGame.setHasPlayerRolledTheDice(false);
                        mDatabase.child("games").child(databaseGame.getGameId()).child("hasPlayerRolledTheDice").setValue(databaseGame.getHasPlayerRolledTheDice());

                    }

                    dismiss();
                    getActivity().finish();
                }
            });

        } else {
            if (isItMyTurn == false && amIRequestedAdditionalAction == false) {
                dialog.setContentView(R.layout.gameplay_dialog_1st_passive_frame);
            } else if (isItMyTurn == true && isRemotePlayerRequestedAdditionalAction == true) {
                dialog.setContentView(R.layout.gameplay_dialog_2nd_passive_frame);
                gameplaySpellNameTextView = (TextView) dialog.findViewById(R.id.gameplay_spell_name_text_view);
                gameplaySpellDescriptionTextView = (TextView) dialog.findViewById(R.id.gameplay_spell_description_text_view);

                final int randomPick = Integer.parseInt(databaseGame.getTextPlaceholder1());

                switch (randomPick) {
                    case 1:
                        gameplaySpellNameTextView.setText("Time Reversal");
                        gameplaySpellDescriptionTextView.setText("Both players are set back\n3 positions on the board");
                        break;
                    case 2:
                        gameplaySpellNameTextView.setText("Time Spiral");
                        gameplaySpellDescriptionTextView.setText("The choosing player is\nteleported either back or forth, to\na random place on the field, within maximum 5 fields of his/her current\nposition");
                        break;
                    case 3:
                        gameplaySpellNameTextView.setText("Venom Trap");
                        gameplaySpellDescriptionTextView.setText("The awaiting player is pinned in\nplace for 1 turn");
                        break;
                    case 4:
                        gameplaySpellNameTextView.setText("Time Skip");
                        gameplaySpellDescriptionTextView.setText("Both players advance\n2 positions on the board");
                        break;
                    case 5:
                        gameplaySpellNameTextView.setText("World Shift");
                        gameplaySpellDescriptionTextView.setText("Both players switch their current positions between them");
                        break;
                    case 6:
                        gameplaySpellNameTextView.setText("Void Peering");
                        gameplaySpellDescriptionTextView.setText("Everyone has escaped untouched\nthe hands of fate!\n\nNothing has been gained or lost.\nThe game continues in its normal order.");
                        break;
                }
            } else
                dialog.setContentView(R.layout.gameplay_dialog_1st_passive_frame);
        }

        buttonClose = (FloatingActionButton) dialog.findViewById(R.id.button_dialog_gameplay_close);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                getActivity().finish();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    public void movePlayerOnTheBoard(String whichPlayer, int steps) {
        databaseGame.setPlayer1Position(databaseGame.getPlayer1Position() + steps);

        if (whichPlayer.equals("player1")) {
            if (databaseGame.getPlayer1Position() > 29) {
                databaseGame.setPlayer1Position(30);
                mDatabase.child("games").child(databaseGame.getGameId()).child("player1Position").setValue(databaseGame.getPlayer1Position());
            } else if (databaseGame.getPlayer1Position() < 1) {
                databaseGame.setPlayer1Position(0);
                mDatabase.child("games").child(databaseGame.getGameId()).child("player1Position").setValue(databaseGame.getPlayer1Position());
            } else {
                mDatabase.child("games").child(databaseGame.getGameId()).child("player1Position").setValue(databaseGame.getPlayer1Position());
            }
        } else if (whichPlayer.equals("player2")) {
            databaseGame.setPlayer2Position(databaseGame.getPlayer2Position() + steps);

            if (databaseGame.getPlayer2Position() > 29) {
                databaseGame.setPlayer2Position(30);
                mDatabase.child("games").child(databaseGame.getGameId()).child("player2Position").setValue(databaseGame.getPlayer2Position());
            } else if (databaseGame.getPlayer2Position() < 1) {
                databaseGame.setPlayer2Position(0);
                mDatabase.child("games").child(databaseGame.getGameId()).child("player2Position").setValue(databaseGame.getPlayer2Position());
            } else {
                mDatabase.child("games").child(databaseGame.getGameId()).child("player2Position").setValue(databaseGame.getPlayer2Position());
            }
        }
    }
}