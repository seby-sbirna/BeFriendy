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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.EssentialClasses.DatabaseGame;
import com.example.EssentialClasses.Player;
import com.example.EssentialClasses.Wildcard;
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


public class WildcardDialogFragment extends DialogFragment {
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 1567;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://befriendy-e8f50.appspot.com");
    private Wildcard wildcard;
    private DatabaseGame databaseGame;
    private boolean isItMyTurn;
    private boolean amIPlayer1;
    private boolean amIRequestedAdditionalAction;
    private boolean isRemotePlayerRequestedAdditionalAction;
    private Uri pictureUri;
    private String wildcardImageLocation;

    private FloatingActionButton buttonClose;

    private EditText wildcardQuestionEditText;
    private TextView wildcardQuestionTextView;
    private EditText answerWildcardEditText;

    private Button buttonDecline;
    private Button buttonSubmit;

    private Button buttonPhoto;
    private Button buttonGallery;

    private ImageView wildcardPhotoImageView;

    private TextView answerWildcardTextView;

    private RadioGroup choicesRadioGroup;
    private String radioButtonChoiceString = "text";

    private Button buttonConfirm;

    public WildcardDialogFragment() {
        // Required empty public constructor
    }

    public static WildcardDialogFragment newInstance(String gameId, int positionOnTheBoard, boolean isItMyTurn, boolean amIPlayer1, boolean amIRequestedAdditionalAction, boolean isRemotePlayerRequestedAdditionalAction) {
        WildcardDialogFragment fragment = new WildcardDialogFragment();

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
            wildcard = new Wildcard(getArguments().getInt("position"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//      dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (isItMyTurn == true && amIRequestedAdditionalAction == false && isRemotePlayerRequestedAdditionalAction == false) {
            dialog.setContentView(R.layout.wildcard_dialog_1st_active_frame);
            wildcardQuestionEditText = (EditText) dialog.findViewById(R.id.question_wildcard_edit_text);
            buttonDecline = (Button) dialog.findViewById(R.id.button_decline_wildcard);
            buttonSubmit = (Button) dialog.findViewById(R.id.button_submit_wildcard);
            choicesRadioGroup = (RadioGroup) dialog.findViewById(R.id.choices_radio_group);

            choicesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_button_text:
                            radioButtonChoiceString = "text";
                            break;
                        case R.id.radio_button_photo:
                            radioButtonChoiceString = "photo";
                            break;
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

            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String writtenText = wildcardQuestionEditText.getText().toString();
                    if (writtenText.isEmpty() || writtenText.equals(" ") || writtenText.equals("  ") || writtenText.equals("   ")) {
                        Toast.makeText(getContext(), "Please type an answer before submitting!", Toast.LENGTH_LONG).show();
                    } else {
                        wildcard.setQuestion(writtenText);
                        databaseGame.setTextPlaceholder1(wildcard.getQuestion());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder1").setValue(databaseGame.getTextPlaceholder1());

                        databaseGame.setTextPlaceholder2(radioButtonChoiceString);
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

        } else if (databaseGame.getTextPlaceholder2().equals("text") && isItMyTurn == false && amIRequestedAdditionalAction == true) {
            dialog.setContentView(R.layout.wildcard_dialog_text_2nd_active_frame);
            wildcardQuestionTextView = (TextView) dialog.findViewById(R.id.wildcard_question_text_view);
            answerWildcardEditText = (EditText) dialog.findViewById(R.id.answer_wildcard_edit_text);
            buttonSubmit = (Button) dialog.findViewById(R.id.button_submit_wildcard);
            buttonDecline = (Button) dialog.findViewById(R.id.button_decline_wildcard);

            wildcard.setQuestion(databaseGame.getTextPlaceholder1());
            wildcardQuestionTextView.setText(wildcard.getQuestion());

            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String writtenText = answerWildcardEditText.getText().toString();
                    if (writtenText.isEmpty() || writtenText.equals(" ") || writtenText.equals("  ") || writtenText.equals("   ")) {
                        Toast.makeText(getContext(), "Please type an answer before submitting!", Toast.LENGTH_LONG).show();
                    } else {
                        wildcard.setAnswer(writtenText);
                        databaseGame.setTextPlaceholder3(wildcard.getAnswer());
                        mDatabase.child("games").child(databaseGame.getGameId()).child("textPlaceholder3").setValue(databaseGame.getTextPlaceholder3());

                        //Change the gameplay now
                        if (amIPlayer1 == true) {
                            databaseGame.setIsPlayer2AdditionalActionNeeded(true);
                            mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());

                            databaseGame.setIsPlayer1AdditionalActionNeeded(false);
                            mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());
                        } else {
                            databaseGame.setIsPlayer1AdditionalActionNeeded(true);
                            mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer1AdditionalActionNeeded").setValue(databaseGame.getIsPlayer1AdditionalActionNeeded());

                            databaseGame.setIsPlayer2AdditionalActionNeeded(false);
                            mDatabase.child("games").child(databaseGame.getGameId()).child("isPlayer2AdditionalActionNeeded").setValue(databaseGame.getIsPlayer2AdditionalActionNeeded());
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

        } else if (databaseGame.getTextPlaceholder2().equals("photo") && isItMyTurn == false && amIRequestedAdditionalAction == true) {
            dialog.setContentView(R.layout.wildcard_dialog_photo_2nd_active_frame);
            wildcardQuestionTextView = (TextView) dialog.findViewById(R.id.wildcard_question_text_view);
            buttonDecline = (Button) dialog.findViewById(R.id.button_decline_wildcard);
            buttonPhoto = (Button) dialog.findViewById(R.id.button_photo_wildcard);
            buttonGallery = (Button) dialog.findViewById(R.id.button_gallery_wildcard);

            wildcard.setQuestion(databaseGame.getTextPlaceholder1());
            wildcardQuestionTextView.setText(wildcard.getQuestion());

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
                            wildcardImageLocation = pictureUri.getPath();
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

        } else if (databaseGame.getTextPlaceholder2().equals("text") && isItMyTurn == true && amIRequestedAdditionalAction == true) {
            dialog.setContentView(R.layout.wildcard_dialog_text_3rd_active_frame);
            wildcardQuestionTextView = (TextView) dialog.findViewById(R.id.wildcard_question_text_view);
            answerWildcardTextView = (TextView) dialog.findViewById(R.id.answer_wildcard_text_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_wildcard);

            wildcard.setQuestion(databaseGame.getTextPlaceholder1());
            wildcard.setAnswer(databaseGame.getTextPlaceholder3());
            wildcardQuestionTextView.setText(wildcard.getQuestion());
            answerWildcardTextView.setText(wildcard.getAnswer());

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

        } else if (databaseGame.getTextPlaceholder2().equals("photo") && isItMyTurn == true && amIRequestedAdditionalAction == true) {
            dialog.setContentView(R.layout.wildcard_dialog_photo_3rd_active_frame);
            wildcardQuestionTextView = (TextView) dialog.findViewById(R.id.wildcard_question_text_view);
            wildcardPhotoImageView = (ImageView) dialog.findViewById(R.id.wildcard_picture_image_view);
            buttonConfirm = (Button) dialog.findViewById(R.id.button_confirm_wildcard);

            wildcard.setQuestion(databaseGame.getTextPlaceholder1());
            wildcardQuestionTextView.setText(wildcard.getQuestion());

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
                    wildcardPhotoImageView.setImageBitmap(BitmapFactory.decodeFile(finalLocalImageFile.getPath()));
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

        } else {
            if (databaseGame.getTextPlaceholder2().equals("text")) {
                dialog.setContentView(R.layout.wildcard_dialog_text_passive_frame);
                wildcardQuestionTextView = (TextView) dialog.findViewById(R.id.wildcard_question_text_view);
                answerWildcardTextView = (TextView) dialog.findViewById(R.id.answer_wildcard_text_view);

                if ((databaseGame.getTextPlaceholder1() != null) && (!(databaseGame.getTextPlaceholder1().equals(" ")))) {
                    wildcard.setQuestion(databaseGame.getTextPlaceholder1());
                    wildcardQuestionTextView.setText(wildcard.getQuestion());
                } else {
                    wildcardQuestionTextView.setText("Waiting for question...");
                }

                if ((databaseGame.getTextPlaceholder3() != null) && (!(databaseGame.getTextPlaceholder3().equals(" ")))) {
                    wildcard.setAnswer(databaseGame.getTextPlaceholder3());
                    answerWildcardTextView.setText(wildcard.getAnswer());
                } else {
                    answerWildcardTextView.setText("Waiting for answer...");
                }
            } else if (databaseGame.getTextPlaceholder2().equals("photo")) {
                dialog.setContentView(R.layout.wildcard_dialog_photo_passive_frame);
                wildcardQuestionTextView = (TextView) dialog.findViewById(R.id.wildcard_question_text_view);
                wildcardPhotoImageView = (ImageView) dialog.findViewById(R.id.wildcard_picture_image_view);

                if ((databaseGame.getTextPlaceholder1() != null) && (!(databaseGame.getTextPlaceholder1().equals(" ")))) {
                    wildcard.setQuestion(databaseGame.getTextPlaceholder1());
                    wildcardQuestionTextView.setText(wildcard.getQuestion());
                } else {
                    wildcardQuestionTextView.setText("Waiting for question...");
                }

                if ((databaseGame.getTextPlaceholder3() != null) && (!(databaseGame.getTextPlaceholder3().equals(" ")))) {
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
                            wildcardPhotoImageView.setImageBitmap(BitmapFactory.decodeFile(finalLocalImageFile.getPath()));
                            finalLocalImageFile.delete();
                        }
                    });
                }
            } else {
                dialog.setContentView(R.layout.wildcard_dialog_text_passive_frame);
                wildcardQuestionTextView = (TextView) dialog.findViewById(R.id.wildcard_question_text_view);
                answerWildcardTextView = (TextView) dialog.findViewById(R.id.answer_wildcard_text_view);

                wildcardQuestionTextView.setText("Waiting for question...");
                answerWildcardTextView.setText("Waiting for content type...");

            }
        }
        buttonClose = (FloatingActionButton) dialog.findViewById(R.id.button_dialog_wildcard_close);

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
