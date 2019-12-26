package com.randomapps.randomapp.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.enums.YesNoType;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class YesNoTrueFalseFragment extends Fragment {
    //Yes-No UI elements
    TextView yesNoTrueFalseNameTextView;
    Spinner yesNoTrueFalseTypeSpinner;
    Button generateButton;
    ImageView yesNoTrueFalseImageView;

    public YesNoTrueFalseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_yes_no_true_false, container, false);

        //Random Dice UI handling
        InitializeYesNoUIElements(root);
        return root;
    }

    private void InitializeYesNoUIElements(View root) {
        yesNoTrueFalseNameTextView = root.findViewById(R.id.yesNoTrueFalseNameTextView);
        yesNoTrueFalseTypeSpinner = root.findViewById(R.id.yesNoTrueFalseTypeSpinner);
        yesNoTrueFalseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GenerateRandomYesNoResult(root);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        generateButton = root.findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> GenerateRandomYesNoResult(v));
        yesNoTrueFalseImageView = root.findViewById(R.id.yesNoTrueFalseImageView);
    }

    private void GenerateRandomYesNoResult(View v) {
        YesNoType yesNoType = GetSelectedDeckType();
        GenerateRandomYesNoTypeAnswer(yesNoType);
    }

    private void GenerateRandomYesNoTypeAnswer(YesNoType yesNoType) {
        Random random = new Random();
        int randomResult = random.nextInt(2);
        Boolean result = randomResult == 1;
        UpdateResultImageView(result, yesNoType);
    }

    //Better flip animation
    void SetViewFlip(ImageView yesNoTrueFalseImageView){
        ObjectAnimator flip = ObjectAnimator.ofFloat(yesNoTrueFalseImageView, "rotationY", 0f, 360f); // or rotationX
        flip.setDuration(2000); // 2 seconds
        flip.start();
    }

    private void UpdateResultImageView(Boolean result, YesNoType yesNoType) {
        Animation zoomInAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoomin);
        if(yesNoType == YesNoType.YES_NO) {
            if(result){
                yesNoTrueFalseImageView.setImageResource(R.drawable.yes);
                yesNoTrueFalseImageView.startAnimation(zoomInAnimation);
                yesNoTrueFalseNameTextView.setText(getString(R.string.result_yes));
            } else {
                yesNoTrueFalseImageView.setImageResource(R.drawable.no);
                yesNoTrueFalseImageView.startAnimation(zoomInAnimation);
                yesNoTrueFalseNameTextView.setText(getString(R.string.result_no));
            }
        } else if(yesNoType == YesNoType.TRUE_FALSE) {
            if(result){
                yesNoTrueFalseImageView.setImageResource(R.drawable.true_image);
                yesNoTrueFalseImageView.startAnimation(zoomInAnimation);
                yesNoTrueFalseNameTextView.setText(getString(R.string.result_true));
            } else {
                yesNoTrueFalseImageView.setImageResource(R.drawable.false_image);
                yesNoTrueFalseImageView.startAnimation(zoomInAnimation);
                yesNoTrueFalseNameTextView.setText(getString(R.string.result_false));
            }
        }
    }

    private YesNoType GetSelectedDeckType() {
        YesNoType yesNoType;
        int selectedItem = yesNoTrueFalseTypeSpinner.getSelectedItemPosition();
        switch(selectedItem) {
            case 0:
                yesNoType = YesNoType.YES_NO;
                break;
            case 1:
                yesNoType = YesNoType.TRUE_FALSE;
                break;
            default:
                yesNoType = YesNoType.YES_NO;
                break;
        }
        return yesNoType;
    }
}
