package com.randomapps.randomapp.ui;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import random.RandomColorsGenTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomGenColorsFragment extends Fragment {
    private final int MAX_GENERATION_LIMIT = 50;
    ArrayList<String> generatedRandomColors;

    //Random Colors UI elements
    TextInputEditText howManyColorsEditText;
    LinearLayout colorDisplayListLayout;
    TextView maxColorsLimitTextView;
    Button generateRandom;
    ImageView copyImageView, shareImageView;

    public RandomGenColorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_random_gen_colors, container, false);

        //Random Colors generator UI handling
        InitializeRandomColorsGeneratorUIElements(root);
        return root;
    }

    private void InitializeRandomColorsGeneratorUIElements(View root) {
        generateRandom = root.findViewById(R.id.butGenerateRandom);
        generateRandom.setOnClickListener(v -> GenerateRandomHandler(v));
        maxColorsLimitTextView = root.findViewById(R.id.maxColorsLimitTextView);
        maxColorsLimitTextView.setText(getResources().getString(R.string.text_max_limited_to) + " " + MAX_GENERATION_LIMIT);
        howManyColorsEditText = root.findViewById(R.id.howManyColorsEditText);
        howManyColorsEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(howManyColorsEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });
        colorDisplayListLayout = root.findViewById(R.id.colorDisplayListLayout);

        copyImageView = root.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(v -> CopyToClipBoard(v));

        shareImageView = root.findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(v -> ShareTheRandomResult(v));
    }

    private void ShareTheRandomResult(View v) {
        if(generatedRandomColors != null && generatedRandomColors.size() > 0){
            String content = TextUtils.join(", ", generatedRandomColors);
            //String toastMsg = "Results copied to Clipboard";
            Utils.ShareTheResultContent(content, getActivity());
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_colors_generated_error), Toast.LENGTH_LONG).show();
        }

    }

    private void CopyToClipBoard(View v) {
        if(generatedRandomColors != null && generatedRandomColors.size() > 0){
            String content = TextUtils.join(", ", generatedRandomColors);
            String toastMsg = getString(R.string.results_copied_success_msg);
            Utils.CopyContentToClipBoard(getActivity(), getString(R.string.text_results),content, toastMsg);
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_colors_generated_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set defaults
        howManyColorsEditText.setText("1");
        GenerateRandomColorsHandler(view);
    }

    private void GenerateRandomHandler(View v) {
        //Hide the keyBoard
        Utils.hideKeyboard(v, getActivity());
        //For Random Colors
        GenerateRandomColorsHandler(v);
    }

    private boolean ValidateGenericInputs() {

        if (howManyColorsEditText.getText().toString() == null || howManyColorsEditText.getText().toString().isEmpty()){
            howManyColorsEditText.setError(getString(R.string.value_empty_error_msg));
            howManyColorsEditText.requestFocus();
            return false;
        } else {
            howManyColorsEditText.setError(null);
        }

        if(Integer.valueOf(howManyColorsEditText.getText().toString()) > MAX_GENERATION_LIMIT){
            howManyColorsEditText.setError(getString(R.string.colors_range_exceed_error_msg, MAX_GENERATION_LIMIT));
            howManyColorsEditText.requestFocus();
            return false;
        } else {
            howManyColorsEditText.setError(null);
        }

        int quantity = Integer.parseInt(howManyColorsEditText.getText().toString());
        if(quantity < 1){
            howManyColorsEditText.setError(getString(R.string.value_greater_than_zero_error_msg));
            howManyColorsEditText.requestFocus();
            return false;
        } else {
            howManyColorsEditText.setError(null);
        }
        return true;
    }

    private void GenerateRandomColorsHandler(View v) {
        colorDisplayListLayout.removeAllViews();
        if(ValidateGenericInputs()) {
            int quantity = Integer.parseInt(howManyColorsEditText.getText().toString());
            RandomColorsGenTask randomColorsGenTask = new RandomColorsGenTask(quantity);
            ArrayList<String> randomColors = null;
            try {
                randomColors = randomColorsGenTask.execute().get();

                //Once random colors are generated cache locally for copy and share
                generatedRandomColors = new ArrayList<>();
                generatedRandomColors.addAll(randomColors);
                if(randomColors != null ) {
                    for (int i = 0; i < randomColors.size(); i++) {
                        RelativeLayout colorDisplayTile = CreateColorDisplayRelativeLayout(randomColors.get(i));
                        colorDisplayListLayout.addView(colorDisplayTile);
                    }
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private RelativeLayout CreateColorDisplayRelativeLayout(String color) {
        /*<RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:background="#E0DEDB"
        android:layout_weight="1"
        android:layout_margin="20dp"
        android:elevation="5dp">*/
        RelativeLayout colorDisplayTile = new RelativeLayout(getActivity());

        //Convert DPs into px
        /*int dps = 200;
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int heightPixels = (int) (dps * scale + 0.5f);*/
        int heightPixels = GetDpsToPixels(200);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightPixels);

        int marginPixels = GetDpsToPixels(20);
        layoutParams.setMargins(marginPixels, marginPixels, marginPixels, marginPixels);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        colorDisplayTile.setLayoutParams(layoutParams);
        colorDisplayTile.setBackgroundColor(Color.parseColor("#E0DEDB"));
        colorDisplayTile.setElevation(5);

        /*<ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:layout_marginTop="20dp"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:layout_marginBottom="30dp"
        android:background="@color/colorPrimary" />*/
        ImageView imageView = new ImageView(this.getActivity());
        //imageView.setImageResource(drawableID);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        int imageMargins20 = GetDpsToPixels(20);
        int imageMargins30 = GetDpsToPixels(30);
        imageLayoutParams.setMargins(imageMargins20, imageMargins20, imageMargins20, imageMargins30);
        //imageLayoutParams.weight = 1.0f;
        imageView.setLayoutParams(imageLayoutParams);
        imageView.setBackgroundColor(Color.parseColor(color));
        imageView.setElevation(8);
        colorDisplayTile.addView(imageView);

        /*<TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:layout_marginLeft="10dp"
            android:layout_marginRight="10dp"
            android:layout_marginBottom="10dp"
            android:layout_alignParentBottom="true"
            android:layout_centerHorizontal="true"
            android:elevation="8dp"
            android:textAlignment="center"
            android:text="#1A1B1C"
            android:textSize="16sp"
            android:textColor="@android:color/black"
            android:textStyle="bold"
            android:padding="10dp"
            android:background="@drawable/roundedbutton"
            android:layout_gravity="center_horizontal" />*/
        //Add textView
        TextView colorTv = new TextView(getActivity());
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        textViewParams.setMargins(GetDpsToPixels(10), GetDpsToPixels(5), GetDpsToPixels(10), GetDpsToPixels(10));
        textViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        colorTv.setLayoutParams(textViewParams);

        colorTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        colorTv.setText(color);
        /*//Convert sp to px and set
        float sp = 16;
        float px = sp * getResources().getDisplayMetrics().scaledDensity;*/
        colorTv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        colorTv.setTextColor(Color.BLACK);
        colorTv.setTypeface(Typeface.DEFAULT_BOLD);
        colorTv.setAllCaps(true);

        //Dp to pixel
        int valueInDP = 20;
        float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDP, getResources().getDisplayMetrics());
        colorTv.setPadding(valueInDP,valueInDP,valueInDP,valueInDP);

        colorTv.setBackgroundResource(R.drawable.roundedbutton);
        colorTv.setElevation(10);
        colorDisplayTile.addView(colorTv);

        //implment tap to copy
        SetDoubleTapListener(colorDisplayTile);
        return colorDisplayTile;
    }

    private void SetDoubleTapListener(RelativeLayout colorDisplayTile) {
        TextView colorCodeTv = (TextView) colorDisplayTile.getChildAt(1);
        String colorCode = colorCodeTv.getText().toString().toUpperCase();

        //setup onclcik listener for textView
        colorCodeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String toastMsg = "Color code " + colorCode + " copied";
                String toastMsg = getString(R.string.color_code_copied_msg, colorCode);
                Utils.CopyContentToClipBoard(getActivity(), getString(R.string.text_color_code),colorCode, toastMsg);
            }
        });
    }

    private float GetSpsToPixel(int sps) {
        float pixels = sps * getResources().getDisplayMetrics().scaledDensity;
        return pixels;
    }

    private int GetDpsToPixels(int dps) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }
}
