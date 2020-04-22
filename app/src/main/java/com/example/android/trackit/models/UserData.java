package com.example.android.trackit.models;

import android.net.Uri;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * This class is a Data Model Class that stores the User Data and it follows the JavaBean naming pattern which allows Firestore
 * to automatically map values when getting and setting data. The rules that the pattern follows are that:
 * Getter and Setter methods are defined which are strictly named to what variable they correspond to (for example: getName() provides the name field).
 * An extra empty constructor is also defined which lets Firestore do the automatic data mapping.
 */
public class UserData {

    private String userDisplayName;

    private String userEmail;

    private String userPhoto;

    private String userIntroduction;

    private Date timestamp;

    public UserData() {
        //empty constructor is required for Firestore's automatic data mapping.
    }

    public UserData(String userDisplayName, String userEmail, String userPhoto) {
        this.userDisplayName = userDisplayName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserIntroduction() {
        return userIntroduction;
    }

    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction;
    }

    @ServerTimestamp
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
