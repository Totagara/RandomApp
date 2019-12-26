package com.randomapps.randomapp.ui;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import random.RandomStringsGenTask;
import random.RandomTextFormatterTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomGenStringsFragment extends Fragment {
    final int MAX_GENERATION_LIMIT = 10000;
    Boolean formatToVertical = true;

    final int MAX_STRING_LIMIT = 100;

    //Random Strings generator UI elements
    Button generateRandom;
    EditText resultsEditText;
    TextInputEditText minLengthEditText, maxLengthEditText, quantityStringsEditText, specialCharsEditText;
    CheckBox upperCaseCheckbox, lowerCaseCheckbox, digitsCheckbox, specialCharsCheckbox;
    ImageView copyImageView, shareImageView, resSeparatorImageView;

    public RandomGenStringsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_random_gen_strings, container, false);

        //Random Strings generator UI handling
        InitializeRandomStringsGeneratorUIElements(root);
        return root;
    }

    private void InitializeRandomStringsGeneratorUIElements(View root) {
        String specialCharacterSet = "!@#$%^&*()-_+=~./\\,\':;\"|{}[]<>?";
        InputFilter SpecialCharFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (!specialCharacterSet.contains(("" + source)) || dest.toString().contains(source)) {
                    return "";
                }
                return null;
            }
        };

        resultsEditText = root.findViewById(R.id.resultTextBox);
        resultsEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        copyImageView = root.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(v -> CopyToClipBoard(v));

        shareImageView = root.findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(v -> ShareTheRandomResult(v));
        
        resSeparatorImageView = root.findViewById(R.id.resSeparatorImageView);
        resSeparatorImageView.setOnClickListener(v -> UpdateResultsWithSeparator(v));

        generateRandom = root.findViewById(R.id.butGenerateRandom);
        generateRandom.setOnClickListener(v -> GenerateRandomHandler(v));

        minLengthEditText = root.findViewById(R.id.minLengthEditText);
        minLengthEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(minLengthEditText, 1,MAX_STRING_LIMIT, null, getActivity()) });
        maxLengthEditText = root.findViewById(R.id.maxLengthEditText);
        maxLengthEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(maxLengthEditText, 1,MAX_STRING_LIMIT, null, getActivity()) });
        quantityStringsEditText = root.findViewById(R.id.quantityStringsEditText);
        quantityStringsEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(quantityStringsEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });

        upperCaseCheckbox = root.findViewById(R.id.upperCaseCheckbox);
        lowerCaseCheckbox = root.findViewById(R.id.lowerCaseCheckbox);
        digitsCheckbox = root.findViewById(R.id.digitsCheckbox);
        specialCharsCheckbox = root.findViewById(R.id.specialCharsCheckbox);
        specialCharsCheckbox.setOnClickListener(v -> SpecialCharsCheckboxChangeHandler(v));
        specialCharsEditText = root.findViewById(R.id.specialCharsEditText);
        specialCharsEditText.setText("!@#$&*_-%^");
        specialCharsEditText.setFilters(new InputFilter[] { SpecialCharFilter });
        specialCharsEditText.setEnabled(false);

        //Set uppercase, lowercase and digits to default
        upperCaseCheckbox.setChecked(true);
        lowerCaseCheckbox.setChecked(true);
        digitsCheckbox.setChecked(true);

        //Set min, max and quantity to defaults
        minLengthEditText.setText("1");
        maxLengthEditText.setText("10");
        quantityStringsEditText.setText("1");
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

    private void CopyToClipBoard(View v) {
        String content = resultsEditText.getText().toString();
        if(!content.equals("") && content != null) {
            try {
                String toastMsg = getString(R.string.results_copied_success_msg);
                Utils.CopyContentToClipBoard(getActivity(), getString(R.string.hint_text_results), content, toastMsg);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.too_large_to_copy_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_copy_error), Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateResultsWithSeparator(View v) {
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

    private void GenerateRandomHandler(View v) {
        //Hide the keyBoard
        Utils.hideKeyboard(v, getActivity());
        //For Random strings
        GenerateRandomStringsHandler(v);
    }

    //private Boolean ValidateInputs(int minLen, int maxLen, int quantity){
    private Boolean ValidateInputs(){
        //Check empty validation
        if(minLengthEditText.getText().toString().isEmpty()){
            minLengthEditText.setError(getString(R.string.value_empty_error_msg));
            minLengthEditText.requestFocus();
            return false;
        } else {
            minLengthEditText.setError(null);
        }

        if(maxLengthEditText.getText().toString().isEmpty()){
            maxLengthEditText.setError(getString(R.string.value_empty_error_msg));
            maxLengthEditText.requestFocus();
            return false;
        } else {
            maxLengthEditText.setError(null);
        }

        if(quantityStringsEditText.getText().toString().isEmpty()){
            quantityStringsEditText.setError(getString(R.string.value_empty_error_msg));
            quantityStringsEditText.requestFocus();
            return false;
        } else {
            quantityStringsEditText.setError(null);
        }

        int minLen = Integer.valueOf(minLengthEditText.getText().toString());
        int maxLen = Integer.valueOf(maxLengthEditText.getText().toString());
        int quantity = Integer.valueOf(quantityStringsEditText.getText().toString());

        if(!upperCaseCheckbox.isChecked() && !lowerCaseCheckbox.isChecked()
                && !digitsCheckbox.isChecked() && !specialCharsCheckbox.isChecked() ){

            AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity())
                    .setMessage(getString(R.string.no_char_type_selected_error_msg))
                    .setTitle(getString(R.string.dialog_header_usage_notice)).setPositiveButton(getString(R.string.button_ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
            dialog.show();
            return false;
        }

        if(minLen < 1 || minLen > 100){
            minLengthEditText.setError(getString(R.string.string_len_range_error_msg));
            minLengthEditText.requestFocus();
            return false;
        } else {
            minLengthEditText.setError(null);
        }

        if(maxLen < 1 || maxLen > 100){
            maxLengthEditText.setError(getString(R.string.string_len_range_error_msg));
            maxLengthEditText.requestFocus();
            return false;
        } else {
            maxLengthEditText.setError(null);
        }

        if(maxLen < minLen){
            maxLengthEditText.setError(getString(R.string.max_is_more_than_min_error_msg));
            maxLengthEditText.requestFocus();
            return false;
        } else {
            maxLengthEditText.setError(null);
        }

        if(quantity < 1){
            quantityStringsEditText.setError(getString(R.string.value_greater_than_zero_error_msg));
            quantityStringsEditText.requestFocus();
            return false;
        } else {
            quantityStringsEditText.setError(null);
        }

        //MAX Generation limit
        if(quantity > MAX_GENERATION_LIMIT){
            quantityStringsEditText.setError(getString(R.string.max_strings_generation_limit_error_msg, MAX_GENERATION_LIMIT));
            quantityStringsEditText.requestFocus();
            return false;
        } else {
            quantityStringsEditText.setError(null);
        }

        if(specialCharsCheckbox.isChecked() && specialCharsEditText.getText().toString().isEmpty()){
            specialCharsEditText.setError(getString(R.string.min_special_char_required_error_msg));
            specialCharsEditText.requestFocus();
            return false;
        } else {
            specialCharsEditText.setError(null);
        }
        return true;
    }

    private void GenerateRandomStringsHandler(View v) {
        resultsEditText.setText("");
        /*int minLen = Integer.valueOf(minLengthEditText.getText().toString());
        int maxLen = Integer.valueOf(maxLengthEditText.getText().toString());
        int quantity = Integer.valueOf(quantityStringsEditText.getText().toString());*/
        boolean upperCaseAllowed = upperCaseCheckbox.isChecked(),
                lowerCaseAllowed = lowerCaseCheckbox.isChecked(),
                digitsAllowed = digitsCheckbox.isChecked(),
                specialCharsAllowed = specialCharsCheckbox.isChecked();

        //boolean isToBeVertical  = (resSeparatorImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.ic_format_vertical).getConstantState())? true: false;
        //String resultsSeparator = isToBeVertical? ", " : "\n";
        String resultsSeparator = formatToVertical? ", " : "\n";

        //if(ValidateInputs(minLen, maxLen, quantity)) {
        if(ValidateInputs()) {
            int minLen = Integer.valueOf(minLengthEditText.getText().toString());
            int maxLen = Integer.valueOf(maxLengthEditText.getText().toString());
            int quantity = Integer.valueOf(quantityStringsEditText.getText().toString());
            RandomStringsGenTask randomStringsGenTask = null;
            String specCharSet = specialCharsAllowed ? specialCharsEditText.getText().toString() : null;
            randomStringsGenTask = new RandomStringsGenTask(getActivity(),minLen, maxLen, quantity,
                    upperCaseAllowed, lowerCaseAllowed, digitsAllowed, specialCharsAllowed, specCharSet, resultsEditText, resultsSeparator);


            if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                randomStringsGenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                randomStringsGenTask.execute();
            }
        }
    }

    private void SpecialCharsCheckboxChangeHandler(View v) {
        if(((CheckBox)v).isChecked()){
            specialCharsEditText.setEnabled(true);
        } else {
            specialCharsEditText.setEnabled(false);
        }
    }
}
