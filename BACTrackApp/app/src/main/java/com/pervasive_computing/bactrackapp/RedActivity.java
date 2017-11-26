package com.pervasive_computing.bactrackapp;
/*
  Created by Pratik on 11/15/2017.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Set;

public class RedActivity extends BaseActivity {
    private static final String TAG = "RedActivity";
    private final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_screen);
        float bac_level = getIntent().getFloatExtra(getString(R.string.BAC_LEVEL), -1);
        TextView textView = findViewById(R.id.textView5);
        textView.setText(String.format("BAC: %s", bac_level));
        sendSMS(bac_level);
    }

    protected void sendSMS(float bac) {
        Log.wtf(TAG, "Send SMS");

        StringBuilder numbers = new StringBuilder();
        DBHelper db = new DBHelper(getApplicationContext());
        Set<String> allContacts = db.getAllContacts().keySet();
        for (String phone : allContacts) {
            numbers.append(phone).append(";");
        }
        if (allContacts.isEmpty()) {
            return;
        }
        String smsText = "My BAC level is " + bac;
        String location = getIntent().getStringExtra(KEY_LOCATION_UPDATES_RESULT);
        if(location!=null && !location.equals("")) {
            smsText += " and my location is " + location;
        }
        smsText += ".";
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(numbers.toString(), null, smsText, null, null);
        Log.wtf(TAG, "Finished sending SMS...");
    }
}
