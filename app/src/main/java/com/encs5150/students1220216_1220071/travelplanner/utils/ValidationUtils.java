package com.encs5150.students1220216_1220071.travelplanner.utils;

import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");

    private ValidationUtils() {
    }

    public static String cleanInput(String value) {
        return value == null ? "" : value.trim();
    }

    public static boolean isBlank(String value) {
        return cleanInput(value).isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(cleanInput(email)).matches();
    }

    public static boolean isValidName(String name) {
        return cleanInput(name).length() >= MIN_NAME_LENGTH;
    }

    public static boolean hasMinimumPasswordLength(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean hasPasswordLetter(String password) {
        return password != null && password.matches(".*[A-Za-z].*");
    }

    public static boolean hasPasswordNumber(String password) {
        return password != null && password.matches(".*[0-9].*");
    }

    public static boolean isValidPassword(String password) {
        return hasMinimumPasswordLength(password)
                && hasPasswordLetter(password)
                && hasPasswordNumber(password);
    }

    public static boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    public static boolean isValidSpinnerSelection(int selectedPosition) {
        return selectedPosition > 0;
    }

    public static boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(normalizePhone(phone)).matches();
    }

    public static String normalizePhone(String phone) {
        return cleanInput(phone).replaceAll("[\\s()-]", "");
    }
}
