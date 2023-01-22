package com.example.emptyactivityapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

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
    EditText editTextNumber;
    TextView promptTextView;
    int maxNumToLearn;
    final int MIN_NUMTOLEARN = 3; // A hard-coded value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //playSound(context, "apples");
        editTextNumber = (EditText)findViewById(R.id.editTextNumToLearnNumber);
        promptTextView = findViewById(R.id.textView);
        maxNumToLearn = 3;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.e(TAG, String.valueOf(extras.getInt("numToLearn", 3)));
            editTextNumber.setText(String.valueOf(extras.getInt("numToLearn", 3)));
            maxNumToLearn = extras.getInt("maxNumToLearn", 3);
        }
        promptTextView.setText("Set the number of words to be learned ("+MIN_NUMTOLEARN+"-"+maxNumToLearn+"): ");

        editTextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        /*
        editTextNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // When focus is lost check that the text field has valid values.

                if (!hasFocus) {
                    // Validate youredittext
                }
            }
        });
         */
    }


    public void backToMenu(View v) {
        if(editTextNumber.getText().toString().length() == 0) { // If not a valid numToLearn, then do nothing
            return;
        }

        // Cap the number of editTextNumber's field to be maxNumToLearn
        if(editTextNumber.getText().toString().length() > 0) {
            int editTextNumberValue = Integer.parseInt(editTextNumber.getText().toString());
            if (editTextNumberValue > maxNumToLearn) {
                editTextNumber.setText(String.valueOf(maxNumToLearn));
            }
            else if(editTextNumberValue < MIN_NUMTOLEARN) {
                editTextNumber.setText(String.valueOf(MIN_NUMTOLEARN));
            }
        }

        Intent intentSettings = new Intent(SettingsActivity.this ,
                MainActivity.class);
        intentSettings.putExtra("numToLearn", Integer.parseInt(editTextNumber.getText().toString()));
        SettingsActivity.this.startActivity(intentSettings);
        //Log.i("Content "," Main layout ");
        //playSound(context, "apples");
    }
}