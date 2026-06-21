package com.encs5150.students1220216_1220071.travelplanner.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.encs5150.students1220216_1220071.travelplanner.database.DatabaseHelper;
import com.encs5150.students1220216_1220071.travelplanner.models.User;
import com.encs5150.students1220216_1220071.travelplanner.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final DatabaseHelper dbHelper;
    private boolean lastLoginOperationFailed;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context, "travel_planner.db", null, 1);
    }

    // Public registration must never be able to create an administrator.
    public boolean registerUser(User user) {
        return registerAccount(user, SessionManager.ROLE_USER);
    }

    // Called only from the protected admin flow.
    public boolean registerAdmin(User admin) {
        return registerAccount(admin, SessionManager.ROLE_ADMIN);
    }

    private boolean registerAccount(User user, String role) {
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
            values.put("role", role);
            values.put("is_active", 1);

            // A deleted account keeps its row so reservations and favorites remain linked.
            // Reusing that email restores the same row with the new account details and role.
            int restoredRows = db.update(
                    "users",
                    values,
                    "email = ? and is_active = 0",
                    new String[]{user.getEmail()}
            );
            if (restoredRows > 0) {
                return true;
            }

            // Active duplicate emails still fail through the unique email constraint.
            return db.insert("users", null, values) != -1;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Inactive accounts can be restored, so only active emails block registration.
    public boolean emailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                new String[]{"id"},
                "email = ? and is_active = 1",
                new String[]{email},
                null,
                null,
                null
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // find a user by email, returns the user object or null if not found
    public User findUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "users",
                null,
                "email = ? and is_active = 1",
                new String[]{email},
                null,
                null,
                null
        );
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
        lastLoginOperationFailed = false;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String hashedPassword = DatabaseHelper.hashPassword(password);
            Cursor cursor = db.query(
                    "users",
                    null,
                    "email = ? and password = ? and is_active = 1",
                    new String[]{email, hashedPassword},
                    null,
                    null,
                    null
            );
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
            lastLoginOperationFailed = true;
            return null;
        }
    }

    public boolean didLastLoginOperationFail() {
        return lastLoginOperationFailed;
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

    // count active admin accounts, used to prevent deleting the last admin
    public int countAdmins() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from users where role = 'admin' and is_active = 1", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // search users by name, email or phone
    public List<User> searchUsers(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from users where role = 'user' and is_active = 1 " +
                        "and (email like '%" + query + "%' " +
                        "or first_name like '%" + query + "%' " +
                        "or last_name like '%" + query + "%' " +
                        "or phone like '%" + query + "%')", null);
        while (cursor.moveToNext()) {
            users.add(cursorToUser(cursor));
        }
        cursor.close();
        return users;
    }
}
