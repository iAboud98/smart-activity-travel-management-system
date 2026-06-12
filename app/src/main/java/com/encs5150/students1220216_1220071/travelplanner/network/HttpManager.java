package com.encs5150.students1220216_1220071.travelplanner.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// fetches data from the given URL and returns it as a JSON string
// returns null if the request fails
public class HttpManager {

    public static String getData(String urlString) {
        BufferedReader bufferedReader = null;
        try {
            // creating URL object from the string
            URL url = new URL(urlString);
            // open connection to the URL
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line + '\n');
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            Log.d("HttpManager", e.toString());
        }
        return null;
    }
}
