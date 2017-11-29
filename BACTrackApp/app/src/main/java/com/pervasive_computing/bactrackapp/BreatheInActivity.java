package com.pervasive_computing.bactrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
  Created by Pratik on 11/10/2017.
 */

public class BreatheInActivity extends BaseActivity {
    private static int MAX_PROGRESS;
    private ProgressBar progressBar;
    private int progressStatus;
    private Handler handler = new Handler();
    private TextView title;
    private TextView body, done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathe);
        progressBar = findViewById(R.id.progressBar);
        MAX_PROGRESS = progressBar.getMax();
        title = findViewById(R.id.breatheTitle);
        body = findViewById(R.id.breatheBody);
        done = findViewById(R.id.breatheDone);
        processThread();

    }

    private void processThread() {
        final String titleTxt, bodyTxt;
        titleTxt = "Take Deep Breaths";
        bodyTxt = "Continue breathing deeply for the next few seconds";
        progressStatus = 0;
        progressBar.setSecondaryProgress(MAX_PROGRESS);
        handler.post(new Runnable() {
            public void run() {
                title.setText(titleTxt);
                done.setText("");
                body.setText(bodyTxt);
            }
        });

        Thread t = new Thread(new Runnable() {
            public void run() {
                while (progressStatus < MAX_PROGRESS) {
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            if (progressStatus + 10 >= MAX_PROGRESS) {
                                done.setText(R.string.DONE);
                                startActivity(new Intent(getApplicationContext(), BlowActivity.class));
                            }
                        }
                    });
                    progressStatus += 10;
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

}
