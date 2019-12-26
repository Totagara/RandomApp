package com.randomapps.randomapp.ui;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigInteger;

import random.RandomDecimalNumbersGenTask;
import random.RandomIntegerNumbersGenTask;
import random.RandomTextFormatterTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomGenNumbersFragment extends Fragment {
    final int MAX_GENERATION_LIMIT = 50000;
    Boolean formatToVertical = true;

    String copyContent = null;

    //Random numbers Generator UI elements
    CheckBox decimalCheckBox, sortCheckBox, repeatCheckBox;
    RadioGroup sortRadioGroup;
    LinearLayout precisionLayout;
    Button generateRandom;
    TextInputEditText minEditText, maxEditText, quantityEditText;
    EditText resultsEditText;
    RadioButton ascRadioButton, desRadioButtom;
    Spinner precisionSpinner;
    ImageView copyImageView, shareImageView, resSeparatorImageView;

    public RandomGenNumbersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_random_gen_numbers, container, false);
        InitializeRandomNumbersGeneratorUIElements(root);
        return root;
    }

    private void InitializeRandomNumbersGeneratorUIElements(View root) {
        //Random Numbers generator UI handling
        decimalCheckBox = root.findViewById(R.id.decimalCheckbox);
        sortCheckBox = root.findViewById(R.id.sortCheckbox);
        sortRadioGroup = root.findViewById(R.id.sortRadioGroup);
        precisionLayout = root.findViewById(R.id.precisionLayout);
        generateRandom = root.findViewById(R.id.butGenerateRandom);

        minEditText = root.findViewById(R.id.minEditText);
        minEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(minEditText, Integer.MIN_VALUE, Integer.MAX_VALUE, null, getActivity()) });

        maxEditText = root.findViewById(R.id.maxEditText);
        maxEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(maxEditText, Integer.MIN_VALUE, Integer.MAX_VALUE, null, getActivity()) });

        quantityEditText = root.findViewById(R.id.howManyEditText);
        quantityEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(quantityEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });

        resultsEditText = root.findViewById(R.id.resultTextBox);
        resultsEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        repeatCheckBox = root.findViewById(R.id.repeatCheckbox);

        //trying for custom spinner
        precisionSpinner = root.findViewById(R.id.precisionSpinner);

        copyImageView = root.findViewById(R.id.copyImageView);
        shareImageView = root.findViewById(R.id.shareImageView);
        resSeparatorImageView = root.findViewById(R.id.resSeparatorImageView);

        ascRadioButton = root.findViewById(R.id.ascRadioButton);
        desRadioButtom = root.findViewById(R.id.desRadioButton);
        ascRadioButton.setChecked(true);

        decimalCheckBox.setOnClickListener(v -> DecimalCheckBoxClickHandler(v));
        sortCheckBox.setOnClickListener(v -> SortCheckBoxClickHandler(v));
        generateRandom.setOnClickListener(v -> GenerateRandomHandler(v));
        copyImageView.setOnClickListener(v -> CopyToClipBoard(v));
        shareImageView.setOnClickListener(v -> ShareTheRandomResult(v));
        resSeparatorImageView.setOnClickListener(v -> UpdateResultsWithSeparator(v));

        //Set min, max and quantity to defaults
        minEditText.setText("1");
        maxEditText.setText("100");
        quantityEditText.setText("1");
    }

    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }*/


    private void UpdateResultsWithSeparator(View v) {
        //boolean isToBeVertical  = (resSeparatorImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.ic_format_vertical).getConstantState())? true: false;
        String fromSeparator, toSeparator;
        String content = resultsEditText.getText().toString();

        if(!content.equals("") && content != null) {
            boolean isToBeVertical = formatToVertical;
            if (formatToVertical) {
                fromSeparator = ", ";
                toSeparator = "\n";
                formatToVertical = false;
            } else {
                toSeparator = ", ";
                fromSeparator = "\n";
                formatToVertical = true;
            }

            RandomTextFormatterTask randomTextFormatterTask = null;
            randomTextFormatterTask = new RandomTextFormatterTask(getActivity(), fromSeparator, toSeparator, content, resultsEditText, resSeparatorImageView, isToBeVertical);

            if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                randomTextFormatterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                randomTextFormatterTask.execute();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_format_error), Toast.LENGTH_LONG).show();
        }
    }

    private void GenerateRandomHandler(View v){
        //Hide the keyBoard
        Utils.hideKeyboard(v, getActivity());

        //For Random number
        GenerateRandomNumbersHandler(v);
    }

    private void GenerateRandomNumbersHandler(View v) {
        resultsEditText.setText("");
        boolean isRepeatAllowed = repeatCheckBox.isChecked();
        boolean generateDecimals = decimalCheckBox.isChecked();
        //Integer quantityVal = Integer.valueOf(quantityEditText.getText().toString());
        boolean isToBeSorted = sortCheckBox.isChecked();
        boolean isAscendingSort = ascRadioButton.isChecked();

        //Generic validations
        if(ValidateGenericInputs()) {
            Integer quantityVal = Integer.valueOf(quantityEditText.getText().toString());
            //boolean isToBeVertical  = (resSeparatorImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.ic_format_vertical).getConstantState())? true: false;
            //String resultsSeparator = isToBeVertical? ", " : "\n";
            String resultsSeparator = formatToVertical? ", " : "\n";
            if (generateDecimals) {
                RandomDecimalNumbersGenTask randomDecimalNumbersGenTask = null;
                Double dMinVal = Double.valueOf(minEditText.getText().toString());
                Double dMaxVal = Double.valueOf(maxEditText.getText().toString());
                int precision = precisionSpinner.getSelectedItemPosition() + 1;

                if(ValidateDecimalInputs(isRepeatAllowed, quantityVal, dMinVal, dMaxVal, precision)){
                    randomDecimalNumbersGenTask = new RandomDecimalNumbersGenTask(getActivity(), dMinVal, dMaxVal, quantityVal, precision, isRepeatAllowed, isToBeSorted, isAscendingSort, resultsEditText, resultsSeparator);

                    if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                        randomDecimalNumbersGenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        randomDecimalNumbersGenTask.execute();
                    }
                }
            } else {
                RandomIntegerNumbersGenTask randomIntegerNumbersGenTask = null;

                Integer iMinRange = Integer.valueOf(minEditText.getText().toString());
                Integer iMaxRange = Integer.valueOf(maxEditText.getText().toString());

                if(ValidateIntegerInputs(isRepeatAllowed, quantityVal, iMinRange, iMaxRange)) {
                    randomIntegerNumbersGenTask = new RandomIntegerNumbersGenTask(getActivity(), isToBeSorted, isAscendingSort,
                            isRepeatAllowed, quantityVal, iMinRange, iMaxRange, resultsEditText, resultsSeparator);

                    if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                        randomIntegerNumbersGenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        randomIntegerNumbersGenTask.execute();
                    }
                }
            }
        }
    }

    private void CopyToClipBoard(View v) {
        String content = resultsEditText.getText().toString();
        if(!content.equals("") && content != null) {
            try {
                String toastMsg = getString(R.string.results_copied_success_msg);
                Utils.CopyContentToClipBoard(getActivity(), getString(R.string.text_results), content, toastMsg);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.too_large_to_copy_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_copy_error), Toast.LENGTH_LONG).show();
        }
    }

    private void ShareTheRandomResult(View v) {
        String content = resultsEditText.getText().toString();
        if(!content.equals("") && content != null) {
            try {
                Utils.ShareTheResultContent(content, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.too_large_to_share_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_share_error), Toast.LENGTH_LONG).show();
        }
    }

    private void DecimalCheckBoxClickHandler(View v) {
        if(((CheckBox)v).isChecked()){
            precisionLayout.setVisibility(View.VISIBLE);
            minEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            maxEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            precisionLayout.setVisibility(View.INVISIBLE);
            minEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            maxEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

            if(minEditText.getText().toString() != null && !minEditText.getText().toString().isEmpty()) {
                minEditText.setText(String.valueOf((int) Math.round(Double.valueOf(minEditText.getText().toString()))));
                maxEditText.setText(String.valueOf((int) Math.round(Double.valueOf(maxEditText.getText().toString()))));
            }
        }
    }

    private void SortCheckBoxClickHandler(View v) {
        if(((CheckBox)v).isChecked()){
            sortRadioGroup.setVisibility(View.VISIBLE);
        } else {
            sortRadioGroup.setVisibility(View.INVISIBLE);
        }
    }

    private boolean ValidateGenericInputs(){
        //Check empty validation
        if(minEditText.getText().toString().isEmpty()){
            minEditText.setError(getString(R.string.value_empty_error_msg));
            minEditText.requestFocus();
            return false;
        } else {
            minEditText.setError(null);
        }

        if (maxEditText.getText().toString().isEmpty()){
            maxEditText.setError(getString(R.string.value_empty_error_msg));
            maxEditText.requestFocus();
            return false;
        } else {
            maxEditText.setError(null);
        }

        if (quantityEditText.getText().toString().isEmpty()) {
            quantityEditText.setError(getString(R.string.value_empty_error_msg));
            quantityEditText.requestFocus();
            return false;
        } else {
            quantityEditText.setError(null);
        }

        //Quantity input validations
        Integer quantityVal = Integer.valueOf(quantityEditText.getText().toString());
        if(quantityVal < 1){
            quantityEditText.setError(getString(R.string.value_greater_than_zero_error_msg));
            quantityEditText.requestFocus();
            return false;
        } else {
            quantityEditText.setError(null);
        }

        //MAX Generation limit
        if(quantityVal > MAX_GENERATION_LIMIT){
            quantityEditText.setError(getString(R.string.max_generation_limit_error_msg, MAX_GENERATION_LIMIT));
            quantityEditText.requestFocus();
            return false;
        } else {
            quantityEditText.setError(null);
        }

        //Radio button validations
        if(sortCheckBox.isChecked() && !ascRadioButton.isChecked() && !desRadioButtom.isChecked()){
            ascRadioButton.setError(getString(R.string.sort_order_not_selected_error_msg));
            return false;
        } else {
            ascRadioButton.setError(null);
        }
        return true;
    }

    private boolean ValidateDecimalInputs(boolean repeatAllowed, Integer quantityVal, Double dMinVal, Double dMaxVal, int precision){
        //Check for Max possible Double values
        //if(dMinVal < Double.MIN_VALUE || dMinVal > Double.MAX_VALUE) {
        if(dMinVal < Integer.MIN_VALUE || dMinVal > Integer.MAX_VALUE) {
            minEditText.setError(getString(R.string.values_range_error_msg, Integer.MIN_VALUE, Integer.MAX_VALUE));
            minEditText.requestFocus();
            return false;
        } else {
            minEditText.setError(null);
        }

        //if(dMaxVal < Double.MIN_VALUE || dMaxVal > Double.MAX_VALUE){
        if(dMaxVal < Integer.MIN_VALUE || dMaxVal > Integer.MAX_VALUE){
            maxEditText.setError(getString(R.string.values_range_error_msg, Integer.MIN_VALUE, Integer.MAX_VALUE));
            maxEditText.requestFocus();
            return false;
        } else {
            maxEditText.setError(null);
        }

        //Min max validation
        if(dMinVal > dMaxVal || dMinVal.equals(dMaxVal)){
            maxEditText.setError(getString(R.string.max_is_more_than_min_error_msg));
            maxEditText.requestFocus();
            return false;
        } else {
            maxEditText.setError(null);
        }

        //Range validations
        BigInteger range = RandomDecimalNumbersGenTask.GetDoubleRange(dMinVal, dMaxVal, precision);

        //Check if -0.0 and +0.0 is given as input
        if(range.signum() == 0){
            maxEditText.setError(getString(R.string.max_is_more_than_min_error_msg));
            maxEditText.requestFocus();
            return false;
        } else if(range.signum() == 1){
            maxEditText.setError(null);
        }

        BigInteger decimalQuantity = BigInteger.valueOf(quantityVal);
        if (!repeatAllowed && decimalQuantity.compareTo(range) == 1) {
            quantityEditText.setError(getString(R.string.generation_range_exceeds_error_msg, range));
            quantityEditText.requestFocus();
            return false;
        } else {
            quantityEditText.setError(null);
        }
        return true;
    }

    private boolean ValidateIntegerInputs(boolean repeatAllowed, Integer quantityVal, Integer iMinRange, Integer iMaxRange){
        //Check for Max possible Integer values
        if(iMinRange < Integer.MIN_VALUE || iMinRange > Integer.MAX_VALUE) {
            minEditText.setError(getString(R.string.values_range_error_msg, Integer.MIN_VALUE, Integer.MAX_VALUE));
            minEditText.requestFocus();
            return false;
        } else {
            minEditText.setError(null);
        }

        if(iMaxRange < Integer.MIN_VALUE || iMaxRange > Integer.MAX_VALUE){
            maxEditText.setError(getString(R.string.values_range_error_msg, Integer.MIN_VALUE, Integer.MAX_VALUE));
            maxEditText.requestFocus();
            return false;
        } else {
            maxEditText.setError(null);
        }

        //Min max validation
        if(iMinRange > iMaxRange || iMinRange == iMaxRange){
            maxEditText.setError(getString(R.string.max_is_more_than_min_error_msg));
            maxEditText.requestFocus();
            return false;
        } else {
            maxEditText.setError(null);
        }

        //Range validations
        BigInteger range = RandomIntegerNumbersGenTask.GetIntegerRange(iMinRange, iMaxRange);
        BigInteger integerQuantity = BigInteger.valueOf(quantityVal);
        if (!repeatAllowed && integerQuantity.compareTo(range) == 1) {
            quantityEditText.setError(getString(R.string.generation_range_exceeds_error_msg, range));
            quantityEditText.requestFocus();
            return false;
        } else {
            quantityEditText.setError(null);
        }
        return true;
    }
}
