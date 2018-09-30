package com.hackathon.codechefapp.preferences;

/**
 * Created by SANDIP JANA on 10-09-2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.hackathon.codechefapp.constants.PreferenceConstants;

import java.util.HashSet;

public class SharedPreferenceUtils {
    private static SharedPreferenceUtils mSharedPreferenceUtils;
    protected Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    private SharedPreferenceUtils(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

    public static synchronized SharedPreferenceUtils getInstance(Context context) {

        if (mSharedPreferenceUtils == null) {
            mSharedPreferenceUtils = new SharedPreferenceUtils(context.getApplicationContext());
        }
        return mSharedPreferenceUtils;
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public void setValue(String key, String value) {
        mSharedPreferencesEditor.putString(key, value);
        mSharedPreferencesEditor.commit();
    }

    public void setValue(String key, int value) {
        mSharedPreferencesEditor.putInt(key, value);
        mSharedPreferencesEditor.commit();
    }

    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    public void setValue(String key, long value) {
        mSharedPreferencesEditor.putLong(key, value);
        mSharedPreferencesEditor.commit();
    }

    public void setValue(String key, boolean value) {
        mSharedPreferencesEditor.putBoolean(key, value);
        mSharedPreferencesEditor.commit();
    }

    public String getStringValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public int getIntValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public long getLongValue(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public boolean getBoolanValue(String keyFlag, boolean defaultValue) {
        return mSharedPreferences.getBoolean(keyFlag, defaultValue);
    }

    public HashSet<String> getCookies() {
        return (HashSet<String>) mSharedPreferences.getStringSet(PreferenceConstants.COOKIES, new HashSet<String>());
    }

    public boolean setCookies(HashSet<String> cookies) {
        return mSharedPreferencesEditor.putStringSet(PreferenceConstants.COOKIES, cookies).commit();
    }

    public void removeKey(String key) {
        if (mSharedPreferencesEditor != null) {
            mSharedPreferencesEditor.remove(key);
            mSharedPreferencesEditor.commit();
        }
    }


    public void clear() {
        mSharedPreferencesEditor.clear().commit();
    }

}
