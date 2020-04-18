package com.example.android.trackit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.trackit.models.SavedGame;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class CardAdapter extends FirestoreRecyclerAdapter<SavedGame, CardAdapter.CardHolder> {

    //this listener will get passed in the fragment

    private OnItemClickListener mListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CardAdapter(@NonNull FirestoreRecyclerOptions<SavedGame> options) {
        super(options);
    }

    // note the difference here is in the additional model
    @Override
    protected void onBindViewHolder(@NonNull CardHolder holder, int position, @NonNull SavedGame model) {
        String displayedNames = model.getHomeTeam() + " VS " + model.getAwayTeam();
        holder.teams.setText(displayedNames);

        holder.date.setText(model.getTimestamp().toString());

        holder.winner.setText(model.getWinner());

    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card, parent, false);

        return new CardHolder(view);
    }

    public void deleteItem (int position) {
        //getsnapshots returns all the documents snapshots then getsnapshot and this returns the documents snaphot at this
        //particular position in the recycler view then on this call getreference which returns the documents reference then delete
        //this method deletes the documents at this particular position from the firestore database

        getSnapshots().getSnapshot(position).getReference().delete();

        //note that methods like notifyItemRemoved(position); notifyItemRangeChanged(position, imageModelArrayList.size()
        //are handled by firestore ui recycleradapter


    }

    class CardHolder extends RecyclerView.ViewHolder {

        TextView teams;

        TextView winner;

        TextView date;

        ImageView shareButton;



        public CardHolder(@NonNull View itemView) {
            super(itemView);

            teams = itemView.findViewById(R.id.game_card_teams);

            winner = itemView.findViewById(R.id.game_card_winner);

            date = itemView.findViewById(R.id.game_card_date);

            shareButton = itemView.findViewById(R.id.game_card_share_button);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(getSnapshots().getSnapshot(position), position);

                    }
                }
            });


        }
    }
    //this interface will be implemented in the fragment that displays the cards and the method onitemclick will be overrided there
    //pass documentsnapshot to the fragment because from documentsnapshot we willrecreate the whole Saved Game object, and get
    //the unique id of the document in firestore and we can get document reference if we want to make changes to it

    //with this interface and method we send data from the adapter to the undelying activity or fragment that implements this interface
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        // this method will be used later in the fragment to set the listener to the adapter
        mListener = listener;
    }

}
