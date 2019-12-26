package com.randomapps.randomapp.ui;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigInteger;
import java.util.List;

import DB.DBHelper;
import random.RandomSeriesGenTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesGeneratorFragment extends Fragment {
    final int MAX_GENERATION_LIMIT = 1000;
    DBHelper dbHelper;
    List<String> selectedListItems = null;

    //Series generator UI elements
    Button butGenerateRandomSeries;
    Spinner selectSeriesGenListSpinner;
    EditText resultTextBox, selectedItemsEditText;
    TextInputEditText howManySeriesEditText, seriesSizeEditText;
    CheckBox excludeGeneratedItemsCheckbox, excludeSetsWithSameItemsCheckbox;
    LinearLayout selectedListItemsLayout;
    TextView butNewList;
    ImageView copyImageView, shareImageView;
    TextInputLayout selectedItemsTextInput;

    public SeriesGeneratorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_series_generator, container, false);

        //Series generator UI handling
        InitializeSeriesGeneratorUIElements(root);
        return root;
    }

    private void InitializeSeriesGeneratorUIElements(View root) {
        butNewList = root.findViewById(R.id.butNewList);
        butNewList.setOnClickListener(v -> AddNewList(v));
        butGenerateRandomSeries = root.findViewById(R.id.butGenerateRandomSeries);
        butGenerateRandomSeries.setOnClickListener(v -> GenerateSeriesHandler(v));
        selectSeriesGenListSpinner = root.findViewById(R.id.selectSeriesGenListSpinner);
        SetUpListToSpinner(selectSeriesGenListSpinner);

        selectSeriesGenListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedList = String.valueOf(selectSeriesGenListSpinner.getSelectedItem());
                ShowSelectedListDetails(selectSeriesGenListSpinner, selectedList);
                DefaultInputValuesSetupForSeriesGeneration();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seriesSizeEditText = root.findViewById(R.id.seriesSizeEditText);
        seriesSizeEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(seriesSizeEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });

        howManySeriesEditText = root.findViewById(R.id.howManySeriesEditText);
        howManySeriesEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(howManySeriesEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });
        resultTextBox = root.findViewById(R.id.resultTextBox);
        resultTextBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        copyImageView = root.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(v -> CopyToClipBoard(v));

        shareImageView = root.findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(v -> ShareTheRandomResult(v));

        excludeSetsWithSameItemsCheckbox = root.findViewById(R.id.excludeSetsWithSameItemsCheckbox);

        excludeGeneratedItemsCheckbox = root.findViewById(R.id.excludeGeneratedItemsCheckbox);
        excludeGeneratedItemsCheckbox.setOnClickListener(v -> ExcludeGeneratedItemsCheckboxHandler(v));

        selectedListItemsLayout = root.findViewById(R.id.selectedListItemsLayout);
        selectedItemsTextInput = root.findViewById(R.id.selectedItemsTextInput);
        selectedItemsEditText = root.findViewById(R.id.selectedListItemsEditText);
        selectedItemsEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        //Setup first time default input values
        DefaultInputValuesSetupForSeriesGeneration();
    }

    private void ShareTheRandomResult(View v) {
        String content = resultTextBox.getText().toString();
        if(!content.equals("") && content != null) {
            Utils.ShareTheResultContent(content, getActivity());
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_share_error), Toast.LENGTH_LONG).show();
        }
    }

    private void CopyToClipBoard(View v) {
        String content = resultTextBox.getText().toString();
        if(!content.equals("") && content != null) {
            String toastMsg = getString(R.string.results_copied_success_msg);
            Utils.CopyContentToClipBoard(getActivity(), getString(R.string.text_results), content, toastMsg);
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_resultsTo_copy_error), Toast.LENGTH_LONG).show();
        }
    }

    private void DefaultInputValuesSetupForSeriesGeneration() {
        //after spinner set up setup defaults with selected list
        String selectedListItem = String.valueOf(selectSeriesGenListSpinner.getSelectedItem());
        List<String> listItemsSet = dbHelper.getListItems(selectedListItem);
        if(listItemsSet != null && listItemsSet.size() > 0){
            howManySeriesEditText.setText("1");
            seriesSizeEditText.setText(String.valueOf(listItemsSet.size()));
        }
    }

    private void GenerateSeriesHandler(View v){
        //Hide the keyBoard
        Utils.hideKeyboard(v, getActivity());
        //For Random Series
        GenerateSeries(v);
    }

    private void GenerateSeries(View v) {
        resultTextBox.setText("");
        List<String> seriesItemsList = null;
        if(ValidateGenericInput()){
            int seriesSize = Integer.parseInt(seriesSizeEditText.getText().toString());
            int quantity = Integer.parseInt(howManySeriesEditText.getText().toString());

            Boolean excludeSetsWithSameItems = excludeSetsWithSameItemsCheckbox.isChecked();
            Boolean excludeGeneratedItems = excludeGeneratedItemsCheckbox.isChecked();

            //Selected list items
            seriesItemsList = dbHelper.getListItems(String.valueOf(selectSeriesGenListSpinner.getSelectedItem()));
            RandomSeriesGenTask randomSeriesGenTask = null;
            randomSeriesGenTask = new RandomSeriesGenTask(getActivity(), quantity, seriesSize, seriesItemsList, excludeGeneratedItems, resultTextBox);

            BigInteger possibleSeriesCount = randomSeriesGenTask.GetPossibleSeriesCount(seriesItemsList, seriesSize, quantity, excludeSetsWithSameItems,  excludeGeneratedItems,  seriesItemsList.size());
            if(ValidateSeriesGeneratorInputValues(seriesSize, quantity, seriesItemsList, possibleSeriesCount)){
                if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                    randomSeriesGenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    randomSeriesGenTask.execute();
                }
            }
        }
    }

    private boolean ValidateGenericInput() {
        //Error validations
        if(howManySeriesEditText.getText().toString().isEmpty() || howManySeriesEditText.getText().toString() == null){
            howManySeriesEditText.setError(getString(R.string.value_empty_error_msg));
            howManySeriesEditText.requestFocus();
            return false;
        } else {
            howManySeriesEditText.setError(null);
        }

        if(seriesSizeEditText.getText().toString().isEmpty() || seriesSizeEditText.getText().toString() == null){
            seriesSizeEditText.setError(getString(R.string.value_empty_error_msg));
            seriesSizeEditText.requestFocus();
            return false;
        } else {
            seriesSizeEditText.setError(null);
        }

        if(selectSeriesGenListSpinner.getAdapter().getCount() < 1){
            Snackbar snack = Snackbar.make(getView(), getString(R.string.add_list_error, getString(R.string.text_series)), Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();

            butNewList.requestFocus();
            return false;
        }
        return true;
    }

    Boolean ValidateSeriesGeneratorInputValues(int seriesSize, int quantity, List<String> seriesItemsList, BigInteger possibleSeriesCount){
        if(seriesSize > seriesItemsList.size()){
            seriesSizeEditText.setError(getString(R.string.series_size_morethan_list_items_error));
            seriesSizeEditText.requestFocus();
            return false;
        } else {
            seriesSizeEditText.setError(null);
        }

        //MAX Generation limit
        if(quantity > MAX_GENERATION_LIMIT){
            howManySeriesEditText.setError(getString(R.string.max_generation_limit_error_msg_series_generation, MAX_GENERATION_LIMIT));
            howManySeriesEditText.requestFocus();
            return false;
        } else {
            howManySeriesEditText.setError(null);
        }

        if(possibleSeriesCount.compareTo(BigInteger.valueOf(quantity)) == -1){
            howManySeriesEditText.setError(getString(R.string.series_range_exceed_error_msg, possibleSeriesCount));
            howManySeriesEditText.requestFocus();
            return false;
        } else {
            howManySeriesEditText.setError(null);
        }
        return true;
    }

    private void ShowSelectedListDetails(Spinner selectSeriesGenListSpinner, String selectedList){
        List<String> listItems = dbHelper.getListItems(selectedList);
        selectedListItems = listItems;
        if(listItems != null && listItems.size() > 0) {
            selectedListItemsLayout.setVisibility(View.VISIBLE);

            //selectedItemsEditText.setText(TextUtils.join(", ", listItems));
            selectedItemsEditText.post(new Runnable() {
                @Override
                public void run() {
                    selectedItemsEditText.setText(TextUtils.join(", ", listItems));
                }
            });

            selectedItemsTextInput.setHint(getString(R.string.text_selected_listItems) + listItems.size());


        }
    }

    private void ExcludeGeneratedItemsCheckboxHandler(View v) {
        if(excludeGeneratedItemsCheckbox.isChecked()){
            excludeSetsWithSameItemsCheckbox.setChecked(false);
            excludeSetsWithSameItemsCheckbox.setEnabled(false);
        } else {
            excludeSetsWithSameItemsCheckbox.setEnabled(true);
        }
    }

    private void SetUpListToSpinner(Spinner selectSeriesGenListSpinner) {
        List<String> listNames = dbHelper.getAllListNames();
        ArrayAdapter<String> listSpinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listNames);
        listSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSeriesGenListSpinner.setAdapter(listSpinnerAdapter);
    }

    private void AddNewList(View v) {
        Navigation.findNavController(v).navigate(R.id.action_seriesGenerator_to_listHolder);
    }
}
