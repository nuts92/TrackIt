package com.example.android.trackit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.trackit.models.UserData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    private String userId;

    private FirebaseUser mCurrentUser;

    private ImageView userDisplayedPhoto;

    private TextView userDisplayedName;

    private  TextView userDisplayedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare and initialize an instance of Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();


        //Initialize the mDrawerLayout object variable

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Declare and initialize the toolBar, the navigationView, and the navigationHeader object variables

        Toolbar toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.navigation_view);

        //getHeaderView gets the header view at the specified position in this case 0 which index header.

        View navigationHeader = navigationView.getHeaderView(0); // 0-index header

        //set the toolBar to act as the ActionBar for this Activity window.

        setSupportActionBar(toolbar);

        //Declaring and initializing the ImageView and the two TextViews found in the navigationHeader
       userDisplayedPhoto = navigationHeader.findViewById(R.id.navigation_user_image);

       userDisplayedName = navigationHeader.findViewById(R.id.navigation_user_name);

       userDisplayedEmail = navigationHeader.findViewById(R.id.navigation_user_email);

        // to display the name, photo, and email of the user in the navigationHeader, we need to get the user data first
        // so we will get this data either from the FirebaseAuth instance if the user is new or from FirebaseFirestore database if
        // the user is a returning user who might have updated his/her profile data.

        //Declaring and initializing an instance of FirebaseUser then check if the user is already signed in and not null
        mCurrentUser  = auth.getCurrentUser();

        if (mCurrentUser != null) {
            //Get the userId from the currentUser and this Id is unique for every user and will be used to store data
            // in FirebaseFirestore database

            userId = mCurrentUser.getUid();
        }

        //Declaring and initializing an instance of FirebaseFirestore database

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //check if the user has data stored in Firestore database by declaring and initializing a userDocumentReference based
        //on the unique userId, here the collection name in the database is Users and every user has a document in this collection
        // and the name of the document is the unique userId

        DocumentReference userDocumentReference = db.collection("Users").document(userId);

        //get the user document which contains information including updated name, email, self introduction, photo, etc.

        userDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //Retrieve the user data by creating a retrievedData object from the documentSnapshot which is basically the user document

                if (documentSnapshot != null) {
                    UserData retrievedData = documentSnapshot.toObject(UserData.class);
                    //check if there is data stored and not null, this will be null if the user is new and didn't update the profile data
                    if (retrievedData != null) {
                        displayDatabaseInfo(retrievedData);
                    } else {
                        //in case the user is new user and no data stored in database

                        displayAuthData();

                    }

                }

            }});



        //set OnNavigationItemSelectedListener on navigationView object for handling events on navigation items.

        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartGameFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_new_game);

        }
    }

    /**
     * This method displays the user data including the name, email, and photo obtained from FirebaseAuth instance
     */

    private void displayAuthData() {

        //This is the case where the user is a new one and no data is stored in FirebaseFirestore database
        //In this case we will display the data that is obtained from FirebaseAuth instance instead

        String userName = mCurrentUser.getDisplayName();

        userDisplayedName.setText(userName);

        String userEmail = mCurrentUser.getEmail();

        userDisplayedEmail.setText(userEmail);

        //check if the user has a photo because if the user signed up with the email option, he/she won't have a photo
        //to be displayed unless the user later updated the profile photo

        if(mCurrentUser.getPhotoUrl() != null) {

            Uri userProfilePhoto = mCurrentUser.getPhotoUrl();

            Glide.with(MainActivity.this).load(userProfilePhoto).into(userDisplayedPhoto);
        }
    }

    /**
     * This method displays the name, email, and photo of the user stored in the user document in FirebaseFirestore database
     * @param retrievedData UserData: the user data retrieved from the document with userId name inside Users collection in FirebaseFirestore database
     */

    private void displayDatabaseInfo(UserData retrievedData) {

        String retrievedName = retrievedData.getUserDisplayName();
        String retrievedEmail = retrievedData.getUserEmail();

        userDisplayedName.setText(retrievedName);
        userDisplayedEmail.setText(retrievedEmail);

        //Check if there is a photo because there is a case where retrievedData exists but the photo is null
        // if the user signed up with email option which doesn't require a photo and then did not update his/her profile photo
        // so the default avatar will be the one displayed and retreivedData.getUserPhoto() would be null

        if (retrievedData.getUserPhoto() != null) {
            String retrievedPhoto = retrievedData.getUserPhoto();
            Glide.with(MainActivity.this).load(retrievedPhoto).into(userDisplayedPhoto);

        }
    }

    /**
     * OnBackPressed() is called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * onNavigationItemSelected is called when an item in the navigation menu is selected.
     * @param item 	MenuItem: The selected item
     * @return boolean: true to display the item as the selected item
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_new_game:
                //open the StartGameFragment by adding the fragment to the activity during the activity runtime.
                //call getSupportFragmentManager() to get a FragmentManager using the Support Library APIs.
                // Then call beginTransaction() to create a FragmentTransaction and call replace() to add the fragment to
                // the 'fragment_container' FrameLayout then  call commit().
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartGameFragment()).commit();
                break;
            case R.id.nav_how_to:
                Intent intent = new Intent(MainActivity.this, IntroductionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment())
                        .addToBackStack(null).commit();
                break;
            case R.id.nav_saved_games:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SavedGamesFragment())
                        .addToBackStack(null).commit();
                //to go back to the previous fragment for example go back to start a new game fragment instead of getiing ouside the app
                break;
        }
        //Close the specified drawer mDrawerLayout by animating it out of view.
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
