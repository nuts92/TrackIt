package com.example.android.trackit.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * //note that the format here is imp for firestore to work properly like for example the empty constructor
 * //A few things to note about this model class:
 * //The getters and setters follow the JavaBean naming pattern which allows Firestore to map the data to field names (ex: getName() provides the name field).
 * //The class has an empty constructor, which is required for Firestore's automatic data mapping.
 */

public class SavedGame {

    private String homeTeam;

    private String awayTeam;

    private Date mTimestamp;

    private int homeScore;

    private int awayScore;

    private int homeFouls;

    private int awayFouls;

    private String winner;

    public SavedGame() {
        //empty constructor is required for Firestore's automatic data mapping.
    }


    public SavedGame(String homeTeam, String awayTeam, int homeScore, int awayScore, int homeFouls,
                     int awayFouls, String winner) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.homeFouls = homeFouls;
        this.awayFouls = awayFouls;
        this.winner = winner;

    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getHomeFouls() {
        return homeFouls;
    }

    public void setHomeFouls(int homeFouls) {
        this.homeFouls = homeFouls;
    }

    public int getAwayFouls() {
        return awayFouls;
    }

    public void setAwayFouls(int awayFouls) {
        this.awayFouls = awayFouls;
    }

    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
