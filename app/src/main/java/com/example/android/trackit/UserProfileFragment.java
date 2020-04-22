package com.example.android.trackit;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.trackit.models.UserData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * UserProfileFragment subclass displays the user profile information including the user photo if it exists, user name, and user
 * self introduction if it exists in addition to the log out and update profile options.
 */
public class UserProfileFragment extends Fragment {

    //Declaring all Object Variables
    private String userId;

    private ImageView mUserProfilePhotoView;

    private TextView mUserNameView;

    private TextView mUserEmailView;

    private TextView mUserSelfIntroductionView;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_profile, container, false);

        //Initializing all Object Variables
        mUserProfilePhotoView = rootView.findViewById(R.id.user_profile_photo);

        mUserNameView = rootView.findViewById(R.id.user_name);

        mUserEmailView = rootView.findViewById(R.id.user_email);

        mUserSelfIntroductionView = rootView.findViewById(R.id.user_self_introduction);

        Button editProfileButton = rootView.findViewById(R.id.edit_profile_button);

        Button logOutButton = rootView.findViewById(R.id.logout_button);

        //Declaring and Initializing an instance of FirebaseAuth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //Declaring and Initializing an instance of FirebaseFirestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Declaring and initializing an instance of FirebaseUser
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            //Get the currentUserId from the currentUser which will be used to get user data from FirebaseFirestore database
            userId = currentUser.getUid();
        }

        //Check if the user has data stored in Firestore database by initializing a userDocumentReference based on the unique userId,
        // the user data is stored in a document and the name of the document is the unique userId in a collection called "Users"
        DocumentReference userDocumentReference = db.collection("Users").document(userId);

        //Get the user document which contains information including updated name, self introduction, photo, etc.
        userDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e("UserProfileFragment", e.toString());
                    return;
                }

                //Retrieve the user data by creating a retrievedData object from the documentSnapshot which is basically the user document
                if (documentSnapshot != null) {

                    UserData retrievedData = documentSnapshot.toObject(UserData.class);

                    if (retrievedData != null) {

                        //Call displayDatabaseInfo to display the user data stored in Firestore Database
                        displayDatabaseInfo(retrievedData);
                    }
                }
            }
        });

        //Attaching an OnClickListener to the editProfileButton that determines the behavior that will happen
        // when the user clicks on that button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open the UpdateUserProfileActivity through passing openIntent to startActivity method
                Intent openIntent = new Intent(getActivity(), UpdateUserProfileActivity.class);

                startActivityForResult(openIntent,0);
            }
        });

        //Attaching an OnClickListener to the logOutButton that determines the behavior that will happen
        // when the user clicks on that button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sign out the user, finish the MainActivity and open the SignUpActivity
                if (getActivity() != null) {
                    AuthUI.getInstance()
                            .signOut(getActivity())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {

                                    // user is now signed out
                                    startActivity(new Intent(getActivity(), SignUpActivity.class));

                                    // Display a Toast Message that says the user has signed out
                                    Toast.makeText(getActivity(), "You've Signed Out", Toast.LENGTH_SHORT).show();

                                    //Finish the activity from the fragment.
                                    getActivity().finish();
                                }
                            });
                }
            }
        });


        return rootView;
    }

    /**
     * This method retrieves and displays the user data stored in Firestore Database
     *
     * @param retrievedData UserData: is the user data stored in user document in Firestore database
     */

    private void displayDatabaseInfo(UserData retrievedData) {

        //Get the username, email, self introduction, and photo from the retreivedData Object
        String userName = retrievedData.getUserDisplayName();

        String userEmail = retrievedData.getUserEmail();

        String userIntro = retrievedData.getUserIntroduction();

        String userPhotoUrl = retrievedData.getUserPhoto();

        //Display the retrieved username and email in the mUserNameView and mUserEmailView object variables
        mUserNameView.setText(userName);
        mUserEmailView.setText(userEmail);

        //if the retrieved user Introduction is not null then display it in the mUserSelfIntroductionView. Otherwise the default
        //introduction will be displayed, this self introduction  will be null if the user didn't update it in EditProfile
        if (userIntro != null) {
            mUserSelfIntroductionView.setText(userIntro);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            //For devices equal or higher than lollipop set the translation above everything else
//            mUserProfilePhotoView.setTranslationZ(6);
//
//            //Redraw the view to show the translation
//            mUserProfilePhotoView.invalidate();
//        }

        //if the retrieved user photo is not null then display it in the mUserProfilePhotoView. Otherwise the default avatar
        // will be displayed, this photo will be null if the user signed up with the email option and did not update the profile photo.
        if (userPhotoUrl != null) {

            Glide.with(UserProfileFragment.this).load(userPhotoUrl).into(mUserProfilePhotoView);
        }
    }
}
