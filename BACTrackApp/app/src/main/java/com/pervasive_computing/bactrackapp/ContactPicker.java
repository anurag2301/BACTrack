package com.pervasive_computing.bactrackapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
                DBHelper dbInstance = new DBHelper(getApplicationContext());
                dbInstance.insertContact(name, number);
            }
        }
        finish();
    }
}

