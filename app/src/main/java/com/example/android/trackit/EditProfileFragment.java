package com.example.android.trackit;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.trackit.models.UserData;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

/**
 * EditProfileFragment subclass displays a screen where the user can update his/her photo, name, and self introduction.
 */
public class EditProfileFragment extends Fragment {

    //Declaring and initializing an arbitrary request code constant value
    private static final int PICK_IMAGE_REQUEST = 1;

    //Declaring all Object Variables
    private ImageView mDisplayedUserPhoto;

    private EditText mUserNameEditText;

    private EditText mUserIntroductionEditText;

    private FirebaseFirestore db;

    private String userId;

    private Uri chosenPhotoUri;

    private UserData updatedUserData;

    private String downloadUrl;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_profile, container, false);

        //Initializing the mDisplayedUserPhoto, userNameEditText, and userIntroductionEditText object variables
        mDisplayedUserPhoto = rootView.findViewById(R.id.edit_user_profile_photo);

        mUserNameEditText = rootView.findViewById(R.id.edit_name_view);

        mUserIntroductionEditText = rootView.findViewById(R.id.edit_intro_view);

        //Declaring and initializing the updatePhotoButton and the saveProfileButton object variables
        ImageView mUpdatePhotoButton = rootView.findViewById(R.id.edit_user_photo_picker_button);

        Button mSaveProfileButton = rootView.findViewById(R.id.save_profile_button);

        //Declaring and Initializing an instance of FirebaseFirestore database
        db = FirebaseFirestore.getInstance();

        //Declaring and Initializing an instance of FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Declaring and initializing an instance of FirebaseUser
        FirebaseUser currentUser = auth.getCurrentUser();

        //Get the currentUserId from the currentUser which will be used to get data from FirebaseFirestore database
        if (currentUser != null) {

            userId = currentUser.getUid();
        }

        //Initializing the updatedUserData object variable which is currently an empty object which will be filled later
        updatedUserData = new UserData();

        //Get the user document that contains information including name, email, self introduction, photo, etc.
        // the user data is stored in a document and the name of the document is the unique userId in a collection called "Users"
        db.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null) {

                    //Retrieve the user data by creating a userData object from the documentSnapshot which is basically the user document
                    UserData userData = documentSnapshot.toObject(UserData.class);

                    if (userData != null) {

                        //Update the value of the empty updatedUserData to the retrieved userData object variable
                        updatedUserData = userData;

                        //Check if there is a photo because if the user signed up with the email option and then did not update his/her profile photo
                        // the userData.getUserPhoto() will return null and we will leave the default avatar to be the one displayed
                        if (userData.getUserPhoto() != null) {

                            Glide.with(EditProfileFragment.this).load(updatedUserData.getUserPhoto()).into(mDisplayedUserPhoto);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("EditProfileFragment", e.toString());
            }
        });

        //Attaching an OnClickListener to the mUpdatePhotoButton that determines the behavior that will happen
        // when the user clicks on that button
        mUpdatePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //Attaching an OnClickListener to the mSaveProfileButton that determines the behavior that will happen
        // when the user clicks on that button
        mSaveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        return rootView;

    }

    /**
     * This method opens a photo picker or a chooser for the user to pick the photo that will be displayed as the profile photo
     */
    private void openFileChooser() {

        //Creating an intent object and setting its type and action, then passing it as an input argument along with
        //the PICK_IMAGE_REQUEST code previously declared to the startActivityForResult
        Intent intent = new Intent();

        //setType to image/* so that only images will show up
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * Receive the result from a previous call to startActivityForResult(Intent, int).
     *
     * @param requestCode int: The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode  int: The integer result code returned by the child activity through its setResult().
     * @param data        Intent: An Intent, which can return result data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the user actually chose an image
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            //chosenPhotoUri is the Uri of the image the user has picked
            chosenPhotoUri = data.getData();

            //Load the image to be displayed in mDisplayedUserPhoto ImageView
            Glide.with(this).load(chosenPhotoUri).into(mDisplayedUserPhoto);
        }
    }

    /**
     * This method saves the user profile updates made by the user and stores it in the Firestore database so that the user
     * is informed that the data is saved through a Toast message and updated in addition to closing the UpdateUserProfileActivity
     */
    private void saveProfile() {

//        uploadImageFile();

        String updatedName = mUserNameEditText.getText().toString().trim();
        String updatedIntroduction = mUserIntroductionEditText.getText().toString().trim();


        if (!TextUtils.isEmpty(updatedName)) {
            updatedUserData.setUserDisplayName(updatedName);
        }


        if (!TextUtils.isEmpty(updatedIntroduction)) {
            updatedUserData.setUserIntroduction(updatedIntroduction);
        }

        if (chosenPhotoUri != null) {
//
//            if the user picked a file then we want to ulpoad it to the firestore database
//            files in the storage should have unique names to avoid overriding thefiles
//            to have a unique name for each file, we can achieve this by naming the file based on the
//            current time in milliseconds/ this will create a file name like users uploads/4217371773371839.jpg

            StorageReference mStorageReference = FirebaseStorage.getInstance().getReference("users uploads");

            final StorageReference fileReference = mStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(chosenPhotoUri));

            fileReference.putFile(chosenPhotoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Get a URL to the uploaded content taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

//                            String downloadUrl = "";

//                            if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
//
//                                downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                                updatedUserData.setUserPhoto(downloadUrl);
//                                db.collection("Users").document(userId).set(updatedUserData, SetOptions.merge());
//                            }

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadUrl = uri.toString();
                                    updatedUserData.setUserPhoto(downloadUrl);
                                     saveData();
//                                    db.collection("Users").document(userId).set(updatedUserData, SetOptions.merge());

                                }
                            });
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle unsuccessful uploads
                    Log.e("EditProfileFragment", e.toString());
                    e.printStackTrace();
                }
            });
        } else {

            saveData();

        }

    }

    private void saveData() {

        db.collection("Users").document(userId).set(updatedUserData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void aVoid) {
                        //if uploading data to database is successful
                        Log.d("EditProfileFragment", "User Data is updated and saved Successfully");


                        if (getActivity() != null) {

                            getActivity().setResult(RESULT_OK);

                            getActivity().finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("EditProfileFragment", e.toString());

            }
        });
    }

    /**
     * This method will get the file extension from the image uri chosen and convert it to something like jpg for example
     * @param uri Uri: the image Uri chosen by the user when he/she opened the photo picker
     * @return String: Returns the formatted image file extension
     */
    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver = null;

        if(getActivity() != null){

         contentResolver = getActivity().getContentResolver();
        }
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        String mimeType = null;

        if(contentResolver != null) {

            mimeType = contentResolver.getType(uri);
        }
        return mime.getExtensionFromMimeType(mimeType);
    }

    /**
     * This method uploads the chosen photo by the user to Firebase Firestorage and then if uploaded successfully to FireStorage
     * it will be then stored in Firestore database
     */
    private void uploadImageFile() {


    }
}
