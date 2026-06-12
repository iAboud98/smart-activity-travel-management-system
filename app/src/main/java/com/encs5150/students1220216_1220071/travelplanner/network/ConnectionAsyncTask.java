package com.encs5150.students1220216_1220071.travelplanner.network;

import android.os.AsyncTask;

import com.encs5150.students1220216_1220071.travelplanner.models.Trip;

import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, Void, List<Trip>> {
    // API URL
    public static final String API_URL = "https://mocki.io/v1/9febcb0b-b3f6-493a-8e78-714a28fa676e";

    // interface to return result to the caller
    public interface OnTripsFetchedListener {
        void onSuccess(List<Trip> trips);
        void onFailure(String errorMessage);
    }

    private OnTripsFetchedListener listener;

    public ConnectionAsyncTask(OnTripsFetchedListener listener) {
        this.listener = listener;
    }

    // runs on background thread
    // gets raw JSON string using HttpManager then parses it
    @Override
    protected List<Trip> doInBackground(String... params) {
        // get raw JSON string from the API
        String data = HttpManager.getData(API_URL);
        if (data == null) {
            return null;
        }
        // parse the JSON string into a list of Trip objects
        return TripJsonParser.getTripsFromJson(data);
    }

    // runs on main UI thread after doInBackground finishes
    @Override
    protected void onPostExecute(List<Trip> trips) {
        if (trips == null || trips.isEmpty()) {
            listener.onFailure("Failed to fetch trips. Please check your connection.");
        } else {
            listener.onSuccess(trips);
        }
    }
}
