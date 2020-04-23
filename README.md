# TrackIt App

This is the repository for the Game Counter App Project required as Project Number Two in Udacity Nanodegree Program.

### App Description

TrackIt App is a Soccer Counter App that allows the user to track the Goals and the Fouls of his/her Soccer Team in real time 
in addition to tracking the Goals and the Fouls of the Guest Team. The user can save the Game Results and this will allow the
user to view all the saved games ordered by date and choose which result he/she would like to share with friends.

### App Features

The App has different screens and features starting with a Splash Screen which is the first startup screen that appears when 
the TrackIt App is opened.

### The Splash Screen
It is a simple constant screen that displays for a fized amount of time basically 3000 seconds which is used to display the TrackIt
App Logo in an animated way.

### The Splash Screen displays as below:


### The Introduction Wizard (How to use TrackIt Guide)

After the Splash Screen,  the user is redirected to an introduction wizard that explains how to use TrackIt App in the form of
five simple steps with the images of the App’s Functionalities attached in these steps. If the user clicked on the “Ok, Got It” Button,
the user will be then redirected to a SignUp Screen where he/she can sign up either by Google or Email as Sign In Providers. The user
can also navigate back to the introduction wizard guide if he/she wants to check it out again frtom within the App's Navigation Drawer.

### Introduction Wizard Screenshot:

### TrackIt Sign Up Process:

The TrackIt App Sign Up Process is built using the FirebaseUI Authentication which provides the user with two Sign-In Providers’ Options
which are Google and Email. If the user chooses to sign up with the Google Option, the process will be as follows, first he will she 

###Firebase Authentication Screenshot:

### Signing Up using Google:

However, if the user chooses to signs up with the Email Option and the user was a new one (hasn’t signed before), the authentication process will
ask the user to enter an email, first and last name, and a password. Then the user will be directed to the main screen where he/she can start 
a new game or navigate to other sections in the app. 

### Signing Up using Email:

 When the user signs up with either option, the user data is extracted from the Firebase Auth instance and a user profile document
 is created and stored in Firebase Firestore database so that we can display the user info in different 
 places in the app using this document and also so that when the use wants to update his/her profile, the updated data will be merged
 with the currently existing user data stored in this document replacing it. Each user document is named after the unique 
 userId and stored inside a collection called “Users” as displayed in the screenshot below.
 
 ### FireStore Database Screenshot:





