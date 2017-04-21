package com.parthmehta.cat_mouse_elephant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.View.*;

/**
 * full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class GameCenterActivity extends AppCompatActivity {

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int numberOfWins, numberOfLosses;

    // Initialize the required variables.
    private ImageView computerAnimal, playerAnimal;
    private ImageButton catButton, mouseButton, elephantButton;
    private TextView winnerLabel, playerScoreLabel, computerScoreLabel, gameOverLabel;
    private RelativeLayout gameOverLayout, gameLayout;   //to hide and show layouts.
    private Button resetButton, quitButton2;
    private MediaPlayer roundMusic, winMusic, loseMusic; //for Media
    int computerWins = 0; //to count computer score.
    int playerWins = 0;   //to count user score.

    //Listener from the template
    MyOnClickListener myOnClickListener = new MyOnClickListener();

    //Listener for the quit button
    QuitButtonClickListener quitButtonClickListener = new QuitButtonClickListener();

    //Listener for the reset button
    ResetButtonClickListener resetButtonClickListener = new ResetButtonClickListener();


    // ------------------ this code is auto-generated for full-screen activity ---------------------------

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(SYSTEM_UI_FLAG_LOW_PROFILE
                    | SYSTEM_UI_FLAG_FULLSCREEN
                    | SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final OnTouchListener mDelayHideTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    //-------------------------- code for full-screen activiy ends ---------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_game_center);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        settings = getSharedPreferences("winsAndLosses", Context.MODE_PRIVATE);
        editor = settings.edit();

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });


        //variables for various sounds.
        roundMusic = MediaPlayer.create(getApplicationContext(),R.raw.round_sound);
        winMusic = MediaPlayer.create(getApplicationContext(),R.raw.win_sound);
        loseMusic = MediaPlayer.create(getApplicationContext(),R.raw.lose_sound);

        //layouts to hide and show when game is over.
        gameOverLayout = (RelativeLayout) this.findViewById(R.id.gameOverLayout);
        gameLayout = (RelativeLayout) this.findViewById(R.id.gameLayout);

        //The 3 animal buttons.
        catButton = (ImageButton) findViewById(R.id.catButton);
        mouseButton = (ImageButton) findViewById(R.id.mouseButton);
        elephantButton = (ImageButton) findViewById(R.id.elephantButton);

        //Reset and Quit buttons.
        resetButton = (Button) findViewById(R.id.restartButton);
        quitButton2 = (Button) findViewById(R.id.quitButton2);

        //The holder for images displayed in a face-off, they are initially empty.
        computerAnimal = (ImageView) findViewById(R.id.computerImageView);
        playerAnimal = (ImageView) findViewById(R.id.playerImageView);

        //Game Labels.
        winnerLabel = (TextView) findViewById(R.id.winnerLabel);
        playerScoreLabel = (TextView) findViewById(R.id.playerScoreLabel);
        computerScoreLabel = (TextView) findViewById(R.id.computerScoreLabel);
        gameOverLabel = (TextView) findViewById(R.id.gameOverLabel);

        //Listeners to handle events when an animal is selected.
        catButton.setOnClickListener(myOnClickListener);
        mouseButton.setOnClickListener(myOnClickListener);
        elephantButton.setOnClickListener(myOnClickListener);

        //Listeners for reset and quit buttons.
        resetButton.setOnClickListener(resetButtonClickListener);
        quitButton2.setOnClickListener(quitButtonClickListener);

    }

    //Event handler for Quit button
    private class QuitButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            //Quit app by calling the home screen activity
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    //Event handler for reset button
    private class ResetButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            //Call the MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


    //From the template, with a few changes.
    private class MyOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {

            // get a random number form 1 to 3
            int rand = (int) (Math.random() * 3 + 1);

            switch (rand) {
                /**
                 * rand = 1 means computer selects cat,
                 * 2 means computer selects mouse,
                 * 3 means computer selects elephant
                 */

                /*
                * computerWins counts computerScore
                * playerWins counts playerScore
                * winnerLabel is the label that displays who won the round
                * */
                case 1:
                    computerAnimal.setImageResource(R.drawable.cat);  //computer choose Cat
                    roundMusic.start();
                    switch (v.getId()) {
                        case R.id.catButton:   //player choose cat
                            playerAnimal.setImageResource(R.drawable.cat);
                            winnerLabel.setText("No Winner");
                            break;
                        case R.id.mouseButton:  //player choose Paper
                            playerAnimal.setImageResource(R.drawable.mouse);
                            computerWins++;
                            winnerLabel.setText("Computer Wins");
                            computerScoreLabel.setText(String.valueOf(computerWins));
                            break;
                        case R.id.elephantButton:  //player choose Scissors
                            playerAnimal.setImageResource(R.drawable.elephant);
                            playerWins++;
                            winnerLabel.setText("Player Wins");
                            playerScoreLabel.setText(String.valueOf(playerWins));
                            break;
                    }
                    break;
                case 2:
                    computerAnimal.setImageResource(R.drawable.mouse);  //computer choose Mouse
                    roundMusic.start();
                    switch (v.getId()) {
                        case R.id.catButton:
                            playerAnimal.setImageResource(R.drawable.cat);
                            playerWins++;
                            winnerLabel.setText("Player Wins");
                            playerScoreLabel.setText(String.valueOf(playerWins));
                            break;
                        case R.id.mouseButton:
                            playerAnimal.setImageResource(R.drawable.mouse);
                            winnerLabel.setText("No Winner");
                            break;
                        case R.id.elephantButton:
                            playerAnimal.setImageResource(R.drawable.elephant);
                            computerWins++;
                            winnerLabel.setText("Computer Wins");
                            computerScoreLabel.setText(String.valueOf(computerWins));
                            break;
                    }
                    break;
                case 3:
                    computerAnimal.setImageResource(R.drawable.elephant);  //computer choose Elephant
                    roundMusic.start();
                    switch (v.getId()) {
                        case R.id.catButton:
                            playerAnimal.setImageResource(R.drawable.cat);
                            computerWins++;
                            winnerLabel.setText("Computer Wins");
                            computerScoreLabel.setText(String.valueOf(computerWins));
                            break;
                        case R.id.mouseButton:
                            playerAnimal.setImageResource(R.drawable.mouse);
                            playerWins++;
                            winnerLabel.setText("Player Wins");
                            playerScoreLabel.setText(String.valueOf(playerWins));
                            break;
                        case R.id.elephantButton:
                            playerAnimal.setImageResource(R.drawable.elephant);
                            winnerLabel.setText("No Winner");
                            break;
                    }
                    break;

            }

            /* If computer wins, play losing sound and show YOU LOSE */
            if(computerWins==5){
                gameOverLabel.setText("YOU LOSE");
                gameOverLayout.setVisibility(VISIBLE);
                gameLayout.setVisibility(INVISIBLE);
                loseMusic.start();

                //Update the number of losses.
                numberOfLosses = settings.getInt("numberOfLosses", 0);
                editor.putInt("numberOfLosses", numberOfLosses+1);

                // Commit the updates!
                editor.commit();

            }

            /* If user wins, play winning sound and show YOU WIN */
            else if(playerWins==5){
                gameOverLabel.setText("YOU WIN");
                gameOverLayout.setVisibility(VISIBLE);
                gameLayout.setVisibility(INVISIBLE);
                winMusic.start();

                //Update the number of wins.
                numberOfWins = settings.getInt("numberOfWins", 1);
                editor.putInt("numberOfWins", numberOfWins+1);

                // Commit the updates!
                editor.commit();
            }
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
