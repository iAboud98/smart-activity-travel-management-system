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
        if (json == null || json.trim().isEmpty()) {
            return trips;
        }

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                Object item = jsonArray.opt(i);
                if (!(item instanceof JSONObject)) {
                    continue;
                }
                JSONObject jsonObject = (JSONObject) item;

                if (!jsonObject.has("id") || !jsonObject.has("destination") ||
                        !jsonObject.has("country") || !jsonObject.has("duration_days") ||
                        !jsonObject.has("price") || !jsonObject.has("rating") ||
                        !jsonObject.has("description") || !jsonObject.has("image")) {
                    // skip this trip object if any required field is missing
                    continue;
                }

                try {
                    int apiId = jsonObject.getInt("id");
                    String destination = jsonObject.getString("destination").trim();
                    String country = jsonObject.getString("country").trim();
                    int durationDays = jsonObject.getInt("duration_days");
                    double price = jsonObject.getDouble("price");
                    double rating = jsonObject.getDouble("rating");
                    String description = jsonObject.getString("description").trim();
                    String imageUrl = jsonObject.getString("image").trim();

                    if (apiId < 0 || destination.isEmpty() || country.isEmpty()
                            || durationDays < 1 || price <= 0 || rating < 0 || rating > 5
                            || description.isEmpty() || imageUrl.isEmpty()) {
                        continue;
                    }

                    Trip trip = new Trip();
                    trip.setApiID(apiId);
                    trip.setDestination(destination);
                    trip.setCountry(country);
                    trip.setDurationDays(durationDays);
                    trip.setPrice(price);
                    trip.setRating(rating);
                    trip.setDescription(description);
                    trip.setImageUrl(imageUrl);
                    trip.setIsActive(1);
                    trips.add(trip);
                } catch (Exception ignored) {
                    // Skip only the malformed item so later valid trips can still be imported.
                }
            }
        } catch (Exception ignored) {
            // Invalid top-level JSON produces an empty, safely handled result.
        }

        return trips;
    }
}
