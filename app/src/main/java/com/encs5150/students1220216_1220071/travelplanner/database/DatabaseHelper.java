package com.encs5150.students1220216_1220071.travelplanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating users table
        db.execSQL("create table users (" +
                "id integer primary key autoincrement, " +
                "email text unique not null, " +  // unique to prevent duplicate accounts
                "first_name text not null, " +
                "last_name text not null, " +
                "password text not null, " +
                "gender text, " +
                "category text, " +
                "phone text, " +
                "profile_picture text, " +
                "role text not null default 'user'," +
                "is_active integer not null default 1 " + ")"); // 1 = active, 0 = deleted (data is kept but treated as deleted)

        // creating trips table
        db.execSQL("create table trips (" +
                "id integer primary key autoincrement, " +
                "api_id integer unique, " +   // unique to prevent importing the same trip twice
                "destination text not null, " +
                "country text not null, " +
                "duration_days integer, " +
                "price real, " +
                "rating real, " +
                "description text, " +
                "image_url text," +
                "is_active integer not null default 1" +")");  // 1 = active, 0 = deleted (data is kept but treated as deleted)

        // creating favorites table
        db.execSQL("create table favorites (" +
                "id integer primary key autoincrement, " +
                "user_id integer not null, " +
                "trip_id integer not null, " +
                "created_date text, " +
                "unique(user_id, trip_id)" +  ")");// unique to prevent duplicate favorites

        // creating reservations table
        db.execSQL("create table reservations (" +
                "id integer primary key autoincrement, " +
                "user_id integer not null, " +
                "trip_id integer not null, " +
                "quantity integer, " +
                "reservation_type text, " +
                "reservation_date text, " +
                "status text, " +
                "additional_info text" +
                ")");

        // pre creating admin account
        // password Admin123! is hashed with SHA-256
        String hashedPassword = hashPassword("Admin123!");
        db.execSQL("insert into users (email, first_name, last_name, password, gender, category, phone, profile_picture, role) " +
                "values ('admin@admin.com', 'Admin', 'Admin', '" + hashedPassword + "', '', '', '', '', 'admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // hashes a plain text password using SHA-256
    // returns the hashed string, or null if hashing fails
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // convert the password string to bytes and hash it
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();  // convert the hashed bytes to a hex string
            for (int i = 0; i < hashBytes.length; i++) {
                // convert each byte to a 2-character hex value
                String hex = Integer.toHexString(0xff & hashBytes[i]);
                // if hex is only 1 character add a leading zero
                if (hex.length() == 1) {
                    hexString.append('0'); }

                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
