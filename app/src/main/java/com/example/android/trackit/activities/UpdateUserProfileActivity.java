package com.example.android.trackit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.trackit.R;

/**
 * This class displays the EditProfile Screen where the user can update his/her profile by either
 * changing the profile photo, name, self introduction or all of them.
 */
public class UpdateUserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        //set the title of the toolbar to Update Profile
        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle("Update Profile");
        }
    }

    /**
     * This method is called whenever an item in the options menu is selected.
     *
     * @param item MenuItem: The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Let the user tap the home/up button to go back. If the item that was selected was the home/Up button
        // then finish this activity, otherwise allow normal menu processing to proceed
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
