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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.Player;
import com.example.EssentialClasses.Truth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TruthDialogFragment extends DialogFragment {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Truth truth;
    private DatabaseGame databaseGame;
    private boolean isItMyTurn;
    private boolean amIPlayer1;
    private boolean amIRequestedAdditionalAction;
    private boolean isRemotePlayerRequestedAdditionalAction;

    private FloatingActionButton buttonClose;

    private TextView questionTextView;
    private EditText answerEditText;
    private Button buttonSubmit;
    private Button buttonDecline;

    private TextView answerTextView;

    private Button buttonConfirm;

    public TruthDialogFragment() {
        // Required empty public constructor
    }

    public static TruthDialogFragment newInstance(String gameId, int positionOnTheBoard, boolean isItMyTurn, boolean amIPlayer1, boolean amIRequestedAdditionalAction, boolean isRemotePlayerRequestedAdditionalAction) {
        TruthDialogFragment fragment = new TruthDialogFragment();

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
            truth = new Truth(getArguments().getInt("position"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//      dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (isItMyTurn == true && isRemotePlayerRequestedAdditionalAction == false) {
            dialog.setContentView(R.layout.truth_dialog_1st_active_frame);
            questionTextView = (TextView) dialog.findViewById(R.id.truth_question_text_view);
            answerEditText = (EditText) dialog.findViewById(R.id.answer_truth_edit_text);
            buttonSubmit = (Button) dialog.findViewById(R.id.button_submit_truth);
            buttonDecline = (Button) dialog.findViewById(R.id.button_decline_truth);

            truth.setQuestion(databaseGame.getTextPlaceholder1());
            questionTextView.setText(truth.getQuestion());

            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String writtenText = answerEditText.getText().toString();
                    if (writtenText.isEmpty() || writtenText.equals(" ") || writtenText.equals("  ") || writtenText.equals("   ")) {
                        Toast.makeText(getContext(), "Please type an answer before submitting!", Toast.LENGTH_LONG).show();
                    } else {
                        truth.setAnswer(writtenText);
                        databaseGame.setTextPlaceholder2(truth.getAnswer());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());

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

        } else if (isItMyTurn == false && amIRequestedAdditionalAction == true) {
            dialog.setContentView(R.layout.truth_dialog_2nd_active_frame);
            questionTextView = (TextView) dialog.findViewById(R.id.truth_question_text_view);
            answerTextView = (TextView) dialog.findViewById(R.id.answer_truth_text_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_truth);

            truth.setQuestion(databaseGame.getTextPlaceholder1());
            truth.setAnswer(databaseGame.getTextPlaceholder2());
            questionTextView.setText(truth.getQuestion());
            answerTextView.setText(truth.getAnswer());

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Now is the end of this card, and the turn ends completely and shifts to the other player, while all the temporary "turn values" are reset
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

                    dismiss();
                    getActivity().finish();
                }
            });

        } else if ((isItMyTurn == false && amIRequestedAdditionalAction == false) || (isItMyTurn == true && isRemotePlayerRequestedAdditionalAction == true)) {
            dialog.setContentView(R.layout.truth_dialog_passive_frame);
            questionTextView = (TextView) dialog.findViewById(R.id.truth_question_text_view);
            answerTextView = (TextView) dialog.findViewById(R.id.answer_truth_text_view);

            if ((databaseGame.getTextPlaceholder1() != null) && (!(databaseGame.getTextPlaceholder1().equals(" ")))) {
                truth.setQuestion(databaseGame.getTextPlaceholder1());
                questionTextView.setText(truth.getQuestion());
            } else {
                questionTextView.setText("Waiting for question...");
            }

            if ((databaseGame.getTextPlaceholder2() != null) && (!(databaseGame.getTextPlaceholder2().equals(" ")))) {
                truth.setAnswer(databaseGame.getTextPlaceholder2());
                answerTextView.setText(truth.getAnswer());
            } else {
                answerTextView.setText("Waiting for answer...");
            }
        } else
            dialog.setContentView(R.layout.truth_dialog_passive_frame);
        buttonClose = (FloatingActionButton) dialog.findViewById(R.id.button_dialog_truth_close);

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