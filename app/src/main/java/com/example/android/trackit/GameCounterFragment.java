package com.example.android.trackit;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.android.trackit.models.SavedGame;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameCounterFragment extends Fragment {

    //Tracks the Score for the Home Team

    private int homeScore = 0;

    //Tracks the Score for the Away Team

    private int awayScore = 0;

    //Tracks the Fouls number for the Home Team

    private int homeFouls = 0;

    //Tracks the Fouls number for the Away Team

    private int awayFouls = 0;

    //Declaring all object variables

    private TextView mHomeNameView;

    private TextView mAwayNameView;

    private ViewGroup rootView;

    private String mUpdatedHomeName;

    private String mUpdatedAwayName;

    private Button mSaveButton;

    private Button mResetButton;

    private Button mHomeGoalButton;

    private Button mAwayGoalButton;

    private Button mHomeFoulButton;

    private Button mAwayFoulButton;

    private String currentUserId;



    public GameCounterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game_counter, container, false);

        //Initialize all object variables

        mHomeNameView = rootView.findViewById(R.id.home_title);
        mHomeGoalButton = rootView.findViewById(R.id.home_goal_button);
        mHomeFoulButton = rootView.findViewById(R.id.home_foul_button);

        mAwayNameView = rootView.findViewById(R.id.away_title);
        mAwayGoalButton = rootView.findViewById(R.id.away_goal_button);
        mAwayFoulButton = rootView.findViewById(R.id.away_foul_button);


        mResetButton = rootView.findViewById(R.id.reset_button);
        mSaveButton = rootView.findViewById(R.id.save_button);

        //Check if there is a bundle passed from the UpdateTeamsFragment if the user chose to update Teams' Names
        //If there are updated names passed then set these updated names to the TextViews otherwise Leave the TextViews
        //to default (Home, Away)

        if (getArguments() != null) {

            mUpdatedHomeName = getArguments().getString("updated home name");
            mUpdatedAwayName = getArguments().getString("updated away name");

            mHomeNameView.setText(mUpdatedHomeName);
            mAwayNameView.setText(mUpdatedAwayName);
        }


        mHomeGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Increase the score for Home Team by 1 point each click
                homeScore = homeScore + 1;
                displayHomeScore(homeScore);

            }
        });

        mAwayGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Increase the score for Away Team by 1 point each button click
                awayScore = awayScore + 1;
                displayAwayScore(awayScore);
            }
        });

        mHomeFoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Increase the fouls for Home Team by 1 point each click
                homeFouls = homeFouls + 1;
                displayHomeFouls(homeFouls);
            }
        });

        mAwayFoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Increase the fouls for Away Team by 1 point each click
                awayFouls = awayFouls + 1;
                displayAwayFouls(awayFouls);
            }
        });


        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Reset the Score and the Fouls for Home Team and Away Team to zero
                homeScore = 0;
                awayScore = 0;
                homeFouls = 0;
                awayFouls = 0;

                //Display the Updated Score and the updated Fouls for Home Team and Away Team

                displayHomeScore(homeScore);
                displayAwayScore(awayScore);
                displayHomeFouls(homeFouls);
                displayAwayFouls(awayFouls);

                mHomeNameView.setText(R.string.game_counter_fragment_home_title);
                mAwayNameView.setText(R.string.game_counter_fragment_away_title);


            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGame();
            }
        });

        return rootView;
    }

    private void saveGame() {

        //determine the winner

        String winner;

        if (homeScore > awayScore) {

            if (mUpdatedHomeName != null) {

                winner = mUpdatedHomeName;

            } else {

                winner = "Home";
            }
        } else if (homeScore < awayScore) {

            if (mUpdatedAwayName != null) {

                winner = mUpdatedAwayName;

            } else {
                winner = "Away";
            }
        } else {

            winner = "Tie";
        }

        //Determining the final name of home and away teams the moment the user clicked on the save button

        String finalHomeName;

        // if the user already updated the name, then the finalHomeName value will be the updated name otherwise
        //it's value will be the default home

        if (mUpdatedHomeName != null) {
            finalHomeName = mUpdatedHomeName;
        } else {
            finalHomeName = getString(R.string.game_counter_fragment_home_title);
        }

        //Determining the final name of home and away teams the moment the user clicked on the save button

        String finalAwayName;

        // the user already updated the name

        if (mUpdatedAwayName != null) {
            finalAwayName = mUpdatedAwayName;
        } else {
            finalAwayName = getString(R.string.game_counter_fragment_away_title);
        }
     //create an instance of Firestore db, this is the name of our collection and where we want to save this document

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Initialize an instance of the FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser!= null) {
           currentUserId = currentUser.getUid();

        }

        CollectionReference gamesCollectionReference = db.collection("Users").document(currentUserId).collection("Saved Games");


        gamesCollectionReference.add(new SavedGame(finalHomeName, finalAwayName, homeScore, awayScore, homeFouls, awayFouls, winner))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showAnimationDialog();
                        Toast.makeText(getActivity(), "Game is Saved", Toast.LENGTH_SHORT).show();
                        mSaveButton.setVisibility(View.GONE);
                        mHomeGoalButton.setEnabled(false);
                        mAwayGoalButton.setEnabled(false);
                        mHomeFoulButton.setEnabled(false);
                        mAwayFoulButton.setEnabled(false);
                        mResetButton.setText(R.string.game_counter_fragment_reset_button_change);
                        mResetButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                startActivity(new Intent(getActivity(), MainActivity.class));

                                if(getActivity()!=null) {
                                   getActivity().getSupportFragmentManager().beginTransaction().
                                           replace(R.id.fragment_container, new StartGameFragment())
                                           .addToBackStack(null).commit();
                            }
                        }});

                    }
                });

    }

    private void showAnimationDialog() {

        if(getActivity() != null){

            final Dialog dialog = new Dialog(getActivity());

         dialog.setContentView(R.layout.save_game_popup);

        LottieAnimationView resultAnimation = dialog.findViewById(R.id.result_lottie_animation);
        TextView resultText = dialog.findViewById(R.id.result_text);
        ImageView closeResultButton = dialog.findViewById(R.id.result_card_close_button);

        if (homeScore > awayScore) {
            resultAnimation.setAnimation(R.raw.win);
            resultAnimation.playAnimation();
            resultText.setText(R.string.save_game_popup_win_situation);
        } else if (homeScore < awayScore) {
            resultAnimation.setAnimation(R.raw.lose);
            resultAnimation.playAnimation();
            resultText.setText(R.string.save_game_popup_loss_situation);
        } else {
            resultAnimation.setAnimation(R.raw.tie);
            resultAnimation.playAnimation();
            resultText.setText(R.string.save_game_popup_tie_situation);

        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        closeResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }}


    /**
     * Displays the given score for Home Team.
     */
    private void displayHomeScore(int score) {
        TextView homeScoreView = rootView.findViewById(R.id.home_score);
        homeScoreView.setText(String.valueOf(score));

    }

    /**
     * Displays the given score for Away Team.
     */
    private void displayAwayScore(int score) {
        TextView awayScoreView = rootView.findViewById(R.id.away_score);
        awayScoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given fouls for Home Team.
     */
    private void displayHomeFouls(int fouls) {
        TextView homeFoulsView = rootView.findViewById(R.id.home_fouls);
        homeFoulsView.setText(String.valueOf(fouls));
    }

    /**
     * Displays the given fouls for Away Team.
     */
    private void displayAwayFouls(int fouls) {
        TextView awayFoulsView = rootView.findViewById(R.id.away_fouls);
        awayFoulsView.setText(String.valueOf(fouls));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here

            homeScore = savedInstanceState.getInt("home team score");
            awayScore = savedInstanceState.getInt("away team score");
            homeFouls = savedInstanceState.getInt("home team fouls");
            awayFouls = savedInstanceState.getInt("away team fouls");

            displayHomeScore(homeScore);
            displayAwayScore(awayScore);
            displayHomeFouls(homeFouls);
            displayAwayFouls(awayFouls);

        }
    }


    //Save the fragment's state here

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("home team score", homeScore);
        outState.putInt("away team score", awayScore);
        outState.putInt("home team fouls", homeFouls);
        outState.putInt("away team fouls", awayFouls);

    }

}
