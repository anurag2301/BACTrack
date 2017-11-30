package com.pervasive_computing.bactrackapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

/*
  Created by Pratik on 11/03/2017.
 */

public class ContactPicker extends BaseActivity {

    private static final int CONTACT_PICKER_RESULT = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);
        askPermissions(1);
        if (isAllowed(Manifest.permission.SEND_SMS)) {
            startContactIntent();
        } else {
            finish();
        }
    }

    private void startContactIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, CONTACT_PICKER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_PICKER_RESULT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = null;
                if (contactData != null) {
                    cursor = getContentResolver().query(contactData, null, null, null, null);
                }
                String number = "XXX";
                String name = "XXX";
                if (cursor != null) {
                    cursor.moveToFirst();
                    number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    cursor.close();
                }
                addToDb(ContactPicker.this, name, number);
            }
        }
        //finish();
    }

    private void addToDb(final Context context, final String name, final String number) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Add Contact").setCancelable(false)
                .setMessage("Are you sure you would like to add: " + name + " to your reliable contact list?\n" +
                        "\n" +
                        "This person will receive notifications about your BAC level and evening plans in the future.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbInstance = new DBHelper(getApplicationContext());
                        dbInstance.insertContact(name, number);
                        finish();
                        startActivity(new Intent(context, ListContacts.class));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                        finish();
                        startActivity(new Intent(context, ListContacts.class));
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

