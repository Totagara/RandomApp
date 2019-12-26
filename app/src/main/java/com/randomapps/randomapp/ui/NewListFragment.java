package com.randomapps.randomapp.ui;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.ListHandler;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import DB.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewListFragment extends Fragment {
    ListHandler listHandler = null;
    DBHelper dbHelper;
    ArrayList<String> listItems ;
    ArrayAdapter<String> listAdapter;

    ////Add new list UI elements
    Button butAddItem, butSaveList, butCancel, viewSavedListButton;
    ListView listView;
    EditText addITemEditText, listNameEditText;

    public NewListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listHandler = new ListHandler();
        dbHelper = new DBHelper(getActivity());

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_new_list, container, false);

        //Series generator UI handling
        InitializeAddNewListUIElements(root);
        return root;
    }

    private void InitializeAddNewListUIElements(View root) {
        butAddItem = root.findViewById(R.id.butAddItem);
        butAddItem.setOnClickListener(v -> AddItemToList(v));

        butSaveList = root.findViewById(R.id.butSaveList);
        butSaveList.setOnClickListener(v -> SaveNewList(v));

        butCancel = root.findViewById(R.id.butCancel);
        butCancel.setOnClickListener(v -> CancelButtonHandler(v));

        viewSavedListButton = root.findViewById(R.id.viewSavedListButton);
        viewSavedListButton.setOnClickListener(v -> ViewSavedLists(v));

        listView = root.findViewById(R.id.newList);
        addITemEditText = root.findViewById(R.id.addITemEditText);
        addITemEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    butAddItem.performClick();
                    return true;
                }
                return false;
            }
        });

        listNameEditText = root.findViewById(R.id.listNameEditText);
        listItems = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(listAdapter);
    }

    private void ViewSavedLists(View v) {
    }

    private void CancelButtonHandler(View v) {
        Navigation.findNavController(v).navigate(R.id.action_newList_to_seriesGenerator);
    }

    private void SaveNewList(View v) {
        String listName = listNameEditText.getText().toString().trim();
        if(listName.isEmpty()){
            listNameEditText.setError(getString(R.string.empty_list_name_error));
            listNameEditText.requestFocus();
            return;
        } else {
            listNameEditText.setError(null);
        }

        if(listView.getCount() < 1){
            addITemEditText.setError(getString(R.string.no_list_items_error));
            addITemEditText.requestFocus();
            return;
        } else {
            addITemEditText.setError(null);
        }

        Integer itemsCount = listItems.size();
        if(dbHelper.addList(listName, listItems, itemsCount)){
            Snackbar snack = Snackbar.make(getView(), getString(R.string.ListAdditionSuccessMessage), Snackbar.LENGTH_SHORT);
            View view = snack.getView();
            TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();

        } else {
            Snackbar snack = Snackbar.make(getView(), getString(R.string.ListAdditionFailedMessage), Snackbar.LENGTH_SHORT);
            View view = snack.getView();
            TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        }

        //Move back to series Generator page
        Navigation.findNavController(v).navigate(R.id.action_newList_to_seriesGenerator);
    }

    private void AddItemToList(View v) {
        String itemValue = addITemEditText.getText().toString().trim();
        if(!itemValue.isEmpty()) {
            listItems.add(itemValue);
            listAdapter.notifyDataSetChanged();
            addITemEditText.setText("");
        }
        addITemEditText.requestFocus();
    }
}
