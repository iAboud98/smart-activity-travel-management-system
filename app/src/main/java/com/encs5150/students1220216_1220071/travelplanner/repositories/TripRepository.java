package com.encs5150.students1220216_1220071.travelplanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encs5150.students1220216_1220071.travelplanner.database.DatabaseHelper;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripRepository {

    private DatabaseHelper dbHelper;

    public TripRepository(Context context) {
        dbHelper = new DatabaseHelper(context, "travel_planner.db", null, 1);
    }

    // insert or refresh a trip imported from the API
    public boolean insertTrip(Trip trip) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = createTripValues(trip);
        values.put("api_id", trip.getApiID());
        values.put("is_active", 1);
        long result = db.insertWithOnConflict("trips", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (result == -1) {
            return updateTripByApiId(trip);
        }
        return true;
    }

    // create a local trip added by an admin; local trips do not have an API id
    public boolean createTrip(Trip trip) {
        if (trip == null || activeDestinationExists(trip.getDestination(), -1)) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = createTripValues(trip);
        values.putNull("api_id");
        values.put("is_active", 1);
        return db.insert("trips", null, values) != -1;
    }

    // update an existing trip (for admin)
    public boolean updateTrip(Trip trip) {
        if (trip == null || activeDestinationExists(trip.getDestination(), trip.getID())) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = createTripValues(trip);
        int rows = db.update(
                "trips",
                values,
                "id = ?",
                new String[]{String.valueOf(trip.getID())}
        );
        return rows > 0;
    }

    // active destinations must be unique for trips managed by an admin
    public boolean activeDestinationExists(String destination, int excludedTripId) {
        if (destination == null || destination.trim().isEmpty()) {
            return false;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select id from trips " +
                "where is_active = 1 and trim(destination) = ? collate nocase";
        List<String> args = new ArrayList<>();
        args.add(destination.trim());
        if (excludedTripId >= 0) {
            sql += " and id <> ?";
            args.add(String.valueOf(excludedTripId));
        }

        Cursor cursor = db.rawQuery(sql, args.toArray(new String[0]));
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    private ContentValues createTripValues(Trip trip) {
        ContentValues values = new ContentValues();
        values.put("destination", trip.getDestination().trim());
        values.put("country", trip.getCountry());
        values.put("duration_days", trip.getDurationDays());
        values.put("price", trip.getPrice());
        values.put("rating", trip.getRating());
        values.put("description", trip.getDescription());
        values.put("image_url", trip.getImageUrl());
        return values;
    }

    private boolean updateTripByApiId(Trip trip) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("destination", trip.getDestination());
        values.put("country", trip.getCountry());
        values.put("duration_days", trip.getDurationDays());
        values.put("price", trip.getPrice());
        values.put("rating", trip.getRating());
        values.put("description", trip.getDescription());
        values.put("image_url", trip.getImageUrl());
        values.put("is_active", 1);
        int rows = db.update("trips", values, "api_id = ?", new String[]{String.valueOf(trip.getApiID())});
        return rows > 0;
    }

    // soft delete a trip by setting is_active to 0 (for admin)
    public boolean deleteTrip(int tripId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_active", 0);
        int rows = db.update("trips", values, "id = " + tripId, null);
        return rows > 0;
    }

    // list all active trips
    public List<Trip> getAllTrips() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from trips where is_active = 1", null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // get a single trip by its database id
    public Trip getTripById(int tripId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from trips where id = ? and is_active = 1",
                new String[]{String.valueOf(tripId)}
        );
        if (cursor.moveToFirst()) {
            Trip trip = cursorToTrip(cursor);
            cursor.close();
            return trip;
        }
        cursor.close();
        return null;
    }

    // search trips by destination/country/description
    public List<Trip> searchTrips(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        String searchPattern = "%" + (query == null ? "" : query.trim()) + "%";
        Cursor cursor = db.rawQuery("select * from trips where is_active = 1 and " +
                        "(destination like ? or country like ? or description like ?)",
                new String[]{searchPattern, searchPattern, searchPattern});
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // filter trips by duration days range
    public List<Trip> filterByDuration(int minDays, int maxDays) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from trips where duration_days >= " + minDays + " and duration_days <= " + maxDays + " and is_active = 1", null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // filter trips by minimum rating
    public List<Trip> filterByRating(double minRating) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from trips where rating >= " + minRating + " and is_active = 1", null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // filter trips by maximum price
    public List<Trip> filterByPrice(double maxPrice) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from trips where price <= " + maxPrice + " and is_active = 1", null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // search with optional combined filters
    // each category filter is independent 0 means no filter for that category
    public List<Trip> searchWithFilter(String searchQuery, int durationMin, int durationMax, double maxPrice, double minRating) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();

        String sql = "select * from trips where is_active = 1";

        // search condition across destination, country and description
        if (searchQuery != null && !searchQuery.isEmpty()) {
            sql += " and (destination like '%" + searchQuery + "%' or country like '%" + searchQuery + "%' or description like '%" + searchQuery + "%')";
        }

        // duration filter if not 0
        if (durationMin > 0 && durationMax > 0 && durationMax < 100) {
            sql += " and duration_days between " + durationMin + " and " + durationMax;
        } else if (durationMin >= 8) {
            // 8+ days case
            sql += " and duration_days >= " + durationMin;
        }

        // price filter if not 0
        if (maxPrice > 0) {
            sql += " and price <= " + maxPrice;
        }

        // rating filter if not 0
        if (minRating > 0) {
            sql += " and rating >= " + minRating;
        }

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // special section
    // list popular trips based on number of reservations (5 or more reservations)
    public List<Trip> getPopularTrips() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select trips.* from trips " +
                        "join reservations on trips.id = reservations.trip_id " +
                        "where trips.is_active = 1 " +
                        "group by trips.id " +
                        "having count(reservations.id) >= 5 " +
                        "order by count(reservations.id) desc", null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // list trending trips that received 3 or more reservations in the last 7 days
    public List<Trip> getTrendingTrips() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select trips.* from trips " +
                        "join reservations on trips.id = reservations.trip_id " +
                        "where trips.is_active = 1 " +
                        "and reservations.reservation_date >= date('now', '-7 days') " +
                        "group by trips.id " +
                        "having count(reservations.id) >= 3 " +
                        "order by count(reservations.id) desc", null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // list special recommended trips (trips with rating >= 4.5)
    public List<Trip> getTopRatedTrips() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from trips where rating >= 4.5 and is_active = 1", null);
        while (cursor.moveToNext()) {
            trips.add(cursorToTrip(cursor));
        }
        cursor.close();
        return trips;
    }

    // helper method to convert a cursor row to a Trip object
    private Trip cursorToTrip(Cursor cursor) {
        Trip trip = new Trip();
        trip.setID(cursor.getInt(0));
        trip.setApiID(cursor.getInt(1));
        trip.setDestination(cursor.getString(2));
        trip.setCountry(cursor.getString(3));
        trip.setDurationDays(cursor.getInt(4));
        trip.setPrice(cursor.getDouble(5));
        trip.setRating(cursor.getDouble(6));
        trip.setDescription(cursor.getString(7));
        trip.setImageUrl(cursor.getString(8));
        trip.setIsActive(cursor.getInt(9));
        return trip;
    }

    // imports a list of trips from the API into the database
    // inserts or refreshes each trip by api_id and returns the successful count
    public int importTrips(List<Trip> trips) {
        int insertedCount = 0;
        for (int i = 0; i < trips.size(); i++) {
            boolean inserted = insertTrip(trips.get(i));
            if (inserted) {
                insertedCount++;
            }
        }
        return insertedCount;
    }
}
