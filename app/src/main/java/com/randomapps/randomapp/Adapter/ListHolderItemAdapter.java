package com.randomapps.randomapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.randomapps.randomapp.R;

import java.util.List;

import DB.DBHelper;

public class ListHolderItemAdapter extends ArrayAdapter<String> {
    Context context;
    int layoutResource;
    List<String> items;
    //View curView;
    ListView listView;
    DBHelper dbHelper;

    public ListHolderItemAdapter(Context context, int layoutResource, List<String> items){
        super(context, layoutResource, items);
        this.context = context;
        this.layoutResource = layoutResource;
        this.items = items;
        listView = listView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutResource, null);
        dbHelper = new DBHelper(context);
        String item = items.get(position);

        TextView itemTextView = view.findViewById(R.id.itemTextView);
        ImageButton removeButton = view.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(position);
                ListHolderItemAdapter.this.notifyDataSetChanged();
            }
        });

        itemTextView.setText(item);
        return view;
    }
}
