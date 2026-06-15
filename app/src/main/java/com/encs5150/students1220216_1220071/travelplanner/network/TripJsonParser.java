package com.encs5150.students1220216_1220071.travelplanner.network;

import com.encs5150.students1220216_1220071.travelplanner.models.Trip;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

// parses a JSON string into a list of trip objects
// returns an empty list if parsing fails or JSON is invalid
public class TripJsonParser {

    public static List<Trip> getTripsFromJson(String json) {
        List<Trip> trips = new ArrayList<>();
        try {
            // parse the JSON string into a JSON array
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                // get each trip object from the array
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);

                // validate that required fields exist before creating a trip object
                if (!jsonObject.has("id") || !jsonObject.has("destination") ||
                        !jsonObject.has("country") || !jsonObject.has("duration_days") ||
                        !jsonObject.has("price") || !jsonObject.has("rating") ||
                        !jsonObject.has("description") || !jsonObject.has("image")) {
                    // skip this trip object if any required field is missing
                    continue;
                }

                // create a trip object and fill it with data from the JSON object
                Trip trip = new Trip();
                trip.setApiID(jsonObject.getInt("id"));
                trip.setDestination(jsonObject.getString("destination"));
                trip.setCountry(jsonObject.getString("country"));
                trip.setDurationDays(jsonObject.getInt("duration_days"));
                trip.setPrice(jsonObject.getDouble("price"));
                trip.setRating(jsonObject.getDouble("rating"));
                trip.setDescription(jsonObject.getString("description"));
                trip.setImageUrl(jsonObject.getString("image"));
                trip.setIsActive(1);

                trips.add(trip);
            }

        } catch (Exception e) {
            // return empty list if JSON is invalid or parsing fails
            e.printStackTrace();
        }

        return trips;
    }
}
