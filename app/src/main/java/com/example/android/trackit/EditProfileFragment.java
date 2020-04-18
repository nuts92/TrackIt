package com.example.android.trackit;


import android.content.ContentResolver;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseFirestore db;

    private String userId;

    private Uri mChosenPhotoUri;

    private ImageView mDisplayedUserPhoto;

    private StorageReference mStorageReference;

    private UserData updatedUserData;


    private String mDownloadUrl;

    private EditText userNameEditText;

    private EditText userEmailEditText;

    private EditText userIntroductionEditText;

    private String currentUserName;

    private String currentUserEmail;

    private String currentUserIntroduction;

    private String currentUserPhotoUrl;



    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_edit_profile, container, false);

        mDisplayedUserPhoto = rootView.findViewById(R.id.edit_user_profile_photo);

        ImageView updatePhotoButton = rootView.findViewById(R.id.edit_user_photo_picker_button);

        userNameEditText = rootView.findViewById(R.id.edit_name_view);
        userEmailEditText = rootView.findViewById(R.id.edit_email_view);
        userIntroductionEditText = rootView.findViewById(R.id.edit_intro_view);

        Button saveProfileButton = rootView.findViewById(R.id.save_profile_button);

        db = FirebaseFirestore.getInstance();

        mStorageReference = FirebaseStorage.getInstance().getReference("users uploads");

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

        updatedUserData = new UserData();

        if (currentUser != null) {

            userId = currentUser.getUid();

        }


            db.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    UserData userData = documentSnapshot.toObject(UserData.class);
                    if (userData != null) {

                        //null in case of signing in with email/ leave the default avatar
                        if(userData.getUserPhoto() != null) {
                            currentUserPhotoUrl = userData.getUserPhoto();
                            Glide.with(EditProfileFragment.this).load(currentUserPhotoUrl).into(mDisplayedUserPhoto);
                        }

                        //always there ISA/ put in userprofilefragment
                        currentUserName = userData.getUserDisplayName();
                        currentUserEmail = userData.getUserEmail();
                        currentUserIntroduction = userData.getUserIntroduction();

                    }
                }
            });

        updatePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
              }
        });

        return rootView;

    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*"); //only images will show up
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the user actually chose an image

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            //this is the uri of the image the user has picked
            mChosenPhotoUri = data.getData();
            //load the image to be displayed in mdisplayedUserPhoto ImageView
            Glide.with(this).load(mChosenPhotoUri).into(mDisplayedUserPhoto);
        }
    }

    private void saveProfile(){

        String updatedName = userNameEditText.getText().toString().trim();
        String updatedEmail = userEmailEditText.getText().toString().trim();
        String updatedIntroduction = userIntroductionEditText.getText().toString().trim();

        boolean isValidEmail = validateEmail(updatedEmail);


        if (!TextUtils.isEmpty(updatedName)) {
            updatedUserData.setUserDisplayName(updatedName);
        } else {
            updatedName = currentUserName;
        }

//        if (!isValidEmail) {
//            userEmailEditText.setError("InValid Email");
//            return;
//        }

       if (!TextUtils.isEmpty(updatedEmail)) {
            updatedUserData.setUserEmail(updatedEmail);
        }  else {
           updatedEmail = currentUserEmail;
       }

        if (!TextUtils.isEmpty(updatedIntroduction)) {
            updatedUserData.setUserIntroduction(updatedIntroduction);
        } else {
            //the default one
            updatedIntroduction = currentUserIntroduction;
        }


        db.collection("Users").document(userId).set(updatedUserData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void aVoid) {
                        //if uploading data to database is successful
                        Log.d("EditProfileFragment", "User Data is saved Successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("EditProfileFragment", e.toString());

            }
        });
//        Bundle bundle = new Bundle();
//        bundle.putString("updatedUserName", updatedName);
//        bundle.putString("updatedUserEmail", updatedEmail);
//        bundle.putString("updatedUserIntro", updatedIntroduction);

        UserProfileFragment userProfileFragment = new UserProfileFragment();
//        userProfileFragment.setArguments(bundle);

        if(getActivity()!= null) {

            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            // replace the FrameLayout with new Fragment
            fragmentTransaction.replace(R.id.fragment_container, userProfileFragment);
            fragmentTransaction.commit(); //Save the Changes

        }


    }

    //this method will get the file extenision from the image uri like jpg for example

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean validateEmail(String updatedEmail) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = updatedEmail;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
