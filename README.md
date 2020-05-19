# TrackIt App

This is the repository for the Game Counter App Project required by the Udacity Nanodegree Program.

### App Description

TrackIt App is a Soccer Counter App that allows the user to track the Goals and the Fouls of his/her Soccer Team in real time 
in addition to tracking the Goals and Fouls of the Guest Team. The user can save the Game Results and view all the saved games ordered by date and choose which result he/she would like to share with friends.

### App Design

The App has multiple screens and features which are as follows:

### The Splash Screen

When the user opens the TrackIt App, it starts with a Splash Screen which is the first startup screen that appears when 
the TrackIt App is opened. It is a simple constant screen that is displayed for a fixed amount of time basically 3000 seconds which is used to display the TrackIt App Logo in an animated way as shown below.

### The Splash Screen Screenshot

<img src="https://media.giphy.com/media/Woop2FQ4k7DQfY2Ekj/giphy.gif" width="300">

### The Introduction Wizard (How to use TrackIt Guide)

After the Splash Screen,  the user is redirected to an introduction wizard that explains how to use TrackIt App in the form of
five simple steps with the images of the App’s Functionalities attached in these steps. If the user clicked on the “Ok, Got It” Button, the user will be then redirected to a SignUp Screen where he/she can sign up either by Google or Email as Sign In Providers. The user can also navigate back to the introduction wizard guide if he/she wants to check it out again from within the App's Navigation Drawer.

### The Introduction Wizard Screenshots

<img src="https://i.imgur.com/1X5NR71.png" width="300"> <img src="https://i.imgur.com/1LnNPYG.png" width="300"> <img src="https://i.imgur.com/pFwkRdQ.png" width="300"> <img src="https://i.imgur.com/UeOb72z.png" width="300"> <img src="https://i.imgur.com/q5DWIWH.png" width="300"> 

### The TrackIt Sign Up Process

The TrackIt App Sign Up Process is built using the FirebaseUI Authentication that provides the user with two Sign-In Providers’ Options which are Google and Email. 

### The TrackIt Sign Up Screenshot

<img src="https://media.giphy.com/media/Wt0CfQT8j2X9ISvbJE/giphy.gif" width="300">

### Signing Up using Google

If the user chooses to sign up with Google Option, the authentication process will ask the user to  sign up with his/her Google Account and enter his/her google email and password. Also, there are additional useful features in case the user forgot the password, want to sign up with another Goggle Account or create a new Google Account.

### Google Sign Up Screenshots

<img src="https://i.imgur.com/KuWLAYN.png" width="300"> <img src="https://i.imgur.com/XplZmus.png" width="300"> <img src="https://i.imgur.com/pfnidZB.png" width="300"> 

### Signing Up using Email

However, if the user chooses to sign up with the Email Option and the user was a new one (hasn’t signed before), the authentication process will ask the user to enter an email, first and last name, and a password. Then the user will be redirected to the main screen where he/she can start a new game or navigate to other sections in the App. 

### Email Sign Up Screenshots

<img src="https://i.imgur.com/3nP7WS3.png" width="300"> <img src="https://i.imgur.com/LExuRZY.png" width="300"> 
<img src="https://i.imgur.com/pfnidZB.png" width="300"> 

Once the user signs up with either Google or Email option, the user data is extracted from the Firebase Auth instance and a user document is created and stored in Firebase Firestore database so that we can display the user information in different places in the App using this document and also so that when the user wants to update his/her profile, the updated data will be merged with the currently existing user data stored in this document and the new data will be the one displayed. Each user document is named after the unique User Id and stored inside a collection called “Users” as displayed in the screenshot below. So there is a collection called Users and inside this collection there are documents, one for each user and inside each document is another collection called Saved Games.
 
### FireStore Database Users Collection Screenshot
 
  <img src="https://i.imgur.com/wrbzccs.png"> 
  
### Starting A New Game Process
  
Now the user has signed up successfully and logged in to the MainActivity. The MainActivity consists of a Navigation Drawer where the user can navigate between different sections(fragments) of the App includeing Start a New Game, How to use TrackIt Guide, User Profile, and Saved Games List. The first and main screen that appears to the user when he/she signs in is the Start A New Game Screen as shown below. When the user clicks on the Start button, he/she is redirected to a screen where he/she can update the teams' names. Whether the user chooses to update the teams' names or skip this part, he/she will be redirected to the Soccer Counter screen. 

