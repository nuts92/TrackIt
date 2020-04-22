package com.example.android.trackit.introduction_fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.trackit.R;

/**
 * IntroductionFragmentFour subclass represents the fourth step in the introduction wizard.
 */
public class IntroductionFragmentFour extends Fragment {

    public IntroductionFragmentFour() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.introduction_fragment, container, false);

        //Declaring and initializing all object variables
        ImageView introductionImage = rootView.findViewById(R.id.introduction_image);
        TextView introductionTitle = rootView.findViewById(R.id.title_text);
        TextView introductionDescription = rootView.findViewById(R.id.description_text);

        //Setting the right image and texts for the ImageView and two TextViews object variables in introduction wizard step four
        introductionImage.setImageResource(R.drawable.introduction_step_four);
        introductionTitle.setText(R.string.Introduction_step_four_title);
        introductionDescription.setText(R.string.introduction_step_four_description);

        return rootView;
    }
}
