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
import com.example.EssentialClasses.Player;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameWinLoseDialogFragment extends DialogFragment {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseGame databaseGame;
    private boolean isItMyTurn;
    private boolean amIPlayer1;
    private boolean amIRequestedAdditionalAction;
    private boolean isRemotePlayerRequestedAdditionalAction;

    private FloatingActionButton buttonClose;

    private TextView gameWinOrLoseTextView;
    private TextView gameFinalMessageTextView;
    private Button buttonConfirm;

    public GameWinLoseDialogFragment() {
        // Required empty public constructor
    }

    public static GameWinLoseDialogFragment newInstance(String gameId, int positionOnTheBoard, boolean isItMyTurn, boolean amIPlayer1, boolean amIRequestedAdditionalAction, boolean isRemotePlayerRequestedAdditionalAction) {
        GameWinLoseDialogFragment fragment = new GameWinLoseDialogFragment();

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
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//      dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (amIPlayer1 == true) {
            dialog.setContentView(R.layout.game_win_lose_dialog_active_frame);
            gameWinOrLoseTextView = (TextView) dialog.findViewById(R.id.win_or_lose_text_view);
            gameFinalMessageTextView = (TextView) dialog.findViewById(R.id.game_final_message_text_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_game_win_lose);

            if (databaseGame.getPlayer1Position() >= 30) {
                gameWinOrLoseTextView.setText("You won (this time)!!");
                gameFinalMessageTextView.setText("Congratulations!!\nHope you enjoyed the game and had lots of fun with the other person!\n\nThank you for playing! ^_^");
            } else if (databaseGame.getPlayer2Position() >= 30) {
                gameWinOrLoseTextView.setText("You lost (this time)!");
                gameFinalMessageTextView.setText("Even so, hope you had lots of fun\nwith the other person and with the game! :) \n\n Thank you for playing! ^_^");

            }

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (databaseGame.getTextPlaceholder2().equals("confirmGameEndPlayer2")) {
                        Player.get().removeGameFromGameList(Player.get().getGameList().indexOf(databaseGame));

                        mDatabase.child("users").child(databaseGame.getPlayer1Id()).child("userGames").child(databaseGame.getGameId()).setValue(null);
                        mDatabase.child("users").child(databaseGame.getPlayer2Id()).child("userGames").child(databaseGame.getGameId()).setValue(null);

                        mDatabase.child("games").child(databaseGame.getGameId()).setValue(null);
                    } else {
                        databaseGame.setTextPlaceholder1("confirmGameEndPlayer1");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());

                        Player.get().removeGameFromGameList(Player.get().getGameList().indexOf(databaseGame));
                    }

                    dismiss();
                    getActivity().finish();

                }
            });

        } else if (amIPlayer1 == false) {
            dialog.setContentView(R.layout.game_win_lose_dialog_active_frame);
            gameWinOrLoseTextView = (TextView) dialog.findViewById(R.id.win_or_lose_text_view);
            gameFinalMessageTextView = (TextView) dialog.findViewById(R.id.game_final_message_text_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_game_win_lose);

            if (databaseGame.getPlayer1Position() >= 30) {
                gameWinOrLoseTextView.setText("You lost (this time)!");
                gameFinalMessageTextView.setText("Even so, hope you had lots of fun\nwith the other person and with the game! :) \n\n Thank you for playing! ^_^");
            } else if (databaseGame.getPlayer2Position() >= 30) {
                gameWinOrLoseTextView.setText("You won (this time)!!");
                gameFinalMessageTextView.setText("Congratulations!!\nHope you enjoyed the game and had lots of fun with the other person!\n\nThank you for playing! ^_^");
            }

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (databaseGame.getTextPlaceholder1().equals("confirmGameEndPlayer1")) {
                        Player.get().removeGameFromGameList(Player.get().getGameList().indexOf(databaseGame));

                        mDatabase.child("users").child(databaseGame.getPlayer1Id()).child("userGames").child(databaseGame.getGameId()).setValue(null);
                        mDatabase.child("users").child(databaseGame.getPlayer2Id()).child("userGames").child(databaseGame.getGameId()).setValue(null);

                        mDatabase.child("games").child(databaseGame.getGameId()).setValue(null);
                    } else {
                        databaseGame.setTextPlaceholder2("confirmGameEndPlayer2");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());

                        Player.get().removeGameFromGameList(Player.get().getGameList().indexOf(databaseGame));
                    }

                    dismiss();
                    getActivity().finish();

                }
            });
        }

        buttonClose = (FloatingActionButton) dialog.findViewById(R.id.button_dialog_game_win_lose_close);

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
}