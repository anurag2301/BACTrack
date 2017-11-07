package com.pervasive_computing.bactrackapp;

/*
  Created by Pratik on 11/03/2017.
 */

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Util {
    static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }
}