package com.example.emptyactivityapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private Context context;
    ImageView displayedImageView;
    ImageView canvasView;
    int currRound;
    int totalRounds;
    int difficultyValue;
    int currLineNum;
    ArrayList<String> lines, challengeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        context = this;

        currRound = 1;
        difficultyValue = 3;
        currLineNum = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            difficultyValue = extras.getInt("numToLearn");
            Log.e(TAG, "difficultyValue: "+difficultyValue);
            lines = (ArrayList<String>) extras.getSerializable("CSVlines");
        }


    }

    public void playStart(View v) {
        // Hide the stuff on start
        TextView playTextView1 = findViewById(R.id.textView2);
        TextView playTextView2 = findViewById(R.id.textView3);
        EditText editTextRoundsNumber = findViewById(R.id.editTextRoundsNumber);
        Button playStartButton = findViewById(R.id.playStartButton);

        if(editTextRoundsNumber.getText().toString().length() == 0) { // If not a valid number of rounds, then don't do anything
            return;
        }

        playTextView1.setVisibility(View.INVISIBLE);
        playTextView2.setVisibility(View.INVISIBLE);
        editTextRoundsNumber.setVisibility(View.INVISIBLE);
        playStartButton.setVisibility(View.INVISIBLE);

        // Set the number of total rounds
        totalRounds = Integer.parseInt(editTextRoundsNumber.getText().toString());

        // Do the canvas stuff for the first presentation
        doRound();

        // Show the stuff on start
        TextView playStartPrompt = findViewById(R.id.playStartPrompt);
        EditText playStartInputNumber = findViewById(R.id.playStartInputNumber);
        Button playStartSubmitButton = findViewById(R.id.playStartSubmitButton);
        ImageView playStartCanvasImgView = findViewById(R.id.playStartCanvasImgView);
        playStartPrompt.setVisibility(View.VISIBLE);
        playStartInputNumber.setVisibility(View.VISIBLE);
        playStartSubmitButton.setVisibility(View.VISIBLE);
        playStartCanvasImgView.setVisibility(View.VISIBLE);
    }

    public void doRound() {
        // Set up the challenge list, which is easier than using the lines list and it's what was specified in the assignment...
        challengeList = new ArrayList<>(lines.subList(0,difficultyValue));
        Collections.shuffle(challengeList);
        challengeList = new ArrayList<>(challengeList.subList(0,3));

        Random rand = new Random();
        ImageView canvasView = findViewById(R.id.playStartCanvasImgView);

        int[][][] currCanvas = cmpt120image.getWhiteImage(canvasView.getWidth()/MainActivity.CANVAS_IMAGE_SCALE, canvasView.getHeight()/MainActivity.CANVAS_IMAGE_SCALE);
        for(String challengeItem : challengeList) {
            int n = rand.nextInt(4)+1; // # of the item to be displayed
            int[][][] challengeItemImg =  cmpt120image.getImage(context, challengeItem);
            challengeItemImg = Draw.recolorImage_blackOrigImgWhiteBG(challengeItemImg, new int[]{rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)});
            boolean toMinify = rand.nextBoolean();
            if(toMinify) {
                challengeItemImg = Draw.minify_whiteBG(challengeItemImg);
            }
            boolean toMirror = rand.nextBoolean();
            if(toMirror) {
                challengeItemImg = Draw.mirror(challengeItemImg);
            }
            Draw.distributeItems(currCanvas, challengeItemImg, n);
        }

        cmpt120image.renderImageToView_whiteBG(currCanvas, canvasView);
        //cmpt120image.showImage(pixels);
        //int[][][] recoloredChildImg = Draw.recolorImage(childImg, new int[]{255, 0, 0});
    }
}