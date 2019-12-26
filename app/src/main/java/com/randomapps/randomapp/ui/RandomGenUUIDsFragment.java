package com.randomapps.randomapp.ui;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import random.RandomTextFormatterTask;
import random.RandomUUIDsGenTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomGenUUIDsFragment extends Fragment {
    private final int MAX_GENERATION_LIMIT = 10000;
    Boolean formatToVertical = true;

    //Random UUIDs UI elements
    TextInputEditText howManyUUIDsEditText;
    TextView maxUUIDsLimitTextView;
    Button generateRandom;
    EditText resultsEditText;
    ImageView copyImageView, shareImageView, resSeparatorImageView;

    public RandomGenUUIDsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_random_gen_uuids, container, false);

        //Random UUIDs generator UI handling
        InitializeRandomUUIDsGeneratorUIElements(root);
        return root;
    }

    private void InitializeRandomUUIDsGeneratorUIElements(View root) {
        resultsEditText = root.findViewById(R.id.resultTextBox);
        resultsEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        copyImageView = root.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(v -> CopyToClipBoard(v));

        shareImageView = root.findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(v -> ShareTheRandomResult(v));

        resSeparatorImageView = root.findViewById(R.id.resSeparatorImageView);
        resSeparatorImageView.setOnClickListener(v -> UpdateResultsWithSeparator(v));

        maxUUIDsLimitTextView = root.findViewById(R.id.maxUUIDsLimitTextView);
        maxUUIDsLimitTextView.setText(getResources().getString(R.string.text_max_limited_to) + " " + MAX_GENERATION_LIMIT);
        generateRandom = root.findViewById(R.id.butGenerateRandom);
        generateRandom.setOnClickListener(v -> GenerateRandomHandler(v));
        howManyUUIDsEditText = root.findViewById(R.id.howManyUUIDEditText);
        howManyUUIDsEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(howManyUUIDsEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });
        howManyUUIDsEditText.setText("1");
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
                Utils.CopyContentToClipBoard(getActivity(), getString(R.string.text_results), content, toastMsg);
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
        //For Random UUIDs
        GenerateRandomUUIDsHandler(v);
    }

    private void GenerateRandomUUIDsHandler(View v) {
        //int quantity = Integer.parseInt(howManyUUIDsEditText.getText().toString());
        //boolean isToBeVertical  = (resSeparatorImageView.getDrawable().getConstantState() == ContextCompat.getDrawable(getContext(), R.drawable.ic_format_vertical).getConstantState())? true: false;
        //String resultsSeparator = isToBeVertical? ", " : "\n";
        String resultsSeparator = formatToVertical? ", " : "\n";
        if(ValidateGenericInputs()){
            int quantity = Integer.parseInt(howManyUUIDsEditText.getText().toString());
            RandomUUIDsGenTask randomUUIDsGenTask = null;
            randomUUIDsGenTask = new RandomUUIDsGenTask(getActivity(), quantity, resultsEditText, resultsSeparator);
            if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                randomUUIDsGenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                randomUUIDsGenTask.execute();
            }
        }
    }

    private boolean ValidateGenericInputs() {
        if (howManyUUIDsEditText.getText().toString() == null || howManyUUIDsEditText.getText().toString().isEmpty()){
            howManyUUIDsEditText.setError(getString(R.string.value_empty_error_msg));
            howManyUUIDsEditText.requestFocus();
            return false;
        } else {
            howManyUUIDsEditText.setError(null);
        }

        if(Integer.valueOf(howManyUUIDsEditText.getText().toString()) < 1){
            howManyUUIDsEditText.setError(getString(R.string.value_greater_than_zero_error_msg));
            howManyUUIDsEditText.requestFocus();
            return false;
        } else {
            howManyUUIDsEditText.setError(null);
        }

        //MAX Generation limit
        int quantity = Integer.parseInt(howManyUUIDsEditText.getText().toString());
        if(quantity > MAX_GENERATION_LIMIT){
            howManyUUIDsEditText.setError(getString(R.string.max_generation_limit_error_msg_UUIDs, MAX_GENERATION_LIMIT));
            howManyUUIDsEditText.requestFocus();
            return false;
        } else {
            howManyUUIDsEditText.setError(null);
        }
        return true;
    }
}
