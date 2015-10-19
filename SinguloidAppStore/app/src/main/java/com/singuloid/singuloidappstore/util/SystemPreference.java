package com.singuloid.singuloidappstore.util;

import android.content.SharedPreferences;

/**
 * Created by scguo on 15/10/16.
 *
 * The SystemPreference of which extends from SharedPreference and here we using
 * her to persistent some temp data
 *
 */
public class SystemPreference implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mShaderPreference;

    private static SystemPreference sInstance;

    public static SystemPreference getInstance() {
        return sInstance == null ? new SystemPreference() : sInstance;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
