package com.example.android.trackit.main_fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.android.trackit.R;
import com.example.android.trackit.activities.MainActivity;
import com.example.android.trackit.models.SavedGame;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * GameCounterFragment subclass represents the goals and fouls counter screen which is displayed after the user updates the teams'
 * names and here the user can save the game results.
 */
public class GameCounterFragment extends Fragment {

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

    private int homeScore;

    private int awayScore;

    private int homeFouls;

    private int awayFouls;

    private String winner;

    private String finalHomeName;

    private String finalAwayName;

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

        //Initializing homeScore variable that tracks the Score for the Home Team
        homeScore = 0;

        //Initializing awayScore variable that tracks the Score for the Away Team
        awayScore = 0;

        //Initializing homeFouls variable that tracks the Fouls number for the Home Team
        homeFouls = 0;

        //Initializing awayFouls variable that tracks the Fouls number for the Away Team
        awayFouls = 0;

        //Check if there is a bundle passed from the UpdateTeamsFragment if the user chose to update Teams'Names
        if (getArguments() != null) {

            //If there are updated names passed then set these updated names to the TextViews, Otherwise leave the TextViews
            //to the default (Home, Away)
            mUpdatedHomeName = getArguments().getString("updated home name");

            mUpdatedAwayName = getArguments().getString("updated away name");

            mHomeNameView.setText(mUpdatedHomeName);

            mAwayNameView.setText(mUpdatedAwayName);
        }

        //Attaching an OnClickListener to the mHomeGoalButton that determines the behavior that will happen when the user
        // clicks on that button
        mHomeGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Increase the score for Home Team by 1 point each click and then display it
                homeScore = homeScore + 1;

