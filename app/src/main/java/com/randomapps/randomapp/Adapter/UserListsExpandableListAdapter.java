package com.randomapps.randomapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.randomapps.randomapp.R;

import java.util.HashMap;
import java.util.List;

public class UserListsExpandableListAdapter extends BaseExpandableListAdapter {

    HashMap<String, List<String>> listHashMap;
    String[] listNames;

    public UserListsExpandableListAdapter(HashMap<String, List<String>> listHashMap) {
        this.listHashMap = listHashMap;
        this.listNames = listHashMap.keySet().toArray(new String[0]);
    }

    @Override
    public int getGroupCount() {
        return listNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listNames[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listNames[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandible_list_group, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.listNameTextView);
        textView.setText(String.valueOf(getGroup(groupPosition)));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.itemTextView);
        textView.setText(String.valueOf(getChild(groupPosition, childPosition)));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
