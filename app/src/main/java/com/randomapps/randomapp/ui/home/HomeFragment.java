package com.randomapps.randomapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.randomapps.randomapp.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    CardView cardView_randomGenerator, cardView_flipCoin, cardView_dice, cardView_cardPicker,
            cardView_seriesGenerator, cardView_teamsGenerator, cardView_dualAnswers, cardView_rps,
            cardView_randomNumbers, cardView_randomPasswords, cardView_randomDatesTimes, cardView_randomColors, cardView_uuids;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        InitializeHomeUIElements(root);
        return root;
    }

    private void InitializeHomeUIElements(View root) {
        //Random generator
        /*cardView_randomGenerator = root.findViewById(R.id.cardView_randomGenerator);
        cardView_randomGenerator.setOnClickListener(v -> NavigateToRandomGenerator(v));*/

        //Random Numbers
        cardView_randomNumbers = root.findViewById(R.id.cardView_randomNumbers);
        cardView_randomNumbers.setOnClickListener(v -> NavigateToRandomNumbers(v));

        //Random Passwords
        cardView_randomPasswords = root.findViewById(R.id.cardView_randomPasswords);
        cardView_randomPasswords.setOnClickListener(v -> NavigateToRandomPasswords(v));

        //Random Dates & Times
        cardView_randomDatesTimes = root.findViewById(R.id.cardView_randomDatesTimes);
        cardView_randomDatesTimes.setOnClickListener(v -> NavigateToRandomDatesTimes(v));

        //Random Colors
        cardView_randomColors = root.findViewById(R.id.cardView_randomColors);
        cardView_randomColors.setOnClickListener(v -> NavigateToRandomColors(v));

        //Unique Ids
        cardView_uuids = root.findViewById(R.id.cardView_uuids);
        cardView_uuids.setOnClickListener(v -> NavigateToRandomUUIDs(v));

        //Flip coin
        cardView_flipCoin = root.findViewById(R.id.cardView_flipCoin);
        cardView_flipCoin.setOnClickListener(v -> NavigateToFlipCoin(v));

        //Dice
        cardView_dice = root.findViewById(R.id.cardView_dice);
        cardView_dice.setOnClickListener(v -> NavigateToDice(v));

        //Card picker
        cardView_cardPicker = root.findViewById(R.id.cardView_cardPicker);
        cardView_cardPicker.setOnClickListener(v -> NavigateToCardPicker(v));

        //Series generator
        cardView_seriesGenerator = root.findViewById(R.id.cardView_seriesGenerator);
        cardView_seriesGenerator.setOnClickListener(v -> NavigateToSeriesGenerator(v));

        //Teams generator
        cardView_teamsGenerator = root.findViewById(R.id.cardView_teamsGenerator);
        cardView_teamsGenerator.setOnClickListener(v -> NavigateToTeamsGenerator(v));

        //Dual answers
        cardView_dualAnswers = root.findViewById(R.id.cardView_dualAnswers);
        cardView_dualAnswers.setOnClickListener(v -> NavigateToDualAnswers(v));

        //R-P-S
        cardView_rps = root.findViewById(R.id.cardView_rps);
        cardView_rps.setOnClickListener(v -> NavigateToRPS(v));
    }

    private void NavigateToRandomUUIDs(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_randomGenUUIDs);
    }

    private void NavigateToRandomColors(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_randomGenColors);
    }

    private void NavigateToRandomDatesTimes(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_randomGenDateTimes);
    }

    private void NavigateToRandomPasswords(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_randomGenStrings);
    }

    private void NavigateToRandomNumbers(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_randomGenNumbers);
    }

    private void NavigateToRPS(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_rockPaperScissors);
    }

    private void NavigateToDualAnswers(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_yesNo);
    }

    private void NavigateToTeamsGenerator(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_divideAndAssign);
    }

    private void NavigateToSeriesGenerator(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_seriesGenerator);
    }

    private void NavigateToCardPicker(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_cardPicker);
    }

    private void NavigateToDice(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_dice);
    }

    private void NavigateToFlipCoin(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_flipCoin);
    }

    /*private void NavigateToRandomGenerator(View v) {
        Navigation.findNavController(v).navigate(R.id.action_home_to_randomGenHome);
    }*/
}