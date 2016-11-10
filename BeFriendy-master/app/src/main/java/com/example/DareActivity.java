package com.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DareActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 10;
    TextView dareQuestion;
    Button takePhoto;
    Button submitAnswer;
    Button declineButton;
    ImageView darePictureTaken;
    Bitmap cameraImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dare);

        darePictureTaken = (ImageView) findViewById(R.id.dareImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dareQuestion = (TextView) findViewById(R.id.dare_question);
        takePhoto = (Button) findViewById(R.id.take_photo_button);
        submitAnswer = (Button) findViewById(R.id.submit_picture);
        declineButton = (Button) findViewById(R.id.decline_dare_button);


        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                cameraImage = (Bitmap) data.getExtras().get("data");
                darePictureTaken.setImageBitmap(cameraImage);
            }
        }
    }
}