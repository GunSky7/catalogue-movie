package com.devgeek.cataloguemovie;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

/**
 * Created by GunSky7 on 6/7/2018.
 */

public class AppPreferences {
    SharedPreferences preferences;

    Context context;

    public AppPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setFavourite(String key, Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getFavourite(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean getContains(String key) {
        return  preferences.contains(key);
    }

    public void setUri(String key, Uri uri) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, uri.toString());
        editor.apply();
    }

    public String getUri(String key) {
        return preferences.getString(key, "uri_default_key");
    }

    public void setFirtTime(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getFirstTime(String key) {
        return preferences.getBoolean(key, true);
    }
}
