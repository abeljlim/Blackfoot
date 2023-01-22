package com.example.emptyactivityapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class LearnActivity extends AppCompatActivity {

    private Context context;
    int difficultyValue;
    ArrayList<String> lines;
    int currLineNum;
    ImageView learnCanvasView;
    ImageView learnDisplayedImageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        this.context = this;

        // Draw the first image at a random position on the canvas.
        // Get an image to draw and simultaneously play a sound based on a random selection of words in the CSV file.
        //lines = new ArrayList<>();
        difficultyValue = 3;
        currLineNum = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            difficultyValue = extras.getInt("numToLearn"); // If ChatGPT is correct, then if this isn't set, then a NullPointerException will be thrown and I could do a try and catch for this in that case ... well, ChatGPT was not correct. It just returned 0 if the key wasn't found.
            //The key argument here must match that used in the other activity
            Log.e(TAG, "difficultyValue: "+difficultyValue);
            //difficultyValue = extras.getInt("numToLearn2");
            //Log.e(TAG, "difficultyValue (on reassignment to extras.getInt(\"numToLearn2\")): "+difficultyValue);
            lines = (ArrayList<String>) extras.getSerializable("CSVlines");
        }
        // Randomly select a word from the lines ArrayList, which contains an ArrayList of words
        //Collections.shuffle(lines);

        // Do the rest after the activity has loaded
        View myView = findViewById(R.id.learnLayout);
        myView.post(new Runnable() {
            @Override
            public void run() {
                // Get the image to display
                learnCanvasView = findViewById(R.id.learnCanvasImgView);
                learnCanvasView.setVisibility(View.VISIBLE);
                doItem();
            }
        });

    }

    public void doItem() {
        if(currLineNum == difficultyValue-1) { // If the last item, then change the label of the button to "Finish"
            Button nextButton = findViewById(R.id.button4);
            nextButton.setText("Finish");
        }

        String CSVline = lines.get(currLineNum++);

        // Play the sound
        MainActivity.playSound(context, CSVline);

        // Get the image to display
        learnCanvasView = findViewById(R.id.learnCanvasImgView);

        // Get image
        learnDisplayedImageView = findViewById(R.id.learnDisplayedImageView);
        learnDisplayedImageView.setImageResource(context.getResources().getIdentifier(CSVline, "drawable", context.getPackageName()));
        int[][][] displayedImage = cmpt120image.getImage_blackWhiteBG(learnDisplayedImageView);

        int[][][] canvasData = cmpt120image.getWhiteImage(learnCanvasView.getWidth()/MainActivity.CANVAS_IMAGE_SCALE, learnCanvasView.getHeight()/MainActivity.CANVAS_IMAGE_SCALE);
        Random rand = new Random();
        //Log.i(TAG,"learnDisplayedImageView.getWidth(): "+learnDisplayedImageView.getWidth()+"; learnDisplayedImageView.getHeight(): "+learnDisplayedImageView.getHeight());
        int randPosnCol = /*rand.nextInt(learnCanvasView.getWidth() - learnDisplayedImageView.getWidth())*/rand.nextInt((canvasData[0].length - displayedImage[0].length + 1 /*e.g. canvasCols being 2 and displayedImageCols being 1, the range would be 0 inclusive to 2 exclusive or IOW, 0 inclusive to 1 inclusive*/)); // The non-bitmap versions of the images don't seem to have the same dimensions as the bitmap versions, so I'm using the canvas dimensions instead.
        int randPosnRow = /*rand.nextInt(learnCanvasView.getHeight() - learnDisplayedImageView.getHeight())*/rand.nextInt((canvasData.length - displayedImage.length + 1));

        //Log.i(TAG,"canvasData cols: "+learnCanvasView.getWidth()/MainActivity.CANVAS_IMAGE_SCALE+"; canvasData rows: "+learnCanvasView.getHeight()/MainActivity.CANVAS_IMAGE_SCALE);
        //Log.i(TAG,"randPosnCol: "+randPosnCol+"; randPosnRow: "+randPosnRow+";");
        Draw.drawItem_whiteBG(canvasData, displayedImage, randPosnRow, randPosnCol);
        cmpt120image.renderImageToView_whiteBG(canvasData, learnCanvasView);
    }

    public void nextItem(View v) {
        // Get the next image to draw and simultaneously play a sound based on a random selection of words in the CSV file.
        if (currLineNum < difficultyValue) {
            doItem();
        }
        else {
            // Return to the main menu; in other words, finish
            Intent intentLearn = new Intent(LearnActivity.this ,
                    MainActivity.class);
            intentLearn.putExtra("numToLearn", difficultyValue);
            LearnActivity.this.startActivity(intentLearn);
        }
    }
}