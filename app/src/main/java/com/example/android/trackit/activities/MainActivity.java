package com.example.android.trackit.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.trackit.R;
import com.example.android.trackit.main_fragments.SavedGamesFragment;
import com.example.android.trackit.main_fragments.StartGameFragment;
import com.example.android.trackit.main_fragments.UserProfileFragment;
import com.example.android.trackit.models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * This class displays the MainActivity screen where the user can navigate between different sections/fragments including
 * Start A New Game, Profile, and SavedGames. In addition, the user can navigate back to the introduction wizard.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Declaring all object variables

    private DrawerLayout mDrawerLayout;

    private String userId;

    private FirebaseUser mCurrentUser;

    private DocumentReference userDocumentReference;

    private ImageView userDisplayedPhoto;

    private TextView userDisplayedName;

    private TextView userDisplayedEmail;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declaring and initializing an instance of Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Initializing the mDrawerLayout object variable
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //Declaring and initializing the toolBar, the navigationView, and the navigationHeader object variables
        toolbar = findViewById(R.id.toolbar);

        NavigationView navigationView = findViewById(R.id.navigation_view);

        //Calling getHeaderView method on the navigationView gets the navigation header view at the specified position.
        View navigationHeader = navigationView.getHeaderView(0);

        //set the toolBar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);

        //Declaring and initializing the ImageView and the two TextViews found in the navigationHeader
        userDisplayedPhoto = navigationHeader.findViewById(R.id.navigation_user_image);

        userDisplayedName = navigationHeader.findViewById(R.id.navigation_user_name);

        userDisplayedEmail = navigationHeader.findViewById(R.id.navigation_user_email);

        //Declaring and initializing an instance of FirebaseUser then check if the user is already signed in and not null
        mCurrentUser = auth.getCurrentUser();

        if (mCurrentUser != null) {

            //Get the userId from the currentUser and this Id is unique for every user and will be used to store data in FirebaseFirestore database
            userId = mCurrentUser.getUid();
        }

        //Declaring and initializing an instance of FirebaseFirestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Check if the user has data stored in Firestore database by initializing a userDocumentReference based on the unique userId,
        // the user data is stored in a document and the name of the document is the unique userId in a collection called "Users"
        userDocumentReference = db.collection("Users").document(userId);

        //Get the user document which contains information including updated name, email, photo, etc.
        userDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.e("MainActivity", e.toString());
                    return;
                }

                //Retrieve the user data by creating a retrievedData object from the documentSnapshot which is basically a snapshot of the user document
                if (documentSnapshot != null) {

                    UserData retrievedData = documentSnapshot.toObject(UserData.class);

                    //check if there is data stored and not null, this will be null if the user is new
                    //if there is data stored then display it, otherwise create a user document in Firestore so that it will be used to display data
                    if (retrievedData != null) {

                        displayDatabaseInfo(retrievedData);

                    } else {

                        createUserProfile();
                    }
                }
            }
        });

        //set OnNavigationItemSelectedListener on navigationView object for handling events on navigation items.
        navigationView.setNavigationItemSelectedListener(this);

        // Declaring and initializing a toggle object variable. String resources must be provided to describe the open/close drawer actions for accessibility services.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Attach the toggle to the mDrawerLayout through the addDrawerListener to notify when drawer events occur
        mDrawerLayout.addDrawerListener(toggle);

        // Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout.
        toggle.syncState();

        //In case the screen orientation changed from portrait to landscape mode, the savedInstanceState won't be null and the system won't
        //recreate the StartGameFragment again and the user would still be in the fragment he/she selected. The StartGameFragment is the
        // main/first fragment you see when the MainActivity is opened
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartGameFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_new_game);
        }
    }

    /**
     * This method displays the name, email, and photo of the user stored in the user document in FirebaseFirestore database
     *
     * @param retrievedData UserData: the user data retrieved from the document with userId as its name inside Users collection in FirebaseFirestore database
     */
    private void displayDatabaseInfo(UserData retrievedData) {

        String retrievedName = retrievedData.getUserDisplayName();

        String retrievedEmail = retrievedData.getUserEmail();

        userDisplayedName.setText(retrievedName);

        userDisplayedEmail.setText(retrievedEmail);

        //Check if there is a photo because if the user signed up with the email option which doesn't require a photo and then did not update his/her profile photo
        // the retrievedData.getUserPhoto() will return null and we will leave the default avatar to be the one displayed
        if (retrievedData.getUserPhoto() != null) {

            String retrievedPhoto = retrievedData.getUserPhoto();

            Glide.with(this).load(retrievedPhoto).into(userDisplayedPhoto);
        }
    }

    /**
     * This method creates a user document in the FirebaseFirestore database in case the user is a new one. In this case
     * we will display the data that is obtained from the FirebaseAuth instance instead.
     */
    private void createUserProfile() {

        String userName = mCurrentUser.getDisplayName();

        String userEmail = mCurrentUser.getEmail();

        //check if the user has a photo because if the user has signed up with the email option, he/she won't have a photo to be displayed
        String userProfilePhoto = null;

        if (mCurrentUser.getPhotoUrl() != null) {

            userProfilePhoto = mCurrentUser.getPhotoUrl().toString();
        }

        //Declaring and initializing a userData Object Variable that stores the user name, email, and photo if exists to be passed to the database
        UserData userData = new UserData(userName, userEmail, userProfilePhoto);

        //Storing UserData by creating a User Document in Firestore database that will include the user name, the email, and the profile photo if it exists.
        //The user data will be stored in a document and the name of the document is the unique userId in a collection called "Users
        userDocumentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SignUpActivity", "User data is saved in Firestore successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("SignUpActivity", e.toString());

            }
        });
    }

    /**
     * OnBackPressed() is called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            //Close the specified drawer mDrawerLayout by animating it out of view.
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    /**
     * onNavigationItemSelected is called when an item in the navigation menu is selected.
     *
     * @param item MenuItem: The selected item
     * @return boolean: true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_new_game:

                //Open the StartGameFragment by adding the fragment to the activity during the activity runtime.
                //Declare and initialize a startGameFragment instance
                StartGameFragment startGameFragment = new StartGameFragment();

                //Call getSupportFragmentManager() to get a FragmentManager using the Support Library APIs.
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Call beginTransaction() to create a FragmentTransaction and call replace() to add the fragment to
                // the 'fragment_container' FrameLayout then call commit().
                fragmentManager.beginTransaction().replace(R.id.fragment_container, startGameFragment).commit();
                break;
            case R.id.nav_how_to:
                Intent intent = new Intent(MainActivity.this, IntroductionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_saved_games:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SavedGamesFragment())
                        .addToBackStack(null).commit();
                break;
        }
        //Close the specified drawer mDrawerLayout by animating it out of view.
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

