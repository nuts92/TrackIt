package com.example.android.trackit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.trackit.R;
import com.example.android.trackit.models.SavedGame;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * This Class provides the Adapter to populate items/cards inside of the RecyclerView. It extends FirestoreRecyclerAdapter that
 * binds a Query to a RecyclerView and responds to all real-time events included items being added, removed, moved, or changed.
 */
public class CardAdapter extends FirestoreRecyclerAdapter<SavedGame, CardAdapter.CardHolder> {

    //Declaring an instance of mListener interface
    private OnItemClickListener mListener;

    /**
     * This constructor creates a new RecyclerView Adapter that listens to a Firestore Query.
     *
     * @param options FirestoreRecyclerOptions: for configuration options.
     */
    public CardAdapter(@NonNull FirestoreRecyclerOptions<SavedGame> options) {

        super(options);
    }

    /**
     * This method is called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     *
     * @param holder   CardHolder: The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position int: The position of the item within the adapter's data set.
     * @param model    SavedGame: is the data model that represents the user data
     */
    @Override
    protected void onBindViewHolder(@NonNull CardHolder holder, int position, @NonNull SavedGame model) {

        String displayedNames = model.getHomeTeam() + " VS " + model.getAwayTeam();

        holder.teams.setText(displayedNames);

        holder.date.setText(model.getTimestamp().toString());

        holder.winner.setText(model.getWinner());
    }

    /**
     * This method is called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     *
     * @param parent   ViewGroup: The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType int: The view type of the new View.
     * @return CardHolder: A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card, parent, false);

        return new CardHolder(view);
    }

    /**
     * This method deletes the item/card at a specific position and deletes its document from Firestore database.
     *
     * @param position the Adapter position of the item represented by the CardHolder.
     */
    public void deleteItem(int position) {

        //getSnapshots returns all the Documents' Snapshots then calling getSnapshot will return the DocumentSnapshot at this
        //particular position in the recyclerView then calling getReference will return the DocumentReference then calling
        //delete will delete the document at this particular position from the Firestore database.
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    /**
     * This class represents a ViewHolder called CardHolder that describes an item view and metadata about its place within the RecyclerView.
     */
    class CardHolder extends RecyclerView.ViewHolder {

        //Declaring all Object Variables
        TextView teams;

        TextView winner;

        TextView date;

        ImageView shareButton;

        public CardHolder(@NonNull View itemView) {

            super(itemView);

            //Initializing all object variables
            teams = itemView.findViewById(R.id.game_card_teams);

            winner = itemView.findViewById(R.id.game_card_winner);

            date = itemView.findViewById(R.id.game_card_date);

            shareButton = itemView.findViewById(R.id.game_card_share_button);

            //Attaching an OnClickListener to the shareButton that determines the behavior that will happen when the user
            // clicks on that button
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Get the Adapter position of the item represented by this CardHolder
                    int position = getAdapterPosition();

                    //call onItemClick method on the mListener and pass the position along with the documentSnapshot to the fragment because
                    //from the documentSnapshot we will recreate the whole Saved Game object, and get from it the data to create the message
                    // that will be shared
                    if (position != RecyclerView.NO_POSITION && mListener != null) {

                        mListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    /**
     * This interface will be used to send data from the adapter to the underlying activity or fragment that implements this interface
     * It will be implemented in the SavedGamesFragment that displays the Saved Games' Cards.
     */
    public interface OnItemClickListener {

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    /**
     * This method will be used in the SavedGamesFragment to set the OnItemClickListener to the CardAdapter
     *
     * @param listener CardAdapter.OnItemClickListener as an input parameter
     */
    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }
}
