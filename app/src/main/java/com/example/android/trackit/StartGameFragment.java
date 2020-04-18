package com.example.android.trackit;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartGameFragment extends Fragment {


    public StartGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_start_game, container, false);

        Button startButton = rootView.findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new Fragment to be placed in the activity layout
                UpdateTeamsFragment updateTeamsFragment = new UpdateTeamsFragment();

                //create a FragmentTransaction to begin the transaction and replace the Fragment
                if(getActivity()!=null) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    // Replace whatever is in the fragment_container view with this fragment
                    fragmentTransaction.replace(R.id.fragment_container, updateTeamsFragment);
                    // and add the transaction to the back stack so the user can navigate back
                    fragmentTransaction.addToBackStack(null);
                    // Commit the transaction
                    fragmentTransaction.commit();
                }
            }
        });

        return rootView;
    }

}
