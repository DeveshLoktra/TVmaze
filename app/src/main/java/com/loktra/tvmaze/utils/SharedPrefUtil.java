package com.loktra.tvmaze.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SharedPrefUtil {
    private static final String TAG = SharedPrefUtil.class.getSimpleName();
    private static SharedPreferences globalPrefs;

    // Shared Preference Initialization
    public static void init(@NonNull Context context) {
        SharedPrefUtil.globalPrefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    // Saving String Preferences
    public static void saveStringToPreferences(String key,
                                               String value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Getting String Preferences
    public static String getStringFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getString(key, null);
    }

    // Saving Float Preferences
    public static void saveFloatToPreferences(String key, float value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    // Getting Float Preferences
    public static float getFloatFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getFloat(key, 0f);
    }

    // Saving Integer Preferences
    public static void saveIntToPreferences(String key,
                                            int value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // Getting Integer Preferences
    public static int getIntFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getInt(key, 0);
    }

    // Saving Long Preferences
    public static void saveLongToPreferences(String key,
                                             long value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    // Getting Long Preferences
    public static long getLongFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getLong(key, 0);
    }

    // Saving Boolean Preferences
    public static void saveBooleanToPreferences(String key, boolean value) {
        SharedPreferences prefs = globalPrefs;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Getting Boolean Preferences
    public static boolean getBooleanFromPreferences(String key) {

        SharedPreferences prefs = globalPrefs;
        return prefs.getBoolean(key, false);
    }

    // Saving Objects Preferences
    public static void saveObjectToPrefs(@NonNull String key, Object value) {
        String toJson = GsonHelper.getInstance().toJson(value);
        SharedPrefUtil.saveStringToPreferences(key, toJson);
    }
}
