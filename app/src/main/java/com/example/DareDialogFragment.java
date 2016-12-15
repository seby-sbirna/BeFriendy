package com.example;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.EssentialClasses.Dare;
import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.Player;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link TruthDialogFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link TruthDialogFragment#newInstance} factory method to
// * create an instance of this fragment.
// */


public class DareDialogFragment extends DialogFragment {
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 1567;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://befriendy-e8f50.appspot.com");
    private Dare dare;
    private DatabaseGame databaseGame;
    private boolean isItMyTurn;
    private boolean amIPlayer1;
    private boolean amIRequestedAdditionalAction;
    private boolean isRemotePlayerRequestedAdditionalAction;
    private Uri pictureUri;
    private String dareImageLocation;

    private FloatingActionButton buttonClose;

    private TextView dareQuestionTextView;
    private Button buttonDecline;
    private Button buttonPhoto;
    private Button buttonGallery;

    private ImageView darePhotoImageView;

    private Button buttonConfirm;

    public DareDialogFragment() {
        // Required empty public constructor
    }

    public static DareDialogFragment newInstance(String gameId, int positionOnTheBoard, boolean isItMyTurn, boolean amIPlayer1, boolean amIRequestedAdditionalAction, boolean isRemotePlayerRequestedAdditionalAction) {
        DareDialogFragment fragment = new DareDialogFragment();

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
            dare = new Dare(getArguments().getInt("position"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//      dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (isItMyTurn == true && isRemotePlayerRequestedAdditionalAction == false) {
            dialog.setContentView(R.layout.dare_dialog_1st_active_frame);
            dareQuestionTextView = (TextView) dialog.findViewById(R.id.dare_question_text_view);
            buttonDecline = (Button) dialog.findViewById(R.id.button_decline_dare);
            buttonPhoto = (Button) dialog.findViewById(R.id.button_photo_dare);
            buttonGallery = (Button) dialog.findViewById(R.id.button_gallery_dare);

            dare.setDareText(databaseGame.getTextPlaceholder1());
            dareQuestionTextView.setText(dare.getDareText());

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

            buttonPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        File imageFile = null;
                        File pictureDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        String pictureName = "imagePlaceholder1.jpg";
                        imageFile = new File(pictureDirectory, pictureName);
                        Log.d("buttonPhotoOnClick", "The directory is: " + pictureDirectory);

                        if (imageFile != null) {
                            pictureUri = FileProvider.getUriForFile(getContext(), "com.example.android.fileprovider", imageFile);
                            Log.d("buttonPhotoOnClick", "The URI is: " + pictureUri);
                            dareImageLocation = pictureUri.getPath();
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }

                    dismiss();
                }
            });

            buttonGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    dismiss();
                }
            });

        } else if (isItMyTurn == false && amIRequestedAdditionalAction == true) {
            dialog.setContentView(R.layout.dare_dialog_2nd_active_frame);
            dareQuestionTextView = (TextView) dialog.findViewById(R.id.dare_question_text_view);
            darePhotoImageView = (ImageView) dialog.findViewById(R.id.dare_picture_image_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_dare);

            dare.setDareText(databaseGame.getTextPlaceholder1());
            dareQuestionTextView.setText(dare.getDareText());

            final StorageReference storageReferenceImage = mStorageRef.child(databaseGame.getGameId()).child("imagePlaceholder1.jpg");
            File localImageFile = null;
            try {
                localImageFile = File.createTempFile("image", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            final File finalLocalImageFile = localImageFile;
            storageReferenceImage.getFile(localImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    darePhotoImageView.setImageBitmap(BitmapFactory.decodeFile(finalLocalImageFile.getPath()));
                    finalLocalImageFile.delete();
                }
            });

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

                    storageReferenceImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dismiss();
                            getActivity().finish();
                        }
                    });
                }
            });

        } else if ((isItMyTurn == false && amIRequestedAdditionalAction == false) || (isItMyTurn == true && isRemotePlayerRequestedAdditionalAction == true)) {
            dialog.setContentView(R.layout.dare_dialog_passive_frame);
            dareQuestionTextView = (TextView) dialog.findViewById(R.id.dare_question_text_view);
            darePhotoImageView = (ImageView) dialog.findViewById(R.id.dare_picture_image_view);

            if ((databaseGame.getTextPlaceholder1() != null) && (!(databaseGame.getTextPlaceholder1().equals(" ")))) {
                dare.setDareText(databaseGame.getTextPlaceholder1());
                dareQuestionTextView.setText(dare.getDareText());
            } else {
                dareQuestionTextView.setText("Waiting for dare...");
            }

            if ((databaseGame.getTextPlaceholder2() != null) && (!(databaseGame.getTextPlaceholder2().equals(" ")))) {
                StorageReference storageReferenceImage = mStorageRef.child(databaseGame.getGameId()).child("imagePlaceholder1.jpg");
                File localImageFile = null;
                try {
                    localImageFile = File.createTempFile("image", "jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final File finalLocalImageFile = localImageFile;
                storageReferenceImage.getFile(localImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        darePhotoImageView.setImageBitmap(BitmapFactory.decodeFile(finalLocalImageFile.getPath()));
                        finalLocalImageFile.delete();
                    }
                });
            } else {
                darePhotoImageView.setImageDrawable(getResources().getDrawable(R.drawable.placeholder_image));
            }

        } else
            dialog.setContentView(R.layout.dare_dialog_passive_frame);
        buttonClose = (FloatingActionButton) dialog.findViewById(R.id.button_dialog_dare_close);

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
