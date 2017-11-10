package com.pervasive_computing.bactrackapp;

/*
  Created by Pratik on 11/09/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class FirstPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }

    public void checkBAC(View view) {
        startActivity(new Intent(getApplicationContext(), BlowActivity.class));
        finish();
    }
}
