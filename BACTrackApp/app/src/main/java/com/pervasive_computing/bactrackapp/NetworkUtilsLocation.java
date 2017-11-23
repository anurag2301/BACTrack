package com.pervasive_computing.bactrackapp;

/**
 * Created by anura on 11/21/2017.
 */

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtilsLocation {

    final static String GITHUB_BASE_URL =
            "https://maps.googleapis.com/maps/api/place/radarsearch/json";

    final static String PARAM_LOCATION= "location";

    /*
     * The sort field. One of stars, forks, or updated.
     * Default: results are sorted by best match if no field is specified.
     */
    final static String PARAM_RADIUS = "radius";
    final static String PARAM_TYPE = "type";
    final static String PARAM_KEY = "key";
    final static String GOOGLE_API_KEY = "AIzaSyA4E6jz5n97DZKzG7u3V-AEeachsFpIQKQ";


    /**
     * Builds the URL used to query GitHub.
     *
     * @param githubSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the GitHub.
     */
    public static URL buildUrl(String githubSearchQuery) {
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_LOCATION, githubSearchQuery)
                .appendQueryParameter(PARAM_RADIUS, "15")
                .appendQueryParameter(PARAM_TYPE, "bar")
                .appendQueryParameter(PARAM_KEY, GOOGLE_API_KEY)
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
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
