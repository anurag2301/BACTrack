package com.pervasive_computing.bactrackapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by anura on 11/28/2017.
 */

public class WaitActivity extends BaseActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.analyze_screen);
        //TextView title = findViewById(R.id.wait_title);
        //title.setText("Analyzing Result");
    }
}
