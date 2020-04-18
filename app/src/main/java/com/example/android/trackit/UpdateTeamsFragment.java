package com.example.android.trackit;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateTeamsFragment extends Fragment {

    private EditText mHomeName;

    private EditText mAwayName;


    public UpdateTeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_update_teams, container, false);

        mHomeName = rootView.findViewById(R.id.home_team_name_update);
        mAwayName = rootView.findViewById(R.id.away_team_name_update);
        Button updateButton = rootView.findViewById(R.id.team_card_update_button);
        Button skipButton = rootView.findViewById(R.id.skip_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTeams();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipUpdate();
            }
        });

        return rootView;

    }


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

        //Create a bundle object to pass the updatedHomeName and updatedAwayName variables values to gamesCounterFragment

        Bundle args = new Bundle();
        args.putString("updated home name", updatedHomeName);
        args.putString("updated away name", updatedAwayName);
        //setArguments pass the args bundle to the gamesCounterFragment/Supply the construction arguments for this fragment
        gameCounterFragment.setArguments(args);

        //create a FragmentTransaction to begin the transaction and replace the Fragment
        if (getActivity() != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment
            fragmentTransaction.replace(R.id.fragment_container, gameCounterFragment);
            // and add the transaction to the back stack so the user can navigate back
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();

        }

    }

    private void skipUpdate() {
        // Create a new Fragment to be placed in the activity layout
        GameCounterFragment gameCounterFragment = new GameCounterFragment();

        //create a FragmentTransaction to begin the transaction and replace the Fragment
        if (getActivity() != null) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment
            fragmentTransaction.replace(R.id.fragment_container, gameCounterFragment);
            // and add the transaction to the back stack so the user can navigate back
            fragmentTransaction.addToBackStack(null);
            // Commit the transaction
            fragmentTransaction.commit();
        }
    }

}
