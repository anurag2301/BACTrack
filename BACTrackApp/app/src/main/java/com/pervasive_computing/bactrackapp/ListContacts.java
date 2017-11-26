package com.pervasive_computing.bactrackapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/*
  Created by Pratik on 11/05/2017.
 */

public class ListContacts extends BaseActivity {
    private ArrayList<Contact> contacts;
    private DBHelper dbInstance;
    private CustomListAdapter listAdapter;
    private TextView mTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    @SuppressLint("ResourceAsColor")
    private void populateList() {
        setContentView(R.layout.contact_list);
        contacts = new ArrayList<>();
        dbInstance = new DBHelper(getApplicationContext());
        HashMap<String, String> contactsMap = dbInstance.getAllContacts();

        for (String phone : contactsMap.keySet())
            contacts.add(new Contact(contactsMap.get(phone), phone));
        ListView listView = findViewById(R.id.listView);
        mTV = findViewById(R.id.contactListTV);

        if (contactsMap.isEmpty()) {
            mTV.setVisibility(View.VISIBLE);
        } else {
            mTV.setVisibility(View.GONE);
        }

        listView.setBackgroundColor(Color.WHITE);
        listView.setDrawingCacheBackgroundColor(Color.WHITE);
        listAdapter = new CustomListAdapter(contacts, getApplicationContext());
        listView.setAdapter(listAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                final Contact contact = contacts.get(pos);
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ListContacts.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ListContacts.this);
                }
                builder.setTitle("Delete entry")
                        .setMessage("Do you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbInstance.deleteContact(contact.getPhone());
                                dbInstance.close();
                                contacts.remove(contact);
                                listAdapter.remove(contact);
                                if (contacts == null || contacts.isEmpty()) {
                                    mTV.setVisibility(View.VISIBLE);
                                } else {
                                    mTV.setVisibility(View.GONE);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });
    }
}
