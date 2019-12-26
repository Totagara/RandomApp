package com.randomapps.randomapp.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import androidx.core.content.ContextCompat;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlipCoinFragment extends Fragment {
    ImageView coinImageView;
    Button butFlipCoin;
    TextView coinResultTextView;
    Spinner selectCoinSpinner;

    public FlipCoinFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_flip_coin, container, false);

        //Series generator UI handling
        InitializeCoinFlipUIElements(root);
        return root;
    }

    private void InitializeCoinFlipUIElements(View root) {
        coinImageView = root.findViewById(R.id.coinImageView);
        coinResultTextView = root.findViewById(R.id.coinResultTextView);
        butFlipCoin = root.findViewById(R.id.butFlipCoin);
        butFlipCoin.setOnClickListener(v -> FlipTheCoin(v));
        selectCoinSpinner = root.findViewById(R.id.selectCoinSpinner);
        selectCoinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShowRandomCoinFlipResult(position, coinImageView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    int GetOppositeCoinFace(ImageView coinImageView) {
        Integer selectedCoinPosition = selectCoinSpinner.getSelectedItemPosition();
        if (selectedCoinPosition == 0) {
            if (coinImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.heads_indian).getConstantState()) {
                return R.drawable.tails_indian;
            } else {
                return R.drawable.heads_indian;
            }
        } else if (selectedCoinPosition == 1) {
            if (coinImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.heads_american).getConstantState()) {
                return R.drawable.tails_american;
            } else {
                return R.drawable.heads_american;
            }
        } else if (selectedCoinPosition == 2) {
            if (coinImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.heads_canadian).getConstantState()) {
                return R.drawable.tails_canadian;
            } else {
                return R.drawable.heads_canadian;
            }
        }
        return R.drawable.heads_indian;
    }

    ObjectAnimator GetFlipXAsisAnimator(ImageView coinImage) {
        ObjectAnimator xAxisFlipAnimator = ObjectAnimator.ofFloat(coinImage, "rotationX", 180f, 0f);
        xAxisFlipAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                coinImageView.setImageResource(GetOppositeCoinFace(coinImageView));
            }
        });
        xAxisFlipAnimator.setDuration(150);
        return xAxisFlipAnimator;
    }

    void ShowCoinFlipAnimation(ImageView coinImage, Integer selectedCoinPosition) {
        coinResultTextView.setText("");
        Animation zoomIn = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomincoin);
        zoomIn.setDuration(100);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimatorSet animSet = new AnimatorSet();
                animSet.playSequentially(GetFlipXAsisAnimator(coinImageView), GetFlipXAsisAnimator(coinImageView),
                        GetFlipXAsisAnimator(coinImageView), GetFlipXAsisAnimator(coinImageView), GetFlipXAsisAnimator(coinImageView));

                animSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animation zoomOut = AnimationUtils.loadAnimation(getActivity(), R.anim.zoomoutcoin);
                        zoomOut.setDuration(100);
                        coinImageView.setImageResource(GetOppositeCoinFace(coinImageView));
                        //coinImage.setAnimation(zoomIn);
                        coinImage.startAnimation(zoomOut);

                        zoomOut.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ShowRandomCoinFlipResult(selectedCoinPosition, coinImage);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                });

                animSet.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        coinImage.startAnimation(zoomIn);
    }

    void ShowRandomCoinFlipResult(Integer selectedCoinPosition, ImageView coinImageView) {
        Random random = new Random();
        int res = random.nextInt(2);
        if (selectedCoinPosition != null) {
            switch (selectedCoinPosition) {
                case 1:
                    if (res == 1) {
                        coinImageView.setImageResource(R.drawable.heads_american);
                        coinResultTextView.setText(getString(R.string.result_heads));
                    } else {
                        coinImageView.setImageResource(R.drawable.tails_american);
                        coinResultTextView.setText(getString(R.string.result_tails));
                    }
                    break;
                case 2:
                    if (res == 1) {
                        coinImageView.setImageResource(R.drawable.heads_canadian);
                        coinResultTextView.setText(getString(R.string.result_heads));
                    } else {
                        coinImageView.setImageResource(R.drawable.tails_canadian);
                        coinResultTextView.setText(getString(R.string.result_tails));
                    }
                    break;
                case 0:
                default:
                    if (res == 1) {
                        coinImageView.setImageResource(R.drawable.heads_indian);
                        coinResultTextView.setText(getString(R.string.result_heads));
                    } else {
                        coinImageView.setImageResource(R.drawable.tails_indian);
                        coinResultTextView.setText(getString(R.string.result_tails));
                    }
                    break;
            }
        } else {
            if (res == 1) {
                coinImageView.setImageResource(R.drawable.heads_indian);
                coinResultTextView.setText(getString(R.string.result_heads));
            } else {
                coinImageView.setImageResource(R.drawable.tails_indian);
                coinResultTextView.setText(getString(R.string.result_tails));
            }
        }
    }

    private void FlipTheCoin(View v) {
        coinResultTextView.setText("");
        Integer selectedCoinPosition = selectCoinSpinner.getSelectedItemPosition();

        //show flip animation
        ShowCoinFlipAnimation(coinImageView, selectedCoinPosition);
    }
}
