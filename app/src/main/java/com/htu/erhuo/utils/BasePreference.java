package com.htu.erhuo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description
 * Created by yzw on 2017/3/13.
 */

public class BasePreference {
    private SharedPreferences sp;

    public BasePreference(Context context) {
        String FILE_NAME = "user_info";
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    void setString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    String getString(String key) {
        return sp.getString(key, null);
    }

    void setBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    protected void setInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    protected int getInt(String key) {
        return sp.getInt(key, 0);
    }
}