                displayHomeScore(homeScore);
            }
        });

        //Attaching an OnClickListener to the mAwayGoalButton that determines the behavior that will happen when the user
        // clicks on that button
        mAwayGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Increase the score for Away Team by 1 point each button click and then display it
                awayScore = awayScore + 1;

                displayAwayScore(awayScore);
            }
        });

        //Attaching an OnClickListener to the mHomeFoulButton that determines the behavior that will happen when the user
        // clicks on that button
        mHomeFoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Increase the fouls for Home Team by 1 each click and then display them
                homeFouls = homeFouls + 1;

                displayHomeFouls(homeFouls);
            }
        });

        //Attaching an OnClickListener to the mAwayFoulButton that determines the behavior that will happen when the user
        // clicks on that button
        mAwayFoulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Increase the fouls for Away Team by 1 each click and then display them
                awayFouls = awayFouls + 1;

                displayAwayFouls(awayFouls);
            }
        });

        //Attaching an OnClickListener to the mResetButton that determines the behavior that will happen when the user
        // clicks on that button
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Reset the Scores and the Fouls for Home Team and Away Team to zero
                homeScore = 0;

                awayScore = 0;

                homeFouls = 0;

                awayFouls = 0;

                //Display the Updated Score and the updated Fouls for Home Team and Away Team
                displayHomeScore(homeScore);

                displayAwayScore(awayScore);

                displayHomeFouls(homeFouls);

                displayAwayFouls(awayFouls);
            }
        });

        //Attaching an OnClickListener to the mSaveButton that determines the behavior that will happen when the user
        // clicks on that button
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveGame();
            }
        });

        return rootView;
    }

    /**
     * This method determines the winner and final names of the teams then save these values and the game results in Firestore Database.
     */
    private void saveGame() {

        //Determining the winner
        if (homeScore > awayScore) {

            if (mUpdatedHomeName != null) {

                winner = mUpdatedHomeName;

            } else {

                winner = getString(R.string.winner_home);
            }

        } else if (homeScore < awayScore) {

            if (mUpdatedAwayName != null) {

                winner = mUpdatedAwayName;

            } else {

                winner = getString(R.string.winner_away);
            }

        } else {

            winner = getString(R.string.tie);
        }

        //Determining the final name of home team the moment the user clicked on the save button, if the user already
        // updated the name, then the finalHomeName value will be the updated name otherwise it's value will be the default name (home)
        if (mUpdatedHomeName != null) {

            finalHomeName = mUpdatedHomeName;

        } else {

            finalHomeName = getString(R.string.default_home_name);
        }

        //Determining the final name of away team the moment the user clicked on the save button, if the user already
        // updated the name, then the finalAwayName value will be the updated name otherwise it's value will be the default name (Away)
        if (mUpdatedAwayName != null) {

            finalAwayName = mUpdatedAwayName;

        } else {

            finalAwayName = getString(R.string.default_away_name);
        }

        //Save the Game Results in Firestore Database
        saveResultInFirestore();
    }

    /**
     * This method saves the game results in Firestore Database including the winner, the home and away teams' names if updated by
     * the user, and the scores and fouls of each team. In addition, it disables all buttons after the game is saved and just displays
     * Start A New Game Button.
     */
    private void saveResultInFirestore() {

        //Decalring and initializing an instance of Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Declaring and initializing an instance of the FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Declaring and initializing an instance of FirebaseUser
        FirebaseUser currentUser = auth.getCurrentUser();

        //Get the userId from the currentUser and this Id is unique for every user and will be used to store data in FirebaseFirestore database
        if (currentUser != null) {

            currentUserId = currentUser.getUid();
        }

        //Declaring and initializing a gamesCollectionReference to reference to a collection called "Saved Games" in Firestore database that stores
        //the saved games of the user and this collection will be stored inside the user document which is stored in a collection called "Users".
        CollectionReference gamesCollectionReference = db.collection("Users").document(currentUserId).collection("Saved Games");

        //Declaring and initializing a savedGame Object Variable that stores the user game results to be passed to the database
        SavedGame savedGame = new SavedGame(finalHomeName, finalAwayName, homeScore, awayScore, homeFouls, awayFouls, winner);

        gamesCollectionReference.add(savedGame).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                //if game is saved successfully, show a dialog that displays the result and a toast message that the game is saved
                //In addition, disable the goal and foul buttons, hide the mSaveButton, and change the text of resetButton to Start A New Game.
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

                        startActivity(new Intent(getActivity(), MainActivity.class));

                        if (getActivity() != null) {

                            getActivity().finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("GameCounterFragment", e.toString());
            }
        });
    }

    /**
     * This method displays a dialog that shows the Game Result along with a Text Call to Action to start a new game.
     */
    private void showAnimationDialog() {

        if (getActivity() != null) {

            //Declaring and initializing a dialog object variable
            final Dialog dialog = new Dialog(getActivity());

            dialog.setContentView(R.layout.save_game_popup);

            //Declaring and initializing the resultAnimation, resultText, and closeResultButton object variables
            LottieAnimationView resultAnimation = dialog.findViewById(R.id.result_lottie_animation);

            TextView resultText = dialog.findViewById(R.id.result_text);

            ImageView closeResultButton = dialog.findViewById(R.id.result_card_close_button);

            //Determining what to display in the PopUp Dialog based on the Game Result
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

            //setting the background of the dialog window to transparent
            if (dialog.getWindow() != null) {

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            dialog.show();

            //Attaching an OnClickListener to the closeResultButton that closes the dialog when the user clicks on the button
            closeResultButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
        }
    }

    /**
     * This method Displays the given score for the Home Team.
     *
     * @param score int: is the Home Team Score
     */
    private void displayHomeScore(int score) {

        TextView homeScoreView = rootView.findViewById(R.id.home_score);

        homeScoreView.setText(String.valueOf(score));
    }

    /**
     * This method displays the given score for the Away Team.
     *
     * @param score int: is the Away Team Score
     */
    private void displayAwayScore(int score) {

        TextView awayScoreView = rootView.findViewById(R.id.away_score);

        awayScoreView.setText(String.valueOf(score));
    }

    /**
     * This method displays the given fouls for the Home Team.
     *
     * @param fouls int: is the Home Team Fouls' Number
     */
    private void displayHomeFouls(int fouls) {

        TextView homeFoulsView = rootView.findViewById(R.id.home_fouls);

        homeFoulsView.setText(String.valueOf(fouls));
    }

    /**
     * This method displays the given fouls for the Away Team.
     *
     * @param fouls int: is the Away Team Fouls' Number
     */
    private void displayAwayFouls(int fouls) {

        TextView awayFoulsView = rootView.findViewById(R.id.away_fouls);

        awayFoulsView.setText(String.valueOf(fouls));
    }

    /**
     * This method is called when the fragment's activity has been created and this fragment's view hierarchy instantiated.
     * It can be used to do final initialization once these pieces are in place, such as retrieving views or restoring state.
     *
     * @param savedInstanceState Bundle: If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            //Restore the fragment's state from savedInstanceState bundle
            homeScore = savedInstanceState.getInt("home team score");

            awayScore = savedInstanceState.getInt("away team score");

            homeFouls = savedInstanceState.getInt("home team fouls");

            awayFouls = savedInstanceState.getInt("away team fouls");

            //Display the fragment's restored state
            displayHomeScore(homeScore);

            displayAwayScore(awayScore);

            displayHomeFouls(homeFouls);

            displayAwayFouls(awayFouls);
        }
    }

    /**
     * This method is called to ask the fragment to save its current dynamic state, so it can later be reconstructed in a
     * new instance of its process is restarted.
     *
     * @param outState Bundle: Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        //Save the fragment's state in the outState bundle
        outState.putInt("home team score", homeScore);

        outState.putInt("away team score", awayScore);

        outState.putInt("home team fouls", homeFouls);

        outState.putInt("away team fouls", awayFouls);
    }
}
