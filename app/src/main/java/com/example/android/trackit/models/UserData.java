package com.example.android.trackit.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

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

    public UserData(String userDisplayName, String userEmail, String userPhoto, String userIntroduction) {
        this.userDisplayName = userDisplayName;
        this.userEmail = userEmail;
        this.userPhoto = userPhoto;
        this.userIntroduction = userIntroduction;
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
