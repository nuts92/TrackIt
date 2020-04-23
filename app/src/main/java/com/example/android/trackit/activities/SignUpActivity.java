package com.example.android.trackit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.trackit.R;
import com.example.android.trackit.models.UserData;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

/**
 * This class displays both the SignUp and SignIn Authentication Process Screen provided by FirebaseUI where the user
 * chooses to complete either by Google or Email as SignIn Providers
 */
public class SignUpActivity extends AppCompatActivity {

    //Declaring and initializing an arbitrary request code value
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Declaring and initializing an instance of FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Check if user is already signed in (non-null) from a previous session and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // already signed in
            updateUI(currentUser);

        } else {
            //the user isn't signed in then start the authentication process and the FirebaseUI sign-in flow process
            authenticateUser();
        }
    }

    /**
     * authenticateUser() method starts the FirebaseUI SignIn Flow Process by specifying the authentication providers and creating and
     * launching a signIn intent
     */
    private void authenticateUser() {

        //Providing a custom layout XML resource to be used instead of the default one and configuring at least one provider button Id.
        final AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.activity_sign_up)
                .setGoogleButtonId(R.id.google_log_in_button)
                .setEmailButtonId(R.id.email_log_in_button)
                .build();

        //Creating a signIn intent by getting an instance of AuthUI then a builder instance can be retrieved by calling createSignInIntentBuilder() on
        //the retrieved AuthUI instance. Then Enable sign-in providers like Google or Email SignIn by calling the setAvailableProviders method
        // In addition, disable the smartLock Feature, set the customLayout and set the custom theme then call build().
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .setAuthMethodPickerLayout(customLayout).setTheme(R.style.FullScreenTheme)
                .build();

        //To start the FirebaseUI SignIn flow, call startActivityForResult on the SignIn Intent. The second parameter (RC_SIGN_IN)
        // is the request code that was declared to identify the request when the result is returned in onActivityResult().
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * onActivityResult method is called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode int: The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  int: The integer result code returned by the child activity through its setResult().
     * @param data        Intent: An Intent, which can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //RC_SIGN_IN is the request code that was passed into startActivityForResult() when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {

            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                //The user is successfully signed in
                Intent startIntent = new Intent(this, MainActivity.class);
                startActivity(startIntent);
                Toast.makeText(this, "You are Logged In", Toast.LENGTH_SHORT).show();
                finish();

            } else {

                if (response == null) {
                    //The User pressed the back button
                    Toast.makeText(this, "Logging In is Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                if (response.getError() != null) {
                    //There is no internet connection
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                //In case of any other error that may occur
                Toast.makeText(this, "Logging in cancelled because of Unknown error", Toast.LENGTH_SHORT).show();
                Log.e("SignUpActivity", "Log in error: ", response.getError());
            }
        }
    }

    /**
     * This method updates the UI by redirecting the user to the MainActivity and displaying a Welcome Toast Message
     * in case the current user isn't null and already signed in from a previous session
     *
     * @param currentUser the currently signed in user from a previous session
     */
    private void updateUI(FirebaseUser currentUser) {

        //Declaring and initializing an instance of FirebaseFirestore Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting the user data from the Firestore database where the user data is stored in a document named by the userId in a collection called "Users"
        //If the user has signed in before, then a document will be already created in database by createUserProfile method in the MainActivity
        db.collection("Users").document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null) {

                    //Retrieve the user data by creating a userData object from the documentSnapshot which is basically a snapshot of the user document
                    UserData userData = documentSnapshot.toObject(UserData.class);

                    if (userData != null) {

                        //Declaring and initializing a userName variable that will be used in creating a Welcome Toast Message
                        String userName = userData.getUserDisplayName();

                        Toast.makeText(SignUpActivity.this, userName + "! Are You Ready For a New Game?", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("SignUpActivity", e.toString());
            }
        });

        //Starting the MainActvity and finishing the SignUpActivity
        Intent startGameIntent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(startGameIntent);
        finish();
    }
}