### Starting A New Game Process Screenshots
  
<img src="https://media.giphy.com/media/eH9f28djsTny4Rvf9U/giphy.gif" width="300"> <img src="https://media.giphy.com/media/JsUuAiN2kw1DLY8GvY/giphy.gif" width="300"> <img src="https://media.giphy.com/media/ZZMg0iDeTt6MJCncVj/giphy.gif" width="300"> 
   
 ### Soccer Counter Screen
 
Here the user can track the goals and fouls of both the home team and the away team in real time. When the user is done, he/she can choose to save the game result. When the user saves the game result, a popup card shows up with an animation based on the game result and a call to action that prompts the user to start a new game. Also, A toast message is displayed showing that the game is saved. After that, the goal and foul buttons are disabled, the save game button disappears and the reset button is replaced with start a new game button as shown below.

 ### Soccer Counter Screen Game Results

<img src="https://media.giphy.com/media/TJfSUxPjeLuwyvpJI6/giphy.gif" width="300"> <img src="https://media.giphy.com/media/dWkmz9xXymxZf8KfAJ/giphy.gif" width="300"> <img src="https://media.giphy.com/media/hScfE2EAIxjinPOV59/giphy.gif" width="300"> <img src="https://media.giphy.com/media/f7NCKuYhHZkWemIAXK/giphy.gif" width="300"> 

The game counter also takes into consideration configuration changes like rotating the device to the landscape mode so that the scores and fouls that the user has entered won't be lost.

 <img src="https://i.imgur.com/g5JP54B.png" width="500"> <img src="https://i.imgur.com/up4j2mU.png" width="300">
 
### Saved Games List
  
Now after the user clicked on the "Save Game" button, the game will be saved in the Firestore database in the Saved Games collection which is a collection inside the user document as shown below.
  
  <img src="https://i.imgur.com/MqWLlx4.png">
  
If the user wants to view the saved games, he/she should click on the hamburger icon at the top in the toolbar and the navigation drawer will open, then the user can select saved games option and all the saved games will be displayed in the form of cards ordered by timestamp or by date showing the latest game result first.

### Saved Games Screenshots

   <img src="https://i.imgur.com/N4HxAdq.png" width="300"> <img src="https://i.imgur.com/pihIf2M.png" width="300">
   
### Share Option
  
The user has the option to share the game result with friends by clicking on the share button, the Android Sharesheet will open with a wide variety of different apps to share the result on and the user can choose from these Apps that range from Email Apps to Social Networking Apps. The game result message will be as shown below a text message summarizing the game result including the home team name, the away team name, the winner, the date, the number of goals, and the number of fouls for each team.
  
### Share option Screenshot
  
   <img src="https://i.imgur.com/RbnebRQ.png" width="300">  <img src="https://i.imgur.com/tcliFu3.png" width="300"> 
   
### User Profile
  
Every user has a profile where the name and the email are displayed along with the profile photo if the user has signed up with the Google option. However, if the user has signed up with the Email option then the displayed photo in the user profile will be the default avatar. Also, every user can have a small self introduction about him/her self and if the user is new meaning signed up for the first time, the self introduction displayed will be the default one as shown below. Every user can choose to update the profile by clicking on the update button and now he/she can choose a profile photo, a display name, and a self introduction that he/she likes. The new updates will be saved in Firestore database and displayed in the user profile in addition to the navigation view header as shown below.

### User Profile Screenshots
  
<img src="https://i.imgur.com/b7cea32.png" width="300"> <img src="https://i.imgur.com/OPze6Vr.png" width="300">  <img src="https://i.imgur.com/jzqwxzV.png" width="300"> <img src="https://i.imgur.com/V2aywKN.png" width="300"> <img src="https://i.imgur.com/VGCZoMT.png" width="300">

### Creative Assets Copyrights
All the drawable resources used in this project are all MIT Licensed which are free of Copyrights and can be used without attributions.

### Dowload the App
To use this repository, fork/clone it, or download a zip using the green "Clone or download" button at the top of the file list.  

### License

Copyright 2020 Doha Nabil Saad Kash

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.



   
   
  

  
  
   
   
  
  



 
 

 
 







