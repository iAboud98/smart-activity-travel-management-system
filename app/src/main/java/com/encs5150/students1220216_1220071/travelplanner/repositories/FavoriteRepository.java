package com.encs5150.students1220216_1220071.travelplanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encs5150.students1220216_1220071.travelplanner.database.DatabaseHelper;
import com.encs5150.students1220216_1220071.travelplanner.models.Favorite;

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

}
