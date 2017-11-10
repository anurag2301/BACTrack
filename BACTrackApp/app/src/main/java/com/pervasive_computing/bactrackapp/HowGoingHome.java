package com.pervasive_computing.bactrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
  Created by Pratik on 11/07/2017.
 */

public class HowGoingHome extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_going_home);
    }

    public void other_clicked(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }

    public void taxi_clicked(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }

    public void bus_clicked(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }

    public void friends_clicked(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }

    public void car_clicked(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }

    public void walk_clicked(View view) {
        startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
        finish();
    }
}
