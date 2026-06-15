package com.encs5150.students1220216_1220071.travelplanner.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// fetches data from the given URL and returns it as a JSON string
// returns null if the request fails
public class HttpManager {

    private static final int CONNECT_TIMEOUT_MS = 15000;
    private static final int READ_TIMEOUT_MS = 15000;

    public static String getData(String urlString) {
        BufferedReader bufferedReader = null;
        HttpURLConnection httpURLConnection = null;
        try {
            // creating URL object from the string
            URL url = new URL(urlString);
            // open connection to the URL
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            httpURLConnection.setReadTimeout(READ_TIMEOUT_MS);

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("HttpManager", "request failed with HTTP status: " + responseCode);
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = bufferedReader.readLine();
            }
            String data = stringBuilder.toString().trim();
            return data.isEmpty() ? null : data;
        } catch (Exception e) {
            Log.d("HttpManager", e.toString());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.d("HttpManager", e.toString());
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }
}
