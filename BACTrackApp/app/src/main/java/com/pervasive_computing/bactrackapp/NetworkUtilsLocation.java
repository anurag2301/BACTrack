package com.pervasive_computing.bactrackapp;

/*
  Created by Anurag on 11/21/2017.
 */

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

class NetworkUtilsLocation {

    private final static String GITHUB_BASE_URL =
            "https://maps.googleapis.com/maps/api/place/radarsearch/json";

    private final static String PARAM_LOCATION = "location";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    private final static String PARAM_RADIUS = "radius";
    private final static String RADIUS_VALUE = "70";
    private final static String TYPE_VALUE = "bar";
    private final static String PARAM_TYPE = "type";
    private final static String PARAM_KEY = "key";
    private final static String API_KEY_VALUE = "GOOGLE_API_KEY";


    /**
     * Builds the URL used to query GitHub.
     *
     * @param githubSearchQuery The keyword that will be queried for.
     * @param mContext          Context of Application
     * @return The URL to use to query the GitHub.
     */
    static URL buildUrl(String githubSearchQuery, Context mContext) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_LOCATION, githubSearchQuery)
                .appendQueryParameter(PARAM_RADIUS, RADIUS_VALUE)
                .appendQueryParameter(PARAM_TYPE, TYPE_VALUE)
                .appendQueryParameter(PARAM_KEY, Util.getProperty(API_KEY_VALUE, mContext))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
