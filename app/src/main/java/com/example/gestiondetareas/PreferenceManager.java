package com.example.gestiondetareas;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREFERENCE_NAME = "MyAppPreferences";
    private static final String KEY_USER_EMAIL = "userEmail";

    private static final String PREFS_NAME = "MyPrefs";
    private static final Boolean NOTIFICATION_ENABLED_KEY = false;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public void setNotificationEnabledKey(Boolean estadoNotificacion) {
        editor.putBoolean(String.valueOf(NOTIFICATION_ENABLED_KEY), estadoNotificacion);
        editor.apply();
    }

    public Boolean getNotificationEnabledKey() {
        return sharedPreferences.getBoolean(String.valueOf(NOTIFICATION_ENABLED_KEY), true);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}