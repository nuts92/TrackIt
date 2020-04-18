package com.example.android.trackit;


import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.android.trackit.models.SavedGame;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedGamesFragment extends Fragment {

    //Declare an instance of FirebaseFirestore

    private FirebaseFirestore db;

    private CollectionReference gamesCollectionReference;

    private CardAdapter cardAdapter;

    private RecyclerView recyclerView;

    private FirebaseUser currentUser;

    private TextView mEmptyInfoTextView;


    public SavedGamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_saved_games, container, false);

        //Initialize an instance of FirebaseFirestore

        FirebaseAuth auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser();

        String currentUserId = currentUser.getUid();

        db = FirebaseFirestore.getInstance();

        gamesCollectionReference = db.collection("Users").document(currentUserId).collection("Saved Games");

        // obtain a handle to the recyclerview object,
        recyclerView = rootView.findViewById(R.id.recycler_view);

        mEmptyInfoTextView = rootView.findViewById(R.id.emptyInfoTextView);

        setUpRecyclerView();

        return rootView;
    }

    private void setUpRecyclerView() {

        Query query = gamesCollectionReference.orderBy("timestamp", Query.Direction.DESCENDING);

        //configure the adapter by building FirestoreRecyclerOptions,  Configure recycler adapter options:
        //  * query is the Query object defined above.
        //  * SavedGame.class instructs the adapter to convert each DocumentSnapshot to a SavedGame object
        //  FirestoreRecyclerOptions<SavedGame>

        FirestoreRecyclerOptions<SavedGame> options = new FirestoreRecyclerOptions.Builder<SavedGame>()
                .setQuery(query, SavedGame.class)
                .build();

        // specify an adapter (cardAdapter)
        cardAdapter = new CardAdapter(options);

        // obtain a handle to the recyclerview object, connect it to a layout manager,
        // and attach an adapter for the data to be displayed

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //To get the number of items that are returned by the query which is passed to the FirestoreRecyclerOptions object,
        // you need to use getItemCount() method that exist in your adapter class.
        // Because the data from Cloud Firestore is loaded asynchronously, you cannot simply call getItemCount()
        // directly in your adapter class, as it will always be zero. So in order to get the total number of items,
        // you need to register an observer like in the following lines of code:

        cardAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                int totalNumberOfItems = cardAdapter.getItemCount();
                Log.d("SavedGamesFragment", String.valueOf(totalNumberOfItems));
                if(totalNumberOfItems > 0) {
                    mEmptyInfoTextView.setVisibility(View.GONE);
                }
            }
        });

        //attach the adapter to your RecyclerView with the RecyclerView#setAdapter() method.
        // Don't forget to also set a LayoutManager!
        recyclerView.setAdapter(cardAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
                // onMove is for drag and drop which we will not use in this app
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //direction here doesn't matter cause it's when we want to distinguish between left and right swipes but not in this app only left
                cardAdapter.deleteItem(viewHolder.getAdapterPosition());
                Toast.makeText(getActivity(), "Game is successfully deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                SavedGame savedGame = documentSnapshot.toObject(SavedGame.class);
                String documentId = documentSnapshot.getId();

                Bundle bundle = new Bundle();
                bundle.putString("documentId", documentId);


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        cardAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        cardAdapter.stopListening();

    }
}
