package com.pervasive_computing.bactrackapp;
/*
  Created by Pratik on 11/15/2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class RedActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_screen);
    }

    protected void sendSMS(String bac) {
        Log.wtf("Send SMS", "");

        StringBuilder numbers = new StringBuilder();
        DBHelper db = new DBHelper(getApplicationContext());
        for (String phone : db.getAllContacts().keySet()) {
            numbers.append(phone).append(";");
        }
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", numbers.toString());
        smsIntent.putExtra("sms_body", "Test " + bac);

        try {
            startActivity(smsIntent);
            //finish();
            Log.wtf("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RedActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
