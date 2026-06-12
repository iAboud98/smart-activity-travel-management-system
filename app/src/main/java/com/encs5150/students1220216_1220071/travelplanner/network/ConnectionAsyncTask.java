package com.encs5150.students1220216_1220071.travelplanner.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.encs5150.students1220216_1220071.travelplanner.activities.IntroductionActivity;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;

import java.util.List;

public class ConnectionAsyncTask extends AsyncTask<String, Void, List<Trip>> {

    // API URL
    public static final String API_URL = "https://mocki.io/v1/9febcb0b-b3f6-493a-8e78-714a28fa676e";

    Activity activity;

    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }

    // runs on background thread
    // gets raw JSON string using HttpManager then parses it
    @Override
    protected List<Trip> doInBackground(String... params) {
        String data = HttpManager.getData(API_URL);
        if (data == null) {
            Log.d("ConnectionAsyncTask", "fetch failed: no data returned from API");
            return null;
        }
        Log.d("ConnectionAsyncTask", "fetch successful, raw data length: " + data.length());
        return TripJsonParser.getTripsFromJson(data);
    }

    // runs on main UI thread after doInBackground finishes
    @Override
    protected void onPostExecute(List<Trip> trips) {
        if (trips == null || trips.isEmpty()) {
            Log.d("ConnectionAsyncTask", "import failed: trips list is null or empty");
            ((IntroductionActivity) activity).onTripsFetched(null);
        } else {
            Log.d("ConnectionAsyncTask", "import successful: " + trips.size() + " trips fetched");
            ((IntroductionActivity) activity).onTripsFetched(trips);
        }
    }
}