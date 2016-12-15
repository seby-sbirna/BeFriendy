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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.Minigame;
import com.example.EssentialClasses.Player;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MinigameRockPaperScissorsDialogFragment extends DialogFragment {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Minigame minigame;
    private DatabaseGame databaseGame;
    private boolean isItMyTurn;
    private boolean amIPlayer1;
    private boolean amIRequestedAdditionalAction;
    private boolean isRemotePlayerRequestedAdditionalAction;

    private FloatingActionButton buttonClose;

    private ImageButton buttonRock;
    private ImageButton buttonPaper;
    private ImageButton buttonScissors;

    private TextView minigameWinLoseTextView;
    private ImageView minigamePlayerChoiceImageView;
    private ImageView minigameOpponentChoiceImageView;
    private Button buttonConfirm;

    public MinigameRockPaperScissorsDialogFragment() {
        // Required empty public constructor
    }

    public static MinigameRockPaperScissorsDialogFragment newInstance(String gameId, int positionOnTheBoard, boolean isItMyTurn, boolean amIPlayer1, boolean amIRequestedAdditionalAction, boolean isRemotePlayerRequestedAdditionalAction) {
        MinigameRockPaperScissorsDialogFragment fragment = new MinigameRockPaperScissorsDialogFragment();

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
            minigame = new Minigame(getArguments().getInt("position"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//      dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if ((amIPlayer1 == true && databaseGame.getTextPlaceholder1().equals(" ")) || (amIPlayer1 == false && databaseGame.getTextPlaceholder2().equals(" "))) {
            dialog.setContentView(R.layout.minigame_rock_paper_scissors_dialog_1st_and_2nd_active_frame);
            buttonRock = (ImageButton) dialog.findViewById(R.id.button_minigame_rock);
            buttonPaper = (ImageButton) dialog.findViewById(R.id.button_minigame_paper);
            buttonScissors = (ImageButton) dialog.findViewById(R.id.button_minigame_scissors);

            buttonRock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (amIPlayer1 == true) {
                        databaseGame.setTextPlaceholder1("rock");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                    } else {
                        databaseGame.setTextPlaceholder2("rock");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                    }

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

            buttonPaper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (amIPlayer1 == true) {
                        databaseGame.setTextPlaceholder1("paper");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                    } else {
                        databaseGame.setTextPlaceholder2("paper");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                    }

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

            buttonScissors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (amIPlayer1 == true) {
                        databaseGame.setTextPlaceholder1("scissors");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());
                    } else {
                        databaseGame.setTextPlaceholder2("scissors");
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder2").setValue(databaseGame.getTextPlaceholder2());
                    }

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

        } else if ((!databaseGame.getTextPlaceholder1().equals(" ") && !databaseGame.getTextPlaceholder2().equals(" ")) && ((amIPlayer1 == true && !databaseGame.getTextPlaceholder3().equals("confirmPlayer1")) || (amIPlayer1 == false && !databaseGame.getTextPlaceholder3().equals("confirmPlayer2")))) {
            dialog.setContentView(R.layout.minigame_rock_paper_scissors_dialog_3rd_and_4th_active_frame);
            minigameWinLoseTextView = (TextView) dialog.findViewById(R.id.minigame_rock_paper_scissors_win_lose_text_view);
            minigamePlayerChoiceImageView = (ImageView) dialog.findViewById(R.id.minigame_player_choice_image_view);
            minigameOpponentChoiceImageView = (ImageView) dialog.findViewById(R.id.minigame_opponent_choice_image_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_minigame);

            String player1Choice = databaseGame.getTextPlaceholder1();
            String player2Choice = databaseGame.getTextPlaceholder2();

            int whoWon = winOrLose(player1Choice, player2Choice);

            if (whoWon == 1) {
                if (amIPlayer1 == true) {
                    minigameWinLoseTextView.setText("You won!\nYou advance 2 fields and get a new card!");

                    if (player1Choice.equals("rock"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player1Choice.equals("paper"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player1Choice.equals("scissors"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    if (player2Choice.equals("rock"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player2Choice.equals("paper"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player2Choice.equals("scissors"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    buttonConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (databaseGame.getTextPlaceholder3().equals(" ")) {
                                databaseGame.setTextPlaceholder3("confirmPlayer1");
                                mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());
                            } else {
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

                                if (databaseGame.isPlayer1Turn() == false) {
                                    databaseGame.setPlayer1Turn(true);
                                    mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());
                                } /*else {
                                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Service.NOTIFICATION_SERVICE);

                                    Intent intentToGame = new Intent(getContext(), GameActivity.class);
                                    if (databaseGame.getPlayer1Id().equals(Player.get().getUserId())) {
                                        intentToGame.putExtra("remotePlayerId", databaseGame.getPlayer2Id());
                                    } else {
                                        intentToGame.putExtra("remotePlayerId", databaseGame.getPlayer1Id());
                                    }
                                    intentToGame.putExtra("gameId", databaseGame.getGameId());
                                    PendingIntent pendingIntentToGame = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), intentToGame, 0);
                                    Notification notification;

                                    notification = new Notification.Builder(getContext())
                                            .setAutoCancel(true)
                                            .setSmallIcon(R.drawable.ic_check_circle_black_24px)
                                            .setContentTitle("New game update on game #" + String.valueOf(Player.get().getGameList().indexOf(databaseGame) + 1))
                                            .setContentText("The previous minigame has given you another chance!\nYour turn continues!")
                                            .setContentIntent(pendingIntentToGame)
                                            .build();
                                    notification.defaults |= Notification.DEFAULT_SOUND;
                                    notificationManager.notify("Continue turn notification", Player.get().getGameList().indexOf(databaseGame), notification);
                                } */

                                movePlayerOnTheBoard("player1", 2);
                            }
                            dismiss();
                            getActivity().finish();
                        }
                    });
                } else if (amIPlayer1 == false) {
                    minigameWinLoseTextView.setText("You lost, but\ngood luck next time! :)");

                    if (player1Choice.equals("rock"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player1Choice.equals("paper"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player1Choice.equals("scissors"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    if (player2Choice.equals("rock"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player2Choice.equals("paper"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player2Choice.equals("scissors"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    buttonConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (databaseGame.getTextPlaceholder3().equals(" ")) {
                                databaseGame.setTextPlaceholder3("confirmPlayer2");
                                mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());
                            } else {
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

                                if (databaseGame.isPlayer1Turn() == false) {
                                    databaseGame.setPlayer1Turn(true);
                                    mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());
                                }

                                movePlayerOnTheBoard("player1", 2);
                            }
                            dismiss();
                            getActivity().finish();
                        }
                    });
                }
            } else if (whoWon == 2) {
                if (amIPlayer1 == true) {
                    minigameWinLoseTextView.setText("You lost, but\ngood luck next time! :)");

                    if (player1Choice.equals("rock"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player1Choice.equals("paper"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player1Choice.equals("scissors"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    if (player2Choice.equals("rock"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player2Choice.equals("paper"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player2Choice.equals("scissors"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    buttonConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (databaseGame.getTextPlaceholder3().equals(" ")) {
                                databaseGame.setTextPlaceholder3("confirmPlayer1");
                                mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());
                            } else {
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

                                if (databaseGame.isPlayer1Turn() == true) {
                                    databaseGame.setPlayer1Turn(false);
                                    mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());
                                }

                                movePlayerOnTheBoard("player2", 2);
                            }
                            dismiss();
                            getActivity().finish();
                        }
                    });
                } else if (amIPlayer1 == false) {
                    minigameWinLoseTextView.setText("You won!\nYou advance 2 fields and get a new card!");

                    if (player1Choice.equals("rock"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player1Choice.equals("paper"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player1Choice.equals("scissors"))
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    if (player2Choice.equals("rock"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player2Choice.equals("paper"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player2Choice.equals("scissors"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    buttonConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (databaseGame.getTextPlaceholder3().equals(" ")) {
                                databaseGame.setTextPlaceholder3("confirmPlayer2");
                                mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());
                            } else {
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

                                if (databaseGame.isPlayer1Turn() == true) {
                                    databaseGame.setPlayer1Turn(false);
                                    mDatabase.child("games").child(databaseGame.getGameId()).child("player1Turn").setValue(databaseGame.isPlayer1Turn());
                                } /*else {
                                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Service.NOTIFICATION_SERVICE);

                                    Intent intentToGame = new Intent(getContext(), GameActivity.class);
                                    if (databaseGame.getPlayer1Id().equals(Player.get().getUserId())) {
                                        intentToGame.putExtra("remotePlayerId", databaseGame.getPlayer2Id());
                                    } else {
                                        intentToGame.putExtra("remotePlayerId", databaseGame.getPlayer1Id());
                                    }
                                    intentToGame.putExtra("gameId", databaseGame.getGameId());
                                    PendingIntent pendingIntentToGame = PendingIntent.getActivity(getContext(), (int) System.currentTimeMillis(), intentToGame, 0);
                                    Notification notification;

                                    notification = new Notification.Builder(getContext())
                                            .setAutoCancel(true)
                                            .setSmallIcon(R.drawable.ic_check_circle_black_24px)
                                            .setContentTitle("New game update on game #" + String.valueOf(Player.get().getGameList().indexOf(databaseGame) + 1))
                                            .setContentText("The previous minigame has given you another chance!\nYour turn continues!")
                                            .setContentIntent(pendingIntentToGame)
                                            .build();
                                    notification.defaults |= Notification.DEFAULT_SOUND;
                                    notificationManager.notify("Continue turn notification", Player.get().getGameList().indexOf(databaseGame), notification);
                                } */

                                movePlayerOnTheBoard("player2", 2);
                            }
                            dismiss();
                            getActivity().finish();
                        }
                    });
                }
            } else if (whoWon == 0) {
                minigameWinLoseTextView.setText("It's a draw!\nLet's play again!");

                if (player1Choice.equals("rock")) {
                    minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                } else if (player1Choice.equals("paper")) {
                    minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                } else if (player1Choice.equals("scissors")) {
                    minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));
                    minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));
                }

                buttonConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (databaseGame.getTextPlaceholder3().equals(" ")) {
                            if (amIPlayer1 == true) {
                                databaseGame.setTextPlaceholder3("confirmPlayer1");
                                mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());
                            } else if (amIPlayer1 == false) {
                                databaseGame.setTextPlaceholder3("confirmPlayer2");
                                mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());
                            }
                        } else {
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

                        }
                        dismiss();
                        getActivity().finish();
                    }
                });
            }

        } else {
            if (databaseGame.getTextPlaceholder1().equals(" ") || databaseGame.getTextPlaceholder2().equals(" ")) {
                dialog.setContentView(R.layout.minigame_rock_paper_scissors_dialog_1st_passive_frame);
                minigamePlayerChoiceImageView = (ImageView) dialog.findViewById(R.id.minigame_player_choice_image_view);

                String player1Choice = databaseGame.getTextPlaceholder1();
                String player2Choice = databaseGame.getTextPlaceholder2();

                if (amIPlayer1 == true) {
                    if (player1Choice.equals("rock"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player1Choice.equals("paper"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player1Choice.equals("scissors"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));
                } else if (amIPlayer1 == false) {
                    if (player2Choice.equals("rock"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    else if (player2Choice.equals("paper"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    else if (player2Choice.equals("scissors"))
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));
                }

            } else if (!databaseGame.getTextPlaceholder1().equals(" ") && !databaseGame.getTextPlaceholder2().equals(" ")) {
                dialog.setContentView(R.layout.minigame_rock_paper_scissors_dialog_2nd_passive_frame);
                minigameWinLoseTextView = (TextView) dialog.findViewById(R.id.minigame_rock_paper_scissors_win_lose_text_view);
                minigamePlayerChoiceImageView = (ImageView) dialog.findViewById(R.id.minigame_player_choice_image_view);
                minigameOpponentChoiceImageView = (ImageView) dialog.findViewById(R.id.minigame_opponent_choice_image_view);

                String player1Choice = databaseGame.getTextPlaceholder1();
                String player2Choice = databaseGame.getTextPlaceholder2();

                int whoWon = winOrLose(player1Choice, player2Choice);

                if (whoWon == 1) {
                    if (amIPlayer1 == true) {
                        minigameWinLoseTextView.setText("You won!\nYou advance 2 fields and get a new card!");

                        if (player1Choice.equals("rock"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player1Choice.equals("paper"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player1Choice.equals("scissors"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                        if (player2Choice.equals("rock"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player2Choice.equals("paper"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player2Choice.equals("scissors"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    } else if (amIPlayer1 == false) {
                        minigameWinLoseTextView.setText("You lost, but\ngood luck next time! :)");

                        if (player1Choice.equals("rock"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player1Choice.equals("paper"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player1Choice.equals("scissors"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                        if (player2Choice.equals("rock"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player2Choice.equals("paper"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player2Choice.equals("scissors"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    }
                } else if (whoWon == 2) {
                    if (amIPlayer1 == true) {
                        minigameWinLoseTextView.setText("You lost, but\ngood luck next time! :)");

                        if (player1Choice.equals("rock"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player1Choice.equals("paper"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player1Choice.equals("scissors"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                        if (player2Choice.equals("rock"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player2Choice.equals("paper"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player2Choice.equals("scissors"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    } else if (amIPlayer1 == false) {
                        minigameWinLoseTextView.setText("You won!\nYou advance 2 fields and get a new card!");

                        if (player1Choice.equals("rock"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player1Choice.equals("paper"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player1Choice.equals("scissors"))
                            minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                        if (player2Choice.equals("rock"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        else if (player2Choice.equals("paper"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        else if (player2Choice.equals("scissors"))
                            minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));

                    }
                } else if (whoWon == 0) {
                    minigameWinLoseTextView.setText("It's a draw!\nLet's play again!");

                    if (player1Choice.equals("rock")) {
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_rock));
                    } else if (player1Choice.equals("paper")) {
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_paper));
                    } else if (player1Choice.equals("scissors")) {
                        minigamePlayerChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));
                        minigameOpponentChoiceImageView.setImageDrawable(getResources().getDrawable(R.drawable.minigame_scissors));
                    }

                }

            }
        }

        buttonClose = (FloatingActionButton) dialog.findViewById(R.id.button_dialog_minigame_close);

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

    private int winOrLose(String player1Choice, String player2Choice) {
        int whoWon = 0;
        if (player1Choice.equals("rock")) {
            switch (player2Choice) {
                case "rock":
                    whoWon = 0;
                    break;
                case "paper":
                    whoWon = 2;
                    break;
                case "scissors":
                    whoWon = 1;
                    break;
            }
        } else if (player1Choice.equals("paper")) {
            switch (player2Choice) {
                case "rock":
                    whoWon = 1;
                    break;
                case "paper":
                    whoWon = 0;
                    break;
                case "scissors":
                    whoWon = 2;
                    break;
            }
        } else if (player1Choice.equals("scissors")) {
            switch (player2Choice) {
                case "rock":
                    whoWon = 2;
                    break;
                case "paper":
                    whoWon = 1;
                    break;
                case "scissors":
                    whoWon = 0;
                    break;
            }
        }
        return whoWon;
    }

    public void movePlayerOnTheBoard(String whichPlayer, int steps) {
        if (whichPlayer.equals("player1")) {
            databaseGame.setPlayer1Position(databaseGame.getPlayer1Position() + steps);
            mDatabase.child("games").child(databaseGame.getGameId()).child("player1Position").setValue(databaseGame.getPlayer1Position());

            if (databaseGame.getPlayer1Position() > 29) {
                databaseGame.setPlayer1Position(30);
                mDatabase.child("games").child(databaseGame.getGameId()).child("player1Position").setValue(databaseGame.getPlayer1Position());
            }
        } else if (whichPlayer.equals("player2")) {
            databaseGame.setPlayer2Position(databaseGame.getPlayer2Position() + steps);
            mDatabase.child("games").child(databaseGame.getGameId()).child("player2Position").setValue(databaseGame.getPlayer2Position());

            if (databaseGame.getPlayer2Position() > 29) {
                databaseGame.setPlayer2Position(30);
                mDatabase.child("games").child(databaseGame.getGameId()).child("player2Position").setValue(databaseGame.getPlayer2Position());
            }
        }
    }
}