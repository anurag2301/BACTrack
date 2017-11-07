package com.pervasive_computing.bactrackapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/*
  Created by Pratik on 11/05/2017.
 */

class CustomListAdapter extends ArrayAdapter<Contact> {

    CustomListAdapter(ArrayList<Contact> data, Context context) {
        super(context, R.layout.list_item, data);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Contact contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView name = convertView.findViewById(R.id.contact_name);
        TextView phone = convertView.findViewById(R.id.contact_phone);
        if (contact != null) {
            name.setText(contact.getName());
            phone.setText(contact.getPhone());
        }
        return convertView;
    }
}