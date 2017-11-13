package com.pervasive_computing.bactrackapp;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

/*
  Created by Pratik on 11/10/2017.
 */

public class BreatheActivity extends BaseActivity {
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathe);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setSecondaryProgress(progressBar.getMax());
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < progressBar.getMax()) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
