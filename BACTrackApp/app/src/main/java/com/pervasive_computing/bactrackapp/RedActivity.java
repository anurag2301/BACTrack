package com.pervasive_computing.bactrackapp;
/*
  Created by Pratik on 11/15/2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class RedActivity extends BaseActivity {

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
        Log.wtf("Send SMS", "");

        StringBuilder numbers = new StringBuilder();
        DBHelper db = new DBHelper(getApplicationContext());
        Set<String> allContacts = db.getAllContacts().keySet();
        for (String phone : allContacts) {
            numbers.append(phone).append(";");
        }
        /*Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:"+ Uri.encode(numbers.toString())));
        //smsIntent.setType("vnd.android-dir/mms-sms");
        //smsIntent.putExtra("address", numbers.toString());
        smsIntent.putExtra("sms_body", "Test " + bac);
        */
        if(allContacts.isEmpty()) {
            return;
        }
        try {
            /*startActivity(smsIntent);
            finish();*/
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(numbers.toString(),null,"Test"+bac,null,null);


            Log.wtf("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RedActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
}
