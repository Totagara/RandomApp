package com.randomapps.randomapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.randomapps.randomapp.R;
import com.randomapps.randomapp.ui.ViewListsFragmentDirections;
import com.randomapps.randomapp.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;

import DB.DBHelper;
import Models.UserList;

public class UserListsAdapter extends ArrayAdapter<UserList> {
    Context context;
    int layoutResource;
    List<UserList> userLists;
    View curView;
    DBHelper dbHelper;

    public UserListsAdapter(Context context, int layoutResource, List<UserList> userLists, View view){
        super(context, layoutResource, userLists);
        this.context = context;
        this.layoutResource = layoutResource;
        this.userLists = userLists;
        curView = view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutResource, null);
        dbHelper = new DBHelper(context);
        UserList userList = userLists.get(position);

        TextView listNameTextView = view.findViewById(R.id.textViewListName);
        TextView itemsCountValTextView = view.findViewById(R.id.textViewItemsCountVal);
        TextView textViewLastModifiedVal = view.findViewById(R.id.textViewLastModifiedVal);
        RelativeLayout listInfoLayout = view.findViewById(R.id.listInfoLayout);

        //Replacing listview with expandable list view
        ListView listView = view.findViewById(R.id.itemsListView);
        //ExpandableListView exListView = view.findViewById(R.id.expandableItemsListView);

        listNameTextView.setText(userList.getListName());
        itemsCountValTextView.setText(userList.getItemsCount().toString());
        textViewLastModifiedVal.setText(userList.getLastModified());


        //Button DeleteButton = view.findViewById(R.id.buttonDeleteList);
        ImageButton editButton = view.findViewById(R.id.buttonEditList);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewListsFragmentDirections.ActionSavedListsToListHolder actionDirections = ViewListsFragmentDirections.actionSavedListsToListHolder();
                actionDirections.setUserList(userList);
                actionDirections.setMode("EDIT");
                Navigation.findNavController(curView).navigate(actionDirections);
            }
        });
        ImageButton deleteButton = view.findViewById(R.id.buttonDeleteList);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteList(userList, v);
            }
        });

        ImageButton copyButton = view.findViewById(R.id.buttonCopyList);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //copy list items
                String content = TextUtils.join(", ", userList.getItems());
                String toastMsg = "List items copied to Clipboard";
                Utils.CopyContentToClipBoard(v.getContext(), "Results",content, toastMsg);
            }
        });

        /*listNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userList.getExpanded() == false){
                    listView.setVisibility(View.VISIBLE);
                    userList.setExpanded(true);
                } else {
                    listView.setVisibility(View.GONE);
                    userList.setExpanded(false);
                }
            }
        });*/
        listInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userList.getExpanded() == false){
                    listView.setVisibility(View.VISIBLE);
                    userList.setExpanded(true);
                } else {
                    listView.setVisibility(View.GONE);
                    userList.setExpanded(false);
                }
            }
        });


        //Replacing listview with Expandable listview
        List<String> items = userList.getItems();
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        itemsAdapter.notifyDataSetChanged();
        //SetUserListExpandableView(exListView, userList);

        return view;
    }

    private void SetUserListExpandableView(ExpandableListView exListView, UserList userList) {
        HashMap<String, List<String>> userListAndItems = new HashMap<>();
        userListAndItems.put(userList.getListName(), userList.getItems());
        SingleUserExpandableListAdapter singleUserExpandableListAdapter= new SingleUserExpandableListAdapter(userListAndItems);
        exListView.setAdapter(singleUserExpandableListAdapter);
    }

    private void DeleteList(UserList userList, View v) {
        AlertDialog.Builder deleteConfirmDialogBuilder = new AlertDialog.Builder(context);
        deleteConfirmDialogBuilder.setTitle(context.getString(R.string.text_delete_list));
        deleteConfirmDialogBuilder.setMessage(context.getString(R.string.delete_confirmation_msg));
        deleteConfirmDialogBuilder.setPositiveButton(context.getString(R.string.but_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteList(userList.getListId());
                userLists.remove(userList);
                UserListsAdapter.this.notifyDataSetChanged();
                Snackbar snack = Snackbar.make(v, context.getString(R.string.text_list_deleted), Snackbar.LENGTH_SHORT);
                View view = snack.getView();
                TextView tv = view.findViewById(com.google.android.material.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.show();
            }
        });

        deleteConfirmDialogBuilder.setNegativeButton(context.getString(R.string.but_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog deleteDialog = deleteConfirmDialogBuilder.create();
        deleteDialog.show();
        //UserListsAdapter.this.notifyDataSetChanged();
    }
}
