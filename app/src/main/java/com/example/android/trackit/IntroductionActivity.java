package com.example.android.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.trackit.introduction_fragments.IntroductionFragmentOne;
import com.example.android.trackit.introduction_fragments.IntroductionFragmentThree;
import com.example.android.trackit.introduction_fragments.IntroductionFragmentTwo;
import com.google.android.material.tabs.TabLayout;

/**
 * This class is responsible for displaying an introduction wizard where the user can swipe horizontally through the steps
 * that demonstrate how to use the TrackIt App
 */
public class IntroductionActivity extends AppCompatActivity {

     //Declaring and initialing a constant that represents the number of pages (wizard steps) to be displayed.
    private static final int NUM_PAGES = 3;

    //Declaring the ViewPager widget, which handles animation and allows swiping horizontally to access previous
    // and next wizard steps.

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        //Initializing the mViewPager Object Variable
        mViewPager = findViewById(R.id.pager);

        // the pagerAdapter, which provides the pages to the ViewPager widget.
        SlideAdapter pagerAdapter = new SlideAdapter(getSupportFragmentManager());

        // Declaring and initializing the tabLayout and the nextButton object variables.
        TabLayout tabLayout = findViewById(R.id.indicator);
        Button nextButton = findViewById(R.id.next_button);

        //Setting the Adapter to the ViewPager through the setAdapter method
        mViewPager.setAdapter(pagerAdapter);

        // Setting up the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(mViewPager);

        //Attaching an OnClickListener to the nextButton that determines the behavior that will happen
        // when the user clicks on that button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishBoarding();
            }
        });
    }

    /**
     * finishBoarding() method saves the state of whether the user has finished the introduction wizard onBoarding or not
     * and then redirects the user to the MainActivity.
     */

    private void finishBoarding() {
        //Get the sharedPreferences by calling getSharedPreferences method that returns a SharedPreference instance
        // pointing to the file that contains the values of preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        //Save data changes in SharedPreferences by calling edit() method of SharedPreferences class which returns Editor class object.
        //call putBoolean method to save a boolean value in a preference editor and Set onBoardingComplete to true

        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putBoolean("onBoardingComplete", true).apply();

        // Launch the main Activity, called MainActivity
        Intent intent = new Intent(IntroductionActivity.this, SignUpActivity.class);
        startActivity(intent);

        // Close the IntroductionActivity so that when user presses the back, he/she won't go back to this introduction wizard
        finish();

    }

    /**
     * OnBackPressed() is called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    /**
     * This Class provides the Adapter to populate pages inside of the ViewPager
     * The pages represent 3 FragmentOne objects, in sequence.
     */
    private class SlideAdapter extends FragmentStatePagerAdapter {

        public SlideAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        /**
         *This method gets the fragment at the specified position and returns it to be displayed
         * @param position int: the position of the Fragment in the pager.
         * @return Fragment: returns the Fragment associated with a specified position.
         */

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 1 - This will show First Introduction Fragment
                    return new IntroductionFragmentOne();
                case 1: // Fragment # 2 - This will show Second Introduction Fragment
                    return new IntroductionFragmentTwo();
                case 2:  // Fragment # 3 - This will show the Third Introduction Fragment
                    return new IntroductionFragmentThree();
                default:
                    return null;
            }

        }

        /**
         * This method counts the number of views/fragments that are in the data set represented by this Adapter.
         * @return int the number of views available, in this case 3 Fragment objects
         */

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
