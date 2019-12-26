package com.randomapps.randomapp.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.enums.CardDeckType;

import java.util.ArrayList;
import java.util.List;

import random.RandomLibrary;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardPickerFragment extends Fragment {
    //Random Library
    RandomLibrary randomLib;
    List<String> selectedCards;
    View root;

    //Random Card picker UI elements
    LinearLayout resultsHistoryLayout, cardImageLayout;
    TextView cardNameTextView, cardsCountTextView;
    Spinner deckTypeSpinner;
    Button generateButton;
    ImageView ranCardImageView;
    CheckBox removeGeneratedCardsCheckBox;

    public CardPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Random Library instantiation
        randomLib = new RandomLibrary();

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_card_picker, container, false);

        //Random Dice UI handling
        InitializeCarPickerUIElements(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GenerateRandomCard(root);
    }

    private void InitializeCarPickerUIElements(View root) {
        removeGeneratedCardsCheckBox = root.findViewById(R.id.removeGeneratedCardsCheckBox);
        cardsCountTextView = root.findViewById(R.id.cardsCountTextView);

        cardNameTextView = root.findViewById(R.id.cardNameTextView);
        deckTypeSpinner = root.findViewById(R.id.deckTypeSpinner);
        cardImageLayout = root.findViewById(R.id.cardImageLayout);

        generateButton = root.findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> GenerateRandomCard(v));
        resultsHistoryLayout = root.findViewById(R.id.resultsHistoryLayout);

        ranCardImageView = root.findViewById(R.id.ranCardImageView);
    }

    private void GenerateRandomCard(View v) {
        CardDeckType deckType = GetSelectedDeckType();
        int totalCards = (deckType == CardDeckType.Cards_52)? 52 : (deckType == CardDeckType.Cards_54)? 54 : 56;
        String randomCard = null;

        if(removeGeneratedCardsCheckBox.isChecked()){
            if(selectedCards != null && selectedCards.size() == totalCards){
                ShowDeckResetDialog(totalCards);
            } else {
                cardsCountTextView.setVisibility(View.VISIBLE);
                if(selectedCards == null) selectedCards = new ArrayList<>();
                randomCard = randomLib.GetRandomCard(deckType, true, selectedCards);
                if(randomCard != null) {
                    selectedCards.add(randomCard);
                }
                cardsCountTextView.setText(selectedCards.size() + "/" + totalCards);
            }

            /*cardsCountTextView.setVisibility(View.VISIBLE);
            if(selectedCards == null) selectedCards = new ArrayList<>();
            randomCard = randomLib.GetRandomCard(deckType, true, selectedCards);
            if(randomCard != null) {
                selectedCards.add(randomCard);
            }
            cardsCountTextView.setText(selectedCards.size() + "/" + totalCards);*/
        } else {
            selectedCards = null;
            cardsCountTextView.setVisibility(View.INVISIBLE);
            randomCard = randomLib.GetRandomCard(deckType, false, selectedCards);
        }

        if(randomCard != null) {
            String cardName = GetCardName(randomCard);
            int drawableId = getDrawableCardId(randomCard);

            //Set animation
            Animation leftToRightAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.lefttoright);
            Animation rightToLeftAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.righttoleft);
            rightToLeftAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ranCardImageView.setImageResource(drawableId);
                    ranCardImageView.startAnimation(leftToRightAnimation);
                    cardNameTextView.setText(cardName);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            ranCardImageView.startAnimation(rightToLeftAnimation);
        }
    }

    private int getDrawableCardId(String randomCard) {
        int drawableID;
        if(randomCard.startsWith("joker")){
            drawableID = getResources().getIdentifier("joker", "drawable", this.getActivity().getPackageName());
        } else {
            drawableID = getResources().getIdentifier(randomCard, "drawable", this.getActivity().getPackageName());
        }
        return drawableID;
    }

    private void ShowDeckResetDialog(int totalCards) {
        AlertDialog.Builder resetDialogBuilder = new AlertDialog.Builder(this.getActivity())
                .setMessage(getString(R.string.cards_over_msg))
                .setTitle(getString(R.string.cards_over_header)).setPositiveButton(getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedCards = null;
                                cardsCountTextView.setText(0 + "/" + totalCards);
                                dialog.cancel();
                            }
                        });
        resetDialogBuilder.show();
    }

    private String GetCardName(String randomCard) {
        String cardName;
        String cardNumber = GetCardNumber(randomCard);

        if(randomCard.startsWith("sp_")){
            cardName = getString(R.string.cardname_spade) + " - " + cardNumber;
        } else if(randomCard.startsWith("cl_")){
            cardName = getString(R.string.cardname_club) + " - " + cardNumber;
        } else if(randomCard.startsWith("di_")){
            cardName = getString(R.string.cardname_diamond) + " - " + cardNumber;
        } else if(randomCard.startsWith("he_")){
            cardName = getString(R.string.cardname_heart) + " - " + cardNumber;
        } else {
            cardName = getString(R.string.cardname_joker);
        }
        return cardName;
    }

    private String GetCardNumber(String randomCard) {
        String cardType = randomCard.substring(randomCard.indexOf("_") + 1);
        String CardNumberName = null;

        switch (cardType){
            case "a":
                CardNumberName = getString(R.string.cardname_ace);
                break;
            case "j":
                CardNumberName = getString(R.string.cardname_jack);
                break;
            case "q":
                CardNumberName = getString(R.string.cardname_queen);
                break;
            case "k":
                CardNumberName = getString(R.string.cardname_king);
                break;
            default:
                CardNumberName = cardType;
                break;
        }
        return CardNumberName;
    }

    private CardDeckType GetSelectedDeckType() {
        CardDeckType deckType;
        int selectedItem = deckTypeSpinner.getSelectedItemPosition();
        switch(selectedItem) {
            case 1:
                deckType = CardDeckType.Cards_54;
                break;
            case 2:
                deckType = CardDeckType.Cards_56;
                break;
            case 0:
            default:
                deckType = CardDeckType.Cards_52;
                break;
        }
        return deckType;
    }
}
