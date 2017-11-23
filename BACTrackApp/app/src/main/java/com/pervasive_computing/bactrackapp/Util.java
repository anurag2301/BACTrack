package com.pervasive_computing.bactrackapp;

/*
  Created by Pratik on 11/03/2017.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

class Util {
    private static final String TAG = "Util";

    static String getProperty(String key, Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {
            properties.load(assetManager.open("config.properties"));
        } catch (IOException e) {
            Log.e(TAG, "Error reading config");
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }
}