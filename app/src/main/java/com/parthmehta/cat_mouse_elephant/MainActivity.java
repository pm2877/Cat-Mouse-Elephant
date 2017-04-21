package com.parthmehta.cat_mouse_elephant;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Initialize all variables required
    private Button playButton, quitButton, backButton, aboutButton;
    private ImageView imageViewQuestion;
    private MediaPlayer gameMusic;
    private TextView scoreText;
    private RelativeLayout menuLayout, aboutLayout;

    playBtnClickListener playBtnClick = new playBtnClickListener();
    QuitButtonClickListener quitButtonClickListener = new QuitButtonClickListener();
    AboutButtonClickListener aboutButtonClickListener = new AboutButtonClickListener();
    BackButtonClickListener backButtonClickListener = new BackButtonClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("winsAndLosses", 0);
        int numberOfWins = settings.getInt("numberOfWins", 1);
        int numberOfLosses = settings.getInt("numberOfLosses", 0);

        scoreText = (TextView)findViewById(R.id.scoreText);
        scoreText.setText("%Win: " + numberOfWins*100/(numberOfWins + numberOfLosses));

        //get all views using their IDs
        playButton = (Button)findViewById(R.id.buttonPlay);
        quitButton = (Button)findViewById(R.id.buttonQuit);
        backButton = (Button)findViewById(R.id.backButton);
        aboutButton = (Button)findViewById(R.id.buttonAbout);

        menuLayout = (RelativeLayout)findViewById(R.id.menuLayout);
        aboutLayout = (RelativeLayout)findViewById(R.id.aboutLayout);

        imageViewQuestion = (ImageView)findViewById(R.id.imageViewQuestion);

        //Set onClick Listeners to perform tasks when buttons are clicked.
        playButton.setOnClickListener(playBtnClick);
        quitButton.setOnClickListener(quitButtonClickListener);
        aboutButton.setOnClickListener(aboutButtonClickListener);
        backButton.setOnClickListener(backButtonClickListener);

        //Initialize media object with the media resource file
        gameMusic = MediaPlayer.create(getApplicationContext(),R.raw.game_start);

        //Animate the play Button by scaling it along X and Y axes, in 3000 milli-seconds.
        playButton.animate().scaleX(1.35f).setDuration(3000);
        playButton.animate().scaleY(1.35f).setDuration(3000);

        //Animate the Question-mark image by Rotating it 3600 degrees in 20000 milli-seconds
        imageViewQuestion.animate().rotationBy(3600f).setDuration(20000);

        //start the game music here.
        gameMusic.start();
        //Repeat the game music continuously.
        gameMusic.setLooping(true);
    }

    //Click Listener for the about Button
    private class AboutButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //Make the menu invisible and make the about text visible.
            menuLayout.setVisibility(View.INVISIBLE);
            aboutLayout.setVisibility(View.VISIBLE);
        }
    }

    //click listener for the back button
    private class BackButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //Make the about text invisible and the menu visible.
            aboutLayout.setVisibility(View.INVISIBLE);
            menuLayout.setVisibility(View.VISIBLE);
        }
    }

    // Click listener for the Quit Button
    private class QuitButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //Stop the game music to avoid playing it after app is quit.
            gameMusic.stop();

            //Quit the app by calling the intent for the Home Screen.
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    //Listener for the play button
    private class playBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //stop the music.
            gameMusic.stop();

            //Call the GameCenterActivity
            Intent intent = new Intent(getApplicationContext(), GameCenterActivity.class);
            startActivity(intent);
        }
    }
}
