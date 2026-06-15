package com.encs5150.students1220216_1220071.travelplanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.encs5150.students1220216_1220071.travelplanner.database.DatabaseHelper;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context, "travel_planner.db", null, 1);
    }

    // register a new user
    public boolean registerUser(User user) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("email", user.getEmail());
            values.put("first_name", user.getFirstName());
            values.put("last_name", user.getLastName());
            values.put("password", DatabaseHelper.hashPassword(user.getPassword()));
            values.put("gender", user.getGender());
            values.put("category", user.getCategory());
            values.put("phone", user.getPhone());
            values.put("profile_picture", user.getProfilePicturePath());
            values.put("role", "user");
            values.put("is_active", 1);
            long result = db.insert("users", null, values);  // result = -1 if it fails
            // returns true if successful and false if email already exists
            return result != -1;  // converting result to boolean value
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // find a user by email, returns the user object or null if not found
    public User findUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email = '" + email + "' and is_active = 1", null);
        if (cursor.moveToFirst()) {
            User user = cursorToUser(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    // authenticate login for both users and admins
    // returns the user object if email/password match, null if not
    public User loginUser(String email, String password) {
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String hashedPassword = DatabaseHelper.hashPassword(password);
            Cursor cursor = db.rawQuery("select * from users where email = '" + email + "' and password = '" + hashedPassword + "' and is_active = 1", null);
            if (cursor.moveToFirst()) {
                User user = cursorToUser(cursor);
                cursor.close();
                return user;
            }
            cursor.close();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // update first name for a user, return true if successful
    public boolean updateFirstName(int userId, String firstName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        int rows = db.update("users", values, "id = " + userId, null); // returns number of updated rows
        return rows > 0;
    }

    // update last name for a user, return true if successful
    public boolean updateLastName(int userId, String lastName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("last_name", lastName);
        int rows = db.update("users", values, "id = " + userId, null);
        return rows > 0;
    }

    // update password for a user, returns true if successful
    public boolean updatePassword(int userId, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", DatabaseHelper.hashPassword(newPassword));
        int rows = db.update("users", values, "id = " + userId, null);
        return rows > 0;
    }

    // update phone number for a user, returns true if successful
    public boolean updatePhone(int userId, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        int rows = db.update("users", values, "id = " + userId, null);
        return rows > 0;
    }

    // update profile picture path for a user, returns true if successful
    public boolean updateProfilePicture(int userId, String picturePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("profile_picture", picturePath);
        int rows = db.update("users", values, "id = " + userId, null);
        return rows > 0;
    }

    // soft delete a user by setting is_active to 0
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_active", 0);
        int rows = db.update("users", values, "id = " + userId, null);
        return rows > 0;
    }

    // list all active users for admin view
    public List<User> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from users where role = 'user' and is_active = 1", null);
        while (cursor.moveToNext()) {
            users.add(cursorToUser(cursor)); }
        cursor.close();
        return users;
    }

    // method to convert a cursor row to a user object
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setEmail(cursor.getString(1));
        user.setFirstName(cursor.getString(2));
        user.setLastName(cursor.getString(3));
        user.setPassword(cursor.getString(4));
        user.setGender(cursor.getString(5));
        user.setCategory(cursor.getString(6));
        user.setPhone(cursor.getString(7));
        user.setProfilePicturePath(cursor.getString(8));
        user.setRole(cursor.getString(9));
        user.setIsActive(cursor.getInt(10));
        return user;
    }
}
