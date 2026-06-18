package com.encs5150.students1220216_1220071.travelplanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encs5150.students1220216_1220071.travelplanner.database.DatabaseHelper;
import com.encs5150.students1220216_1220071.travelplanner.models.Favorite;
import com.encs5150.students1220216_1220071.travelplanner.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRepository {

    private DatabaseHelper dbHelper;

    public FavoriteRepository(Context context) {
        dbHelper = new DatabaseHelper(context, "travel_planner.db", null, 1);
    }

    // add a trip to favorites for a user
    // returns false if already favorited (duplicate prevented by unique constraint)
    public boolean addFavorite(int userId, int tripId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("trip_id", tripId);
        long result = db.insert("favorites", null, values);
        // insert returns -1 if duplicate
        return result != -1;
    }

    // remove a trip from favorites for a user
    public boolean removeFavorite(int userId, int tripId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("favorites", "user_id = " + userId + " and trip_id = " + tripId, null);
        return rows > 0;
    }

    // check if a trip is already in favorites for a user
    public boolean isFavorite(int userId, int tripId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from favorites where user_id = " + userId + " and trip_id = " + tripId, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // list all favorites for a specific user
    public List<Favorite> getFavoritesByUser(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Favorite> favorites = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from favorites where user_id = " + userId, null);
        while (cursor.moveToNext()) {
            Favorite favorite = new Favorite();
            favorite.setId(cursor.getInt(0));
            favorite.setUserId(cursor.getInt(1));
            favorite.setTripId(cursor.getInt(2));
            favorites.add(favorite);
        }
        cursor.close();
        return favorites;
    }

    // get all favorited trips for a user as Trip objects
    public List<Trip> getFavoriteTripsByUser(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Trip> trips = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select trips.* from trips " +
                        "join favorites on trips.id = favorites.trip_id " +
                        "where favorites.user_id = " + userId +
                        " and trips.is_active = 1", null);
        while (cursor.moveToNext()) {
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
            trips.add(trip);
        }
        cursor.close();
        return trips;
    }

}
