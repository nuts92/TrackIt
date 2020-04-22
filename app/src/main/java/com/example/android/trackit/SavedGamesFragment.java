package com.example.android.trackit;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.trackit.adapters.CardAdapter;
import com.example.android.trackit.models.SavedGame;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * SavedGamesFragment subclass displays a list of all the saved games by the user ordered by the timeStamp where the user can share
 * the information of any of these cards.
 */
public class SavedGamesFragment extends Fragment {

    //Declaring all Object Variables
    private CollectionReference gamesCollectionReference;

    private String currentUserId;

    private CardAdapter cardAdapter;

    private RecyclerView recyclerView;

    private TextView mEmptyInfoTextView;

    public SavedGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_saved_games, container, false);

        //Declaring and Initializing an instance of FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Declaring and initializing an instance of FirebaseUser
        FirebaseUser currentUser = auth.getCurrentUser();

        //Get the currentUserId from the currentUser which will be used to get data from FirebaseFirestore database
        if (currentUser != null) {

            currentUserId = currentUser.getUid();
        }

        //Declaring and Initializing an instance of FirebaseFirestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Declaring and initializing a gamesCollectionReference to reference to a collection called "Saved Games" in Firestore database that stores
        //the saved games of the user and this collection is stored inside the user document which is stored in a collection called "Users".
        gamesCollectionReference = db.collection("Users").document(currentUserId).collection("Saved Games");

        //Initializing the recyclerView and the mEmptyInfoTextView Object Variables
        recyclerView = rootView.findViewById(R.id.recycler_view);

        mEmptyInfoTextView = rootView.findViewById(R.id.emptyInfoTextView);

        //Calling setUpRecyclerView method which sets ups the recyclerView
        setUpRecyclerView();

        return rootView;
    }

    private void setUpRecyclerView() {

        //Declaring and initializing a query object that displays the data from Firestore ordered by timestamp which means
        // the saved game with the latest date is displayed first
        Query query = gamesCollectionReference.orderBy("timestamp", Query.Direction.DESCENDING);

        //configure the adapter by building FirestoreRecyclerOptions,  Configure recycler adapter options:
        // query is the Query object defined above.
        // SavedGame.class instructs the adapter to convert each DocumentSnapshot to a SavedGame object
        FirestoreRecyclerOptions<SavedGame> options = new FirestoreRecyclerOptions.Builder<SavedGame>()
                .setQuery(query, SavedGame.class)
                .build();

        // Initializing the carAdapter Object Variable
        cardAdapter = new CardAdapter(options);

        //This setting improves performance if the changes in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        //Set the layoutManager that the recyclerView will use
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Get the number of items that are returned by the query which is passed to the FirestoreRecyclerOptions object so that
        //if the number of items is zero then we will display the mEmptyInfoTextView that says "No Saved Games Yet"
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("SavedGamesFragment", e.toString());
                    return;
                }

                if (queryDocumentSnapshots != null) {
                    if (queryDocumentSnapshots.getDocuments().size() == 0) {
                        mEmptyInfoTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //Attaching the cardAdapter to the recyclerView with the setAdapter() method.
        recyclerView.setAdapter(cardAdapter);

        //An ItemTouchHelper that will work with the given Callback.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // onMove is for drag and drop which we will not use in this app
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                //when the user swipes the card to the left, the item in this specific position will be deleted and
                // a toast message will be displayed that the deletion process is successful
                cardAdapter.deleteItem(viewHolder.getAdapterPosition());

                Toast.makeText(getActivity(), "Game is successfully deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //Setting an OnItemClickListener on the cardAdapter that determines the behavior that will happen when the user
        // clicks on the shareButton
        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                //Declaring and initializing a savedGame object that is created from the documentSnapshot
                SavedGame savedGame = documentSnapshot.toObject(SavedGame.class);

                //Declaring and initializing a message that will be shared when the user clicks on the shareButton
                String shareMessage = null;

                if (savedGame != null) {

                    if (savedGame.getWinner().equals("Tie")) {

                        shareMessage = "The Result of the Soccer Match between " + savedGame.getHomeTeam() + " and " + savedGame.getAwayTeam() +
                                " held on " + savedGame.getTimestamp().toString() + " is " + savedGame.getWinner() + ".";

                    } else {

                        shareMessage = "The Winner of the Soccer Match between: " + savedGame.getHomeTeam() + " and " + savedGame.getAwayTeam() +
                                " held on " + savedGame.getTimestamp().toString() + " is " + savedGame.getWinner() + ".";
                    }

                    shareMessage += "\nSummary of Match results: " + "\n" + savedGame.getHomeTeam() + " Score: " + savedGame.getHomeScore();

                    shareMessage += "\n" + savedGame.getAwayTeam() + " Score: " + savedGame.getAwayScore();

                    shareMessage += "\n" + savedGame.getHomeTeam() + " Fouls: " + savedGame.getHomeFouls();

                    shareMessage += "\n" + savedGame.getAwayTeam() + " Fouls: " + savedGame.getAwayFouls();
                }

                //Use Android Sharesheet to send text content outside the app and/or directly to another user via email or social networking.
                // create an intent and set its action to Intent.ACTION_SEND.
                Intent sendIntent = new Intent();

                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                sendIntent.setType("text/plain");

                //In order to display the Android Sharesheet, call Intent.createChooser(), passing it the sendIntent object.
                // It returns a version of the intent that will always display the Android Sharesheet.
                Intent shareIntent = Intent.createChooser(sendIntent, null);

                startActivity(shareIntent);
            }
        });
    }

    /**
     * This method is called when the Fragment is visible to the user.
     */
    @Override
    public void onStart() {

        super.onStart();

        //The FirestoreRecyclerAdapter uses a snapshot listener to monitor changes to the Firestore query.
        // To begin listening for data, we call the startListening() method on the cardAdapter.
        cardAdapter.startListening();
    }

    /**
     * This method is called when the Fragment is no longer started.
     */
    @Override
    public void onStop() {

        super.onStop();

        //The stopListening() call removes the snapshot listener and all data in the adapter. We call this method
        // when the containing Fragment stops.
        cardAdapter.stopListening();
    }
}
