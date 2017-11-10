package com.pervasive_computing.bactrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
  Created by Pratik on 11/07/2017.
 */

public class WillYouDrink extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.will_you_drink);
    }

    public void will_drink(View view) {
        startActivity(new Intent(getApplicationContext(), HowGoingHome.class));
        finish();
    }

    public void maybe_will_drink(View view) {
        startActivity(new Intent(getApplicationContext(), HowGoingHome.class));
        finish();
    }

    public void will_not_drink(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }
}
