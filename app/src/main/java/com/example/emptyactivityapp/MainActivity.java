package com.example.emptyactivityapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static MediaPlayer mediaPlayer;

    public static void playSound(Context context, Uri soundFilename) {
        // Play sound using pygame mixer
        mediaPlayer = MediaPlayer.create(context, soundFilename);
        mediaPlayer.start();
    }

    public static void playSound(Context context, String soundFilename) {
        int soundID = context.getResources().getIdentifier(soundFilename, "raw", context.getPackageName());

        // Create a MediaPlayer object and initialize it with the sound resource file
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundID);

        // Start playing the sound
        mediaPlayer.start();
    }
    private Context context;
    ImageView displayedImageView;
    ImageView canvasView;

    // Passed between activities
    ArrayList<String> lines;
    int difficultyValue;

    public static final int CANVAS_IMAGE_SCALE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String languageCSV = "blackfoot";

        // Get any Extras from SettingsActivity of the variable for counting the number of words to learn


        this.context = this;

        // Test code
        /*
        int[][][] childImg = new int[0][][];
        displayedImageView = findViewById(R.id.imageView17);

        displayedImageView.setImageResource(context.getResources().getIdentifier("apples", "drawable", context.getPackageName()));
        ConstraintLayout.LayoutParams displayedImageLayoutParams = (ConstraintLayout.LayoutParams)displayedImageView.getLayoutParams();
        displayedImageLayoutParams.verticalBias = 0.5f; // Set the vertical bias to 0.5 (middle of the parent)
        displayedImageView.setLayoutParams(displayedImageLayoutParams);

        childImg = cmpt120image.getImage(displayedImageView);
        childImg = cmpt120image.getImage(context, "eggs");
        //cmpt120image.showImage(pixels);
        int[][][] testCanvas = cmpt120image.getWhiteImage(300, 537);
        Draw.distributeItems(testCanvas, childImg, 4);
        int[][][] recoloredChildImg = Draw.recolorImage(childImg, new int[]{255, 0, 0});
        int[][][] minifiedChildImg = Draw.minify(childImg);
        int[][][] mirroredChildImg = Draw.mirror(childImg);
        Draw.drawItem(testCanvas, recoloredChildImg, 0, 0);
        //Draw.drawItem(testCanvas, minifiedChildImg, 25, 25);
        Draw.drawItem(testCanvas, mirroredChildImg, 200, 25);
        canvasView = findViewById(R.id.canvasImgView);
        cmpt120image.renderImageToView(testCanvas, canvasView);
        */
        // End test code

        /*View myView = findViewById(R.id.activity_main);
        myView.post(new Runnable() {
            @Override
            public void run() {
                int width = myView.getWidth();
                int height = myView.getHeight();
                // use the width and height values here
                Log.e(TAG, "ConstraintLayout dimensions: "+width+","+height);
            }
        });*/
        //int width = myView.getWidth();
        //int height = myView.getHeight();
        InputStream inputStream = /*getResources().openRawResource(R.raw.blackfoot);*/getResources().openRawResource(/*resourceID*/context.getResources().getIdentifier(languageCSV, "raw", context.getPackageName()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        lines = new ArrayList<>();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                Log.e(TAG, "line: "+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //playSound(context, "apples");

        // Get difficulty value (number of words to learn)
        difficultyValue = 3; // Default value
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            difficultyValue = extras.getInt("numToLearn"); // If ChatGPT is correct, then if this isn't set, then a NullPointerException will be thrown and I could do a try and catch for this in that case ... well, ChatGPT was not correct. It just returned 0 if the key wasn't found.
            //The key argument here must match that used in the other activity
            Log.e(TAG, "difficultyValue: "+difficultyValue);
            //difficultyValue = extras.getInt("numToLearn2");
            //Log.e(TAG, "difficultyValue (on reassignment to extras.getInt(\"numToLearn2\")): "+difficultyValue);
        }
    }

    public void change(View v){
        displayedImageView.setImageResource(R.drawable.child);
    }

    public void learn(View v) {

        // doStuff
        Intent intentMain = new Intent(MainActivity.this ,
                LearnActivity.class);
        intentMain.putExtra("CSVlines", lines);
        intentMain.putExtra("numToLearn", difficultyValue);
        MainActivity.this.startActivity(intentMain);
        Log.i("Content "," Main layout ");
        //playSound(context, "apples");
    }

    public void settings(View v) {
        Intent intentMain = new Intent(MainActivity.this ,
                SettingsActivity.class);
        intentMain.putExtra("numToLearn", difficultyValue);
        int maxNumToLearn = lines.size();
        intentMain.putExtra("maxNumToLearn", maxNumToLearn);
        MainActivity.this.startActivity(intentMain);
    }
    public void play(View v) {

        Intent intentMain = new Intent(MainActivity.this ,
                PlayActivity.class);
        intentMain.putExtra("CSVlines", lines);
        intentMain.putExtra("numToLearn", difficultyValue);
        MainActivity.this.startActivity(intentMain);
    }
}