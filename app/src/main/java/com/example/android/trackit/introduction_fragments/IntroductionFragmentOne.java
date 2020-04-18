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
 * A simple {@link Fragment} subclass that represents the first step in the introduction wizard.
 */
public class IntroductionFragmentOne extends Fragment {


    public IntroductionFragmentOne() {
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

        //Setting the right image and texts for the ImageView and two TextViews object variables in introduction wizard step one
        introductionImage.setImageResource(R.drawable.designed_intro_one_zoom);
        introductionTitle.setText(R.string.Introduction_step_one_title);
        introductionDescription.setText(R.string.introduction_step_one_description);

        return rootView;
    }

}
