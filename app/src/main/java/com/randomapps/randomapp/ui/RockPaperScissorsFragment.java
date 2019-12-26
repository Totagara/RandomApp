package com.randomapps.randomapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.enums.RockPaperScissorsType;

import random.RandomLibrary;

/**
 * A simple {@link Fragment} subclass.
 */
public class RockPaperScissorsFragment extends Fragment {
    //Random Library
    RandomLibrary randomLib;

    //Rock-Paper-Scissors UI elements
    TextView rpsNameTextView;
    Spinner rpsTypeSpinner;
    Button generateButton;
    ImageView rpsImageView;

    public RockPaperScissorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        randomLib = new RandomLibrary();
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_rock_paper_scissors, container, false);

        //Random Dice UI handling
        InitializeRPSUIElements(root);
        return root;
    }

    private void InitializeRPSUIElements(View root) {
        rpsNameTextView = root.findViewById(R.id.rpsNameTextView);
        rpsTypeSpinner = root.findViewById(R.id.rpsTypeSpinner);
        rpsTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GenerateRandomRPSResult(root);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        generateButton = root.findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> GenerateRandomRPSResult(v));
        rpsImageView = root.findViewById(R.id.rpsImageView);
    }

    private void GenerateRandomRPSResult(View v) {
        RockPaperScissorsType rpsType = GetSelectedRpsType();
        GenerateRandomRpsTypeAnswer(rpsType);
    }

    private void GenerateRandomRpsTypeAnswer(RockPaperScissorsType rpsType) {
        String rpsResult = randomLib.GetRandomRpsResult(rpsType);
        UpdateRpsResultImageView(rpsResult);
    }

    private void UpdateRpsResultImageView(String rpsResult) {
        Animation pullInAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoomin);
        int drawableID = getResources().getIdentifier(rpsResult, "drawable", this.getActivity().getPackageName());
        rpsImageView.setImageResource(drawableID);
        rpsImageView.startAnimation(pullInAnimation);
        rpsNameTextView.setText(GetResultText(rpsResult));
    }

    private String GetResultText(String rpsResult) {
        String resultText = null;
        switch (rpsResult){
            case "rock":
                resultText = getString(R.string.result_rock);
                break;
            case "paper":
                resultText = getString(R.string.result_paper);
                break;
            case "scissors":
                resultText = getString(R.string.result_scissors);
                break;
            case "lizard":
                resultText = getString(R.string.result_lizard);
                break;
            case "spock":
                resultText = getString(R.string.result_spock);
                break;
        }
        return resultText;
    }

    private RockPaperScissorsType GetSelectedRpsType() {
        RockPaperScissorsType rpsType;
        int selectedItem = rpsTypeSpinner.getSelectedItemPosition();
        switch(selectedItem) {
            case 1:
                rpsType = RockPaperScissorsType.RPSLS;
                break;
            case 0:
            default:
                rpsType = RockPaperScissorsType.RPS;
                break;
        }
        return rpsType;
    }
}
