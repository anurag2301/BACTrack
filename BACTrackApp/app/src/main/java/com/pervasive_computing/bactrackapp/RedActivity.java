package com.pervasive_computing.bactrackapp;
/*
  Created by Pratik on 11/15/2017.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
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
        TextView textView = findViewById(R.id.bac_level);
        textView.setText(String.format("%s", bac_level));

        if (isAllowed(Manifest.permission.SEND_SMS)) {
            sendSMS(bac_level);
        }
    }

    private void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You said you will be driving home tonight. Your BAC level is currently over the legal limit. Your reliable contact list has been notified about your current BAC level.")
                .setCancelable(false).setTitle("Notifying your contacts")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void sendSMS(float bac) {
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
        showMessage();
        StringBuilder smsText = new StringBuilder();
        smsText.append("My BAC level is ").append(bac);
        String location = getIntent().getStringExtra(KEY_LOCATION_UPDATES_RESULT);
        if (location != null && !location.equals("") && LocationRequestHelper.getRequesting(this)) {
            smsText.append(" and my location is https://www.google.com/maps/search/?api=1&query=").append(location);
        }
        smsText.append(".");
        SmsManager.getDefault().sendTextMessage(numbers.toString(), null, smsText.toString(), null, null);
        Log.wtf(TAG, "Finished sending SMS...");
    }

    public void try_again_clocked(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
    }
}
