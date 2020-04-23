# TrackIt App

This is the repository for the Game Counter App Project required as Project Number Two in Udacity Nanodegree Program.

### App Description

TrackIt App is a Soccer Counter App that allows the user to track the Goals and the Fouls of his/her Soccer Team in real time 
in addition to tracking the Goals and the Fouls of the Guest Team. The user can save the Game Results and this will allow the
user to view all the saved games ordered by date and choose which result he/she would like to share with friends.

### App Design

The App has different screens and features which are as follows:

### The Splash Screen

When the user opens the TrackIt App, it starts with a Splash Screen which is the first startup screen that appears when 
the TrackIt App is opened.It is a simple constant screen that is displayed for a fixed amount of time basically 3000 seconds which is used to display the TrackIt App Logo in an animated way.

### The Splash Screen displays as below:

<img src="https://media.giphy.com/media/Woop2FQ4k7DQfY2Ekj/giphy.gif" width="300">


### The Introduction Wizard (How to use TrackIt Guide)

After the Splash Screen,  the user is redirected to an introduction wizard that explains how to use TrackIt App in the form of
five simple steps with the images of the App’s Functionalities attached in these steps. If the user clicked on the “Ok, Got It” Button, the user will be then redirected to a SignUp Screen where he/she can sign up either by Google or Email as Sign In Providers. The user can also navigate back to the introduction wizard guide if he/she wants to check it out again frtom within the App's Navigation Drawer.

### Introduction Wizard Screenshots:

<img src="https://i.imgur.com/1X5NR71.png" width="300"> <img src="https://i.imgur.com/1LnNPYG.png" width="300"> <img src="https://i.imgur.com/pFwkRdQ.png" width="300"> <img src="https://i.imgur.com/UeOb72z.png" width="300"> <img src="https://i.imgur.com/q5DWIWH.png" width="300"> 

### TrackIt Sign Up Process:

The TrackIt App Sign Up Process is built using the FirebaseUI Authentication that provides the user with two Sign-In Providers’ Options which are Google and Email. 

### TrackIt Sign Up displays as below:

<img src="https://media.giphy.com/media/Wt0CfQT8j2X9ISvbJE/giphy.gif" width="400">

### Signing Up using Google:
If the user chooses to sign up with the Google Option,authentication process will ask the user to  sign up with his/her Google Account and enter his/her google email and password. Also, there are additional useful features in case the user forgot the password, want to sign up with another Goggle Account or create a new Google Account.

### Google Sign Up Screenshots:

<img src="https://i.imgur.com/KuWLAYN.png" width="300"> <img src="https://i.imgur.com/XplZmus.png" width="300"> <img src="https://i.imgur.com/pfnidZB.png" width="300"> 

### Signing Up using Email:

However, if the user chooses to signs up with the Email Option and the user was a new one (hasn’t signed before), the authentication process will ask the user to enter an email, first and last name, and a password. Then the user will be redirected to the main screen where he/she can start a new game or navigate to other sections in the app. 

### Email Sign Up Screenshots:

<img src="https://i.imgur.com/3nP7WS3.png" width="300"> <img src="https://i.imgur.com/LExuRZY.png" width="300"> 
<img src="https://i.imgur.com/pfnidZB.png" width="300"> 

 When the user signs up with either option, the user data is extracted from the Firebase Auth instance and a user profile document is created and stored in Firebase Firestore database so that we can display the user information in different places in the App using this document and also so that when the user wants to update his/her profile, the updated data will be merged
 with the currently existing user data stored in this document replacing it. Each user document is named after the unique 
 User Id and stored inside a collection called “Users” as displayed in the screenshot below.
 
 ### FireStore Database Users Collection Screenshot:
 
  <img src="https://i.imgur.com/wrbzccs.png">  <img src="https://i.imgur.com/MqWLlx4.png">
  
  ### Starting A New Game Process:
  
Now the user has signed up successfully and logged in to the MainActivity. The MainActivity consists of a Navigation Drawer where the user can navigate between different sections(fragments) of the App includeing Start a New Game, How to use TrackIt Guide, User Profile, and Saved Games List. The first and main screen that appears to the user when he/she signs in is the Start A New Game Screen as shown below. When the user clicks on the Start button, he/she is redirected to a screen where he/she can update the teams' names. Whether the user chooses to update the teams' names or skip this part, he/she will be redirected to the Soccer Counter screen. 

  ### Starting A New Game Process Screenshots:
  
   <img src="https://media.giphy.com/media/eH9f28djsTny4Rvf9U/giphy.gif" width="300"> <img src="https://i.imgur.com/4TXRdGH.png" width="300"> <img src="https://i.imgur.com/5rLPGzH.png" width="300"> 
   
 ### Soccer Counter Screen:
 
Here the user can track the goals and fouls of both the home team and the away team in real time. When the user is done, he/she can choose to save the game result. When the user saves the game result, a popup card shows up with an animation based on the game result and a call to action that prompts the user to start a new game. Also, A toast message is displayed showing that the game is saved. The goal and foul buttons are disabled, the save game button disappears and the reset button is replaced with start a new game button.

 ### Soccer Counter Screen Game Results:

<img src="https://i.imgur.com/tGOqVd2.png" width="300"> <img src="https://i.imgur.com/nXWJUM6.png" width="300"> <img src="https://i.imgur.com/Wpr62Kq.png" width="300"><img src="https://i.imgur.com/OgfXE0X.png" width="300"> 
 
 ### Firebase Authentication Screenshot:
 <img src="https://i.imgur.com/BWknrKQ.png" width="300"> <img src="https://i.imgur.com/ZYmQ4Us.png" width="300">
 







