package com.example.android.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * This activity class displays the first startup screen which appears when TrackIt App is opened.
 * It is a simple constant screen for a fixed amount of time which is used to display the App Logo
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Declaring and initializing splashTimeOut variable which represents the amount of time that the splash screen will be displayed.
        int splashTimeOut = 3000;

        //Set time to handler and call Handler().postDelayed, it will call run method of runnable after set time and redirect either to
        // the IntroductionActivity or SignUpActivity based on whether the introWizard onBoarding is complete or not.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Get reference of SharedPreferences object by calling getSharedPreferences (String name,int mode).
                // This returns a SharedPreference instance pointing to the file "MyPrefs" that contains the values of preferences.
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

               // Check if onBoardingComplete from the IntroductionActivity is complete or not
                if (!sharedPreferences.getBoolean("onBoardingComplete", false)) {

                    //if onBoardingComplete is false then the user did not finish up the introWizard and we will
                    // redirect the user to the introductionActivity
                    Intent homeIntent = new Intent(SplashActivity.this, IntroductionActivity.class);
                    startActivity(homeIntent);
                    // close this activity
                    finish();

                } else{
                    //if onBoardingComplete is true or complete then redirect the user to the SignUpActivity
                    Intent homeIntent = new Intent(SplashActivity.this, SignUpActivity.class);
                    startActivity(homeIntent);
                    finish();

                } }}
                , splashTimeOut);

        // Declaring and initializing the animation and logo object variables
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.transition_animation);
        ImageView logo = findViewById(R.id.logo);

        //setting and assigning the animation object to the logo ImageView object so that the logo is displayed with this animation.
        logo.setAnimation(animation);

    }

}



