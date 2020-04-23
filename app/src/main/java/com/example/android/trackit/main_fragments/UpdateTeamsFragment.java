package com.example.android.trackit.main_fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.trackit.R;
import com.example.android.trackit.main_fragments.GameCounterFragment;


/**
 * UpdateTeamsFragment subclass represents the update teams' names screen which is is displayed when the user clicks
 * on Start A New Game Button in the StartGameFragment.
 */
public class UpdateTeamsFragment extends Fragment {

    //Declaring the mHomeName Object Variable
    private EditText mHomeName;

    //Declaring the mAwayName Object Variable
    private EditText mAwayName;

    public UpdateTeamsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_update_teams, container, false);

        //Initializing the mHomeName and mAwayName object variables
        mHomeName = rootView.findViewById(R.id.home_team_name_update);

        mAwayName = rootView.findViewById(R.id.away_team_name_update);

        //Declaring and Initializing the updateButton and skipButton object variables
        Button updateButton = rootView.findViewById(R.id.team_card_update_button);

        Button skipButton = rootView.findViewById(R.id.skip_button);

        //Attaching an OnClickListener to the updateButton that determines the behavior that will happen when the user
        //clicks on that button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTeams();
            }
        });

        //Attaching an OnClickListener to the skipButton that determines the behavior that will happen when the user
        // clicks on that button
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipUpdate();
            }
        });

        return rootView;
    }

    /**
     * This method updates the teams' names by getting the values entered by the user for the teams' names and passing
     * these values to the gamesCounterFragment to be displayed along with opening the gamesCounterFragment
     */
    private void updateTeams() {

        //Get the updatedHomeName and updatedAwayName values entered by the user in the EditTexts and convert them to Strings
        String updatedHomeName = mHomeName.getText().toString().trim();
        String updatedAwayName = mAwayName.getText().toString().trim();

        //If Any of the EditText Fields is empty then set an error to the EditText Field along with a Toast message
        if (TextUtils.isEmpty(updatedHomeName)) {
            mHomeName.setError("Home Name is Required");
            Toast.makeText(getActivity(), "Please Enter Home Team Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(updatedAwayName)) {
            mAwayName.setError("Away Name is required");
            Toast.makeText(getActivity(), "Please Enter Away Team Name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Fragment to be placed in the activity layout
        GameCounterFragment gameCounterFragment = new GameCounterFragment();

        //Create a bundle object to pass the updatedHomeName and updatedAwayName variables' values to the gamesCounterFragment
        Bundle args = new Bundle();

        args.putString("updated home name", updatedHomeName);

        args.putString("updated away name", updatedAwayName);

        //setArguments pass the args bundle to the gamesCounterFragment
        gameCounterFragment.setArguments(args);

        if (getActivity() != null) {

            //create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment
            fragmentTransaction.replace(R.id.fragment_container, gameCounterFragment);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }

    /**
     * This method allows the user to skip the part of updating teams' names when the user clicks on the
     * skipButton and it opens the gamesCounterFragment
     */
    private void skipUpdate() {

        // Create a new Fragment to be placed in the activity layout
        GameCounterFragment gameCounterFragment = new GameCounterFragment();

        if (getActivity() != null) {

            //create a FragmentTransaction to begin the transaction and replace the Fragment
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment
            fragmentTransaction.replace(R.id.fragment_container, gameCounterFragment);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }
}
