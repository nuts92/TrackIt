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
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private FirebaseUser currentUser;

    private String userId;

    private FirebaseFirestore db;
    
    private  ImageView mUserProfilePhotoView;
    private TextView mUserNameView;
    private TextView mUserEmailView;
    
    private  TextView mUserSelfIntroductionView;
    

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_user_profile, container, false);

        mUserProfilePhotoView = rootView.findViewById(R.id.user_profile_photo);
        mUserNameView = rootView.findViewById(R.id.user_name);
        mUserEmailView = rootView.findViewById(R.id.user_email);

        mUserSelfIntroductionView = rootView.findViewById(R.id.user_self_introduction);
        Button editProfileButton = rootView.findViewById(R.id.edit_profile_button);

        Button logOutButton = rootView.findViewById(R.id.logout_button);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();

        }

        DocumentReference userDocumentReference = db.collection("Users").document(userId);

        userDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                //Retrieve the user data by creating a retrievedData object from the documentSnapshot which is basically the user document

                if(documentSnapshot != null){

                UserData retrievedData = documentSnapshot.toObject(UserData.class);
                //check if there is data stored and not null, this will be null if the user is new and didn't update the profile data
                if (retrievedData != null) {
                    displayDatabaseInfo(retrievedData);
                }
                }else {
                    //in case the user is new user and no data stored in database

                    displayAuthData();
                }
            }});

//                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                //Retrieve the user data by creating a retrievedData object from the documentSnapshot which is basically the user document
//
//                UserData retrievedData = documentSnapshot.toObject(UserData.class);
//                //check if there is data stored and not null, this will be null if the user is new and didn't update the profile data
//                if (retrievedData != null) {
//                    displayDatabaseInfo(retrievedData);
//                } else {
//                    //in case the user is new user and no data stored in database
//
//                    displayAuthData();
//                }
//            }
//        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new Fragment to be placed in the activity layout
                EditProfileFragment editProfileFragment = new EditProfileFragment();

                //create a FragmentTransaction to begin the transaction and replace the Fragment
                if(getActivity()!=null) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    // Replace whatever is in the fragment_container view with this fragment
                    fragmentTransaction.replace(R.id.fragment_container, editProfileFragment);
                    // and add the transaction to the back stack so the user can navigate back
                    fragmentTransaction.addToBackStack(null);
                    // Commit the transaction
                    fragmentTransaction.commit();
                }

            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(getActivity(), SignUpActivity.class));
                                // display a message by using a Toast
                                Toast.makeText(getActivity(), "You've Signed Out", Toast.LENGTH_SHORT).show();
//                                    finish();//to finish your activity from fragment.
                                getActivity().finish();
                            }
                        });
            }
        });


        return rootView;
    }

    private void displayAuthData() {
        //this is the case of a new user with no data stored in firestore database yet, so we get the data to be displayed
        //from the firebase auth instance when the user has signed in with email or google
        //first part for display the info

        String userDisplayName = currentUser.getDisplayName();
        mUserNameView.setText(userDisplayName);

        String userDisplayEmail = currentUser.getEmail();
        mUserEmailView.setText(userDisplayEmail);

        Uri userPhotoUri = null;

        //if users signs up with email option, he/she won't have a photo to be displayed so it will be null
        //but if the user signed up with google he will have a display image

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /* For devices equal or higher than lollipop set the translation above everything else */
            mUserProfilePhotoView.setTranslationZ(6);
            /* Redraw the view to show the translation */
            mUserProfilePhotoView.invalidate();
        }

        if(currentUser.getPhotoUrl() != null) {

            userPhotoUri = currentUser.getPhotoUrl();
            Glide.with(UserProfileFragment.this).load(userPhotoUri).into(mUserProfilePhotoView);
        }

        //default self introduction

        String userIntroduction = getString(R.string.user_profile_self_intoduction);
        mUserSelfIntroductionView.setText(userIntroduction);

        //Second part for creating a user document in database/ storing data from FirebaseAuth in database

        //changing uri to a string required for firestore database so that it can serialize the object
        // and does not give an error of  exceed minimum depth of 500
        String userPhoto = null;
        if(userPhotoUri != null){
          userPhoto = userPhotoUri.toString();

        }
                //to create a user document based on the user ID in users collection in Firestore database
                //Create an  new object of UserData to pass to the db
                UserData userData = new UserData(userDisplayName, userDisplayEmail, userPhoto, userIntroduction);
                db.collection("Users").document(userId).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //if uploading data to database is successful
                        Log.d("ProfileFragment", "User Data is saved Successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ProfileFragment", e.toString());

                    }
                });

    }

    private void displayDatabaseInfo(UserData retrievedData) {
        String userName = retrievedData.getUserDisplayName();
        String userEmail = retrievedData.getUserEmail();
        String userIntro = retrievedData.getUserIntroduction();
        String userPhotoUrl = retrievedData.getUserPhoto();

        mUserNameView.setText(userName);
        mUserEmailView.setText(userEmail);

        //if the userIntro is not null set the updated text to the textview otherwise leave the default text that was referenced to in the xml layout
        //it will be null if the user didn't update the intro/ this is no problem anymore
        if(userIntro != null){
            mUserSelfIntroductionView.setText(userIntro);
        }
        // else set text to string is better

        //this will be null in case the user has entered the app before with email sign in option and didn't update
        //the profile photo and left it to the default avatar/however if this is the case, there is user data but no
        //photo url then need to set photo to avatar

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /* For devices equal or higher than lollipop set the translation above everything else */
            mUserProfilePhotoView.setTranslationZ(6);
            /* Redraw the view to show the translation */
            mUserProfilePhotoView.invalidate();}

        if(userPhotoUrl != null) {

            Glide.with(UserProfileFragment.this).load(userPhotoUrl).into(mUserProfilePhotoView);


            }
    }

}
