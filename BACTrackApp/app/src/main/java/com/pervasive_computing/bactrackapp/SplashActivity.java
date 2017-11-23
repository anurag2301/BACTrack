package com.pervasive_computing.bactrackapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

/*
  Created by Pratik on 11/06/2017.
 */

public class SplashActivity extends BaseActivity {

    private final String FIRST_RUN = "firstRun";
    private final String MY_PREF = "BACTrackPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(MY_PREF, Context.MODE_PRIVATE).getBoolean(FIRST_RUN, true)) {
            setContentView(R.layout.splash);
        } else {
            startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
            finish();
        }
    }

    public void get_started(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(FIRST_RUN, false);
        edit.apply();
        startActivity(new Intent(getApplicationContext(), WillYouDrink.class));
        finish();
    }

    public void skip_for_later(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }
}
