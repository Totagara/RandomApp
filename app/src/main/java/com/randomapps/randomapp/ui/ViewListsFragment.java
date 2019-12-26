package com.randomapps.randomapp.ui;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.randomapps.randomapp.Adapter.UserListsAdapter;
import com.randomapps.randomapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DB.DBHelper;
import Models.UserList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewListsFragment extends Fragment {
    DBHelper dbHelper;
    List<UserList> userLists;
    ListView listView;
    Button butAddNew1;
    TextView butAddNew;

    public ViewListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());
        userLists = new ArrayList<>();

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_view_lists, container, false);
        listView = root.findViewById(R.id.userListsListView);
        butAddNew = root.findViewById(R.id.butAddNew);
        butAddNew.setOnClickListener(v -> AddNewList(v));

        LoadUserLists(root);
        return root;
    }

    private void AddNewList(View v) {
        ViewListsFragmentDirections.ActionSavedListsToListHolder actionDirections = ViewListsFragmentDirections.actionSavedListsToListHolder();
        actionDirections.setUserList(null);
        actionDirections.setMode("NEW");
        Navigation.findNavController(v).navigate(actionDirections);
    }

    private void LoadUserLists(View root) {
        Cursor cursor = dbHelper.getAllLists();
        if(cursor.moveToFirst()){
            do{
                String itemsText = cursor.getString(2);
                String[] itemsArray = itemsText.split(",");
                List<String> items = Arrays.asList(itemsArray);
                userLists.add(new UserList(
                    cursor.getInt(0), //id
                        cursor.getString(1), //name
                        items, //items
                        cursor.getInt(3), //itemsCount
                        cursor.getString(4) //lastUpdated
                ));
            } while (cursor.moveToNext());
            UserListsAdapter userListAdapter = new UserListsAdapter(getActivity(), R.layout.listlayout_userlist, userLists, root);
            listView.setAdapter(userListAdapter);
            userListAdapter.notifyDataSetChanged();
        }
    }
}
