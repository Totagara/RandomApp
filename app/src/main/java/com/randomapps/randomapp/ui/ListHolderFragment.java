package com.randomapps.randomapp.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.randomapps.randomapp.Adapter.ListHolderItemAdapter;
import com.randomapps.randomapp.R;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import DB.DBHelper;
import Models.UserList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListHolderFragment extends Fragment {
    final int MAX_LIST_ITEMS__LIMIT = 1000;
    String mode = null;
    DBHelper dbHelper;
    List<String> listItems = new ArrayList<>();
    ListHolderItemAdapter listHolderItemAdapter;
    UserList userList;

    //Add new list UI elements
    Button butSaveList, butCancel, viewSavedListButton;
    ListView listView;
    EditText addITemEditText;
    TextInputEditText listNameEditText;
    ImageButton butAddItem;

    public ListHolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_list_holder, container, false);

        InitializeListHolderListUIElements(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null){
            ListHolderFragmentArgs args = ListHolderFragmentArgs.fromBundle(getArguments());
            if (args.getMode() != null) {
                mode = args.getMode();
                if(mode == "EDIT") {
                    userList = args.getUserList();
                    FillUserListValuesToEdit(userList);
                }
            }
        }
    }

    private void FillUserListValuesToEdit(UserList passedUserList) {
        listNameEditText.setText(passedUserList.getListName());
        listItems.addAll(passedUserList.getItems());
        listHolderItemAdapter = new ListHolderItemAdapter(getActivity(), R.layout.lisholder_item, listItems);
        listView.setAdapter(listHolderItemAdapter);
        listHolderItemAdapter.notifyDataSetChanged();
    }

    private void InitializeListHolderListUIElements(View root) {
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
        listNameEditText.setHint(getString(R.string.text_listName));

        //custom adapter
        listHolderItemAdapter = new ListHolderItemAdapter(getActivity(), R.layout.lisholder_item, listItems);
        listView.setAdapter(listHolderItemAdapter);
        listHolderItemAdapter.notifyDataSetChanged();
    }

    private void ShowMaxListItemsError(){
        AlertDialog.Builder maxListItemsErrorDialogBuilder = new AlertDialog.Builder(getActivity());
        maxListItemsErrorDialogBuilder.setTitle("Max limit error");
        maxListItemsErrorDialogBuilder.setMessage("Maximum "+ MAX_LIST_ITEMS__LIMIT +" list items can be stored");
        maxListItemsErrorDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog deleteDialog = maxListItemsErrorDialogBuilder.create();
        deleteDialog.show();
    }

    private void AddItemToList(View v) {
        String itemValue = addITemEditText.getText().toString().trim();
        if(listItems.size() >= MAX_LIST_ITEMS__LIMIT){
            ShowMaxListItemsError();
        } else {
            if (!itemValue.isEmpty()) {
                listItems.add(itemValue);
                //listAdapter.notifyDataSetChanged();
                listHolderItemAdapter.notifyDataSetChanged();
                addITemEditText.setText("");
            }
        }
        addITemEditText.requestFocus();
    }

    private void SaveNewList(View v) {
        //Hide the keyBoard
        Utils.hideKeyboard(v, getActivity());

        String listName = listNameEditText.getText().toString().trim();
        Integer itemsCount = listItems.size();
        if(GenericInputValidation(listName)){
            if(mode == "EDIT"){
                if(dbHelper.updateUserList(userList.getListId(), listName, listItems, itemsCount)){
                    Snackbar snack = Snackbar.make(getView(), getString(R.string.list_updated_success_msg), Snackbar.LENGTH_SHORT);
                    View view = snack.getView();
                    TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                } else {
                    Snackbar snack = Snackbar.make(getView(), getString(R.string.list_updated_error_msg), Snackbar.LENGTH_SHORT);
                    View view = snack.getView();
                    TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.show();
                }
            } else {
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
            }
            //Move back
            Navigation.findNavController(v).popBackStack();
        }
    }

    private boolean GenericInputValidation(String listName) {
        if(listName.isEmpty()){
            listNameEditText.setError(getString(R.string.empty_list_name_error));
            listNameEditText.requestFocus();
            return false;
        } else {
            listNameEditText.setError(null);
        }

        if(listView.getCount() < 1){
            addITemEditText.setError(getString(R.string.no_list_items_error));
            addITemEditText.requestFocus();
            return false;
        } else {
            addITemEditText.setError(null);
        }
        return true;
    }

    private void CancelButtonHandler(View v) {
        Navigation.findNavController(v).popBackStack();
    }

    private void ViewSavedLists(View v) {
        Navigation.findNavController(v).navigate(R.id.action_listHolder_to_savedLists);
    }
}
