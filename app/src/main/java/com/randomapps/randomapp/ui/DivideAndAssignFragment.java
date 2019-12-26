package com.randomapps.randomapp.ui;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.ListHandler;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Collections;
import java.util.List;

import DB.DBHelper;
import random.RandomDivideAndAssignGenTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class DivideAndAssignFragment extends Fragment {
    final int MAX_GENERATION_LIMIT = 1000;
    DBHelper dbHelper;
    ListHandler listHandler = null;
    List<String> selectedListItems = null;

    //Divide and assign UI elements
    Button butGenerateRandomSeries;
    EditText selectedListItemsEditText, resultTextBox;
    TextView butNewList;
    TextInputEditText numberOfTeamsEditText, itemsPerTeamEditText;
    TextInputLayout selectedItemsTextInput;
    Spinner selectListSpinner;
    LinearLayout selectedListItemsLayout;
    ImageView copyImageView, shareImageView;

    public DivideAndAssignFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());
        listHandler = new ListHandler();

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_divide_and_assign, container, false);

        //Series generator UI handling
        InitializeDivideAndAssignUIElements(root);
        return root;
    }

    private void InitializeDivideAndAssignUIElements(View root) {
        butNewList = root.findViewById(R.id.butNewList);
        butNewList.setOnClickListener(v -> AddNewList(v));
        butGenerateRandomSeries = root.findViewById(R.id.butGenerateRandomSeries);


        numberOfTeamsEditText = root.findViewById(R.id.numberOfTeamsEditText);
        numberOfTeamsEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(numberOfTeamsEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });

        itemsPerTeamEditText = root.findViewById(R.id.itemsPerTeamEditText);
        itemsPerTeamEditText.setFilters(new InputFilter[] {Utils.AttachInputFilter(itemsPerTeamEditText, 1,MAX_GENERATION_LIMIT, null, getActivity()) });


        selectedListItemsLayout = root.findViewById(R.id.selectedListItemsLayout);
        selectedItemsTextInput = root.findViewById(R.id.selectedItemsTextInput);
        selectedListItemsEditText = root.findViewById(R.id.selectedListItemsEditText);
        selectedListItemsEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        selectedListItemsEditText.setMovementMethod(new ScrollingMovementMethod());
        selectListSpinner = root.findViewById(R.id.selectListSpinner);

        copyImageView = root.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(v -> CopyToClipBoard(v));

        shareImageView = root.findViewById(R.id.shareImageView);
        shareImageView.setOnClickListener(v -> ShareTheRandomResult(v));

        SetUpListToSpinner(selectListSpinner);

        selectListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedList = String.valueOf(selectListSpinner.getSelectedItem());
                ShowSelectedListDetails(selectListSpinner, selectedList);
                DefaultInputValuesSetupForTeamsGeneration();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        numberOfTeamsEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(selectedListItems != null && selectedListItems.size() > 0) {
                    if (numberOfTeamsEditText.hasFocus() && s.length() > 0 && !s.toString().equals("")) {
                        int listItemsCount = selectedListItems != null ? selectedListItems.size() : 0;
                        int teamsCount = Integer.valueOf(s.toString());
                        int teamSize = listItemsCount / teamsCount;

                        //check if list items count/team sie is less than given teams count
                        int itemsPerTeam = (listItemsCount % teamsCount) == 0 ? teamSize : (teamSize + 1);
                        int possibleTeamsCount = listItemsCount % itemsPerTeam == 0 ? (listItemsCount / itemsPerTeam) : (listItemsCount / itemsPerTeam) + 1;
                        if (possibleTeamsCount < teamsCount) {
                            itemsPerTeam = teamSize;
                        } else {
                            itemsPerTeam = (listItemsCount % teamsCount) == 0 ? teamSize : (teamSize + 1);
                        }

                        //int itemsPerTeam = (listItemsCount % teamsCount) == 0? teamSize : (teamSize + 1);
                        itemsPerTeamEditText.setText(String.valueOf(itemsPerTeam));
                        //itemsPerTeamEditText.setText(String.valueOf(teamSize));
                    }
                }
            }


        });

        itemsPerTeamEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(selectedListItems != null && selectedListItems.size() > 0) {
                    if (itemsPerTeamEditText.hasFocus() && s.length() > 0 && !s.toString().equals("") && selectedListItems.size() > 0) {
                        int listItemsCount = selectedListItems != null ? selectedListItems.size() : 0;
                        int itemsPerTeam = Integer.valueOf(s.toString());
                        int numOfTeams = listItemsCount / itemsPerTeam;
                        int teamsCount = (listItemsCount % itemsPerTeam == 0) ? numOfTeams : (numOfTeams + 1);
                        numberOfTeamsEditText.setText(String.valueOf(teamsCount));
                    }
                }
            }
        });

        butGenerateRandomSeries.setOnClickListener(v -> GenerateTeamsAndDivideAssignHandler(v));
        resultTextBox = root.findViewById(R.id.resultTextBox);
        resultTextBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        //Setup first time default input values
        DefaultInputValuesSetupForTeamsGeneration();
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

    private void DefaultInputValuesSetupForTeamsGeneration() {
        //after spinner set up setup defaults with selected list
        String selectedListItem = String.valueOf(selectListSpinner.getSelectedItem());
        List<String> listItemsSet = dbHelper.getListItems(selectedListItem);
        if(listItemsSet != null && listItemsSet.size() > 0){
            numberOfTeamsEditText.setText("1");
            itemsPerTeamEditText.setText(String.valueOf(listItemsSet.size()));
        }
    }

    private void ShowSelectedListDetails(Spinner selectSeriesGenListSpinner, String selectedList){
        List<String> listItems = dbHelper.getListItems(selectedList);
        selectedListItems = listItems;
        if(listItems != null && listItems.size() > 0) {
            selectedListItemsLayout.setVisibility(View.VISIBLE);

            //selectedListItemsEditText.setText(TextUtils.join(", ", listItems));
            selectedListItemsEditText.post(new Runnable() {
                @Override
                public void run() {
                    selectedListItemsEditText.setText(TextUtils.join(", ", listItems));
                }
            });

            selectedItemsTextInput.setHint(getString(R.string.text_selected_listItems) + listItems.size());
        } else {
            selectedItemsTextInput.setHint(getString(R.string.text_selected_listItems));
        }
    }

    private void GenerateTeamsAndDivideAssignHandler(View v){
        //Hide the keyBoard
        Utils.hideKeyboard(v, getActivity());
        //For Random Series
        GenerateTeams(v);
    }

    private void GenerateTeams(View v) {
        if(ValidateGenericInput()){
            //shuffle the list before partition for random team allocation
            Collections.shuffle(selectedListItems);

            int howManyTeamsCount = Integer.parseInt(numberOfTeamsEditText.getText().toString());
            int itemsPerTeamCount = Integer.parseInt(itemsPerTeamEditText.getText().toString());

            RandomDivideAndAssignGenTask randomDivideAndAssignGenTask = null;
            if(ValidateDivideAndAssignInputs(itemsPerTeamCount, howManyTeamsCount)) {
                randomDivideAndAssignGenTask = new RandomDivideAndAssignGenTask(getActivity(), selectedListItems, howManyTeamsCount, itemsPerTeamCount, resultTextBox);
                if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
                    randomDivideAndAssignGenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    randomDivideAndAssignGenTask.execute();
                }
            }
        }
    }

    private boolean ValidateDivideAndAssignInputs(int itemsPerTeamCount, int howManyTeamsCount) {
        if(selectedListItems.size() < itemsPerTeamCount){
            itemsPerTeamEditText.setError(getString(R.string.team_size_morethan_list_items_error));
            itemsPerTeamEditText.requestFocus();
            return false;
        } else {
            itemsPerTeamEditText.setError(null);
        }

        if(selectedListItems.size() < howManyTeamsCount){
            numberOfTeamsEditText.setError(getString(R.string.number_0f_teams__morethan_list_items_error));
            numberOfTeamsEditText.requestFocus();
            return false;
        } else {
            numberOfTeamsEditText.setError(null);
        }
        return true;
    }

    private boolean ValidateGenericInput() {
        //Error validations
        if(numberOfTeamsEditText.getText().toString().isEmpty() || numberOfTeamsEditText.getText().toString() == null){
            numberOfTeamsEditText.setError(getString(R.string.value_empty_error_msg));
            numberOfTeamsEditText.requestFocus();
            return false;
        } else {
            numberOfTeamsEditText.setError(null);
        }

        if(itemsPerTeamEditText.getText().toString().isEmpty() || itemsPerTeamEditText.getText().toString() == null){
            itemsPerTeamEditText.setError(getString(R.string.value_empty_error_msg));
            itemsPerTeamEditText.requestFocus();
            return false;
        } else {
            itemsPerTeamEditText.setError(null);
        }

        if(selectListSpinner.getAdapter().getCount() < 1){
            Snackbar snack = Snackbar.make(getView(), getString(R.string.add_list_error, getString(R.string.text_teams)), Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();

            butNewList.requestFocus();
            return false;
        }

        if(selectedListItems == null && selectedListItems.size() < 1){
            Snackbar snack = Snackbar.make(getView(), getString(R.string.list_empty_msg), Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();

            butNewList.requestFocus();
            return false;
        }
        return true;
    }

    private void SetUpListToSpinner(Spinner selectListSpinner) {
        List<String> listNames = dbHelper.getAllListNames();
        ArrayAdapter<String> listSpinnerAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, listNames);
        listSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectListSpinner.setAdapter(listSpinnerAdapter);
    }

    private void AddNewList(View v) {
        Navigation.findNavController(v).navigate(R.id.action_divideAndAssign_to_listHolder);
    }
}
