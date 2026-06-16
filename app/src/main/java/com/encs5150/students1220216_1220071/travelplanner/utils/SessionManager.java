package com.encs5150.students1220216_1220071.travelplanner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.encs5150.students1220216_1220071.travelplanner.models.User;

public final class SessionManager {
    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";

    private static final String SESSION_PREFS_NAME = "travel_planner_session";
    private static final String KEY_USER_ID = "current_user_id";
    private static final String KEY_EMAIL = "current_user_email";
    private static final String KEY_ROLE = "current_user_role";
    private static final int NO_USER_ID = -1;

    private SessionManager() {
    }

    public static void saveSession(Context context, User user) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_ROLE, user.getRole());
        editor.apply();
    }

    public static boolean hasActiveSession(Context context) {
        return getCurrentUserId(context) != NO_USER_ID
                && !ValidationUtils.isBlank(getCurrentUserEmail(context))
                && !ValidationUtils.isBlank(getCurrentUserRole(context));
    }

    public static int getCurrentUserId(Context context) {
        return getPreferences(context).getInt(KEY_USER_ID, NO_USER_ID);
    }

    public static String getCurrentUserEmail(Context context) {
        return getPreferences(context).getString(KEY_EMAIL, "");
    }

    public static String getCurrentUserRole(Context context) {
        return getPreferences(context).getString(KEY_ROLE, "");
    }

    public static boolean isAdmin(Context context) {
        return ROLE_ADMIN.equalsIgnoreCase(getCurrentUserRole(context));
    }

    public static boolean isUser(Context context) {
        return ROLE_USER.equalsIgnoreCase(getCurrentUserRole(context));
    }

    public static void clearSession(Context context) {
        getPreferences(context).edit().clear().apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(SESSION_PREFS_NAME, Context.MODE_PRIVATE);
    }
}
